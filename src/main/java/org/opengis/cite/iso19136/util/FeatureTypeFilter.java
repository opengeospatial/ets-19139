package org.opengis.cite.iso19136.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.namespace.QName;

import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSTypeDefinition;
import org.opengis.cite.iso19136.SchemaComponentFilter;
import org.opengis.cite.iso19136.general.GML32;

/**
 * A schema component filter that accepts feature types--types that derive
 * directly or indirectly from gml:AbstractFeatureType. All other type
 * definitions are rejected. The supplied collection is expected to be of the
 * type {@code Map<QName, XSTypeDefinition>}.
 */
public class FeatureTypeFilter implements SchemaComponentFilter {

    @Override
    public Map<QName, XSObject> doFilter(Map<QName, XSObject> components) {
        // Permit use of mutable Map objects
        Map<QName, XSObject> objectTypes = new ConcurrentHashMap<QName, XSObject>(
                components);
        for (Map.Entry<QName, XSObject> typeDef : objectTypes.entrySet()) {
            XSTypeDefinition type = (XSTypeDefinition) typeDef.getValue();
            if (!type.derivedFrom(GML32.NS_NAME, GML32.ABSTRACT_FEATURE_TYPE,
                    XSConstants.DERIVATION_EXTENSION)) {
                objectTypes.remove(typeDef.getKey());
            }
        }
        return objectTypes;
    }

    /**
     * Removes complex type definitions that are not derived (by extension) from
     * {@code gml:AbstractFeatureType}.
     * 
     * @param complexTypes
     *            A Set of complex type definitions (XSComplexTypeDefinition
     *            objects).
     */
    public void filterSet(Set<XSComplexTypeDefinition> complexTypes) {
        Iterator<XSComplexTypeDefinition> itr = complexTypes.iterator();
        while (itr.hasNext()) {
            XSComplexTypeDefinition typeDef = itr.next();
            if (!typeDef.derivedFrom(GML32.NS_NAME,
                    GML32.ABSTRACT_FEATURE_TYPE,
                    XSConstants.DERIVATION_EXTENSION)) {
                itr.remove();
            }
        }
        return;
    }
}
