/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opengis.cite.iso19139.level2;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
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
import org.w3c.dom.Document;

/**
 *
 * @author upendra
 */
public class VerifyCapability2Tests {
    static final Logger LOGGER = Logger.getLogger("MY Log");
    private static final String SUBJ = "testSubject";
    private static DocumentBuilder docBuilder;
    private static ITestContext testContext;
    private static ISuite suite;
    private static XmlSuite xmlSuite;
    private static final String ETS_ROOT_PKG = "/org/opengis/cite/iso19139/";
    private static Map<String, String> params = new TreeMap<String, String>();
    
    public VerifyCapability2Tests() {
    }
    
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
     * Test of validateXmlAgainstSchematronForNullReason method, of class Capability2Tests.
     */
    @Test
    public void testValidateXmlAgainstSchematronForNullReasonWhenUrlIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testValidateXmlAgainstSchematronForNullReasonWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the validateXmlAgainstSchematronForNullReason when URL valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXmlAgainstSchematronForNullReason(testContext);
    }
    
    @Test
    public void testValidateXmlAgainstSchematronForNullReasonWhenPhysicalPathIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testValidateXmlAgainstSchematronForNullReasonWhenPhysicalPathIsValid\n"
                + "      [TS] : Description: Test for the validateXmlAgainstSchematronForNullReason when Physical Path valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXmlAgainstSchematronForNullReason(testContext);
    }

    
    
    /**
     * Test of checkEncodingStandard method, of class Capability2Tests.
     */
    @Test
    public void testCheckEncodingStandardWhenUrlIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the checkEncodingStandard when URL valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkEncodingStandard(testContext);
    }
    
    @Test
    public void testCheckEncodingStandardWhenPhysicalPathIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenPhysicalPathIsValid\n"
                + "      [TS] : Description: Test for the checkEncodingStandard when Physical Path valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkEncodingStandard(testContext);
    }
    @Test(expected = AssertionError.class)
    public void testCheckEncodingStandardWhenPhysicalPathIsNotValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenPhysicalPathIsNotValid\n"
                + "      [TS] : Description: Test for the checkEncodingStandard when Physical Path Not valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  Fail\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-invalid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkEncodingStandard(testContext);
    }

    /**
     * Test of checkDataIdentification method, of class Capability2Tests.
     */
    @Test
    public void testCheckDataIdentificationWhenUrlIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckDataIdentificationWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the checkDataIdentification when URL valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkDataIdentification(testContext);
    }

    @Test
    public void testCheckDataIdentificationWhenPhysicalPathIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckDataIdentificationWhenPhysicalPathIsValid\n"
                + "      [TS] : Description: Test for the checkDataIdentification when Physical Path valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkDataIdentification(testContext);
    }
    
    @Test(expected = AssertionError.class)
    public void testCheckDataIdentificationWhenPhysicalPathIsNotValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckDataIdentificationWhenPhysicalPathIsNOtValid\n"
                + "      [TS] : Description: Test for the checkDataIdentification when Physical Path not valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-invalid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkDataIdentification(testContext);
    }
    /**
     * Test of checkAggregateInformation method, of class Capability2Tests.
     */
    @Test
    public void testCheckAggregateInformationWhenUrlIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the checkAggregateInformation when URL valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkAggregateInformation(testContext);
    }
    
    @Test
    public void testCheckAggregateInformationWhenPhysicalPathIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenPhysicalPathIsValid\n"
                + "      [TS] : Description: Test for the checkAggregateInformation when Physical Path valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkAggregateInformation(testContext);
    }

    @Test(expected = AssertionError.class)
    public void testCheckAggregateInformationWhenPhysicalPathIsNotValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenPhysicalPathIsNotValid\n"
                + "      [TS] : Description: Test for the checkAggregateInformation when Physical Path not valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-invalid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkAggregateInformation(testContext);
    }
    /**
     * Test of checkLegalConstraints method, of class Capability2Tests.
     */
    @Test
    public void testCheckLegalConstraintsWhenUrlIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the checkLegalConstraints when URL valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkLegalConstraints(testContext);
    }

    @Test
    public void testCheckLegalConstraintsWhenPhysicalPathIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenPhysicalPathIsValid\n"
                + "      [TS] : Description: Test for the checkLegalConstraints when Physical Path valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkLegalConstraints(testContext);
    }
    
    @Test(expected = AssertionError.class)
    public void testCheckLegalConstraintsWhenPhysicalPathIsNotValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenPhysicalPathIsNotValid\n"
                + "      [TS] : Description: Test for the checkLegalConstraints when Physical Path not valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-invalid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkLegalConstraints(testContext);
    }

    /**
     * Test of checkScopeOfXmlFile method, of class Capability2Tests.
     */
    @Test
    public void testCheckScopeOfXmlFileWhenUrlIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the checkScopeOfXmlFile when URL valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkScopeOfXmlFile(testContext);
    }

    @Test
    public void testCheckScopeOfXmlFileWhenPhysicalPathIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenPhysicalPathIsValid\n"
                + "      [TS] : Description: Test for the checkScopeOfXmlFile when Physical Path valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkScopeOfXmlFile(testContext);
    }

    /**
     * Test of checkGeorectified method, of class Capability2Tests.
     */
    @Test
    public void testCheckGeorectifiedWhenUrlIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the checkGeorectified when URL valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkGeorectified(testContext);
    }

    @Test
    public void testCheckGeorectifiedWhenPhysicalPathIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenPhysicalPathIsValid\n"
                + "      [TS] : Description: Test for the checkGeorectified when Physical Path valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkGeorectified(testContext);
    }

    /**
     * Test of checkBandValueOfXml method, of class Capability2Tests.
     */
    @Test
    public void testCheckBandValueOfXmlWhenUrlIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the checkBandValueOfXml when URL valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkBandValueOfXml(testContext);
    }

    @Test
    public void testCheckBandValueOfXmlWhenPhysicalPathIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenPhysicalPathIsValid\n"
                + "      [TS] : Description: Test for the checkBandValueOfXml when Physical Path valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkBandValueOfXml(testContext);
    }

    /**
     * Test of checkMediumOfXml method, of class Capability2Tests.
     */
    @Test
    public void testCheckMediumOfXmlWhenUrlIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the checkMediumOfXml when URL valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkMediumOfXml(testContext);
    }

    @Test
    public void testCheckMediumOfXmlWhenPhysicalPathIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenPhysicalPathIsValid\n"
                + "      [TS] : Description: Test for the checkMediumOfXml when Physical Path valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkMediumOfXml(testContext);
    }

    /**
     * Test of checkExtendedElementInformation method, of class Capability2Tests.
     */
    @Test
    public void testCheckExtendedElementInformationWhenUrlIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the checkExtendedElementInformation when URL valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkExtendedElementInformation(testContext);
    }

    @Test
    public void testCheckExtendedElementInformationWhenPhysicalPathIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenPhysicalPathIsValid\n"
                + "      [TS] : Description: Test for the checkExtendedElementInformation when Physical Path valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkExtendedElementInformation(testContext);
    }

    /**
     * Test of checkExtentValueOfXml method, of class Capability2Tests.
     */
    @Test
    public void testCheckExtentValueOfXmlWhenUrlIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the checkExtentValueOfXml when URL valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkExtentValueOfXml(testContext);
    }
    @Test
    public void testCheckExtentValueOfXmlWhenPhysicalPathIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenPhysicalPathIsValid\n"
                + "      [TS] : Description: Test for the checkExtentValueOfXml when Physical Path valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkExtentValueOfXml(testContext);
    }

    /**
     * Test of checkResponsiblePartyOfXml method, of class Capability2Tests.
     */
    @Test
    public void testCheckResponsiblePartyOfXmlWhenUrlIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenUrlIsValid\n"
                + "      [TS] : Description: Test for the checkResponsiblePartyOfXml when URL valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkResponsiblePartyOfXml(testContext);
    }

    @Test
    public void testCheckResponsiblePartyOfXmlWhenPhysicalPathIsValid() {
        LOGGER.info("[TS] : Name of Test Case: testCheckEncodingStandardWhenPhysicalPathIsValid\n"
                + "      [TS] : Description: Test for the checkResponsiblePartyOfXml when Physical Path valid, points at a Xml and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability2Tests iut = new Capability2Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid-xml-for-schematron.xml").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.checkResponsiblePartyOfXml(testContext);
    }
}
