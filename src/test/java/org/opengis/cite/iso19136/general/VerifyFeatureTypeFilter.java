package org.opengis.cite.iso19136.general;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import junit.framework.Assert;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.cite.iso19136.util.FeatureTypeFilter;
import org.opengis.cite.validation.XSModelBuilder;
import org.opengis.cite.validation.XmlSchemaCompiler;

/**
 * Verifies the behavior of the FeatureTypeFilter class.
 */
@SuppressWarnings("unchecked")
public class VerifyFeatureTypeFilter {

    private static final String EXAMPLE_NS = "http://example.org/ns1";
    private static XSModel model;

    public VerifyFeatureTypeFilter() {
    }

    @BeforeClass
    public static void setUpFixture() throws Exception {
        URL schemaCatalog = VerifyFeatureTypeFilter.class
                .getResource("/schema-catalog.xml");
        XmlSchemaCompiler xsdCompiler = new XmlSchemaCompiler(schemaCatalog);
        URL url = VerifyFeatureTypeFilter.class.getResource("/xsd/simple.xsd");
        Schema xsd = xsdCompiler.compileXmlSchema(new StreamSource(url
                .openStream(), url.toString()));
        model = XSModelBuilder.buildXMLSchemaModel(xsd, EXAMPLE_NS);
    }

    @Test
    public void filterImmutableCollection() {
        XSNamedMap components = model.getComponentsByNamespace(
                XSConstants.TYPE_DEFINITION, EXAMPLE_NS);
        Assert.assertEquals("Unexpected number of type definitions", 4,
                components.getLength());
        FeatureTypeFilter iut = new FeatureTypeFilter();
        Map<QName, XSObject> types = iut.doFilter(components);
        Assert.assertEquals("Unexpected number of feature types", 2,
                types.size());
    }

    @Test
    public void filterMutableCollection() {
        XSNamedMap components = model.getComponentsByNamespace(
                XSConstants.TYPE_DEFINITION, EXAMPLE_NS);
        // Create new mutable Map from original collection
        Map<QName, XSObject> types = new HashMap<QName, XSObject>(components);
        FeatureTypeFilter iut = new FeatureTypeFilter();
        Map<QName, XSObject> featureTypes = iut.doFilter(types);
        Assert.assertEquals("Unexpected number of feature types", 2,
                featureTypes.size());
    }
}
