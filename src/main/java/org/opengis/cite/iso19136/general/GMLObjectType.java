package org.opengis.cite.iso19136.general;

/**
 * Denotes the (abstract) type of GML object that may be defined in an
 * application schema. Clauses 21.3 through 21.11 describe the content-specific
 * conformance classes listed below.
 * 
 * <ul>
 * <li>21.3: Features and Feature Collections</li>
 * <li>21.4: Spatial Geometries</li>
 * <li>21.5: Spatial Topologies</li>
 * <li>21.6: Time</li>
 * <li>21.7: Coordinate Reference Systems</li>
 * <li>21.8: Coverages</li>
 * <li>21.9: Observations</li>
 * <li>21.10: Dictionaries and Definitions</li>
 * <li>21.11: Values</li>
 * </ul>
 * 
 * @see "ISO 19136:2007, cl. 21.2.1"
 */
public enum GMLObjectType {

    FEATURE_TYPE, GEOMETRY, TOPOLOGY, TIME, CRS, COVERAGE, OBSERVATION, DEFINITION, VALUE;
}
