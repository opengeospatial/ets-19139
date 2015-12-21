<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml" version="1.0">
    <xsl:output encoding="UTF-8" indent="yes" method="xml" standalone="no" omit-xml-declaration="no"/>
    <xsl:template match="testng-results">
        <html>
            <body>
                <h3>
                    <font color="black"> Test Name: <xsl:value-of select="suite/@name"/>
                        <br/> Time: <xsl:value-of select="suite/@finished-at"/>
                        <br/> Test Artifact: <xsl:value-of
                            select="/testng-results/reporter-output[1]/line[3]"/>
                        <br/> Result: <xsl:value-of
                            select="/testng-results/reporter-output[1]/line[2]"/>
                    </font>
                </h3>
                <table border="1">
                    <tr>
                        <td>Color Legend </td>
                        <td bgcolor="#B2F0D1"> Pass </td>
                        <td bgcolor="#FFB2B2">Fail</td>
                        <td bgcolor="#CCCCCE">Skip</td>
                    </tr>
                </table>
                <xsl:for-each select="suite[1]/test">
                    <xsl:call-template name="test-overview"/>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>

    <xsl:template name="test-overview">

        <h2>
            <xsl:value-of select="@name"/>
        </h2>
        <xsl:for-each select=".">
            <xsl:variable name="pass"
                select="count(class/test-method[@status  = 'PASS' and not(@is-config='true')])"/>
            <xsl:variable name="fail"
                select="count(class/test-method[@status  = 'FAIL' and not(@is-config='true')])"/>
            <xsl:variable name="skip"
                select="count(class/test-method[@status  = 'SKIP' and not(@is-config='true')])"/>
            <p>
                <table border="1">
                    <tr>
                        <td bgcolor="#B2F0D1">Pass: <xsl:value-of select="$pass"/></td>
                        <td bgcolor="#FFB2B2">Fail: <xsl:value-of select="$fail"/></td>
                        <td bgcolor="#CCCCCE">Skip: <xsl:value-of select="$skip"/></td>
                    </tr>
                </table>
            </p>
        </xsl:for-each>


        <xsl:call-template name="test"/>


    </xsl:template>
    <xsl:template name="test">


        <table border="1">
            <tbody>
                <tr>
                    <th>Name</th>

                    <th>Reason</th>
                </tr>



                <xsl:for-each select="class/test-method">
                    <xsl:if test="not(@is-config='true')">
                        <xsl:if test="@status='PASS'">
                            <tr bgcolor="#B2F0D1">
                                <td>
                                    <xsl:value-of select="@name"/>
                                </td>

                                <td/>
                            </tr>
                        </xsl:if>
                        <xsl:if test="@status='FAIL'">
                            <tr bgcolor="#FFB2B2">
                                <td>
                                    <xsl:value-of select="@name"/>
                                </td>

                                <td>
                                    <xsl:if test="@status='FAIL'">
                                        <xsl:variable name="message">
                                              <xsl:choose>
                        			<xsl:when test="substring-before(exception/message,'expected [')">
                           				<xsl:value-of select="substring-before(exception/message,'expected [')"/>
                        			</xsl:when>
                        		<xsl:otherwise>
                             			<xsl:value-of select="exception/message"/>
                        		</xsl:otherwise>
					</xsl:choose>
                                        </xsl:variable>
                                        <xsl:call-template name="split">
            					<xsl:with-param name="str" select="$message"/>
        				</xsl:call-template>
                                    </xsl:if>
                                </td>
                            </tr>
                        </xsl:if>
                        <xsl:if test="@status='SKIP'">
                            <tr bgcolor="#CCCCCE">
                                <td>
                                    <xsl:value-of select="@name"/>
                                </td>

                                <td/>
                            </tr>
                        </xsl:if>
                    </xsl:if>
                </xsl:for-each>

            </tbody>

        </table>


    </xsl:template>

 	<xsl:template match="text()" name="split">
    <xsl:param name="str" select="."/>
    <xsl:if test="string-length($str)">
      <xsl:value-of select="substring-before(concat($str,','),',')"/>
      <xsl:if test="not($str=.)">
        <br />
      </xsl:if>
      <xsl:call-template name="split">
        <xsl:with-param name="str" select="substring-after($str, ',')"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
