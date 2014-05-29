package org.opengis.cite.iso19136.data;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.testng.ISuite;
import org.testng.ITestContext;

/**
 * Verifies the behavior of the SchematronTests test class.
 */
public class VerifySchematronTests {

	private static ITestContext testContext;
	private static ISuite suite;
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	public VerifySchematronTests() {
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
	public void hasSchematronReference() throws URISyntaxException {
		URL url = this.getClass().getResource("/Alpha-1.xml");
		File file = new File(url.toURI());
		SchematronTests iut = new SchematronTests();
		Map<String, String> piData = iut.getXmlModelPIData(file);
		Assert.assertNotNull(piData);
		Assert.assertEquals("Unexpected href value.",
				"http://example.org/constraints.sch", piData.get("href"));
	}

	@Test
	public void noSchematronReference() throws URISyntaxException {
		String fileName = "/Gamma.xml";
		URL url = this.getClass().getResource(fileName);
		File file = new File(url.toURI());
		SchematronTests iut = new SchematronTests();
		Map<String, String> piData = iut.getXmlModelPIData(file);
		Assert.assertNull("Expected null PI data for " + fileName, piData);
	}

	@Test
	public void checkForDeprecatedGMLElements_envelopeWithPosChildren()
			throws URISyntaxException {
		thrown.expect(AssertionError.class);
		String fileName = "/SimpleFeature-1.xml";
		URL url = this.getClass().getResource(fileName);
		File dataFile = new File(url.toURI());
		SchematronTests iut = new SchematronTests();
		iut.setDataFile(dataFile);
		iut.checkForDeprecatedGMLElements();
	}
}
