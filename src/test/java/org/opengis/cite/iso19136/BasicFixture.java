package org.opengis.cite.iso19136;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import org.apache.xerces.xs.XSModel;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.opengis.cite.validation.XSModelBuilder;
import org.opengis.cite.validation.XmlSchemaCompiler;
import org.xml.sax.SAXException;

public class BasicFixture {
    protected static XmlSchemaCompiler xsdCompiler;
    protected static DocumentBuilder docBuilder;
    protected static final String NS1 = "http://example.org/ns1";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void initBasicFixture() throws ParserConfigurationException {
        URL schemaCatalog = BasicFixture.class
                .getResource("/schema-catalog.xml");
        xsdCompiler = new XmlSchemaCompiler(schemaCatalog);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        docBuilder = dbf.newDocumentBuilder();
    }

    protected static XSModel createXSModel(URL schemaUrl, URI targetNamespace)
            throws IOException, SAXException {
        Schema xsd = xsdCompiler.compileXmlSchema(new StreamSource(schemaUrl
                .openStream(), schemaUrl.toString()));
        XSModel model = XSModelBuilder.buildXMLSchemaModel(xsd,
                targetNamespace.toString());
        return model;
    }
}
