package org.opengis.cite.iso19136.data.spatial;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Verifies the behavior of the EnvelopeTests class.
 */
public class VerifyEnvelopeTests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void findEnvelopes_expectOne() throws URISyntaxException,
            JAXBException, XMLStreamException, IOException {
        URL url = this.getClass().getResource("/SimpleFeature-1.xml");
        File dataFile = new File(url.toURI());
        EnvelopeTests iut = new EnvelopeTests();
        iut.setDataFile(dataFile);
        iut.findEnvelopes();
        assertEquals("Unexpected number of envelopes.", 1, iut.envelopes.size());
    }

    @Test
    public void envelope_valid() throws URISyntaxException, JAXBException,
            XMLStreamException, IOException {
        URL url = this.getClass().getResource("/envelopes/Envelope-valid.xml");
        File dataFile = new File(url.toURI());
        EnvelopeTests iut = new EnvelopeTests();
        iut.setDataFile(dataFile);
        iut.findEnvelopes();
        iut.checkEnvelopePositions();
    }

    @Test
    public void envelope_noCRS() throws URISyntaxException, JAXBException,
            XMLStreamException, IOException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("has no CRS reference");
        URL url = this.getClass().getResource("/envelopes/Envelope-noCRS.xml");
        File dataFile = new File(url.toURI());
        EnvelopeTests iut = new EnvelopeTests();
        iut.setDataFile(dataFile);
        iut.findEnvelopes();
        iut.envelopeHasCRSReference();
    }

    @Test
    public void envelope_invalidCorner() throws URISyntaxException,
            JAXBException, XMLStreamException, IOException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("expected lowerCorner[1] < upperCorner[1]");
        URL url = this.getClass().getResource(
                "/envelopes/Envelope-invalidCorner.xml");
        File dataFile = new File(url.toURI());
        EnvelopeTests iut = new EnvelopeTests();
        iut.setDataFile(dataFile);
        iut.findEnvelopes();
        iut.checkEnvelopePositions();
    }

}
