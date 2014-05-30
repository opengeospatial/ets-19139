package org.opengis.cite.iso19139;

import com.occamlab.te.spi.executors.TestRunExecutor;
import com.occamlab.te.spi.executors.testng.TestNGExecutor;
import com.occamlab.te.spi.jaxrs.TestSuiteController;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.opengis.cite.iso19139.util.TestSuiteLogger;
import org.w3c.dom.Document;

/**
 * Main test run controller oversees execution of TestNG test suites.
 */
public class TestNGController implements TestSuiteController {

    private TestRunExecutor executor;
    private Properties etsProperties = new Properties();
    private static String xmlFileArg = "";

    /**
     * A convenience method to facilitate test development.
     *
     *            Test run arguments (optional). The first argument must refer
     *            to an XML properties file containing the expected set of test
     *            run arguments. If no argument is supplied, the file located at
     *            ${user.home}/test-run-props.xml will be used.
     * @throws Exception
     *             If the test run cannot be executed (usually due to
     *             unsatisfied pre-conditions).
     */
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputValidator inputValidator = new InputValidator();
        File xmlArgs = null;
        if (args.length > 0) {
            xmlArgs = (args[0].startsWith("file:")) ? new File(
                    URI.create(args[0])) : new File(args[0]);
            xmlFileArg = args[0].toString();
        } else {
            String homeDir = System.getProperty("user.home");
            xmlArgs = new File(homeDir, "ssh1");
            xmlFileArg = homeDir + "test-run-props.xml";
        }
        if (!xmlArgs.exists()) {
            throw new Exception(
                    "Test run arguments not found at " + xmlArgs);
        }
        Document testRunArgs = null;
        String str2 = xmlArgs.toString();
        if (inputValidator.extension(xmlArgs.toString(), ".xml") && inputValidator.pathValid(xmlArgs.toString())) {
            testRunArgs = db.parse(xmlArgs);
        } else if (!inputValidator.pathValid(xmlFileArg)) {
            throw new CustomException("The file given as an input does not exist.");
        } else {
            throw new CustomException("The file given as an input is not valid. The input file needs to be an XML file.");
        }
        TestNGController controller = new TestNGController();
        Source testResults = controller.doTestRun(testRunArgs);
        System.out.println("Test results: " + testResults.getSystemId());
    }

    /**
     * Default constructor uses the location given by the "user.home" system
     * property as the root output directory.
     */
    public TestNGController() {

        this(new File(System.getProperty("user.home")).toURI().toString());

    }

    /**
     * Construct a controller that writes results to the given output directory.
     * 
     * @param outputDirUri
     *            A file URI that specifies the location of the directory in
     *            which test results will be written. It will be created if it
     *            does not exist.
     */
    public TestNGController(String outputDirUri) {
        InputStream is = getClass().getResourceAsStream("ets.properties");
        String outputDir = outputDirUri;
        try {
            this.etsProperties.load(is);
        } catch (IOException ex) {
            TestSuiteLogger.log(Level.WARNING,
                    "Unable to load ets.properties. " + ex.getMessage());
        }
        URL tngSuite = TestNGController.class.getResource("testng.xml");
        File resultsDir = new File(URI.create(outputDirUri));
        TestSuiteLogger.log(Level.CONFIG, "Using TestNG config: " + tngSuite);
        TestSuiteLogger.log(Level.CONFIG,
                "Using outputDirPath: " + resultsDir.getAbsolutePath());
        // NOTE: setting third argument to 'true' enables the default listeners
        this.executor = new TestNGExecutor(tngSuite.toString(),
                resultsDir.getAbsolutePath(), true);
    }

    

    @Override
    public String getCode() {
        return etsProperties.getProperty("ets-code");
    }

    @Override
    public String getVersion() {
        return etsProperties.getProperty("ets-version");
    }

    @Override
    public String getTitle() {
        return etsProperties.getProperty("ets-title");
    }

    @Override
    public Source doTestRun(Document objTestRunArgs) throws Exception{
       
        Document testRunArgs=(Document)(objTestRunArgs) ;
//        try {
            validateTestRunArgs(testRunArgs);
//        } catch (Exception ex) {
//           
//             System.out.println("No XML(test run arguments) were supplied.");
//            System.exit(0);
//        }
        return executor.execute(testRunArgs);
       
        
    }



    /**
     * Validates the given set of test run arguments. The test run is aborted if
     * any checks fail.
     *
     * @param testRunArgs A DOM Document containing a set of XML properties
     * (key-value pairs).
     * @throws Exception If any arguments are missing or invalid for some
     * reason.
     */
    void validateTestRunArgs(Document testRunArgs) throws Exception {
        if (null == testRunArgs
                || testRunArgs.getElementsByTagName("entry").getLength() == 0) {
            throw new Exception("No test run arguments were supplied.");
        }
        XPath xpath = XPathFactory.newInstance().newXPath();
        Boolean hasIUTKey = (Boolean) xpath.evaluate(String.format(
                "//entry[@key='%s']", TestRunArg.IUT), testRunArgs,
                XPathConstants.BOOLEAN);
        Boolean hasSchemaKey = (Boolean) xpath.evaluate(
                String.format("//entry[@key='%s']", TestRunArg.XSD),
                testRunArgs, XPathConstants.BOOLEAN);
        InputValidator inputValid = new InputValidator();

        if (!hasIUTKey) {
            throw new Exception(
                    String.format("Missing argument: '%s' must be present.",
                    TestRunArg.IUT));
        }
    }
}
