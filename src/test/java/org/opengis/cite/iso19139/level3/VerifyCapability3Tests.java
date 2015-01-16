/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.opengis.cite.iso19139.level3;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

//Junit test case for Conformance level 3.
public class VerifyCapability3Tests {
  static final Logger LOGGER = Logger.getLogger("MY Log");
    private static final String SUBJ = "testSubject";
    private static DocumentBuilder docBuilder;
    private static ITestContext testContext;
    private static ISuite suite;
    private static XmlSuite xmlSuite;
    private static final String ETS_ROOT_PKG = "/org/opengis/cite/iso19139/";
    private static Map<String, String> params = new TreeMap<String, String>();
    
  public VerifyCapability3Tests() {
  }
  
  @BeforeClass
  public static void setUpClass() throws ParserConfigurationException {
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
   * Test of validateXmlAgainstUserDefinedSchematronWhenURLIsValid method, of class Capability3Tests.
   */
  @Test
  public void testValidateXmlAgainstUserDefinedSchematronWhenURLIsValid() throws Exception {
    LOGGER.info("[TS] : Name of Test Case: testValidateXmlAgainstUserDefinedSchematronWhenURLIsValid\n"
                + "      [TS] : Description: Test for the ValidateXmlAgainstUserDefinedSchematron when URL valid, points at a Schematron and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability3Tests iut = new Capability3Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        params1.put(TestRunArg.SCH.toString(), this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid-schematron-for-schematron.sch").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXmlAgainstUserDefinedSchematron(testContext);
  }

  
  @Test
  public void testValidateXmlAgainstUserDefinedSchematronWhenPhysicalPathIsValid() throws Exception {
    LOGGER.info("[TS] : Name of Test Case: testValidateXmlAgainstUserDefinedSchematronWhenPhysicalPathLIsValid\n"
                + "      [TS] : Description: Test for the ValidateXmlAgainstUserDefinedSchematron when physical path valid, points at a Schematron and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  PASS\n");
        Capability3Tests iut = new Capability3Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        params1.put(TestRunArg.SCH.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-valid-schematron-for-schematron.sch").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXmlAgainstUserDefinedSchematron(testContext);
  }

  @Test(expected = AssertionError.class)
  public void testValidateXmlAgainstUserDefinedSchematronWhenPhysicalPathIsNotValid() throws Exception {
    LOGGER.info("[TS] : Name of Test Case: testValidateXmlAgainstUserDefinedSchematronWhenPhysicalPathLIsNotValid\n"
                + "      [TS] : Description: Test for the ValidateXmlAgainstUserDefinedSchematron when physical path is not valid, points at a Schematron and conforms to ISO 19139.\n"
                + "      [TS] : Expected Result: Xml validation =  FAIL\n");
        Capability3Tests iut = new Capability3Tests();
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        // Map<String, String> params1=null;
        Map<String, String> params1 = testContext.getSuite().getXmlSuite().getParameters();
        params1.put(TestRunArg.IUT.toString(), "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
        params1.put(TestRunArg.SCH.toString(),  this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/test-arg-invalid-schematron-for-schematron.sch").toString());
        testContext.getSuite().getXmlSuite().setParameters(null);
        iut.validateXmlAgainstUserDefinedSchematron(testContext);
  }
  
}
