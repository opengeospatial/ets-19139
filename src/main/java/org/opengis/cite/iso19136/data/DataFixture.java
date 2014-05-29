package org.opengis.cite.iso19136.data;

import java.io.File;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.xerces.xs.XSModel;
import org.opengis.cite.iso19136.SuiteAttribute;
import org.opengis.cite.iso19136.general.GML32;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;

/**
 * A supporting base class that provides a common fixture for validating data
 * sets. The configuration methods are invoked before any that may be defined in
 * a subclass.
 */
public class DataFixture {

    /**
     * A File containing GML data.
     */
    protected File dataFile;

    /**
     * An XSModel object representing a GML application schema.
     */
    protected XSModel model;

    public DataFixture() {
    }

    /**
     * A configuration method ({@code BeforeClass}) that initializes the test
     * fixture as follows:
     * <ol>
     * <li>Obtain the GML data set from the test context. The suite attribute
     * {@link org.opengis.cite.iso19136.SuiteAttribute#GML} should evaluate to a
     * {@code File} object containing the GML data. If no such file reference
     * exists the tests are skipped.</li>
     * <li>Obtain the schema model from the test context. The suite attribute
     * {@link org.opengis.cite.iso19136.SuiteAttribute#XSMODEL model} should
     * evaluate to an {@code XSModel} object representing the GML application
     * schema.</li>
     * </ol>
     * 
     * @param testContext
     *            The test (group) context.
     */
    @BeforeClass(alwaysRun = true)
    public void initDataFixture(ITestContext testContext) {
        Assert.assertTrue(
                testContext.getSuite().getAttributeNames()
                        .contains(SuiteAttribute.GML.getName()),
                "No GML data to validate.");
        this.dataFile = (File) testContext.getSuite().getAttribute(
                SuiteAttribute.GML.getName());
        this.model = (XSModel) testContext.getSuite().getAttribute(
                SuiteAttribute.XSMODEL.getName());
    }

    /**
     * Sets the data file. This is a convenience method intended to facilitate
     * unit testing.
     * 
     * @param dataFile
     *            A File containing the data to be validated.
     */
    public void setDataFile(File dataFile) {
        this.dataFile = dataFile;
    }

    /**
     * Sets the schema model (for unit testing purposes).
     * 
     * @param xsModel
     *            An XSModel object representing a GML application schema.
     */
    public void setSchemaModel(XSModel xsModel) {
        this.model = xsModel;
    }

    /**
     * Generates an XPath expression to find all instances of the given elements
     * in the data being validated. The supplied namespace bindings will be
     * supplemented if necessary.
     * 
     * @param elemNames
     *            A list of qualified names corresponding to element
     *            declarations.
     * @param namespaceBindings
     *            A collection of namespace bindings required to evaluate the
     *            XPath expression, where each entry maps a namespace URI (key)
     *            to a prefix (value).
     * @return An XPath (1.0) expression.
     */
    public String generateXPathExpression(List<QName> elemNames,
            Map<String, String> namespaceBindings) {
        StringBuilder xpath = new StringBuilder();
        ListIterator<QName> itr = elemNames.listIterator();
        while (itr.hasNext()) {
            QName qName = itr.next();
            String namespace = qName.getNamespaceURI();
            String prefix = namespaceBindings.get(namespace);
            if (null == prefix) {
                prefix = (namespace.equals(GML32.NS_NAME)) ? "gml" : "ns"
                        + itr.previousIndex();
                namespaceBindings.put(namespace, prefix);
            }
            xpath.append("//").append(prefix).append(":");
            xpath.append(qName.getLocalPart());
            if (itr.hasNext())
                xpath.append(" | "); // union operator
        }
        return xpath.toString();
    }
}
