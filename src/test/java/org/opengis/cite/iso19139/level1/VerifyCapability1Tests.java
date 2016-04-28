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
    private static final String NOAA_ROOT = "/org/opengis/cite/www.ngdc.noaa.gov/";
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
     * Test for the validateXMLAgainstXSD when URL exists, points at a Xml and
     * conforms to ISO 19139.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test
    public void testValidateXmlAgainstXsdWhenUrlIsValidGMD() throws SAXException, IOException {
        LOGGER.info("[TS] : Name of Test Case: testValidateXmlAgainstXsdWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the validateXMLAgainstXSD when URL exists, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
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
     * Test for the validateXMLAgainstXSD when URL exists, points at a Xml and
     * conforms to ISO 19139.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test
    public void testValidateXmlAgainstXsdWhenUrlIsValidGMI() throws SAXException, IOException {
        LOGGER.info("[TS] : Name of Test Case: testValidateXmlAgainstXsdWhenUrlIsValidGMI\n"
                + "      [TS] : Description: Test for the validateXMLAgainstXSD when URL exists, points at a Xml and conforms to ISO 19139 using gmi:MI_Metadata root\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability1Tests iut = new Capability1Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/IEDA/10000_iso.xml");
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(NOAA_ROOT+  "metadata/published/xsd/ngdcSchema/schema.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLAgainstXSD(testContext);
    }
    /**
     * Test for the validateXMLAgainstXSD when URL exists, points at a Xml and
     * conforms to ISO 19139.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test
    public void testValidateXmlAgainstXsdWhenUrlIsValidDefaultXSDArgGMDFile() throws SAXException, IOException {
        LOGGER.info("[TS] : Name of Test Case: testValidateXmlAgainstXsdWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the validateXMLAgainstXSD when URL exists, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability1Tests iut = new Capability1Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
       // params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLAgainstXSD(testContext);
    }
    /**
     * Test for the validateXMLAgainstXSD when URL exists, points at a Xml and
     * conforms to ISO 19139.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test
    public void testValidateXmlAgainstXsdWhenUrlIsValidDefaultXSDArgGMIFile() throws SAXException, IOException {
        LOGGER.info("[TS] : Name of Test Case: testValidateXmlAgainstXsdWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the validateXMLAgainstXSD when URL exists, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability1Tests iut = new Capability1Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/IEDA/10000_iso.xml");
      //  params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLAgainstXSD(testContext);
    }
    /**
     * Test for the validateXMLAgainstXSD when URL exists but does not points to
     * any Xml.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test(expected = NullPointerException.class)
    public void testValidateXmlAgainstXsdWhenUrlIsNotValid() throws SAXException, IOException {
        LOGGER.info("[TS] : Name of Test Case: testValidateXmlAgainstXsdWhenUrlIsNotValid\n"
                + "      [TS] : Description: Test for the validateXMLAgainstXSD when URL exists but does not points to any Xml.\n"
                + "      [TS] : Expected Result: Xml validation =  FAIL, should throgh NullPointerException\n");
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
     * Test for the validateXMLAgainstXSD when URL exists, points at a Xml and
     * but does not conforms to ISO 19139.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    //@Test(expected = AssertionError.class)
    @Test(expected = NullPointerException.class)
    public void testValidateXmlAgainstXsdWhenUrlIsPresentButNotValid() throws SAXException, IOException {
        LOGGER.info("[TS] : testValidateXmlAgainstXsdWhenUrlIsPresentButNotValid\n"
                + "      [TS] : Description: Test for the validateXMLAgainstXSD when URL exists, points at a Xml and but does not conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  FAIL\n");
        Capability1Tests iut = new Capability1Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
      //  params1.put(TestRunArg.IUT.toString(), "http://www.w3schools.com/dom/books.xml");
        params1.put(TestRunArg.IUT.toString(), "http://www.w3schools.com/dom/books");
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLAgainstXSD(testContext);
    }

    /**
     * Test for the validateXMLAgainstXSD when Physical Path exists, points at a
     * Xml and conforms to ISO 19139.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test
    public void testValidateXmlAgainstXsdWhenPhysicalPathIsPresent() throws SAXException, IOException {
        LOGGER.info("[TS] : Name of Test Case: testValidateXmlAgainstXsdWhenPhysicalPathIsPresent\n"
                + "      [TS] : Description: Test for the validateXMLAgainstXSD when Physical Path exists, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
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
     * Xml and but does not conforms to ISO 19139.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test(expected = AssertionError.class)
    public void testValidateXmlAgainstXsdWhenPhysicalPathIsPresentButNotValid() throws SAXException, IOException {
        LOGGER.info("[TS] : Name of Test Case: testValidateXmlAgainstXsdWhenPhysicalPathIsPresentButNotValid\n"
                + "      [TS] : Description: Test for the validateXMLAgainstXSD when Physical Path exists, points at a Xml and but does not conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  FAIL\n");
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
     * points to any Xml.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test(expected = NullPointerException.class)
    public void testValidateXmlAgainstXsdWhenPhysicalPathIsNotPresent() throws SAXException, IOException {
        LOGGER.info("[TS] : Name of Test Case: testValidateXmlAgainstXsdWhenPhysicalPathIsNotPresent\n"
                + "      [TS] : Description: Test for the validateXMLAgainstXSD when Physical Path exists but does not points to any Xml.\n"
                + "      [TS] : Expected Result: Xml validation =  FAIL, should throgh NullPointerException\n");
        Capability1Tests iut = new Capability1Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/book.xml").toString());
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLAgainstXSD(testContext);
    }
}
