package org.opengis.cite.iso19136.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSModel;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.cite.validation.XSModelBuilder;
import org.opengis.cite.validation.XmlSchemaCompiler;
import org.xml.sax.SAXException;

/**
 * Verifies the behavior of the FeatureTypeFilter class.
 */
public class VerifyFeatureTypeFilter {

    private static final String APP_NS = "http://www.deegree.org/app";
    private static XmlSchemaCompiler xsdCompiler;

    public VerifyFeatureTypeFilter() {
    }

    @BeforeClass
    public static void initSchemaCompiler() {
        URL schemaCatalog = VerifyXMLSchemaModelUtils.class
                .getResource("/schema-catalog.xml");
        xsdCompiler = new XmlSchemaCompiler(schemaCatalog);
    }

    @Test
    public void filterAnonFeatureType() throws SAXException, IOException {
        URL url = this.getClass().getResource("/xsd/autos-anon.xsd");
        Schema xsd = xsdCompiler.compileXmlSchema(new StreamSource(url
                .openStream(), url.toString()));
        XSModel model = XSModelBuilder.buildXMLSchemaModel(xsd, APP_NS);
        Set<XSComplexTypeDefinition> complexTypes = XMLSchemaModelUtils
                .getReferencedComplexTypeDefinitions(model);
        FeatureTypeFilter iut = new FeatureTypeFilter();
        iut.filterSet(complexTypes);
        assertEquals("Unexpected number of feature types remain in set.", 1,
                complexTypes.size());
        assertTrue("Expected anonymous type definition.", complexTypes
                .iterator().next().getAnonymous());
    }

    @Test
    public void filterGlobalFeatureType() throws SAXException, IOException {
        URL url = this.getClass().getResource("/xsd/autos.xsd");
        Schema xsd = xsdCompiler.compileXmlSchema(new StreamSource(url
                .openStream(), url.toString()));
        XSModel model = XSModelBuilder.buildXMLSchemaModel(xsd, APP_NS);
        Set<XSComplexTypeDefinition> complexTypes = XMLSchemaModelUtils
                .getReferencedComplexTypeDefinitions(model);
        FeatureTypeFilter iut = new FeatureTypeFilter();
        iut.filterSet(complexTypes);
        assertEquals("Unexpected number of feature types remain in set.", 1,
                complexTypes.size());
    }
}
