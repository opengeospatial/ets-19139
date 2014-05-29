package org.opengis.cite.iso19136.general;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;

import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.opengis.cite.iso19136.SuiteAttribute;
import org.opengis.cite.validation.XmlSchemaCompiler;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.xml.sax.SAXException;

/**
 * Verifies the behavior of the GeneralSchemaTests test class.
 */
public class VerifyGeneralSchemaTests {

    private static ITestContext testContext;
    private static ISuite suite;

    public VerifyGeneralSchemaTests() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        testContext = mock(ITestContext.class);
        suite = mock(ISuite.class);
        when(testContext.getSuite()).thenReturn(suite);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.newDocumentBuilder();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void getApplicationNamespaceName() throws URISyntaxException {
        Set<URI> schemaURIs = new HashSet<URI>();
        schemaURIs.add(this.getClass().getResource("/xsd/gml-3.2.1.xsd")
                .toURI());
        schemaURIs.add(this.getClass().getResource("/xsd/beta.xsd").toURI());
        GeneralSchemaTests iut = new GeneralSchemaTests();
        URI nsName = iut.getApplicationNamespaceName(schemaURIs);
        assertEquals("Unexpected namespace name.",
                "http://www.example.net/beta", nsName.toString());
    }

    @Test
    public void schemaWithAnonymousFeatureTypes() throws SAXException,
            IOException, URISyntaxException {
        URL url = this.getClass().getResource("/xsd/anon-types.xsd");
        Set<URI> uriSet = new HashSet<URI>();
        uriSet.add(url.toURI());
        when(suite.getAttribute(SuiteAttribute.SCHEMA_LOC_SET.getName()))
                .thenReturn(uriSet);
        Schema schema = compileSchema(uriSet);
        when(suite.getAttribute(SuiteAttribute.SCHEMA.getName())).thenReturn(
                schema);
        GeneralSchemaTests iut = new GeneralSchemaTests();
        iut.createSchemaModel(testContext);
        iut.declaresGMLObjects();
        assertEquals("Unexpected number of feature types found.", 3, iut
                .getSchemaInfo().getFeatureTypes().size());
    }

    private Schema compileSchema(Set<URI> xsdLocations) throws SAXException,
            IOException {
        URL entityCatalog = this.getClass().getResource(
                "/org/opengis/cite/iso19136/schema-catalog.xml");
        XmlSchemaCompiler xsdCompiler = new XmlSchemaCompiler(entityCatalog);
        return xsdCompiler.compileXmlSchema(xsdLocations
                .toArray(new URI[xsdLocations.size()]));
    }

}
