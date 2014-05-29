package org.opengis.cite.iso19136.components;

import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSTypeDefinition;
import org.opengis.cite.iso19136.ETSAssert;
import org.opengis.cite.iso19136.ErrorMessage;
import org.opengis.cite.iso19136.ErrorMessageKeys;
import org.opengis.cite.iso19136.SuiteAttribute;
import org.opengis.cite.iso19136.general.GML32;
import org.opengis.cite.iso19136.general.SchemaModelFixture;
import org.opengis.cite.iso19136.util.XMLSchemaModelUtils;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Verifies schema components that define temporal elements and temporal
 * property types. These test methods belong to the "time" group.
 * 
 * A temporal type must be declared as a global element that can substitute for
 * {@code gml:AbstractTimeObject}, which acts as the head of a substitution
 * group for all temporal primitives and complexes; the content model must be
 * derived from {@code gml:AbstractTimeObjectType}.
 * 
 * A temporal feature property may make use of an existing GML temporal property
 * type (see ISO 19136, Table 6) or reflect the content model of
 * gml:AssociationRoleType.
 * 
 * <h6 style="margin-bottom: 0.5em">Sources</h6>
 * <ul>
 * <li>ISO 19136:2007, cl. 14: GML schema – temporal information and dynamic
 * features</li>
 * <li>ISO 19136:2007, cl. 21.6 Schemas defining Time</li>
 * 
 * </ul>
 */
public class TemporalComponentTests extends SchemaModelFixture {

    private XSTypeDefinition timeBaseType;

    public TemporalComponentTests() {
        super();
    }

    /**
     * Determines if the application schema contains any temporal components
     * (elements or type definitions). If not, all tests in this group are
     * skipped.
     * 
     * @param testContext
     *            The test (set) context.
     */
    @BeforeTest
    public void hasTemporalComponents(ITestContext testContext) {
        if (null == this.model) {
            this.model = (XSModel) testContext.getSuite().getAttribute(
                    SuiteAttribute.XSMODEL.getName());
        }
        timeBaseType = this.model.getTypeDefinition(GML32.ABSTRACT_TIME_TYPE,
                GML32.NS_NAME);
        // types derived by extension from gml:AbstractTimeObjectType
        List<XSElementDeclaration> timeElements = XMLSchemaModelUtils
                .getGlobalElementsByType(this.model, timeBaseType);
        XSElementDeclaration abstractTime = this.model.getElementDeclaration(
                GML32.ABSTRACT_TIME, GML32.NS_NAME);
        // implicit temporal property types
        List<XSElementDeclaration> timeProps = XMLSchemaModelUtils
                .getImplicitProperties(this.model, abstractTime);
        Assert.assertFalse(timeElements.isEmpty() && timeProps.isEmpty(),
                "No temporal (property) components found in schema.");
    }

    /**
     * [{@code Test}] All temporal types (elements) declared in an application
     * schema must substitute for {@code gml:AbstractTimeObject}.
     * 
     * @see "ISO 19136:2007, cl. 21.6.2.1: User-defined Temporal Types"
     */
    @Test
    public void substitutesForAbstractTimeObject() {
        List<XSElementDeclaration> timeElems = XMLSchemaModelUtils
                .getTimeObjectDeclarations(this.model);
        Set<XSTypeDefinition> timeTypeDefs = XMLSchemaModelUtils
                .getDerivedTypeDefinitions(this.model, timeBaseType,
                        XSConstants.DERIVATION_EXTENSION);
        for (XSTypeDefinition typeDef : timeTypeDefs) {
            List<XSElementDeclaration> timeDecls = XMLSchemaModelUtils
                    .getGlobalElementsByType(this.model, typeDef);
            for (XSElementDeclaration timeDecl : timeDecls) {
                Assert.assertTrue(timeElems.contains(timeDecl), ErrorMessage
                        .format(ErrorMessageKeys.SUBSTITUTION_ERROR, new QName(
                                timeDecl.getNamespace(), timeDecl.getName()),
                                "gml:AbstractTimeObject"));
            }
        }
    }

    /**
     * [{@code Test}] The value of a temporal property is an element
     * substitutable for gml:AbstractTimeObject. Temporal properties may be
     * defined explicitly (based on a pre-defined GML temporal property type) or
     * implicitly by mimicking the standard GML property type content model.
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136:2007, cl. 9.7: Temporal Properties</li>
     * <li>ISO 19136:2007, cl. A.1.1.12: Temporal properties</li>
     * <li>ISO 19136:2007, cl. 21.6.2.2: User-defined Temporal Property Types</li>
     * </ul>
     */
    @Test
    public void validateImplicitTemporalProperty() {
        XSElementDeclaration temporal = this.model.getElementDeclaration(
                GML32.ABSTRACT_TIME, GML32.NS_NAME);
        List<XSElementDeclaration> timeProps = XMLSchemaModelUtils
                .getImplicitProperties(model, temporal);
        for (XSElementDeclaration prop : timeProps) {
            XSComplexTypeDefinition typeDef = (XSComplexTypeDefinition) prop
                    .getTypeDefinition();
            ETSAssert.assertValidPropertyType(this.model, typeDef, null);
        }
    }
}
