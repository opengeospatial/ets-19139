package org.opengis.cite.iso19136.general;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

/**
 * Contains various constants pertaining to the standard GML 3.2 content model
 * as specified in ISO 19136:2007.
 */
public class GML32 {

    /**
     * The namespace name for the GML 3.2 schema.
     */
    public static final String NS_NAME = "http://www.opengis.net/gml/3.2";
    /**
     * Total number of top-level element declarations in the GML 3.2 schema.
     */
    public static final int TOTAL_GLOBAL_ELEMS = 474;
    /**
     * Abstract head of the substitution group for all GML objects (see clause
     * 7.2.2.2).
     */
    public static final String ABSTRACT_GML = "AbstractGML";
    /**
     * The base type for all GML object type definitions (see clause 7.2.2.2).
     */
    public static final String ABSTRACT_GML_TYPE = "AbstractGMLType";
    /**
     * Abstract head of the substitution group for all GML feature instances
     * (see clause 9.3.2).
     */
    public static final String ABSTRACT_FEATURE = "AbstractFeature";
    /**
     * The base type for all GML feature type definitions (see clause 9.3.1).
     */
    public static final String ABSTRACT_FEATURE_TYPE = "AbstractFeatureType";
    /**
     * The base type for feature member property types (see clause 9.9.1).
     */
    public static final String FEATURE_MEMBER_TYPE = "AbstractFeatureMemberType";
    /**
     * Abstract head of the substitution group for all GML geometry elements
     * (see clause 10.1.3.4).
     */
    public static final String ABSTRACT_GEOMETRY = "AbstractGeometry";
    /**
     * The base type for all geometry type definitions (see clause 10.1.3.1).
     */
    public static final String ABSTRACT_GEOMETRY_TYPE = "AbstractGeometryType";
    /**
     * Abstract head of the substitution group for all (continuous) curve
     * elements (see clause 10.4.1).
     */
    public static final String ABSTRACT_CURVE = "AbstractCurve";
    /**
     * Abstract head of the substitution group for all (continuous) surface
     * elements (see clause 10.5.1).
     */
    public static final String ABSTRACT_SURFACE = "AbstractSurface";
    /**
     * Generic geometry property type which is typically restricted in an
     * application schema (see clause 10.1.3.5).
     */
    public static final String GEOM_PROP_TYPE = "GeometryPropertyType";
    /**
     * A property that has a gml:Point as its value domain (see clause 10.3.2).
     */
    public static final String POINT_PROP_TYPE = "PointPropertyType";
    /**
     * Generic geometry array property type which is typically restricted in an
     * application schema (see clause 10.1.3.6).
     */
    public static final String GEOM_ARRAY_PROP_TYPE = "GeometryArrayPropertyType";
    /**
     * Immutable set of all predefined GML geometry property types (see clause
     * 9.5, Table 4).
     */
    public static final Set<String> GEOM_PROP_TYPE_SET;
    static {
        Set<String> aSet = new HashSet<String>();
        aSet.add(POINT_PROP_TYPE);
        aSet.add("CurvePropertyType");
        aSet.add("SurfacePropertyType");
        aSet.add("SolidPropertyType");
        aSet.add("MultiPointPropertyType");
        aSet.add("MultiCurvePropertyType");
        aSet.add("MultiSurfacePropertyType");
        aSet.add("MultiSolidPropertyType");
        aSet.add("MultiGeometryPropertyType");
        aSet.add("PointArrayPropertyType");
        aSet.add("CurveArrayPropertyType");
        aSet.add("SurfaceArrayPropertyType");
        aSet.add("SolidArrayPropertyType");
        GEOM_PROP_TYPE_SET = Collections.unmodifiableSet(aSet);
    }
    /**
     * Abstract head of the substitution group for all GML topology elements
     * (see clause 13.2).
     */
    public static final String ABSTRACT_TOPO = "AbstractTopology";
    /**
     * The base type for all topology type definitions (see clause 13.2).
     */
    public static final String ABSTRACT_TOPO_TYPE = "AbstractTopologyType";
    /**
     * Immutable set of all predefined GML topology property types (see clause
     * 9.6, Table 5).
     */
    public static final Set<String> TOPO_PROP_TYPE_SET;
    static {
        Set<String> aSet = new HashSet<String>();
        aSet.add("NodePropertyType");
        aSet.add("DirectedNodePropertyType");
        aSet.add("DirectedEdgePropertyType");
        aSet.add("DirectedFacePropertyType");
        aSet.add("DirectedTopoSolidPropertyType");
        aSet.add("TopoPointPropertyType");
        aSet.add("TopoCurvePropertyType");
        aSet.add("TopoSurfacePropertyType");
        aSet.add("TopoSolidPropertyType");
        aSet.add("TopoVolumePropertyType");
        aSet.add("TopoComplexPropertyType");
        TOPO_PROP_TYPE_SET = Collections.unmodifiableSet(aSet);
    }
    /**
     * Abstract head of the substitution group for all temporal primitives and
     * complexes (see clause 14.2.1.1).
     */
    public static final String ABSTRACT_TIME = "AbstractTimeObject";
    /**
     * The base type for all temporal type definitions (see clause 14.2.1.1).
     */
    public static final String ABSTRACT_TIME_TYPE = "AbstractTimeObjectType";
    /**
     * Immutable set of all predefined GML temporal property types (see clause
     * 9.7, Table 6).
     */
    public static final Set<String> TIME_PROP_TYPE_SET;
    static {
        Set<String> aSet = new HashSet<String>();
        aSet.add("TimePrimitivePropertyType");
        aSet.add("TimeGeometricPrimitivePropertyType");
        aSet.add("TimeInstantPropertyType");
        aSet.add("TimePeriodPropertyType");
        aSet.add("TimeTopologyPrimitivePropertyType");
        aSet.add("TimeEdgePropertyType");
        aSet.add("TimeNodePropertyType");
        aSet.add("TimeTopologyComplexPropertyType");
        aSet.add("TimeOrdinalEraPropertyType");
        aSet.add("TimeCalendarPropertyType");
        aSet.add("TimeCalendarEraPropertyType");
        aSet.add("TimeClockPropertyType");
        aSet.add("TimePositionType");
        aSet.add("TimeIntervalLengthType");
        TIME_PROP_TYPE_SET = Collections.unmodifiableSet(aSet);
    }
    /**
     * Abstract head of the substitution group for all coordinate reference
     * system (CRS) descriptions (see clause 12.2.3.1).
     */
    public static final String ABSTRACT_CRS = "AbstractCRS";
    /**
     * Abstract head of the substitution group for all GML coverage elements
     * (see clause 19.3.1).
     */
    public static final String ABSTRACT_COVERAGE = "AbstractCoverage";
    /**
     * Head of the substitution group for all GML observations (see clause
     * 18.2.2).
     */
    public static final String OBSERVATION = "Observation";
    /**
     * Head of the substitution group for all definition elements, including
     * dictionaries (see clause 15.2).
     */
    public static final String DEFINITION = "Definition";
    /**
     * Abstract head of the substitution group for all GML value elements (see
     * clauses 16.4.2, D.3.15).
     */
    public static final String ABSTRACT_VALUE = "AbstractValue";
    /**
     * Base type for defining metadata property types (see clause 7.2.6.1).
     */
    public static final String MD_PROP_TYPE = "AbstractMetadataPropertyType";
    /**
     * Base type for defining object property types that represent the members
     * of a GML object collection (see clause 7.2.5.1).
     */
    public static final String MEMBER_PROP_TYPE = "AbstractMemberType";
    /**
     * Envelope defines an extent using a pair of positions defining opposite
     * corners in arbitrary dimensions (see clause 10.1.4.6).
     */
    public static final String ENVELOPE = "Envelope";
    /**
     * A gml:Point is a realization of GM_Point from ISO 19107 (10.3.1).
     */
    public static final String POINT = "Point";
    /**
     * A gml:Curve is a realization of GM_Curve from ISO 19107 (10.4.5).
     */
    public static final String CURVE = "Curve";
    /**
     * A gml:CompositeCurve is a realization of GM_CompositeCurve from ISO 19107
     * (11.2.2.2).
     */
    public static final String COMP_CURVE = "CompositeCurve";
    /**
     * Indicates that a geometry or topology element has positive orientation.
     */
    public static final String ORIENT_POS = "+";
    /**
     * Indicates that a geometry or topology element has negative orientation.
     */
    public static final String ORIENT_NEG = "-";
    /**
     * A gml:Surface is a realization of GM_Surface from ISO 19107 (10.5.10).
     */
    public static final String SURFACE = "Surface";
    /**
     * Immutable set containing all curve-based geometry elements (that
     * substitute for gml:AbstractCurve).
     */
    public static final Set<QName> CURVE_SET;
    static {
        Set<QName> aSet = new HashSet<QName>();
        aSet.add(new QName(NS_NAME, "Curve"));
        aSet.add(new QName(NS_NAME, "OrientableCurve"));
        aSet.add(new QName(NS_NAME, "CompositeCurve"));
        aSet.add(new QName(NS_NAME, "LineString"));
        CURVE_SET = Collections.unmodifiableSet(aSet);
    }
    /**
     * The srsName attribute identifies a spatial reference system.
     */
    public static final String SRS = "srsName";

    private GML32() {
    }
}
