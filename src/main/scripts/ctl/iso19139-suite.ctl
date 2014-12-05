<?xml version="1.0" encoding="UTF-8"?>
<ctl:package xmlns:ctl="http://www.occamlab.com/ctl"
             xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
             xmlns:tns="http://www.opengis.net/cite/iso19139"
             xmlns:saxon="http://saxon.sf.net/"
             xmlns:tec="java:com.occamlab.te.TECore"
             xmlns:tng="java:org.opengis.cite.iso19139.TestNGController">

  <ctl:function name="tns:run-${ets-code}">
    <ctl:param name="testRunArgs">A Document node containing test run arguments (as XML properties).</ctl:param>
    <ctl:param name="outputDir">The directory in which the test results will be written.</ctl:param>
    <ctl:return>The test results as a Source object (root node).</ctl:return>
    <ctl:description>Runs the ISO 19139 (${ets-code}-${version}) test suite.</ctl:description>
    <ctl:code>
      <xsl:variable name="controller" select="tng:new($outputDir)" />
      <xsl:copy-of select="tng:doTestRun($controller, $testRunArgs)" />
    </ctl:code>
  </ctl:function>

  <ctl:suite name="tns:${ets-code}-${version}">
    <ctl:title>ISO 19139</ctl:title>
    <ctl:description>Describe scope of testing.</ctl:description>
    <ctl:starting-test>tns:Main</ctl:starting-test>
  </ctl:suite>
 
  <ctl:test name="tns:Main">
    <ctl:assertion>The test subject satisfies all applicable constraints.</ctl:assertion>
    <ctl:code>
      <xsl:variable name="form-data">
        <ctl:form method="POST" width="800" height="600" xmlns="http://www.w3.org/1999/xhtml">
          <h2>ISO 19139</h2>
          <div style="background:#F0F8FF" bgcolor="#F0F8FF">
            <p>This facility validates metadata instances that conform to ISO 19139</p>
            <ul>
              <li>
                <a href="http://54.209.245.204/teamengine/rest/suites/iso19139/1.0/" target="_blank">ISO 19139's Overview</a>
              </li>
              <li>
                <a href="http://hydro10.sdsc.edu/metadata/Raquel_Files/" target="_blank">Lists of XML</a> that conform to ISO 19139 standards.</li>
            </ul>
            <p>Two conformance levels can be tested:</p>
            <ul>
              <li>Conformance Level 1 (Schema)</li>
              <li>Conformance Level 2 (Constraints)</li>
            </ul>
          </div>
          <fieldset style="background:#ccffff">
            <legend style="font-family: sans-serif; color: #000099; 
			                 background-color:#F0F8FF; border-style: solid; 
                       border-width: medium; padding:4px">Implementation under test</legend>
            <p>
              <label for="uri">
                <h4 style="margin-bottom: 0.5em">Please provide the URL of the file to be tested:</h4>
              </label>
              <input id="uri" name="uri" size="128" type="text" value="http://hydro10.sdsc.edu/metadata/ScienceBase_WAF_dump/018F7983-78AA-4368-989A-B84F4FEB36D9.xml" />
            </p>
            <p>
              <label for="doc">
                <h4 style="margin-bottom: 0.5em">Upload file to be tested:</h4>
              </label>
              <input name="doc" id="doc" size="128" type="file" />
            </p>
            <div id="toggleLevelThree" style="display: none;">
              <p>
                <label for="XSDuri">
                  <h4 style="margin-bottom: 0.5em">Please provide the Schematron URL of the file to be tested:</h4>
                </label>
                <input id="XSDuri" name="XSDuri" size="128" type="text" />
              </p>
              <p>
                <label for="XSDdoc">
                  <h4 style="margin-bottom: 0.5em">Upload Schematron file to be tested:</h4>
                </label>
                <input name="XSDdoc" id="XSDdoc" size="128" type="file" />
              </p>
            </div>      
            <p> Sselect the Conformance Class to be tested:
              <label for="level">Conformance class: </label>
              <input id="level-1" type="radio" name="level" value="1" checked="checked" onclick="document.getElementById('toggleLevelThree').style.display='none'" />
              <label for="level-1"> Level 1 | </label>
              <input id="level-2" type="radio" name="level" value="2" onclick="document.getElementById('toggleLevelThree').style.display='none'"/>
              <label class="form-label" for="level-2"> Level 1 + Level 2</label>
              <input id="level-3" type="radio" name="level" value="3" onclick="document.getElementById('toggleLevelThree').style.display='block'"/>
              <label class="form-label1" for="level-3"> Level 1 + Level 2+Level 3</label>
            </p>
          </fieldset>
          <p>
            <input class="form-button" type="submit" value="Start"/> | 
            <input class="form-button" type="reset" value="Clear"/>
          </p>
        </ctl:form>
      </xsl:variable>
      <xsl:variable name="iut-file" select="$form-data//value[@key='doc']/ctl:file-entry/@full-path" />
      <xsl:variable name="sch-file" select="$form-data//value[@key='XSDdoc']/ctl:file-entry/@full-path" />
      <xsl:variable name="test-run-props">
        <properties version="1.0">
          <entry key="iut">
            <xsl:choose>
              <xsl:when test="empty($iut-file)">
                <xsl:value-of select="$form-data/values/value[@key='uri']"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:copy-of select="concat('file:///', $iut-file)" />
              </xsl:otherwise>
            </xsl:choose>
          </entry>
          <entry key="sch">
            <xsl:choose>
              <xsl:when test="empty($sch-file)">
                <xsl:value-of select="$form-data/values/value[@key='XSDuri']"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:copy-of select="concat('file:///', $sch-file)" />
              </xsl:otherwise>
            </xsl:choose>
          </entry>
          <entry key="ics">
            <xsl:value-of select="$form-data/values/value[@key='level']"/>
          </entry>
        </properties>
      </xsl:variable>
      <xsl:variable name="testRunDir">
        <xsl:value-of select="tec:getTestRunDirectory($te:core)"/>
      </xsl:variable>
      <xsl:variable name="test-results">
        <ctl:call-function name="tns:run-${ets-code}">
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
            Test method <xsl:value-of select="./@name"/>: expected [true] but found [false]
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
