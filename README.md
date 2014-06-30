# Test-Suite for geographic metadata 19139

This Test-Suite provides the Executable Test Script (ETS) to test implementations against the following specification(s):

- [ISO19139](http://www.iso.org/iso/home/store/catalogue_tc/catalogue_detail.htm?csnumber=32557)


Detailed information about this test suite is available [here] (src/main/site/index.html).

## License

[Apache 2.0 License](LICENSE.md)

## Using
The test can be tested at this endpoint:
http://cite-dev-03.opengeospatial.org/teamengine/about/iso19139/1.0/web/overview.html

### User interface
Login in to TEAM Engine will allow to use the user interface and send the results via a web form

### HTTP 

URL: [http://localhost:8080/teamengine/rest/suites/iso19139/1.0/run](http://localhost:8080/teamengine/rest/suites/iso19139/1.0/run)
Parameter is iut = Instance (or implementation) under test

For example:

[http://localhost:8080/teamengine/rest/suites/iso19139/1.0/run?iut=http://hydro10.sdsc.edu/metadata/Raquel_Files/37E28B7A-0406-449B-8A45-3988AE675368.xml](http://localhost:8080/teamengine/rest/suites/iso19139/1.0/run?iut=http://hydro10.sdsc.edu/metadata/Raquel_Files/37E28B7A-0406-449B-8A45-3988AE675368.xml)

Results are given in XML TestNG:

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

