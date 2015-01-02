<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    /**
    *
    * This file is part of DQ_Scope profile of ISO 19139.
    *
    */
-->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron">
   <sch:title>ISO19139 profile schematron</sch:title>
   <sch:ns prefix="gmd" uri="http://www.isotc211.org/2005/gmd" />
   <sch:ns prefix="gco" uri="http://www.isotc211.org/2005/gco" />
   <sch:pattern>
      <sch:title>"levelDescription" is mandatory if "level" notEqual 'dataset' or 'series'</sch:title>
      <sch:rule context="//gmd:DQ_Scope">
         <sch:let name="levelDesc" value="gmd:level/gmd:MD_ScopeCode/@codeListValue='dataset' or gmd:level/gmd:MD_ScopeCode/@codeListValue='series' or (gmd:levelDescription and ((normalize-space(gmd:levelDescription) != '') or (gmd:levelDescription/gmd:MD_ScopeDescription) or (gmd:levelDescription/@gco:nilReason and contains('inapplicable missing template unknown withheld',gmd:levelDescription/@gco:nilReason))))" />
         <sch:assert test="$levelDesc">"levelDescription" is mandatory</sch:assert>
      </sch:rule>
   </sch:pattern>
</sch:schema>
