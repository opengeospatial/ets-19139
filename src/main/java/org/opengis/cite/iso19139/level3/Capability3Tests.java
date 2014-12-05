package org.opengis.cite.iso19139.level3;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.*;
import org.opengis.cite.iso19139.ETSAssert;
import org.opengis.cite.iso19139.Namespaces;
import org.opengis.cite.iso19139.SuiteAttribute;
import org.opengis.cite.iso19139.TestRunArg;
import org.opengis.cite.iso19139.util.TestSuiteLogger;
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
public class Capability3Tests {

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
    System.out.println("CONFORMANCE LEVEL 3 :");
    System.out.println();
    Object obj = testContext.getSuite().getAttribute(
            SuiteAttribute.TEST_SUBJECT.getName());
    if ((null != obj) && Document.class.isAssignableFrom(obj.getClass())) {
      this.testSubject = Document.class.cast(obj);
    }
  }

  /**
   * Sets the test subject. This method is intended to facilitate unit testing.
   *
   * @param testSubject A Document node representing the test subject or
   * metadata about it.
   */
  public void setTestSubject(Document testSubject) {
    this.testSubject = testSubject;
  }

  @BeforeTest
  public void validateConfromanceLevelThreeEnabled(ITestContext testContext) throws IOException {
    Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
    int level = Integer.parseInt(params.get(TestRunArg.ICS.toString()));
    if (level > 2) {
      Assert.assertTrue(true);
    } else {
      Assert.assertTrue(false, "Conformance level three is not enabled");
    }
  }

  /**
   * Attempts to construct a Schematron validator from a schema reference given
   * in (a) the ISO data file, or (b) a test run argument (in the ISuite
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

  /**
   * Attempts to construct a local XML file.
   *
   * @param testContext The test set context.
   * @return
   */
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
   * [{@code Test}] Checks for the presence of any Encoding error on ISO
   * elements. A warning is issued for each occurrence.
   *
   * @param testContext
   * @throws java.net.MalformedURLException
   * @see "ISO 19136, Annex I: Backwards compatibility with earlier versions of
   * ISO"
   */
  @Test(description = "Implements ATC 3")
  public void validateXmlAgainstUserDefinedSchematron(ITestContext testContext) throws MalformedURLException {
    testContext.getSuite().setAttribute(SuiteAttribute.SCHEMA.getName(), "");
    Map<String, String> params = testContext.getSuite().getXmlSuite().getParameters();
    String url = params.get(TestRunArg.IUT.toString());
    String schematron = params.get(TestRunArg.SCH.toString());
    //check the schematron file is valid or not.
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      db.parse(schematron);
    } catch (Exception e) {
      Assert.assertTrue(false, "Inserted Schematron fIle is not a Valid XML file");
    }
    //Check xml file against schematron file.
    URL schRef = new URL(schematron);
    File dataFile = localFileCreation(url);
    String description = "Validate Xml Against User Defined Schematron=XML Validation against user define schematron.";
    ETSAssert
            .assertSchematronValid(schRef, new StreamSource(dataFile), description);

  }

  /**
   * Indicates whether or not the given PI data includes a Schematron schema
   * reference.
   *
   * @param piData A Map containing PI data (pseudo-attributes).
   * @return {@code true} if the "schematypens" pseudo-attribute has the value
   * {@value org.opengis.cite.iso19136.Namespaces#SCH}; {@code false} otherwise;
   */
  boolean isSchematronReference(Map<String, String> piData
  ) {
    if (null != piData && null != piData.get("schematypens")) {
      return piData.get("schematypens").equals(Namespaces.SCH);
    }
    return false;
  }

  /**
   * Extracts the data items from the {@code xml-model} processing instruction.
   * The PI must appear before the document element.
   *
   * @param dataFile A File containing the ISO instance.
   * @return A Map containing the supplied pseudo-attributes, or {@code null} if
   * the PI is not present.
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
