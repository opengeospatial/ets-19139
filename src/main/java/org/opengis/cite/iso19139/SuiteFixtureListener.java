package org.opengis.cite.iso19139;

import org.opengis.cite.iso19139.util.XMLUtils;


import org.w3c.dom.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import org.opengis.cite.iso19139.util.TestSuiteLogger;
import org.opengis.cite.iso19139.util.URIUtils;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.Reporter;

/**
 * A listener that performs various tasks before and after a test suite is run,
 * usually concerned with maintaining a shared test suite fixture. Since this
 * listener is loaded using the ServiceLoader mechanism, its methods will be
 * called before those of other suite listeners listed in the test suite
 * definition and before any annotated configuration methods.
 *
 * Attributes set on an ISuite instance are not inherited by constituent test
 * group contexts (ITestContext). However, suite attributes are still accessible
 * from lower contexts.
 *
 * @see org.testng.ISuite ISuite interface
 */
public class SuiteFixtureListener implements ISuiteListener {

    private static final String ETS_ROOT_PKG = "/org/opengis/cite/beta/";

    @Override
    public void onStart(ISuite suite) {
        processIUTParameter(suite);
        processSuiteParameters(suite);
        processRequiredSuiteParameters(suite);
        StringBuilder str = new StringBuilder("Initial test run parameters: ");
        str.append(suite.getXmlSuite().getAllParameters().toString());
        Reporter.log(str.toString());
        TestSuiteLogger.log(Level.CONFIG, str.toString());
    }

    @Override
    public void onFinish(ISuite suite) {
        Reporter.clear(); // clear output from previous test runs
        // Reporter.log("Test suite parameters:");
        // Reporter.log(suite.getXmlSuite().getAllParameters().toString());
        Reporter.log("The result of the test is-\n\n");

        //Following code gets the suite name
        String suiteName = suite.getName();
        //Getting the results for the said suite
        Map suiteResults = suite.getResults();
        int count = 0;
        for (Object obj : suiteResults.values()) {
            count++;
            ISuiteResult sr = (ISuiteResult) obj;
            ITestContext tc = sr.getTestContext();

            if (count == 1) {
                try {
                    Reporter.log("Passed tests for suite '" + suiteName
                            + "' is:" + tc.getPassedTests().getAllResults().size());

                    Reporter.log("Failed tests for suite '" + suiteName
                            + "' is:"
                            + tc.getFailedTests().getAllResults().size());

                    Reporter.log("Skipped tests for suite '" + suiteName
                            + "' is:"
                            + tc.getSkippedTests().getAllResults().size());

                    String failReport = tc.getAttribute("FailReport").toString();
                    Reporter.log("\nREASON:\n\n");
                    Reporter.log(failReport);


                } catch (Exception ex) {
                    Reporter.log("The test did not pass the input validation. Either the file given as the input does not exist or the file is not an XML file");

                }
            }
        }
    }

    /**
     * Processes the {@link org.opengis.cite.iso19139.TestRunArg#IUT} test suite
     * parameter. Sets the XML schema location for ISO 19139 against which the the test argument will be tested.
     * @param suite
     *            An ISuite object representing a TestNG test suite.
     */
    void processIUTParameter(ISuite suite) {
        Map<String, String> params = suite.getXmlSuite().getParameters();
        String iutRef = params.get(TestRunArg.IUT.toString());
        params.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());

    }

    /**
     * Processes test suite arguments and sets suite attributes accordingly. The
     * entity referenced by the {@link TestRunArg#IUT iut} argument is parsed
     * and the resulting Document is set as the value of the "testSubject"
     * attribute.
     * 
     * @param suite
     *            An ISuite object representing a TestNG test suite.
     */
    void processRequiredSuiteParameters(ISuite suite) {
        Map<String, String> params = suite.getXmlSuite().getParameters();
        TestSuiteLogger.log(Level.CONFIG,
                "Suite parameters\n" + params.toString());
        String dataURI = params.get(TestRunArg.IUT.toString());
        File dataFile = null;
        Set<URI> schemaURIs = new HashSet<URI>();
        if ((dataURI != null) && !dataURI.isEmpty()) {
            try {
                dataFile = URIUtils.dereferenceURI(URI.create(dataURI));
                // schemaURIs.addAll(ValidationUtils.extractSchemaReferences(
                // new StreamSource(dataFile), dataURI));
            } catch (Exception iox) {
                System.out.println("Failed to dereference resource located at "
                        + dataURI + ".\n The file or URL given as the input does not exist.");
                
            }
        } else if (null != params.get(TestRunArg.XSD.toString())) {
            String xsdParam = params.get(TestRunArg.XSD.toString());
            schemaURIs.add(URI.create(xsdParam));
        } else {
            System.out.println("Required test run parameter (i.e XML to be validated) not found.");
           
        }
        if (null != dataFile && dataFile.exists()) {
            suite.setAttribute(SuiteAttribute.IUT.getName(), dataFile);
            TestSuiteLogger.log(Level.FINE,
                    "GMD data file: " + dataFile.getAbsolutePath());
        }
        suite.setAttribute(SuiteAttribute.SCHEMA_LOC_SET.getName(), schemaURIs);
        if (TestSuiteLogger.isLoggable(Level.FINE)) {
            StringBuilder logMsg = new StringBuilder("Schema references: ");
            logMsg.append(schemaURIs);
            TestSuiteLogger.log(Level.FINE, logMsg.toString());
        }

    }
    // ORIGINAL

    void processSuiteParameters(ISuite suite) {
        Map<String, String> params = suite.getXmlSuite().getParameters();
        TestSuiteLogger.log(Level.CONFIG,
                "Suite parameters\n" + params.toString());
        String iutParam = params.get(TestRunArg.IUT.toString());
        String iutXsd = params.get(TestRunArg.XSD.toString());
        if ((null == iutParam) || iutParam.isEmpty()) {
            System.out.println(
                    "Required test run parameter (i.e XML to be validated) not found: "
                    + TestRunArg.IUT.toString());
            onFinish(suite);
            
        }

        URI iutRef = URI.create(iutParam.trim());
        if(!"".equals(iutRef.toString())){
            System.out.println(
                "The file/ URL given as input is: "
                + iutRef);
        }
        else
        {
            System.out.println(
                "NO FILE/ URL GIVEN AS INPUT.");
        }
        
        File entityFile = null;
        try {
            entityFile = URIUtils.dereferenceURI(iutRef);
        } catch (Exception iox) {
            System.out.println("Failed to dereference resource located at "
                    + iutRef + ".\n The file or URL given as the input does not exist.");
            onFinish(suite);
            
        }
        Document iutDoc = null;
        try {
            iutDoc = URIUtils.parseURI(entityFile.toURI());
        } catch (Exception x) {
            System.out.println("Failed to parse resource retrieved from " + iutRef + ".\nThe given input is not a XML file.");
            onFinish(suite);
           

        }
        suite.setAttribute(SuiteAttribute.TEST_SUBJECT.getName(), iutDoc);
        if (TestSuiteLogger.isLoggable(Level.FINE)) {
            StringBuilder logMsg = new StringBuilder(
                    "Parsed resource retrieved from ");
            logMsg.append(iutRef).append("\n");
            logMsg.append(XMLUtils.writeNodeToString(iutDoc));
            TestSuiteLogger.log(Level.FINE, logMsg.toString());
        }
    }

    /**
     * Determines if the content of the given file represents an XML Schema.
     * 
     * @param file
     *            A File object.
     * @return {@code true} if the file contains an XML Schema; {@code false}
     *         otherwise.
     */
    boolean isXMLSchema(File file) {
        if (!file.exists() || (file.length() == 0)) {
            return false;
        }
        QName docElemName = QName.valueOf("");
        InputStream inStream = null;
        XMLEventReader reader = null;
        try {
            inStream = new FileInputStream(file);
            XMLInputFactory factory = XMLInputFactory.newInstance();
            reader = factory.createXMLEventReader(inStream);
            StartElement docElem = reader.nextTag().asStartElement();
            docElemName = docElem.getName();
        } catch (Exception e1) {
            return false;
        } finally {
            try {
                reader.close();
                inStream.close();
            } catch (Exception e2) {
                TestSuiteLogger.log(Level.INFO, "Error closing resource.", e2);
            }
        }
        // TO-DO
        boolean isXmlSchema = docElemName.getNamespaceURI().equals(
                XMLConstants.W3C_XML_SCHEMA_NS_URI);
        // return docElemName.getNamespaceURI().equals(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        return true;
    }
}
