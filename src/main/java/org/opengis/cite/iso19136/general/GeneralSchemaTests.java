package org.opengis.cite.iso19136.general;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.validation.Schema;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.opengis.cite.iso19136.ErrorMessage;
import org.opengis.cite.iso19136.ErrorMessageKeys;
import org.opengis.cite.iso19136.SuiteAttribute;
import org.opengis.cite.iso19136.util.TestSuiteLogger;
import org.opengis.cite.iso19136.util.XMLSchemaModelUtils;
import org.opengis.cite.validation.XSModelBuilder;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Verifies that a GML application schema is constructed in accord with all
 * mandatory constraints stipulated in clause A.1.1 of ISO 19136. All test
 * methods in the class belong to the "general" group.
 */
public class GeneralSchemaTests {

    private Schema appSchema;
    private XSModel model;
    private URI targetNamespace;
    private AppSchemaInfo schemaInfo;

    /**
     * Creates a schema model using the Schema object obtained from the ISuite
     * context. The suite attribute {@link SuiteAttribute#SCHEMA schema} should
     * evaluate to a Schema object.
     * 
     * If the model is successfully created it is added to the ISuite context as
     * the {@link SuiteAttribute#XSMODEL model} attribute.
     * 
     * @param testContext
     *            The test (group) context.
     */
    @BeforeClass
    @SuppressWarnings("unchecked")
    public void createSchemaModel(ITestContext testContext) {
        Object schema = testContext.getSuite().getAttribute(
                SuiteAttribute.SCHEMA.getName());
        if ((null != schema)
                && Schema.class.isAssignableFrom(schema.getClass())) {
            this.appSchema = Schema.class.cast(schema);
        } else {
            throw new MissingResourceException(
                    "Unable to obtain Schema object from ITestContext",
                    SuiteAttribute.SCHEMA.getType().getName(),
                    SuiteAttribute.SCHEMA.getName());
        }
        Set<URI> schemaURIs = (Set<URI>) testContext.getSuite().getAttribute(
                SuiteAttribute.SCHEMA_LOC_SET.getName());
        this.targetNamespace = getApplicationNamespaceName(schemaURIs);
        this.model = XSModelBuilder.buildXMLSchemaModel(appSchema,
                targetNamespace.toString());
        if (null != model) {
            testContext.getSuite().setAttribute(
                    SuiteAttribute.XSMODEL.getName(), this.model);
        }
        this.schemaInfo = new AppSchemaInfo();
        testContext.getSuite().setAttribute(
                SuiteAttribute.SCHEMA_INFO.getName(), this.schemaInfo);
    }

    /**
     * [{@code @Test}] An application schema shall declare a target namespace
     * and it must not be "http://www.opengis.net/gml/3.2".
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136:2007, cl. 21.2.2</li>
     * <li>ISO 19136:2007, cl. A.1.1.1 (Use of XML Namespaces)</li>
     * </ul>
     */
    @Test
    public void declareTargetNamespace() {
        Assert.assertTrue(targetNamespace.isAbsolute(), ErrorMessage.format(
                ErrorMessageKeys.RELATIVE_NS, targetNamespace.toString()));
        Assert.assertNotEquals(targetNamespace, URI.create(GML32.NS_NAME),
                ErrorMessage.get(ErrorMessageKeys.UNEXPECTED_NS));
    }

    /**
     * [{@code @Test}] A GML application schema shall import the full GML
     * schema. The schemaLocation to the imported GML schema document must be
     * provided.
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136:2007, cl. 21.2.3</li>
     * <li>ISO 19136:2007, cl. A.1.1.3 (Import of GML schema components)</li>
     * </ul>
     */
    @Test
    public void importFullGMLSchema() {
        XSNamedMap gmlElemDecls = model.getComponentsByNamespace(
                XSConstants.ELEMENT_DECLARATION, GML32.NS_NAME);
        Assert.assertEquals(gmlElemDecls.getLength(), GML32.TOTAL_GLOBAL_ELEMS,
                ErrorMessage.get(ErrorMessageKeys.IMPORT_FULL_GML));
    }

    /**
     * [{@code @Test}] A GML application schema shall be of at least one of the
     * schema types described in clauses 21.3 through 21.11. That is, a
     * compliant application schema must include definitions created in accord
     * with at least one of the following conformance classes:
     * <ul>
     * <li>GML application schemas defining Features and Feature Collections</li>
     * <li>GML application schemas defining Spatial Geometries</li>
     * <li>GML application schemas defining Spatial Topologies</li>
     * <li>GML application schemas defining Time</li>
     * <li>GML application schemas defining Coordinate Reference Systems</li>
     * <li>GML application schemas defining Coverages</li>
     * <li>GML application schemas defining Observations</li>
     * <li>GML application schemas defining Dictionaries and Definitions</li>
     * <li>GML application schemas defining Values</li>
     * </ul>
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136:2007, cl. A.1.1.2 (General rules)</li>
     * <li>ISO 19136:2007, cl. 21.2.1</li>
     * </ul>
     * 
     */
    @Test
    public void declaresGMLObjects() {
        schemaInfo.setFeatureTypes(XMLSchemaModelUtils
                .getFeatureDeclarations(model));
        schemaInfo.setGeometryTypes(XMLSchemaModelUtils
                .getGeometryDeclarations(model));
        // geometryProperyTypes?
        schemaInfo.setTopoTypes(XMLSchemaModelUtils
                .getTopologyDeclarations(model));
        schemaInfo.setTimeTypes(XMLSchemaModelUtils
                .getTimeObjectDeclarations(model));
        schemaInfo.setCrsTypes(XMLSchemaModelUtils.getCRSDeclarations(model));
        schemaInfo.setCoverageTypes(XMLSchemaModelUtils
                .getCoverageDeclarations(model));
        schemaInfo.setObservationTypes(XMLSchemaModelUtils
                .getObservationDeclarations(model));
        schemaInfo.setDefinitionTypes(XMLSchemaModelUtils
                .getDefinitionDeclarations(model));
        TestSuiteLogger.log(Level.FINE,
                "GML objects declared in app schema\n {0}",
                new Object[] { schemaInfo.toString() });
        Assert.assertTrue(schemaInfo.conforms(),
                ErrorMessage.get(ErrorMessageKeys.NO_GML_DEFS));
    }

    AppSchemaInfo getSchemaInfo() {
        return schemaInfo;
    }

    /**
     * Determines the target namespace of a GML application schema, which is
     * assumed to be the first schema found to have a target namespace name that
     * is <strong>not</strong> in the opengis.net domain. If a URI cannot be
     * dereferenced it is skipped.
     * 
     * @param schemaLocations
     *            A Set<URI> of schema references.
     * @return A URI indicating the target namespace of a GML application
     *         schema.
     */
    URI getApplicationNamespaceName(Set<URI> schemaLocations) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        String tns = null;
        for (URI uri : schemaLocations) {
            InputStream inStream = null;
            try {
                inStream = uri.toURL().openStream();
                XMLEventReader reader = factory.createXMLEventReader(inStream,
                        "UTF-8");
                StartElement docElem = reader.nextTag().asStartElement();
                Attribute targetNS = docElem.getAttributeByName(new QName(
                        "targetNamespace"));
                if (null != targetNS
                        && !targetNS.getValue().contains("opengis.net")) {
                    tns = targetNS.getValue();
                    break;
                }
            } catch (Exception e) {
                TestSuiteLogger.log(Level.WARNING,
                        "Failed to read schema from " + uri, e);
                continue;
            } finally {
                if (null != inStream) {
                    try {
                        inStream.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
        return URI.create(tns);
    }
}
