package org.opengis.cite.iso19136.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.namespace.QName;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSTypeDefinition;
import org.opengis.cite.iso19136.SchemaComponentFilter;
import org.opengis.cite.iso19136.general.GML32;

/**
 * A schema component filter that accepts GML object types--types that derive
 * directly or indirectly from gml:AbstractGMLType. All other type definitions
 * are rejected. The supplied collection is expected to be of the type
 * {@code Map<QName, XSTypeDefinition>}.
 */
public class GMLObjectTypeFilter implements SchemaComponentFilter {

    @Override
    public Map<QName, XSObject> doFilter(Map<QName, XSObject> components) {
        // Permit use of mutable Map objects
        Map<QName, XSObject> objectTypes = new ConcurrentHashMap<QName, XSObject>(
                components);
        for (Map.Entry<QName, XSObject> typeDef : objectTypes.entrySet()) {
            XSTypeDefinition type = (XSTypeDefinition) typeDef.getValue();
            if (!type.derivedFrom(GML32.NS_NAME, GML32.ABSTRACT_GML_TYPE,
                    XSConstants.DERIVATION_EXTENSION)) {
                objectTypes.remove(typeDef.getKey());
            }
        }
        return objectTypes;
    }
}
