<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron"
  xmlns:gml="http://www.opengis.net/gml/3.2" 
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xml:lang="en"
  queryBinding="xslt2">

  <sch:title>Additional constraints for GML 3.2 (ISO 19136) instance documents.</sch:title>
  <sch:ns prefix="sch" uri="http://purl.oclc.org/dsdl/schematron"/>
  <sch:ns prefix="gml" uri="http://www.opengis.net/gml/3.2"/>
  <sch:ns prefix="xlink" uri="http://www.w3.org/1999/xlink"/>

  <sch:pattern id="ValueArray">
    <sch:rule context="gml:ValueArray">
      <sch:assert test="not(@codeSpace and @uom)">ValueArray may not carry both a reference to a
        codeSpace and a uom</sch:assert>
      <sch:assert
        test="count(gml:valueComponent/*) = count(gml:valueComponent/*[name() = name(../../gml:valueComponent[1]/*[1])])"
        >All components shall be of the same type</sch:assert>
      <sch:assert
        test="count(gml:valueComponents/*) = count(gml:valueComponents/*[name() = name(../*[1])])"
        >All components shall be of the same type</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="DirectPosition">
    <sch:rule context="gml:pos">
      <sch:assert test="not(@srsDimension) or @srsName">The presence of a dimension attribute
        implies the presence of the srsName attribute.</sch:assert>
      <sch:assert test="not(@axisLabels) or @srsName">The presence of an axisLabels attribute
        implies the presence of the srsName attribute.</sch:assert>
      <sch:assert test="not(@uomLabels) or @srsName">The presence of an uomLabels attribute implies
        the presence of the srsName attribute.</sch:assert>
      <sch:assert test="(not(@uomLabels) and not(@axisLabels)) or (@uomLabels and @axisLabels)">The
        presence of an uomLabels attribute implies the presence of the axisLabels attribute and vice
        versa.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="PolyhedralSurface">
    <sch:rule context="gml:PolyhedralSurface">
      <sch:assert test="count(gml:patches/*)=count(gml:patches/gml:PolygonPatch)">All patches shall
        be gml:PolygonPatch elements or an element in the substitution group of gml:PolygonPatch.
        Note that the test currently does not identify substitutable elements correctly, this will
        require the use of XPath 2 in the future.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="TriangulatedSurface">
    <sch:rule context="gml:TriangulatedSurface">
      <sch:assert test="count(gml:patches/*)=count(gml:patches/gml:Triangle)">All patches shall be
        gml:Triangle elements or an element in the substitution group of gml:PolygonPatch. Note that
        the test currently does not identify substitutable elements correctly, this will require the
        use of XPath 2 in the future.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="MultiPointDomain">
    <sch:rule context="gml:MultiPointDomain">
      <sch:assert test="count(gml:domainSet/*)=count(gml:domainSet/gml:MultiPoint)">All values in
        the domain set shall be gml:MultiPoint elements or an element in its substitution group.
        Note that the test currently does not identify substitutable elements correctly, this will
        require the use of XPath 2 in the future.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="MultiCurveDomain">
    <sch:rule context="gml:MultiCurveDomain">
      <sch:assert test="count(gml:domainSet/*)=count(gml:domainSet/gml:MultiCurve)">All values in
        the domain set shall be gml:MultiCurve elements or an element in its substitution group.
        Note that the test currently does not identify substitutable elements correctly, this will
        require the use of XPath 2 in the future.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="MultiSurfaceDomain">
    <sch:rule context="gml:MultiSurfaceDomain">
      <sch:assert test="count(gml:domainSet/*)=count(gml:domainSet/gml:MultiSurface)">All values in
        the domain set shall be gml:MultiSurface elements or an element in its substitution group.
        Note that the test currently does not identify substitutable elements correctly, this will
        require the use of XPath 2 in the future.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="MultiSolidDomain">
    <sch:rule context="gml:MultiSolidDomain">
      <sch:assert test="count(gml:domainSet/*)=count(gml:domainSet/gml:MultiSolid)">All values in
        the domain set shall be gml:MultiSolid elements or an element in its substitution group.
        Note that the test currently does not identify substitutable elements correctly, this will
        require the use of XPath 2 in the future.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="GridDomain">
    <sch:rule context="gml:GridDomain">
      <sch:assert test="count(gml:domainSet/*)=count(gml:domainSet/gml:Grid)">All values in the
        domain set shall be gml:Grid elements or an element in its substitution group. Note that the
        test currently does not identify substitutable elements correctly, this will require the use
        of XPath 2 in the future.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="RectifiedGridDomain">
    <sch:rule context="gml:RectifiedGridDomain">
      <sch:assert test="count(gml:domainSet/*)=count(gml:domainSet/gml:RectifiedGrid)">All values in
        the domain set shall be gml:RectifiedGrid elements or an element in its substitution group.
        Note that the test currently does not identify substitutable elements correctly, this will
        require the use of XPath 2 in the future.</sch:assert>
    </sch:rule>
  </sch:pattern>

  <!--
  <sch:pattern id="StrictAssociation">
    <sch:p>Checking this rule requires schema-awareness (substitution group).</sch:p>
    <sch:rule context="gml:abstractStrictAssociationRole">
      <sch:assert test="not(@xlink:href and (*|text()))">Property element may not carry both a
        reference to an object and contain an object.</sch:assert>
      <sch:assert test="@xlink:href | (*|text())">Property element shall either carry a reference to
        an object or contain an object.</sch:assert>
    </sch:rule>
  </sch:pattern>
  -->
</sch:schema>
