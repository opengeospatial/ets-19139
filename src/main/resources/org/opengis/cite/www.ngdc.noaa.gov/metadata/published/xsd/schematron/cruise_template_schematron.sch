<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:iso="http://purl.oclc.org/dsdl/schematron" schemaVersion="ISO19757-3" queryBinding="xslt2">
 <ns prefix="gmd" uri="http://www.isotc211.org/2005/gmd"/>
 <ns prefix="gco" uri="http://www.isotc211.org/2005/gco"/>
 <ns prefix="gml" uri="http://www.opengis.net/gml/3.2"/>
 <ns prefix="gts" uri="http://www.isotc211.org/2005/gts"/>
 <ns prefix="gmi" uri="http://www.isotc211.org/2005/gmi"/>
 <ns prefix="xlink" uri="http://www.w3.org/1999/xlink"/>
 <ns prefix="gmx" uri="http://www.isotc211.org/2005/gmx"/>
 <ns prefix="dc" uri="http://purl.org/dc/elements/1.1/"/>
 <ns prefix="dcterms" uri="http://purl.org/dc/terms/"/>
 <ns prefix="rdf" uri="http://www.w3.org/1999/02/22-rdf-syntax-ns#"/>
 <ns prefix="skos" uri="http://www.w3.org/2004/02/skos/core#"/>
 <!-- Editor: Anna.Milan@noaa.gov-->
 <!-- Date: March 15, 2011 -->
 <!-- mandatory elements required by cruise level metadata, but not base ISO 19115/19115-2 -->
 <!-- Note: in some xpaths it is difficult to test for compliance with the Cruise Level Metadata template and this may not test for all fields. -->
 <!-- Purpose: To test if referenced vocabularies in //gmx:Anchor[@xlink:href] exist and are formatted as SKOS XML. -->
 <pattern id="checkAnchorXlink">
  <rule context="//gmx:Anchor">
   <let name="xlinkVoc" value="document(./attribute::xlink:href)"/>
   <assert test="$xlinkVoc">
    <value-of select="./attribute::xlink:href"/> is not a valid URL. 
			</assert>
   <assert test="document(./attribute::xlink:href)//rdf:RDF/skos:Concept"> Vocabulary is not SKOS XML. </assert>
   <!-- this provides a more detailed report of the xlink, whether is is valid or not.  -->
   <!--<report test="$xlinkVoc">Report of <value-of select="./attribute::xlink:href"/>: <value-of select="$xlinkVoc"/></report>-->
  </rule>
 </pattern>
 <!--  -->
 <pattern>
  <title>Cruise Identifiers</title>
  <rule context="gmi:MI_Metadata">
   <assert test="gmd:fileIdentifier">Cruise Identifier is required. </assert>
  </rule>
  <rule context="gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation">
   <assert test="gmd:identifier"> Cruise Identifier is required. </assert>
   <assert test="gmd:identifier//gmd:code"> Cruise Identifier is required in gmd:code. </assert>
  </rule>
 </pattern>
 <pattern>
  <title>Metadata Hierarchy Level</title>
  <rule context="/gmi:MI_Metadata">
   <assert test="gmd:hierarchyLevel">hierarchyLevel is required.</assert>
  </rule>
  <rule context="gmi:MI_Metadata/gmd:hierarchyLevel">
   <assert test="gmd:MD_ScopeCode">MD_ScopeCode is required in hierarchyLevel.</assert>
   <assert test="contains('fieldSession', normalize-space(gmd:MD_ScopeCode))">'fieldSession' required in MD_ScopeCode.</assert>
  </rule>
  <rule context="gmi:MI_Metadata/gmd:hierarchyLevelName">
   <assert test="contains('Cruise Level Metadata', normalize-space(.))">'Cruise Level Metadata' required in hierarchyLevelName.</assert>
  </rule>
 </pattern>
 <pattern>
  <title>Email for metadata contact</title>
  <rule context="/gmi:MI_Metadata/gmd:contact/gmd:CI_ResponsibleParty">
   <assert test=".//gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:electronicMailAddress">Email address required for metadata contact.</assert>
  </rule>
 </pattern>
 <pattern>
  <title>Name and version of metadata standard</title>
  <rule context="/gmi:MI_Metadata">
   <assert test="gmd:metadataStandardName">metadataStandardName is required.</assert>
   <assert test="contains('ISO 19115-2 Geographic Information - Metadata Part 2 Extensions for imagery and gridded data', normalize-space(gmd:metadataStandardName))">'ISO 19115-2 Geographic Information - Metadata Part 2 Extensions for imagery and gridded data' required in metadataStandardName.</assert>
   <assert test="gmd:metadataStandardVersion">metadataStandardVersion is required. </assert>
   <assert test="contains('ISO 19115-2:2009(E)', normalize-space(./gmd:metadataStandardVersion))">'ISO 19115-2:2009(E)' required in metadataStandardVersion. </assert>
  </rule>
 </pattern>
 <pattern>
  <title>CI_DateTypeCode is 'creation'</title>
  <rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:dateType/gmd:CI_DateTypeCode">
   <assert test="contains('creation', normalize-space(.))">'creation' is required for CI_DateTypeCode.</assert>
  </rule>
 </pattern>
 <pattern>
  <title>Cruise Status</title>
  <rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification">
   <assert test="gmd:status">Status of cruise is required.</assert>
  </rule>
 </pattern>
 <pattern>
  <title>Principle Investigator Information</title>
  <rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification">
   <assert test="gmd:pointOfContact">Chief Scientist must be identified.</assert>
  </rule>
  <rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact[1]">
   <assert test="contains('principalInvestigator', normalize-space(gmd:CI_ResponsibleParty/gmd:role/gmd:CI_RoleCode))">Role code for chief scientist must be 'principleInvestigator'.</assert>
   <assert test="gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:electronicMailAddress">Email address required for chief scientist.</assert>
  </rule>
 </pattern>
 <pattern>
  <title>Keywords</title>
  <rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification">
   <assert test="gmd:descriptiveKeywords">Keywords from 'NASA/GCMD Science Keywords' and IHO's 'Limits of Oceans and Seas' are required.</assert>
  </rule>
  <rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification//gmd:descriptiveKeywords/gmd:MD_Keywords">
   <report test="contains('NASA/GCMD Science Keywords', normalize-space(gmd:thesaurusName/gmd:CI_Citation/gmd:title)) and not(normalize-space(gmd:keyword) = 'EARTH SCIENCE &gt; Oceans')">'EARTH SCIENCE &gt; Oceans' from GCMD keywords are required.</report>
  </rule>
 </pattern>
 <pattern>
  <title>Use Limitation</title>
  <rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification">
   <assert test="gmd:resourceConstraints/gmd:MD_Constraints/gmd:useLimitation">Disclaimer is required.</assert>
  </rule>
 </pattern>
 <pattern>
  <title>Larger Work/Program</title>
  <rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:aggregationInfo[1]">
   <assert test="contains('largerWorkCitation', normalize-space(gmd:MD_AggregateInformation/gmd:associationType/gmd:DS_AssociationTypeCode))"/>
   <assert test="contains('program', normalize-space(gmd:MD_AggregateInformation/gmd:initiativeType/gmd:DS_InitiativeTypeCode))"/>
   <assert test="normalize-space(gmd:MD_AggregateInformation/gmd:aggregateDataSetIdentifier/gmd:MD_Identifier/gmd:code)">Identifier for larger work program required.</assert>
  </rule>
 </pattern>
 <pattern>
  <title>Topic Categories</title>
  <rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification">
   <assert test="gmd:topicCategory">topicCategory required with 'oceans' and 'geoscientificInformation'.</assert>
  </rule>
  <rule context="//gmd:topicCategory[1]">
   <assert test="normalize-space(.)='oceans'">'oceans' in first MD_TopicCategoryCode required.</assert>
  </rule>
  <rule context="//gmd:topicCategory[2]">
   <assert test="normalize-space(.)='geoscientificInformation'">'geoscientificInformation' in second MD_TopicCategoryCode required.</assert>
  </rule>
 </pattern>
 <pattern>
  <title>EX_GeographicBoundingBox</title>
  <rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification">
   <assert test="//gmd:extent//gmd:EX_GeographicBoundingBox">Bounding coordinates of cruise area is required.</assert>
  </rule>
  <rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification//gmd:extent//gmd:EX_GeographicBoundingBox">
   <assert test="@id">Attribute id required with value of 'boundingGeographicBoundingBox'</assert>
   <!--<assert test="contains('boundingGeographicBoundingBox', @id)">'boundingGeographicBoundingBox' required in id attribute.</assert>-->
  </rule>
 </pattern>
 <pattern>
  <title>Cruise Start and End</title>
  <rule context="/gmi:MI_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent">
   <!-- /gmi:MI_Metadata/gmd:identificationInfo[1]/gmd:MD_DataIdentification[1]/gmd:extent[3]/gmd:EX_Extent[1]/gmd:temporalElement[1] -->
   <assert test="//gmd:temporalElement">temporalElement required to document cruise start and end time and location.</assert>
   <!-- <assert test="contains('temporalExtent', normalize-space(gmd:EX_Extent/@id))">Attribute id required with value of 'temporalExtent' </assert>-->
   <!-- <assert test="contains('Cruise Start and End', (normalize-space(gmd:EX_Extent/gmd:description)))">'Cruise Start and End' required for description of cruise extent.</assert>-->
  </rule>
 </pattern>
 <pattern>
  <title>Scope of Lineage</title>
  <rule context="/gmi:MI_Metadata/gmd:dataQualityInfo/gmd:DQ_DataQuality/gmd:lineage">
   <assert test="../gmd:scope">Scope of Lineage must be described.</assert>
  </rule>
 </pattern>
 <pattern>
  <title>DQ_Scope content</title>
  <rule context="/gmi:MI_Metadata/gmd:dataQualityInfo/gmd:DQ_DataQuality/gmd:scope/gmd:DQ_Scope">
   <assert test="gmd:level/gmd:MD_ScopeCode">MD_ScopeCode required</assert>
   <assert test="contains('collectionSession', normalize-space(gmd:level/gmd:MD_ScopeCode))">Collection session is required for MD_ScopeCode.</assert>
   <assert test="gmd:levelDescription/gmd:MD_ScopeDescription/gmd:other">MD_ScopeDescription/other required.</assert>
   <assert test="gmd:levelDescription/gmd:MD_ScopeDescription/gmd:other">'Descriptions of dataypes collected by devices.' required in MD_ScopeDescription.</assert>
  </rule>
 </pattern>
 <pattern>
  <title>Platform/Ship Information</title>
  <rule context="/gmi:MI_Metadata/gmi:acquisitionInformation/gmi:MI_AcquisitionInformation">
   <assert test="gmi:platform">Platform required.</assert>
  </rule>
 </pattern>
 <pattern>
  <title>Ship Sponsor</title>
  <rule context="/gmi:MI_Metadata/gmi:acquisitionInformation/gmi:MI_AcquisitionInformation/gmi:platform/gmi:MI_Platform">
   <assert test="gmi:sponsor">Ship Operator required.</assert>
   <assert test="gmi:sponsor/gmd:CI_ResponsibleParty/gmd:organisationName">Organization of ship operator is required.</assert>
   <assert test="gmi:sponsor/gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:country">Country of ship operator is required.</assert>
  </rule>
 </pattern>
 <pattern>
  <title>Instrument Information</title>
  <rule context="/gmi:MI_Metadata/gmi:acquisitionInformation/gmi:MI_AcquisitionInformation/gmi:platform/gmi:MI_Platform//gmi:instrument/gmi:MI_Instrument">
   <assert test="gmi:description">Description of instrument is required.</assert>
  </rule>
 </pattern>
</schema>
