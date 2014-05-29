package org.opengis.cite.iso19136.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import junit.framework.Assert;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTypeDefinition;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.cite.iso19136.general.GML32;
import org.opengis.cite.validation.XSModelBuilder;
import org.opengis.cite.validation.XmlSchemaCompiler;
import org.xml.sax.SAXException;

/**
 * Verifies the behavior of the XMLSchemaModelUtils class.
 */
public class VerifyXMLSchemaModelUtils {

    private static final String ALPHA_NS = "http://www.example.net/alpha";
    private static final String BETA_NS = "http://www.example.net/beta";
    private static final String GAMMA_NS = "http://www.example.net/gamma";
    private static final String EXAMPLE_NS = "http://example.org/ns1";
    private static XmlSchemaCompiler xsdCompiler;

    public VerifyXMLSchemaModelUtils() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        URL schemaCatalog = VerifyXMLSchemaModelUtils.class
                .getResource("/schema-catalog.xml");
        xsdCompiler = new XmlSchemaCompiler(schemaCatalog);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void buildSchemaModel_gamma() throws SAXException {
        XmlSchemaCompiler compiler = new XmlSchemaCompiler(null);
        InputStream is = this.getClass().getResourceAsStream("/xsd/gamma.xsd");
        Schema xsd = compiler.compileXmlSchema(new StreamSource(is));
        XSModel model = XSModelBuilder.buildXMLSchemaModel(xsd, GAMMA_NS);
        Assert.assertNotNull("XSModel instance is null", model);
        Assert.assertNotNull("Top-level element declaration 'Gamma' not found",
                model.getElementDeclaration("Gamma", GAMMA_NS));
    }

    @Test
    public void buildSchemaModel_alpha() throws IOException, SAXException {
        URL url = this.getClass().getResource("/xsd/alpha.xsd");
        Schema xsd = xsdCompiler.compileXmlSchema(new StreamSource(url
                .openStream(), url.toString()));
        XSModel model = XSModelBuilder.buildXMLSchemaModel(xsd, ALPHA_NS);
        Assert.assertNotNull("XSModel instance is null", model);
        Assert.assertNotNull("Top-level element declaration 'Beta' not found",
                model.getElementDeclaration("Beta", BETA_NS));
    }

    @Test
    public void findFeatures_simpleFeatures() throws IOException, SAXException {
        URL url = this.getClass().getResource("/xsd/simple.xsd");
        Schema xsd = xsdCompiler.compileXmlSchema(new StreamSource(url
                .openStream(), url.toString()));
        XSModel model = XSModelBuilder.buildXMLSchemaModel(xsd, EXAMPLE_NS);
        Assert.assertNotNull("XSModel instance is null", model);
        List<XSElementDeclaration> features = XMLSchemaModelUtils
                .getFeatureDeclarations(model);
        Assert.assertEquals("Unexpected number of feature declarations", 2,
                features.size());
    }

    @Test
    public void findGeometries_simpleFeatures() throws IOException,
            SAXException {
        URL url = this.getClass().getResource("/xsd/simple.xsd");
        Schema xsd = xsdCompiler.compileXmlSchema(new StreamSource(url
                .openStream(), url.toString()));
        XSModel model = XSModelBuilder.buildXMLSchemaModel(xsd, EXAMPLE_NS);
        Assert.assertNotNull("XSModel instance is null", model);
        List<XSElementDeclaration> geometries = XMLSchemaModelUtils
                .getGeometryDeclarations(model);
        Assert.assertEquals("Unexpected number of geometry declarations", 0,
                geometries.size());
    }

    @Test
    public void findGeometries_customGeom() throws IOException, SAXException {
        URL url = this.getClass().getResource("/xsd/customGeom.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        Assert.assertNotNull("XSModel instance is null", model);
        List<XSElementDeclaration> geometries = XMLSchemaModelUtils
                .getGeometryDeclarations(model);
        Assert.assertEquals("Found unexpected number of geometry declarations",
                2, geometries.size());
    }

    @Test
    public void getAppNamespaces_customGeom() throws IOException, SAXException {
        URL url = this.getClass().getResource("/xsd/customGeom.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        Assert.assertNotNull("XSModel instance is null", model);
        Set<String> nsNames = XMLSchemaModelUtils
                .getApplicationNamespaces(model);
        Assert.assertEquals("Unexpected number of namespace names", 1,
                nsNames.size());
        Assert.assertTrue("Namespace name not in set: " + EXAMPLE_NS,
                nsNames.contains(EXAMPLE_NS));
    }

    @Test
    public void findGlobalElementsByComplexType_PointWithBearingPropertyType()
            throws IOException, SAXException {
        URL url = this.getClass().getResource("/xsd/customGeom.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        Assert.assertNotNull("XSModel instance is null", model);
        List<XSElementDeclaration> elems = XMLSchemaModelUtils
                .getGlobalElementsByType(model, model.getTypeDefinition(
                        "PointWithBearingPropertyType", EXAMPLE_NS));
        Assert.assertEquals("Unexpected number of element declarations", 1,
                elems.size());
    }

    @Test
    public void findLocalElementsBySimpleType_AngleType() throws IOException,
            SAXException {
        URL url = this.getClass().getResource("/xsd/customGeom.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        Assert.assertNotNull("XSModel instance is null", model);
        List<XSElementDeclaration> elems = XMLSchemaModelUtils
                .getLocalElementsByType(model,
                        model.getTypeDefinition("AngleType", GML32.NS_NAME),
                        null);
        Assert.assertEquals("Unexpected number of element declarations", 1,
                elems.size());
        Assert.assertNotNull(
                "Expected elem to have enclosing complex type def.", elems
                        .iterator().next().getEnclosingCTDefinition());
    }

    @Test
    public void findLocalElementsWithFilter_MeasureType() throws IOException,
            SAXException {
        URL url = this.getClass().getResource("/xsd/customGeom.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        Assert.assertNotNull("XSModel instance is null", model);
        List<XSElementDeclaration> elems = XMLSchemaModelUtils
                .getLocalElementsByType(model,
                        model.getTypeDefinition("MeasureType", GML32.NS_NAME),
                        new FeatureTypeFilter());
        Assert.assertEquals("Unexpected number of element declarations", 1,
                elems.size());
        Assert.assertEquals("Unexpected name of enclosing type.",
                "RadioTowerType", elems.iterator().next()
                        .getEnclosingCTDefinition().getName());
    }

    @Test
    public void findGeometryProperties_customGeom() throws IOException,
            SAXException {
        URL url = this.getClass().getResource("/xsd/customGeom.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        Assert.assertNotNull("XSModel instance is null", model);
        List<XSElementDeclaration> geomProps = XMLSchemaModelUtils
                .getExplicitGeometryProperties(model);
        Assert.assertEquals("Unexpected number of geometry properties", 3,
                geomProps.size());
    }

    @Test
    public void findExplicitGeometryProperties_simple() throws IOException,
            SAXException {
        URL url = this.getClass().getResource("/xsd/simple.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        List<XSElementDeclaration> geomProps = XMLSchemaModelUtils
                .getExplicitGeometryProperties(model);
        Assert.assertEquals("Unexpected number of geometry properties", 3,
                geomProps.size());
    }

    @Test
    public void getAllElementsInParticle_AbstractGMLType() throws IOException,
            SAXException {
        URL url = this.getClass().getResource("/xsd/customGeom.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        Assert.assertNotNull("XSModel instance is null", model);
        XSComplexTypeDefinition typeDef = (XSComplexTypeDefinition) model
                .getTypeDefinition("AbstractGMLType", GML32.NS_NAME);
        List<XSElementDeclaration> elemDecls = XMLSchemaModelUtils
                .getAllElementsInParticle(typeDef.getParticle());
        Assert.assertEquals("Unexpected number of element declarations", 5,
                elemDecls.size());
    }

    @Test
    public void getAllElementsInParticleWithNestedModelGroup()
            throws IOException, SAXException {
        URL url = this.getClass().getResource("/xsd/customGeom.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        // gml:GridType contains sequence/choice
        XSComplexTypeDefinition gridTypeDef = (XSComplexTypeDefinition) model
                .getTypeDefinition("GridType", GML32.NS_NAME);
        List<XSElementDeclaration> elemDecls = XMLSchemaModelUtils
                .getAllElementsInParticle(gridTypeDef.getParticle());
        Assert.assertEquals("Unexpected number of element declarations", 8,
                elemDecls.size());
        Assert.assertEquals("Unexpected name for component[6]", "axisLabels",
                elemDecls.get(6).getName());
    }

    @Test
    public void getComplexTypes_Beta() throws IOException, SAXException {
        URL url = this.getClass().getResource("/xsd/beta.xsd");
        XSModel model = createXSModel(url, URI.create(BETA_NS));
        Set<XSComplexTypeDefinition> typeDefs = XMLSchemaModelUtils
                .getReferencedComplexTypeDefinitions(model);
        Assert.assertEquals("Unexpected number of type definitions", 2,
                typeDefs.size());
        XSTypeDefinition gammaPropType = model.getTypeDefinition(
                "GammaPropertyType", BETA_NS);
        Assert.assertFalse("Found type def not referenced by global element: "
                + gammaPropType, typeDefs.contains(gammaPropType));
    }

    @Test
    public void pointPropertyTypeHasGeometryValueDomain() throws IOException,
            SAXException {
        URL url = this.getClass().getResource("/xsd/simple.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        XSComplexTypeDefinition pointProp = (XSComplexTypeDefinition) model
                .getTypeDefinition(GML32.POINT_PROP_TYPE, GML32.NS_NAME);
        boolean result = XMLSchemaModelUtils.propertyHasValueDomain(pointProp,
                model.getElementDeclaration(GML32.ABSTRACT_GEOMETRY,
                        GML32.NS_NAME));
        Assert.assertTrue(result);
    }

    @Test
    public void pointPropertyTypeDoesNotHaveTemporalValueDomain()
            throws IOException, SAXException {
        URL url = this.getClass().getResource("/xsd/simple.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        XSComplexTypeDefinition pointProp = (XSComplexTypeDefinition) model
                .getTypeDefinition(GML32.POINT_PROP_TYPE, GML32.NS_NAME);
        boolean result = XMLSchemaModelUtils
                .propertyHasValueDomain(pointProp, model.getElementDeclaration(
                        GML32.ABSTRACT_TIME, GML32.NS_NAME));
        Assert.assertFalse(result);
    }

    @Test
    public void findImplicitCurveProperties() throws IOException, SAXException {
        URL url = this.getClass().getResource("/xsd/simple.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        XSElementDeclaration curve = model.getElementDeclaration(
                GML32.ABSTRACT_CURVE, GML32.NS_NAME);
        List<XSElementDeclaration> curveProps = XMLSchemaModelUtils
                .getImplicitProperties(model, curve);
        Assert.assertEquals("Unexpected number of implicit curve properties.",
                2, curveProps.size());
    }

    @Test
    public void findAllGeometryProperties() throws IOException, SAXException {
        URL url = this.getClass().getResource("/xsd/simple.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        XSElementDeclaration geometry = model.getElementDeclaration(
                GML32.ABSTRACT_GEOMETRY, GML32.NS_NAME);
        List<XSElementDeclaration> geomProps = XMLSchemaModelUtils
                .getImplicitProperties(model, geometry);
        geomProps.addAll(XMLSchemaModelUtils
                .getExplicitGeometryProperties(model));
        Assert.assertEquals("Unexpected number of geometry properties.", 5,
                geomProps.size());
    }

    @Test
    public void findElementParticlesInComplexFeatureType() throws IOException,
            SAXException {
        URL url = this.getClass().getResource("/xsd/simple.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        XSComplexTypeDefinition type = (XSComplexTypeDefinition) model
                .getTypeDefinition("ComplexFeatureType", EXAMPLE_NS);
        List<XSParticle> particles = XMLSchemaModelUtils
                .getAllElementParticles(type.getParticle());
        Assert.assertEquals("Unexpected number of element particles.", 12,
                particles.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void xsNamedMapIsImmutable() throws IOException, SAXException {
        URL url = this.getClass().getResource("/xsd/simple.xsd");
        XSModel model = createXSModel(url, URI.create(EXAMPLE_NS));
        XSNamedMap typeDefs = model.getComponentsByNamespace(
                XSConstants.TYPE_DEFINITION, EXAMPLE_NS);
        QName typeName = new QName(EXAMPLE_NS, "SimpleFeatureType");
        typeDefs.remove(typeName);
    }

    XSModel createXSModel(URL schemaUrl, URI targetNamespace)
            throws IOException, SAXException {
        Schema xsd = xsdCompiler.compileXmlSchema(new StreamSource(schemaUrl
                .openStream(), schemaUrl.toString()));
        XSModel model = XSModelBuilder.buildXMLSchemaModel(xsd,
                targetNamespace.toString());
        return model;
    }
}
