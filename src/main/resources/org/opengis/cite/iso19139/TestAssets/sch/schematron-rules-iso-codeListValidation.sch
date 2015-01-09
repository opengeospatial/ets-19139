<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    /**
    *
    * This file is part of SeaDataNet metadata profile of ISO 19115.
    *
    * Copyright (C) 2012 Enrico Boldrini, Stefano Nativi
    * CNR - Institute of Atmospheric Pollution Research
    *
    * schematron rules version 10.0.0
    */
-->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron">
   <sch:title>SeaDatanet profile schematron</sch:title>
   <sch:ns prefix="gmd" uri="http://www.isotc211.org/2005/gmd" />
   <sch:ns prefix="gco" uri="http://www.isotc211.org/2005/gco" />
   <sch:ns prefix="gml" uri="http://www.opengis.net/gml/3.2" />
   <sch:ns prefix="gml32" uri="http://www.opengis.net/gml/3.2" />
   <!-- Metadata information -->
   

   

   
   


   

   <!-- <sch:pattern>
      <sch:title>$loc/strings/M19</sch:title>
      <sch:rule context="//gmd:MD_Distribution">
         <sch:let name="count" value="count(gmd:distributionFormat)&gt;0 or count(gmd:distributor/gmd:MD_Distributor/gmd:distributorFormat)&gt;0" />
         <sch:assert test="$count">$loc/strings/alert.M19</sch:assert>
      </sch:rule>
   </sch:pattern>
-->
   

   

  
</sch:schema>
