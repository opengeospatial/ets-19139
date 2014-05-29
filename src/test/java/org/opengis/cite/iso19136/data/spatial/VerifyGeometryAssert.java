package org.opengis.cite.iso19136.data.spatial;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.geotoolkit.gml.xml.v321.CurveType;
import org.geotoolkit.xml.MarshallerPool;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.cite.iso19136.BasicFixture;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Verifies the behavior of the GeometryAssert class.
 */
public class VerifyGeometryAssert extends BasicFixture {

    private static Unmarshaller gmlUnmarshaller;

    @BeforeClass
    public static void initFixture() throws JAXBException {
        MarshallerPool pool = new MarshallerPool("org.geotoolkit.gml.xml.v321");
        gmlUnmarshaller = pool.acquireUnmarshaller();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void assertCurveCoveredByValidArea() throws JAXBException {
        URL url = this.getClass().getResource("/geom/Curve-LineString.xml");
        JAXBElement<CurveType> result = (JAXBElement<CurveType>) gmlUnmarshaller
                .unmarshal(url);
        CurveType curve = result.getValue();
        GeometryAssert.assertGeometryCoveredByValidArea(curve);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void assertCurveCoveredByValidAreaFails() throws JAXBException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("[@gml:id='Curve-2'] is not covered by valid area of CRS");
        URL url = this.getClass().getResource(
                "/geom/Curve-LineString-axisOrder.xml");
        JAXBElement<CurveType> result = (JAXBElement<CurveType>) gmlUnmarshaller
                .unmarshal(url);
        CurveType curve = result.getValue();
        GeometryAssert.assertGeometryCoveredByValidArea(curve);
    }

    @Test
    public void assertCurveIsConnected() throws SAXException, IOException {
        InputStream is = this.getClass().getResourceAsStream(
                "/geom/Curve-GeodesicString.xml");
        Document curve = docBuilder.parse(is);
        GeometryAssert.assertCurveSegmentsAreConnected(curve
                .getDocumentElement());
    }

    @Test
    public void assertCurveIsConnectedFails() throws SAXException, IOException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("gml:id='Curve-disconnected'");
        InputStream is = this.getClass().getResourceAsStream(
                "/geom/Curve-disconnected.xml");
        Document curve = docBuilder.parse(is);
        GeometryAssert.assertCurveSegmentsAreConnected(curve
                .getDocumentElement());
    }

    @Test
    public void assertCompositeCurveWithOrientableMemberIsConnected()
            throws JAXBException, SAXException, IOException {
        InputStream is = this.getClass().getResourceAsStream(
                "/geom/CompositeCurve.xml");
        Document curve = docBuilder.parse(is);
        GeometryAssert.assertCurveComponentsAreConnected(curve
                .getDocumentElement());
    }

    @Test
    public void assertTripartiteCurveIsConnected() throws SAXException,
            IOException {
        InputStream is = this.getClass().getResourceAsStream(
                "/geom/Curve-tripartite.xml");
        Document curve = docBuilder.parse(is);
        GeometryAssert.assertCurveSegmentsAreConnected(curve
                .getDocumentElement());
    }

    @Test
    public void assertValidSurfaceBoundary() throws SAXException, IOException {
        InputStream is = this.getClass().getResourceAsStream(
                "/geom/Surface-PolygonPatch-1.xml");
        Document surface = docBuilder.parse(is);
        GeometryAssert.assertValidSurfaceBoundary(surface.getDocumentElement());
    }

    @Test
    public void assertValidSurfaceBoundaryAsTripartiteCurve()
            throws SAXException, IOException {
        InputStream is = this.getClass().getResourceAsStream(
                "/geom/Surface-PolygonPatch-ExteriorCurve.xml");
        Document surface = docBuilder.parse(is);
        GeometryAssert.assertValidSurfaceBoundary(surface.getDocumentElement());
    }

    @Test
    public void validAIXMSurfaceBoundary() throws SAXException, IOException {
        InputStream is = this.getClass().getResourceAsStream(
                "/geom/AIXMSurface.xml");
        Document surface = docBuilder.parse(is);
        GeometryAssert.assertValidSurfaceBoundary(surface.getDocumentElement());
    }

    @Test
    public void invalidAIXMSurfaceBoundary() throws SAXException, IOException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("Interior boundary not covered by surface");
        InputStream is = this.getClass().getResourceAsStream(
                "/geom/AIXMSurface-InteriorCrossesExterior.xml");
        Document surface = docBuilder.parse(is);
        GeometryAssert.assertValidSurfaceBoundary(surface.getDocumentElement());
    }

    @Test
    public void assertValidSurfaceOrientation() throws SAXException,
            IOException {
        InputStream is = this.getClass().getResourceAsStream(
                "/geom/Surface-PolygonPatch-2.xml");
        Document surface = docBuilder.parse(is);
        GeometryAssert.assertValidSurfaceOrientation(surface
                .getDocumentElement());
    }

    @Test
    public void assertValidSurfaceOrientation_PolygonUTM() throws SAXException,
            IOException {
        InputStream is = this.getClass().getResourceAsStream(
                "/geom/Polygon-UTM.xml");
        Document surface = docBuilder.parse(is);
        GeometryAssert.assertValidSurfaceOrientation(surface
                .getDocumentElement());
    }

    @Test
    public void assertValidSurfaceOrientation_exteriorHasCWOrientation()
            throws SAXException, IOException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("Exterior boundary of surface");
        thrown.expectMessage("is not oriented CCW");
        InputStream is = this.getClass().getResourceAsStream(
                "/geom/Surface-ExteriorCW.xml");
        Document surface = docBuilder.parse(is);
        GeometryAssert.assertValidSurfaceOrientation(surface
                .getDocumentElement());
    }

    @Test
    public void assertValidSurfaceOrientation_interiorHasCCWOrientation()
            throws SAXException, IOException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("is not oriented CW");
        thrown.expectMessage("gml:interior[1]");
        InputStream is = this.getClass().getResourceAsStream(
                "/geom/Surface-InteriorCCW.xml");
        Document surface = docBuilder.parse(is);
        GeometryAssert.assertValidSurfaceOrientation(surface
                .getDocumentElement());
    }

    @Test
    public void validSurfaceOrientation_AIXMSurface() throws SAXException,
            IOException {
        InputStream is = this.getClass().getResourceAsStream(
                "/geom/AIXMSurface.xml");
        Document surface = docBuilder.parse(is);
        GeometryAssert.assertValidSurfaceOrientation(surface
                .getDocumentElement());
    }

    @Test
    public void invalidSurfaceOrientation_AIXMSurface() throws SAXException,
            IOException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("Interior boundary of surface with @gml:id='SFIRAMSWELL' is not oriented CW");
        InputStream is = this.getClass().getResourceAsStream(
                "/geom/AIXMSurface-InteriorCCW.xml");
        Document surface = docBuilder.parse(is);
        GeometryAssert.assertValidSurfaceOrientation(surface
                .getDocumentElement());
    }
}
