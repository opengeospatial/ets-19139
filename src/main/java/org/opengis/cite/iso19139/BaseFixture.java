package org.opengis.cite.iso19139;

import java.util.Collections;
import java.util.Map;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tanvishah
 */
public class BaseFixture {

    /**
     * An immutable Map containing a KML namespace binding where the prefix is
     * "kml"
     */
    protected static final Map<String, String> NS_MAP = Collections.singletonMap(Namespaces.GMD, "gmd");
    /**
     * A DOM Document representing the main KML document
     */
    protected Document testSubject;

    /**
     * Obtains the test subject from the ISuite context. The suite attribute
     * {@link org.opengis.cite.iso19139.SuiteAttribute#TEST_SUBJECT} should
     * evaluate to a DOM Document node.
     *
     * @param testContext The test (group) context.
     */
    @BeforeClass(alwaysRun = true)
    public void obtainTestSubject(ITestContext testContext) {
        Object obj = testContext.getSuite().getAttribute(
                SuiteAttribute.TEST_SUBJECT.getName());
        if ((null != obj) && Document.class.isAssignableFrom(obj.getClass())) {
            this.testSubject = Document.class.cast(obj);
        }
    }

    /**
     * Sets the test subject. This method is intended to facilitate unit
     * testing.
     *
     * @param testSubject A Document node representing the test subject or
     * metadata about it.
     */
    public void setTestSubject(Document testSubject) {
        this.testSubject = testSubject;
    }

    /**
     * Verifies the string is empty.
     */
    @Test(description = "Implements ATC 1-1")
    public void isEmpty() {
        String str = "  foo   ";
        Assert.assertTrue(str.isEmpty(),
                ErrorMessage.get(ErrorMessageKeys.EMPTY_STRING));
    }
}
