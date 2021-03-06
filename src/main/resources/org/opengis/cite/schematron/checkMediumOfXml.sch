<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    /**
    *
    * This file is part of MD_Medium profile of ISO 19139.
    *
    */
-->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron">
   <sch:title>ISO19139 profile schematron</sch:title>
   <sch:ns prefix="gmd" uri="http://www.isotc211.org/2005/gmd" />
   <sch:ns prefix="gco" uri="http://www.isotc211.org/2005/gco" />
   <sch:pattern>
      <sch:title>"densityUnits" is mandatory if "density" is provided</sch:title>
      <sch:rule context="//gmd:MD_Medium">
         <sch:let name="density" value="gmd:density and not(gmd:densityUnits[@gco:nilReason!='missing' or not(@gco:nilReason)])" />
         <sch:assert test="$density = false()">"densityUnits" is mandatory</sch:assert>
      </sch:rule>
   </sch:pattern>
</sch:schema>

