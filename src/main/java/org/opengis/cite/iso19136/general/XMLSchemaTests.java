package org.opengis.cite.iso19136.general;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.validation.Schema;

import org.opengis.cite.iso19136.ErrorMessage;
import org.opengis.cite.iso19136.ErrorMessageKeys;
import org.opengis.cite.iso19136.SuiteAttribute;
import org.opengis.cite.validation.ValidationErrorHandler;
import org.opengis.cite.validation.XmlSchemaCompiler;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

/**
 * Verifies that an XML Schema resource conforms to the W3C XML Schema
 * Recommendation. This is a fundamental requirement; if any XML Schema
 * constraints are violated all remaining tests should be skipped.
 * 
 * @see <a href="http://www.w3.org/TR/xmlschema-1/">XML Schema Part 1</a>
 * 
 */
public class XMLSchemaTests {

    /** Timeout for compiling schemas /ms */
    static final long COMPILE_TIMEOUT = 60000;
    private final Logger logr = Logger.getLogger(this.getClass().getPackage()
            .getName());
    private static final String ETS_ROOT_PKG = "/org/opengis/cite/iso19136/";
    private Set<URI> xsdLocations;

    /**
     * Obtains the schema locations from the ISuite context. The suite attribute
     * {@link SuiteAttribute#SCHEMA_LOC_SET} should evaluate to a Set of URI
     * objects specifying the locations of the relevant XML Schema grammars.
     * 
     * @param testContext
     *            The test (group) context.
     */
    @BeforeClass
    @SuppressWarnings("unchecked")
    public void getSchemaURIsFromTestContext(ITestContext testContext) {
        Object uriSet = testContext.getSuite().getAttribute(
                SuiteAttribute.SCHEMA_LOC_SET.getName());
        if ((null != uriSet) && Set.class.isAssignableFrom(uriSet.getClass())) {
            this.xsdLocations = (Set<URI>) uriSet;
        } else {
            throw new MissingResourceException(
                    "Unable to obtain XML Schema locations from ITestContext",
                    SuiteAttribute.SCHEMA_LOC_SET.getType().getName(),
                    SuiteAttribute.SCHEMA_LOC_SET.getName());
        }
    }

    /**
     * [{@code @Test}] Verifies the validity of the GML application schema XML
     * document with respect to the W3C XML Schema specification. It must
     * satisfy all relevant constraints imposed by the XML Schema specification.
     * 
     * <p>
     * If the schema is successfully compiled with no errors it is added to the
     * ISuite context as the value of the {@link SuiteAttribute#SCHEMA schema}
     * attribute. The resulting (immutable, thread-safe) Schema object may then
     * be used to construct a Validator.
     * </p>
     * 
     * <p>
     * A fail verdict is produced if it takes longer than
     * {@value #COMPILE_TIMEOUT} ms to compile the schema(s).
     * </p>
     * 
     * @see "ISO 19136:2007, cl. A.1.1.4 (Valid XML Schema)"
     * 
     * @param testContext
     *            The test (group) context.
     * @throws SAXException
     *             If a schema cannot be read.
     * @throws IOException
     *             If a schema resource cannot be accessed for any reason.
     */
    @Test(timeOut = COMPILE_TIMEOUT)
    public void compileXMLSchema(ITestContext testContext) throws SAXException,
            IOException {
        logr.log(Level.INFO, "Compiling schemas...\n" + xsdLocations);
        URL entityCatalog = this.getClass().getResource(
                ETS_ROOT_PKG + "schema-catalog.xml");
        XmlSchemaCompiler xsdCompiler = new XmlSchemaCompiler(entityCatalog);
        Schema schema = xsdCompiler.compileXmlSchema(xsdLocations
                .toArray(new URI[xsdLocations.size()]));
        Assert.assertNotNull(schema, ErrorMessage.format(
                ErrorMessageKeys.SCHEMA_ERROR, xsdLocations));
        ValidationErrorHandler errHandler = xsdCompiler.getErrorHandler();
        Assert.assertFalse(
                errHandler.errorsDetected(),
                ErrorMessage.format(ErrorMessageKeys.XSD_INVALID,
                        errHandler.getErrorCount(), errHandler.toString()));
        if (null != schema) {
            testContext.getSuite().setAttribute(
                    SuiteAttribute.SCHEMA.getName(), schema);
        }
    }

    /**
     * Sets the application schema(s) to check. This method is intended only to
     * facilitate unit testing.
     * 
     * @param schemaURIs
     *            A Set of URI objects representing schema locations.
     */
    void setSchemaLocations(Set<URI> schemaURIs) {
        this.xsdLocations = schemaURIs;
    }
}
