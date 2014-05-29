package org.opengis.cite.iso19136.recommendations;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.Set;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.opengis.cite.iso19136.ErrorMessage;
import org.opengis.cite.iso19136.ErrorMessageKeys;
import org.opengis.cite.iso19136.general.SchemaModelFixture;
import org.opengis.cite.iso19136.util.XMLSchemaModelUtils;
import org.opengis.cite.validation.ErrorSeverity;
import org.opengis.cite.validation.ValidationErrorHandler;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Includes tests for compliance with various lexical conventions that are
 * "strongly recommended" in the specification. These recommendations primarily
 * deal with the names of schema components.
 * 
 * <h6 style="margin-bottom: 0.5em">Sources</h6>
 * <ul>
 * <li>ISO 19136:2007, cl. A.1.1.5: Support for the GML model and syntax</li>
 * <li>ISO 19136:2007, 7.1.2: Lexical conventions</li>
 * </ul>
 * 
 */
public class LexicalConventionTests extends SchemaModelFixture {

    /**
     * [{@code Test}] The [local name] of a GML object should be in
     * UpperCamelCase form.
     * 
     * @see "ISO 19136:2007, 7.1.2: Lexical conventions"
     */
    @Test
    public void verifyObjectNameIsUpperCamelCase() {
        ValidationErrorHandler errCollector = new ValidationErrorHandler();
        for (XSElementDeclaration gmlObject : gmlObjects) {
            String localName = gmlObject.getName();
            if (!isUpperCamelCase(localName)) {
                errCollector.addError(ErrorSeverity.WARNING, ErrorMessage
                        .format(ErrorMessageKeys.NOT_UCC_NAME,
                                gmlObject.getNamespace(), localName), null);
            }
        }
        Assert.assertFalse(errCollector.errorsDetected(),
                errCollector.toString());
    }

    /**
     * [{@code Test}] The [local name] of a GML object property should be in
     * lowerCamelCase form. Furthermore, the name of an abstract element should
     * start with "abstract".
     * 
     * @see "ISO 19136:2007, 7.1.2: Lexical conventions"
     */
    @Test
    public void verifyObjectPropertyNameIsLowerCamelCase() {
        ValidationErrorHandler errCollector = new ValidationErrorHandler();
        for (XSElementDeclaration gmlObject : gmlObjects) {
            // GML object must have a complex type definition
            XSComplexTypeDefinition typeDef = (XSComplexTypeDefinition) gmlObject
                    .getTypeDefinition();
            // examine all element declaration particles
            List<XSElementDeclaration> elemDecls = XMLSchemaModelUtils
                    .getAllElementsInParticle(typeDef.getParticle());
            for (XSElementDeclaration elem : elemDecls) {
                String localName = elem.getName();
                if (!isLowerCamelCase(localName)) {
                    errCollector.addError(ErrorSeverity.WARNING, ErrorMessage
                            .format(ErrorMessageKeys.NOT_LCC_NAME,
                                    elem.getNamespace(), localName), null);
                }
                if (elem.getAbstract() && !localName.startsWith("abstract")) {
                    errCollector
                            .addError(ErrorSeverity.WARNING, ErrorMessage
                                    .format(ErrorMessageKeys.NOT_ABSTRACT_NAME,
                                            "abstract", elem.getNamespace(),
                                            localName), null);
                }
            }
        }
        Assert.assertFalse(errCollector.errorsDetected(),
                errCollector.toString());
    }

    /**
     * [{@code Test}] The [local name] of an abstract GML object should start
     * with "Abstract".
     * 
     * @see "ISO 19136:2007, 7.1.2: Lexical conventions"
     */
    @Test
    public void verifyAbstractObjectName() {
        ValidationErrorHandler errCollector = new ValidationErrorHandler();
        for (XSElementDeclaration gmlObject : gmlObjects) {
            String localName = gmlObject.getName();
            if (gmlObject.getAbstract() && !localName.startsWith("Abstract")) {
                errCollector.addError(ErrorSeverity.WARNING, ErrorMessage
                        .format(ErrorMessageKeys.NOT_ABSTRACT_NAME, "Abstract",
                                gmlObject.getNamespace(), localName), null);
            }
        }
        Assert.assertFalse(errCollector.errorsDetected(),
                errCollector.toString());
    }

    /**
     * [{@code Test}] The [local name] of a complex type definition should 1) be
     * in UpperCamelCase form, and 2) end with "Type". Furthermore, the name of
     * an abstract complex type definition should start with "Abstract".
     * 
     * @see "ISO 19136:2007, 7.1.2: Lexical conventions"
     */
    @Test
    public void verifyComplexTypeName() {
        ValidationErrorHandler errCollector = new ValidationErrorHandler();
        Set<XSComplexTypeDefinition> typeDefs = XMLSchemaModelUtils
                .getGlobalComplexTypeDefinitions(model);
        for (XSComplexTypeDefinition type : typeDefs) {
            String localName = type.getName();
            if (!isUpperCamelCase(localName)) {
                errCollector.addError(
                        ErrorSeverity.WARNING,
                        ErrorMessage.format(ErrorMessageKeys.NOT_UCC_NAME,
                                type.getNamespace(), localName), null);
            }
            if (!localName.endsWith("Type")) {
                errCollector.addError(ErrorSeverity.WARNING, ErrorMessage
                        .format(ErrorMessageKeys.MISSING_TYPE_SUFFIX,
                                type.getNamespace(), localName), null);
            }
            if (type.getAbstract() && !localName.startsWith("Abstract")) {
                errCollector.addError(ErrorSeverity.WARNING, ErrorMessage
                        .format(ErrorMessageKeys.NOT_ABSTRACT_NAME, "Abstract",
                                type.getNamespace(), localName), null);
            }
        }
        Assert.assertFalse(errCollector.errorsDetected(),
                errCollector.toString());
    }

    /**
     * Checks if a String value is in UpperCamelCase form. The following rules
     * apply:
     * 
     * <ol>
     * <li>The first character is upper case.</li>
     * <li>At least one of the remaining characters is not upper case.</li>
     * </ol>
     * 
     * @param localName
     *            The [local name] of the schema component to check.
     * @return 'true' if the given string is in UpperCamelCase form; 'false'
     *         otherwise.
     */
    boolean isUpperCamelCase(String localName) {
        boolean isUpperCamelCase = true;
        StringCharacterIterator itr = new StringCharacterIterator(localName);
        if (!Character.isUpperCase(itr.first())) {
            return false;
        }
        boolean foundNonUpperCaseChar = false;
        for (char ch = itr.next(); ch != CharacterIterator.DONE; ch = itr
                .next()) {
            if (!Character.isUpperCase(ch)) {
                foundNonUpperCaseChar = true;
                break;
            }
        }
        if (!foundNonUpperCaseChar && localName.length() > 1) {
            isUpperCamelCase = false;
        }
        return isUpperCamelCase;
    }

    /**
     * Checks if a String value is in lowerCamelCase form. That is, the first
     * character is lower case. Since the name may consist of only a single
     * word, all lower case characters are acceptable.
     * 
     * @param localName
     *            The [local name] of the schema component to check.
     * @return 'true' if the given string is in lowerCamelCase form; 'false'
     *         otherwise.
     */
    boolean isLowerCamelCase(String localName) {
        return Character.isLowerCase(localName.charAt(0));
    }

}
