/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opengis.cite.iso19139.level1;

import java.net.URI;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.logging.Logger;
import org.apache.xerces.xs.XSModel;
import org.opengis.cite.iso19139.SuiteAttribute;
import org.opengis.cite.iso19139.InputValidator;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import javax.xml.validation.Schema;
import org.opengis.cite.iso19139.TestRunArg;
import org.testng.Assert;
import org.xml.sax.SAXException;

/**
 * Includes tests that validate whether the entered XML file conforms to the
 * clause A.1 of ISO 19139.
 */
/**
 *
 * @author tanvishah
 */
public class ValidateInputTest {

    private Document testSubject;
    /**
     * Timeout for compiling schemas /ms
     */
    static final long COMPILE_TIMEOUT = 180000;
    private final Logger logr = Logger.getLogger(this.getClass().getPackage().getName());
    private static final String ETS_ROOT_PKG = "/org/opengis/cite/iso19139/";
    private Set<URI> xsdLocations;
    private Schema appSchema;
    private XSModel model;
    private URI targetNamespace;

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
     * Obtains the schema locations from the ISuite context. The suite attribute
     * {@link SuiteAttribute#SCHEMA_LOC_SET} should evaluate to a Set of URI
     * objects specifying the locations of the relevant XML Schema grammars.
     *
     * @param testContext The test (group) context.
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
     * Validates whether the given input file is an XML file (based on its
     * extension OR file content)
     */
    @Test(groups = {"inputvalidation"}, description = "This function validates XML file", dependsOnMethods = "validateXMLPath")
    public void validateXMLFile(ITestContext testContext) throws SAXException,
            IOException {
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String url = params.get(TestRunArg.IUT.toString());
        InputValidator iv = new InputValidator();
        boolean hasValidExt = iv.extension(url, ".xml");
        if (hasValidExt) {
            System.out.println("RESULT:\n" + url + " is an XML file.\n\n");
        } else {
            testContext.setAttribute("FailReport", "The file path or URL given as the input '" + url + "' does not point to an XML.");
        }
        Assert.assertTrue(hasValidExt, "File extension of the given input is not valid.");

    }

    /**
     * Validates whether the given input file exists or not
     */
    @Test(groups = {"inputvalidation"}, description = "This function validates XML file Path")
    public void validateXMLPath(ITestContext testContext) throws SAXException,
            IOException {
        testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
        Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
        String url = params.get(TestRunArg.IUT.toString());
        boolean hasValidPath = false;
        if (url.startsWith("http:")) {
            URL urlValidator = new URL(url);
            HttpURLConnection http;
            http = (HttpURLConnection) urlValidator.openConnection();
            int statusCode = http.getResponseCode();
            if (statusCode == 200) {
                System.out.println("\nRESULT:\n" + url + " is given as input does exists.\n\n");
                hasValidPath = true;
            } else {
                testContext.setAttribute("FailReport", "The URL given as the input '" + url + "' is not a valid URL or it responding.");
            }
            Assert.assertTrue(hasValidPath, "File path of the given input is not valid.");
        } else {
            InputValidator iv = new InputValidator();
            hasValidPath = iv.pathValid(url);
            if (hasValidPath) {
                System.out.println("\nRESULT:\n" + url + " is given as input does exists.\n\n");
            } else {
                testContext.setAttribute("FailReport", "The uploaded file or the file whose path is given as the input '" + url + "' is not present.");
            }

            Assert.assertTrue(hasValidPath, "File path of the given input is not valid.");
        }

    }
}
