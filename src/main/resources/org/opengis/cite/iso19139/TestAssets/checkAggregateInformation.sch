<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    /**
    *
    * This file is part of MD_AggregateInformation profile of ISO 19139.
    *
    */
-->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron">
   <sch:title>ISO19139 profile schematron</sch:title>
   <sch:ns prefix="gmd" uri="http://www.isotc211.org/2005/gmd" />
   <sch:pattern>
      <sch:title>MD_AggregateInformation</sch:title>
      <sch:rule context="gmd:MD_AggregateInformation">
         <sch:assert test="gmd:aggregateDataSetName or gmd:aggregateDataSetIdentifier">aggregateDataSetName or aggregateDataSetIdentifier 			missing</sch:assert>
      </sch:rule>
   </sch:pattern>
</sch:schema>
