<?xml version="1.1" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gts="http://www.isotc211.org/2005/gts" xmlns:gsr="http://www.isotc211.org/2005/gsr" xmlns:srv="http://www.isotc211.org/2005/srv" xmlns:gmx="http://www.isotc211.org/2005/gmx" xmlns:gmi="http://www.isotc211.org/2005/gmi"
    xmlns:gss="http://www.isotc211.org/2005/gss" xmlns="http://www.spase-group.org/data/schema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema" version="2.0">
    <xsl:output version="1.1"/>
    <xsl:template match="model">
        <!-- === SPASE == -->
        <xsl:result-document method="xml" href="SPASE/spase.xml">
            <Spase xmlns="http://www.spase-group.org/data/schema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.spase-group.org/data/schema http://www.spase-group.org/data/schema/spase-2_2_3.xsd" xsl:exclude-result-prefixes="gmd gco xlink gml gsr srv gts gss gmx gmi xsi xs">
                <Version>2.2.3</Version>
                <xsl:for-each select="numericalData">
                    <NumericalData>
                        <xsl:if test="spaseID">
                            <ResourceID>
                                <xsl:value-of select="normalize-space(spaseID)"/>
                            </ResourceID>
                        </xsl:if>
                        <ResourceHeader>
                            <xsl:if test="name">
                                <ResourceName>
                                    <xsl:value-of select="normalize-space(name)"/>
                                </ResourceName>
                            </xsl:if>
                            <xsl:for-each select="alternateName">
                                <AlternateName>
                                    <xsl:value-of select="normalize-space(.)"/>
                                </AlternateName>
                            </xsl:for-each>
                            <xsl:if test="metadataDateTime">
                                <ReleaseDate>
                                    <xsl:value-of select="normalize-space(metadataDateTime)"/>
                                </ReleaseDate>
                            </xsl:if>
                            <xsl:if test="description">
                                <Description>
                                    <xsl:value-of select="normalize-space(description)"/>
                                </Description>
                            </xsl:if>
                            <xsl:if test="acknowledgement">
                                <Acknowledgement>
                                    <xsl:value-of select="normalize-space(acknowledgement)"/>
                                </Acknowledgement>
                            </xsl:if>
                            <xsl:for-each select="dataContact">
                                <Contact>
                                    <PersonID>
                                        <xsl:value-of select="normalize-space(@spaseIDRef)"/>
                                    </PersonID>
                                    <xsl:for-each select="@contactRole">
                                        <Role>
                                            <xsl:value-of select="normalize-space(.)"/>
                                        </Role>
                                    </xsl:for-each>
                                </Contact>
                            </xsl:for-each>
                            <xsl:for-each select="partOfProgram">
                                <Association>
                                    <xsl:if test="@spaseIDRef">
                                        <AssociationID>
                                            <xsl:value-of select="normalize-space(@spaseIDRef)"/>
                                        </AssociationID>
                                    </xsl:if>
                                    <AssociationType>PartOf</AssociationType>
                                </Association>
                            </xsl:for-each>
                            <xsl:for-each select="priorID">
                                <PriorID>
                                    <xsl:value-of select="normalize-space(@spaseIDRef)"/>
                                </PriorID>
                            </xsl:for-each>
                        </ResourceHeader>
                        <xsl:for-each select="distribution">
                            <AccessInformation>
                                <xsl:if test="repository">
                                    <RepositoryID>
                                        <xsl:value-of select="repository/@spaseIDRef"/>
                                    </RepositoryID>
                                </xsl:if>
                                <xsl:for-each select="access">
                                    <xsl:if test="functionCode='download'">
                                        <AccessURL>
                                            <URL>
                                                <xsl:value-of select="normalize-space(URL)"/>
                                            </URL>
                                        </AccessURL>
                                    </xsl:if>
                                </xsl:for-each>
                                <!-- spase does not allow repeating formats -->
                                <xsl:for-each select="format[1]">
                                    <Format>
                                        <xsl:value-of select="formatName"/>
                                    </Format>
                                </xsl:for-each>
                                <xsl:if test="encoding">
                                    <Encoding>
                                        <xsl:value-of select="encoding"/>
                                    </Encoding>
                                </xsl:if>
                                <xsl:if test="dataExtent">
                                    <DataExtent>
                                        <xsl:if test="dataExtent/quantity">
                                            <Quantity>
                                                <xsl:value-of select="dataExtent/quantity"/>
                                            </Quantity>
                                        </xsl:if>
                                        <xsl:if test="dataExtent/units">
                                            <Units>
                                                <xsl:value-of select="dataExtent/units"/>
                                            </Units>
                                        </xsl:if>
                                        <xsl:if test="dataExtent/per">
                                            <Per>
                                                <xsl:value-of select="dataExtent/per"/>
                                            </Per>
                                        </xsl:if>
                                    </DataExtent>
                                </xsl:if>
                            </AccessInformation>
                        </xsl:for-each>
                        <xsl:if test="processingLevel">
                            <ProcessingLevel>
                                <xsl:value-of select="normalize-space(processingLevel)"/>
                            </ProcessingLevel>
                        </xsl:if>
                        <!-- not included
                            <ProviderResourceName/>
                            <ProviderProcessingLevel/>
                            <ProviderVersion/>
                        -->
                        <xsl:for-each select="//acquisitionInfo/instrument">
                            <InstrumentID>
                                <xsl:value-of select="normalize-space(@spaseIDRef)"/>
                            </InstrumentID>
                        </xsl:for-each>
                        <xsl:for-each select="measurementType">
                            <MeasurementType>
                                <xsl:value-of select="normalize-space(.)"/>
                            </MeasurementType>
                        </xsl:for-each>
                        <xsl:if test="temporalDescription">
                            <TemporalDescription>
                                <TimeSpan>
                                    <xsl:if test="temporalDescription/startDateTime">
                                        <StartDate>
                                            <xsl:value-of select="normalize-space(temporalDescription/startDateTime)"/>
                                        </StartDate>
                                    </xsl:if>
                                    <xsl:choose>
                                        <xsl:when test="temporalDescription/stopDateTime">
                                            <StopDate>
                                                <xsl:value-of select="normalize-space(temporalDescription/stopDateTime)"/>
                                            </StopDate>
                                        </xsl:when>
                                        <xsl:when test="temporalDescription/relativeStopDate">
                                            <RelativeStopDate>
                                                <xsl:value-of select="normalize-space(temporalDescription/relativeStopDate)"/>
                                            </RelativeStopDate>
                                        </xsl:when>
                                    </xsl:choose>

                                    <xsl:if test="temporalDescription/note">
                                        <Note>
                                            <xsl:value-of select="normalize-space(temporalDescription/note)"/>
                                        </Note>
                                    </xsl:if>
                                </TimeSpan>
                                <xsl:if test="cadence">
                                    <Cadence>
                                        <xsl:value-of select="normalize-space(cadence)"/>
                                    </Cadence>
                                </xsl:if>
                                <xsl:if test="exposure">
                                    <Exposure>
                                        <xsl:value-of select="normalize-space(exposure)"/>
                                    </Exposure>
                                </xsl:if>
                            </TemporalDescription>
                        </xsl:if>
                        <xsl:for-each select="spectralRange">
                            <SpectralRange>
                                <xsl:value-of select="normalize-space(.)"/>
                            </SpectralRange>
                        </xsl:for-each>
                        <xsl:for-each select="observedRegion">
                            <ObservedRegion>
                                <xsl:value-of select="normalize-space(.)"/>
                            </ObservedRegion>
                        </xsl:for-each>
                        <xsl:if test="caveats">
                            <Caveats>
                                <xsl:value-of select="normalize-space(caveats)"/>
                            </Caveats>
                        </xsl:if>
                        <xsl:for-each select="keyword">
                            <Keyword>
                                <xsl:value-of select="normalize-space(.)"/>
                            </Keyword>
                        </xsl:for-each>
                        <!-- <InputResourceID/>-->
                        <xsl:for-each select="contentInformation/Parameter">
                            <xsl:call-template name="copyAll"/>
                        </xsl:for-each>
                    </NumericalData>
                </xsl:for-each>
            </Spase>
        </xsl:result-document>
        <xsl:for-each select="//contact">
            <xsl:result-document method="xml" href="SPASE/person-{@uuid}.xml">
                <Spase xmlns="http://www.spase-group.org/data/schema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.spase-group.org/data/schema http://www.spase-group.org/data/schema/spase-2_2_3.xsd" xsl:exclude-result-prefixes="gmd gco xlink gml gsr srv gts gss gmx gmi xsi xs">
                    <Version>2.2.3</Version>
                    <Person>
                        <ResourceID>
                            <xsl:value-of select="@spaseID"/>
                        </ResourceID>
                        <xsl:if test="name">
                            <PersonName>
                                <xsl:value-of select="name"/>
                            </PersonName>
                        </xsl:if>
                        <OrganizationName>
                            <xsl:value-of select="organization"/>
                        </OrganizationName>
                        <xsl:if test="address">
                            <Address>
                                <xsl:value-of select="address/deliveryPoint, address/city, address/state, address/country" separator=", "/>
                            </Address>
                        </xsl:if>
                        <xsl:for-each select="email">
                            <Email>
                                <xsl:value-of select="normalize-space(.)"/>
                            </Email>
                        </xsl:for-each>
                        <xsl:for-each select="telephone/voice">
                            <PhoneNumber>
                                <xsl:value-of select="normalize-space(.)"/>
                            </PhoneNumber>
                        </xsl:for-each>
                        <xsl:for-each select="telephone/fax">
                            <FaxNumber>
                                <xsl:value-of select="normalize-space(.)"/>
                            </FaxNumber>
                        </xsl:for-each>
                        <xsl:if test="note">
                            <Note>
                                <xsl:value-of select="note"/>
                            </Note>
                        </xsl:if>
                    </Person>
                </Spase>
            </xsl:result-document>
        </xsl:for-each>
        <xsl:for-each select="instrument">
            <xsl:result-document method="xml" href="SPASE/instrument-{@uuid}.xml">
                <Spase xmlns="http://www.spase-group.org/data/schema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.spase-group.org/data/schema http://www.spase-group.org/data/schema/spase-2_2_3.xsd" xsl:exclude-result-prefixes="gmd gco xlink gml gsr srv gts gss gmx gmi xsi xs">
                    <Version>2.2.3</Version>
                    <Instrument xmlns="http://www.spase-group.org/data/schema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.spase-group.org/data/schema file:/C:/Users/amilan/Documents/1Metadata/SPASE-ISO/spase-2_2_2.xsd" xsl:exclude-result-prefixes="gmd gco xlink gml gsr srv gts gss gmx gmi xsi xs">
                        <ResourceID>
                            <xsl:value-of select="@spaseID"/>
                        </ResourceID>
                        <ResourceHeader>
                            <ResourceName>
                                <xsl:value-of select="normalize-space(name)"/>
                            </ResourceName>
                            <xsl:for-each select="alternateName">
                                <AlternateName>
                                    <xsl:value-of select="normalize-space(.)"/>
                                </AlternateName>
                            </xsl:for-each>
                            <ReleaseDate>
                                <xsl:value-of select="metadataDateTime"/>
                            </ReleaseDate>
                            <xsl:if test="description">
                                <Description>
                                    <xsl:value-of select="normalize-space(description)"/>
                                </Description>
                            </xsl:if>
                            <xsl:for-each select="instrumentContact">
                                <Contact>
                                    <PersonID>
                                        <xsl:value-of select="@spaseIDRef"/>
                                    </PersonID>
                                    <Role>
                                        <xsl:value-of select="@contactRole"/>
                                    </Role>
                                </Contact>
                            </xsl:for-each>
                            <xsl:for-each select="informationURL">
                                <InformationURL>
                                    <Name>
                                        <xsl:value-of select="normalize-space(name)"/>
                                    </Name>
                                    <URL>
                                        <xsl:value-of select="normalize-space(URL)"/>
                                    </URL>
                                    <Description>
                                        <xsl:value-of select="normalize-space(description)"/>
                                    </Description>
                                </InformationURL>
                            </xsl:for-each>
                        </ResourceHeader>
                        <InstrumentType>
                            <xsl:value-of select="instrumentType"/>
                        </InstrumentType>
                        <xsl:for-each select="investigationName">
                            <InvestigationName>
                                <xsl:value-of select="normalize-space(.)"/>
                            </InvestigationName>
                        </xsl:for-each>
                        <OperatingSpan>
                            <StartDate>
                                <xsl:value-of select="normalize-space(operatingSpanStart)"/>
                            </StartDate>
                            <xsl:if test="operatingSpanStop">
                                <StopDate>
                                    <xsl:value-of select="operatingSpanStop"/>
                                </StopDate>
                            </xsl:if>
                            <xsl:if test="operatingSpanNote">
                                <Note>
                                    <xsl:value-of select="normalize-space(operatingSpanNote)"/>
                                </Note>
                            </xsl:if>
                        </OperatingSpan>
                        <ObservatoryID>
                            <xsl:value-of select="onObservatory/@spaseIDRef"/>
                        </ObservatoryID>
                        <xsl:if test="caveats">
                            <Caveats>
                                <xsl:value-of select="normalize-space(caveats)"/>
                            </Caveats>
                        </xsl:if>
                    </Instrument>
                </Spase>
            </xsl:result-document>
        </xsl:for-each>

        <xsl:for-each select="observatory">
            <xsl:result-document method="xml" href="SPASE/observatory-{@uuid}.xml">
                <Spase xmlns="http://www.spase-group.org/data/schema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.spase-group.org/data/schema http://www.spase-group.org/data/schema/spase-2_2_3.xsd" xsl:exclude-result-prefixes="gmd gco xlink gml gsr srv gts gss gmx gmi xsi xs">
                    <Version>2.2.3</Version>
                    <Observatory>
                        <ResourceID>
                            <xsl:value-of select="@spaseID"/>
                        </ResourceID>
                        <ResourceHeader>
                            <ResourceName>
                                <xsl:value-of select="normalize-space(name)"/>
                            </ResourceName>
                            <xsl:for-each select="alternateName">
                                <AlternateName>
                                    <xsl:value-of select="normalize-space(.)"/>
                                </AlternateName>
                            </xsl:for-each>
                            <ReleaseDate>
                                <xsl:value-of select="normalize-space(metadataDateTime)"/>
                            </ReleaseDate>
                            <Description>
                                <xsl:value-of select="normalize-space(description)"/>
                            </Description>
                            <xsl:for-each select="observatoryContact">
                                <Contact>
                                    <PersonID>
                                        <xsl:value-of select="@spaseIDRef"/>
                                    </PersonID>
                                    <Role>
                                        <xsl:value-of select="@contactRole"/>
                                    </Role>
                                </Contact>
                            </xsl:for-each>
                            <xsl:for-each select="informationURL">
                                <InformationURL>
                                    <Name>
                                        <xsl:value-of select="normalize-space(name)"/>
                                    </Name>
                                    <URL>
                                        <xsl:value-of select="normalize-space(URL)"/>
                                    </URL>
                                    <Description>
                                        <xsl:value-of select="normalize-space(description)"/>
                                    </Description>
                                </InformationURL>
                            </xsl:for-each>
                        </ResourceHeader>
                        <Location>
                            <xsl:for-each select="locationRegion">
                                <ObservatoryRegion>
                                    <xsl:value-of select="normalize-space(.)"/>
                                </ObservatoryRegion>
                            </xsl:for-each>
                            <xsl:if test="locationCoordinateSystem">
                                <CoordinateSystemName>
                                    <xsl:value-of select="normalize-space(locationCoordinateSystem)"/>
                                </CoordinateSystemName>
                            </xsl:if>
                            <xsl:if test="locationLatitude">
                                <Latitude>
                                    <xsl:value-of select="normalize-space(locationLatitude)"/>
                                </Latitude>
                            </xsl:if>
                            <xsl:if test="locationLongitude">
                                <Longitude>
                                    <xsl:value-of select="normalize-space(locationLongitude)"/>
                                </Longitude>
                            </xsl:if>
                            <xsl:if test="locationElevation">
                                <Elevation>
                                    <xsl:value-of select="normalize-space(locationElevation)"/>
                                </Elevation>
                            </xsl:if>
                        </Location>
                        <OperatingSpan>
                            <StartDate>
                                <xsl:value-of select="normalize-space(operatingSpanStart)"/>
                            </StartDate>
                            <xsl:if test="operatingSpanStop">
                                <StopDate>
                                    <xsl:value-of select="normalize-space(operatingSpanStop)"/>
                                </StopDate>
                            </xsl:if>
                            <xsl:if test="operatingSpanNote">
                                <Note>
                                    <xsl:value-of select="normalize-space(operatingSpanNote)"/>
                                </Note>
                            </xsl:if>
                        </OperatingSpan>
                    </Observatory>
                </Spase>
            </xsl:result-document>
        </xsl:for-each>
        <!-- === ISO == -->

        <xsl:result-document method="xml" href="ISO/iso.xml">
            <gmi:MI_Metadata xmlns:gmi="http://www.isotc211.org/2005/gmi" xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:srv="http://www.isotc211.org/2005/srv" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:gmx="http://www.isotc211.org/2005/gmx" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:gsr="http://www.isotc211.org/2005/gsr" xmlns:gss="http://www.isotc211.org/2005/gss" xmlns:gts="http://www.isotc211.org/2005/gts"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.spase-group.org/data/schema" xsi:schemaLocation="http://www.isotc211.org/2005/gmd http://www.ngdc.noaa.gov/metadata/published/xsd/schema.xsd">
                <xsl:for-each select="numericalData">
                    <xsl:if test="spaseID">
                        <gmd:fileIdentifier>
                            <gco:CharacterString>
                                <xsl:value-of select="normalize-space(spaseID)"/>
                            </gco:CharacterString>
                        </gmd:fileIdentifier>
                    </xsl:if>
                    <xsl:choose>
                        <xsl:when test="//dataContact/@contactRole='MetadataContact'">
                            <xsl:for-each select="//dataContact[@contactRole='MetadataContact']">
                                <gmd:contact>
                                    <xsl:call-template name="CI_ResponsibleParty">
                                        <xsl:with-param name="spaseID" select="@spaseIDRef"/>
                                        <xsl:with-param name="xlinkISO" select="@uuidRef"/>
                                        <xsl:with-param name="title" select="@title"/>
                                        <xsl:with-param name="contactRole" select="@contactRole"/>
                                    </xsl:call-template>
                                </gmd:contact>
                            </xsl:for-each>
                        </xsl:when>
                        <xsl:otherwise>
                            <gmd:contact gco:nilReason="missing"/>
                        </xsl:otherwise>
                    </xsl:choose>
                    <gmd:dateStamp>
                        <gco:DateTime>
                            <xsl:value-of select="current-dateTime()"/>
                        </gco:DateTime>
                    </gmd:dateStamp>
                    <gmd:identificationInfo>
                        <gmd:MD_DataIdentification>
                            <gmd:citation>
                                <gmd:CI_Citation>
                                    <xsl:if test="name">
                                        <gmd:title>
                                            <gco:CharacterString>
                                                <xsl:value-of select="normalize-space(name)"/>
                                            </gco:CharacterString>
                                        </gmd:title>
                                    </xsl:if>
                                    <xsl:for-each select="alternateName">
                                        <gmd:alternateTitle>
                                            <gco:CharacterString>
                                                <xsl:value-of select="normalize-space(.)"/>
                                            </gco:CharacterString>
                                        </gmd:alternateTitle>
                                    </xsl:for-each>
                                    <gmd:date>
                                        <gmd:CI_Date>
                                            <xsl:if test="metadataDateTime">
                                                <gmd:date>
                                                    <gco:DateTime>
                                                        <xsl:value-of select="normalize-space(metadataDateTime)"/>
                                                    </gco:DateTime>
                                                </gmd:date>
                                            </xsl:if>
                                            <gmd:dateType>
                                                <gmd:CI_DateTypeCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode" codeListValue="publication">publication</gmd:CI_DateTypeCode>
                                            </gmd:dateType>
                                        </gmd:CI_Date>
                                    </gmd:date>
                                    <xsl:if test="spaseID">
                                        <gmd:identifier>
                                            <gmd:MD_Identifier>
                                                <gmd:code>
                                                    <gco:CharacterString>
                                                        <xsl:value-of select="normalize-space(spaseID)"/>
                                                    </gco:CharacterString>
                                                </gmd:code>
                                            </gmd:MD_Identifier>
                                        </gmd:identifier>
                                    </xsl:if>
                                    <xsl:choose>
                                        <xsl:when test="//dataContact/@contactRole='Publisher'">
                                            <xsl:for-each select="//dataContact[@contactRole='Publisher']">
                                                <gmd:citedResponsibleParty>
                                                    <xsl:call-template name="CI_ResponsibleParty">
                                                        <xsl:with-param name="spaseID" select="@spaseIDRef"/>
                                                        <xsl:with-param name="xlinkISO" select="@uuidRef"/>
                                                        <xsl:with-param name="title" select="@title"/>
                                                        <xsl:with-param name="contactRole" select="@contactRole"/>
                                                    </xsl:call-template>
                                                </gmd:citedResponsibleParty>
                                            </xsl:for-each>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <gmd:citedResponsibleParty gco:nilReason="missing"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <xsl:for-each select="onlineResource[functionCode='information']">
                                        <gmd:citedResponsibleParty>
                                            <gmd:CI_ResponsibleParty>
                                                <gmd:contactInfo>
                                                    <gmd:CI_Contact>
                                                        <gmd:onlineResource>
                                                            <xsl:call-template name="CI_OnlineResource">
                                                                <xsl:with-param name="function" select="'information'"/>
                                                            </xsl:call-template>
                                                        </gmd:onlineResource>
                                                    </gmd:CI_Contact>
                                                </gmd:contactInfo>
                                                <gmd:role gco:nilReason="inapplicable"/>
                                            </gmd:CI_ResponsibleParty>
                                        </gmd:citedResponsibleParty>
                                    </xsl:for-each>
                                </gmd:CI_Citation>
                            </gmd:citation>
                            <xsl:if test="description">
                                <gmd:abstract>
                                    <gco:CharacterString>
                                        <xsl:value-of select="normalize-space(description)"/>
                                    </gco:CharacterString>
                                </gmd:abstract>
                            </xsl:if>
                            <xsl:if test="acknowledgement">
                                <gmd:credit>
                                    <gco:CharacterString>
                                        <xsl:value-of select="normalize-space(acknowledgement)"/>
                                    </gco:CharacterString>
                                </gmd:credit>
                            </xsl:if>
                            <xsl:if test="progressCode">
                                <gmd:status>
                                    <gmd:MD_ProgressCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_ProgressCode">
                                        <xsl:attribute name="codeListValue" select="normalize-space(progressCode)"/>
                                        <xsl:value-of select="normalize-space(progressCode)"/>
                                    </gmd:MD_ProgressCode>
                                </gmd:status>
                            </xsl:if>
                            <xsl:choose>
                                <xsl:when test="//dataContact">
                                    <xsl:for-each select="dataContact[@contactRole!='MetadataContact']">
                                        <gmd:pointOfContact>
                                            <xsl:call-template name="CI_ResponsibleParty">
                                                <xsl:with-param name="spaseID" select="@spaseIDRef"/>
                                                <xsl:with-param name="xlinkISO" select="@uuidRef"/>
                                                <xsl:with-param name="title" select="@title"/>
                                                <xsl:with-param name="contactRole" select="@contactRole"/>
                                            </xsl:call-template>
                                        </gmd:pointOfContact>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:otherwise>
                                    <gmd:pointOfContact gco:nilReason="missing"/>
                                </xsl:otherwise>
                            </xsl:choose>
                            <xsl:if test="maintenanceFrequencyCode">
                                <gmd:resourceMaintenance>
                                    <gmd:MD_MaintenanceInformation>
                                        <gmd:maintenanceAndUpdateFrequency>
                                            <gmd:MD_MaintenanceFrequencyCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_MaintenanceFrequencyCode" codeListValue="asNeeded">
                                                <xsl:attribute name="codeListValue" select="maintenanceFrequencyCode"/>
                                                <xsl:value-of select="normalize-space(maintenanceFrequencyCode)"/>
                                            </gmd:MD_MaintenanceFrequencyCode>
                                        </gmd:maintenanceAndUpdateFrequency>

                                    </gmd:MD_MaintenanceInformation>
                                </gmd:resourceMaintenance>
                            </xsl:if>
                            <xsl:if test="browseGraphic">
                                <gmd:graphicOverview>
                                    <gmd:MD_BrowseGraphic>
                                        <gmd:fileName>
                                            <gco:CharacterString>
                                                <xsl:value-of select="normalize-space(browseGraphic/onlineResource/URL)"/>
                                            </gco:CharacterString>
                                        </gmd:fileName>
                                        <gmd:fileDescription>
                                            <gco:CharacterString>
                                                <xsl:value-of select="normalize-space(browseGraphic/onlineResource/description)"/>
                                            </gco:CharacterString>
                                        </gmd:fileDescription>
                                        <gmd:fileType>
                                            <gco:CharacterString>
                                                <xsl:value-of select="normalize-space(browseGraphic/fileType)"/>
                                            </gco:CharacterString>
                                        </gmd:fileType>
                                    </gmd:MD_BrowseGraphic>
                                </gmd:graphicOverview>

                            </xsl:if>
                            <xsl:if test="measurementType,spectralRange">
                                <gmd:descriptiveKeywords>
                                    <gmd:MD_Keywords>
                                        <xsl:for-each select="measurementType">
                                            <gmd:keyword>
                                                <gco:CharacterString>
                                                    <xsl:value-of select="concat('Spase.MeasurementType.',normalize-space(.))"/>
                                                </gco:CharacterString>
                                            </gmd:keyword>
                                        </xsl:for-each>
                                        <xsl:for-each select="spectralRange">
                                            <gmd:keyword>
                                                <gco:CharacterString>
                                                    <xsl:value-of select="concat('Spase.SpectralRange.',normalize-space(.))"/>
                                                </gco:CharacterString>
                                            </gmd:keyword>
                                        </xsl:for-each>
                                        <gmd:type>
                                            <gmd:MD_KeywordTypeCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_KeywordTypeCode" codeListValue="theme">theme</gmd:MD_KeywordTypeCode>
                                        </gmd:type>
                                        <gmd:thesaurusName xlink:title="SPASE Base dictionary" xlink:href="https://www.ngdc.noaa.gov/docucomp/b540160e-b9ec-40f9-a585-87a26d926011"/>
                                    </gmd:MD_Keywords>
                                </gmd:descriptiveKeywords>
                            </xsl:if>
                            <xsl:if test="observedRegion">
                                <gmd:descriptiveKeywords>
                                    <gmd:MD_Keywords>
                                        <xsl:for-each select="observedRegion">
                                            <gmd:keyword>
                                                <gco:CharacterString>
                                                    <xsl:value-of select="concat('observedRegion.',normalize-space(.))"/>
                                                </gco:CharacterString>
                                            </gmd:keyword>
                                        </xsl:for-each>
                                        <gmd:type>
                                            <gmd:MD_KeywordTypeCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_KeywordTypeCode" codeListValue="place">place</gmd:MD_KeywordTypeCode>
                                        </gmd:type>
                                        <gmd:thesaurusName xlink:title="SPASE Base dictionary" xlink:href="https://www.ngdc.noaa.gov/docucomp/b540160e-b9ec-40f9-a585-87a26d926011"/>
                                    </gmd:MD_Keywords>
                                </gmd:descriptiveKeywords>
                            </xsl:if>
                            <xsl:if test="keyword">
                                <gmd:descriptiveKeywords>
                                    <gmd:MD_Keywords>
                                        <xsl:for-each select="keyword">
                                            <gmd:keyword>
                                                <gco:CharacterString>
                                                    <xsl:value-of select="normalize-space(.)"/>
                                                </gco:CharacterString>
                                            </gmd:keyword>
                                        </xsl:for-each>
                                        <gmd:type>
                                            <gmd:MD_KeywordTypeCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_KeywordTypeCode" codeListValue="theme">theme</gmd:MD_KeywordTypeCode>
                                        </gmd:type>
                                    </gmd:MD_Keywords>
                                </gmd:descriptiveKeywords>
                            </xsl:if>
                            <gmd:resourceConstraints gco:nilReason="template"/>
                            <xsl:for-each select="partOfProgram">
                                <gmd:aggregationInfo>
                                    <gmd:MD_AggregateInformation>
                                        <gmd:aggregateDataSetIdentifier>
                                            <gmd:MD_Identifier>
                                                <gmd:code>
                                                    <gco:CharacterString>
                                                        <xsl:value-of select="normalize-space(@spaseIDRef)"/>
                                                    </gco:CharacterString>
                                                </gmd:code>
                                            </gmd:MD_Identifier>
                                        </gmd:aggregateDataSetIdentifier>
                                        <gmd:associationType>
                                            <gmd:DS_AssociationTypeCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#DS_AssociationTypeCode" codeListValue="largerWorkCitation">largerWorkCitation</gmd:DS_AssociationTypeCode>
                                        </gmd:associationType>
                                        <gmd:initiativeType>
                                            <gmd:DS_InitiativeTypeCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#DS_InitiativeTypeCode" codeListValue="collection">collection</gmd:DS_InitiativeTypeCode>
                                        </gmd:initiativeType>
                                    </gmd:MD_AggregateInformation>
                                </gmd:aggregationInfo>
                            </xsl:for-each>
                            <xsl:if test="temporalDescription/cadence">
                                <gmd:temporalResolution>
                                    <gts:TM_PeriodDuration>
                                        <xsl:value-of select="normalize-space(temporalDescription/cadence)"/>
                                    </gts:TM_PeriodDuration>
                                </gmd:temporalResolution>
                            </xsl:if>
                            <gmd:language>
                                <gco:CharacterString>eng; USA</gco:CharacterString>
                            </gmd:language>
                            <xsl:if test="temporalDescription">
                                <gmd:extent>
                                    <gmd:EX_Extent>
                                        <gmd:description>
                                            <gco:CharacterString>non-geographic dataset</gco:CharacterString>
                                        </gmd:description>
                                        <gmd:temporalElement>
                                            <gmd:EX_TemporalExtent>
                                                <gmd:extent>
                                                    <gml:TimePeriod>
                                                        <xsl:attribute name="gml:id" select="generate-id()"/>
                                                        <xsl:if test="temporalDescription/startDateTime">
                                                            <gml:beginPosition>
                                                                <xsl:value-of select="normalize-space(temporalDescription/startDateTime)"/>
                                                            </gml:beginPosition>
                                                        </xsl:if>
                                                        <xsl:if test="temporalDescription/stopDateTime">
                                                            <gml:endPosition>
                                                                <xsl:value-of select="normalize-space(temporalDescription/stopDateTime)"/>
                                                            </gml:endPosition>
                                                        </xsl:if>
                                                        <xsl:if test="not(temporalDescription/stopDateTime)">
                                                            <gml:endPosition indeterminatePosition="now"/>
                                                        </xsl:if>
                                                        <!-- <xsl:if test="temporalDescription/cadence">
                                                            <gml:timeInterval unit="unknown">
                                                                <xsl:value-of select="normalize-space(temporalDescription/cadence)"/>
                                                            </gml:timeInterval>
                                                        </xsl:if>-->
                                                        <xsl:if test="temporalDescription/exposure">
                                                            <gml:duration>
                                                                <xsl:value-of select="normalize-space(temporalDescription/exposure)"/>
                                                            </gml:duration>
                                                        </xsl:if>
                                                    </gml:TimePeriod>
                                                </gmd:extent>
                                            </gmd:EX_TemporalExtent>
                                        </gmd:temporalElement>

                                    </gmd:EX_Extent>
                                </gmd:extent>
                            </xsl:if>
                            <xsl:if test="caveats">
                                <gmd:supplementalInformation>
                                    <gco:CharacterString>
                                        <xsl:value-of select="normalize-space(caveats)"/>
                                    </gco:CharacterString>
                                </gmd:supplementalInformation>
                            </xsl:if>
                        </gmd:MD_DataIdentification>
                    </gmd:identificationInfo>
                    <xsl:if test="contentInformation">
                        <xsl:for-each select="contentInformation/Parameter">
                            <gmd:contentInfo>
                                <gmi:MI_CoverageDescription>
                                    <gmd:attributeDescription>
                                        <gco:RecordType>spase parameter</gco:RecordType>
                                    </gmd:attributeDescription>
                                    <gmd:contentType>
                                        <gmd:MD_CoverageContentTypeCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_CoverageContentTypeCode" codeListValue="physicalMeasurement">physicalMeasurement</gmd:MD_CoverageContentTypeCode>
                                    </gmd:contentType>
                                    <gmd:dimension>
                                        <gmd:MD_Band>
                                            <gmd:sequenceIdentifier>
                                                <gco:MemberName>
                                                    <gco:aName>
                                                        <gco:CharacterString>
                                                            <xsl:value-of select="Name"/>
                                                        </gco:CharacterString>
                                                    </gco:aName>
                                                    <gco:attributeType/>
                                                </gco:MemberName>
                                            </gmd:sequenceIdentifier>
                                            <gmd:descriptor>
                                                <gco:CharacterString>
                                                    <xsl:value-of select="Description"/>
                                                </gco:CharacterString>
                                            </gmd:descriptor>
                                            <xsl:if test="ValidMax">
                                                <gmd:maxValue>
                                                    <gco:Real>
                                                        <xsl:value-of select="ValidMax"/>
                                                    </gco:Real>
                                                </gmd:maxValue>
                                            </xsl:if>
                                            <xsl:if test="ValidMin">
                                                <gmd:minValue>
                                                    <gco:Real>
                                                        <xsl:value-of select="ValidMin"/>
                                                    </gco:Real>
                                                </gmd:minValue>
                                            </xsl:if>
                                            <xsl:if test="Units">
                                                <gmd:units>
                                                    <gml:UnitDefinition>
                                                        <xsl:attribute name="gml:id" select="generate-id()"/>
                                                        <gml:identifier codeSpace="local">
                                                            <xsl:value-of select="Units"/>
                                                        </gml:identifier>
                                                        <gml:remarks>
                                                            <xsl:value-of select="UnitsConversion"/>
                                                        </gml:remarks>
                                                    </gml:UnitDefinition>
                                                </gmd:units>
                                            </xsl:if>
                                        </gmd:MD_Band>
                                    </gmd:dimension>
                                    <xsl:if test="FillValue">
                                        <gmi:rangeElementDescription>
                                            <gmi:MI_RangeElementDescription>
                                                <gmi:name>
                                                    <gco:CharacterString>spase.FillValue</gco:CharacterString>
                                                </gmi:name>
                                                <gmi:definition>
                                                    <gco:CharacterString>A value that indicates that a quantity is undefined.</gco:CharacterString>
                                                </gmi:definition>
                                                <gmi:rangeElement>
                                                    <gco:Record>
                                                        <xsl:value-of select="FillValue"/>
                                                    </gco:Record>
                                                </gmi:rangeElement>
                                            </gmi:MI_RangeElementDescription>
                                        </gmi:rangeElementDescription>
                                    </xsl:if>
                                    <xsl:if test="CoordinateSystem/CoordinateRepresentation">
                                        <gmi:rangeElementDescription>
                                            <gmi:MI_RangeElementDescription>
                                                <gmi:name>
                                                    <gco:CharacterString>spase.CoordinateRepresentation</gco:CharacterString>
                                                </gmi:name>
                                                <gmi:definition>
                                                    <gco:CharacterString>The method or form for specifying a given point or vector in a given coordinate system</gco:CharacterString>
                                                </gmi:definition>
                                                <gmi:rangeElement>
                                                    <gco:Record>
                                                        <xsl:value-of select="CoordinateSystem/CoordinateRepresentation"/>
                                                    </gco:Record>
                                                </gmi:rangeElement>
                                            </gmi:MI_RangeElementDescription>
                                        </gmi:rangeElementDescription>
                                    </xsl:if>
                                    <xsl:if test="CoordinateSystem/CoordinateSystemName">
                                        <gmi:rangeElementDescription>
                                            <gmi:MI_RangeElementDescription>
                                                <gmi:name>
                                                    <gco:CharacterString>spase.CoordinateSystemName</gco:CharacterString>
                                                </gmi:name>
                                                <gmi:definition>
                                                    <gco:CharacterString>The coordinate system in which the position, direction or observation has been expressed.</gco:CharacterString>
                                                </gmi:definition>
                                                <gmi:rangeElement>
                                                    <gco:Record>
                                                        <xsl:value-of select="CoordinateSystem/CoordinateSystemName"/>
                                                    </gco:Record>
                                                </gmi:rangeElement>
                                            </gmi:MI_RangeElementDescription>
                                        </gmi:rangeElementDescription>
                                    </xsl:if>
                                </gmi:MI_CoverageDescription>
                            </gmd:contentInfo>
                        </xsl:for-each>
                    </xsl:if>
                    <xsl:if test="distribution">
                        <gmd:distributionInfo>
                            <gmd:MD_Distribution>
                                <xsl:for-each select="distribution/format">
                                    <gmd:distributionFormat>
                                        <gmd:MD_Format>
                                            <xsl:if test="formatName">
                                                <gmd:name>
                                                    <gco:CharacterString>
                                                        <xsl:value-of select="formatName"/>
                                                    </gco:CharacterString>
                                                </gmd:name>
                                            </xsl:if>
                                            <xsl:choose>
                                                <xsl:when test="version">
                                                    <gmd:version>
                                                        <gco:CharacterString>
                                                            <xsl:value-of select="version"/>
                                                        </gco:CharacterString>
                                                    </gmd:version>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <gmd:version gco:nilReason="unknown"/>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                            <xsl:if test="encoding">
                                                <gmd:fileDecompressionTechnique>
                                                    <gco:CharacterString>
                                                        <xsl:value-of select="encoding"/>
                                                    </gco:CharacterString>
                                                </gmd:fileDecompressionTechnique>
                                            </xsl:if>
                                        </gmd:MD_Format>
                                    </gmd:distributionFormat>
                                </xsl:for-each>
                                <gmd:distributor>
                                    <gmd:MD_Distributor>
                                        <xsl:for-each select="distribution/distributionContact">
                                            <gmd:distributorContact>
                                                <xsl:call-template name="CI_ResponsibleParty">
                                                    <xsl:with-param name="spaseID" select="@spaseIDRef"/>
                                                    <xsl:with-param name="xlinkISO" select="@uuidRef"/>
                                                    <xsl:with-param name="title" select="@title"/>
                                                    <xsl:with-param name="contactRole" select="@contactRole"/>
                                                </xsl:call-template>
                                            </gmd:distributorContact>
                                        </xsl:for-each>
                                        <!--<xsl:for-each select="//repository/repositoryContact">
                                            <gmd:distributorContact>
                                                <xsl:call-template name="CI_ResponsibleParty">
                                                    <xsl:with-param name="spaseID" select="@spaseIDRef"/>
                                                    <xsl:with-param name="xlinkISO" select="@uuidRef"/>
                                                    <xsl:with-param name="title" select="@title"/>
                                                    <xsl:with-param name="contactRole" select="@contactRole"/>
                                                </xsl:call-template>
                                            </gmd:distributorContact>
                                        </xsl:for-each>-->
                                        <xsl:if test="distribution/fees, distribution/orderingInstructions">
                                            <gmd:distributionOrderProcess>
                                                <gmd:MD_StandardOrderProcess>
                                                    <xsl:if test="distribution/fees">
                                                        <gmd:fees>
                                                            <gco:CharacterString>
                                                                <xsl:value-of select="distribution/fees"/>
                                                            </gco:CharacterString>
                                                        </gmd:fees>
                                                    </xsl:if>
                                                    <xsl:if test="distribution/orderingInstructions">
                                                        <gmd:orderingInstructions>
                                                            <gco:CharacterString>
                                                                <xsl:value-of select="distribution/orderingInstructions"/>
                                                            </gco:CharacterString>
                                                        </gmd:orderingInstructions>
                                                    </xsl:if>
                                                </gmd:MD_StandardOrderProcess>
                                            </gmd:distributionOrderProcess>
                                        </xsl:if>
                                    </gmd:MD_Distributor>
                                </gmd:distributor>
                                <xsl:for-each select="distribution/onlineAccess/onlineResource">
                                    <gmd:transferOptions>
                                        <gmd:MD_DigitalTransferOptions>
                                            <xsl:if test="../../dataExtent">
                                                <gmd:unitsOfDistribution>
                                                    <gco:CharacterString>
                                                        <xsl:value-of select="concat(../../dataExtent/quantity,' ', ../../dataExtent/units, ' per ', ../../dataExtent/per)"/>
                                                    </gco:CharacterString>
                                                </gmd:unitsOfDistribution>
                                            </xsl:if>
                                            <gmd:onLine>
                                                <xsl:call-template name="CI_OnlineResource">
                                                    <xsl:with-param name="function" select="'download'"/>
                                                </xsl:call-template>
                                            </gmd:onLine>
                                        </gmd:MD_DigitalTransferOptions>
                                    </gmd:transferOptions>
                                </xsl:for-each>
                            </gmd:MD_Distribution>
                        </gmd:distributionInfo>
                    </xsl:if>
                    <gmd:metadataMaintenance>
                        <gmd:MD_MaintenanceInformation>
                            <gmd:maintenanceAndUpdateFrequency>
                                <gmd:MD_MaintenanceFrequencyCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#MD_MaintenanceFrequencyCode" codeListValue="asNeeded">asNeeded</gmd:MD_MaintenanceFrequencyCode>
                            </gmd:maintenanceAndUpdateFrequency>
                            <gmd:maintenanceNote>
                                <gco:CharacterString>This metadata record was transformed <xsl:value-of select="adjust-date-to-timezone(current-date(),UTC)"/> with model-to-stds.xsl. </gco:CharacterString>
                            </gmd:maintenanceNote>
                        </gmd:MD_MaintenanceInformation>
                    </gmd:metadataMaintenance>
                    <xsl:if test="acquisitionInfo">
                        <gmi:acquisitionInformation>
                            <gmi:MI_AcquisitionInformation>
                                <xsl:for-each select="acquisitionInfo/acqInstrument">
                                    <xsl:call-template name="instrument">
                                        <xsl:with-param name="idRefSpase" select="@spaseIDRef"/>
                                        <xsl:with-param name="xlinkISO" select="@uuidRef"/>
                                        <xsl:with-param name="title" select="@title"/>
                                    </xsl:call-template>
                                </xsl:for-each>
                                <xsl:for-each select="acquisitionInfo/acqObservatory">
                                    <xsl:call-template name="platform">
                                        <xsl:with-param name="idRefSpase" select="@spaseIDRef"/>
                                        <xsl:with-param name="xlinkISO" select="@uuidRef"/>
                                        <xsl:with-param name="title" select="@title"/>
                                    </xsl:call-template>

                                </xsl:for-each>
                            </gmi:MI_AcquisitionInformation>
                        </gmi:acquisitionInformation>
                    </xsl:if>
                </xsl:for-each>
            </gmi:MI_Metadata>
        </xsl:result-document>
        <xsl:for-each select="//contact">
            <xsl:variable name="contactRole">
                <xsl:choose>
                    <xsl:when test="role='ArchiveSpecialist'">custodian</xsl:when>
                    <xsl:when test="role='CoInvestigator'">principalInvestigator</xsl:when>
                    <xsl:when test="role='Contributor'">collaborator</xsl:when>
                    <xsl:when test="role='DataProducer'">originator</xsl:when>
                    <xsl:when test="role='DeputyPI'">principalInvestigator</xsl:when>
                    <xsl:when test="role='FormerPI'">principalInvestigator</xsl:when>
                    <xsl:when test="role='GeneralContact'">pointOfContact</xsl:when>
                    <xsl:when test="role='MetadataContact'">pointOfContact</xsl:when>
                    <xsl:when test="role='PrincipalInvestigator'">principalInvestigator</xsl:when>
                    <xsl:when test="role='ProjectScientist'">collaborator</xsl:when>
                    <xsl:when test="role='Publisher'">publisher</xsl:when>
                    <xsl:when test="role='Scientist'">collaborator</xsl:when>
                    <xsl:when test="role='TeamLeader'">collaborator</xsl:when>
                    <xsl:when test="role='TeamMember'">collaborator</xsl:when>
                    <xsl:when test="role='TechnicalContact'">collaborator</xsl:when>
                </xsl:choose>
            </xsl:variable>
            <xsl:result-document method="xml" href="ISO/CI_ResponsibleParty-{@uuid}.xml">
                <gmd:CI_ResponsibleParty uuid="{@uuid}">
                    <xsl:if test="name">
                        <gmd:individualName>
                            <gco:CharacterString>
                                <xsl:value-of select="name"/>
                            </gco:CharacterString>
                        </gmd:individualName>
                    </xsl:if>
                    <xsl:if test="organization">
                        <gmd:organisationName>
                            <gco:CharacterString>
                                <xsl:value-of select="organization"/>
                            </gco:CharacterString>
                        </gmd:organisationName>
                    </xsl:if>
                    <xsl:if test="telephone,address">
                        <gmd:contactInfo>
                            <gmd:CI_Contact>
                                <xsl:if test="telephone">
                                    <gmd:phone>
                                        <gmd:CI_Telephone>
                                            <xsl:for-each select="telephone/voice">
                                                <gmd:voice>
                                                    <gco:CharacterString>
                                                        <xsl:value-of select="normalize-space(.)"/>
                                                    </gco:CharacterString>
                                                </gmd:voice>
                                            </xsl:for-each>
                                            <xsl:if test="telephone/fax">
                                                <gmd:facsimile>
                                                    <gco:CharacterString>
                                                        <xsl:value-of select="telephone/fax"/>
                                                    </gco:CharacterString>
                                                </gmd:facsimile>
                                            </xsl:if>
                                        </gmd:CI_Telephone>
                                    </gmd:phone>
                                </xsl:if>
                                <xsl:if test="address">
                                    <gmd:address>
                                        <gmd:CI_Address>
                                            <xsl:if test="address/deliveryPoint">
                                                <gmd:deliveryPoint>
                                                    <gco:CharacterString>
                                                        <xsl:value-of select="address/deliveryPoint"/>
                                                    </gco:CharacterString>
                                                </gmd:deliveryPoint>
                                            </xsl:if>
                                            <xsl:if test="address/city">
                                                <gmd:city>
                                                    <gco:CharacterString>
                                                        <xsl:value-of select="address/city"/>
                                                    </gco:CharacterString>
                                                </gmd:city>
                                            </xsl:if>
                                            <xsl:if test="address/state">
                                                <gmd:administrativeArea>
                                                    <gco:CharacterString>
                                                        <xsl:value-of select="address/state"/>
                                                    </gco:CharacterString>
                                                </gmd:administrativeArea>
                                            </xsl:if>
                                            <xsl:if test="address/postalCode">
                                                <gmd:postalCode>
                                                    <gco:CharacterString>
                                                        <xsl:value-of select="address/postalCode"/>
                                                    </gco:CharacterString>
                                                </gmd:postalCode>
                                            </xsl:if>
                                            <xsl:if test="address/country">
                                                <gmd:country>
                                                    <gco:CharacterString>
                                                        <xsl:value-of select="address/country"/>
                                                    </gco:CharacterString>
                                                </gmd:country>
                                            </xsl:if>
                                            <xsl:for-each select="address/email">
                                                <gmd:electronicMailAddress>
                                                    <gco:CharacterString>
                                                        <xsl:value-of select="normalize-space(.)"/>
                                                    </gco:CharacterString>
                                                </gmd:electronicMailAddress>
                                            </xsl:for-each>
                                        </gmd:CI_Address>
                                    </gmd:address>
                                </xsl:if>
                                <xsl:if test="note">
                                    <gmd:contactInstructions>
                                        <gco:CharacterString>
                                            <xsl:value-of select="note"/>
                                        </gco:CharacterString>
                                    </gmd:contactInstructions>
                                </xsl:if>
                            </gmd:CI_Contact>
                        </gmd:contactInfo>
                    </xsl:if>
                    <gmd:role>
                        <gmd:CI_RoleCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#CI_RoleCode">
                            <xsl:attribute name="codeListValue" select="$contactRole"/>
                            <xsl:value-of select="$contactRole"/>
                        </gmd:CI_RoleCode>
                    </gmd:role>
                </gmd:CI_ResponsibleParty>
            </xsl:result-document>
        </xsl:for-each>
        <xsl:for-each select="//instrument">
            <xsl:result-document method="xml" href="ISO/MI_Instrument-{@uuid}.xml">
                <gmi:MI_Instrument uuid="{@uuid}">
                    <gmi:citation>
                        <gmd:CI_Citation>
                            <gmd:title>
                                <gco:CharacterString>
                                    <xsl:value-of select="normalize-space(./name)"/>
                                </gco:CharacterString>
                            </gmd:title>
                            <xsl:for-each select="alternateName">
                                <gmd:alternateTitle>
                                    <gco:CharacterString>
                                        <xsl:value-of select="normalize-space(.)"/>
                                    </gco:CharacterString>
                                </gmd:alternateTitle>
                            </xsl:for-each>
                            <gmd:date>
                                <gmd:CI_Date>
                                    <gmd:date>
                                        <gco:DateTime>
                                            <xsl:value-of select="normalize-space(operatingSpanStart)"/>
                                        </gco:DateTime>
                                    </gmd:date>
                                    <gmd:dateType>
                                        <gmd:CI_DateTypeCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode" codeListValue="creation">creation</gmd:CI_DateTypeCode>
                                    </gmd:dateType>
                                </gmd:CI_Date>
                            </gmd:date>

                            <gmd:identifier>
                                <gmd:MD_Identifier>
                                    <gmd:code>
                                        <gco:CharacterString>
                                            <xsl:value-of select="normalize-space(@spaseID)"/>
                                        </gco:CharacterString>
                                    </gmd:code>
                                </gmd:MD_Identifier>
                            </gmd:identifier>
                            <xsl:choose>
                                <xsl:when test="//instrumentContact">
                                    <xsl:for-each select="//instrumentContact">
                                        <gmd:citedResponsibleParty>
                                            <xsl:call-template name="CI_ResponsibleParty">
                                                <xsl:with-param name="spaseID" select="@spaseIDRef"/>
                                                <xsl:with-param name="xlinkISO" select="@uuidRef"/>
                                                <xsl:with-param name="title" select="@title"/>
                                                <xsl:with-param name="contactRole" select="@contactRole"/>
                                            </xsl:call-template>
                                        </gmd:citedResponsibleParty>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:otherwise>
                                    <gmd:citedResponsibleParty gco:nilReason="missing"/>
                                </xsl:otherwise>
                            </xsl:choose>

                            <xsl:if test="caveats">
                                <gmd:otherCitationDetails>
                                    <gco:CharacterString>
                                        <xsl:value-of select="normalize-space(caveats)"/>
                                    </gco:CharacterString>
                                </gmd:otherCitationDetails>
                            </xsl:if>
                        </gmd:CI_Citation>
                    </gmi:citation>
                    <gmi:identifier>
                        <gmd:MD_Identifier>
                            <gmd:code>
                                <gco:CharacterString>
                                    <xsl:value-of select="normalize-space(@spaseID)"/>
                                </gco:CharacterString>
                            </gmd:code>
                        </gmd:MD_Identifier>
                    </gmi:identifier>
                    <gmi:type>
                        <gco:CharacterString>
                            <xsl:value-of select="normalize-space(instrumentType)"/>
                        </gco:CharacterString>
                    </gmi:type>
                    <xsl:if test="description">
                        <gmi:description>
                            <gco:CharacterString>
                                <xsl:value-of select="normalize-space(description)"/>
                            </gco:CharacterString>
                        </gmi:description>
                    </xsl:if>
                    <gmi:mountedOn>
                        <xsl:attribute name="xlink:href">
                            <xsl:value-of select="concat('#',observatoryID)"/>
                        </xsl:attribute>
                        <xsl:attribute name="xlink:title">Observatory ID</xsl:attribute>
                    </gmi:mountedOn>
                </gmi:MI_Instrument>

            </xsl:result-document>
        </xsl:for-each>

        <xsl:for-each select="//observatory">
            <xsl:result-document method="xml" href="ISO/MI_Platform-{@uuid}.xml">
                <gmi:MI_Platform uuid="{@uuid}">
                    <gmi:citation>
                        <gmd:CI_Citation>
                            <gmd:title>
                                <gco:CharacterString>
                                    <xsl:value-of select="name"/>
                                </gco:CharacterString>
                            </gmd:title>
                            <gmd:date>
                                <gmd:CI_Date>
                                    <xsl:choose>
                                        <xsl:when test="operatingSpanStart">
                                            <gmd:date>
                                                <gco:DateTime>
                                                    <xsl:value-of select="operatingSpanStart"/>
                                                </gco:DateTime>
                                            </gmd:date>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:attribute name="gco:nilReason">missing</xsl:attribute>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <gmd:dateType>
                                        <gmd:CI_DateTypeCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode" codeListValue="creation">creation</gmd:CI_DateTypeCode>
                                    </gmd:dateType>
                                </gmd:CI_Date>
                            </gmd:date>
                            <xsl:if test="spaseID">
                                <gmd:identifier>
                                    <gmd:MD_Identifier>
                                        <gmd:code>
                                            <gco:CharacterString>
                                                <xsl:value-of select="spaseID"/>
                                            </gco:CharacterString>
                                        </gmd:code>
                                    </gmd:MD_Identifier>
                                </gmd:identifier>
                            </xsl:if>
                            <xsl:choose>
                                <xsl:when test="//observatoryContact">
                                    <xsl:for-each select="//observatoryContact">
                                        <gmd:citedResponsibleParty>
                                            <xsl:call-template name="CI_ResponsibleParty">
                                                <xsl:with-param name="spaseID" select="@spaseIDRef"/>
                                                <xsl:with-param name="xlinkISO" select="@uuidRef"/>
                                                <xsl:with-param name="title" select="@title"/>
                                                <xsl:with-param name="contactRole" select="@contactRole"/>
                                            </xsl:call-template>
                                        </gmd:citedResponsibleParty>
                                    </xsl:for-each>
                                </xsl:when>
                                <xsl:otherwise>
                                    <gmd:citedResponsibleParty gco:nilReason="missing"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </gmd:CI_Citation>
                    </gmi:citation>
                    <xsl:if test="@spaseID">
                        <gmi:identifier>
                            <gmd:MD_Identifier>
                                <gmd:code>
                                    <gco:CharacterString>
                                        <xsl:value-of select="@spaseID"/>
                                    </gco:CharacterString>
                                </gmd:code>
                            </gmd:MD_Identifier>
                        </gmi:identifier>
                    </xsl:if>
                    <xsl:if test="description">
                        <gmi:description>
                            <gco:CharacterString>
                                <xsl:value-of select="description"/>
                            </gco:CharacterString>
                        </gmi:description>
                    </xsl:if>
                    <xsl:for-each select="hasInstrument/@spaseIDRef">
                        <gmi:instrument>
                            <xsl:attribute name="xlink:href">
                                <xsl:value-of select="concat('#', normalize-space(.))"/>
                            </xsl:attribute>
                        </gmi:instrument>
                    </xsl:for-each>
                </gmi:MI_Platform>


            </xsl:result-document>
        </xsl:for-each>

    </xsl:template>
    <xsl:template match="node()" name="copyAll">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template name="CI_ResponsibleParty">
        <xsl:param name="contactRole"/>
        <xsl:param name="spaseID"/>
        <xsl:param name="xlinkISO"/>
        <xsl:param name="title"/>
        <xsl:choose>
            <xsl:when test="$xlinkISO">
                <xsl:attribute name="xlink:href" select="concat('http://www.ngdc.noaa.gov/docucomp/',$xlinkISO)"/>
                <xsl:attribute name="xlink:title" select="$title"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="../..//contact">
                    <xsl:choose>
                        <xsl:when test="normalize-space(spaseID)=$spaseID">
                            <gmd:CI_ResponsibleParty>
                                <xsl:if test="name">
                                    <gmd:individualName>
                                        <gco:CharacterString>
                                            <xsl:value-of select="name"/>
                                        </gco:CharacterString>
                                    </gmd:individualName>
                                </xsl:if>
                                <xsl:if test="organization">
                                    <gmd:organisationName>
                                        <gco:CharacterString>
                                            <xsl:value-of select="organization"/>
                                        </gco:CharacterString>
                                    </gmd:organisationName>
                                </xsl:if>
                                <xsl:if test="telephone,address">
                                    <gmd:contactInfo>
                                        <gmd:CI_Contact>
                                            <xsl:if test="telephone">
                                                <gmd:phone>
                                                    <gmd:CI_Telephone>
                                                        <xsl:for-each select="telephone/voice">
                                                            <gmd:voice>
                                                                <gco:CharacterString>
                                                                    <xsl:value-of select="normalize-space(.)"/>
                                                                </gco:CharacterString>
                                                            </gmd:voice>
                                                        </xsl:for-each>
                                                        <xsl:if test="telephone/fax">
                                                            <gmd:facsimile>
                                                                <gco:CharacterString>
                                                                    <xsl:value-of select="telephone/fax"/>
                                                                </gco:CharacterString>
                                                            </gmd:facsimile>
                                                        </xsl:if>
                                                    </gmd:CI_Telephone>
                                                </gmd:phone>
                                            </xsl:if>
                                            <xsl:if test="address">
                                                <gmd:address>
                                                    <gmd:CI_Address>
                                                        <xsl:if test="address/deliveryPoint">
                                                            <gmd:deliveryPoint>
                                                                <gco:CharacterString>
                                                                    <xsl:value-of select="address/deliveryPoint"/>
                                                                </gco:CharacterString>
                                                            </gmd:deliveryPoint>
                                                        </xsl:if>
                                                        <xsl:if test="address/city">
                                                            <gmd:city>
                                                                <gco:CharacterString>
                                                                    <xsl:value-of select="address/city"/>
                                                                </gco:CharacterString>
                                                            </gmd:city>
                                                        </xsl:if>
                                                        <xsl:if test="address/state">
                                                            <gmd:administrativeArea>
                                                                <gco:CharacterString>
                                                                    <xsl:value-of select="address/state"/>
                                                                </gco:CharacterString>
                                                            </gmd:administrativeArea>
                                                        </xsl:if>
                                                        <xsl:if test="address/postalCode">
                                                            <gmd:postalCode>
                                                                <gco:CharacterString>
                                                                    <xsl:value-of select="address/postalCode"/>
                                                                </gco:CharacterString>
                                                            </gmd:postalCode>
                                                        </xsl:if>
                                                        <xsl:if test="address/country">
                                                            <gmd:country>
                                                                <gco:CharacterString>
                                                                    <xsl:value-of select="address/country"/>
                                                                </gco:CharacterString>
                                                            </gmd:country>
                                                        </xsl:if>
                                                        <xsl:for-each select="address/email">
                                                            <gmd:electronicMailAddress>
                                                                <gco:CharacterString>
                                                                    <xsl:value-of select="normalize-space(.)"/>
                                                                </gco:CharacterString>
                                                            </gmd:electronicMailAddress>
                                                        </xsl:for-each>
                                                    </gmd:CI_Address>
                                                </gmd:address>
                                            </xsl:if>
                                            <xsl:if test="note">
                                                <gmd:contactInstructions>
                                                    <gco:CharacterString>
                                                        <xsl:value-of select="note"/>
                                                    </gco:CharacterString>
                                                </gmd:contactInstructions>
                                            </xsl:if>
                                        </gmd:CI_Contact>
                                    </gmd:contactInfo>
                                </xsl:if>
                                <gmd:role>
                                    <gmd:CI_RoleCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#CI_RoleCode">
                                        <xsl:attribute name="codeListValue" select="$contactRole"/>
                                        <xsl:value-of select="$contactRole"/>
                                    </gmd:CI_RoleCode>
                                </gmd:role>
                            </gmd:CI_ResponsibleParty>
                        </xsl:when>
                    </xsl:choose>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="CI_OnlineResource">
        <xsl:param name="function"/>
        <gmd:CI_OnlineResource>
            <xsl:if test="URL">
                <gmd:linkage>
                    <gmd:URL>
                        <xsl:value-of select="normalize-space(URL)"/>
                    </gmd:URL>
                </gmd:linkage>
                <gmd:name>
                    <gco:CharacterString>
                        <xsl:value-of select="normalize-space(name)"/>
                    </gco:CharacterString>
                </gmd:name>
                <gmd:description>
                    <gco:CharacterString>
                        <xsl:value-of select="normalize-space(description)"/>
                    </gco:CharacterString>
                </gmd:description>
            </xsl:if>
            <gmd:function>
                <gmd:CI_OnLineFunctionCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#CI_OnLineFunctionCode" codeListValue="{$function}">
                    <xsl:value-of select="$function"/>
                </gmd:CI_OnLineFunctionCode>
            </gmd:function>
        </gmd:CI_OnlineResource>
    </xsl:template>
    <xsl:template name="instrument">
        <xsl:param name="idRefSpase" select="@spaseIDRef"/>
        <xsl:param name="xlinkISO" select="@xlinkISO"/>
        <xsl:param name="title" select="@title"/>
        <xsl:choose>
            <xsl:when test="$xlinkISO">
                <gmi:instrument>
                    <xsl:attribute name="xlink:href" select="concat('http://www.ngdc.noaa.gov/docucomp/',$xlinkISO)"/>
                    <xsl:attribute name="xlink:title" select="$title"/>
                </gmi:instrument>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="//instrument">
                    <xsl:if test="@spaseID=$idRefSpase">
                        <xsl:comment><xsl:value-of select="spaseID"/> <xsl:value-of select="$idRefSpase"/></xsl:comment>
                        <gmi:instrument xlink:title="{$title}">
                            <gmi:MI_Instrument>
                                <gmi:citation>
                                    <gmd:CI_Citation>
                                        <gmd:title>
                                            <gco:CharacterString>
                                                <xsl:value-of select="normalize-space(./name)"/>
                                            </gco:CharacterString>
                                        </gmd:title>
                                        <xsl:for-each select="alternateName">
                                            <gmd:alternateTitle>
                                                <gco:CharacterString>
                                                    <xsl:value-of select="normalize-space(.)"/>
                                                </gco:CharacterString>
                                            </gmd:alternateTitle>
                                        </xsl:for-each>
                                        <gmd:date>
                                            <gmd:CI_Date>
                                                <gmd:date>
                                                    <gco:DateTime>
                                                        <xsl:value-of select="normalize-space(operatingSpanStart)"/>
                                                    </gco:DateTime>
                                                </gmd:date>
                                                <gmd:dateType>
                                                    <gmd:CI_DateTypeCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode" codeListValue="creation">creation</gmd:CI_DateTypeCode>
                                                </gmd:dateType>
                                            </gmd:CI_Date>
                                        </gmd:date>

                                        <gmd:identifier>
                                            <gmd:MD_Identifier>
                                                <gmd:code>
                                                    <gco:CharacterString>
                                                        <xsl:value-of select="normalize-space(@spaseID)"/>
                                                    </gco:CharacterString>
                                                </gmd:code>
                                            </gmd:MD_Identifier>
                                        </gmd:identifier>
                                        <xsl:choose>
                                            <xsl:when test="//instrumentContact">
                                                <xsl:for-each select="//instrumentContact">
                                                    <gmd:citedResponsibleParty>
                                                        <xsl:call-template name="CI_ResponsibleParty">
                                                            <xsl:with-param name="spaseID" select="@spaseIDRef"/>
                                                            <xsl:with-param name="xlinkISO" select="@uuidRef"/>
                                                            <xsl:with-param name="title" select="@title"/>
                                                            <xsl:with-param name="contactRole" select="@contactRole"/>
                                                        </xsl:call-template>
                                                    </gmd:citedResponsibleParty>
                                                </xsl:for-each>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <gmd:citedResponsibleParty gco:nilReason="missing"/>
                                            </xsl:otherwise>
                                        </xsl:choose>

                                        <xsl:if test="caveats">
                                            <gmd:otherCitationDetails>
                                                <gco:CharacterString>
                                                    <xsl:value-of select="normalize-space(caveats)"/>
                                                </gco:CharacterString>
                                            </gmd:otherCitationDetails>
                                        </xsl:if>
                                    </gmd:CI_Citation>
                                </gmi:citation>
                                <gmi:identifier>
                                    <gmd:MD_Identifier>
                                        <gmd:code>
                                            <gco:CharacterString>
                                                <xsl:value-of select="normalize-space(@spaseID)"/>
                                            </gco:CharacterString>
                                        </gmd:code>
                                    </gmd:MD_Identifier>
                                </gmi:identifier>
                                <gmi:type>
                                    <gco:CharacterString>
                                        <xsl:value-of select="normalize-space(instrumentType)"/>
                                    </gco:CharacterString>
                                </gmi:type>
                                <xsl:if test="description">
                                    <gmi:description>
                                        <gco:CharacterString>
                                            <xsl:value-of select="normalize-space(description)"/>
                                        </gco:CharacterString>
                                    </gmi:description>
                                </xsl:if>
                                <gmi:mountedOn>
                                    <xsl:attribute name="xlink:href">
                                        <xsl:value-of select="concat('#',observatoryID)"/>
                                    </xsl:attribute>
                                    <xsl:attribute name="xlink:title">Observatory ID</xsl:attribute>
                                </gmi:mountedOn>
                            </gmi:MI_Instrument>
                        </gmi:instrument>
                    </xsl:if>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="platform">
        <xsl:param name="idRefSpase" select="@spaseIDRef"/>
        <xsl:param name="xlinkISO" select="@xlinkISO"/>
        <xsl:param name="title" select="@title"/>
        <xsl:choose>
            <xsl:when test="$xlinkISO">
                <gmi:platform>
                    <xsl:attribute name="xlink:href" select="concat('http://www.ngdc.noaa.gov/docucomp/',$xlinkISO)"/>
                    <xsl:attribute name="xlink:title" select="$title"/>
                </gmi:platform>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="//observatory">
                    <xsl:if test="spaseID=$idRefSpase">
                        <gmi:platform>
                            <gmi:MI_Platform>
                                <gmi:citation>
                                    <gmd:CI_Citation>
                                        <gmd:title>
                                            <gco:CharacterString>
                                                <xsl:value-of select="name"/>
                                            </gco:CharacterString>
                                        </gmd:title>
                                        <gmd:date>
                                            <gmd:CI_Date>
                                                <xsl:choose>
                                                    <xsl:when test="operatingSpanStart">
                                                        <gmd:date>
                                                            <gco:DateTime>
                                                                <xsl:value-of select="operatingSpanStart"/>
                                                            </gco:DateTime>
                                                        </gmd:date>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:attribute name="gco:nilReason">missing</xsl:attribute>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                                <gmd:dateType>
                                                    <gmd:CI_DateTypeCode codeList="http://www.ngdc.noaa.gov/metadata/published/xsd/schema/resources/Codelist/gmxCodelists.xml#CI_DateTypeCode" codeListValue="creation">creation</gmd:CI_DateTypeCode>
                                                </gmd:dateType>
                                            </gmd:CI_Date>
                                        </gmd:date>
                                        <xsl:if test="spaseID">
                                            <gmd:identifier>
                                                <gmd:MD_Identifier>
                                                    <gmd:code>
                                                        <gco:CharacterString>
                                                            <xsl:value-of select="spaseID"/>
                                                        </gco:CharacterString>
                                                    </gmd:code>
                                                </gmd:MD_Identifier>
                                            </gmd:identifier>
                                        </xsl:if>
                                        <xsl:choose>
                                            <xsl:when test="//observatoryContact">
                                                <xsl:for-each select="//observatoryContact">
                                                    <gmd:citedResponsibleParty>
                                                        <xsl:call-template name="CI_ResponsibleParty">
                                                            <xsl:with-param name="spaseID" select="@spaseIDRef"/>
                                                            <xsl:with-param name="xlinkISO" select="@uuidRef"/>
                                                            <xsl:with-param name="title" select="@title"/>
                                                            <xsl:with-param name="contactRole" select="@contactRole"/>
                                                        </xsl:call-template>
                                                    </gmd:citedResponsibleParty>
                                                </xsl:for-each>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <gmd:citedResponsibleParty gco:nilReason="missing"/>
                                            </xsl:otherwise>
                                        </xsl:choose>

                                    </gmd:CI_Citation>
                                </gmi:citation>
                                <xsl:if test="spaseID">
                                    <gmi:identifier>
                                        <gmd:MD_Identifier>
                                            <gmd:code>
                                                <gco:CharacterString>
                                                    <xsl:value-of select="spaseID"/>
                                                </gco:CharacterString>
                                            </gmd:code>
                                        </gmd:MD_Identifier>
                                    </gmi:identifier>
                                </xsl:if>
                                <xsl:if test="description">
                                    <gmi:description>
                                        <gco:CharacterString>
                                            <xsl:value-of select="description"/>
                                        </gco:CharacterString>
                                    </gmi:description>
                                </xsl:if>
                                <xsl:for-each select="hasInstrument/@spaseIDRef">
                                    <gmi:instrument>
                                        <xsl:attribute name="xlink:href">
                                            <xsl:value-of select="concat('#', normalize-space(.))"/>
                                        </xsl:attribute>
                                    </gmi:instrument>
                                </xsl:for-each>
                            </gmi:MI_Platform>
                        </gmi:platform>
                    </xsl:if>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>
</xsl:stylesheet>
