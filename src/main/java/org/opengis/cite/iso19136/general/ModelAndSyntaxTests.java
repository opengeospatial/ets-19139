package org.opengis.cite.iso19136.general;

import java.util.List;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.opengis.cite.iso19136.ErrorMessage;
import org.opengis.cite.iso19136.ErrorMessageKeys;
import org.opengis.cite.iso19136.util.XMLSchemaModelUtils;
import org.opengis.cite.validation.ErrorSeverity;
import org.opengis.cite.validation.ValidationErrorHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Includes tests for adherence to mandatory GML model and syntax constraints. A
 * GML object is an element assigned to the gml:AbstractGML substitution group
 * (and thus is derived from gml:AbstractGMLType). Note that the so-called
 * "object-property model" is very similar to the "striped" encoding syntax
 * convention used in RDF/XML representations, where elements alternately
 * represent nodes (resources) and edges (properties).
 * 
 * <h6 style="margin-bottom: 0.5em">Sources</h6>
 * <ul>
 * <li>ISO 19136:2007, cl. A.1.1.5: Support for the GML model and syntax</li>
 * <li>ISO 19136:2007, 7.1.2: Lexical conventions</li>
 * </ul>
 */
public class ModelAndSyntaxTests extends SchemaModelFixture {

    /**
     * [{@code Test}] A GML property is a child element of a GML object; such a
     * property cannot itself be a GML object (i.e. derived from
     * gml:AbstractGMLType). Furthermore, it must not declare any attributes of
     * type xsd:ID.
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136:2007, 7.1.3: XML schema definition of GML language</li>
     * <li>ISO 19136:2007, A.1.1.7: Property elements are not object elements</li>
     * </ul>
     */
    @Test
    public void verifyGMLObjectPropertyPattern() {
        ValidationErrorHandler errCollector = new ValidationErrorHandler();
        for (XSElementDeclaration gmlObject : gmlObjects) {
            XSComplexTypeDefinition typeDef = (XSComplexTypeDefinition) gmlObject
                    .getTypeDefinition();
            // examine all element declaration particles
            List<XSElementDeclaration> elemDecls = XMLSchemaModelUtils
                    .getAllElementsInParticle(typeDef.getParticle());
            for (XSElementDeclaration elem : elemDecls) {
                if (isGMLObject(elem)) {
                    errCollector.addError(ErrorSeverity.ERROR, ErrorMessage
                            .format(ErrorMessageKeys.PROP_IS_GML_OBJ,
                                    elem.getNamespace(), elem.getName(),
                                    typeDef.getName()), null);
                }
                if (hasIDTypeAttribute(elem)) {
                    errCollector.addError(ErrorSeverity.ERROR, ErrorMessage
                            .format(ErrorMessageKeys.PROP_HAS_ID_ATTR,
                                    elem.getNamespace(), elem.getName(),
                                    typeDef.getName()), null);
                }
            }
        }
        Assert.assertFalse(errCollector.errorsDetected(),
                errCollector.toString());
    }

    /**
     * Determines if an element is a GML object. A GML object is an XML element
     * of a type derived from gml:AbstractGMLType.
     * 
     * @param elemDecl
     *            An XSElementDeclaration object.
     * @return {@code true} if the element corresponds to a GML object;
     *         {@code false} otherwise.
     * 
     * @see "ISO 19136:2007, 7.1.3: XML schema definition of GML language"
     * @see "ISO 19136:2007, 7.2.4.1: Derivation from AbstractGMLType"
     */
    boolean isGMLObject(XSElementDeclaration elemDecl) {
        XSTypeDefinition typeDef = elemDecl.getTypeDefinition();
        if (typeDef.getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE) {
            return false;
        }
        boolean isGMLObject = typeDef.derivedFrom(GML32.NS_NAME,
                GML32.ABSTRACT_GML_TYPE, XSConstants.DERIVATION_EXTENSION)
                || typeDef.derivedFrom(GML32.NS_NAME, GML32.ABSTRACT_GML_TYPE,
                        XSConstants.DERIVATION_RESTRICTION);
        return isGMLObject;
    }

    /**
     * Determines if the type definition for the given element declaration
     * includes an attribute of type xsd:ID. Only GML objects should have one
     * (gml:id).
     * 
     * @param elemDecl
     *            An XSElementDeclaration object.
     * @return {@code true} if the element content model contains an ID type
     *         attribute; {@code false} otherwise.
     * 
     * @see "ISO 19136:2007, 7.1.3: XML schema definition of GML language"
     */
    boolean hasIDTypeAttribute(XSElementDeclaration elemDecl) {
        XSTypeDefinition typeDef = elemDecl.getTypeDefinition();
        if (typeDef.getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE) {
            return false;
        }
        XSComplexTypeDefinition complexType = (XSComplexTypeDefinition) typeDef;
        XSObjectList attrUses = complexType.getAttributeUses();
        boolean foundIDAttr = false;
        for (int i = 0; i < attrUses.getLength(); i++) {
            XSAttributeUse attrUse = (XSAttributeUse) attrUses.item(i);
            XSSimpleTypeDefinition attrType = attrUse.getAttrDeclaration()
                    .getTypeDefinition();
            if (attrType.getBuiltInKind() == XSConstants.ID_DT) {
                foundIDAttr = true;
                break;
            }
        }
        return foundIDAttr;
    }
}
