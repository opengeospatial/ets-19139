<?xml version="1.0" encoding="UTF-8"?>
<ctl:package xmlns:ctl="http://www.occamlab.com/ctl"
             xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
             xmlns:tns="http://www.opengis.net/cite/iso19139"
             xmlns:saxon="http://saxon.sf.net/"
             xmlns:tec="java:com.occamlab.te.TECore"
             xmlns:tng="java:org.opengis.cite.iso19139.TestNGController">

    <ctl:function name="tns:run-ets-${ets-code}">
        <ctl:param name="testRunArgs">A Document node containing test run arguments (as XML properties).</ctl:param>
        <ctl:param name="outputDir">The directory in which the test results will be written.</ctl:param>
        <ctl:return>The test results as a Source object (root node).</ctl:return>
        <ctl:description>Runs the ISO 19139 (${ets-code}-${version}) test suite.</ctl:description>
        <ctl:code>
            <xsl:variable name="controller" select="tng:new($outputDir)" />
            <xsl:copy-of select="tng:doTestRun($controller, $testRunArgs)" />
        </ctl:code>
    </ctl:function>

    <ctl:suite name="tns:ets-${ets-code}-${version}">
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
                        <p>The implementation under test (IUT) is checked against ISO 19139 schema.</p>
                        <ul>
                            <li>
                                <a href="file:///home/upendra/TE_BASE/scripts/iso19139/1.0/web/overview.html">ISO 19139's Overview</a> : More detail information about ISO19139. 
                                 </li>
                            <li>
                                <a href="http://hydro10.sdsc.edu/metadata/Raquel_Files/">Lists of XML</a> that conform to ISO 19139 standards</li>
                        </ul>
                        <p>Two conformance levels are defined:</p>
                        <ul>
                            <li>Level 1</li>
                            <li>Level 2</li>
                        </ul>
                    </div>
                    <fieldset style="background:#ccffff">
                        <legend style="font-family: sans-serif; color: #000099; 
			                 background-color:#F0F8FF; border-style: solid; 
                       border-width: medium; padding:4px">Implementation under test</legend>
                        <p>
                            <label for="uri">
                                <h4 style="margin-bottom: 0.5em">Location of IUT (absolute http: or file: URI)</h4>
                            </label>
                            <input id="uri" name="uri" size="128" type="text" value="http://hydro10.sdsc.edu/metadata/Raquel_Files/178F9F70-E056-4A06-B86E-C255471F99BD.xml" />
                        </p>
                        <p>
                            <label for="doc">
                                <h4 style="margin-bottom: 0.5em">Upload IUT</h4>
                            </label>
                            <input name="doc" id="doc" size="128" type="file" />
                        </p>
                        <p>
                            <label for="level">Conformance class: </label>
                            <input id="level-1" type="radio" name="level" value="1" checked="checked" />
                            <label for="level-1"> Level 1 | </label>
                            <input id="level-2" type="radio" name="level" value="2" />
                            <label class="form-label" for="level-2"> Level 2</label>
                        </p>
                    </fieldset>
                    <p>
                        <input class="form-button" type="submit" value="Start"/> | 
                        <input class="form-button" type="reset" value="Clear"/>
                    </p>
                </ctl:form>
            </xsl:variable>
            <xsl:variable name="iut-file" select="$form-data//value[@key='doc']/ctl:file-entry/@full-path" />
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
                    <entry key="ics">
                        <xsl:value-of select="$form-data/values/value[@key='level']"/>
                    </entry>
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
