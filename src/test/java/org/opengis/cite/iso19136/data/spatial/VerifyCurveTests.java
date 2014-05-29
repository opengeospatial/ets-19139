package org.opengis.cite.iso19136.data.spatial;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.opengis.cite.iso19136.BasicFixture;
import org.opengis.util.FactoryException;
import org.w3c.dom.DOMException;

import static org.junit.Assert.*;

/**
 * Verifies the behavior of the CurveTests class.
 */
public class VerifyCurveTests extends BasicFixture {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void findCurves_expect2() throws URISyntaxException {
        URL url = this.getClass().getResource("/geom/MultiCurve-1.xml");
        File dataFile = new File(url.toURI());
        CurveTests iut = new CurveTests();
        iut.setDataFile(dataFile);
        iut.findCurves();
        assertEquals("Unexpected number of curves.", 2,
                iut.curveNodes.getLength());
    }

    @Test
    public void verifyCurveHasValidCRS() throws URISyntaxException {
        URL url = this.getClass().getResource("/geom/MultiCurve-1.xml");
        File dataFile = new File(url.toURI());
        CurveTests iut = new CurveTests();
        iut.setDataFile(dataFile);
        iut.findCurves();
        iut.curveHasValidCRS();
    }

    @Test
    public void checkLineStringSegmentsAreValid() throws URISyntaxException,
            DOMException, FactoryException {
        URL url = this.getClass().getResource("/geom/MultiCurve-1.xml");
        File dataFile = new File(url.toURI());
        CurveTests iut = new CurveTests();
        iut.setDataFile(dataFile);
        iut.findCurves();
        iut.validCurveSegments();
    }

    @Test
    public void checkGeodesicStringSegmentsAreValid()
            throws URISyntaxException, DOMException, FactoryException {
        URL url = this.getClass().getResource("/geom/Curve-GeodesicString.xml");
        File dataFile = new File(url.toURI());
        CurveTests iut = new CurveTests();
        iut.setDataFile(dataFile);
        iut.findCurves();
        iut.validCurveSegments();
    }

    @Test
    public void checkCurveSegments_wrongAxisOrder() throws URISyntaxException,
            DOMException, FactoryException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("not covered by valid area of CRS");
        URL url = this.getClass().getResource(
                "/geom/Curve-LineString-axisOrder.xml");
        File dataFile = new File(url.toURI());
        CurveTests iut = new CurveTests();
        iut.setDataFile(dataFile);
        iut.findCurves();
        iut.validCurveSegments();
    }

    @Test
    public void missingCurveSegments() throws URISyntaxException, DOMException,
            FactoryException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("has no curve segments");
        URL url = this.getClass().getResource("/geom/Curve-empty.xml");
        File dataFile = new File(url.toURI());
        CurveTests iut = new CurveTests();
        iut.setDataFile(dataFile);
        iut.findCurves();
        iut.validCurveSegments();
    }

    @Test
    public void curveWithArcByCenterPoint() throws URISyntaxException,
            DOMException, FactoryException {
        URL url = this.getClass().getResource(
                "/geom/Curve-ArcByCenterPoint.xml");
        File dataFile = new File(url.toURI());
        CurveTests iut = new CurveTests();
        iut.setDataFile(dataFile);
        iut.findCurves();
        iut.validCurveSegments();
    }

    @Test
    public void curveWith3Segments() throws URISyntaxException, DOMException,
            FactoryException {
        URL url = this.getClass().getResource("/geom/Curve-ID_250.xml");
        File dataFile = new File(url.toURI());
        CurveTests iut = new CurveTests();
        iut.setDataFile(dataFile);
        iut.findCurves();
        iut.validCurveSegments();
    }
}
