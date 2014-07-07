package org.opengis.cite.iso19139.level2;

//import cz.jakubmaly.schematronassert.schematron.model.Schema;
//import cz.jakubmaly.schematronassert.schematron.validation.XsltSchematronValidator;
//import cz.jakubmaly.schematronassert.svrl.model.ValidationOutput;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.logging.Level;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.*;
import org.opengis.cite.iso19139.ETSAssert;
import org.opengis.cite.iso19139.ErrorMessage;
import org.opengis.cite.iso19139.ErrorMessageKeys;
import org.opengis.cite.iso19139.Namespaces;
import org.opengis.cite.iso19139.SuiteAttribute;
import org.opengis.cite.iso19139.TestRunArg;
import org.opengis.cite.iso19139.util.TestSuiteLogger;
import org.opengis.cite.iso19139.util.XMLUtils;
import org.opengis.cite.validation.SchematronValidator;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

/**
 * Includes various tests of capability 2.
 */
public class Capability2Tests {

    private Document testSubject;
    private SchematronValidator dataValidator;
    /**
     * Timeout for compiling schemas /ms
     */
    static final long COMPILE_TIMEOUT = 180000;
    private static final String ETS_ROOT_PKG = "/org/opengis/cite/iso19139/";

    /**
     * Obtains the test subject from the ISuite context. The suite attribute
     * {@link org.opengis.cite.iso19139.SuiteAttribute#TEST_SUBJECT} should
     * evaluate to a DOM Document node.
     *
     * @param testContext The test (group) context.
     */
    @BeforeClass(alwaysRun = true)
    public void obtainTestSubject(ITestContext testContext) {
        Object obj = testContext.getSuite().getAttribute(
                SuiteAttribute.TEST_SUBJECT.getName());
        if ((null != obj) && Document.class.isAssignableFrom(obj.getClass())) {
            this.testSubject = Document.class.cast(obj);
        }
    }
    
    /**
     * Sets the test subject. This method is intended to facilitate unit
     * testing.
     *
     * @param testSubject A Document node representing the test subject or
     * metadata about it.
     */
    public void setTestSubject(Document testSubject) {
        this.testSubject = testSubject;
    }
    
    @BeforeTest
    public void validateConfromanceLevelTwoEnabled(ITestContext testContext) {
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String level = params.get(TestRunArg.ICS.toString());
        if ("2".equals(level)) {
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(false, "Conformance level two is not enabled");
        }
    }

    /**
     * Attempts to construct a Schematron validator from a schema reference
     * given in (a) the ISO data file, or (b) a test run argument (in the ISuite
     * context).
     *
     * @param testContext The test set context.
     */
    @BeforeClass
    public void createSchematronValidator(ITestContext testContext) {
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> piData = testContext.getSuite().getXmlSuite().getParameters();
        String phase = "#ALL";
        URI schematronURI;
        Source schema = null;
        if (isSchematronReference(piData)) {
            schematronURI = URI.create(piData.get("href"));
            if (!schematronURI.isAbsolute()) {
                // resolve relative URI against location of ISO data
                String dataURI = testContext.getSuite().getParameter(
                        TestRunArg.IUT.toString());
                URI baseURI = URI.create(dataURI);
                schematronURI = baseURI.resolve(schematronURI);
            }
            schema = new StreamSource(schematronURI.toString());
            if (piData.containsKey("phase")) {
                phase = piData.get("phase");
            }
        } else { // look for suite attribute (test run argument)
            Set<String> suiteAttrs = testContext.getSuite().getAttributeNames();
            if (suiteAttrs.contains(SuiteAttribute.SCHEMATRON.getName())) {
                schematronURI = (URI) testContext.getSuite().getAttribute(
                        SuiteAttribute.SCHEMATRON.getName());
                schema = new StreamSource(schematronURI.toString());
            }
        }
        if (null != schema) {
            try {
                this.dataValidator = new SchematronValidator(schema, phase);
            } catch (Exception e) {
                System.out.println("Failed to create SchematronValidator.\n" + e);

            }
        }
    }

    public File localFileCreation(String testContext) {
        String url = testContext;
        String destinationDir = "";
        int periodIndex = url.lastIndexOf('.');
        int slashIndex = url.lastIndexOf("/");
        String fileName = url.substring(slashIndex + 1);

        if (((periodIndex >= 1) || (periodIndex == -1)) && slashIndex >= 0 && slashIndex < url.length() - 1) {
            destinationDir = System.getProperty("user.home").toString() + "/XMLFolder/";
            File directory = new File(destinationDir);
            if (!directory.exists()) {
                directory.mkdir();
            }
            try {
                directory.setExecutable(true);
                directory.setReadable(true);
                directory.setWritable(true);
                //Runtime.getRuntime().exec(directory.getAbsolutePath().toString());
            } catch (Exception e) {
                //Ignore
            }
            final int size = 1024;
            OutputStream outStream = null;
            @SuppressWarnings("UnusedAssignment")
            URLConnection uCon = null;
            InputStream is = null;
            try {
                URL Url;
                byte[] buf;
                int ByteRead;
                Url = new URL(url);
                outStream = new BufferedOutputStream(new FileOutputStream(destinationDir + fileName));

                uCon = Url.openConnection();
                is = uCon.getInputStream();
                buf = new byte[size];
                //Copy the contents of he XML in a physical file
                while ((ByteRead = is.read(buf)) != -1) {
                    outStream.write(buf, 0, ByteRead);
                }
                is.close();
                outStream.close();
            } catch (IOException e) {
                //XML file is corrupted or malformed
            }
        }
        return new File(destinationDir + fileName);
    }

    /**
     * [{@code Test}] Verifies that a ISO instance satisfies the additional
     * Schematron constraints specified in ISO 19136.
     *
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li><a
     * href="http://schemas.opengis.net/gml/3.2.1/SchematronConstraints.xml">
     * Schematron constraints for ISO 19136</a></li>
     * </ul>
     *
     * @param testContext
     */
    @Test(description = "Implements ATC 2-1")
    public void validateXmlAgainstSchematronForNullReason(ITestContext testContext) {
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String url = params.get(TestRunArg.IUT.toString());
        URL schRef = this.getClass().getResource("/org/opengis/cite/schematron/iso19139_schematron_nil.sch");
            File dataFile = localFileCreation(url);
     SchematronValidator validator;
        try {
            validator = new SchematronValidator(new StreamSource(
                    schRef.toString()), "#ALL");
        } catch (Exception e) {
            StringBuilder msg = new StringBuilder(
                    "Failed to process Schematron schema at ");
            msg.append(schRef).append('\n');
            msg.append(e.getMessage());
            throw new AssertionError(msg);
        }
        DOMResult result = validator.validate(new StreamSource(dataFile));

        // Get number of violation count
        String countNo = ErrorMessage.format(ErrorMessageKeys.NOT_SCHEMA_VALID,
                validator.getRuleViolationCount());
        
        // Fetch error message when schema is not valid
        String errorMessage = ErrorMessage.format(ErrorMessageKeys.NOT_SCHEMA_VALID,
                XMLUtils.writeNodeToString(result.getNode()));
        
        String error = "";
        
        String delims = "<svrl:failed-assert";
        String[] failedAssertList = errorMessage.split(delims);
        
        for (int i = 1; i < failedAssertList.length; i++) {
            
            delims = "location=";
            String[] locationToken = failedAssertList[i].split(delims);
            
            for (int j = 1; j < locationToken.length; j++) {
                error = error + ",Location:"+",";
                
                delims = "/\\*:";
                String[] tokens2 = locationToken[j].split(delims);
                for (int k = 1; k < tokens2.length; k++) {
                                
                    if (tokens2[k].contains("<svrl:text>")) {
                        
                        delims = "<svrl:text>";
                        String[] failedAssertMessage = tokens2[k].split(delims);
                        
                        for (int l = 0; l < failedAssertMessage.length; l++) {
                            if(failedAssertMessage[l].contains("</svrl:text>"))
                            {
                                failedAssertMessage[l]="Reason :," + failedAssertMessage[l].split("</svrl:text>")[0];
                            }
                            else{
                                failedAssertMessage[l]=failedAssertMessage[l].split(">")[0];
                            }
                            error = error + failedAssertMessage[l]+",";
                        }
                    }
                     else {
                                error = error + tokens2[k]+",";                            
                    }
                }
            }
        }
        
        errorMessage = countNo + "," + error;
        if(validator.ruleViolationsDetected())
        {
            testContext.setAttribute("FailedReport","Conferms the clause of A.2.1 of ISO19139");
        }
        else
        {
            testContext.setAttribute("FailedReport","Does not conferms the clause of A.2.1 of ISO19139");
        }
        Assert.assertFalse(validator.ruleViolationsDetected(), errorMessage);
    }

    /**
     * [{@code Test}] Checks for the presence of any deprecated ISO elements. A
     * warning is issued for each occurrence.
     *
     * @param testContext
     * @see "ISO 19136, Annex I: Backwards compatibility with earlier versions
     * of ISO"
     */
    
    @Test(description = "Checking XML File encoding standard", dependsOnMethods ="validateXmlAgainstSchematronForNullReason")
    public void checkEncodingStandard(ITestContext testContext){
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String url = params.get(TestRunArg.IUT.toString());
        URL schRef = this.getClass().getResource("/org/opengis/cite/schematron/checkEncodingStandard.sch");
        File dataFile = localFileCreation(url);
        ETSAssert
                .assertSchematronValid(schRef, new StreamSource(dataFile));
    }
    
    @Test(description = "Checking Data Identification of XML File", dependsOnMethods ="checkEncodingStandard")
    public void checkDataIdentification(ITestContext testContext){
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String url = params.get(TestRunArg.IUT.toString());
        URL schRef = this.getClass().getResource("/org/opengis/cite/schematron/checkDataIdentification.sch");
        File dataFile = localFileCreation(url);
        ETSAssert
                .assertSchematronValid(schRef, new StreamSource(dataFile));
    }
    
    @Test(description = "Checking aggregate information of XML File", dependsOnMethods ="checkDataIdentification")
    public void checkAggregateInformation(ITestContext testContext){
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String url = params.get(TestRunArg.IUT.toString());
        URL schRef = this.getClass().getResource("/org/opengis/cite/schematron/checkAggregateInformation.sch");
        File dataFile = localFileCreation(url);
        ETSAssert
                .assertSchematronValid(schRef, new StreamSource(dataFile));
    }
    
    @Test(description = "Checking legal constraints of XML File", dependsOnMethods ="checkAggregateInformation")
    public void checkLegalConstraints(ITestContext testContext){
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String url = params.get(TestRunArg.IUT.toString());
        URL schRef = this.getClass().getResource("/org/opengis/cite/schematron/checkLegalConstraints.sch");
        File dataFile = localFileCreation(url);
        ETSAssert
                .assertSchematronValid(schRef, new StreamSource(dataFile));
    }
    
    @Test(description = "Checking scope of XML File", dependsOnMethods ="checkLegalConstraints")
    public void checkScopeOfXmlFile(ITestContext testContext){
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String url = params.get(TestRunArg.IUT.toString());
        URL schRef = this.getClass().getResource("/org/opengis/cite/schematron/checkScopeOfXmlFile.sch");
        File dataFile = localFileCreation(url);
        ETSAssert
                .assertSchematronValid(schRef, new StreamSource(dataFile));
    }
    
    @Test(description = "Checking georectified of XML File", dependsOnMethods ="checkScopeOfXmlFile")
    public void checkGeorectified(ITestContext testContext){
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String url = params.get(TestRunArg.IUT.toString());
        URL schRef = this.getClass().getResource("/org/opengis/cite/schematron/checkGeorectified.sch");
        File dataFile = localFileCreation(url);
        ETSAssert
                .assertSchematronValid(schRef, new StreamSource(dataFile));
    }
    
    @Test(description = "Checking band value of XML File", dependsOnMethods ="checkGeorectified")
    public void checkBandValueOfXml(ITestContext testContext){
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String url = params.get(TestRunArg.IUT.toString());
        URL schRef = this.getClass().getResource("/org/opengis/cite/schematron/checkBandValueOfXml.sch");
        File dataFile = localFileCreation(url);
        ETSAssert
                .assertSchematronValid(schRef, new StreamSource(dataFile));
    }
    
    @Test(description = "Checking medium of XML File", dependsOnMethods ="checkBandValueOfXml")
    public void checkMediumOfXml(ITestContext testContext){
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String url = params.get(TestRunArg.IUT.toString());
        URL schRef = this.getClass().getResource("/org/opengis/cite/schematron/checkMediumOfXml.sch");
        File dataFile = localFileCreation(url);
        ETSAssert
                .assertSchematronValid(schRef, new StreamSource(dataFile));
    }
    
    @Test(description = "Checking extended element information of XML File", dependsOnMethods ="checkMediumOfXml")
    public void checkExtendedElementInformation(ITestContext testContext){
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String url = params.get(TestRunArg.IUT.toString());
        URL schRef = this.getClass().getResource("/org/opengis/cite/schematron/checkExtendedElementInformation.sch");
        File dataFile = localFileCreation(url);
        ETSAssert
                .assertSchematronValid(schRef, new StreamSource(dataFile));
    }
    
    @Test(description = "Checking extent value of XML File", dependsOnMethods ="checkExtendedElementInformation")
    public void checkExtentValueOfXml(ITestContext testContext){
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String url = params.get(TestRunArg.IUT.toString());
        URL schRef = this.getClass().getResource("/org/opengis/cite/schematron/checkExtentValueOfXml.sch");
        File dataFile = localFileCreation(url);
        ETSAssert
                .assertSchematronValid(schRef, new StreamSource(dataFile));
    }
    
    @Test(description = "Checking responcible party of XML File", dependsOnMethods ="checkExtentValueOfXml")
    public void checkResponsiblePartyOfXml(ITestContext testContext){
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String url = params.get(TestRunArg.IUT.toString());
        URL schRef = this.getClass().getResource("/org/opengis/cite/schematron/checkResponsiblePartyOfXml.sch");
        File dataFile = localFileCreation(url);
        ETSAssert
                .assertSchematronValid(schRef, new StreamSource(dataFile));
    }

    /**
     * Indicates whether or not the given PI data includes a Schematron schema
     * reference.
     *
     * @param piData A Map containing PI data (pseudo-attributes).
     * @return {@code true} if the "schematypens" pseudo-attribute has the value
     * {@value org.opengis.cite.iso19136.Namespaces#SCH}; {@code false}
     * otherwise;
     */
    boolean isSchematronReference(Map<String, String> piData
    ) {
        if (null != piData && null != piData.get("schematypens")) {
            return piData.get("schematypens").equals(Namespaces.SCH);
        }
        return false;
    }

    /**
     * Extracts the data items from the {@code xml-model} processing
     * instruction. The PI must appear before the document element.
     *
     * @param dataFile A File containing the ISO instance.
     * @return A Map containing the supplied pseudo-attributes, or {@code null}
     * if the PI is not present.
     */
    Map<String, String> getXmlModelPIData(File dataFile
    ) {
        Map<String, String> piData = null;
        XMLStreamReader reader = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(dataFile);
            XMLInputFactory factory = XMLInputFactory.newInstance();
            reader = factory.createXMLStreamReader(input);
            int event = reader.getEventType();
            // Now in START_DOCUMENT state. Stop at document element.
            while (event != XMLStreamReader.START_ELEMENT) {
                event = reader.next();
                if (event == XMLStreamReader.PROCESSING_INSTRUCTION) {
                    if (reader.getPITarget().equals("xml-model")) {
                        String[] pseudoAttrs = reader.getPIData().split("\\s+");
                        piData = new HashMap<String, String>();
                        for (String pseudoAttr : pseudoAttrs) {
                            String[] nv = pseudoAttr.split("=");
                            piData.put(nv[0].trim(), nv[1].replace('"', ' ')
                                    .trim());
                        }
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            TestSuiteLogger.log(Level.WARNING, "Failed to parse document at "
                    + dataFile.getAbsolutePath(), e);
            return null; // not an XML document
        } catch (XMLStreamException e) {
            TestSuiteLogger.log(Level.WARNING, "Failed to parse document at "
                    + dataFile.getAbsolutePath(), e);
            return null; // not an XML document
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
                if (null != input) {
                    input.close();
                }
            } catch (XMLStreamException x) {
                TestSuiteLogger.log(Level.INFO, x.getMessage());
            } catch (IOException x) {
                TestSuiteLogger.log(Level.INFO, x.getMessage());
            }
        }
        return piData;
    }
}
