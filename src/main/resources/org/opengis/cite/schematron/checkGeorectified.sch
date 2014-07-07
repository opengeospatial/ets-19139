<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    /**
    *
    * This file is part of MD_Georectified profile of ISO 19139.
    *
    */
-->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron">
   <sch:title>ISO19139 profile schematron</sch:title>
   <sch:ns prefix="gmd" uri="http://www.isotc211.org/2005/gmd" />
   <sch:ns prefix="gco" uri="http://www.isotc211.org/2005/gco" />
   <sch:pattern>
      <sch:title>"checkPointDescription" is mandatory if "checkPointAvailability" = 1</sch:title>
      <sch:rule context="//gmd:MD_Georectified">
         <sch:let name="cpd" value="(gmd:checkPointAvailability/gco:Boolean='1' or gmd:checkPointAvailability/gco:Boolean='true') and (not(gmd:checkPointDescription) or count(gmd:checkPointDescription[@gco:nilReason='missing'])&gt;0)" />
         <sch:assert test="$cpd = false()">"checkPointDescription" is mandatory</sch:assert>
      </sch:rule>
   </sch:pattern>
</sch:schema>
