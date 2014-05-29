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
import org.opengis.cite.iso19136.general.AppSchemaInfo;
import org.opengis.cite.iso19136.general.GML32;
import org.opengis.cite.iso19136.general.SchemaModelFixture;
import org.opengis.cite.iso19136.util.FeatureTypeFilter;
import org.opengis.cite.iso19136.util.XMLSchemaModelUtils;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Verifies schema components that define geographic features and feature
 * collections. These test methods belong to the "feature" group.
 * 
 * <h6 style="margin-bottom: 0.5em">Sources</h6>
 * <ul>
 * <li>ISO 19136:2007, cl. 9: GML schema - features</li>
 * <li>ISO 19136:2007, cl. 21.3: Schemas defining Features and Feature
 * Collections</li>
 * </ul>
 */
public class FeatureComponentTests extends SchemaModelFixture {

    public FeatureComponentTests() {
        super();
    }

    /**
     * Determines if the application schema has any components (elements or type
     * definitions) that denote geographic features. If not, all tests in this
     * group are skipped.
     * 
     * A geographic feature or feature collection must be declared as a global
     * element that can substitute for {@code gml:AbstractFeature}; its content
     * model must be derived from {@code gml:AbstractFeatureType}.
     * 
     * @param testContext
     *            The test (set) context.
     */
    @BeforeTest
    public void hasFeatureComponents(ITestContext testContext) {
        if (null == this.model) {
            this.model = (XSModel) testContext.getSuite().getAttribute(
                    SuiteAttribute.XSMODEL.getName());
        }
        Set<XSComplexTypeDefinition> complexTypes = XMLSchemaModelUtils
                .getReferencedComplexTypeDefinitions(this.model);
        FeatureTypeFilter typeFilter = new FeatureTypeFilter();
        typeFilter.filterSet(complexTypes);
        Assert.assertFalse(complexTypes.isEmpty(),
                "No GML feature type definitions found in schema.");
        if (null == this.schemaInfo) {
            this.schemaInfo = (AppSchemaInfo) testContext.getSuite()
                    .getAttribute(SuiteAttribute.SCHEMA_INFO.getName());
        }
        this.schemaInfo.setFeatureDefinitions(complexTypes);
    }

    /**
     * [{@code Test}] All feature types (elements) declared in an application
     * schema must substitute for {@code gml:AbstractFeature}.
     * 
     * @see "ISO 19136:2007, cl. A.1.1.15: Substitution group of feature elements"
     */
    @Test
    public void substitutesForAbstractFeature() {
        // find all members of gml:AbstractFeature substitution group
        List<XSElementDeclaration> gmlFeatures = XMLSchemaModelUtils
                .getFeatureDeclarations(this.model);
        Set<XSComplexTypeDefinition> featureDefs = this.schemaInfo
                .getFeatureDefinitions();
        for (XSComplexTypeDefinition typeDef : featureDefs) {
            List<XSElementDeclaration> features = XMLSchemaModelUtils
                    .getGlobalElementsByType(this.model, typeDef);
            for (XSElementDeclaration feature : features) {
                Assert.assertTrue(gmlFeatures.contains(feature), ErrorMessage
                        .format(ErrorMessageKeys.SUBSTITUTION_ERROR, new QName(
                                feature.getNamespace(), feature.getName()),
                                "gml:AbstractFeature"));
            }
        }
    }

    /**
     * [{@code Test}] A feature collection has one or more properties of a type
     * that derives by extension from {@code gml:AbstractFeatureMemberType}.
     * Such a property type must contain (or refer to) a feature.
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136:2007, cl. A.1.1.16: GML Feature Collections</li>
     * <li>ISO 19136:2007, cl. 9.9.2: AbstractFeatureMemberType and derived
     * property types</li>
     * </ul>
     */
    @Test
    public void verifyFeatureMemberProperties() {
        XSTypeDefinition baseType = this.model.getTypeDefinition(
                GML32.FEATURE_MEMBER_TYPE, GML32.NS_NAME);
        XSElementDeclaration gmlAbstractFeature = this.model
                .getElementDeclaration(GML32.ABSTRACT_FEATURE, GML32.NS_NAME);
        Set<XSTypeDefinition> typeDefs = XMLSchemaModelUtils
                .getDerivedTypeDefinitions(this.model, baseType,
                        XSConstants.DERIVATION_EXTENSION);
        for (XSTypeDefinition typeDef : typeDefs) {
            XSComplexTypeDefinition complexType = (XSComplexTypeDefinition) typeDef;
            ETSAssert.assertValidPropertyType(this.model, complexType,
                    gmlAbstractFeature);
        }
    }

    // TODO: validate standard feature properties. See 9.4,
}
