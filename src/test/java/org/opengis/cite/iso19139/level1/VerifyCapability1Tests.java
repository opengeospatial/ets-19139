package org.opengis.cite.iso19139.level1;

import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.opengis.cite.iso19139.SuiteAttribute;
import org.opengis.cite.iso19139.TestRunArg;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Verifies the behavior of the Capability1Tests test class. Test stubs replace
 * fixture constituents where appropriate.
 */
public class VerifyCapability1Tests {

    private static final String SUBJ = "testSubject";
    private static DocumentBuilder docBuilder;
    private static ITestContext testContext;
    private static ISuite suite;
     private static final String ETS_ROOT_PKG = "/org/opengis/cite/beta/";

    public VerifyCapability1Tests() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        testContext = mock(ITestContext.class);
        suite = mock(ISuite.class);
        when(testContext.getSuite()).thenReturn(suite);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        docBuilder = dbf.newDocumentBuilder();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test(expected = AssertionError.class)
    public void testIsEmpty() throws SAXException, IOException {
        Capability1Tests iut = new Capability1Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params1=null;
        testContext.getSuite().getXmlSuite().setParameters(null);
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        params.put(TestRunArg.IUT.toString(),"http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        params.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());

        iut.validateXMLAgainstXSD(testContext);
        
    }
    
//
//    @Test(expected = AssertionError.class)
//    public void testIsEmpty() {
//        Capability1Tests iut = new Capability1Tests();
//        iut.isEmpty();
//    }
//
//    @Test
//    public void testTrim() {
//        Capability1Tests iut = new Capability1Tests();
//        iut.trim();
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void supplyNullTestSubject() throws SAXException, IOException {
//        Capability1Tests iut = new Capability1Tests();
//        iut.docIsValidAtomFeed();
//    }
//
//    @Test
//    public void supplyValidAtomFeedViaTestContext() throws SAXException,
//            IOException {
//        Document doc = docBuilder.parse(this.getClass().getResourceAsStream(
//                "/atom-feed.xml"));
//        when(suite.getAttribute(SUBJ)).thenReturn(doc);
//        Capability1Tests iut = new Capability1Tests();
//        iut.obtainTestSubject(testContext);
//        iut.docIsValidAtomFeed();
//    }
}
