package org.opengis.cite.iso19136.data.spatial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.xerces.xs.XSElementDeclaration;
import org.geotoolkit.gml.xml.v321.CurveType;
import org.geotoolkit.xml.MarshallerPool;
import org.opengis.cite.geomatics.gml.GmlUtils;
import org.opengis.cite.iso19136.data.DataFixture;
import org.opengis.cite.iso19136.general.GML32;
import org.opengis.cite.iso19136.util.TestSuiteLogger;
import org.opengis.cite.iso19136.util.XMLSchemaModelUtils;
import org.opengis.cite.iso19136.util.XMLUtils;
import org.opengis.util.FactoryException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Validates the content of a gml:Curve element (or any element in its
 * substitution group), which implements the GM_Curve class from ISO 19107.
 * <p>
 * A curve is composed of one or more curve segments, each of which may be
 * defined using a different interpolation method. The curve segments are
 * connected to one another, with the end point of each segment (except the
 * last) being the start point of the next segment.
 * </p>
 * 
 * <h6 style="margin-bottom: 0.5em">Sources</h6>
 * <ul>
 * <li>ISO 19136:2007, cl. 10.4.5: CurveType, Curve</li>
 * <li>ISO 19107:2003, cl. 6.3.16: GM_Curve</li>
 * </ul>
 */
public class CurveTests extends DataFixture {

    NodeList curveNodes;
    List<QName> curveElems = new ArrayList<QName>();

    /**
     * A configuration method ({@code BeforeClass}) that looks for gml:Curve
     * elements in the GML document under test. If none are found all test
     * methods defined in the class will be skipped.
     */
    @BeforeClass(alwaysRun = true)
    public void findCurves() {
        Source data = new StreamSource(this.dataFile);
        this.curveElems.add(new QName(GML32.NS_NAME, GML32.CURVE));
        if (null != this.model) {
            XSElementDeclaration gmlCurve = this.model.getElementDeclaration(
                    GML32.CURVE, GML32.NS_NAME);
            List<XSElementDeclaration> curveDecls = XMLSchemaModelUtils
                    .getElementsByAffiliation(this.model, gmlCurve);
            for (XSElementDeclaration decl : curveDecls) {
                this.curveElems.add(new QName(decl.getNamespace(), decl
                        .getName()));
            }
        }
        Map<String, String> namespaceBindings = new HashMap<String, String>();
        String xpath = generateXPathExpression(this.curveElems,
                namespaceBindings);
        try {
            this.curveNodes = (NodeList) XMLUtils.evaluateXPath(data, xpath,
                    namespaceBindings, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) { // won't happen
            throw new RuntimeException(xpe);
        }
        Assert.assertFalse(this.curveNodes.getLength() == 0,
                "gml:Curve elements not found.");
    }

    /**
     * [{@code Test}] Verifies that a gml:Curve element has a valid CRS
     * reference.
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136, cl. 9.10, 10.1.3.2</li>
     * <li>ISO 19107, cl. 6.2.2.17 (Coordinate Reference System association)</li>
     * </ul>
     */
    @Test(description = "urn:iso:std:iso:19136:clause:9.10,10.1.3.2")
    public void curveHasValidCRS() {
        for (int i = 0; i < this.curveNodes.getLength(); i++) {
            Element geom = (Element) this.curveNodes.item(i);
            GeometryAssert.assertValidCRS(geom);
        }
    }

    /**
     * [{@code Test}] Verifies the segments of a gml:Curve element. All of the
     * following constraints must be satisfied:
     * <ol>
     * <li>the curve has one or more segments;</li>
     * <li>the segments are connected;</li>
     * <li>the segments are all located within the valid area of the CRS.</li>
     * </ol>
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136: 10.4.5 (CurveType, Curve)</li>
     * </ul>
     */
    @Test(description = "urn:iso:std:iso:19136:clause:10.4.5")
    public void validCurveSegments() throws DOMException, FactoryException {
        Unmarshaller gmlUnmarshaller;
        try {
            MarshallerPool pool = new MarshallerPool(
                    "org.geotoolkit.gml.xml.v321");
            gmlUnmarshaller = pool.acquireUnmarshaller();
        } catch (JAXBException jxe) {
            throw new RuntimeException(jxe);
        }
        for (int i = 0; i < this.curveNodes.getLength(); i++) {
            Element curveElem = (Element) this.curveNodes.item(i);
            GmlUtils.findCRSReference(curveElem);
            GeometryAssert.assertAllCurveSegmentsHaveRequiredLength(curveElem);
            GeometryAssert.assertCurveSegmentsAreConnected(curveElem);
            CurveType curve;
            try {
                JAXBElement<CurveType> result = gmlUnmarshaller.unmarshal(
                        curveElem, CurveType.class);
                curve = result.getValue();
            } catch (JAXBException e) {
                TestSuiteLogger.log(Level.WARNING,
                        "Failed to unmarshal curve geometry.", e);
                continue;
            }
            int nSegments = curve.getSegments().getJbAbstractCurveSegment()
                    .size();
            Assert.assertFalse(nSegments == 0, String.format(
                    "%s[@gml:id='%s'] has no curve segments.",
                    curveElem.getLocalName(),
                    curveElem.getAttributeNS(GML32.NS_NAME, "id")));
            GeometryAssert.assertGeometryCoveredByValidArea(curve);
        }
    }
}
