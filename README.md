# Test-Suite for geographic metadata 19139

This Test-Suite provides the Executable Test Script (ETS) to test implementations against the following specification(s):

- [ISO19139](http://www.iso.org/iso/home/store/catalogue_tc/catalogue_detail.htm?csnumber=32557)


Detailed information about this test suite is available [here] (http://htmlpreview.github.com/?https://github.com/opengeospatial/ets-19139/blob/master/src/main/javadoc/overview.html).

## License

[Apache 2.0 License](LICENSE.md)

## Validating Metadata Instances

These are the following ways to validate an instance.

   1. Via a web user interface
   2. Via HTTP request
   3. Via console

### Validating via a Web User Interface
The web site is available here:
   http://cite-dev-03.opengeospatial.org/teamengine/

It requires an easy registrationa and login  process.

###  Validating via an HTTP Request


- URL: http://cite-dev-03.opengeospatial.org/teamengine/rest/suites/iso19139/1.0/run
- Parameters can be:
-
      iut = Instance (or implementation) under test (Mandatory)

      ics = An implementation conformance statement that indicates which conformance classes or options are supported.
      
      sch = A URI that refers to the schematron and when ics=3 then sch is mandatory.

      NOTE: Ampersand ('&') characters must be percent-encoded as '%26'

For example:

[http://cite-dev-03.opengeospatial.org/teamengine/rest/suites/iso19139/1.0/run?iut=http://hydro10.sdsc.edu/metadata/Raquel_Files/37E28B7A-0406-449B-8A45-3988AE675368.xml](http://cite-dev-03.opengeospatial.org/teamengine/rest/suites/iso19139/1.0/run?iut=http://hydro10.sdsc.edu/metadata/Raquel_Files/37E28B7A-0406-449B-8A45-3988AE675368.xml)

Example with sch:

[http://cite-dev-03.opengeospatial.org/teamengine/rest/suites/iso19139/1.0/run?iut=http://hydro10.sdsc.edu/metadata/Raquel_Files/1E97BD2D-0FDD-4BAC-8DEA-FEB57AB53A6E.xml&sch=http://cite-dev-03.opengeospatial.org/teamengine/checkScopeOfXmlFile.sch](http://cite-dev-03.opengeospatial.org/teamengine/rest/suites/iso19139/1.0/run?iut=http://hydro10.sdsc.edu/metadata/Raquel_Files/1E97BD2D-0FDD-4BAC-8DEA-FEB57AB53A6E.xml&sch=http://cite-dev-03.opengeospatial.org/teamengine/checkScopeOfXmlFile.sch)

     In GET Request : iut and sch are URI's

     In POST Request : iut and sch are keys of the files attached in the POST Body

### To test via GET API :
 
   Using the GET API you can send the URL's as query parameters. For ics=2 (i.e. Conformance level 2) iut (i.e. Metadata URL) is mandatory.
   
   For ics=3 (i.e. Conformance level 3) iut (i.e. Metadata URL) is mandatory and sch (i.e. Schematron URL) also needs to be passed otherwise the confomrance 3 tests will fail.

    curl -sS 'http://teamengineProjectURI/rest/suites/testName/1.0/run?iut=www.exampleURL/Metadata.xml&sch=www.exampleURL/Schematron.sch&ics=3'
  
### To test via POST API :
   
   A POST request by default take ics=3  (i.e. Conformance level 3)
   
   Whenever a user wants to test a Metadata file against a given Schematron (both given as a input by the user) with the help of the Teamengine's REST POST API where both the Metadata and Schematron is present in the Local file system then he needs to send these files via POST body instead of query parameters(as shown in the example below):

      curl -X POST --header "Content-Type:multipart/form-data" -F "iut=@path/to/XML" -F "sch=@path/to/Schematorn" http://teamengineProjectURI/rest/suites/testName/1.0/run


In the above example path/to/XML is the path to the Local Metadata file and path/to/Schematorn is the path to the Local Schematron file.

### Results are given in XML TestNG:

```xml

   <?xml version="1.0" encoding="UTF-8"?>
      <testng-results failed="0" passed="3" skipped="0" total="3">
         <reporter-output>
            <line>The result of the test is-</line>
            <line>Passed tests for suite 'iso19139-1.0' is:3</line>
            <line>Failed tests for suite 'iso19139-1.0' is:0</line>
            <line>Skipped tests for suite 'iso19139-1.0' is:0</line>
            <line>REASON:</line>
            <line>http://hydro10.sdsc.edu/metadata/Raquel_Files/37E28B7A-0406-449B-8A45-3988AE675368.xml conforms to the clause A.1 of ISO 19139.</line>
        </reporter-output>
    </testng-results>
 
````

More detales at the [Overview Page](http://cite-dev-03.opengeospatial.org/teamengine/about/iso19139/1.0/web/overview.html)

## Extracting Failed Test Information

The following Snippet will help you parse the result(response) of the test and display title, description, and details of the failed tests, and shows you the XPath:

```xml
<xsl:variable name="failed-test">
      <service-requests>
          <xsl:copy-of select="doc({filepath})"/>
      </service-requests>
</xsl:variable>
<b><xsl:text>Test Name :</xsl:text></b><xsl:value-of select="string($failed-test//test-method[@status='FAIL']/@name)"/>
<br/>
<b><xsl:text>Test Discription :</xsl:text></b><xsl:value-of select="string($failed-test//test-method[@status='FAIL']/@description)"/>
<br/>   
<b><xsl:text>Test Detail :</xsl:text></b><xsl:value-of select="string($failed-test//test-method[@status='FAIL']/exception/message)"/>
````

JAVA Code Snippet:

Please find the JAVA code snippet to extract failed test information on the below given link
[https://github.com/opengeospatial/ets-19139/blob/master/src/main/javadoc/XmlParser.java](https://github.com/opengeospatial/ets-19139/blob/master/src/main/javadoc/XmlParser.java)
## Revisions

<b>r1</b>

Capabilities of conformance level 1

Minimum conformance with this Technical Specification requires that geographic metadata instance (XML) documents can be validated without error against the XML schemas. While many tools are available to test validation of XML instance documents against provided XML Schemas, it is important to understand that not all validation tools implement the full W3C XML Schema recommendation and not all validation tools interpret the W3C XML Schema recommendation in the same manner. It is recommended that a tool with strict interpretation of XML Schema and full support for the W3C XML Schema recommendation be used to ensure conformance.

<b>r2</b>

Capabilities of conformance level 2

   1. A.2.1 By-Value or By-Reference or gco:nilReason
   Validation of XML instance documents against the schemas described in this document is not all that is required for conformance to this specification. However, because of the design of the XCPT the property element may have no content or attributes, or it may have both content and attributes and still be XML Schema-valid. It is not possible to constrain the co-occurrence of content or attributes. Some mechanism in addition to an XML Schema validation (e.g., Schematron, XSL Transformations) must be used to restrict a property to be exclusively by-value, or by-reference, or expressing a NULL reason.
   
   2. A.2.2 Co-Constraints
   XML 1.0 does not support the enforcement of certain types of constraints. For example, co-constraints such as the requirement that an 'extent' in the form of an 'EX_GeographicBoundingBox' or 'EX_GeographicDescription' be used in the 'MD_DataIdentification' object when the 'hierarchyLevel' of 'MD_Metadata' is equal to "dataset" can not be enforced with an XML schema.

<b>r3</b>

Capabilities of conformance level 3:

This conformance level validates an XML instance documents against a schematron uploaded by the users. This revison lets users test the XML instance against conformance 1 and 2 along with conformance 3 to validate the XML agsint the technical specifications as well as any other rules that are defined in the user entered schematron.

<b>r4</b>
Changes made in TeamEngine-ServiceProvider for Conformance Level 3:
Added REST API Handler in Teamengine Service Provider Module to handle POST requests where both Metadata and Schematron are sent as files in post body.

## Building

This test is build using [Apache Maven](http://maven.apache.org/) To 
build the test suite run maven from the root directory.
   % mvn install
     
To test an application run:
    % mvn test
    
More information about how to build and install the test in TEAM Engine at the [TEAM Engine project site](https://github.com/opengeospatial/teamengine/tree/master/src/site)

## Bugs

Issue tracker is available at [github](https://github.com/opengeospatial/ets-19139/issues)

## Mailing Lists

The [cite-forum](http://cite.opengeospatial.org/forum) is where software developers discuss issues and solutions related to OGC tests. 

## More Information

Visit the [CITE website](http://cite.opengeospatial.org/) to get more information about the CITE program and tools.

