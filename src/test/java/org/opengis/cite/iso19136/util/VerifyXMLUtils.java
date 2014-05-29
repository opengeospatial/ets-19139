package org.opengis.cite.iso19136.util;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;

import static junit.framework.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Verifies the behavior of the XMLUtils class.
 */
public class VerifyXMLUtils {

    private static final String ATOM_NS = "http://www.w3.org/2005/Atom";
    private static final String EX_NS = "http://example.org/ns1";
    private static DocumentBuilder docBuilder;

    public VerifyXMLUtils() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        docBuilder = dbf.newDocumentBuilder();
    }

    @Test
    public void writeDocToString() throws SAXException, IOException {
        Document doc = docBuilder.parse(this.getClass().getResourceAsStream(
                "/atom-feed.xml"));
        String content = XMLUtils.writeNodeToString(doc);
        assertTrue("String should start with '<feed'",
                content.startsWith("<feed"));
    }

    @Test
    public void evaluateXPathExpression_match()
            throws XPathExpressionException, SAXException, IOException {
        Document doc = docBuilder.parse(this.getClass().getResourceAsStream(
                "/atom-feed.xml"));
        String expr = "/tns:feed/tns:author[ns1:phone]";
        Map<String, String> nsBindings = new HashMap<String, String>();
        nsBindings.put(ATOM_NS, "tns");
        nsBindings.put(EX_NS, "ns1");
        NodeList results = XMLUtils.evaluateXPath(doc, expr, nsBindings);
        assertTrue("Expected 1 node in results.", results.getLength() == 1);
        assertEquals("author", results.item(0).getLocalName());
    }

    @Test
    public void evaluateXPathExpression_noMatch()
            throws XPathExpressionException, SAXException, IOException {
        Document doc = docBuilder.parse(this.getClass().getResourceAsStream(
                "/atom-feed.xml"));
        String expr = "/tns:feed/tns:author[ns1:blog]";
        Map<String, String> nsBindings = new HashMap<String, String>();
        nsBindings.put(ATOM_NS, "tns");
        nsBindings.put(EX_NS, "ns1");
        NodeList results = XMLUtils.evaluateXPath(doc, expr, nsBindings);
        assertTrue("Expected empty results.", results.getLength() == 0);
    }

    @Test(expected = XPathExpressionException.class)
    public void evaluateXPathExpression_booleanResult()
            throws XPathExpressionException, SAXException, IOException {
        Document doc = docBuilder.parse(this.getClass().getResourceAsStream(
                "/atom-feed.xml"));
        String expr = "count(//tns:entry) > 0";
        Map<String, String> nsBindings = new HashMap<String, String>();
        nsBindings.put(ATOM_NS, "tns");
        NodeList results = XMLUtils.evaluateXPath(doc, expr, nsBindings);
        assertNull(results);
    }

    @Test
    public void createElement_Alpha() {
        QName qName = new QName("http://example.org", "Alpha");
        Element elem = XMLUtils.createElement(qName);
        assertEquals("Alpha", elem.getLocalName());
        assertNull(elem.getParentNode());
        assertNotNull(elem.getOwnerDocument());
    }

    @Test
    public void getFragment_shorthandPointer() throws SAXException, IOException {
        Document doc = docBuilder.parse(this.getClass().getResourceAsStream(
                "/features/FZK-Haus-LoD2-KIT.xml"));
        String fragmentId = "PolyID7353_166_774155_320806";
        Element elem = XMLUtils.getFragment(doc, fragmentId);
        assertNotNull("Expected matching element.", elem);
        assertNull("Duplicate node should have no parent.",
                elem.getParentNode());
        assertEquals("Element has unexpected local name.", "Polygon",
                elem.getLocalName());
    }

    @Test
    public void getPropertyValueInSameDoc_validShorthandPointer()
            throws SAXException, IOException, XPathExpressionException {
        URL url = this.getClass()
                .getResource("/features/FZK-Haus-LoD2-KIT.xml");
        Document doc = docBuilder.parse(url.toString());
        String xpath = "//gml:surfaceMember[4]";
        NodeList results = XMLUtils.evaluateXPath(doc, xpath, null);
        Node value = XMLUtils.getPropertyValue(results.item(0));
        assertNotNull("Expected property value.", value);
        assertEquals("Element has unexpected local name.", "Polygon",
                value.getLocalName());
    }

    @Test
    public void getPropertyValueInSameDoc_invalidShorthandPointer()
            throws SAXException, IOException, XPathExpressionException {
        URL url = this.getClass()
                .getResource("/features/FZK-Haus-LoD2-KIT.xml");
        Document doc = docBuilder.parse(url.toString());
        String xpath = "//gml:surfaceMember[7]";
        NodeList results = XMLUtils.evaluateXPath(doc, xpath, null);
        Node value = XMLUtils.getPropertyValue(results.item(0));
        assertNull("Expected property value to be null.", value);
    }

    @Test
    public void getPropertyValueInExternalDoc_validShorthandPointer()
            throws SAXException, IOException, XPathExpressionException {
        URL url = this.getClass()
                .getResource("/features/FZK-Haus-LoD2-KIT.xml");
        Document doc = docBuilder.parse(url.toString());
        String xpath = "//gml:surfaceMember[1]";
        NodeList results = XMLUtils.evaluateXPath(doc, xpath, null);
        Node value = XMLUtils.getPropertyValue(results.item(0));
        assertNotNull("Expected property value.", value);
        assertEquals("Element has unexpected local name.", "Polygon",
                value.getLocalName());
    }
}
