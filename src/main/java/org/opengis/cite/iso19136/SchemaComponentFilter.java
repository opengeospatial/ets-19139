package org.opengis.cite.iso19136;

import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.xerces.xs.XSObject;

/**
 * Filters a collection of XML Schema components according to the criteria
 * realized by an implementing class. It is used when accessing an
 * {@link org.apache.xerces.xs.XSModel XSModel} instance using the XML Schema
 * API implemented by the Apache Xerces processor.
 * 
 * @see <a href="http://xerces.apache.org/xerces2-j/javadocs/xs/index.html">XML
 *      Schema API</a>
 */
public interface SchemaComponentFilter {

    /**
     * Filters the given collection of top-level schema components according to
     * specific acceptance criteria. The supplied Map object is unmodified.
     * 
     * @param components
     *            A (possibly immutable) collection of schema components, keyed
     *            by QName.
     * @return A new Map object containing those components from the given
     *         collection that satisfy the filtering criteria.
     */
    public Map<QName, XSObject> doFilter(Map<QName, XSObject> components);
}
