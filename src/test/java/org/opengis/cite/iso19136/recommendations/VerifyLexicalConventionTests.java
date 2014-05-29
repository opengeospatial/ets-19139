package org.opengis.cite.iso19136.recommendations;

import org.junit.Assert;
import org.junit.Test;

/**
 * Verifies methods in the LexicalConventionTests class.
 */
public class VerifyLexicalConventionTests {

    public VerifyLexicalConventionTests() {
    }

    @Test
    public void isUpperCamelCase_allUpperCase() {
        String localName = "ABBA";
        LexicalConventionTests iut = new LexicalConventionTests();
        boolean result = iut.isUpperCamelCase(localName);
        Assert.assertFalse("Name has all upper case chars: " + localName,
                result);
    }

    @Test
    public void isUpperCamelCase_upperCaseWithTerminalDigit() {
        String localName = "ALPHA1";
        LexicalConventionTests iut = new LexicalConventionTests();
        boolean result = iut.isUpperCamelCase(localName);
        Assert.assertTrue("Should be considered in UCC form: " + localName,
                result);
    }

    @Test
    public void isUpperCamelCase_nameStartsWithLowerCase() {
        String localName = "aLPHA";
        LexicalConventionTests iut = new LexicalConventionTests();
        boolean result = iut.isUpperCamelCase(localName);
        Assert.assertFalse("Name has initial lower case char: " + localName,
                result);
    }

    @Test
    public void isUpperCamelCase_upperCaseWithDash() {
        String localName = "ALPHA-BETA";
        LexicalConventionTests iut = new LexicalConventionTests();
        boolean result = iut.isUpperCamelCase(localName);
        Assert.assertTrue("Should be considered in UCC form: " + localName,
                result);
    }

    @Test
    public void isUpperCamelCase_X() {
        String localName = "X";
        LexicalConventionTests iut = new LexicalConventionTests();
        boolean result = iut.isUpperCamelCase(localName);
        Assert.assertTrue("String is in UCC form: " + localName, result);
    }

    @Test
    public void isLowerCamelCase_x() {
        String localName = "x";
        LexicalConventionTests iut = new LexicalConventionTests();
        boolean result = iut.isLowerCamelCase(localName);
        Assert.assertTrue("String is in LCC form: " + localName, result);
    }

    @Test
    public void isLowerCamelCase_X1() {
        String localName = "X1";
        LexicalConventionTests iut = new LexicalConventionTests();
        boolean result = iut.isLowerCamelCase(localName);
        Assert.assertFalse("String is not in LCC form: " + localName, result);
    }
}
