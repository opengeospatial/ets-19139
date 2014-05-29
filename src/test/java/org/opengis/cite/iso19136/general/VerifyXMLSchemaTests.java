package org.opengis.cite.iso19136.general;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.xml.validation.Schema;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Matchers;
import org.opengis.cite.iso19136.SuiteAttribute;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.xml.sax.SAXException;

/**
 * Verifies the behavior of the XMLSchemaTests test class. Test stubs replace
 * fixture constituents where appropriate.
 */
public class VerifyXMLSchemaTests {

    private static ITestContext testContext;
    private ISuite suite;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public VerifyXMLSchemaTests() {
    }

    @BeforeClass
    public static void initTestContext() {
        testContext = mock(ITestContext.class);
    }

    @Before
    public void initSuite() {
        this.suite = mock(ISuite.class);
        when(testContext.getSuite()).thenReturn(suite);
    }

    @Test(expected = NullPointerException.class)
    public void supplyNullSchema() throws SAXException, IOException {
        XMLSchemaTests iut = new XMLSchemaTests();
        iut.compileXMLSchema(testContext);
    }

    @Test
    public void validSchemaViaTestContext() throws SAXException, IOException,
            URISyntaxException {
        URL url = this.getClass().getResource("/xsd/alpha.xsd");
        Set<URI> uriSet = new HashSet<URI>();
        uriSet.add(url.toURI());
        when(suite.getAttribute(SuiteAttribute.SCHEMA_LOC_SET.getName()))
                .thenReturn(uriSet);
        XMLSchemaTests iut = new XMLSchemaTests();
        iut.getSchemaURIsFromTestContext(testContext);
        iut.compileXMLSchema(testContext);
        verify(suite).setAttribute(
                Matchers.eq(SuiteAttribute.SCHEMA.getName()),
                Matchers.isA(Schema.class));
    }

    @Test
    public void compileInvalidSchemaShouldFail() throws SAXException,
            IOException, URISyntaxException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("Cannot resolve the name 'tns:DeltaType'");
        URL url = this.getClass().getResource("/xsd/gamma-MissingTypeDef.xsd");
        Set<URI> uriSet = new HashSet<URI>();
        uriSet.add(url.toURI());
        XMLSchemaTests iut = new XMLSchemaTests();
        iut.setSchemaLocations(uriSet);
        iut.compileXMLSchema(testContext);
    }

    @Test
    public void compileSchemaWithNonASCIIElements() throws SAXException,
            IOException, URISyntaxException {
        URL url = this.getClass().getResource("/xsd/cite-gmlsf0.xsd");
        Set<URI> uriSet = new HashSet<URI>();
        uriSet.add(url.toURI());
        when(suite.getAttribute(SuiteAttribute.SCHEMA_LOC_SET.getName()))
                .thenReturn(uriSet);
        XMLSchemaTests iut = new XMLSchemaTests();
        iut.getSchemaURIsFromTestContext(testContext);
        iut.compileXMLSchema(testContext);
        verify(suite).setAttribute(
                Matchers.eq(SuiteAttribute.SCHEMA.getName()),
                Matchers.isA(Schema.class));
    }
}
