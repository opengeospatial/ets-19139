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
 * Verifies schema components that define geometry and geometry property types.
 * These test methods belong to the "geometry" group.
 * 
 * A geometry type must be declared as a global element that can substitute for
 * {@code gml:AbstractGeometry}; its content model must be derived from
 * {@code gml:AbstractGeometryType}. A geometry property type may restrict
 * gml:GeometryPropertyType or reflect the content model of
 * gml:AssociationRoleType.
 * 
 * <h6 style="margin-bottom: 0.5em">Sources</h6>
 * <ul>
 * <li>ISO 19136:2007, cl. 10: GML schema - geometric primitives</li>
 * <li>ISO 19136:2007, cl. 11: GML schema - geometric complex, geometric
 * composites and geometric aggregates</li>
 * <li>ISO 19136:2007, cl. 21.4: Schemas defining Spatial Geometries</li>
 * <li>ISO 19136:2007, cl. A.1.5: GML application schemas defining Spatial
 * Geometries</li>
 * </ul>
 */
public class GeometryComponentTests extends SchemaModelFixture {

    private XSTypeDefinition geomBaseType;

    public GeometryComponentTests() {
        super();
    }

    /**
     * Determines if the application schema includes any geometry-related
     * components (elements or type definitions). If not, all tests in this
     * group are skipped.
     * 
     * @param testContext
     *            The test (set) context.
     */
    @BeforeTest
    public void hasGeometryComponents(ITestContext testContext) {
        if (null == this.model) {
            this.model = (XSModel) testContext.getSuite().getAttribute(
                    SuiteAttribute.XSMODEL.getName());
        }
        geomBaseType = this.model.getTypeDefinition(
                GML32.ABSTRACT_GEOMETRY_TYPE, GML32.NS_NAME);
        // types derived by extension from gml:AbstractGeometryType
        List<XSElementDeclaration> geomElements = XMLSchemaModelUtils
                .getGlobalElementsByType(this.model, geomBaseType);
        XSTypeDefinition geomPropBaseType = this.model.getTypeDefinition(
                GML32.GEOM_PROP_TYPE, GML32.NS_NAME);
        // types that restrict gml:GeometryPropertyType
        Set<XSTypeDefinition> geomPropTypes = XMLSchemaModelUtils
                .getDerivedTypeDefinitions(this.model, geomPropBaseType,
                        XSConstants.DERIVATION_RESTRICTION);
        XSElementDeclaration abstractGeom = this.model.getElementDeclaration(
                GML32.ABSTRACT_GEOMETRY, GML32.NS_NAME);
        // implicit geometry properties
        List<XSElementDeclaration> geomProps = XMLSchemaModelUtils
                .getImplicitProperties(this.model, abstractGeom);
        // explicit geometry properties
        geomProps.addAll(XMLSchemaModelUtils
                .getExplicitGeometryProperties(this.model));
        Assert.assertFalse(geomElements.isEmpty() && geomPropTypes.isEmpty()
                && geomProps.isEmpty(),
                ErrorMessage.get(ErrorMessageKeys.NO_USERDEF_GEOM));
    }

    /**
     * [{@code Test}] All geometry types (elements) declared in an application
     * schema must substitute for {@code gml:AbstractGeometry}.
     * 
     * @see "ISO 19136:2007, cl. 21.4.2.1: User-defined Geometry Types"
     */
    @Test
    public void substitutesForGMLGeometry() {
        // find all members of gml:AbstractGeometry substitution group
        List<XSElementDeclaration> geometries = XMLSchemaModelUtils
                .getGeometryDeclarations(this.model);
        Set<XSTypeDefinition> geomTypeDefs = XMLSchemaModelUtils
                .getDerivedTypeDefinitions(this.model, geomBaseType,
                        XSConstants.DERIVATION_EXTENSION);
        for (XSTypeDefinition typeDef : geomTypeDefs) {
            List<XSElementDeclaration> geomDecls = XMLSchemaModelUtils
                    .getGlobalElementsByType(this.model, typeDef);
            for (XSElementDeclaration geomDecl : geomDecls) {
                Assert.assertTrue(geometries.contains(geomDecl), ErrorMessage
                        .format(ErrorMessageKeys.SUBSTITUTION_ERROR, new QName(
                                geomDecl.getNamespace(), geomDecl.getName()),
                                "gml:AbstractGeometry"));
            }
        }
    }

    /**
     * [{@code Test}] The value of a geometry property is an element
     * substitutable for gml:AbstractGeometry. Geometry properties may be
     * defined explicitly (based on a pre-defined GML geometry property type) or
     * implicitly by mimicking the property type content model.
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136:2007, A.1.1.10: Spatial geometry properties</li>
     * <li>ISO 19136:2007, cl. 9.5: Geometry Properties</li>
     * <li>ISO 19136:2007, cl. 21.4.2.2: User-defined Geometry Property Types</li>
     * </ul>
     */
    @Test
    public void validateImplicitGeometryProperty() {
        XSElementDeclaration geometry = this.model.getElementDeclaration(
                GML32.ABSTRACT_GEOMETRY, GML32.NS_NAME);
        List<XSElementDeclaration> geomProps = XMLSchemaModelUtils
                .getImplicitProperties(model, geometry);
        for (XSElementDeclaration prop : geomProps) {
            XSComplexTypeDefinition typeDef = (XSComplexTypeDefinition) prop
                    .getTypeDefinition();
            ETSAssert.assertValidPropertyType(model, typeDef, null);
        }
    }
}
