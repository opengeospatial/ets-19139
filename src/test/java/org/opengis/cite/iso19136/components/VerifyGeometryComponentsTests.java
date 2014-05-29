package org.opengis.cite.iso19136.components;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.xml.validation.Schema;

import org.apache.xerces.xs.XSModel;
//import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.opengis.cite.validation.XSModelBuilder;
import org.opengis.cite.validation.XmlSchemaCompiler;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.xml.sax.SAXException;

/**
 * Verifies the behavior of the GeometryComponentTests class.
 */
public class VerifyGeometryComponentsTests {

    private static final String CITIES_NS = "http://www.interactive-instruments.de/namespaces/demo/cities/2.0/cities";
    private static ITestContext testContext;
    private static ISuite suite;

    public VerifyGeometryComponentsTests() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        testContext = mock(ITestContext.class);
        suite = mock(ISuite.class);
        when(testContext.getSuite()).thenReturn(suite);
    }

    @Test
    public void findLocalGeomProperties() throws URISyntaxException,
            SAXException, IOException {
        URL url = this.getClass().getResource("/xsd/cities.xsd");
        XSModel model = createXSModel(url, URI.create(CITIES_NS));
        GeometryComponentTests iut = new GeometryComponentTests();
        iut.setSchemaModel(model);
        iut.hasGeometryComponents(testContext);
    }

    XSModel createXSModel(URL schemaUrl, URI targetNamespace)
            throws URISyntaxException, SAXException, IOException {
        Set<URI> uriSet = new HashSet<URI>();
        uriSet.add(schemaUrl.toURI());
        URL entityCatalog = getClass().getResource(
                "/org/opengis/cite/iso19136/schema-catalog.xml");
        XmlSchemaCompiler xsdCompiler = new XmlSchemaCompiler(entityCatalog);
        Schema xsd = xsdCompiler.compileXmlSchema(uriSet.toArray(new URI[uriSet
                .size()]));
        XSModel model = XSModelBuilder.buildXMLSchemaModel(xsd,
                targetNamespace.toString());
        return model;
    }
}
