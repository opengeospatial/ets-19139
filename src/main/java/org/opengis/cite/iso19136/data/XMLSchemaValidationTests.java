package org.opengis.cite.iso19136.data;

import java.io.IOException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.opengis.cite.iso19136.ErrorMessage;
import org.opengis.cite.iso19136.ErrorMessageKeys;
import org.opengis.cite.iso19136.SuiteAttribute;
import org.opengis.cite.validation.ValidationErrorHandler;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

/**
 * Verifies that a GML instance document is valid with respect to an application
 * schema.
 * 
 * <h6 style="margin-bottom: 0.5em">Sources</h6>
 * <ul>
 * <li>ISO 19136:2007, cl. A.3: Abstract test suite for GML documents</li>
 * </ul>
 */
public class XMLSchemaValidationTests extends DataFixture {

    /**
     * A GML application schema.
     */
    private Schema appSchema;

    /**
     * Obtains the GML application schema from the ISuite context. The value of
     * the {@link org.opengis.cite.iso19136.SuiteAttribute#SCHEMA} attribute is
     * expected to be a Schema object.
     * 
     * @param testContext
     *            The test (group) context.
     */
    @BeforeClass
    public void getXMLSchema(ITestContext testContext) {
        this.appSchema = (Schema) testContext.getSuite().getAttribute(
                SuiteAttribute.SCHEMA.getName());
    }

    /**
     * [{@code Test}] Verifies that a GML instance is valid with respect to its
     * application schema.
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136:2007, cl. A.3.4: Valid XML</li>
     * </ul>
     * 
     * @throws SAXException
     *             If a fatal error occurs (e.g. instance is not well-formed).
     */
    @Test
    public void isXMLSchemaValid() throws SAXException {
        Validator validator = this.appSchema.newValidator();
        ValidationErrorHandler errHandler = new ValidationErrorHandler();
        validator.setErrorHandler(errHandler);
        try {
            validator.validate(new StreamSource(this.dataFile));
        } catch (IOException e) {
            // ignore--not processing a SAXSource here (see API documentation)
        }
        Assert.assertFalse(errHandler.errorsDetected(), ErrorMessage.format(
                ErrorMessageKeys.NOT_SCHEMA_VALID, errHandler.getErrorCount(),
                errHandler.toString()));
    }

}
