<?xml version="1.0" encoding="UTF-8"?>
<ctl:package xmlns:ctl="http://www.occamlab.com/ctl"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:tns="http://www.opengis.net/cite/iso19136"
  xmlns:saxon="http://saxon.sf.net/"
  xmlns:tec="java:com.occamlab.te.TECore"
  xmlns:tng="java:org.opengis.cite.iso19136.TestNGController">

  <ctl:function name="tns:run-ets-${ets-code}">
    <ctl:param name="testRunArgs">A Document node containing test run arguments (as XML properties).</ctl:param>
    <ctl:param name="outputDir">The directory in which the test results will be written.</ctl:param>
    <ctl:return>The test results as a Source object (root node).</ctl:return>
    <ctl:description>Runs the GML 3.2 (${version}) test suite.</ctl:description>
    <ctl:code>
      <xsl:variable name="controller" select="tng:new($outputDir)" />
      <xsl:copy-of select="tng:doTestRun($controller, $testRunArgs)" />
    </ctl:code>
  </ctl:function>

  <ctl:suite name="tns:ets-${ets-code}-${version}">
    <ctl:title>GML 3.2 (ISO 19136:2007) Conformance Test Suite</ctl:title>
    <ctl:description>This executable test suite (ETS) validates GML application 
     schemas or data in accord with ISO 19136:2007.</ctl:description>
    <ctl:starting-test>tns:Main</ctl:starting-test>
  </ctl:suite>

  <ctl:test name="tns:Main">
    <ctl:assertion>The GML application schema or data set satisfies all 
      relevant constraints.</ctl:assertion>
    <ctl:code>
      <xsl:variable name="form-data">
        <ctl:form method="POST" width="800" height="600" xmlns="http://www.w3.org/1999/xhtml">
          <h2>GML 3.2.1 (ISO 19136:2007) Conformance Test Suite</h2>
          <div style="background:#F0F8FF" bgcolor="#F0F8FF">
            <p>The GML resource is checked against the following specifications:</p>
            <ul>
              <li><a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=32554" 
                   target="_blank">ISO 19136:2007</a>, Geographic information - 
                   Geography Markup Language (GML)</li>
              <li><a href="http://www.w3.org/TR/xmlschema-1/" target="_blank"> 
                    XML Schema Part 1: Structures</a>, Second Edition</li>
            </ul>
          </div>
          <fieldset style="background:#ccffff">
            <legend style="font-family: sans-serif; color: #000099; 
			   background-color:#F0F8FF; border-style: solid; border-width: medium; padding:4px">
			   GML resource (application schema or data set)</legend>
            <p>
              <label for="xsd-uri">
                <h4 style="margin-bottom: 0.5em">Location of GML application schema (http: or file: URI)</h4>
              </label>
              <input id="xsd-uri" name="xsd-uri" size="128" type="text" value="" />
            </p>
            <p>
              <label for="gml-uri">
                <h4 style="margin-bottom: 0.5em">Location of GML document (http: or file: URI)</h4>
              </label>
              <input id="gml-uri" name="gml-uri" size="128" type="text" value="" />
            </p>
            <p>
              <label for="gml-doc">
                <h4 style="margin-bottom: 0.5em">Upload GML document</h4>
              </label>
              <input name="gml-doc" size="128" type="file" />
            </p>
            <p>
              <label class="form-label" for="sch-uri">
                <h4 style="margin-bottom: 0.5em">Location of Schematron schema defining supplementary constraints 
                (http: or file: URI)</h4>
              </label>
              <input id="sch-uri" name="sch-uri" size="128" type="text" value="" />
            </p>
          </fieldset>
          <p>
            <input class="form-button" type="submit" value="Start"/> | 
            <input class="form-button" type="reset" value="Clear"/>
          </p>
        </ctl:form>
      </xsl:variable>
      <xsl:variable name="gml-file" select="$form-data//value[@key='gml-doc']/ctl:file-entry/@full-path" />
      <xsl:variable name="test-run-props">
        <properties version="1.0">
          <entry key="xsd"><xsl:value-of select="$form-data/values/value[@key='xsd-uri']"/></entry>
          <entry key="gml">
            <xsl:choose>
              <xsl:when test="empty($gml-file)">
                <xsl:value-of select="$form-data/values/value[@key='gml-uri']"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:copy-of select="concat('file:///', $gml-file)" />
              </xsl:otherwise>
            </xsl:choose>
          </entry>
          <entry key="sch"><xsl:value-of select="$form-data/values/value[@key='sch-uri']"/></entry>
        </properties>
      </xsl:variable>
      <xsl:variable name="testRunDir">
        <xsl:value-of select="tec:getTestRunDirectory($te:core)"/>
      </xsl:variable>
      <xsl:variable name="test-results">
        <ctl:call-function name="tns:run-ets-${ets-code}">
          <ctl:with-param name="testRunArgs" select="$test-run-props"/>
          <ctl:with-param name="outputDir" select="$testRunDir" />
        </ctl:call-function>
      </xsl:variable>
      <xsl:call-template name="tns:testng-report">
        <xsl:with-param name="results" select="$test-results" />
        <xsl:with-param name="outputDir" select="$testRunDir" />
      </xsl:call-template>
      <xsl:variable name="summary-xsl" select="tec:findXMLResource($te:core, '/testng-summary.xsl')" />
      <ctl:message>
        <xsl:value-of select="saxon:transform(saxon:compile-stylesheet($summary-xsl), $test-results)"/>
See detailed test report in the TE_BASE/users/<xsl:value-of 
select="concat(substring-after($testRunDir, 'users/'), '/html/')" /> directory.
      </ctl:message>
      <xsl:if test="xs:integer($test-results/testng-results/@failed) gt 0">
        <xsl:for-each select="$test-results//test-method[@status='FAIL' and not(@is-config='true')]">
          <ctl:message>
Test method <xsl:value-of select="./@name"/>: <xsl:value-of select=".//message"/>
          </ctl:message>
        </xsl:for-each>
        <ctl:fail/>
      </xsl:if>
    </ctl:code>
  </ctl:test>

  <xsl:template name="tns:testng-report">
    <xsl:param name="results" />
    <xsl:param name="outputDir" />
    <xsl:variable name="stylesheet" select="tec:findXMLResource($te:core, '/testng-report.xsl')" />
    <xsl:variable name="reporter" select="saxon:compile-stylesheet($stylesheet)" />
    <xsl:variable name="report-params" as="node()*">
      <xsl:element name="testNgXslt.outputDir">
        <xsl:value-of select="concat($outputDir, '/html')" />
      </xsl:element>
    </xsl:variable>
    <xsl:copy-of select="saxon:transform($reporter, $results, $report-params)" />
  </xsl:template>
</ctl:package>
