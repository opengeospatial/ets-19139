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
 * Verifies schema components that define topology and topology property types.
 * These test methods belong to the "topology" group.
 * 
 * A topology type must be declared as a global element that can substitute for
 * {@code gml:AbstractTopology}; its content model must be derived from
 * {@code gml:AbstractTopologyType}. A topology property type may restrict
 * gml:TopoPrimitiveMemberType or reflect the content model of
 * gml:AssociationRoleType.
 * 
 * <h6 style="margin-bottom: 0.5em">Sources</h6>
 * <ul>
 * <li>ISO 19136:2007, cl. 13: GML schema - topology</li>
 * <li>ISO 19136:2007, cl. 21.5 Schemas defining Spatial Topologies</li>
 * <li>ISO 19136:2007, cl. A.1.6: GML application schemas defining Spatial
 * Topologies</li>
 * </ul>
 */
public class TopologyComponentTests extends SchemaModelFixture {

    private XSTypeDefinition topoBaseType;

    public TopologyComponentTests() {
        super();
    }

    /**
     * Determines if the application schema includes any topology-related
     * components (elements or type definitions). If not, all tests in this
     * group are skipped.
     * 
     * @param testContext
     *            The test (set) context.
     */
    @BeforeTest
    public void hasTopologyComponents(ITestContext testContext) {
        if (null == this.model) {
            this.model = (XSModel) testContext.getSuite().getAttribute(
                    SuiteAttribute.XSMODEL.getName());
        }
        topoBaseType = this.model.getTypeDefinition(GML32.ABSTRACT_TOPO_TYPE,
                GML32.NS_NAME);
        // types derived by extension from gml:AbstractTopologyType
        List<XSElementDeclaration> topoElements = XMLSchemaModelUtils
                .getGlobalElementsByType(this.model, topoBaseType);
        XSElementDeclaration abstractTopo = this.model.getElementDeclaration(
                GML32.ABSTRACT_TOPO, GML32.NS_NAME);
        // implicit topology property types
        List<XSElementDeclaration> topoProps = XMLSchemaModelUtils
                .getImplicitProperties(this.model, abstractTopo);
        Assert.assertFalse(topoElements.isEmpty() && topoProps.isEmpty(),
                "No GML topology (property) components found in schema.");
    }

    /**
     * [{@code Test}] All topology types (elements) declared in an application
     * schema must substitute for {@code gml:AbstractTopology}.
     * 
     * @see "ISO 19136:2007, cl. 21.5.2.1: User-defined Topology Types"
     */
    @Test
    public void substitutesForGMLTopology() {
        List<XSElementDeclaration> topoElems = XMLSchemaModelUtils
                .getTopologyDeclarations(this.model);
        Set<XSTypeDefinition> topoTypeDefs = XMLSchemaModelUtils
                .getDerivedTypeDefinitions(this.model, topoBaseType,
                        XSConstants.DERIVATION_EXTENSION);
        for (XSTypeDefinition typeDef : topoTypeDefs) {
            List<XSElementDeclaration> topoDecls = XMLSchemaModelUtils
                    .getGlobalElementsByType(this.model, typeDef);
            for (XSElementDeclaration topoDecl : topoDecls) {
                Assert.assertTrue(topoElems.contains(topoDecl), ErrorMessage
                        .format(ErrorMessageKeys.SUBSTITUTION_ERROR, new QName(
                                topoDecl.getNamespace(), topoDecl.getName()),
                                "gml:AbstractTopology"));
            }
        }
    }

    /**
     * [{@code Test}] The value of a topology property is an element
     * substitutable for gml:AbstractTopology. Topology properties may be
     * defined explicitly (based on a pre-defined GML topology property type) or
     * implicitly by mimicking the GML property type content model.
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136:2007, cl. 9.6: Topology Properties</li>
     * <li>ISO 19136:2007, A.1.1.11: Spatial topology properties</li>
     * <li>ISO 19136:2007, cl. 21.5.2.2: User-defined Topology Property Types</li>
     * </ul>
     */
    @Test
    public void validateImplicitTopologyProperty() {
        XSElementDeclaration topology = this.model.getElementDeclaration(
                GML32.ABSTRACT_TOPO, GML32.NS_NAME);
        List<XSElementDeclaration> topoProps = XMLSchemaModelUtils
                .getImplicitProperties(model, topology);
        for (XSElementDeclaration prop : topoProps) {
            XSComplexTypeDefinition typeDef = (XSComplexTypeDefinition) prop
                    .getTypeDefinition();
            ETSAssert.assertValidPropertyType(model, typeDef, null);
        }
    }
}
