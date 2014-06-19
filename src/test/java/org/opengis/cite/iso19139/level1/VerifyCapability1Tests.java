package org.opengis.cite.iso19139.level1;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
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
import org.testng.xml.XmlSuite;
import org.testng.ITestContext;
import org.xml.sax.SAXException;
import java.util.logging.Logger;

/**
 * Verifies the behavior of the Capability1Tests test class. Test stubs replace
 * fixture constituents where appropriate.
 */
public class VerifyCapability1Tests {

    static final Logger LOGGER = Logger.getLogger("MY Log");
    private static final String SUBJ = "testSubject";
    private static DocumentBuilder docBuilder;
    private static ITestContext testContext;
    private static ISuite suite;
    private static XmlSuite xmlSuite;
    private static final String ETS_ROOT_PKG = "/org/opengis/cite/iso19139/";
    private static Map<String, String> params = new TreeMap<String, String>();

    public VerifyCapability1Tests() {
    }

    /**
     * Declare mock environment for the Capability1Tests test class.
     *
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        testContext = mock(ITestContext.class);
        suite = mock(ISuite.class);
        xmlSuite = mock(org.testng.xml.XmlSuite.class);

        when(testContext.getSuite()).thenReturn(suite);
        when(testContext.getSuite().getXmlSuite()).thenReturn(xmlSuite);
        when(testContext.getSuite().getXmlSuite().getParameters()).thenReturn(params);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        docBuilder = dbf.newDocumentBuilder();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test for the validateXMLAgainstXSD when URL exists, points at a XML and
     * conforms to ISO 19139.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test
    public void testValidateXMLAgainstXSDWhenUrlIsValid() throws SAXException, IOException {
        LOGGER.info("[TS] : Name of Test Case: testValidateXMLAgainstXSDWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the validateXMLAgainstXSD when URL exists, points at a XML and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: XML validation =  PASS\n");
        Capability1Tests iut = new Capability1Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLAgainstXSD(testContext);
    }

    /**
     * Test for the validateXMLAgainstXSD when URL exists but does not points to
     * any XML.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test(expected = NullPointerException.class)
    public void testValidateXMLAgainstXSDWhenUrlIsNotValid() throws SAXException, IOException {
        LOGGER.info("[TS] : Name of Test Case: testValidateXMLAgainstXSDWhenUrlIsNotValid\n"
                + "      [TS] : Description: Test for the validateXMLAgainstXSD when URL exists but does not points to any XML.\n"
                + "      [TS] : Expected Result: XML validation =  FAIL, should throgh NullPointerException\n");
        Capability1Tests iut = new Capability1Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43B.xml");
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLAgainstXSD(testContext);
    }

    /**
     * Test for the validateXMLAgainstXSD when URL exists, points at a XML and
     * but does not conforms to ISO 19139.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test(expected = AssertionError.class)
    public void testValidateXMLAgainstXSDWhenUrlIsPresentButNotValid() throws SAXException, IOException {
        LOGGER.info("[TS] : testValidateXMLAgainstXSDWhenUrlIsPresentButNotValid\n"
                + "      [TS] : Description: Test for the validateXMLAgainstXSD when URL exists, points at a XML and but does not conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: XML validation =  FAIL\n");
        Capability1Tests iut = new Capability1Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://www.w3schools.com/dom/books.xml");
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLAgainstXSD(testContext);
    }

    /**
     * Test for the validateXMLAgainstXSD when Physical Path exists, points at a
     * XML and conforms to ISO 19139.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test
    public void testValidateXMLAgainstXSDWhenPhysicalPathIsPresent() throws SAXException, IOException {
        LOGGER.info("[TS] : Name of Test Case: testValidateXMLAgainstXSDWhenPhysicalPathIsPresent\n"
                + "      [TS] : Description: Test for the validateXMLAgainstXSD when Physical Path exists, points at a XML and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: XML validation =  PASS\n");
        Capability1Tests iut = new Capability1Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid.xml").toString());
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLAgainstXSD(testContext);
    }

    /**
     * Test for the validateXMLAgainstXSD when Physical Path exists, points at a
     * XML and but does not conforms to ISO 19139.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test(expected = AssertionError.class)
    public void testValidateXMLAgainstXSDWhenPhysicalPathIsPresentButNotValid() throws SAXException, IOException {
        LOGGER.info("[TS] : Name of Test Case: testValidateXMLAgainstXSDWhenPhysicalPathIsPresentButNotValid\n"
                + "      [TS] : Description: Test for the validateXMLAgainstXSD when Physical Path exists, points at a XML and but does not conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: XML validation =  FAIL\n");
        Capability1Tests iut = new Capability1Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-invalid.xml").toString());
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLAgainstXSD(testContext);
    }

    /**
     * Test for the validateXMLAgainstXSD when Physical Path exists but does not
     * points to any XML.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test(expected = NullPointerException.class)
    public void testValidateXMLAgainstXSDWhenPhysicalPathIsNotPresent() throws SAXException, IOException {
        LOGGER.info("[TS] : Name of Test Case: testValidateXMLAgainstXSDWhenPhysicalPathIsNotPresent\n"
                + "      [TS] : Description: Test for the validateXMLAgainstXSD when Physical Path exists but does not points to any XML.\n"
                + "      [TS] : Expected Result: XML validation =  FAIL, should throgh NullPointerException\n");
        Capability1Tests iut = new Capability1Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/book.xml").toString());
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLAgainstXSD(testContext);
    }
}
