package org.opengis.cite.iso19136.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;

import javax.ws.rs.core.MediaType;
import javax.xml.namespace.QName;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.opengis.cite.iso19136.ETSAssert;
import org.opengis.cite.iso19136.ErrorMessage;
import org.opengis.cite.iso19136.ErrorMessageKeys;
import org.opengis.cite.iso19136.Namespaces;
import org.opengis.cite.iso19136.general.GML32;
import org.opengis.cite.iso19136.util.TestSuiteLogger;
import org.opengis.cite.iso19136.util.XMLUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.NodeList;

/**
 * Checks various property value constraints.
 */
public class PropertyValueTests extends DataFixture {

    private XMLInputFactory factory = XMLInputFactory.newInstance();

    /**
     * [{@code @Test}] If a gml:locationName property has a codeSpace attribute,
     * then the attribute value must identify a controlled list (that includes
     * the location name). The list resource must be available, but no
     * particular format is assumed.
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136:2007, cl. 9.4.2: locationName, locationReference</li>
     * </ul>
     * 
     * @throws FileNotFoundException
     *             If no data file is found.
     * @throws MalformedURLException
     *             If the codeSpace value is a malformed URL.
     */
    @Test
    public void validateLocationName() throws FileNotFoundException,
            MalformedURLException {
        Source source = new StreamSource(this.dataFile);
        String xpath = "//gml:locationName/@codeSpace";
        NodeList codeSpaceList;
        try {
            codeSpaceList = (NodeList) XMLUtils.evaluateXPath(source, xpath,
                    null, XPathConstants.NODESET);
        } catch (XPathExpressionException xpe) {
            throw new RuntimeException(xpe);
        }
        for (int i = 0; i < codeSpaceList.getLength(); i++) {
            URL url = new URL(codeSpaceList.item(i).getTextContent());
            ETSAssert.assertURLIsResolvable(url, null);
        }
    }

    /**
     * [{@code @Test}] The gml:locationReference property must include the
     * xlink:href attribute. When the URI value is dereferenced the result must
     * be a text value (that purports to describe the location of the feature).
     * 
     * <h6 style="margin-bottom: 0.5em">Sources</h6>
     * <ul>
     * <li>ISO 19136:2007, cl. 9.4.2: locationName, locationReference</li>
     * </ul>
     * 
     * @throws FileNotFoundException
     *             If no data file is found.
     * @throws MalformedURLException
     *             If a location reference is a malformed URL.
     * @throws URISyntaxException
     */
    @Test
    public void validateLocationReference() throws FileNotFoundException,
            MalformedURLException {
        InputStream inStream = new FileInputStream(this.dataFile);
        XMLEventReader reader = null;
        try {
            XMLEventReader baseReader = factory.createXMLEventReader(inStream);
            reader = factory.createFilteredReader(baseReader,
                    new GMLEventFilter("locationReference"));
            while (reader.hasNext()) {
                QName xlinkHref = new QName(Namespaces.XLINK, "href");
                StartElement locationRef = reader.nextEvent().asStartElement();
                Attribute href = locationRef.getAttributeByName(xlinkHref);
                Assert.assertNotNull(href, ErrorMessage.format(
                        ErrorMessageKeys.MISSING_INFOSET_ITEM, xlinkHref,
                        locationRef.getLocation().toString()));
                URI uri = URI.create(href.getValue());
                if (!uri.isAbsolute()) {
                    uri = this.dataFile.toURI().resolve(uri);
                }
                ETSAssert.assertURLIsResolvable(uri.toURL(),
                        MediaType.TEXT_PLAIN_TYPE);
            }
        } catch (XMLStreamException e) {
        } finally {
            try {
                reader.close();
                inStream.close();
            } catch (Exception e) {
                TestSuiteLogger.log(Level.INFO, "Failed to close resource.", e);
            }
        }
    }

    /**
     * An EventFilter that accepts GML elements specified by (local) name.
     */
    class GMLEventFilter implements EventFilter {

        String tagName;

        public GMLEventFilter(String localName) {
            tagName = localName;
        }

        @Override
        public boolean accept(XMLEvent event) {
            if (event.getEventType() != XMLStreamConstants.START_ELEMENT) {
                return false;
            }
            boolean isLocationReference = false;
            QName qName = new QName(GML32.NS_NAME, tagName);
            StartElement startTag = event.asStartElement();
            if (startTag.getName().equals(qName)) {
                isLocationReference = true;
            }
            return isLocationReference;
        }
    }
}
