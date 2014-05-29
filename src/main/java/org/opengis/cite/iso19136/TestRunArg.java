package org.opengis.cite.iso19136;

/**
 * An enumerated type defining all recognized test run arguments.
 */
public enum TestRunArg {

    /**
     * An absolute URI that refers to a representation of the test subject or
     * metadata about it.
     */
    IUT,
    /**
     * An absolute URI referring to a GML application schema (XML Schema)
     * resource.
     */
    XSD,
    /**
     * An absolute URI referring to a Schematron schema (ISO 19757-3) that
     * defines supplementary data constraints.
     */
    SCH,
    /**
     * An absolute URI referring to a GML data resource.
     */
    GML;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
