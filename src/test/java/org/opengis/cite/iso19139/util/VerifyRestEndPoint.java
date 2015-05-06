/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opengis.cite.iso19139.util;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

//testing Single thread rest end point using junit.
public class VerifyRestEndPoint {

  static final Logger LOGGER = Logger.getLogger("MY Log");
  private static final String ETS_ROOT_PKG = "/org/opengis/cite/iso19139/";

  public VerifyRestEndPoint() {
  }

  @BeforeClass
  public static void setUpClass() {
  }
  
//Test the code when test fail
  @Test
  public void restEndPointTestWhenTestFail() throws IOException, Exception {
    LOGGER.info("[TS] : Name of Test Case: restEndPointTestWhenTestFail\n"
            + "      [TS] : Description: Test for the ISO 19139 using rest API.\n"
            + "      [TS] : Expected Result: Xml validation =  Fail\n");
    String uri = "http://hydro10.sdsc.edu/metadata/Raquel_Files/1E97BD2D-0FDD-4BAC-8DEA-FEB57AB53A6E.xml";
    String sch = this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/sch/checkScopeOfXmlFile.sch").toString();
    URI uriRef = URI.create("http://localhost:8080/teamengine/rest/suites/iso19139/1.0/run?iut=" + uri + "&sch=" + sch);
    try {
      Document doc = URIUtils.parseURI(uriRef);
      if ("1".equals(doc.getElementsByTagName("testng-results").item(0).getAttributes().getNamedItem("failed").getNodeValue()) && doc.getElementsByTagName("line").item(2).getTextContent().contains("http://hydro10.sdsc.edu/metadata/Raquel_Files/1E97BD2D-0FDD-4BAC-8DEA-FEB57AB53A6E.xml")) {
        System.out.println("Final Result: Fail\n");
      }
    } catch (Exception e) {
      System.out.println("Final Result: " + e.getLocalizedMessage() + "\n");
    }
  }

  //Test the code when test pass.
  @Test
  public void restEndPointTestWhenTestPass() throws IOException, Exception {
    LOGGER.info("[TS] : Name of Test Case: restEndPointTestWhenTestPass\n"
            + "      [TS] : Description: Test for the ISO 19139 using rest API.\n"
            + "      [TS] : Expected Result: Xml validation =  Pass\n");
    String uri = this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/LangCodesIncorrect.xml").toString();
    String sch = this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/sch/checkAggregateInformation.sch").toString();
    URI uriRef = URI.create("http://localhost:8080/teamengine/rest/suites/iso19139/1.0/run?iut=" + uri + "&sch=" + sch);
    try {
      Document doc = URIUtils.parseURI(uriRef);
      if ("0".equals(doc.getElementsByTagName("testng-results").item(0).getAttributes().getNamedItem("failed").getNodeValue()) && doc.getElementsByTagName("line").item(2).getTextContent().contains("LangCodesIncorrect.xml")) {
        System.out.println("Final Result: Pass\n");
      }
    } catch (Exception e) {
      System.out.println("Final Result: " + e.getLocalizedMessage());
    }
  }

  //Test the code when url is not accessible.
  @Test
  public void restEndPointTestWhenURLWrong() throws IOException, Exception {
    LOGGER.info("[TS] : Name of Test Case: restEndPointTestWhenURLWrong\n"
            + "      [TS] : Description: Test for the ISO 19139 using rest API when URL wrong.\n"
            + "      [TS] : Expected Result: Xml validation =  Fail\n");
    String uri = "http://hydro10.sdsc.edu/metadata/Raque/1E97BD2D-0FDD-4BAC-8DEA-FEB57AB53A6E.xml";
    String sch = this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/sch/checkAggregateInformation.sch").toString();
    URI uriRef = URI.create("http://localhost:8080/teamengine/rest/suites/iso19139/1.0/run?iut=" + uri + "&sch=" + sch);
    try {
      Document doc = URIUtils.parseURI(uriRef);
      if ("0".equals(doc.getElementsByTagName("testng-results").item(0).getAttributes().getNamedItem("failed").getNodeValue()) && doc.getElementsByTagName("line").item(2).getTextContent().contains("LangCodesIncorrect.xml")) {
        System.out.println("Final Result: Pass\n");
      }
    } catch (Exception e) {
      System.out.println("Final Result: " + e.getLocalizedMessage());
    }
  }
}
