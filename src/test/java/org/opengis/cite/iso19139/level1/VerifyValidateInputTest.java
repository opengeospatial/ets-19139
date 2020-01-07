/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opengis.cite.iso19139.level1;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.opengis.cite.iso19139.SuiteAttribute;
import org.opengis.cite.iso19139.TestRunArg;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite;
import org.xml.sax.SAXException;
import java.util.logging.Logger;


public class VerifyValidateInputTest {

    static final Logger LOGGER1 = Logger.getLogger("MY Logs");
    private static final String SUBJ = "testSubject";
    private static DocumentBuilder docBuilder;
    private static ITestContext testContext;
    private static ISuite suite;
    private static XmlSuite xmlSuite;
    private static final String ETS_ROOT_PKG = "/org/opengis/cite/iso19139/";
    private static Map<String, String> params = new TreeMap<String, String>();

    public VerifyValidateInputTest() {
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
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test for the validateXMLFile when URL exists and points at a Xml.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test
    public void testValidateXmlFileWhenUrlIsValid() throws SAXException, IOException {
        LOGGER1.info("[TS] : Name of Test Case: testValidateXmlFileWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the validateXMLFile when URL exists and points at a Xml.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        ValidateInputTest iut = new ValidateInputTest();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/example/gmd/databib_1.xml");
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLFile(testContext);
    }

    /**
     * Test for the validateXMLFile when URL exists and does not points at a
     * Xml.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test(expected = AssertionError.class)
    public void testValidateXmlFileWhenUrlIsNotValid() throws SAXException, IOException {
        LOGGER1.info("[TS] : Name of Test Case: testValidateXmlFileWhenUrlIsNotValid\n"
                + "      [TS] : Description: Test for the validateXMLFile when URL exists and does not points at a Xml.\n"
                + "      [TS] : Expected Result: Xml validation =  FAIL\n");
        ValidateInputTest iut = new ValidateInputTest();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441");
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLFile(testContext);
    }

    /**
     * Test for the validateXMLFile when Physical Path exists and points at a
     * Xml.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test
    public void testValidateXmlFileWhenPhysicalPathIsValid() throws SAXException, IOException {
        LOGGER1.info("[TS] : Name of Test Case: testValidateXmlFileWhenPhysicalPathIsValid\n"
                + "      [TS] : Description: Test for the validateXMLFile when Physical Path exists and points at a Xml.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        ValidateInputTest iut = new ValidateInputTest();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid.xml").toString());
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLFile(testContext);
    }

    /**
     * Test for the validateXMLFile when Physical Path not exists.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test(expected = NullPointerException.class)
    public void testValidateXmlFileWhenPhysicalPathIsNotValid() throws SAXException, IOException {
        LOGGER1.info("[TS] : Name of Test Case: testValidateXmlFileWhenPhysicalPathIsNotValid\n"
                + "      [TS] : Description: Test for the validateXMLFile when Physical Path not exists.\n"
                + "      [TS] : Expected Result: Xml validation =  FAIL, should throgh NullPointerException\n");
        ValidateInputTest iut = new ValidateInputTest();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-invalid").toString());
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLFile(testContext);
    }

    /**
     * Test for the validateXMLFile when Valid URL exists which points to a Xml.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test
    public void testValidateXmlPathWhenUrlIsValid() throws SAXException, IOException {
        LOGGER1.info("[TS] : Name of Test Case: testValidateXmlFileWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the validateXMLFile when Valid URL exists which points to a Xml.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS");
        ValidateInputTest iut = new ValidateInputTest();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/example/gmd/databib_1.xml");
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLPath(testContext);
    }

    /**
     * Test for the validateXMLFile when Valid URL not exists.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test(expected = AssertionError.class)
    public void testValidateXmlPathWhenUrlIsNotValid() throws SAXException, IOException {
        LOGGER1.info("[TS] : Name of Test Case: testValidateXmlFileWhenUrlIsNotValid\n"
                + "      [TS] : Description: Test for the validateXMLFile when Valid URL not exists.\n"
                + "      [TS] : Expected Result: Xml validation =  FAIL\n");
        ValidateInputTest iut = new ValidateInputTest();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441");
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLPath(testContext);
    }

    /**
     * Test for the validateXMLFile when Valid Physical Path exists which points
     * at a Xml.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test
    public void testValidateXmlPathWhenPhysicalPathIsValid() throws SAXException, IOException {
        LOGGER1.info("[TS] : Name of Test Case: testValidateXmlFileWhenPhysicalPathIsValid\n"
                + "      [TS] : Description: Test for the validateXMLFile when Physical Path exists and points at a Xml.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS");
        ValidateInputTest iut = new ValidateInputTest();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid.xml").toString());
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLPath(testContext);
    }

    /**
     * Test for the validateXMLFile when Valid Physical Path does not exists.
     *
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    @Test(expected = NullPointerException.class)
    public void testValidateXmlPathWhenPhysicalPathIsNotValid() throws SAXException, IOException {
        LOGGER1.info("[TS] : Name of Test Case: testValidateXmlPathWhenPhysicalPathIsNotValid\n"
                + "      [TS] : Description: Test for the validateXMLFile when Valid Physical Path does not exists.\n"
                + "      [TS] : Expected Result: Xml validation =  FAIL, should throgh NullPointerException\n");
        ValidateInputTest iut = new ValidateInputTest();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-invalid").toString());
        params1.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXMLPath(testContext);
    }

}
