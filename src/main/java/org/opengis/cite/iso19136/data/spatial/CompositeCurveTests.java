package org.opengis.cite.iso19136.data.spatial;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.opengis.cite.geomatics.gml.GmlUtils;
import org.opengis.cite.iso19136.data.DataFixture;
import org.opengis.cite.iso19136.general.GML32;
import org.opengis.cite.iso19136.util.XMLUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Validates the content of a gml:CompositeCurve element, which implements the
 * GM_CompositeCurve class from ISO 19107. A composite curve is a sequence of
 * orientable curves agreeing in orientation such that each curve (except the
 * first) begins where the previous one ends.
 * 
 * <h6 style="margin-bottom: 0.5em">Sources</h6>
 * <ul>
 * <li>ISO 19136:2007, cl. 11.2.2.2: CompositeCurveType, CompositeCurve</li>
 * <li>ISO 19107:2003, cl. 6.6.5: GM_CompositeCurve</li>
 * </ul>
 */
public class CompositeCurveTests extends DataFixture {

    NodeList curveNodes;
    List<QName> curveElems = new ArrayList<QName>();

    /**
     * A configuration method ({@code BeforeClass}) that looks for
     * gml:CompositeCurve elements in the GML document under test. If none are
     * found all test methods defined in the class will be skipped.
     */
    @BeforeClass(alwaysRun = true)
    public void findCompositeCurves() {
        Source data = new StreamSource(this.dataFile);
        this.curveElems.add(new QName(GML32.NS_NAME, GML32.COMP_CURVE));
        String xpath = "//gml:CompositeCurve";
        try {
            this.curveNodes = (NodeList) XMLUtils.evaluateXPath(data, xpath,
                    null, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) { // won't happen
            throw new RuntimeException(xpe);
        }
        Assert.assertFalse(this.curveNodes.getLength() == 0,
                "No gml:CompositeCurve elements were found.");
    }

    /**
     * [{@code Test}] Verifies that the component curves of a gml:CompositeCurve
     * element are connected.
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136, 11.2.2.2: CompositeCurveType, CompositeCurve</li>
     * </ul>
     */
    @Test(description = "urn:iso:std:iso:19136:clause:11.2.2.2")
    public void compositeCurveComponentsAreConnected() {
        for (int i = 0; i < this.curveNodes.getLength(); i++) {
            Element curveElem = (Element) this.curveNodes.item(i);
            GmlUtils.findCRSReference(curveElem);
            GeometryAssert.assertCurveComponentsAreConnected(curveElem);
        }
    }
}
