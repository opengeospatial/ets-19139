package org.opengis.cite.iso19136.general;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.cite.iso19136.util.VerifyXMLSchemaModelUtils;
import org.opengis.cite.validation.XSModelBuilder;
import org.opengis.cite.validation.XmlSchemaCompiler;
import org.xml.sax.SAXException;

/**
 * Verifies methods in the ModelAndSyntaxTests class.
 */
public class VerifyModelAndSyntaxTests {

    private static final String EXAMPLE_NS = "http://example.org/ns1";
    private static XmlSchemaCompiler xsdCompiler;

    public VerifyModelAndSyntaxTests() {
    }

    @BeforeClass
    public static void setUpClass() {
        URL schemaCatalog = VerifyXMLSchemaModelUtils.class
                .getResource("/schema-catalog.xml");
        xsdCompiler = new XmlSchemaCompiler(schemaCatalog);
    }

    @Test
    public void hasIDAttribute_GenericFeature() throws IOException,
            SAXException {
        URL url = this.getClass().getResource("/xsd/simple2.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        XSElementDeclaration elemDecl = model.getElementDeclaration(
                "GenericFeature", EXAMPLE_NS);
        ModelAndSyntaxTests iut = new ModelAndSyntaxTests();
        boolean result = iut.hasIDTypeAttribute(elemDecl);
        Assert.assertTrue("Expected to find ID attribute declared for: "
                + elemDecl.getName(), result);
    }

    @Test
    public void hasIDAttribute_LinearRing() throws IOException, SAXException {
        URL url = this.getClass().getResource("/xsd/simple2.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        XSElementDeclaration elemDecl = model.getElementDeclaration(
                "LinearRing", GML32.NS_NAME);
        ModelAndSyntaxTests iut = new ModelAndSyntaxTests();
        boolean result = iut.hasIDTypeAttribute(elemDecl);
        Assert.assertFalse(
                "Expected no ID attribute declared for: " + elemDecl.getName(),
                result);
    }

    XSModel createXSModel(URL schemaUrl, URI targetNamespace)
            throws IOException, SAXException {
        Schema xsd = xsdCompiler.compileXmlSchema(new StreamSource(schemaUrl
                .openStream(), schemaUrl.toString()));
        XSModel model = XSModelBuilder.buildXMLSchemaModel(xsd,
                targetNamespace.toString());
        return model;
    }
}
