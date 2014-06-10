package org.opengis.cite.iso19139.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.sf.saxon.s9api.DOMDestination;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import org.opengis.cite.iso19139.Namespaces;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Provides various utility methods for accessing or manipulating XML
 * representations.
 */
public class XMLUtils {

    /**
     * Writes the content of a DOM Node to a String. An XML declaration is
     * always omitted.
     *
     * @param node The DOM Node to be serialized.
     * @return A String representing the content of the given node.
     */
    public static String writeNodeToString(Node node) {
        StringWriter writer = null;
        try {
            Transformer identity = TransformerFactory.newInstance()
                    .newTransformer();
            Properties outProps = new Properties();
            outProps.setProperty("encoding", "UTF-8");
            outProps.setProperty("omit-xml-declaration", "yes");
            outProps.setProperty("indent", "yes");
            identity.setOutputProperties(outProps);
            writer = new StringWriter();
            identity.transform(new DOMSource(node), new StreamResult(writer));
        } catch (TransformerException ex) {
            TestSuiteLogger.log(Level.WARNING, "Failed to serialize DOM node: "
                    + node.getNodeName(), ex);
        }
        return writer.toString();
    }

    /**
     * Writes the content of a DOM Node to a byte stream. An XML declaration is
     * always omitted.
     *
     * @param node The DOM Node to be serialized.
     * @param outputStream The destination OutputStream reference.
     */
    public static void writeNode(Node node, OutputStream outputStream) {
        try {
            Transformer idTransformer = TransformerFactory.newInstance()
                    .newTransformer();
            Properties outProps = new Properties();
            outProps.setProperty(OutputKeys.METHOD, "xml");
            outProps.setProperty(OutputKeys.ENCODING, "UTF-8");
            outProps.setProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            outProps.setProperty(OutputKeys.INDENT, "yes");
            idTransformer.setOutputProperties(outProps);
            idTransformer.transform(new DOMSource(node), new StreamResult(
                    outputStream));
        } catch (TransformerException ex) {
            String nodeName = (node.getNodeType() == Node.DOCUMENT_NODE) ? Document.class
                    .cast(node).getDocumentElement().getNodeName()
                    : node.getNodeName();
            TestSuiteLogger.log(Level.WARNING, "Failed to serialize DOM node: "
                    + nodeName, ex);
        }
    }

    /**
     * Evaluates an XPath 1.0 expression using the given context and returns the
     * result as a node set.
     *
     * @param context The context node.
     * @param expr An XPath expression.
     * @param namespaceBindings A collection of namespace bindings for the XPath
     * expression, where each entry maps a namespace URI (key) to a prefix
     * (value). Standard bindings do not need to be declared (see
     * {@link NamespaceBindings#withStandardBindings()}.
     * @return A NodeList containing nodes that satisfy the expression (it may
     * be empty).
     * @throws XPathExpressionException If the expression cannot be evaluated
     * for any reason.
     */
    public static NodeList evaluateXPath(Node context, String expr,
            Map<String, String> namespaceBindings)
            throws XPathExpressionException {
        return (NodeList) evaluateXPath(context, expr, namespaceBindings,
                XPathConstants.NODESET);
    }

    /**
     * Evaluates an XPath 1.0 expression using the given context and returns the
     * result as the specified type.
     *
     * @param context The context node.
     * @param expr An XPath expression.
     * @param namespaceBindings A collection of namespace bindings for the XPath
     * expression, where each entry maps a namespace URI (key) to a prefix
     * (value). Standard bindings do not need to be declared (see
     * {@link NamespaceBindings#withStandardBindings()}.
     * @param returnType The desired return type (as declared in
     * {@link XPathConstants} ).
     * @return The result converted to the desired returnType.
     * @throws XPathExpressionException If the expression cannot be evaluated
     * for any reason.
     */
    public static Object evaluateXPath(Node context, String expr,
            Map<String, String> namespaceBindings, QName returnType)
            throws XPathExpressionException {
        NamespaceBindings bindings = NamespaceBindings.withStandardBindings();
        bindings.addAllBindings(namespaceBindings);
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(bindings);
        return xpath.evaluate(expr, context, returnType);
    }

    /**
     * Evaluates an XPath 1.0 expression using the given Source object and
     * returns the result as the specified type.
     *
     * @param source A Source object (not StAXSource) that supplies an XML
     * entity.
     * @param expr An XPath expression.
     * @param namespaceBindings A collection of namespace bindings for the XPath
     * expression, where each entry maps a namespace URI (key) to a prefix
     * (value). Standard bindings do not need to be declared (see
     * {@link NamespaceBindings#withStandardBindings()}.
     * @param returnType The desired return type (as declared in
     * {@link XPathConstants} ).
     * @return The result converted to the desired returnType.
     * @throws XPathExpressionException If the expression cannot be evaluated
     * for any reason.
     */
    public static Object evaluateXPath(Source source, String expr,
            Map<String, String> namespaceBindings, QName returnType)
            throws XPathExpressionException {
        if (StAXSource.class.isInstance(source)) {
            throw new IllegalArgumentException("StAXSource not supported.");
        }
        if (DOMSource.class.isInstance(source)) {
            DOMSource domSource = (DOMSource) source;
            return evaluateXPath(domSource.getNode(), expr, namespaceBindings,
                    returnType);
        }
        InputSource xmlSource = null;
        if (StreamSource.class.isInstance(source)) {
            StreamSource streamSource = (StreamSource) source;
            xmlSource = new InputSource(streamSource.getInputStream());
            xmlSource.setSystemId(source.getSystemId());
        }
        if (SAXSource.class.isInstance(source)) {
            SAXSource saxSource = (SAXSource) source;
            xmlSource = saxSource.getInputSource();
            xmlSource.setSystemId(source.getSystemId());
        }
        NamespaceBindings bindings = NamespaceBindings.withStandardBindings();
        bindings.addAllBindings(namespaceBindings);
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(bindings);
        return xpath.evaluate(expr, xmlSource, returnType);
    }

    /**
     * Evaluates an XPath 2.0 expression using the Saxon s9api interfaces.
     *
     * @param xmlSource The XML Source.
     * @param expr The XPath expression to be evaluated.
     * @param nsBindings A collection of namespace bindings required to evaluate
     * the XPath expression, where each entry maps a namespace URI (key) to a
     * prefix (value); this may be {@code null} if not needed.
     * @return An XdmValue object representing a value in the XDM data model;
     * this is a sequence of zero or more items, where each item is either an
     * atomic value or a node.
     * @throws SaxonApiException If an error occurs while evaluating the
     * expression; this always wraps some other underlying exception.
     */
    public static XdmValue evaluateXPath2(Source xmlSource, String expr,
            Map<String, String> nsBindings) throws SaxonApiException {
        Processor proc = new Processor(false);
        XPathCompiler compiler = proc.newXPathCompiler();
        if (null != nsBindings) {
            for (String nsURI : nsBindings.keySet()) {
                compiler.declareNamespace(nsBindings.get(nsURI), nsURI);
            }
        }
        XPathSelector xpath = compiler.compile(expr).load();
        DocumentBuilder builder = proc.newDocumentBuilder();
        XdmNode node = null;
        if (DOMSource.class.isInstance(xmlSource)) {
            DOMSource domSource = (DOMSource) xmlSource;
            node = builder.wrap(domSource.getNode());
        } else {
            node = builder.build(xmlSource);
        }
        xpath.setContextItem(node);
        return xpath.evaluate();
    }

    /**
     * Creates a new Element having the specified qualified name. The element
     * must be {@link Document#adoptNode(Node) adopted} when inserted into
     * another Document.
     *
     * @param qName A QName object.
     * @return An Element node (with a Document owner but no parent).
     */
    public static Element createElement(QName qName) {
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .newDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Element elem = doc.createElementNS(qName.getNamespaceURI(),
                qName.getLocalPart());
        return elem;
    }

    /**
     * Returns a List of all descendant Element nodes having the specified
     * [namespace name] property. The elements are listed in document order.
     *
     * @param node The node to search from.
     * @param namespaceURI An absolute URI denoting a namespace name.
     * @return A List containing elements in the specified namespace; the list
     * is empty if there are no elements in the namespace.
     */
    public static List<Element> getElementsByNamespaceURI(Node node,
            String namespaceURI) {
        List<Element> list = new ArrayList<Element>();
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (child.getNamespaceURI().equals(namespaceURI)) {
                list.add((Element) child);
            }
        }
        return list;
    }

    /**
     * Transforms the content of a DOM Node using a specified XSLT stylesheet.
     *
     * @param xslt A Source object representing a stylesheet (XSLT 1.0 or 2.0).
     * @param source A Node representing the XML source. If it is an Element
     * node it will be imported into a new DOM Document.
     * @return A DOM Document containing the result of the transformation.
     */
    public static Document transform(Source xslt, Node source) {
        Document sourceDoc = null;
        Document resultDoc = null;
        try {
            resultDoc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().newDocument();
            if (source.getNodeType() == Node.DOCUMENT_NODE) {
                sourceDoc = (Document) source;
            } else {
                sourceDoc = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder().newDocument();
                sourceDoc.appendChild(sourceDoc.importNode(source, true));
            }
        } catch (ParserConfigurationException pce) {
            throw new RuntimeException(pce);
        }
        Processor processor = new Processor(false);
        XsltCompiler compiler = processor.newXsltCompiler();
        try {
            XsltExecutable exec = compiler.compile(xslt);
            XsltTransformer transformer = exec.load();
            transformer.setSource(new DOMSource(sourceDoc));
            transformer.setDestination(new DOMDestination(resultDoc));
            transformer.transform();
        } catch (SaxonApiException e) {
            throw new RuntimeException(e);
        }
        return resultDoc;
    }

    /**
     * Reads the given property element and returns either (a) the child
     * element, or (b) the XLink referent. If the xlink:href attribute is
     * present an attempt will be made to dereference the URI value, which may
     * contain a fragment identifier (a string that adheres to the XPointer
     * syntax).
     *
     * @param propertyNode A property node; the value is supplied in-line or by
     * reference.
     * @return A DOM Node representing the property value, or {@code null} if it
     * cannot be accessed or parsed.
     */
    public static Node getPropertyValue(final Node propertyNode) {
        Element value = null;
        Element propElem = (Element) propertyNode;
        String href = propElem.getAttributeNS(Namespaces.XLINK, "href");
        if (href.isEmpty()) {
            value = (Element) propElem.getElementsByTagName("*").item(0);
        } else {
            URI uriRef = null;
            try {
                Document referent = null;
                uriRef = URI.create(href);
                if (!uriRef.isAbsolute()) {
                    String baseURI = propertyNode.getOwnerDocument()
                            .getBaseURI();
                    uriRef = URIUtils.resolveRelativeURI(baseURI,
                            uriRef.toString());
                }
                referent = URIUtils.parseURI(uriRef);
                if (null == uriRef.getFragment()) {
                    value = referent.getDocumentElement();
                } else {
                    value = getFragment(referent, uriRef.getFragment());
                }
            } catch (SAXException saxEx) {
                TestSuiteLogger.log(Level.WARNING, String.format(
                        "Failed to read value of property %s from %s",
                        propertyNode.getNodeName(), uriRef));
            } catch (IOException ioEx) {
                TestSuiteLogger.log(Level.WARNING, String.format(
                        "Failed to read value of property %s from %s",
                        propertyNode.getNodeName(), uriRef));
            } catch (Exception ioEx) {
                TestSuiteLogger.log(Level.WARNING, String.format(
                        "Failed to read value of property %s from %s",
                        propertyNode.getNodeName(), uriRef));
            }
        }
        return value;
    }

    /**
     * Extracts the specified fragment from the given XML source document. The
     * fragment identifier is expected to conform to the W3C XPointer framework.
     * However, only shorthand pointers are currently supported.
     *
     * <p>
     * In GMD documents such pointers refer to the element that has a matching
     * gmd:id attribute value; this attribute is a <a target="_blank"
     * href="http://www.w3.org/TR/xptr-framework/#term-sdi">schema-determined
     * ID</a> as defined in the XPointer specification.
     * </p>
     *
     * @param doc A DOM Document.
     * @param fragmentId A fragment identifier that adheres to the XPointer
     * syntax.
     * @return A copy of the matching Element, or {@code null} if no matching
     * element was found.
     * @see <a target="_blank"
     * href="http://www.w3.org/TR/xptr-framework/">XPointer Framework</a>
     * @see <a target="_blank"
     * href="http://www.w3.org/2005/04/xpointer-schemes/">XPointer Registry</a>
     */
    public static Element getFragment(Document doc, String fragmentId) {
        if (fragmentId.indexOf('(') > 0) {
            throw new UnsupportedOperationException(
                    "Scheme-based pointers are not currently supported.");
        }
        Element fragment = null;
        String expr = String.format("//*[@gmd:id='%s']", fragmentId);
        try {
            NodeList nodeList = evaluateXPath(doc, expr, null);
            if (nodeList.getLength() > 0) {
                fragment = (Element) nodeList.item(0).cloneNode(true);
            }
        } catch (XPathExpressionException xpe) {
            throw new RuntimeException(xpe);
        }
        return fragment;
    }
}
