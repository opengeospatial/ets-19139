package org.opengis.cite.iso19136;

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
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.transform.stream.StreamSource;
import org.opengis.cite.iso19136.util.TestSuiteLogger;
import org.opengis.cite.iso19136.util.URIUtils;
import org.opengis.cite.iso19136.util.ValidationUtils;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.Reporter;

/**
 * A listener that performs various tasks before and after a test suite is run,
 * usually concerned with maintaining a shared test suite fixture. Since this
 * listener is loaded using the ServiceLoader mechanism, its methods will be
 * called before those of other suite listeners listed in the test suite
 * definition and before any annotated configuration methods.
 * 
 * Attributes set on an ISuite instance are not inherited by constituent test
 * group contexts (ITestContext). However, they are still accessible from lower
 * contexts.
 * 
 * @see org.testng.ISuite ISuite interface
 */
public class SuiteFixtureListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        processIUTParameter(suite);
        processRequiredSuiteParameters(suite);
        processSchematronSchema(suite);
        StringBuilder str = new StringBuilder("Initial test run parameters: ");
        str.append(suite.getXmlSuite().getAllParameters().toString());
        Reporter.log(str.toString());
        TestSuiteLogger.log(Level.CONFIG, str.toString());
    }

    @Override
    public void onFinish(ISuite suite) {
        Reporter.log("Success? " + !suite.getSuiteState().isFailed());
        String reportDir = suite.getOutputDirectory();
        String msg = String.format(
                "Test run directory: %s",
                reportDir.substring(0,
                        reportDir.lastIndexOf(File.separatorChar)));
        Reporter.log(msg);
    }

    /**
     * Processes the {@link org.opengis.cite.iso19136.TestRunArg#IUT} test suite
     * parameter. Its value is a URI referring to either an application schema
     * or a GML document. If the resource is an XML Schema the
     * {@link org.opengis.cite.iso19136.TestRunArg#XSD} parameter is set;
     * otherwise it is assumed to be a data resource.
     * 
     * @param suite
     *            An ISuite object representing a TestNG test suite.
     */
    void processIUTParameter(ISuite suite) {
        Map<String, String> params = suite.getXmlSuite().getParameters();
        String iutRef = params.get(TestRunArg.IUT.toString());
        if ((iutRef != null) && !iutRef.isEmpty()) {
            try {
                File iutFile = URIUtils.resolveURIAsFile(URI.create(iutRef));
                if (isXMLSchema(iutFile)) {
                    params.put(TestRunArg.XSD.toString(), iutRef);
                } else {
                    params.put(TestRunArg.GML.toString(), iutRef);
                }
            } catch (Exception x) {
                throw new RuntimeException("Failed to read resource from "
                        + iutRef, x);
            }
            params.remove(TestRunArg.IUT.toString());
        }
    }

    /**
     * Processes test suite parameters and sets suite attributes accordingly.
     * For example, adding the URI given by the "xsd" parameter to the
     * {@link SuiteAttribute#SCHEMA_LOC_SET} attribute ({@code Set<URI>}).
     * 
     * If both arguments are present, the schema(s) referenced by the
     * xsi:schemaLocation attribute in the GML instance document takes
     * precedence over the value of the
     * {@link org.opengis.cite.iso19136.TestRunArg#XSD} parameter.
     * 
     * @param suite
     *            An ISuite object representing a TestNG test suite.
     */
    void processRequiredSuiteParameters(ISuite suite) {
        Map<String, String> params = suite.getXmlSuite().getParameters();
        TestSuiteLogger.log(Level.CONFIG,
                "Suite parameters\n" + params.toString());
        String dataURI = params.get(TestRunArg.GML.toString());
        File dataFile = null;
        Set<URI> schemaURIs = new HashSet<URI>();
        if ((dataURI != null) && !dataURI.isEmpty()) {
            try {
                dataFile = URIUtils.resolveURIAsFile(URI.create(dataURI));
                schemaURIs.addAll(ValidationUtils.extractSchemaReferences(
                        new StreamSource(dataFile), dataURI));
            } catch (IOException iox) {
                throw new RuntimeException("Failed to obtain data from "
                        + dataURI, iox);
            } catch (XMLStreamException xse) {
                throw new RuntimeException(
                        "Failed to get schema reference from source: "
                                + dataFile.getAbsolutePath(), xse);
            }
        } else if (null != params.get(TestRunArg.XSD.toString())) {
            String xsdParam = params.get(TestRunArg.XSD.toString());
            schemaURIs.add(URI.create(xsdParam));
        } else {
            throw new IllegalArgumentException("Required parameters not found");
        }
        if (null != dataFile && dataFile.exists()) {
            suite.setAttribute(SuiteAttribute.GML.getName(), dataFile);
            TestSuiteLogger.log(Level.FINE,
                    "GML data file: " + dataFile.getAbsolutePath());
        }
        suite.setAttribute(SuiteAttribute.SCHEMA_LOC_SET.getName(), schemaURIs);
        if (TestSuiteLogger.isLoggable(Level.FINE)) {
            StringBuilder logMsg = new StringBuilder("Schema references: ");
            logMsg.append(schemaURIs);
            TestSuiteLogger.log(Level.FINE, logMsg.toString());
        }
    }

    /**
     * Adds a URI reference specifying the location of a Schematron schema.
     * 
     * @param suite
     *            An ISuite object representing a TestNG test suite.
     */
    void processSchematronSchema(ISuite suite) {
        Map<String, String> params = suite.getXmlSuite().getParameters();
        String schRef = params.get(TestRunArg.SCH.toString());
        if ((schRef != null) && !schRef.isEmpty()) {
            URI schURI = URI.create(params.get(TestRunArg.SCH.toString()));
            suite.setAttribute(SuiteAttribute.SCHEMATRON.getName(), schURI);
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
        return docElemName.getNamespaceURI().equals(
                XMLConstants.W3C_XML_SCHEMA_NS_URI);
    }

}
