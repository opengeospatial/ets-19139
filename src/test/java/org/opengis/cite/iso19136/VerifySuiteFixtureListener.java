package org.opengis.cite.iso19136;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

public class VerifySuiteFixtureListener {

    private static XmlSuite xmlSuite;
    private static ISuite suite;

    public VerifySuiteFixtureListener() {
    }

    @BeforeClass
    public static void setUpClass() {
        xmlSuite = mock(XmlSuite.class);
        suite = mock(ISuite.class);
        when(suite.getXmlSuite()).thenReturn(xmlSuite);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test(expected = IllegalArgumentException.class)
    public void noSuiteParameters() {
        Map<String, String> params = new HashMap<String, String>();
        when(xmlSuite.getParameters()).thenReturn(params);
        SuiteFixtureListener iut = new SuiteFixtureListener();
        iut.onStart(suite);
    }

    @Test
    public void processSchemaParameter() throws URISyntaxException {
        URL url = this.getClass().getResource("/xsd/gamma.xsd");
        Map<String, String> params = new HashMap<String, String>();
        params.put(TestRunArg.XSD.toString(), url.toURI().toString());
        when(xmlSuite.getParameters()).thenReturn(params);
        SuiteFixtureListener iut = new SuiteFixtureListener();
        iut.onStart(suite);
        verify(suite).setAttribute(
                Matchers.eq(SuiteAttribute.SCHEMA_LOC_SET.getName()),
                Matchers.isA(Set.class));
    }

}
