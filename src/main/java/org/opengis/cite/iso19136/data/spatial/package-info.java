/**
 * Contains tests that apply to GML geometry representations and envelopes. A 
 * geometry object is any element that substitutes for {@code gml:AbstractGeometry};
 * as a consequence its type definition must also be derived from {@code 
 * gml:AbstractGeometryType}.
 * 
 * <p>
 * If a geometry element does not carry the srsName attribute (a reference to a 
 * coordinate reference system), then it shall be inherited from a broader 
 * context as follows:
 * </p>
 * <ol>
 *   <li>the nearest ancestor geometry (aggregate) that has the srsName attribute; or</li>
 *   <li>the gml:boundedBy/gml:Envelope element in the containing feature instance.</li>
 * </ol>
 * 
 * <h6 style="margin-bottom: 0.5em">Sources</h6>
 * <ul>
 *   <li>ISO 19136, cl. 10.1.3: Abstract geometry</li>
 *   <li>ISO 19136, cl. 9.10: Spatial reference system used in a feature or
 * feature collection</li>
 *   <li>ISO 19107, cl. 6: Geometry packages</li>
 * </ul>
 * 
 */
package org.opengis.cite.iso19136.data.spatial;