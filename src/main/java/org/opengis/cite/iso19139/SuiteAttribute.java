package org.opengis.cite.iso19139;

import java.io.File;
import java.net.URI;
import java.util.Set;
import javax.swing.text.Document;

import javax.xml.validation.Schema;
import org.apache.xerces.xs.XSModel;

/**
 * An enumerated type defining ISuite attributes that may be set to constitute a
 * shared test fixture.
 */
@SuppressWarnings("rawtypes")
public enum SuiteAttribute {

    /**
     * A {@code Set<URI>} specifying the locations of XML Schema grammars.
     */
    SCHEMA_LOC_SET("schema-loc-set", Set.class),
    /**
     * An immutable XML Schema object representing a set of constraints defined
     * in some grammar-based schema language.
     */
    SCHEMA("schema", Schema.class),
    /**
     * A File containing GMD data.
     */
    IUT("gmd-data", File.class),
    /**
     * An absolute URI referring to a Schematron schema.
     */
    SCHEMATRON("schematron", URI.class),
    /**
     * Contains the XML Schema components comprising an application schema.
     */
    XSMODEL("xsmodel", XSModel.class),
    /**
     * Provides information about the types of geographic content in an
     * application schema.
     */
    // SCHEMA_INFO("schema-info", AppSchemaInfo.class),

    /**
     * A DOM Document representation of the test subject or metadata about it.
     */
    TEST_SUBJECT("testSubject", Document.class);
    private final Class attrType;
    private final String attrName;

    private SuiteAttribute(String attrName, Class attrType) {
        this.attrName = attrName;
        this.attrType = attrType;
    }

    public Class getType() {
        return attrType;
    }

    public String getName() {
        return attrName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(attrName);
        sb.append('(').append(attrType.getName()).append(')');
        return sb.toString();
    }
}
