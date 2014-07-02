<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron"
    queryBinding="xslt2">
    <!--
This Schematron schema looks up the codeList and checks that
the codeListValue in the XML metadata document are valid
according to that codeList.
-->
    <!--
This script was developed for ANZLIC - the Spatial Information Council
by Geoscience Australia
as part of a project to develop an XML implementation of the ANZLIC ISO Metadata Profile.
July 2007.
This work is licensed under the Creative Commons Attribution 2.5 License.
To view a copy of this license, visit
http://creativecommons.org/licenses/by/2.5/au/
or send a letter to
Creative Commons,
543 Howard Street, 5th Floor,
San Francisco, California, 94105,
USA.
-->
    <sch:title>codeList Schematron validation for ANZLIC ISO 19115(19139) Profile Version 1.1</sch:title>
    <sch:ns prefix="gml" uri="http://www.opengis.net/gml" />
    <sch:ns prefix="gmx" uri="http://www.isotc211.org/2005/gmx"/>
        <!-- Metadata information -->
    <sch:pattern>
        <sch:title>language (3) -&gt; mandatory; modified datatype; restricted domain</sch:title>
        <sch:rule context="gmd:MD_Metadata">            
            <sch:assert test="gmd:language">language missing</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern>
        <sch:title>characterSet (4) -&gt; mandatory; restricted textual domain</sch:title>
        <sch:rule context="gmd:MD_Metadata">            
            <sch:assert test="gmd:characterSet">characterSet missing</sch:assert>
            <sch:assert test="gmd:characterSet/gmd:MD_CharacterSetCode/@codeListValue='utf8'">characterSet is not "utf8"</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern>
        <sch:title>characterSet (40) -&gt; mandatory; restricted textual domain</sch:title>
        <sch:rule context="gmd:MD_DataIdentification">            
            <sch:assert test="gmd:characterSet">characterSet missing</sch:assert>
        </sch:rule>
    </sch:pattern>
    		
    <sch:pattern>
	<sch:title>$loc/strings/M21</sch:title>
	<sch:rule context="//gmd:MD_DataIdentification|//*[@gco:isoType='gmd:MD_DataIdentification']">
		<sch:let name="extent" value="(not(../../gmd:hierarchyLevel)
			or ../../gmd:hierarchyLevel/gmd:MD_ScopeCode/@codeListValue='dataset'
			or ../../gmd:hierarchyLevel/gmd:MD_ScopeCode/@codeListValue='')
			and (count(gmd:extent/*/gmd:geographicElement/gmd:EX_GeographicBoundingBox)
			+ count (gmd:extent/*/gmd:geographicElement/gmd:EX_GeographicDescription))>=1"/>
	<sch:assert
		test="$extent = false()">$loc/strings/alert.M21</sch:assert>
	<sch:report 
		test="$extent = false()">$loc/strings/report.M21</sch:report>
</sch:rule>
</sch:pattern>


	<!-- MD_DataIdentification MD_Metadata.hierarchyLevel notEqual "dataset" implies topicCategory is not
                     mandatory	-->

    <sch:pattern>
        <sch:title>MD_AggregateInformation (66.1)</sch:title>
        <sch:rule context="gmd:MD_AggregateInformation">            
            <sch:assert test="gmd:aggregateDataSetName or gmd:aggregateDataSetIdentifier">aggregateDataSetName or aggregateDataSetIdentifier 			missing</sch:assert>
        </sch:rule>
    </sch:pattern>
    <sch:pattern>
	<sch:title>otherConstraints (72) -> conditional</sch:title>
		<sch:rule context="gmd:MD_LegalConstraints">
		<sch:let name="restrictionCode" value="gmd:accessConstraints/gmd:useConstraints"/>
		<sch:assert test="gmd:otherConstraints or $restrictionCode='otherRestrictions'">otherConstraints missing while accessConstraints="otherRestrictions"</sch:assert>
		</sch:rule>
    </sch:pattern>
</sch:schema>
