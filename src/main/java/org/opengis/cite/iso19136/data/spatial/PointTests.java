package org.opengis.cite.iso19136.data.spatial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.xerces.xs.XSElementDeclaration;
import org.geotoolkit.geometry.Envelopes;
import org.geotoolkit.geometry.GeneralDirectPosition;
import org.geotoolkit.geometry.ImmutableEnvelope;
import org.geotoolkit.referencing.CRS;
import org.opengis.cite.geomatics.gml.GmlUtils;
import org.opengis.cite.iso19136.data.DataFixture;
import org.opengis.cite.iso19136.general.GML32;
import org.opengis.cite.iso19136.util.TestSuiteLogger;
import org.opengis.cite.iso19136.util.XMLSchemaModelUtils;
import org.opengis.cite.iso19136.util.XMLUtils;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.FactoryException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Validates the content of a gml:Point element (or any element in its
 * substitution group), which implements the GM_Point class from ISO 19107. A
 * point is defined by a single coordinate tuple.
 * 
 * <h6 style="margin-bottom: 0.5em">Sources</h6>
 * <ul>
 * <li>ISO 19136:2007, cl. 10.3.1: PointType, Point</li>
 * <li>ISO 19107:2003, cl. 6.3.11: GM_Point</li>
 * </ul>
 */
public class PointTests extends DataFixture {

    NodeList points;
    List<QName> pointElems = new ArrayList<QName>();

    /**
     * A configuration method ({@code BeforeClass}) that looks for gml:Point
     * elements in the GML document under test. If none are found all test
     * methods defined in the class will be skipped.
     */
    @BeforeClass
    public void findPoints() {
        Source data = new StreamSource(this.dataFile);
        this.pointElems.add(new QName(GML32.NS_NAME, GML32.POINT));
        if (null != this.model) {
            XSElementDeclaration gmlPoint = this.model.getElementDeclaration(
                    GML32.POINT, GML32.NS_NAME);
            List<XSElementDeclaration> pointDecls = XMLSchemaModelUtils
                    .getElementsByAffiliation(this.model, gmlPoint);
            for (XSElementDeclaration decl : pointDecls) {
                this.pointElems.add(new QName(decl.getNamespace(), decl
                        .getName()));
            }
        }
        Map<String, String> namespaceBindings = new HashMap<String, String>();
        String xpath = generateXPathExpression(this.pointElems,
                namespaceBindings);
        try {
            this.points = (NodeList) XMLUtils.evaluateXPath(data, xpath,
                    namespaceBindings, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) { // won't happen
            throw new RuntimeException(xpe);
        }
        Assert.assertFalse(this.points.getLength() == 0,
                "gml:Point elements not found.");
    }

    /**
     * [{@code Test}] Verifies that a gml:Point element has a valid CRS
     * reference.
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136, cl. 9.10, 10.1.3.2</li>
     * <li>ISO 19107, cl. 6.2.2.17 (Coordinate Reference System association)</li>
     * </ul>
     */
    @Test(description = "urn:iso:std:iso:19136:clause:9.10,10.1.3.2")
    public void pointHasValidCRS() {
        for (int i = 0; i < this.points.getLength(); i++) {
            Element point = (Element) this.points.item(i);
            GeometryAssert.assertValidCRS(point);
        }
    }

    /**
     * [{@code Test}] Verifies the coordinates of a gml:Point. All of the
     * following constraints must be satisfied:
     * <ol>
     * <li>length of coordinate tuple = CRS dimension;</li>
     * <li>the point is located within the valid area of the CRS.</li>
     * </ol>
     * 
     * <p>
     * Note that the last constraint will detect obvious cases where the axis
     * order is incorrect (e.g. a position expressed in EPSG 4326 as -122.22,
     * 50.55).
     * </p>
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19107:2003, cl. 6.2.2.10 (coordinateDimension)</li>
     * </ul>
     */
    @Test(description = "urn:iso:std:iso:19107:clause:6.2.2.10")
    public void pointHasValidPosition() {
        for (int i = 0; i < this.points.getLength(); i++) {
            Element point = (Element) this.points.item(i);
            DirectPosition dpos = createDirectPosition(point);
            Assert.assertEquals(dpos.getDimension(), dpos
                    .getCoordinateReferenceSystem().getCoordinateSystem()
                    .getDimension(), String.format(
                    "%s[@gml:id='%s'] has unexpected coordinate dimension.",
                    point.getLocalName(),
                    point.getAttributeNS(GML32.NS_NAME, "id")));
            ImmutableEnvelope validArea = new ImmutableEnvelope(
                    Envelopes.getDomainOfValidity(dpos
                            .getCoordinateReferenceSystem()));
            Assert.assertTrue(validArea.contains(dpos), String.format(
                    "%s[@gml:id='%s'] is not within CRS area of use: %s.",
                    point.getLocalName(),
                    point.getAttributeNS(GML32.NS_NAME, "id"),
                    validArea.toString()));
        }
    }

    /**
     * Creates a DirectPosition object from a gml:Point element. It will have
     * the same CRS as the given point; the reference system may be inherited
     * from a containing geometry (aggregate) or feature.
     * 
     * @param point
     *            A gml:Point element (or an element in its substitution group).
     * @return A DirectPosition holding the coordinates of the original point.
     */
    DirectPosition createDirectPosition(Element point) {
        NodeList posList = point.getElementsByTagNameNS(GML32.NS_NAME, "pos");
        if (posList.getLength() != 1) {
            throw new IllegalArgumentException(
                    "Expected point geometry containing exactly 1 gml:pos element; received "
                            + point.getNodeName());
        }
        String srsName = GmlUtils.findCRSReference(point);
        CoordinateReferenceSystem crs = null;
        try {
            crs = CRS.decode(srsName);
        } catch (FactoryException fex) {
            TestSuiteLogger.log(Level.WARNING, String.format(
                    "Unknown srsName found in %s[@gml:id='%s']: %s",
                    point.getLocalName(),
                    point.getAttributeNS(GML32.NS_NAME, "id"), srsName), fex);
        }
        DirectPosition dpos = new GeneralDirectPosition(crs);
        Element posElem = (Element) posList.item(0);
        String[] coordTuple = posElem.getTextContent().trim().split("\\s+");
        for (int i = 0; i < coordTuple.length; i++) {
            double val = Double.parseDouble(coordTuple[i]);
            dpos.setOrdinate(i, val);
        }
        return dpos;
    }
}
