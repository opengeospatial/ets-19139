<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    /**
    *
    * This file is part of MD_Band profile of ISO 19139.
    *
    */
-->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron">
   <sch:title>ISO19139 profile schematron</sch:title>
   <sch:ns prefix="gmd" uri="http://www.isotc211.org/2005/gmd" />
   <sch:ns prefix="gco" uri="http://www.isotc211.org/2005/gco" />
   <sch:pattern>
      <sch:title>"units" is mandatory if "maxValue" or "minValue" are provided</sch:title>
      <sch:rule context="//gmd:MD_Band[gmd:maxValue or gmd:minValue]">
         <sch:let name="values" value="(gmd:maxValue[@gco:nilReason!='missing' or not(@gco:nilReason)] or gmd:minValue[@gco:nilReason!='missing' or not(@gco:nilReason)]) and not(gmd:units)" />
         <sch:assert test="$values = false()">units is mandatory if maxValue or minValue are provided</sch:assert>
      </sch:rule>
   </sch:pattern>
</sch:schema>
