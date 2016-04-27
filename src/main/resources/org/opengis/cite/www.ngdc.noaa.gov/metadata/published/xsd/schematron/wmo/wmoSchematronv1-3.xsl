<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<axsl:stylesheet xmlns:axsl="http://www.w3.org/1999/XSL/Transform" xmlns:sch="http://www.ascc.net/xml/schematron" xmlns:iso="http://purl.oclc.org/dsdl/schematron" xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmx="http://www.isotc211.org/2005/gmx" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.0">
	<!--Implementers: please note that overriding process-prolog or process-root is the preferred method for meta-stylesheets to use where possible. -->
<axsl:param name="archiveDirParameter"/><axsl:param name="archiveNameParameter"/><axsl:param name="fileNameParameter"/><axsl:param name="fileDirParameter"/>

<!--PHASES-->


<!--PROLOG-->
<axsl:output xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" xmlns:svrl="http://purl.oclc.org/dsdl/svrl" method="xml" omit-xml-declaration="no" standalone="yes" indent="yes"/>
	<html xmlns="http://www.w3.org/1999/xhtml">
		<body>
			<p>Schematron validation is used to ensure WMO metadata complies with the WMO profile.  The validation checks included in this schematron are documented on 
				page 35 of the <a href="http://wis.wmo.int/2010/metadata/version_1-2/WMO%20Core%20Metadata%20Profile%20v1-2%20Guidance%20Documentation%20v0.1%20%28DRAFT%29.pdf">WMO Core Metadata Profile.</a>
				This WMO schematron version is based on the recommendations presented in WMO Core Metadata Profile v0.1.  This schematron will be updated to coincide with the TT-ApMD Expert Panel recommendations
				when they become available. Visit the <a href="https://geo-ide.noaa.gov/wiki/index.php?title=WMO_Metadata_Validation">WMO Metadata Validation</a> wiki page for 
				additional more information about validating with Schematron. 
			</p>
			<p>This report is produced using this <a href="http://www.ngdc.noaa.gov/metadata/published/xsl/wmoSchematronValidation.xsl">stylesheet.</a>. Please contact <a href="mailto:john.kozimor@noaa.gov">John Kozimor
			</a> if you have questions or suggestions.
			</p>
			<hr style="border-style:solid;border-bottom-color:blue;"/>
		</body>
	</html>

<!--KEYS-->


<!--DEFAULT RULES-->
	
<!--MODE: SCHEMATRON-SELECT-FULL-PATH-->
<!--This mode can be used to generate an ugly though full XPath for locators-->
<axsl:template match="*" mode="schematron-select-full-path"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:template>

<!--MODE: SCHEMATRON-FULL-PATH-->
<!--This mode can be used to generate an ugly though full XPath for locators-->
<axsl:template match="*" mode="schematron-get-full-path"><axsl:apply-templates select="parent::*" mode="schematron-get-full-path"/><axsl:text>/</axsl:text><axsl:choose><axsl:when test="namespace-uri()=''"><axsl:value-of select="name()"/><axsl:variable name="p_1" select="1+    count(preceding-sibling::*[name()=name(current())])"/><axsl:if test="$p_1&gt;1 or following-sibling::*[name()=name(current())]">[<axsl:value-of select="$p_1"/>]</axsl:if></axsl:when><axsl:otherwise><axsl:text>*[local-name()='</axsl:text><axsl:value-of select="local-name()"/><axsl:text>' and namespace-uri()='</axsl:text><axsl:value-of select="namespace-uri()"/><axsl:text>']</axsl:text><axsl:variable name="p_2" select="1+   count(preceding-sibling::*[local-name()=local-name(current())])"/><axsl:if test="$p_2&gt;1 or following-sibling::*[local-name()=local-name(current())]">[<axsl:value-of select="$p_2"/>]</axsl:if></axsl:otherwise></axsl:choose></axsl:template><axsl:template match="@*" mode="schematron-get-full-path"><axsl:text>/</axsl:text><axsl:choose><axsl:when test="namespace-uri()=''">@<axsl:value-of select="name()"/></axsl:when><axsl:otherwise><axsl:text>@*[local-name()='</axsl:text><axsl:value-of select="local-name()"/><axsl:text>' and namespace-uri()='</axsl:text><axsl:value-of select="namespace-uri()"/><axsl:text>']</axsl:text></axsl:otherwise></axsl:choose></axsl:template>

<!--MODE: SCHEMATRON-FULL-PATH-2-->
<!--This mode can be used to generate prefixed XPath for humans-->
<axsl:template match="node() | @*" mode="schematron-get-full-path-2"><axsl:for-each select="ancestor-or-self::*"><axsl:text>/</axsl:text><axsl:value-of select="name(.)"/><axsl:if test="preceding-sibling::*[name(.)=name(current())]"><axsl:text>[</axsl:text><axsl:value-of select="count(preceding-sibling::*[name(.)=name(current())])+1"/><axsl:text>]</axsl:text></axsl:if></axsl:for-each><axsl:if test="not(self::*)"><axsl:text/>/@<axsl:value-of select="name(.)"/></axsl:if></axsl:template>

<!--MODE: GENERATE-ID-FROM-PATH -->
<axsl:template match="/" mode="generate-id-from-path"/><axsl:template match="text()" mode="generate-id-from-path"><axsl:apply-templates select="parent::*" mode="generate-id-from-path"/><axsl:value-of select="concat('.text-', 1+count(preceding-sibling::text()), '-')"/></axsl:template><axsl:template match="comment()" mode="generate-id-from-path"><axsl:apply-templates select="parent::*" mode="generate-id-from-path"/><axsl:value-of select="concat('.comment-', 1+count(preceding-sibling::comment()), '-')"/></axsl:template><axsl:template match="processing-instruction()" mode="generate-id-from-path"><axsl:apply-templates select="parent::*" mode="generate-id-from-path"/><axsl:value-of select="concat('.processing-instruction-', 1+count(preceding-sibling::processing-instruction()), '-')"/></axsl:template><axsl:template match="@*" mode="generate-id-from-path"><axsl:apply-templates select="parent::*" mode="generate-id-from-path"/><axsl:value-of select="concat('.@', name())"/></axsl:template><axsl:template match="*" mode="generate-id-from-path" priority="-0.5"><axsl:apply-templates select="parent::*" mode="generate-id-from-path"/><axsl:text>.</axsl:text><axsl:value-of select="concat('.',name(),'-',1+count(preceding-sibling::*[name()=name(current())]),'-')"/></axsl:template><!--MODE: SCHEMATRON-FULL-PATH-3-->
<!--This mode can be used to generate prefixed XPath for humans 
	(Top-level element has index)-->
<axsl:template match="node() | @*" mode="schematron-get-full-path-3"><axsl:for-each select="ancestor-or-self::*"><axsl:text>/</axsl:text><axsl:value-of select="name(.)"/><axsl:if test="parent::*"><axsl:text>[</axsl:text><axsl:value-of select="count(preceding-sibling::*[name(.)=name(current())])+1"/><axsl:text>]</axsl:text></axsl:if></axsl:for-each><axsl:if test="not(self::*)"><axsl:text/>/@<axsl:value-of select="name(.)"/></axsl:if></axsl:template>

<!--MODE: GENERATE-ID-2 -->
<axsl:template match="/" mode="generate-id-2">U</axsl:template><axsl:template match="*" mode="generate-id-2" priority="2"><axsl:text>U</axsl:text><axsl:number level="multiple" count="*"/></axsl:template><axsl:template match="node()" mode="generate-id-2"><axsl:text>U.</axsl:text><axsl:number level="multiple" count="*"/><axsl:text>n</axsl:text><axsl:number count="node()"/></axsl:template><axsl:template match="@*" mode="generate-id-2"><axsl:text>U.</axsl:text><axsl:number level="multiple" count="*"/><axsl:text>_</axsl:text><axsl:value-of select="string-length(local-name(.))"/><axsl:text>_</axsl:text><axsl:value-of select="translate(name(),':','.')"/></axsl:template><!--Strip characters--><axsl:template match="text()" priority="-1"/>

<!--SCHEMA METADATA-->
<axsl:template match="/">
	<svrl:schematron-output xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" title="Requirements by WMO Core Metadata Profile v1.3" schemaVersion="2013-04-23">
	<axsl:comment>
		 <axsl:value-of select="$archiveDirParameter"/>   
		 <axsl:value-of select="$archiveNameParameter"/>  
		 <axsl:value-of select="$fileNameParameter"/>  
		 <axsl:value-of select="$fileDirParameter"/>
    </axsl:comment>
	<svrl:ns-prefix-in-attribute-values uri="http://www.isotc211.org/2005/gmd" prefix="gmd"/>
	<svrl:ns-prefix-in-attribute-values uri="http://www.isotc211.org/2005/gco" prefix="gco"/>
	<svrl:ns-prefix-in-attribute-values uri="http://www.isotc211.org/2005/gmx" prefix="gmx"/>
		<svrl:ns-prefix-in-attribute-values uri="http://www.opengis.net/gml/3.2" prefix="gml"/>
		<svrl:ns-prefix-in-attribute-values uri="http://www.w3.org/1999/xlink" prefix="xlink"/>
		<!--<html xmlns="http://www.w3.org/1999/xhtml">
			<body>
				<head>
					<link type="text/css" href="http://metadata/published/xsd/schematron/schematronOutputWMO.css"></link>
				</head>
				<p>This report is produced using this <a
					href="https://toyoda-eizi.net/2013/0415gawsis/wcmp13-sch.xsl"
					>stylesheet.</a>. Please contact <a href="mailto:toyoda.eizi@gmail.com">Eizi Toyoda
					</a> if you have questions or suggestions.
				</p>
				<hr style="border-style:solid;border-bottom-color:blue;"/>
			</body>
		</html>-->
		<svrl:active-pattern><axsl:attribute name="id">iso19139rules</axsl:attribute><axsl:attribute name="name">iso19139rules</axsl:attribute><axsl:apply-templates/></svrl:active-pattern><axsl:apply-templates select="/" mode="M6"/><svrl:active-pattern><axsl:attribute name="id">wcmpbase</axsl:attribute><axsl:attribute name="name">wcmpbase</axsl:attribute><axsl:apply-templates/></svrl:active-pattern><axsl:apply-templates select="/" mode="M7"/>
</svrl:schematron-output></axsl:template>

<!--SCHEMATRON PATTERNS-->
<svrl:text xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron">Requirements by WMO Core Metadata Profile v1.3</svrl:text>
	
<!--PATTERN iso19139rules-->


	<!--RULE -->
<axsl:template match="gmd:*[contains('abcdefghijklmnopqrstuvwxyz',   substring(local-name(),1,1))]" priority="1016" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:*[contains('abcdefghijklmnopqrstuvwxyz',   substring(local-name(),1,1))]"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="(string-length(*)&gt;0) or @xlink:href or @uuidref or @gco:nilReason"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="(string-length(*)&gt;0) or @xlink:href or @uuidref or @gco:nilReason"><axsl:attribute name="id">ISO19139.A21.nilReason</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>XML Class Type must have value either by content, by uuid reference or by xlink reference, otherwise use gco:nilReason to document why the value is missing.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template><axsl:variable name="hierarchyLevel" select="substring-before(normalize-space(concat( //gmd:MD_Metadata/gmd:hierarchyLevel/*/@codeListValue, ' ', translate(normalize-space(//gmd:MD_Metadata/gmd:hierarchyLevel/*), ' ', '_'), ' ', 'dataset --dummy--')), ' ')"/>

	<!--RULE -->
<axsl:template match="gmd:MD_Metadata" priority="1014" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:MD_Metadata"/>

		<!--REPORT -->
<axsl:if test="true()"><svrl:successful-report xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="true()"><axsl:attribute name="id">print.fileIdentifier</axsl:attribute><axsl:attribute name="flag">I-NONE</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text><axsl:text/><axsl:value-of select="gmd:fileIdentifier"/><axsl:text/></svrl:text></svrl:successful-report></axsl:if>

		<!--REPORT -->
<axsl:if test="not(gmd:language)"><svrl:successful-report xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="not(gmd:language)"><axsl:attribute name="id">ISO19139.TA1.language</axsl:attribute><axsl:attribute name="flag">C-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>MD_Metadata.language MUST be documented unless defined by the encoding standard.</svrl:text></svrl:successful-report></axsl:if>

		<!--REPORT -->
<axsl:if test="not(gmd:characterSet)"><svrl:successful-report xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="not(gmd:characterSet)"><axsl:attribute name="id">ISO19139.TA1.mdCharset</axsl:attribute><axsl:attribute name="flag">C-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>MD_Metadata.characterSet MUST be documented if ISO/IEC 10646 not used and not defined by the encoding standard.</svrl:text></svrl:successful-report></axsl:if><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:MD_DataIdentification" priority="1013" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:MD_DataIdentification"/>

		<!--REPORT -->
<axsl:if test="not(gmd:characterSet)"><svrl:successful-report xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="not(gmd:characterSet)"><axsl:attribute name="id">ISO19139.TA1.dsCharset</axsl:attribute><axsl:attribute name="flag">C-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>MD_DataIdentification.characterSet MUST be documented if ISO/IEC 10646 not used.</svrl:text></svrl:successful-report></axsl:if>

		<!--ASSERT -->
<axsl:choose><axsl:when test="($hierarchyLevel != 'dataset') or (count( gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicBoundingBox) + count( gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicDescription) &gt; 0)"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="($hierarchyLevel != 'dataset') or (count( gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicBoundingBox) + count( gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicDescription) &gt; 0)"><axsl:attribute name="id">ISO19139.TA1.geographic</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>When hierarchyLevel is dataset, either EX_GeographicBoundingBox or EX_GeographicDescription MUST be documented.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="($hierarchyLevel != 'dataset') or gmd:topicCategory"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="($hierarchyLevel != 'dataset') or gmd:topicCategory"><axsl:attribute name="id">ISO19139.TA1.topicCategory</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>topicCategory is mandatory unless hierarchyLevel is dataset</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:MD_AggregateInformation" priority="1012" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:MD_AggregateInformation"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="gmd:aggregateDataSetName or gmd:aggregateDataSetIdentifier"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="gmd:aggregateDataSetName or gmd:aggregateDataSetIdentifier"><axsl:attribute name="id">ISO19139.TA1.aggregateDatasetName</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>Either aggregateDataSetName or aggregateDataSetIdentifier must be
documented</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:MD_LegalConstraints" priority="1011" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:MD_LegalConstraints"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="gmd:otherConstraints or not(       gmd:accessConstraints/*/@codeListValue='otherRestrictions' or       gmd:accessConstraints/*/text()='otherRestrictions' or       gmd:useConstraints/*/@codeListValue='otherRestrictions' or       gmd:useConstraints/*/text()='otherRestrictions'     )"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="gmd:otherConstraints or not( gmd:accessConstraints/*/@codeListValue='otherRestrictions' or gmd:accessConstraints/*/text()='otherRestrictions' or gmd:useConstraints/*/@codeListValue='otherRestrictions' or gmd:useConstraints/*/text()='otherRestrictions' )"><axsl:attribute name="id">ISO19139.TA1.otherConstraints</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>otherConstraints must be documented if accessConstraints or useConstraints = "otherRestrictions"</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:DQ_DataQuality" priority="1010" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:DQ_DataQuality"/><axsl:variable name="level" select="substring-before(normalize-space(concat(       gmd:scope/gmd:DQ_Scope/gmd:level/*/@codeListValue, ' ',       translate(normalize-space(gmd:scope/gmd:DQ_Scope/gmd:level/*), ' ', '_'),       ' --dummy--'     )), ' ')"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="($level != 'dataset') or (gmd:report or gmd:lineage)"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="($level != 'dataset') or (gmd:report or gmd:lineage)"><axsl:attribute name="id">ISO19139.TA1.dqReportLineage</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>"report" or "lineage" role is mandatory if scope.DQ_Scope.level = 'dataset'</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:DQ_Scope" priority="1009" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:DQ_Scope"/><axsl:variable name="level" select="substring-before(normalize-space(concat(       gmd:level/*/@codeListValue, ' ',       translate(normalize-space(gmd:level/*), ' ', '_'),       ' --dummy--'     )), ' ')"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="gmd:levelDescription or ($level = 'dataset' or $level = 'series')"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="gmd:levelDescription or ($level = 'dataset' or $level = 'series')"><axsl:attribute name="id">ISO19139.TA1.levelDescription</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>"levelDescription" is mandatory if "level" notEqual 'dataset' or 'series'</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:LI_Lineage" priority="1008" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:LI_Lineage"/><axsl:variable name="level" select="substring-before(normalize-space(concat(       ../../gmd:scope/gmd:DQ_Scope/gmd:level/*/@codeListValue, ' ',       translate(normalize-space(../../gmd:scope/gmd:DQ_Scope/gmd:level/*),       ' ', '_'),       ' --dummy--'     )), ' ')"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="($level != 'dataset' and $level != 'series') or       (gmd:source or gmd:processStep) or gmd:statement"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="($level != 'dataset' and $level != 'series') or (gmd:source or gmd:processStep) or gmd:statement"><axsl:attribute name="id">ISO19139.TA1.lineageStatement</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>If(count(source) + count(processStep) =0) and (DQ_DataQuality.scope.level = 'dataset' or 'series') then statement is mandatory</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="gmd:source or gmd:processStep or gmd:statement"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="gmd:source or gmd:processStep or gmd:statement"><axsl:attribute name="id">ISO19139.TA1.lineageContent</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>At least one of gmd:source, gmd:processStep or gmd:statement must be documented.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:LI_Source" priority="1007" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:LI_Source"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="gmd:description or gmd:sourceExtent"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="gmd:description or gmd:sourceExtent"><axsl:attribute name="id">ISO19139.TA1.LI_Source</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>Either description or sourceExtent must be documented</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:MD_Georectified" priority="1006" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:MD_Georectified"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="(gmd:checkPointAvailability = '0' or gmd:checkPointAvailability = 'false') or gmd:checkPointDescription"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="(gmd:checkPointAvailability = '0' or gmd:checkPointAvailability = 'false') or gmd:checkPointDescription"><axsl:attribute name="id">ISO19139.TA1.MD_Georectified</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>"checkPointDescription" is mandatory if "checkPointAvailability" = 1</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:MD_Band" priority="1005" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:MD_Band"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="not(gmd:maxValue or gmd:minValue) or gmd:units"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="not(gmd:maxValue or gmd:minValue) or gmd:units"><axsl:attribute name="id">ISO19139.TA1.bandUnits</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>"units" is mandatory if "maxValue" or "minValue" are provided</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:MD_Distribution" priority="1004" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:MD_Distribution"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="gmd:distributionFormat or gmd:distributor/*/gmd:distributorFormat"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="gmd:distributionFormat or gmd:distributor/*/gmd:distributorFormat"><axsl:attribute name="id">ISO19139.TA1.distributionFormat</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>count (distributionFormat + distributorFormat) &gt; 0</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:MD_ExtendedElementInformation" priority="1003" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:MD_ExtendedElementInformation"/><axsl:variable name="dataType" select="substring-before(normalize-space(concat(       gmd:dataType/*/@codeListValue, ' ',       translate(normalize-space(gmd:dataType/*), ' ', '_'),       ' --dummy--'     )), ' ')"/><axsl:variable name="obligation" select="substring-before(normalize-space(concat(       gmd:obligation/*/@codeListValue, ' ',       translate(normalize-space(gmd:obligation/*), ' ', '_'),       ' --dummy--'     )), ' ')"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="$dataType = 'codelist' or $dataType = 'enumeration' or       $dataType = 'codelistElement' or (gmd:obligation and       gmd:maximumOccurence and gmd:domainValue)"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="$dataType = 'codelist' or $dataType = 'enumeration' or $dataType = 'codelistElement' or (gmd:obligation and gmd:maximumOccurence and gmd:domainValue)"><axsl:attribute name="id">ISO19139.TA1.extObligation</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>if "dataType" notEqual 'codelist', 'enumeration' or 'codelistElement' then "obligation", "maximumOccurence" and "domainValue" are mandatory</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="$obligation != 'conditional' or gmd:condition"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="$obligation != 'conditional' or gmd:condition"><axsl:attribute name="id">ISO19139.TA1.extCondition</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>if "obligation" = 'conditional' then "condition" is mandatory</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="$dataType != 'codeListElement' or gmd:domainCode"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="$dataType != 'codeListElement' or gmd:domainCode"><axsl:attribute name="id">ISO19139.TA1.extDomainCode</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>if "dataType" = 'codelistElement' then "domainCode" is mandatory</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="$dataType = 'codelistElement' or gmd:shortName"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="$dataType = 'codelistElement' or gmd:shortName"><axsl:attribute name="id">ISO19139.TA1.extShortName</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>if "dataType" notEqual 'codelistElement' then "shortName" is mandatory</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:EX_Extent" priority="1002" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:EX_Extent"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="gmd:description or gmd:geographicElement or gmd:temporalElement or gmd:verticalElement"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="gmd:description or gmd:geographicElement or gmd:temporalElement or gmd:verticalElement"><axsl:attribute name="id">ISO19139.TA1.extent</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>count(description + geographicElement + temporalElement + verticalElement) &gt; 0</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:CI_ResponsibleParty" priority="1001" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:CI_ResponsibleParty"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="gmd:individualName or gmd:organisationName or gmd:positionName"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="gmd:individualName or gmd:organisationName or gmd:positionName"><axsl:attribute name="id">ISO19139.TA1.responsibleParty:</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>count of (individualName + organisationName + positionName) &gt; 0</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:EX_GeographicBoundingBox" priority="1000" mode="M6"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:EX_GeographicBoundingBox"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="(-180.0 &lt;= number(gmd:westBoundLongitude)) and     (number(gmd:westBoundLongitude) &lt;= 180.0)"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="(-180.0 &lt;= number(gmd:westBoundLongitude)) and (number(gmd:westBoundLongitude) &lt;= 180.0)"><axsl:attribute name="id">ISO19115.B.R344</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>-180,0 &lt;= West Bounding Longitude value &lt;= 180,0</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="(-180.0 &lt;= number(gmd:eastBoundLongitude)) and     (number(gmd:eastBoundLongitude) &lt;= 180.0)"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="(-180.0 &lt;= number(gmd:eastBoundLongitude)) and (number(gmd:eastBoundLongitude) &lt;= 180.0)"><axsl:attribute name="id">ISO19115.B.R345</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>-180,0 &lt;= East Bounding Longitude value &lt;= 180,0</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="(-90.0 &lt;= number(gmd:southBoundLatitude)) and     (number(gmd:southBoundLatitude) &lt;= 90.0)"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="(-90.0 &lt;= number(gmd:southBoundLatitude)) and (number(gmd:southBoundLatitude) &lt;= 90.0)"><axsl:attribute name="id">ISO19115.B.R346</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>-90,0 &lt;= South Bounding Latitude value {<axsl:text/><axsl:value-of select="number(gmd:southBoundLatitude)"/><axsl:text/>} &lt;= 90,0</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="(-90.0 &lt;= number(gmd:northBoundLatitude)) and     (number(gmd:northBoundLatitude) &lt;= 90.0)"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="(-90.0 &lt;= number(gmd:northBoundLatitude)) and (number(gmd:northBoundLatitude) &lt;= 90.0)"><axsl:attribute name="id">ISO19115.B.R347a</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>-90,0 &lt;= North Bounding Latitude value {<axsl:text/><axsl:value-of select="number(gmd:northBoundLatitude)"/><axsl:text/>} &lt;= 90,0</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="number(gmd:southBoundLatitude) &lt;= number(gmd:northBoundLatitude)"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="number(gmd:southBoundLatitude) &lt;= number(gmd:northBoundLatitude)"><axsl:attribute name="id">ISO19115.B.R347b</axsl:attribute><axsl:attribute name="flag">M-ISO</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>South Bounding Latitude value {<axsl:text/><axsl:value-of select="number(gmd:southBoundLatitude)"/><axsl:text/>} &lt;= North Bounding Latitude value {<axsl:text/><axsl:value-of select="number(gmd:northBoundLatitude)"/><axsl:text/>}</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template><axsl:template match="text()" priority="-1" mode="M6"/><axsl:template match="@*|node()" priority="-2" mode="M6"><axsl:apply-templates select="@*|*" mode="M6"/></axsl:template>

<!--PATTERN wcmpbase-->


	<!--RULE -->
<axsl:template match="gmd:MD_Metadata" priority="1012" mode="M7"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:MD_Metadata"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="true()"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="true()"><axsl:attribute name="id">WCMP13.ISO-TS-19139-2007-xml-schema-validation</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>6.1.1 Each WIS Discovery Metadata record shall validate without error against the XML schemas defined in ISO/TS 19139:2007.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="true()"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="true()"><axsl:attribute name="id">WCMP13.ISO-TS-19139-2007-rule-based-validation</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>6.1.2 Each WIS Discovery Metadata record shall validate without error against the rule-based constraints listed in ISO/TS 19139:2007 Annex A (Table A.1).</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="not(.//*[name()=local-name()])"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="not(.//*[name()=local-name()])"><axsl:attribute name="id">WCMP13.explicit-xml-namespace-identification</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>6.2.1 Each WIS Discovery Metadata record shall name explicitly all namespaces used within the record; use of default namespaces is prohibited.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="not(.//*[namespace-uri() = 'http://www.opengis.net/gml'])"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="not(.//*[namespace-uri() = 'http://www.opengis.net/gml'])"><axsl:attribute name="id">WCMP13.gml-namespace-specification</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>6.3.1 Each WIS Discovery Metadata record shall declare the following XML namespace for GML: http://www.opengis.net/gml/3.2.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--REPORT -->
<axsl:if test="(string(gmd:metadataStandardName/*) != 'WMO Core Metadata Profile of ISO 19115 (WMO Core), 2003/Cor.1:2006 (ISO 19115), 2007 (ISO/TS 19139)') or (string(gmd:metadataStandardVersion/*) != '1.3') "><svrl:successful-report xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="(string(gmd:metadataStandardName/*) != 'WMO Core Metadata Profile of ISO 19115 (WMO Core), 2003/Cor.1:2006 (ISO 19115), 2007 (ISO/TS 19139)') or (string(gmd:metadataStandardVersion/*) != '1.3')"><axsl:attribute name="id">WCMP.metadataStandard</axsl:attribute><axsl:attribute name="flag">N-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>a WIS Discovery Metadata record may declare compliance with WCMP by
metadataStandardName = "WMO Core Metadata Profile of ISO 19115 (WMO Core), 2003/Cor.1:2006 (ISO 19115), 2007 (ISO/TS 19139)" (currently "<axsl:text/><axsl:value-of select="string(gmd:metadataStandardName/*)"/><axsl:text/>") and
metadataStandardVersion = "1.3" (currently "<axsl:text/><axsl:value-of select="string(gmd:metadataStandardVersion/*)"/><axsl:text/>").</svrl:text></svrl:successful-report></axsl:if>

		<!--ASSERT -->
<axsl:choose><axsl:when test="count(gmd:fileIdentifier) = 1"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="count(gmd:fileIdentifier) = 1"><axsl:attribute name="id">WCMP13.fileIdentifier-cardinality</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>8.1.1 Each WIS Discovery Metadata record shall include one gmd:MD_Metadata/gmd:fileIdentifier attribute</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="starts-with(gmd:fileIdentifier/*/text(), 'urn:x-wmo:md:')"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="starts-with(gmd:fileIdentifier/*/text(), 'urn:x-wmo:md:')"><axsl:attribute name="id">WCMP.fileIdentifier-style</axsl:attribute><axsl:attribute name="flag">R-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>[§8.1 ¶5] WMO Core Metadata Profile recommends the use of a URI structure for gmd:fileIdentifier attributes.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="gmd:identificationInfo/*/gmd:descriptiveKeywords[       (*/gmd:thesaurusName/*/gmd:title/* = 'WMO_CategoryCode') or       (*/gmd:thesaurusName/*/gmd:title/*/@xlink:href =         'http://wis.wmo.int/2012/codelists/WMOCodeLists.xml#WMO_CategoryCode')       ]     "/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="gmd:identificationInfo/*/gmd:descriptiveKeywords[ (*/gmd:thesaurusName/*/gmd:title/* = 'WMO_CategoryCode') or (*/gmd:thesaurusName/*/gmd:title/*/@xlink:href = 'http://wis.wmo.int/2012/codelists/WMOCodeLists.xml#WMO_CategoryCode') ]"><axsl:attribute name="id">WCMP13.WMO_CategoryCode-keyword-cardinality</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>Requirement 8.2.1: Each WIS Discovery Metadata record shall include at least one keyword from the WMO_CategoryCode code list. [such descriptiveKeywords must be present]</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M7"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:descriptiveKeywords[     (*/gmd:thesaurusName/*/gmd:title/* = 'WMO_CategoryCode') or     (*/gmd:thesaurusName/*/gmd:title/*/@xlink:href =       'http://wis.wmo.int/2012/codelists/WMOCodeLists.xml#WMO_CategoryCode')   ]" priority="1011" mode="M7"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:descriptiveKeywords[     (*/gmd:thesaurusName/*/gmd:title/* = 'WMO_CategoryCode') or     (*/gmd:thesaurusName/*/gmd:title/*/@xlink:href =       'http://wis.wmo.int/2012/codelists/WMOCodeLists.xml#WMO_CategoryCode')   ]"/><axsl:variable name="kwlist" select="'!weatherObservations!weatherForecasts!meteorology!hydrology!climatology!landMeteorologyClimate!synopticMeteorology!marineMeteorology!agriculturalMeteorology!aerology!marineAerology!oceanography!landHydrology!rocketSounding!pollution!waterPollution!landWaterPollution!seaPollution!landPollution!airPollution!glaciology!actinometry!satelliteObservation!airplaneObservation!observationPlatform!atmosphericComposition!'"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="*/gmd:keyword[contains($kwlist,       concat('!',normalize-space(*/text()),'!'))]"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="*/gmd:keyword[contains($kwlist, concat('!',normalize-space(*/text()),'!'))]"><axsl:attribute name="id">WCMP13.WMO_CategoryCode-keyword-cardinality</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>Requirement 8.2.1: Each WIS Discovery Metadata record shall include at least one keyword from the WMO_CategoryCode code list. [keyword must be chosen from the list]</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="*/gmd:type/gmd:MD_KeywordTypeCode = 'theme'"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="*/gmd:type/gmd:MD_KeywordTypeCode = 'theme'"><axsl:attribute name="id">WCMP13.WMO_CategoryCode-keyword-theme</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>Requirement 8.2.2: Keywords from WMO_CategoryCode code list shall be defined as keyword type “theme”.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M7"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:descriptiveKeywords" priority="1010" mode="M7"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:descriptiveKeywords"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="count(*/gmd:thesaurusName/*/gmd:title) &lt;= 1"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="count(*/gmd:thesaurusName/*/gmd:title) &lt;= 1"><axsl:attribute name="id">WCMP13.keyword-grouping</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>Requirement 8.2.3: All keywords sourced from a particular keyword thesaurus shall be grouped into a single instance of the MD_Keywords class.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--REPORT -->
<axsl:if test="not(*/gmd:thesaurusName[*/gmd:title or @gco:nilReason])"><svrl:successful-report xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="not(*/gmd:thesaurusName[*/gmd:title or @gco:nilReason])"><axsl:attribute name="id">WCMP.keyword-grouping-without-thesaurus</axsl:attribute><axsl:attribute name="flag">N-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>It is beneficial to identify and document gmd:thesaurus for gmd:desciptiveKeywords.</svrl:text></svrl:successful-report></axsl:if><axsl:apply-templates select="@*|*" mode="M7"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:MD_Metadata" priority="1009" mode="M7"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:MD_Metadata"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="(gmd:hierarchyLevel/* = 'nonGeographicDataset') or     (gmd:identificationInfo/*/gmd:extent/*/gmd:geographicElement/      gmd:EX_GeographicBoundingBox)"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="(gmd:hierarchyLevel/* = 'nonGeographicDataset') or (gmd:identificationInfo/*/gmd:extent/*/gmd:geographicElement/ gmd:EX_GeographicBoundingBox)"><axsl:attribute name="id">WCMP13.geographic-bounding-box</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>Requirement 8.2.4: Each WIS Discovery Metadata record describing geographic
data shall include the description of at least one geographic bounding box defining the spatial extent of the data.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="(gmd:language/*/@codeListValue = 'en') or     (gmd:language/*/@codeListValue = 'eng') or      (gmd:locale/*/gmd:languageCode/*/@codeListValue = 'en') or      (gmd:locale/*/gmd:languageCode/*/@codeListValue = 'eng') "/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="(gmd:language/*/@codeListValue = 'en') or (gmd:language/*/@codeListValue = 'eng') or (gmd:locale/*/gmd:languageCode/*/@codeListValue = 'en') or (gmd:locale/*/gmd:languageCode/*/@codeListValue = 'eng')"><axsl:attribute name="id">WCMP.atLeastEnglish</axsl:attribute><axsl:attribute name="flag">R-proposed</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>Requirement 8.2.5: All information within a metadata record shall, as a minimum, be provided in English within the metadata record.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:variable name="global_exchange_data" select="starts-with(gmd:fileIdentifier/*/text(),         'urn:x-wmo:md:int.wmo.wis::')"/>

		<!--REPORT -->
<axsl:if test="$global_exchange_data"><svrl:successful-report xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="$global_exchange_data"><axsl:attribute name="id">WCMP13.fileIdentifier-for-globally-exchanged-data</axsl:attribute><axsl:attribute name="flag">N-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>fileIdentifier (<axsl:text/><axsl:value-of select="gmd:fileIdentifier"/><axsl:text/>) is considered as global distribution.
Requirement 9.2.1: A WIS Discovery Metadata record describing data for global exchange via the WIS shall have a gmd:MD_Metadata/gmd:fileIdentifier attribute formatted as follows (where {uid} is a unique identifier derived from the GTS bulletin or file name): urn:x-wmo:md:int.wmo.wis::{uid}.</svrl:text></svrl:successful-report></axsl:if>

		<!--ASSERT -->
<axsl:choose><axsl:when test="not($global_exchange_data) or     (gmd:identificationInfo/*/gmd:descriptiveKeywords/*[         ((gmd:thesaurusName/*/gmd:title/* = 'WMO_DistributionScopeCode') or  (gmd:thesairusName/*/gmd:title/*/@xlink:href = 'http://wis.wmo.int/2012/codelists/WMOCodeLists.xml#WMO_DistributionScopeCode')         )         and (gmd:type/* = 'dataCentre')  and (gmd:keyword = 'GlobalExchange')       ]     )"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="not($global_exchange_data) or (gmd:identificationInfo/*/gmd:descriptiveKeywords/*[ ((gmd:thesaurusName/*/gmd:title/* = 'WMO_DistributionScopeCode') or (gmd:thesairusName/*/gmd:title/*/@xlink:href = 'http://wis.wmo.int/2012/codelists/WMOCodeLists.xml#WMO_DistributionScopeCode') ) and (gmd:type/* = 'dataCentre') and (gmd:keyword = 'GlobalExchange') ] )"><axsl:attribute name="id">WCMP13.identification-of-globally-exchanged-data</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>Requirement 9.1.1: A WIS Discovery Metadata record describing data for global exchange via the WIS shall indicate the scope of distribution using the keyword “GlobalExchange” of type “dataCenterdataCentre” from thesaurus WMO_DistributionScopeCode.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="not($global_exchange_data) or     gmd:identificationInfo/*/gmd:resourceConstraints/*/gmd:otherConstraints/*[       contains('!WMOEssential!WMOAdditional!WMOOther!', concat('!', ., '!'))     ]"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="not($global_exchange_data) or gmd:identificationInfo/*/gmd:resourceConstraints/*/gmd:otherConstraints/*[ contains('!WMOEssential!WMOAdditional!WMOOther!', concat('!', ., '!')) ]"><axsl:attribute name="id">WCMP13.WMO-data-policy-for-globally-exchangeddata</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>Requirement 9.3.1: A WIS Discovery Metadata record describing data for global exchange via the WIS shall indicate the WMO Data License as Legal Constraint (type: “otherConstraints”) using one and only one term from the WMO_DataLicenseCode code list.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="count(     gmd:identificationInfo/*/gmd:resourceConstraints/*/gmd:otherConstraints/*[       contains('!WMOEssential!WMOAdditional!WMOOther!', concat('!', ., '!'))     ]) &lt;= 1"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="count( gmd:identificationInfo/*/gmd:resourceConstraints/*/gmd:otherConstraints/*[ contains('!WMOEssential!WMOAdditional!WMOOther!', concat('!', ., '!')) ]) &lt;= 1"><axsl:attribute name="id">WCMP.WMO-data-policy-disambiguity</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>[§9.3 ¶5] The presence of more than one WMO Data Policy statement in a single metadata record yields an ambiguous state; a WIS Discovery Metadata record describing data for global exchange shall declare only a single WMO Data Policy.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose>

		<!--ASSERT -->
<axsl:choose><axsl:when test="not($global_exchange_data) or     (contains('!GTSPriority1!GTSPriority2!GTSPriority3!GTSPriority4!',      concat('!', gmd:identificationInfo/*/gmd:resourceConstraints/*/      gmd:otherConstraints/*, '!'))     or (gmd:identificationInfo/*/gmd:resourceConstraints/*/      gmd:otherConstraints/*/@xlink:href      = 'http://wis.wmo.int/2012/codelists/WMOCodeLists.xml#WMO_GTSProductCategoryCode'     ))"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="not($global_exchange_data) or (contains('!GTSPriority1!GTSPriority2!GTSPriority3!GTSPriority4!', concat('!', gmd:identificationInfo/*/gmd:resourceConstraints/*/ gmd:otherConstraints/*, '!')) or (gmd:identificationInfo/*/gmd:resourceConstraints/*/ gmd:otherConstraints/*/@xlink:href = 'http://wis.wmo.int/2012/codelists/WMOCodeLists.xml#WMO_GTSProductCategoryCode' ))"><axsl:attribute name="id">WCMP13.GTS-priority-for-globally-exchanged-data</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>Requirement 9.3.2: A WIS Discovery Metadata record describing data for global exchange via the WIS shall indicate the GTS Priority as Legal Constraint (type: “otherConstraints”) using one and only one term from the WMO_GTSProductCategoryCode code list.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M7"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:distributionFormat" priority="1008" mode="M7"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:distributionFormat"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="not(*/gmd:formatDistributor)"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="not(*/gmd:formatDistributor)"><axsl:attribute name="id">proposed.recommendation-formatlooping</axsl:attribute><axsl:attribute name="flag">R-proposed</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>it is discouraged to describe formatDistributor in MD_Format as distributionFormat. That will cause looping.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M7"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:formatDistributor" priority="1007" mode="M7"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:formatDistributor"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="not(*/gmd:distributorFormat)"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="not(*/gmd:distributorFormat)"><axsl:attribute name="id">proposed.recommendation-formatlooping</axsl:attribute><axsl:attribute name="flag">R-proposed</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>it is discouraged to describe distributorFormat in MD_Distributor as formatDistributor. That will cause looping.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M7"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:CI_Citation" priority="1006" mode="M7"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:CI_Citation"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="not(gmd:identifier/*/gmd:authority)"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="not(gmd:identifier/*/gmd:authority)"><axsl:attribute name="id">proposed.recommendation-authorityloop</axsl:attribute><axsl:attribute name="flag">R-proposed</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>it is discouraged to describe MD_Identifier.authority in CI_Citation.identifier.  That will cause looping.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M7"/></axsl:template>

	<!--RULE -->
<axsl:template match="*[@codeListValue]" priority="1005" mode="M7"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="*[@codeListValue]"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="@codeListValue = text()"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="@codeListValue = text()"><axsl:attribute name="id">proposed.recommendation-codelistvalue</axsl:attribute><axsl:attribute name="flag">R-proposed</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>For code list types, it is recommended to make @codeListValue and text() identical, following INSPIRE's recommendation for interoperability.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M7"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:dateStamp" priority="1004" mode="M7"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:dateStamp"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="     starts-with(translate(*, '0123456789', '9999999999'),'9999-99-99') or     starts-with(translate(*, '0123456789', '9999999999'),'9999-99-99T99:99:99')     "/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="starts-with(translate(*, '0123456789', '9999999999'),'9999-99-99') or starts-with(translate(*, '0123456789', '9999999999'),'9999-99-99T99:99:99')"><axsl:attribute name="id">WCMP.dateStamp</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>[§8.1 ¶9] WMO Core Metadata Profile mandates dateStamp in format YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M7"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:citationDate" priority="1003" mode="M7"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:citationDate"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="     starts-with(translate(*, '0123456789', '9999999999'),'9999-99-99') or     starts-with(translate(*, '0123456789', '9999999999'),'9999-99-99T99:99:99')     "/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="starts-with(translate(*, '0123456789', '9999999999'),'9999-99-99') or starts-with(translate(*, '0123456789', '9999999999'),'9999-99-99T99:99:99')"><axsl:attribute name="id">WCMP.citationDate</axsl:attribute><axsl:attribute name="flag">R-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>[§8.1 ¶11] WMO Core Metadata Profile recommends citation date in format YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M7"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:contact" priority="1002" mode="M7"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:contact"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="*/gmd:role/*/@codeListValue = 'pointOfContact'"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="*/gmd:role/*/@codeListValue = 'pointOfContact'"><axsl:attribute name="id">WCMP.mdContRole</axsl:attribute><axsl:attribute name="flag">R-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>[§8.2 ¶3] WMO Core Metadata Profile recommends gmd:contact should use role = pointOfContact.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M7"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:identificationInfo/*/gmd:pointOfContact" priority="1001" mode="M7"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:identificationInfo/*/gmd:pointOfContact"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="*/gmd:contactInfo/*/gmd:address/*/gmd:electronicMailAddress[     contains(*,'@') or @gco:nilReason or @xlink:href or @uuidref ]"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="*/gmd:contactInfo/*/gmd:address/*/gmd:electronicMailAddress[ contains(*,'@') or @gco:nilReason or @xlink:href or @uuidref ]"><axsl:attribute name="id">WCMP.idPocEmail</axsl:attribute><axsl:attribute name="flag">R-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>[§8.2 ¶5] WMO Core Metadata Profile recommends email addres described in gmd:pointOfContact.</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M7"/></axsl:template>

	<!--RULE -->
<axsl:template match="gmd:descriptiveKeywords/*[     (gmd:thesaurusName/*/gmd:title/* = 'WMO_DistributionScopeCode') or     (gmd:thesairusName/*/gmd:title/*/@xlink:href = 'http://wis.wmo.int/2012/codelists/WMOCodeLists.xml#WMO_DistributionScopeCode')   ]" priority="1000" mode="M7"><svrl:fired-rule xmlns:svrl="http://purl.oclc.org/dsdl/svrl" context="gmd:descriptiveKeywords/*[     (gmd:thesaurusName/*/gmd:title/* = 'WMO_DistributionScopeCode') or     (gmd:thesairusName/*/gmd:title/*/@xlink:href = 'http://wis.wmo.int/2012/codelists/WMOCodeLists.xml#WMO_DistributionScopeCode')   ]"/>

		<!--ASSERT -->
<axsl:choose><axsl:when test="gmd:keyword[       (*/text() = 'GlobalExchange') or       (*/text() = 'RegionalExchange') or       (*/text() = 'OriginatingCentre')     ]"/><axsl:otherwise><svrl:failed-assert xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:schold="http://www.ascc.net/xml/schematron" test="gmd:keyword[ (*/text() = 'GlobalExchange') or (*/text() = 'RegionalExchange') or (*/text() = 'OriginatingCentre') ]"><axsl:attribute name="id">WCMP.scope-distribution</axsl:attribute><axsl:attribute name="flag">M-WCMP</axsl:attribute><axsl:attribute name="location"><axsl:apply-templates select="." mode="schematron-get-full-path"/></axsl:attribute><svrl:text>[§9.1 ¶1] The scope of distribution for data within WIS shall be expressed using the following controlled vocabulary: "GlobalExchange", “RegionalExchange” and “OriginatingCentre” (if the scope of distribution is documented).</svrl:text></svrl:failed-assert></axsl:otherwise></axsl:choose><axsl:apply-templates select="@*|*" mode="M7"/></axsl:template><axsl:template match="text()" priority="-1" mode="M7"/><axsl:template match="@*|node()" priority="-2" mode="M7"><axsl:apply-templates select="@*|*" mode="M7"/></axsl:template></axsl:stylesheet>