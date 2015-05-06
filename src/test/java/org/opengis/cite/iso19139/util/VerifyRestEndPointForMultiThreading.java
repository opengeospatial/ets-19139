/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opengis.cite.iso19139.util;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

public class VerifyRestEndPointForMultiThreading {

  static final Logger LOGGER = Logger.getLogger("MY Log");
  private static final int MYTHREADS = 30;
  private static final String ETS_ROOT_PKG = "/org/opengis/cite/iso19139/";

  //Multithreading/Load Testing jUnit test method
  public VerifyRestEndPointForMultiThreading() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  // method for testing multithreading using junit.
  @Test
  public void multithreadingReporterOutputTest() throws JSONException {
    LOGGER.info("[TS] : Name of Test Case: multithreadingReporterOutputTest\n"
            + "      [TS] : Description: Test for the ISO 19139 using rest API when multiple URL use at a time.\n"
            + "      [TS] : Expected Result: Xml validation =  Mix Result\n");
    JSONArray testContentArray = new JSONArray();
    ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
    
//Store required xml and schematron file into jsonobject.
    JSONObject obj = new JSONObject();
    obj.put("uri", "http://hydro10.sdsc.edu/metadata/Raquel_Files/1E97BD2D-0FDD-4BAC-8DEA-FEB57AB53A6E.xml");
    obj.put("sch", this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/sch/checkScopeOfXmlFile.sch").toString());
    testContentArray.put(obj);
    obj = new JSONObject();
    obj.put("uri", this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/LangCodesIncorrect.xml").toString());
    obj.put("sch", this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/sch/checkAggregateInformation.sch").toString());
    testContentArray.put(obj);
    obj = new JSONObject();
    obj.put("uri", "http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/0070D26B-28CD-4512-91BB-43BB0E573441.xml");
    obj.put("sch", this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/sch/checkBandValueOfXml.sch").toString());
    testContentArray.put(obj);
    obj = new JSONObject();
    obj.put("uri", this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/MissingNameSpace_gmd.xml").toString());
    obj.put("sch", this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/sch/checkResponsiblePartyOfXml.sch").toString());
    testContentArray.put(obj);
    obj = new JSONObject();
    obj.put("uri", this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/MissingMDTransferOptions_noLinks.xml").toString());
    obj.put("sch", this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/sch/schematron-rules-iso-codeListValidation.sch").toString());
    testContentArray.put(obj);
    obj = new JSONObject();
    obj.put("uri", this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/DateTimeValueIncorrect.xml").toString());
    obj.put("sch", this.getClass().getResource(ETS_ROOT_PKG + "TestAssets/sch/checkExtentValueOfXml.sch").toString());
    testContentArray.put(obj);
    Random random = new Random();

    for (int i = 0; i < 10; i++) {
      int n = random.nextInt(testContentArray.length());
      String uri = testContentArray.getJSONObject(n).getString("uri");
      String sch = testContentArray.getJSONObject(n).getString("sch");
      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      Date date = new Date();
      System.out.println(dateFormat.format(date));
//use executer service for implementing multithreading.
      Runnable worker = new MyRunnable(uri, sch);
      executor.execute(worker);
    }
    executor.shutdownNow();
    // Wait until all threads are finish
    while (!executor.isTerminated()) {

    }
  }

  public static class MyRunnable implements Runnable {

    private final String uri;
    private final String sch;

    MyRunnable(String url, String sch) {
      this.uri = url;
      this.sch = sch;
    }

    @Override
    public void run() {
      URI uriRef = URI.create("http://localhost:8080/teamengine/rest/suites/iso19139/1.0/run?iut=" + uri + "&sch=" + sch);
      try {
        Document doc = URIUtils.parseURI(uriRef);
    //Checking the result.
        if ("0".equals(doc.getElementsByTagName("testng-results").item(0).getAttributes().getNamedItem("failed").getNodeValue()) && doc.getElementsByTagName("line").item(2).getTextContent().contains("LangCodesIncorrect.xml")) {
          System.out.println("Final Result: Pass\n");
        } else if ("0".equals(doc.getElementsByTagName("testng-results").item(0).getAttributes().getNamedItem("failed").getNodeValue()) && doc.getElementsByTagName("line").item(2).getTextContent().contains("MissingMDTransferOptions_noLinks.xml")) {
          System.out.println("Final Result: Pass\n");
        } else if ("1".equals(doc.getElementsByTagName("testng-results").item(0).getAttributes().getNamedItem("failed").getNodeValue()) && doc.getElementsByTagName("line").item(2).getTextContent().contains(uri)) {
          System.out.println("Final Result: Fail\n");
        } else if ("1".equals(doc.getElementsByTagName("testng-results").item(0).getAttributes().getNamedItem("failed").getNodeValue()) && doc.getElementsByTagName("line").item(2).getTextContent().contains("DateTimeValueIncorrect.xml")) {
          System.out.println("Final Result: Fail\n");
        } else if ("3".equals(doc.getElementsByTagName("testng-results").item(0).getAttributes().getNamedItem("failed").getNodeValue()) && doc.getElementsByTagName("line").item(2).getTextContent().contains("MissingNameSpace_gmd.xml")) {
          System.out.println("Final Result: Fail\n");
        } else if ("0".equals(doc.getElementsByTagName("testng-results").item(0).getAttributes().getNamedItem("failed").getNodeValue()) && doc.getElementsByTagName("line").item(2).getTextContent().contains(uri)) {
          System.out.println("Final Result: Pass\n");
        } else {
          System.out.println("Final Result: NA\n");
        }
      } catch (Exception e) {
        System.out.println("Final Result: " + e.getLocalizedMessage());
      }

    }
  }
}
