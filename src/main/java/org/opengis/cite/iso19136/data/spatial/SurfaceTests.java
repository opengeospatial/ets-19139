package org.opengis.cite.iso19136.data.spatial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.xerces.xs.XSElementDeclaration;
import org.opengis.cite.iso19136.data.DataFixture;
import org.opengis.cite.iso19136.general.GML32;
import org.opengis.cite.iso19136.util.XMLSchemaModelUtils;
import org.opengis.cite.iso19136.util.XMLUtils;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Validates the content of a gml:Surface element (or any element in its
 * substitution group), which implements the GM_Surface class from ISO 19107. A
 * surface is a 2-dimensional primitive composed of one or more surface patches.
 * <p>
 * A surface has an "up" direction in terms of the upward (positive) normal,
 * which is the side of the surface from which the exterior boundary appears
 * counterclockwise and interior boundaries are traversed in a clockwise manner
 * (see figure below). If the surface is the boundary of a solid, the "up"
 * direction is usually outward.
 * </p>
 * <p style="text-align: center">
 * <img src="./doc-files/surface.png" alt="Surface upNormal" />
 * </p>
 * 
 * <h6 style="margin-bottom: 0.5em">Sources</h6>
 * <ul>
 * <li>ISO 19136:2007, cl. 10.5.10 SurfaceType, Surface</li>
 * <li>ISO 19107:2003, cl. 6.3.17 GM_Surface</li>
 * </ul>
 */
public class SurfaceTests extends DataFixture {

    NodeList surfaceNodes;
    List<QName> surfaceElems = new ArrayList<QName>();

    /**
     * A configuration method ({@code BeforeClass}) that looks for gml:Surface
     * elements in the GML document under test (including any elements in its
     * substitution group). If none are found all test methods defined in the
     * class will be skipped.
     */
    @BeforeClass(alwaysRun = true)
    public void findSurfaces() {
        Source data = new StreamSource(this.dataFile);
        if (null != this.model) {
            XSElementDeclaration gmlSurface = this.model.getElementDeclaration(
                    GML32.ABSTRACT_SURFACE, GML32.NS_NAME);
            List<XSElementDeclaration> surfaceDecls = XMLSchemaModelUtils
                    .getElementsByAffiliation(this.model, gmlSurface);
            for (XSElementDeclaration decl : surfaceDecls) {
                this.surfaceElems.add(new QName(decl.getNamespace(), decl
                        .getName()));
            }
        }
        Map<String, String> namespaceBindings = new HashMap<String, String>();
        String xpath = generateXPathExpression(this.surfaceElems,
                namespaceBindings);
        try {
            this.surfaceNodes = (NodeList) XMLUtils.evaluateXPath(data, xpath,
                    namespaceBindings, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) { // won't happen
            throw new RuntimeException(xpe);
        }
        if (this.surfaceNodes.getLength() == 0) {
            throw new SkipException("No surface elements (that substitute for gml:Surface) were found.");
        }
    }

    /**
     * [{@code Test}] Verifies that a gml:Surface element has a valid CRS
     * reference.
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136, cl. 9.10, 10.1.3.2</li>
     * <li>ISO 19107, cl. 6.2.2.17 (Coordinate Reference System association)</li>
     * </ul>
     */
    @Test(description = "urn:iso:std:iso:19136:clause:9.10,10.1.3.2")
    public void surfaceHasValidCRS() {
        for (int i = 0; i < this.surfaceNodes.getLength(); i++) {
            Element geom = (Element) this.surfaceNodes.item(i);
            GeometryAssert.assertValidCRS(geom);
        }
    }

    /**
     * [{@code Test}] Checks that the boundary of a surface is topologically
     * correct. Each component of the surface boundary must be a simple closed
     * curve (ring); that is, it must not self-intersect and it forms a cycle
     * such that the end points are identical.
     * 
     * Furthermore, each interior ring must be covered by the surface delimited
     * by the exterior boundary (the rings may touch at a tangent point).
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136:2007, cl. 10.5.11.1: Ring, RingType, curveMember</li>
     * <li>ISO 19136:2007, cl. 10.5.5: exterior, interior</li>
     * <li>ISO 19107:2003, cl. 6.3.6: GM_Ring</li>
     * <li>ISO 19107:2003, cl. 6.3.7.2: exterior, interior</li>
     * </ul>
     */
    @Test(description = "urn:iso:std:iso:19136:clause:10.5.5,10.5.11.1")
    public void validSurfaceBoundary() {
        for (int i = 0; i < this.surfaceNodes.getLength(); i++) {
            Element surface = (Element) this.surfaceNodes.item(i);
            GeometryAssert.assertValidSurfaceBoundary(surface);
        }
    }

    /**
     * [{@code Test}] Checks that the orientation of the surface boundary is
     * consistent with the upward normal. In essence, the interior is always to
     * the left of a boundary curve. All patches must be oriented in the same
     * manner.
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136:2007, cl. 10.5.10: SurfaceType, Surface</li>
     * <li>ISO 19107:2003, cl. 6.3.17: GM_Surface</li>
     * <li>ISO 19107:2003, cl. 6.4.34: GM_SurfacePatch</li>
     * </ul>
     */
    @Test(description = "urn:iso:std:iso:19136:clause:10.5.10")
    public void validSurfaceOrientation() {
        for (int i = 0; i < this.surfaceNodes.getLength(); i++) {
            Element surface = (Element) this.surfaceNodes.item(i);
            GeometryAssert.assertValidSurfaceOrientation(surface);
        }
    }

}
