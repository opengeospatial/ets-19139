package org.opengis.cite.iso19139;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.opengis.cite.iso19139.util.NamespaceBindings;
import org.opengis.cite.iso19139.util.TestSuiteLogger;
import org.opengis.cite.iso19139.util.XMLUtils;
import org.opengis.cite.validation.SchematronValidator;
import org.opengis.cite.validation.ValidationErrorHandler;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Provides a set of custom assertion methods.
 */
public class ETSAssert {

    private ETSAssert() {
    }

    /**
     * Asserts that the qualified name of a DOM Node matches the expected value.
     *
     * @param node The Node to check.
     * @param qName A QName object containing a namespace name (URI) and a local
     * part.
     */
    public static void assertQualifiedName(Node node, QName qName) {
        Assert.assertEquals(node.getLocalName(), qName.getLocalPart(),
                ErrorMessage.get(ErrorMessageKeys.LOCAL_NAME));
        Assert.assertEquals(node.getNamespaceURI(), qName.getNamespaceURI(),
                ErrorMessage.get(ErrorMessageKeys.NAMESPACE_NAME));
    }

    /**
     * Asserts that an XPath 1.0 expression holds true for the given evaluation
     * context. The following standard namespace bindings do not need to be
     * explicitly declared:
     *
     * <ul>
     * <li>ows: {@value org.opengis.cite.iso19139.Namespaces#OWS}</li>
     * <li>xlink: {@value org.opengis.cite.iso19139.Namespaces#XLINK}</li>
     * <li>gml: {@value org.opengis.cite.iso19139.Namespaces#GML}</li>
     * </ul>
     *
     * @param expr A valid XPath 1.0 expression.
     * @param context The context node.
     * @param namespaceBindings A collection of namespace bindings for the XPath
     * expression, where each entry maps a namespace URI (key) to a prefix
     * (value). It may be {@code null}.
     */
    public static void assertXPath(String expr, Node context,
            Map<String, String> namespaceBindings) {
        if (null == context) {
            throw new NullPointerException("Context node is null.");
        }
        NamespaceBindings bindings = NamespaceBindings.withStandardBindings();
        bindings.addAllBindings(namespaceBindings);
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(bindings);
        Boolean result;
        try {
            result = (Boolean) xpath.evaluate(expr, context,
                    XPathConstants.BOOLEAN);
        } catch (XPathExpressionException xpe) {
            String msg = ErrorMessage.format(ErrorMessageKeys.XPATH_ERROR, expr);
            TestSuiteLogger.log(Level.WARNING, msg, xpe);
            throw new AssertionError(msg);
        }
        Assert.assertTrue(
                result,
                ErrorMessage.format(ErrorMessageKeys.XPATH_RESULT,
                        context.getNodeName(), expr));
    }

    /**
     * Asserts that an XML resource is schema-valid.
     *
     * @param validator The Validator to use.
     * @param source The XML Source to be validated.
     */
    public static void assertSchemaValid(Validator validator, Source source) {
        ValidationErrorHandler errHandler = new ValidationErrorHandler();
        validator.setErrorHandler(errHandler);
        try {
            validator.validate(source);
        } catch (IOException e) {
            throw new AssertionError(ErrorMessage.format(
                    ErrorMessageKeys.XML_ERROR, e.getMessage()));
        } catch (SAXException e) {
            throw new AssertionError(ErrorMessage.format(
                    ErrorMessageKeys.XML_ERROR, e.getMessage()));
        }
        Assert.assertFalse(errHandler.errorsDetected(), ErrorMessage.format(
                ErrorMessageKeys.NOT_SCHEMA_VALID, errHandler.getErrorCount(),
                errHandler.toString()));
    }

    /**
     * Asserts that an XML resource satisfies all applicable constraints
     * specified in a Schematron (ISO 19757-3) schema. The "xslt2" query
     * language binding is supported. All patterns are checked.
     *
     * @param schemaRef A URL that denotes the location of a Schematron schema.
     * @param xmlSource The XML Source to be validated.
     * @param description Test Description
     */
    public static void assertSchematronValid(URL schemaRef, Source xmlSource,String description) {
        SchematronValidator validator;
        try {
            validator = new SchematronValidator(new StreamSource(
                    schemaRef.toString()), "#ALL");
        } catch (Exception e) {
            StringBuilder msg = new StringBuilder(
                    "Failed to process Schematron schema at ");
            msg.append(schemaRef).append('\n');
            msg.append(e.getMessage());
           throw new AssertionError(msg);
        }
        Result result = validator.validate(xmlSource);

        // Fetch error message when schema is not valid
        String errorMessage = ErrorMessage.format(ErrorMessageKeys.NOT_SCHEMA_VALID,
                validator.getRuleViolationCount(), XMLUtils.resultToString(result));
        String error = "";

        String delims = "<svrl:text>";
        String[] failedAssertMessage = errorMessage.split(delims);

        for (int l = 1; l < failedAssertMessage.length; l++) {
            if (failedAssertMessage[l].contains("</svrl:text>")) {
                failedAssertMessage[l] = failedAssertMessage[l].split("</svrl:text>")[0];
            }
            error = error + failedAssertMessage[l] + ",";
        }

        errorMessage = error;
        if (null != errorMessage && errorMessage.length() > 0) {
            int endIndex = errorMessage.lastIndexOf(",");
            if (endIndex != -1) {
                errorMessage = errorMessage.substring(0, endIndex);
            }
        }
        String[] output=description.split("=", 2);
        System.out.println("TEST NAME : \n"+ output[0]);
        System.out.println("DESCRIPTION :");
        String[] descript=output[1].split(",");
        for (String descript1 : descript) {
            System.out.println(descript1);
        }
        System.out.println("RESULT : ");        
        if(validator.ruleViolationsDetected()==true)
        {
            System.out.println("FAIL");
            System.out.println("REASON FOR FAIL : ");
            String[] errorSet=errorMessage.split(",");
            for (String errorSet1 : errorSet) {
                System.out.println(errorSet1);
            }
                    
        }
        else
        {
            System.out.println("PASS");
        }
        System.out.println();
        Assert.assertFalse(validator.ruleViolationsDetected(), errorMessage);
    }

    /**
     * Asserts that the given XML entity contains the expected number of
     * descendant elements having the specified name.
     *
     * @param xmlEntity A Document representing an XML entity.
     * @param elementName The qualified name of the element.
     * @param expectedCount The expected number of occurrences.
     */
    public static void assertDescendantElementCount(Document xmlEntity,
            QName elementName, int expectedCount) {
        NodeList features = xmlEntity.getElementsByTagNameNS(
                elementName.getNamespaceURI(), elementName.getLocalPart());
        Assert.assertEquals(features.getLength(), expectedCount, String.format(
                "Unexpected number of %s descendant elements.", elementName));
    }
}
