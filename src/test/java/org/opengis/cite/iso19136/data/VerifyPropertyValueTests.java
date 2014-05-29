package org.opengis.cite.iso19136.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.testng.ISuite;
import org.testng.ITestContext;

/**
 * Verifies the behavior of the PropertyValueTests class.
 */
public class VerifyPropertyValueTests {

	private static ITestContext testContext;
	private static ISuite suite;
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	public VerifyPropertyValueTests() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		testContext = mock(ITestContext.class);
		suite = mock(ISuite.class);
		when(testContext.getSuite()).thenReturn(suite);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.newDocumentBuilder();
	}

	@Test
	public void locationReference_notTextEntity() throws URISyntaxException,
			FileNotFoundException, MalformedURLException {
		thrown.expect(AssertionError.class);
		thrown.expectMessage("Response entity has unexpected media type");
		URL url = this.getClass().getResource("/SimpleFeature-1.xml");
		File dataFile = new File(url.toURI());
		PropertyValueTests iut = new PropertyValueTests();
		iut.setDataFile(dataFile);
		iut.validateLocationReference();
	}

	@Test
	public void locationReference_missingHref() throws URISyntaxException,
			FileNotFoundException, MalformedURLException {
		thrown.expect(AssertionError.class);
		thrown.expectMessage("Infoset item is missing or empty");
		URL url = this.getClass().getResource("/SimpleFeature-2.xml");
		File dataFile = new File(url.toURI());
		PropertyValueTests iut = new PropertyValueTests();
		iut.setDataFile(dataFile);
		iut.validateLocationReference();
	}

	@Test
	public void locationName_unknownHost() throws URISyntaxException,
			FileNotFoundException, MalformedURLException {
		thrown.expect(AssertionError.class);
		thrown.expectMessage("Failed to connect to URL");
		URL url = this.getClass().getResource("/SimpleFeature-2.xml");
		File dataFile = new File(url.toURI());
		PropertyValueTests iut = new PropertyValueTests();
		iut.setDataFile(dataFile);
		iut.validateLocationName();
	}

}
