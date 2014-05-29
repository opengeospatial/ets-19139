package org.opengis.cite.iso19136.data.spatial;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.xpath.XPathExpressionException;

import org.geotoolkit.geometry.Envelopes;
import org.geotoolkit.gml.xml.AbstractRing;
import org.geotoolkit.gml.xml.v321.AbstractCurveSegmentType;
import org.geotoolkit.gml.xml.v321.AbstractCurveType;
import org.geotoolkit.gml.xml.v321.AbstractGeometryType;
import org.geotoolkit.gml.xml.v321.AbstractSurfaceType;
import org.geotoolkit.gml.xml.v321.CurveType;
import org.geotoolkit.referencing.CRS;
import org.geotoolkit.xml.MarshallerPool;
import org.opengis.cite.geomatics.Extents;
import org.opengis.cite.geomatics.GeodesyUtils;
import org.opengis.cite.geomatics.gml.CurveCoordinateListFactory;
import org.opengis.cite.geomatics.gml.CurveSegmentType;
import org.opengis.cite.geomatics.gml.GmlUtils;
import org.opengis.cite.geomatics.gml.SurfaceCoordinateListFactory;
import org.opengis.cite.iso19136.ErrorMessage;
import org.opengis.cite.iso19136.ErrorMessageKeys;
import org.opengis.cite.iso19136.general.GML32;
import org.opengis.cite.iso19136.util.TestSuiteLogger;
import org.opengis.cite.iso19136.util.XMLUtils;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.FactoryException;
import org.testng.Assert;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Provides specialized assertion methods that apply to representations of
 * geometry objects.
 */
public class GeometryAssert {

    private static final Logger LOGR = Logger.getLogger(GeometryAssert.class
            .getPackage().getName());
    private static final Unmarshaller GML_UNMARSHALLER = initGmlUnmarshaller();

    private static Unmarshaller initGmlUnmarshaller() {
        Unmarshaller unmarshaller = null;
        try {
            MarshallerPool pool = new MarshallerPool(
                    "org.geotoolkit.gml.xml.v321");
            unmarshaller = pool.acquireUnmarshaller();
        } catch (JAXBException je) {
            throw new RuntimeException(je);
        }
        return unmarshaller;
    }

    private GeometryAssert() {
    }

    /**
     * Asserts that the given geometry element has a valid CRS reference. The
     * value of the srsName attribute is an absolute URI that either identifies
     * a "well-known" CRS definition or refers to a CRS definition. The
     * reference may be inherited from a containing geometry (aggregate) or
     * feature envelope.
     * 
     * @param geom
     *            A DOM Element node representing a GML geometry element (or an
     *            element that can be substituted for one).
     * 
     * @see "ISO 19136: cl. 9.10, 10.1.3.2"
     */
    public static void assertValidCRS(Element geom) {
        String srsName = GmlUtils.findCRSReference(geom);
        Assert.assertFalse(
                srsName.isEmpty(),
                String.format("%s[@gml:id='%s'] has no associated CRS.",
                        geom.getLocalName(),
                        geom.getAttributeNS(GML32.NS_NAME, "id")));
    }

    /**
     * Asserts that the given geometry element is covered by the valid area of
     * its associated CRS.
     * 
     * @param gmlGeom
     *            A GML geometry element.
     */
    public static void assertGeometryCoveredByValidArea(
            AbstractGeometryType gmlGeom) {
        Envelope crsDomain = Envelopes.getDomainOfValidity(gmlGeom
                .getCoordinateReferenceSystem());
        Polygon validArea = Extents.envelopeAsPolygon(crsDomain);
        Geometry geom = GmlUtils.computeConvexHull(gmlGeom);
        if (geom.getClass().equals(GeometryCollection.class)) {
            return; // ignore unsupported geometry
        }
        Assert.assertTrue(validArea.covers(geom), String.format(
                "%s[@gml:id='%s'] is not covered by valid area of CRS.",
                gmlGeom.getClass().getName(), gmlGeom.getId()));
    }

    /**
     * Asserts that the number of direct positions in the posList element
     * appearing within each segment of the given curve geometry satisfies the
     * minimum length requirements.
     * 
     * The number of entries in a posList element is equal to the product of the
     * dimensionality of the CRS and the number of direct positions. An
     * additional check ensures that the number of entries is consistent with
     * the CRS even if the minimum length constraint is satisfied; that is,
     * <code>size(posList) % dimCRS = 0</code>.
     * 
     * @param gmlCurve
     *            An Element node representing a GML curve geometry.
     */
    public static void assertAllCurveSegmentsHaveRequiredLength(Element gmlCurve) {
        NodeList segments;
        try {
            segments = XMLUtils.evaluateXPath(gmlCurve,
                    "gml:segments/*[gml:posList]", null);
        } catch (XPathExpressionException xpe) {
            throw new RuntimeException(xpe);
        }
        if (segments.getLength() == 0)
            return;
        int crsDim = 2;
        String srsName = gmlCurve.getAttribute("srsName");
        try {
            CoordinateReferenceSystem crs = CRS.decode(srsName);
            crsDim = crs.getCoordinateSystem().getDimension();
        } catch (FactoryException fe) {
            TestSuiteLogger.log(Level.WARNING, srsName, fe);
        }
        for (int i = 0; i < segments.getLength(); i++) {
            Element segment = (Element) segments.item(i);
            String segmentType = segment.getLocalName();
            int minLength = GmlUtils.minCurveSegmentLength(segmentType);
            Element posList = (Element) segment.getElementsByTagNameNS(
                    GML32.NS_NAME, "posList").item(0);
            String[] values = posList.getTextContent().trim().split("\\s+");
            Assert.assertTrue(
                    values.length >= (crsDim * minLength),
                    String.format(
                            "gml:posList[%d] in %s[@gml:id='%s'] has fewer than %d values.",
                            i + 1, gmlCurve.getLocalName(),
                            gmlCurve.getAttributeNS(GML32.NS_NAME, "id"),
                            crsDim * minLength));
            Assert.assertTrue(
                    values.length % crsDim == 0,
                    String.format(
                            "gml:posList[%d] in %s[@gml:id='%s'] is not consistent with a %dD CRS.",
                            i + 1, gmlCurve.getLocalName(),
                            gmlCurve.getAttributeNS(GML32.NS_NAME, "id"),
                            crsDim));
        }
    }

    /**
     * Asserts that all segments of a curve are connected; that is, the end
     * point of each segment (except the last) is identical to the start point
     * of the next segment.
     * 
     * @param gmlCurve
     *            An Element node representing a gml:Curve element.
     */
    @SuppressWarnings("unchecked")
    public static void assertCurveSegmentsAreConnected(Element gmlCurve) {
        NodeList curveSegments;
        try {
            curveSegments = XMLUtils.evaluateXPath(gmlCurve, "gml:segments/*",
                    null);
        } catch (XPathExpressionException xpe) {
            throw new RuntimeException(xpe);
        }
        if (curveSegments.getLength() < 2)
            return;
        GmlUtils.findCRSReference(gmlCurve);
        CurveType curve;
        try {
            JAXBElement<CurveType> result = (JAXBElement<CurveType>) GML_UNMARSHALLER
                    .unmarshal(gmlCurve);
            curve = result.getValue();
        } catch (JAXBException je) {
            throw new RuntimeException(je);
        }
        Coordinate firstPoint = null;
        Coordinate lastPoint = null;
        List<? extends AbstractCurveSegmentType> segmentList = curve
                .getSegments().getAbstractCurveSegment();
        for (int i = 0; i < segmentList.size(); i++) {
            AbstractCurveSegmentType segment = segmentList.get(i);
            CurveSegmentType segmentType = CurveCoordinateListFactory.segmentTypeMap
                    .get(segment.getClass().getName());
            List<Coordinate> coordList = segmentType.getCoordinateList(segment,
                    curve.getCoordinateReferenceSystem());
            firstPoint = coordList.get(0);
            if (i > 0) {
                assertCoordinateEquals(
                        firstPoint,
                        lastPoint,
                        2,
                        String.format(
                                "In Curve with @gml:id='%s', segments %d and %d are not connected.",
                                gmlCurve.getAttributeNS(GML32.NS_NAME, "id"),
                                i, i + 1));
            }
            lastPoint = coordList.get(coordList.size() - 1);
        }
    }

    /**
     * Asserts that the components of a composite curve are connected; that is,
     * the orientation of the curve members is such that each component (except
     * the first) begins where the preceding one ends.
     * 
     * @param gmlCurve
     *            An Element node representing a gml:CompositeCurve element.
     */
    @SuppressWarnings("unchecked")
    public static void assertCurveComponentsAreConnected(Element gmlCurve) {
        NodeList curveMembers;
        try {
            curveMembers = XMLUtils.evaluateXPath(gmlCurve,
                    "gml:curveMember/*", null);
        } catch (XPathExpressionException xpe) {
            throw new RuntimeException(xpe);
        }
        if (curveMembers.getLength() < 2)
            return;
        CurveCoordinateListFactory coordFactory = new CurveCoordinateListFactory();
        Coordinate firstPoint = null;
        Coordinate lastPoint = null;
        for (int i = 0; i < curveMembers.getLength(); i++) {
            Element curveElem = (Element) curveMembers.item(i);
            GmlUtils.findCRSReference(curveElem);
            JAXBElement<AbstractCurveType> curveType;
            try {
                curveType = (JAXBElement<AbstractCurveType>) GML_UNMARSHALLER
                        .unmarshal(curveElem);
            } catch (JAXBException je) {
                throw new RuntimeException(je);
            }
            List<Coordinate> coordList = coordFactory
                    .createCoordinateList(curveType.getValue());
            boolean negOrientation = curveElem.getAttribute("orientation")
                    .equals(GML32.ORIENT_NEG);
            if (negOrientation) {
                firstPoint = coordList.get(coordList.size() - 1);
            } else {
                firstPoint = coordList.get(0);
            }
            if (i > 1) {
                assertCoordinateEquals(
                        firstPoint,
                        lastPoint,
                        2,
                        String.format(
                                "In CompositeCurve with @gml:id='%s', curve members %d and %d are not connected.",
                                gmlCurve.getAttributeNS(GML32.NS_NAME, "id"),
                                i, i + 1));
            }
            if (negOrientation) {
                lastPoint = coordList.get(0);
            } else {
                lastPoint = coordList.get(coordList.size() - 1);
            }
        }
    }

    /**
     * Asserts that the given Coordinate tuples are equal within the specified
     * tolerance.
     * 
     * @param actualPos
     *            The actual position.
     * @param expectedPos
     *            The expected position.
     * @param tolerancePPM
     *            The maximum tolerable difference between tuple elements,
     *            expressed in parts per million (ppm).
     * @param message
     *            The assertion error message.
     */
    public static void assertCoordinateEquals(Coordinate actualPos,
            Coordinate expectedPos, int tolerancePPM, String message) {
        double tolerance = tolerancePPM * 1E-06;
        Assert.assertEquals(
                Math.abs((actualPos.x / expectedPos.x) - 1.0),
                0.0,
                tolerance,
                message
                        + String.format(
                                "\nFirst element of tuple is out of tolerance (%d ppm).",
                                tolerancePPM));
        Assert.assertEquals(
                Math.abs((actualPos.y / expectedPos.y) - 1.0),
                0.0,
                tolerance,
                message
                        + String.format(
                                "\nSecond element of tuple is out of tolerance (%d ppm).",
                                tolerancePPM));
    }

    /**
     * Asserts that the boundary of the given surface is topologically correct.
     * Each boundary component (curve) must be:
     * <ol>
     * <li><em>simple</em> (does not self-intersect at points other than
     * boundary points), and</li>
     * <li><em>closed</em> (forms a cycle).</li>
     * </ol>
     * 
     * Furthermore, each interior ring must be covered by the surface delimited
     * by the exterior boundary (the rings may touch at a tangent point).
     * 
     * Note: Surface patches based on parametric curves are not supported.
     * 
     * @param surfaceElem
     *            A DOM Element node representing a surface geometry
     *            (substitutes for gml:AbstractSurface).
     */
    public static void assertValidSurfaceBoundary(Element surfaceElem) {
        if (LOGR.isLoggable(Level.FINE)) {
            LOGR.log(
                    Level.FINE,
                    "Checking boundary of {0} with @gml:id=\"{1}\"",
                    new Object[] { surfaceElem.getNodeName(),
                            surfaceElem.getAttributeNS(GML32.NS_NAME, "id") });
        }
        List<Coordinate> extCoordList;
        SurfaceCoordinateListFactory coordFactory = new SurfaceCoordinateListFactory();
        AbstractSurfaceType surfaceType = null;
        try {
            @SuppressWarnings("unchecked")
            JAXBElement<AbstractSurfaceType> jaxbSurface = (JAXBElement<AbstractSurfaceType>) GML_UNMARSHALLER
                    .unmarshal(surfaceElem);
            surfaceType = jaxbSurface.getValue();
            extCoordList = coordFactory.createCoordinateList(surfaceType);
        } catch (JAXBException je) {
            // probably an application-defined extension
            extCoordList = coordFactory.createCoordinateList(surfaceElem);
        }
        GeodesyUtils.removeConsecutiveDuplicates(extCoordList, 1);
        GeometryFactory geomFactory = new GeometryFactory();
        LineString exteriorCurve = geomFactory.createLineString(extCoordList
                .toArray(new Coordinate[extCoordList.size()]));
        if (LOGR.isLoggable(Level.FINE)) {
            StringBuilder msg = new StringBuilder("Exterior boundary of ");
            msg.append(surfaceElem.getAttributeNS(GML32.NS_NAME, "id"));
            msg.append("\n").append(exteriorCurve.toString());
            LOGR.fine(msg.toString());
        }
        Assert.assertTrue(
                exteriorCurve.isSimple(),
                String.format(
                        "Exterior boundary of surface with @gml:id='%s' is not simple.",
                        surfaceElem.getAttributeNS(GML32.NS_NAME, "id")));
        Assert.assertTrue(
                exteriorCurve.isClosed(),
                String.format(
                        "Exterior boundary of surface with @gml:id='%s' is not closed.",
                        surfaceElem.getAttributeNS(GML32.NS_NAME, "id")));
        Polygon coveringPolygon = geomFactory.createPolygon(exteriorCurve
                .getCoordinates());
        Set<List<Coordinate>> interiorCoordSet = null;
        if (null != surfaceType) {
            interiorCoordSet = coordFactory.interiorCoordinatesSet(surfaceType);
        } else {
            // argument is probably an application-defined extension
            interiorCoordSet = coordFactory.interiorCoordinatesSet(surfaceElem);
        }
        for (List<Coordinate> ringCoords : interiorCoordSet) {
            LineString interiorCurve = geomFactory.createLineString(ringCoords
                    .toArray(new Coordinate[0]));
            Assert.assertTrue(
                    interiorCurve.isSimple(),
                    String.format(
                            "Interior boundary of surface with @gml:id='%s' is not simple. Starting position: %s.",
                            surfaceElem.getAttributeNS(GML32.NS_NAME, "id"),
                            interiorCurve.getCoordinateN(0)));
            Assert.assertTrue(
                    interiorCurve.isClosed(),
                    String.format(
                            "Interior boundary of surface with @gml:id='%s' is not closed. Starting position: %s.",
                            surfaceElem.getAttributeNS(GML32.NS_NAME, "id"),
                            interiorCurve.getCoordinateN(0)));
            Assert.assertTrue(
                    interiorCurve.coveredBy(coveringPolygon),
                    String.format(
                            "Interior boundary not covered by surface with @gml:id='%s'. Starting position: %s.",
                            surfaceElem.getAttributeNS(GML32.NS_NAME, "id"),
                            interiorCurve.getCoordinateN(0)));
        }
    }

    /**
     * Asserts that the boundary of the given surface is correctly oriented with
     * respect to the upward normal. In essence, the interior of the surface
     * (patch) is always to the left of a boundary curve. The following
     * constraints must be satisfied:
     * <ol>
     * <li>the exterior ring is oriented in a counter-clockwise (CCW) direction,
     * and</li>
     * <li>all interior rings are oriented in a clockwise (CW) direction.</li>
     * </ol>
     * 
     * @param surfaceElem
     *            A DOM Element node representing a surface geometry
     *            (substitutes for gml:AbstractSurface).
     */
    @SuppressWarnings("unchecked")
    static void assertValidSurfaceOrientation(Element surfaceElem) {
        AbstractRing gmlRing = null;
        NodeList exteriorProps = surfaceElem.getElementsByTagNameNS(
                GML32.NS_NAME, "exterior");
        for (int i = 0; i < exteriorProps.getLength(); i++) {
            Element extRingElem = (Element) XMLUtils
                    .getPropertyValue(exteriorProps.item(i));
            try {
                JAXBElement<AbstractRing> jaxbRing = (JAXBElement<AbstractRing>) GML_UNMARSHALLER
                        .unmarshal(extRingElem);
                gmlRing = jaxbRing.getValue();
                gmlRing.setSrsName(surfaceElem.getAttribute(GML32.SRS));
            } catch (JAXBException je) {
                throw new RuntimeException(je);
            }
            // JTS algorithm assumes right-handed coordinates (e.g. lon,lat)
            Coordinate[] exteriorCoords = GeodesyUtils
                    .transformRingToRightHandedCS(gmlRing);
            Assert.assertTrue(CGAlgorithms.isCCW(exteriorCoords), ErrorMessage
                    .format(ErrorMessageKeys.EXT_BOUNDARY_ORIENT,
                            surfaceElem.getAttributeNS(GML32.NS_NAME, "id")));
        }
        NodeList interiorProps = surfaceElem.getElementsByTagNameNS(
                GML32.NS_NAME, "interior");
        for (int j = 0; j < interiorProps.getLength(); j++) {
            Element intRingElem = (Element) XMLUtils
                    .getPropertyValue(interiorProps.item(j));
            try {
                JAXBElement<AbstractRing> jaxbRing = (JAXBElement<AbstractRing>) GML_UNMARSHALLER
                        .unmarshal(intRingElem);
                gmlRing = jaxbRing.getValue();
                gmlRing.setSrsName(surfaceElem.getAttribute(GML32.SRS));
            } catch (JAXBException je) {
                throw new RuntimeException(je);
            }
            Coordinate[] interiorCoords = GeodesyUtils
                    .transformRingToRightHandedCS(gmlRing);
            Assert.assertFalse(CGAlgorithms.isCCW(interiorCoords), ErrorMessage
                    .format(ErrorMessageKeys.INT_BOUNDARY_ORIENT,
                            surfaceElem.getAttributeNS(GML32.NS_NAME, "id"),
                            j + 1));
        }
    }
}
