package org.opengis.cite.iso19139.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.opengis.cite.iso19139.util.ValidationUtils;
import org.opengis.cite.validation.SchematronValidator;

/**
 * Verifies the behavior of the ValidationUtils class.
 */
@Ignore
public class VerifyValidationUtils {

    public VerifyValidationUtils() {
    }

    @Test
    public void testBuildSchematronValidator() {
        String schemaRef = "http://schemas.opengis.net/gml/3.2.1/SchematronConstraints.xml";
        String phase = "";
        SchematronValidator result = ValidationUtils.buildSchematronValidator(
                schemaRef, phase);
        assertNotNull(result);
    }

    @Test
    public void extractRelativeSchemaReference() throws FileNotFoundException,
            XMLStreamException {
        File xmlFile = new File("src/test/resources/Alpha-1.xml");
        Set<URI> xsdSet = ValidationUtils.extractSchemaReferences(
                new StreamSource(xmlFile), null);
        URI schemaURI = xsdSet.iterator().next();
        assertTrue("Expected schema reference */xsd/alpha.xsd", schemaURI
                .toString().endsWith("/xsd/alpha.xsd"));
    }
}
