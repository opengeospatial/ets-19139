package org.opengis.cite.iso19136.general;

import java.util.List;

import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.opengis.cite.iso19136.SuiteAttribute;
import org.opengis.cite.iso19136.util.XMLSchemaModelUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;

/**
 * A supporting base class that provides configuration methods to establish a
 * test fixture that provides access to a schema model. These methods are
 * invoked before any configuration methods defined in a subclass.
 */
public class SchemaModelFixture {

    /**
     * An XSModel object representing an XML Schema resource.
     */
    protected XSModel model;
    /**
     * Provides information about the types of geographic objects defined in the
     * application schema.
     */
    protected AppSchemaInfo schemaInfo;
    /**
     * A list of the GML objects declared in the application schema (these can
     * substitute for gml:AbstractGML).
     */
    protected List<XSElementDeclaration> gmlObjects;

    /**
     * Obtains the schema model from the ISuite context. The suite attribute
     * {@link SuiteAttribute#XSMODEL model} should evaluate to an XSModel object
     * representing the contents of the application schema.
     * 
     * @param testContext
     *            The test (set) context.
     */
    @BeforeClass
    public void initSchemaModelFixture(ITestContext testContext) {
        if (null == this.model) {
            this.model = (XSModel) testContext.getSuite().getAttribute(
                    SuiteAttribute.XSMODEL.getName());
        }
        if (null == this.schemaInfo) {
            this.schemaInfo = (AppSchemaInfo) testContext.getSuite()
                    .getAttribute(SuiteAttribute.SCHEMA_INFO.getName());
        }
        XSElementDeclaration gmlObject = this.model.getElementDeclaration(
                GML32.ABSTRACT_GML, GML32.NS_NAME);
        this.gmlObjects = XMLSchemaModelUtils.getElementsByAffiliation(
                this.model, gmlObject);
    }

    /**
     * Sets the schema model (intended only to facilitate unit testing).
     * 
     * @param xsModel
     *            An XSModel object representing an XML Schema.
     */
    public void setSchemaModel(XSModel xsModel) {
        this.model = xsModel;
    }
}
