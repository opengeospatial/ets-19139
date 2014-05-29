package org.opengis.cite.iso19136.general;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSElementDeclaration;

/**
 * Provides information about the types of objects declared in a GML application
 * schema. Clause 21.2.1 in ISO 19136 requires a conforming schema to include
 * specific types of geographic objects.
 */
public class AppSchemaInfo {

    private Set<GMLObjectType> gmlContentTypes = EnumSet
            .noneOf(GMLObjectType.class);
    private List<XSElementDeclaration> featureTypes;
    private Set<XSComplexTypeDefinition> featureDefinitions;
    private List<XSElementDeclaration> geometryTypes;
    private List<XSElementDeclaration> topoTypes;
    private List<XSElementDeclaration> timeTypes;
    private List<XSElementDeclaration> crsTypes;
    private List<XSElementDeclaration> coverageTypes;
    private List<XSElementDeclaration> observationTypes;
    private List<XSElementDeclaration> definitionTypes;

    public Set<GMLObjectType> getGMLContentTypes() {
        return gmlContentTypes;
    }

    public List<XSElementDeclaration> getFeatureTypes() {
        return featureTypes;
    }

    public void setFeatureTypes(List<XSElementDeclaration> features) {
        if (!features.isEmpty()) {
            this.featureTypes = Collections.unmodifiableList(features);
            gmlContentTypes.add(GMLObjectType.FEATURE_TYPE);
        }
    }

    public Set<XSComplexTypeDefinition> getFeatureDefinitions() {
        return featureDefinitions;
    }

    public void setFeatureDefinitions(Set<XSComplexTypeDefinition> typeDefs) {
        if (null != typeDefs) {
            this.featureDefinitions = Collections.unmodifiableSet(typeDefs);
        }
    }

    public List<XSElementDeclaration> getGeometryTypes() {
        return geometryTypes;
    }

    public void setGeometryTypes(List<XSElementDeclaration> geometries) {
        if (!geometries.isEmpty()) {
            this.geometryTypes = Collections.unmodifiableList(geometries);
            gmlContentTypes.add(GMLObjectType.GEOMETRY);
        }
    }

    public List<XSElementDeclaration> getTimeTypes() {
        return timeTypes;
    }

    public void setTimeTypes(List<XSElementDeclaration> timeTypes) {
        if (!timeTypes.isEmpty()) {
            this.timeTypes = Collections.unmodifiableList(timeTypes);
            gmlContentTypes.add(GMLObjectType.TIME);
        }
    }

    public List<XSElementDeclaration> getTopoTypes() {
        return topoTypes;
    }

    public void setTopoTypes(List<XSElementDeclaration> topoTypes) {
        if (!topoTypes.isEmpty()) {
            this.topoTypes = Collections.unmodifiableList(topoTypes);
            gmlContentTypes.add(GMLObjectType.TOPOLOGY);
        }
    }

    public List<XSElementDeclaration> getCoverageTypes() {
        return coverageTypes;
    }

    public void setCoverageTypes(List<XSElementDeclaration> coverageTypes) {
        if (!coverageTypes.isEmpty()) {
            this.coverageTypes = Collections.unmodifiableList(coverageTypes);
            gmlContentTypes.add(GMLObjectType.COVERAGE);
        }
    }

    public List<XSElementDeclaration> getCrsTypes() {
        return crsTypes;
    }

    public void setCrsTypes(List<XSElementDeclaration> crsTypes) {
        if (!crsTypes.isEmpty()) {
            this.crsTypes = Collections.unmodifiableList(crsTypes);
            gmlContentTypes.add(GMLObjectType.CRS);
        }
    }

    public List<XSElementDeclaration> getDefinitionTypes() {
        return definitionTypes;
    }

    public void setDefinitionTypes(List<XSElementDeclaration> definitionTypes) {
        if (!definitionTypes.isEmpty()) {
            this.definitionTypes = Collections
                    .unmodifiableList(definitionTypes);
            gmlContentTypes.add(GMLObjectType.DEFINITION);
        }
    }

    public List<XSElementDeclaration> getObservationTypes() {
        return observationTypes;
    }

    public void setObservationTypes(List<XSElementDeclaration> obsTypes) {
        if (!obsTypes.isEmpty()) {
            this.observationTypes = Collections.unmodifiableList(obsTypes);
            gmlContentTypes.add(GMLObjectType.OBSERVATION);
        }
    }

    /**
     * Indicates whether or not the application schema includes any of the
     * schema components identified in clause 21.2.1. In essence, at least one
     * type of GML object must be defined.
     * 
     * @return {@code true} if the required schema components are present;
     *         {@code false} otherwise.
     */
    public boolean conforms() {
        return !gmlContentTypes.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("AppSchemaInfo [\n");
        str.append("featureTypes: ").append(featureTypes).append('\n');
        str.append("geometryTypes: ").append(geometryTypes).append('\n');
        str.append("topoTypes: ").append(topoTypes).append('\n');
        str.append("timeTypes: ").append(timeTypes).append('\n');
        str.append("crsTypes: ").append(crsTypes).append('\n');
        str.append("coverageTypes: ").append(coverageTypes).append('\n');
        str.append("observationTypes: ").append(observationTypes).append('\n');
        str.append("definitionTypes: ").append(definitionTypes).append('\n');
        str.append("]\n");
        return str.toString();
    }
}
