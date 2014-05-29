package org.opengis.cite.iso19136.data.spatial;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.xerces.xs.XSModel;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.cite.iso19136.BasicFixture;
import org.xml.sax.SAXException;

/**
 * Verifies the behavior of the SurfaceTests class.
 */
public class VerifySurfaceTests extends BasicFixture {

    private static XSModel model;

    @BeforeClass
    public static void initXSModel() throws IOException, SAXException {
        URL xsdURL = VerifySurfaceTests.class
                .getResource("/xsd/customGeom.xsd");
        model = createXSModel(xsdURL, URI.create(NS1));
    }

    @Test
    public void findSurfaces_expect1() throws URISyntaxException {
        URL url = this.getClass().getResource("/geom/ElevatedSurface.xml");
        File dataFile = new File(url.toURI());
        SurfaceTests iut = new SurfaceTests();
        iut.setDataFile(dataFile);
        iut.setSchemaModel(model);
        iut.findSurfaces();
        assertEquals("Unexpected number of surfaces.", 1,
                iut.surfaceNodes.getLength());
    }

    @Test
    public void validSurfaceBoundary_Surface() throws URISyntaxException {
        URL url = this.getClass().getResource(
                "/geom/Surface-PolygonPatch-1.xml");
        File dataFile = new File(url.toURI());
        SurfaceTests iut = new SurfaceTests();
        iut.setDataFile(dataFile);
        iut.setSchemaModel(model);
        iut.findSurfaces();
        iut.validSurfaceBoundary();
    }

    @Test
    public void validSurfaceBoundary_notClosed() throws URISyntaxException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("Exterior boundary of surface with @gml:id='g1' is not closed");
        URL url = this.getClass().getResource("/geom/Polygon-NotClosed.xml");
        File dataFile = new File(url.toURI());
        SurfaceTests iut = new SurfaceTests();
        iut.setDataFile(dataFile);
        iut.setSchemaModel(model);
        iut.findSurfaces();
        iut.validSurfaceBoundary();
    }

    @Test
    public void validSurfaceBoundary_PolygonWithInteriorRing()
            throws URISyntaxException {
        URL url = this.getClass().getResource("/geom/Polygon-InteriorRing.xml");
        File dataFile = new File(url.toURI());
        SurfaceTests iut = new SurfaceTests();
        iut.setDataFile(dataFile);
        iut.setSchemaModel(model);
        iut.findSurfaces();
        iut.validSurfaceBoundary();
    }

    @Test
    public void validSurfaceBoundary_InteriorTouchesExterior()
            throws URISyntaxException {
        URL url = this.getClass().getResource(
                "/geom/Polygon-InteriorTouchesExterior.xml");
        File dataFile = new File(url.toURI());
        SurfaceTests iut = new SurfaceTests();
        iut.setDataFile(dataFile);
        iut.setSchemaModel(model);
        iut.findSurfaces();
        iut.validSurfaceBoundary();
    }

    @Test
    public void validSurfaceBoundary_InteriorCrossesExterior()
            throws URISyntaxException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("Interior boundary not covered by surface with @gml:id='Polygon-Invalid-1'");
        thrown.expectMessage("Starting position: (49.2286, -123.0463");
        URL url = this.getClass().getResource(
                "/geom/Polygon-InteriorCrossesExterior.xml");
        File dataFile = new File(url.toURI());
        SurfaceTests iut = new SurfaceTests();
        iut.setDataFile(dataFile);
        iut.setSchemaModel(model);
        iut.findSurfaces();
        iut.validSurfaceBoundary();
    }

    @Test
    public void validSurfaceBoundary_InteriorNotClosed()
            throws URISyntaxException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("Interior boundary of surface with @gml:id='Polygon-Invalid-2' is not closed");
        thrown.expectMessage("Starting position: (49.2286, -123.0463");
        URL url = this.getClass().getResource(
                "/geom/Polygon-InteriorNotClosed.xml");
        File dataFile = new File(url.toURI());
        SurfaceTests iut = new SurfaceTests();
        iut.setDataFile(dataFile);
        iut.setSchemaModel(model);
        iut.findSurfaces();
        iut.validSurfaceBoundary();
    }

    @Test
    public void validSurfaceOrientation_TwoPatchesAreConsistent()
            throws URISyntaxException {
        URL url = this.getClass().getResource(
                "/geom/Surface-PolygonPatch-2.xml");
        File dataFile = new File(url.toURI());
        SurfaceTests iut = new SurfaceTests();
        iut.setDataFile(dataFile);
        iut.setSchemaModel(model);
        iut.findSurfaces();
        iut.validSurfaceOrientation();
    }

    @Test
    public void validSurfaceOrientation_exteriorIsCCW()
            throws URISyntaxException {
        URL url = this.getClass().getResource("/geom/Surface-ExteriorCCW.xml");
        File dataFile = new File(url.toURI());
        SurfaceTests iut = new SurfaceTests();
        iut.setDataFile(dataFile);
        iut.setSchemaModel(model);
        iut.findSurfaces();
        iut.validSurfaceOrientation();
    }

    @Test
    public void validSurfaceOrientation_exteriorIsCW()
            throws URISyntaxException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("Exterior boundary of surface with @gml:id='Surface-ExteriorCW' is not oriented CCW");
        URL url = this.getClass().getResource("/geom/Surface-ExteriorCW.xml");
        File dataFile = new File(url.toURI());
        SurfaceTests iut = new SurfaceTests();
        iut.setDataFile(dataFile);
        iut.setSchemaModel(model);
        iut.findSurfaces();
        iut.validSurfaceOrientation();
    }

    @Test
    public void validSurfaceBoundary_RectangleAndTriangle()
            throws URISyntaxException {
        URL url = this.getClass().getResource(
                "/geom/Surface-RectangleTriangle.xml");
        File dataFile = new File(url.toURI());
        SurfaceTests iut = new SurfaceTests();
        iut.setDataFile(dataFile);
        iut.setSchemaModel(model);
        iut.findSurfaces();
        iut.validSurfaceBoundary();
    }

    @Test
    public void validSurfaceBoundary_discontiguousPatches()
            throws URISyntaxException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("Exterior boundary of surface with @gml:id='id_5ccb6c80' is not simple");
        URL url = this.getClass().getResource(
                "/geom/Surface-DiscontiguousPatches.xml");
        File dataFile = new File(url.toURI());
        SurfaceTests iut = new SurfaceTests();
        iut.setDataFile(dataFile);
        iut.setSchemaModel(model);
        iut.findSurfaces();
        iut.validSurfaceBoundary();
    }

    @Test
    public void validSurfaceOrientation_geodesicCurveSegments()
            throws URISyntaxException {
        URL url = this.getClass().getResource(
                "/geom/Surface-PolygonPatch-ExteriorCurve.xml");
        File dataFile = new File(url.toURI());
        SurfaceTests iut = new SurfaceTests();
        iut.setDataFile(dataFile);
        iut.setSchemaModel(model);
        iut.findSurfaces();
        iut.validSurfaceOrientation();
    }

    @Test
    public void validSurfaceOrientation_exteriorCurveIsCW()
            throws URISyntaxException {
        thrown.expect(AssertionError.class);
        thrown.expectMessage("Exterior boundary of surface with @gml:id='ID_249' is not oriented CCW");
        URL url = this.getClass().getResource(
                "/geom/Surface-PolygonPatch-ExteriorCurveCW.xml");
        File dataFile = new File(url.toURI());
        SurfaceTests iut = new SurfaceTests();
        iut.setDataFile(dataFile);
        iut.setSchemaModel(model);
        iut.findSurfaces();
        iut.validSurfaceOrientation();
    }

    @Test
    public void validSurfaceBoundaryWith3Segments() throws URISyntaxException {
        URL url = this.getClass().getResource("/geom/Surface-Curve-ID_250.xml");
        File dataFile = new File(url.toURI());
        SurfaceTests iut = new SurfaceTests();
        iut.setDataFile(dataFile);
        iut.setSchemaModel(model);
        iut.findSurfaces();
        iut.validSurfaceBoundary();
    }

}
