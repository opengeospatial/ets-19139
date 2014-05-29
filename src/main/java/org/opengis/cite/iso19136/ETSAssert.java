package org.opengis.cite.iso19136;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.logging.Level;

import javax.ws.rs.core.MediaType;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;
import org.opengis.cite.iso19136.util.NamespaceBindings;
import org.opengis.cite.iso19136.util.XMLSchemaModelUtils;
import org.opengis.cite.iso19136.util.XMLUtils;
import org.opengis.cite.iso19136.ErrorMessage;
import org.opengis.cite.iso19136.ErrorMessageKeys;
import org.opengis.cite.iso19136.util.TestSuiteLogger;
import org.opengis.cite.validation.SchematronValidator;
import org.opengis.cite.validation.ValidationErrorHandler;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides a set of custom assertion methods.
 */
public class ETSAssert {

    private ETSAssert() {
    }

    /**
     * Asserts that the qualified name of a DOM Node matches the expected value.
     * 
     * @param node
     *            The Node to check.
     * @param qName
     *            A QName object containing a namespace name (URI) and a local
     *            part.
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
     * <li>ows: {@value org.opengis.cite.iso19136.Namespaces#OWS}</li>
     * <li>xlink: {@value org.opengis.cite.iso19136.Namespaces#XLINK}</li>
     * <li>gml: {@value org.opengis.cite.iso19136.general.GML32#NS_NAME}</li>
     * </ul>
     * 
     * @param expr
     *            A valid XPath 1.0 expression.
     * @param context
     *            The context node.
     * @param namespaceBindings
     *            A collection of namespace bindings for the XPath expression,
     *            where each entry maps a namespace URI (key) to a prefix
     *            (value). It may be {@code null}.
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
            String msg = ErrorMessage
                    .format(ErrorMessageKeys.XPATH_ERROR, expr);
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
     * @param validator
     *            The Validator to use.
     * @param source
     *            The XML Source to be validated.
     */
    public static void assertSchemaValid(Validator validator, Source source) {
        ValidationErrorHandler errHandler = new ValidationErrorHandler();
        validator.setErrorHandler(errHandler);
        try {
            validator.validate(source);
        } catch (Exception e) {
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
     * @param schemaRef
     *            A URL that denotes the location of a Schematron schema.
     * @param xmlSource
     *            The XML Source to be validated.
     */
    public static void assertSchematronValid(URL schemaRef, Source xmlSource) {
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
        DOMResult result = validator.validate(xmlSource);
        Assert.assertFalse(validator.ruleViolationsDetected(), ErrorMessage
                .format(ErrorMessageKeys.NOT_SCHEMA_VALID,
                        validator.getRuleViolationCount(),
                        XMLUtils.writeNodeToString(result.getNode())));
    }

    /**
     * Asserts that the given URL is resolvable; that is, it can be dereferenced
     * to obtain a resource representation that corresponds to an expected media
     * type.
     * 
     * @param url
     *            The URL to be dereferenced.
     * @param expectedMediaType
     *            The expected media type of the representation; if not
     *            specified any type of content is acceptable.
     */
    public static void assertURLIsResolvable(URL url,
            MediaType expectedMediaType) {
        MediaType contentType;
        int contentLength = 0;
        // WARNING: Ignores HTTP redirects 3xx
        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            contentLength = urlConnection.getContentLength();
            contentType = MediaType.valueOf(urlConnection.getContentType());
        } catch (IOException iox) {
            throw new AssertionError(String.format(
                    "Failed to connect to URL %s \n %s", url.toString(), iox));
        }
        Assert.assertTrue(contentLength > 0,
                ErrorMessage.get(ErrorMessageKeys.MISSING_ENTITY));
        if (null != expectedMediaType) {
            Assert.assertEquals(contentType, expectedMediaType,
                    ErrorMessage.get(ErrorMessageKeys.UNEXPECTED_MEDIA_TYPE));
        }
    }

    /**
     * Asserts that the content model of the given type definition mimics a GML
     * property type. The type is presumably not derived (by restriction) from
     * some base GML property type (gml:AssociationRoleType, gml:ReferenceType,
     * gml:InlinePropertyType). The following constraints apply:
     * 
     * <ul>
     * <li>if not empty, includes only one element declaration (the value
     * object, which may occur more than once in an array property type)</li>
     * <li>if empty (or possibly empty), includes the attribute group
     * gml:AssociationAttributeGroup</li>
     * <li>includes the attribute group gml:OwnershipAttributeGroup</li>
     * <li>does not contain a wildcard</li>
     * <li>the property value may substitute for the designated head element (if
     * specified)</li>
     * </ul>
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136:2007, cl. 7.2.3.3: abstractAssociationRole,
     * AssociationRoleType</li>
     * <li>ISO 19136:2007, cl. 7.2.3.7: abstractReference, ReferenceType</li>
     * <li>ISO 19136:2007, cl. 7.2.3.8: abstractInlineProperty,
     * InlinePropertyType</li>
     * </ul>
     * 
     * @param model
     *            An XSModel object representing an XML Schema resource.
     * @param typeDef
     *            An XSComplexTypeDefinition object representing a complex type
     *            definition.
     * @param head
     *            The head of the substitution group to which the property value
     *            belongs (may be {@code null}).
     */
    public static void assertValidPropertyType(XSModel model,
            XSComplexTypeDefinition typeDef, XSElementDeclaration head) {
        boolean isEmpty = (typeDef.getContentType() == XSComplexTypeDefinition.CONTENTTYPE_EMPTY);
        XSObjectList attrUses = typeDef.getAttributeUses();
        if (isEmpty) {
            Assert.assertNotNull(
                    getAttributeUseByName(attrUses, new QName(Namespaces.XLINK,
                            "href")), ErrorMessage.format(
                            ErrorMessageKeys.ATTRIB_REQUIRED, "xlink:href",
                            typeDef.getNamespace(), typeDef.getName()));
        } else {
            XSParticle topParticle = typeDef.getParticle();
            if (topParticle.getMinOccurs() == 0) {
                // may be empty, so a reference is required
                Assert.assertNotNull(
                        getAttributeUseByName(attrUses, new QName(
                                Namespaces.XLINK, "href")),
                        "Property value has minOccurs = 0. "
                                + ErrorMessage.format(
                                        ErrorMessageKeys.ATTRIB_REQUIRED,
                                        "xlink:href", typeDef.getNamespace(),
                                        typeDef.getName()));
            }
            XSTerm term = topParticle.getTerm();
            Assert.assertTrue(XSModelGroup.class.isInstance(term), ErrorMessage
                    .format(ErrorMessageKeys.MODEL_GRP_EXPECTED,
                            typeDef.getNamespace(), typeDef.getName()));
            XSModelGroup group = (XSModelGroup) term;
            Assert.assertEquals(
                    group.getCompositor(),
                    XSModelGroup.COMPOSITOR_SEQUENCE,
                    ErrorMessage.format(ErrorMessageKeys.SEQ_EXPECTED,
                            typeDef.getNamespace(), typeDef.getName()));
            boolean isArrayProperty = (topParticle.getMaxOccursUnbounded() || (topParticle
                    .getMaxOccurs() > 1));
            if (isArrayProperty) {
                Assert.assertNull(
                        getAttributeUseByName(attrUses, new QName(
                                Namespaces.XLINK, "href")), ErrorMessage
                                .format(ErrorMessageKeys.ATTRIB_PROHIBITED,
                                        "xlink:href", typeDef.getNamespace(),
                                        typeDef.getName()));
            }
            Assert.assertTrue(group.getParticles().size() == 1, ErrorMessage
                    .format(ErrorMessageKeys.TOO_MANY_PARTS,
                            typeDef.getNamespace(), typeDef.getName()));

            XSParticle particle = (XSParticle) group.getParticles().item(0);
            Assert.assertTrue(XSElementDeclaration.class.isInstance(particle
                    .getTerm()), ErrorMessage.format(
                    ErrorMessageKeys.ELEM_DECL_REQUIRED,
                    typeDef.getNamespace(), typeDef.getName()));
            XSElementDeclaration propValue = (XSElementDeclaration) particle
                    .getTerm();
            if (null != head) {
                Assert.assertTrue(
                        XMLSchemaModelUtils.getElementsByAffiliation(model,
                                head).contains(propValue), ErrorMessage.format(
                                ErrorMessageKeys.DISALLOWED_SUBSTITUTION,
                                propValue, head, typeDef.getNamespace(),
                                typeDef.getName()));
            }
        }
    }

    /**
     * Returns an attribute use schema component identified by qualified name.
     * 
     * @param attrUses
     *            An XSObjectList containing a collection of XSAttributeUse
     *            items.
     * @param qName
     *            The qualified name of the attribute declaration to seek. The
     *            namespace name may be empty ("").
     * @return The matching XSAttributeUse object, or {@code null} if one cannot
     *         be found.
     */
    private static XSAttributeUse getAttributeUseByName(XSObjectList attrUses,
            QName qName) {
        XSAttributeUse attrUse = null;
        for (int i = 0; i < attrUses.getLength(); i++) {
            XSAttributeUse item = (XSAttributeUse) attrUses.item(i);
            XSAttributeDeclaration attrDecl = item.getAttrDeclaration();
            if (attrDecl.getName().equals(qName.getLocalPart())) {
                if (attrDecl.getNamespace() != null
                        && !attrDecl.getNamespace().equals(
                                qName.getNamespaceURI())) {
                    // XSObject.getNamespace() may return null!!
                    continue;
                }
                attrUse = item;
                break;
            }
        }
        return attrUse;
    }

    /**
     * Asserts that the given XML entity contains the expected number of
     * descendant elements having the specified name.
     * 
     * @param xmlEntity
     *            A Document representing an XML entity.
     * @param elementName
     *            The qualified name of the element.
     * @param expectedCount
     *            The expected number of occurrences.
     */
    public static void assertDescendantElementCount(Document xmlEntity,
            QName elementName, int expectedCount) {
        NodeList features = xmlEntity.getElementsByTagNameNS(
                elementName.getNamespaceURI(), elementName.getLocalPart());
        Assert.assertEquals(features.getLength(), expectedCount, String.format(
                "Unexpected number of %s descendant elements.", elementName));
    }
}
