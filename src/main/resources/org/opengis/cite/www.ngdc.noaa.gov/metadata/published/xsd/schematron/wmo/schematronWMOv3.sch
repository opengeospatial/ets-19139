<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" schemaVersion="ISO19757-3" queryBinding="xslt2">
  <sch:ns prefix="gmd" uri="http://www.isotc211.org/2005/gmd"/>
  <sch:ns prefix="gco" uri="http://www.isotc211.org/2005/gco"/>
  <sch:ns prefix="gml" uri="http://www.opengis.net/gml/3.2"/>
  <sch:ns prefix="gts" uri="http://www.isotc211.org/2005/gts"/>
  <sch:ns prefix="gmi" uri="http://www.isotc211.org/2005/gmi"/>
  <sch:ns prefix="xlink" uri="http://www.w3.org/1999/xlink"/>
  <sch:ns prefix="gmx" uri="http://www.isotc211.org/2005/gmx"/>
  
  <!--  ISO 19115 and 19115-2 rules that are not checked by 19139 schema validation -->
  <!-- 
		This schematron was created for validatation of the WMO profile
	-->
  <!-- Editor: john.kozimor@noaa.gov-->
  <!-- Date: March 27, 2012 -->
  <!-- Update: May 14, 2010 -->
  <!-- Update: June 22, 2010 include report for non-html friendly characters, supplied by Kate.Wringe@sybase.com -->
  <!-- Update Sept 30, 2011:  moved codelist test and weird character's test out of this schematron. See codeListValidation.sch, asciiValidationForISO.sch, and asciiValidationForFGDC.sch.-->
  <!-- Update Nov 28, 2011: changed gml namespace declaration to 3.2 -->
  
  <sch:pattern>
    <sch:title>check for fileIdentifier existence</sch:title>
    <sch:rule context="/gmi:MI_Metadata|/gmd:MD_Metadata">
      <sch:assert test="./gmd:fileIdentifier/gco:CharacterString != '' ">fileIdentifier is mandatory and must be populated</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>check for hierarchyLevel occurrence</sch:title>
    <sch:rule context="/gmi:MI_Metadata|/gmd:MD_Metadata">
      <sch:assert test="(count(gmd:hierarchyLevel) = 0) or (count(gmd:hierarchyLevel) = 1)">hierarchyLevel is limited to one occurence</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>check for hierarchyLevelName occurrence</sch:title>
    <sch:rule context="/gmi:MI_Metadata|/gmd:MD_Metadata">
      <sch:assert test="(count(gmd:hierarchyLevelName) = 0) or (count(gmd:hierarchyLevelName) = 1)">hierarchyLevelName is limited to one occurence</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>check for WMO_CategoryCode codelist reference</sch:title>
    <sch:rule context="/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification">
      <sch:assert test="./gmd:descriptiveKeywords/gmd:MD_Keywords/gmd:thesaurusName/gmd:CI_Citation/gmd:title/gmx:Anchor[@xlink:href='http://wis.wmo.int/2010/metadata/version_1-2/WMOCodelists.xml#WMO_CategoryCode']">WMO_CategoryCode codelist reference not found. A minimum of one wmo category code keyword is mandatory.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>check for WMO_CategoryCode value existence</sch:title>
    <sch:rule context="//gmd:descriptiveKeywords/gmd:MD_Keywords/gmd:thesaurusName/gmd:CI_Citation/gmd:title/gmx:Anchor[@xlink:href]">
      <sch:let name="codeListDoc" value="document(substring-before(@xlink:href,'#'))//gmx:CodeListDictionary[@gml:id = substring-after(current()/@xlink:href,'#')]" /> 
      <sch:assert test="$codeListDoc">Unable to find the WMO category codeList document or CodeListDictionary node.</sch:assert>
      <sch:assert test="ancestor::gmd:MD_Keywords/gmd:keyword/gco:CharacterString = $codeListDoc/gmx:codeEntry/gmx:CodeDefinition/gml:identifier">codeListValue is not a valid WMO_CategoryCode (http://wis.wmo.int/2010/metadata/version_1-2/WMOCodelists.xml#WMO_CategoryCode).</sch:assert>
      <sch:assert test="ancestor::gmd:MD_Keywords/gmd:type/gmd:MD_KeywordTypeCode/@codeListValue = 'theme' ">gmd:MD_KeywordTypeCode/@codeListValue must be 'theme' for WMO_CategoryCode descriptiveKeywords.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>check for topicCategory existence</sch:title>
    <sch:rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification|/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification">
      <sch:assert test="./gmd:topicCategory/gmd:MD_TopicCategoryCode[text()!='']">topicCategory is mandatory and must be populated</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>checks for spatial extent</sch:title>
    <sch:rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent|/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent">
      <sch:assert test="(count(.) > 0)">a minimum of one gmd:EX_Extent is required</sch:assert>
    </sch:rule>
    <sch:rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent|/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement">
      <sch:assert test="(count(.) > 0)">a minimum of one gmd:geographicElement is required</sch:assert>
    </sch:rule>
    <sch:rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement|/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicBoundingBox">
      <sch:assert test="(count(.) = 1)">geographicBoundingBox is required</sch:assert>
    </sch:rule>
    <sch:rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicBoundingBox|/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicBoundingBox">
      <sch:assert test="./gmd:westBoundLongitude/gco:Decimal">westBoundLongitude required</sch:assert>
      <sch:assert test="./gmd:eastBoundLongitude/gco:Decimal">eastBoundLongitude required</sch:assert>
      <sch:assert test="./gmd:northBoundLatitude/gco:Decimal">northBoundLongitude required</sch:assert>
      <sch:assert test="./gmd:southBoundLatitude/gco:Decimal">southBoundLongitude required</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>check for useLimitation existence</sch:title>
    <sch:rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification|/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification">
      <sch:assert test="./gmd:resourceConstraints/gmd:MD_Constraints/gmd:useLimitation/gco:CharacterString != ''">useLimitation is required for compliance with INSPIRE rules. If no conditions apply, then a
        phrase such as “No conditions apply” should be recorded </sch:assert> 
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:title>check for dataQualityInfo report existence</sch:title>
    <sch:rule context="/gmi:MI_Metadata|/gmd:MD_Metadata">
      <sch:assert test="./gmd:dataQualityInfo/gmd:DQ_DataQuality/gmd:report">a dataQualityInfo report is required for compliance with INSPIRE rules.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>

