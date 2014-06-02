package org.opengis.cite.iso19139.level1;

import java.net.URI;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.logging.Logger;
import org.apache.xerces.xs.XSModel;
import org.opengis.cite.iso19139.SuiteAttribute;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.opengis.cite.iso19139.TestRunArg;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Includes tests that validate whether the entered XML file conforms to the clause A.1 of ISO 19139.
 */
public class Capability1Tests {

    private Document testSubject;
    /** Timeout for compiling schemas /ms */
    static final long COMPILE_TIMEOUT = 180000;
    private final Logger logr = Logger.getLogger(this.getClass().getPackage().getName());
    private static final String ETS_ROOT_PKG = "/org/opengis/cite/beta/";
    private Set<URI> xsdLocations;
    private Schema appSchema;
    private XSModel model;
    private URI targetNamespace;

    /**
     * Obtains the test subject from the ISuite context. The suite attribute
     * {@link org.opengis.cite.iso19139.SuiteAttribute#TEST_SUBJECT} should
     * evaluate to a DOM Document node.
     * 
     * @param testContext
     *            The test (group) context.
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
     * Obtains the schema locations from the ISuite context. The suite attribute
     * {@link SuiteAttribute#SCHEMA_LOC_SET} should evaluate to a Set of URI
     * objects specifying the locations of the relevant XML Schema grammars.
     * 
     * @param testContext
     *            The test (group) context.
     */
    @BeforeClass
    @SuppressWarnings("unchecked")
    public void getSchemaURIsFromTestContext(ITestContext testContext) {
        Object uriSet = testContext.getSuite().getAttribute(
                SuiteAttribute.SCHEMA_LOC_SET.getName());
        if ((null != uriSet) && Set.class.isAssignableFrom(uriSet.getClass())) {
            this.xsdLocations = (Set<URI>) uriSet;
        } else {
            throw new MissingResourceException(
                    "Unable to obtain XML Schema locations from ITestContext",
                    SuiteAttribute.SCHEMA_LOC_SET.getType().getName(),
                    SuiteAttribute.SCHEMA_LOC_SET.getName());
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
    
     /**
     * This test will run only after all the tests belonging to the group 'inputvalidation' pass 
     * successfully.
     * This test method is used to check whether the given input XML conforms to clause A.1 of 
     * ISO 19139.
     */
    @Test(dependsOnGroups = {"inputvalidation.*"})
    public void validateXMLAgainstXSD(ITestContext testContext) throws SAXException,
            IOException {
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String url = params.get(TestRunArg.IUT.toString());
        int periodIndex = url.lastIndexOf('.');
        int slashIndex = url.lastIndexOf("/");
        boolean testResult = false;
        params.put(TestRunArg.XSD.toString(), this.getClass().getResource(ETS_ROOT_PKG + "xsd/iso/19139/20070417/gmd/gmd.xsd").toString());
        String fileName = url.substring(slashIndex + 1);

        if (((periodIndex >= 1)||(periodIndex == -1)) && slashIndex >= 0 && slashIndex < url.length() - 1)
        {
            String destinationDir = System.getProperty("user.home").toString() + "/XMLFolder/";
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
                //Give the gmd.xsd schema (root schema) path
                Source schemaFile = new StreamSource((params.get(TestRunArg.XSD.toString())));
                String check = params.get(TestRunArg.XSD.toString());
                Source xmlFile = new StreamSource(new File(destinationDir + fileName));
                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = schemaFactory.newSchema(schemaFile);

                System.out.println("==========================================================================================\n\n");
                Validator validator = schema.newValidator();
                try {
                    //Validate the XML file against ISO 19139 XSD
                    validator.validate(xmlFile);
                    System.out.println(url + " conforms to the clause A.1 of ISO 19139.\n");
                    testResult = true;
                    testContext.setAttribute("FailReport", url + " conforms to the clause A.1 of ISO 19139.");

                } catch (SAXParseException e) {

                    //Print the reason why the XML validation fails
                    System.out.println(url + " doesn't conform to the clause A.1 of ISO 19139.\n");
                    System.out.println("Reason: " + e.getLocalizedMessage());
                    System.out.println("Line Number \t: " + e.getLineNumber());
                    System.out.println("Column Number\t: " + e.getColumnNumber() + "\n");

                    String failReport = url + " doesn't conform to the clause A.1 of ISO 19139.\n"
                            + "Reason: " + e.getLocalizedMessage()
                            + "\nLine Number \t: " + e.getLineNumber()
                            + "\nColumn Number\t: " + e.getColumnNumber() + "\n";
                    testContext.setAttribute("FailReport", failReport);


                    testResult = false;
                }
            } catch (IOException e) {
                //XML file is corrupted or malformed
                testContext.setAttribute("FailReport", "Error in reading or writing from the XML file " + url);

            } catch (SAXException e) {
                //Schema file (XSD) is corrupted or malformed
                testContext.setAttribute("FailReport", "Error in reading the XML Schema.");

            } finally {
                try {
                    is.close();
                    outStream.close();
                } catch (IOException e) {
                    testContext.setAttribute("FailReport", "Error in reading or writing from the XML file " + url);

                }
            }
        }

        Assert.assertTrue(testResult, url + " doesn't conform to the clause A.1 of ISO 19139.\n");

    }

    /**
     * Sets the application schema(s) to check. This method is intended only to
     * facilitate unit testing.
     * 
     * @param schemaURIs
     *            A Set of URI objects representing schema locations.
     */
    void setSchemaLocations(Set<URI> schemaURIs) {
        this.xsdLocations = schemaURIs;
    }
}
