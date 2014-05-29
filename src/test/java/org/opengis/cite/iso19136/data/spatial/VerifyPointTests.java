package org.opengis.cite.iso19136.data.spatial;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.xerces.xs.XSModel;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.opengis.cite.iso19136.BasicFixture;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

/**
 * Verifies the behavior of the PointTests class.
 */
public class VerifyPointTests extends BasicFixture {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void findPoints_expect2() throws URISyntaxException {
        URL url = this.getClass().getResource("/geom/MultiPoint-1.xml");
        File dataFile = new File(url.toURI());
        PointTests iut = new PointTests();
        iut.setDataFile(dataFile);
        iut.findPoints();
        assertEquals("Unexpected number of points.", 2, iut.points.getLength());
    }

    @Test
    public void findPointWithBearing() throws URISyntaxException, IOException,
            SAXException {
        PointTests iut = new PointTests();
        URL dataURL = this.getClass().getResource("/geom/PointWithBearing.xml");
        iut.setDataFile(new File(dataURL.toURI()));
        URL xsdURL = getClass().getResource("/xsd/customGeom.xsd");
        XSModel model = createXSModel(xsdURL, URI.create(NS1));
        iut.setSchemaModel(model);
        iut.findPoints();
        assertEquals("Unexpected number of points.", 1, iut.points.getLength());
        assertEquals("Point geometry has unexpected name.", "PointWithBearing",
                iut.points.item(0).getLocalName());
    }

    @Test
    public void verifyPointHasValidCRS() throws URISyntaxException {
        URL url = this.getClass().getResource("/geom/MultiPoint-1.xml");
        File dataFile = new File(url.toURI());
        PointTests iut = new PointTests();
        iut.setDataFile(dataFile);
        iut.findPoints();
        iut.pointHasValidCRS();
    }

    @Test
    public void verifyPointHasValidPosition() throws URISyntaxException {
        URL url = this.getClass().getResource("/geom/MultiPoint-1.xml");
        File dataFile = new File(url.toURI());
        PointTests iut = new PointTests();
        iut.setDataFile(dataFile);
        iut.findPoints();
        iut.pointHasValidPosition();
    }

    @Test
    public void pointIsOutsideValidArea() throws URISyntaxException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("not within CRS area of use");
        URL url = this.getClass().getResource("/geom/Point-axisOrder.xml");
        File dataFile = new File(url.toURI());
        PointTests iut = new PointTests();
        iut.setDataFile(dataFile);
        iut.findPoints();
        iut.pointHasValidPosition();
    }
}
