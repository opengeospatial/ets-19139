package org.opengis.cite.iso19139;

/**
 * An enumerated type defining all recognized test run arguments.
 */
public enum TestRunArg {

    /**
     * An absolute URI referring to the ISO19139 application schema (XML Schema)
     * resource.
     */
    XSD,
    /**
     * An absolute URI that refers to a representation of the test subject or
     * metadata about it.
     */
    IUT,
    
    SCH,
    
    ICS;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
