<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron"
  xmlns:gml="http://www.opengis.net/gml/3.2" 
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xml:lang="en"
  queryBinding="xslt2"
  schemaVersion="3.2.1">

  <sch:title>Deprecated infoset items in GML 3.2 (see ISO 19136, Annex I).</sch:title>

  <sch:ns prefix="sch" uri="http://purl.oclc.org/dsdl/schematron"/>
  <sch:ns prefix="gml" uri="http://www.opengis.net/gml/3.2"/>
  <sch:ns prefix="xlink" uri="http://www.w3.org/1999/xlink"/>

  <sch:pattern id="DeprecatedItems">
    <sch:p>Reports the occurrence of deprecated GML element and attribute information items.</sch:p>
    <sch:rule context="*[@gml:id]">
      <sch:report flag="warning"
        test="gml:metaDataProperty">The gml:metaDataProperty element is deprecated (cl. 7.2.2.2).</sch:report>
      <sch:report flag="warning" 
        test="gml:location">The gml:location element is deprecated in GML features (cl. 9.3.1).</sch:report>
      <sch:report flag="warning"
        test="gml:description[@xlink:href]">The use of gml:description to reference an 
        external description has been deprecated--use gml:descriptionReference instead 
        (cl. 7.2.4.2).</sch:report>
      <sch:report flag="warning"
        test="gml:dataSource[@xlink:href]">The use of gml:dataSource to refer to a remote 
        data source has been deprecated--use gml:dataSourceReference instead (cl. 14.5.2).</sch:report>
    </sch:rule>
    <sch:rule context="gml:boundedBy">
      <sch:report flag="warning"
        test="gml:Null">The gml:Null element is deprecated in gml:BoundingShapeType (cl. 9.4.1).</sch:report>
    </sch:rule>
    <sch:rule context="gml:Envelope">
      <sch:report flag="warning"
        test="gml:coordinates">The gml:coordinates element is deprecated (cl. 10.1.4.6).</sch:report>
      <sch:report flag="warning"
        test="gml:pos">The gml:pos element is deprecated (cl. 10.1.4.6).</sch:report>
    </sch:rule>
    <sch:rule context="gml:Point">
      <sch:report flag="warning"  
        test="gml:coordinates">The gml:coordinates element is deprecated. Use gml:pos instead 
        (cl. 10.3.1).</sch:report>
    </sch:rule>
    <sch:rule context="gml:LinearRing">
      <sch:report flag="warning" 
        test="gml:coordinates">The gml:coordinates element is deprecated. Use gml:posList instead 
        (cl. 10.5.8).</sch:report>
      <sch:report flag="warning" 
        test="gml:pointRep">The gml:pointRep element is deprecated. Use gml:pointProperty instead 
        (cl. I.5.3).</sch:report>
    </sch:rule>
  </sch:pattern>

</sch:schema>
