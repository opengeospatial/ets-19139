<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    /**
    *
    * This file is part of MD_DataIdentification profile of ISO 19139.
    *
    */
-->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron">
   <sch:title>ISO19139 profile schematron</sch:title>
   <sch:ns prefix="gmd" uri="http://www.isotc211.org/2005/gmd" />
   <sch:ns prefix="gco" uri="http://www.isotc211.org/2005/gco" />
   <!-- Metadata information -->
   <sch:pattern>
      <sch:title>characterSet: documented if ISO/IEC 10646 is not used</sch:title>
      <sch:rule context="gmd:MD_DataIdentification">
         <sch:assert test="gmd:citation/gmd:CI_Citation/gmd:title/gco:CharacterString">characterSet missing</sch:assert>
      </sch:rule>
   </sch:pattern>
   <sch:pattern>
      <sch:title>MD_Metadata.hierarchyLevel &gt;= 1</sch:title>
      <sch:rule context="//gmd:MD_DataIdentification|//*[@gco:isoType='gmd:MD_DataIdentification']">
         <sch:assert test="(../../gmd:hierarchyLevel or ../../gmd:hierarchyLevel/gmd:MD_ScopeCode/@codeListValue='dataset' or ../../gmd:hierarchyLevel/gmd:MD_ScopeCode/@codeListValue='') and (count(gmd:extent/*/gmd:geographicElement/gmd:EX_GeographicBoundingBox) + count (gmd:extent/*/gmd:geographicElement/gmd:EX_GeographicDescription))&gt;=1">Metadata Hierarchy Level is not valid</sch:assert>
      </sch:rule>
   </sch:pattern>
</sch:schema>
