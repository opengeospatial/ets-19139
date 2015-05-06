<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" schemaVersion="ISO19757-3" queryBinding="xslt2">
  <sch:ns prefix="gmd" uri="http://www.isotc211.org/2005/gmd"/>
  <sch:ns prefix="gco" uri="http://www.isotc211.org/2005/gco"/>
  <sch:ns prefix="gml" uri="http://www.opengis.net/gml/3.2"/>
  <sch:ns prefix="gts" uri="http://www.isotc211.org/2005/gts"/>
  <sch:ns prefix="gmi" uri="http://www.isotc211.org/2005/gmi"/>
  <sch:ns prefix="xlink" uri="http://www.w3.org/1999/xlink"/>
  <sch:ns prefix="gmx" uri="http://www.isotc211.org/2005/gmx"/>
  <!--
    ISO 19115 and 19115-2 Style Suggestions
    
    This schematron checks several recommended practices for of ISO Metadata.
    Following these practices is not essential, but it improves the metadata.
    
    Created by ted.habermann@noaa.gov 20120526
  -->
  <sch:pattern>
    <!-- The standard form for codeLists includes the value as an attribute and as content in the element -->
    <sch:title>Check the form of CodeLists</sch:title>
    <sch:rule context="//*[ends-with(name(./*[1]),'Code') and count(*)=1 and not(name(.)='gmd:topicCategory')]">
      <sch:assert test="./*[1] = ./*[1]/@codeListValue">[ISOStyle:codeListForm] Make the codeListValue attribute match the content of the codeList element</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <!--
      The uuid and uuidref attributes are expected to be valid Universally Unique Identifiers. These consist of
      32 characters optionally separated into groups of 8-4-4-4-12 characters by dashes.
    -->
    <sch:title>Check the form of uuid attributes</sch:title>
    <sch:rule context="//@uuid">
      <sch:assert test="matches(.,'^[\d|\w]{8}-[\d|\w]{4}-[\d|\w]{4}-[\d|\w]{4}-[\d|\w]{12}$') or matches(.,'^[\d|\w]{32}$')">[ISOStyle:ValidUUID] Make uuid attributes valid uuids</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <!--
       The uuid and uuidref attributes are expected to be valid Universally Unique Identifiers. These consist of
       32 characters optionally separated into groups of 8-4-4-4-12 characters by dashes.
     -->
    <sch:title>Check the form of uuidref attributes</sch:title>
    <sch:rule context="//@uuidref">
      <sch:assert test="matches(.,'^[\d|\w]{8}-[\d|\w]{4}-[\d|\w]{4}-[\d|\w]{4}-[\d|\w]{12}$') or matches(.,'^[\d|\w]{32}$')">[ISOStyle:ValidUUIDRef] Make uuidref attributes valid uuids</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <!--
      onlineResources are a critical part of the interface between metadata and users. The ISO standard requires only
      the linkage element but the name, description, and function are important for improving usability of the metadata.
    -->
    <sch:title>Check onlineResources</sch:title>
    <sch:rule context="//gmd:CI_OnlineResource">
      <sch:assert test="gmd:name">[ISOStyle:onlinResourceName] Add a name to the onlineResources</sch:assert>
      <sch:assert test="gmd:description">[ISOStyle:onlinResourceDescription] Add a description to the onlineResources</sch:assert>
      <sch:assert test="gmd:function">[ISOStyle:onlinResourceFunction] Add a functionCode to the onlineResources</sch:assert>
    </sch:rule>
  </sch:pattern>
  <!-- check the validity of a URL in gmx:Anchor tag -->
  <sch:pattern>
    <sch:title>Check xlink in gmx:Anchor</sch:title>
    <sch:rule context="//gmx:Anchor">
      <sch:let name="xlinkVoc" value="document(./attribute::xlink:href)"/>
      <sch:assert test="$xlinkVoc">
        <sch:value-of select="./attribute::xlink:href"/>[ISOStyle:validXLink] Make sure this is a valid URL. </sch:assert>
    </sch:rule>
  </sch:pattern>
  <!-- Best Practices for @id in extent objects. Note: when there are repeating gmd:extents - this assertion incorrectly expects for each //xpath below to contain these id attributes.-->
  <sch:pattern>
    <sch:title>Spatial/temporal extent tests</sch:title>
    <sch:rule context="/*[gmd:hierarchyLevel/gmd:MD_ScopeCode!='nonGeographicDataset']/gmd:identificationInfo/gmd:MD_DataIdentification">
      <!-- Test for gmd:EX_Extent with id = "boundingExtent"  and that boundingExtent contains boundingGeographicBoundingBox and boundingGeographicBoundingBox -->
      <sch:assert test="count(gmd:extent/gmd:EX_Extent[@id = 'boundingExtent']) = 1">[ISOStyle:boundingExtentId] Add an id="boundingExtent" to the bounding EX_Extent (only one allowed)</sch:assert>
      <sch:assert test="count(gmd:extent/gmd:EX_Extent[@id = 'boundingExtent']/gmd:geographicElement) > 0">[ISOStyle:boundingGeographicElement] Add a geographicElement to the boundingExtent (one is required)</sch:assert>
      <sch:assert test="count(gmd:extent/gmd:EX_Extent[@id = 'boundingExtent']/gmd:geographicElement/gmd:EX_GeographicBoundingBox) > 0">[ISOStyle:boundingGeographicBoundingBox] Add a geographicBoundingBox to the boundingExtent (one is required)</sch:assert>
      <sch:assert test="count(gmd:extent/gmd:EX_Extent[@id = 'boundingExtent']/gmd:geographicElement/gmd:EX_GeographicBoundingBox[@id = 'boundingGeographicBoundingBox']) = 1">[ISOStyle:boundingGeographicBoundingBoxId] Add a id="boundingGeographicBoundingBox" to the EX_GeographicBoundingBox in the
        bounding extent</sch:assert>
      <!-- Test to make sure that there is only one boundingGeographicBoundingBox -->
      <sch:assert test="count(gmd:extent/gmd:EX_Extent/gmd:geographicElement/gmd:EX_GeographicBoundingBox[@id = 'boundingGeographicBoundingBox']) = 1">[ISOStyle:oneBoundingGeographicBoundingBox] Delete the extra EX_GeographicBoundingBox with id="boundingGeographicBoundingBox" (only one
        allowed)</sch:assert>
      <sch:assert test="count(gmd:extent/gmd:EX_Extent[@id = 'boundingExtent']/gmd:temporalElement) > 0">[ISOStyle:boundingTemporalElement] Add a temporalElement to the boundingExtent (one is required)</sch:assert>
      <sch:assert test="count(gmd:extent/gmd:EX_Extent[@id = 'boundingExtent']/gmd:temporalElement/gmd:EX_TemporalExtent) > 0">[ISOStyle:boundingTemporalExtent] Add a EX_TemporalExtent to the boundingExtent (one is required)</sch:assert>
      <sch:assert test="count(gmd:extent/gmd:EX_Extent[@id = 'boundingExtent']/gmd:temporalElement/gmd:EX_TemporalExtent[@id = 'boundingTemporalExtent']) = 1">[ISOStyle:boundingTemporalExtentId] Add a EX_TemporalExtent identified with id="boundingTemporalExtent" to the bounding extent</sch:assert>
      <!-- Test to make sure that there is only one boundingTemporalExtent -->
      <sch:assert test="count(gmd:extent/gmd:EX_Extent/gmd:temporalElement/gmd:EX_TemporalExtent[@id = 'boundingTemporalExtent']) = 1">[ISOStyle:oneBoundingTemporalExtent] Delete the extra EX_TemporalExtent with id="boundingTemporalExtent" (only one allowed)</sch:assert>
      <!-- Test for geographic bounds -->
      <sch:assert test="count(gmd:extent/gmd:EX_Extent[@id = 'boundingExtent']/gmd:geographicElement/gmd:EX_GeographicBoundingBox) > 0">[ISOStyle:boundingGeographicBoundingBox] Add a geographicBoundingBox to the boundingExtent (one is required)</sch:assert>
      <sch:assert test="gmd:extent/gmd:EX_Extent[@id = 'boundingExtent']/gmd:geographicElement/gmd:EX_GeographicBoundingBox/gmd:westBoundLongitude/gco:Decimal">Add westBoundLongitude to the EX_GeographicBoundingBox</sch:assert>
      <sch:assert test="gmd:extent/gmd:EX_Extent[@id = 'boundingExtent']/gmd:geographicElement/gmd:EX_GeographicBoundingBox/gmd:eastBoundLongitude/gco:Decimal">Add eastBoundLongitude to the EX_GeographicBoundingBox</sch:assert>
      <sch:assert test="gmd:extent/gmd:EX_Extent[@id = 'boundingExtent']/gmd:geographicElement/gmd:EX_GeographicBoundingBox/gmd:northBoundLatitude/gco:Decimal">Add northBoundLatitude to the EX_GeographicBoundingBox</sch:assert>
      <sch:assert test="gmd:extent/gmd:EX_Extent[@id = 'boundingExtent']/gmd:geographicElement/gmd:EX_GeographicBoundingBox/gmd:southBoundLatitude/gco:Decimal">Add southBoundLatitude to the EX_GeographicBoundingBox</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
