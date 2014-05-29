package org.opengis.cite.iso19136.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.opengis.cite.iso19136.SchemaComponentFilter;
import org.opengis.cite.iso19136.general.GML32;

/**
 * Provides utility methods for accessing components of XML Schema models.
 */
public class XMLSchemaModelUtils {

    /**
     * Returns a collection of concrete feature element declarations in the
     * application namespace(s). These elements may substitute for
     * gml:AbstractFeature.
     * 
     * @param model
     *            An XSModel object representing the application schema.
     * @return A List containing all (global) element declarations belonging to
     *         the substitution group having gml:AbstractFeature as its head.
     * 
     * @see "ISO 19136:2007, 21.3.3"
     */
    public static List<XSElementDeclaration> getFeatureDeclarations(
            XSModel model) {
        XSElementDeclaration gmlAbstractFeature = model.getElementDeclaration(
                GML32.ABSTRACT_FEATURE, GML32.NS_NAME);
        List<XSElementDeclaration> featureElems = getElementsByAffiliation(
                model, gmlAbstractFeature);
        removeGmlElementsFromList(featureElems);
        return featureElems;
    }

    /**
     * Returns a collection of concrete geometry element declarations in the
     * application namespace(s). These elements may substitute for
     * gml:AbstractGeometry.
     * 
     * @param model
     *            An XSModel object representing the application schema.
     * @return A List containing all (global) element declarations belonging to
     *         the substitution group having gml:AbstractGeometry as its head.
     * 
     * @see "ISO 19136:2007, 21.4.2.1"
     */
    public static List<XSElementDeclaration> getGeometryDeclarations(
            XSModel model) {
        XSElementDeclaration gmlAbstractGeometry = model.getElementDeclaration(
                GML32.ABSTRACT_GEOMETRY, GML32.NS_NAME);
        List<XSElementDeclaration> geomElems = getElementsByAffiliation(model,
                gmlAbstractGeometry);
        removeGmlElementsFromList(geomElems);
        return geomElems;
    }

    /**
     * Returns a list of geometry properties explicitly declared in an
     * application namespace. Such geometry properties are identified as
     * indicated below.
     * 
     * <ul>
     * 
     * <li>Elements declared to have a type that derives by restriction from
     * gml:GeometryPropertyType</li>
     * 
     * <li>Elements declared to have a type that derives by restriction from
     * gml:GeometryArrayPropertyType</li>
     * 
     * <li>Elements declared to use a predefined GML geometry property type (see
     * cl. 9.5, Table 4)</li>
     * 
     * </ul>
     * 
     * @param model
     *            An XSModel object incorporating application schema components.
     * @return A list of element declarations that correspond to explicit
     *         geometry properties. The list is empty if none are found.
     */
    public static List<XSElementDeclaration> getExplicitGeometryProperties(
            XSModel model) {
        List<XSElementDeclaration> geomProps = new ArrayList<XSElementDeclaration>();
        Set<String> nsNames = getApplicationNamespaces(model);
        for (String nsName : nsNames) {
            // Uses type derived by restriction from generic property types
            XSNamedMap typeDefs = model.getComponentsByNamespace(
                    XSConstants.TYPE_DEFINITION, nsName);
            for (int i = 0; i < typeDefs.getLength(); i++) {
                XSTypeDefinition typeDef = (XSTypeDefinition) typeDefs.item(i);
                if (typeDef.derivedFrom(GML32.NS_NAME, GML32.GEOM_PROP_TYPE,
                        XSConstants.DERIVATION_RESTRICTION)
                        || typeDef.derivedFrom(GML32.NS_NAME,
                                GML32.GEOM_ARRAY_PROP_TYPE,
                                XSConstants.DERIVATION_RESTRICTION)) {
                    geomProps.addAll(getGlobalElementsByType(model, typeDef));
                }
            }
            // Uses predefined GML geometry property type
            for (String geomPropType : GML32.GEOM_PROP_TYPE_SET) {
                XSTypeDefinition typeDef = model.getTypeDefinition(
                        geomPropType, GML32.NS_NAME);
                geomProps.addAll(getGlobalElementsByType(model, typeDef));
                geomProps.addAll(getLocalElementsByType(model, typeDef,
                        new FeatureTypeFilter()));
            }
        }
        return geomProps;
    }

    /**
     * Returns a list of implicitly defined GML properties whose expected value
     * is substitutable for the specified head element. Such properties have a
     * content model that is consistent with gml:AssociationRoleType but is not
     * explicitly derived from any predefined GML property type.
     * 
     * @param model
     *            An XSModel object incorporating application schema components.
     * @param headElem
     *            The head of the substitution group to which the property value
     *            belongs.
     * @return A list of element declarations that correspond to implicit GML
     *         properties having the specified value domain. The list is empty
     *         if none are found.
     */
    public static List<XSElementDeclaration> getImplicitProperties(
            XSModel model, XSElementDeclaration headElem) {

        List<XSComplexTypeDefinition> typeDefs = new ArrayList<XSComplexTypeDefinition>();
        // find complex types with element-only content type
        for (String nsName : getApplicationNamespaces(model)) {
            XSNamedMap types = model.getComponentsByNamespace(
                    XSTypeDefinition.COMPLEX_TYPE, nsName);
            for (int i = 0; i < types.getLength(); i++) {
                XSComplexTypeDefinition type = (XSComplexTypeDefinition) types
                        .item(i);
                if (type.getContentType() == XSComplexTypeDefinition.CONTENTTYPE_ELEMENT) {
                    typeDefs.add(type);
                }
            }
        }
        List<XSElementDeclaration> props = new ArrayList<XSElementDeclaration>();
        for (XSComplexTypeDefinition typeDef : typeDefs) {
            if (propertyHasValueDomain(typeDef, headElem)) {
                props.addAll(getGlobalElementsByType(model, typeDef));
                props.addAll(getLocalElementsByType(model, typeDef,
                        new GMLObjectTypeFilter()));
            }
        }
        return props;
    }

    /**
     * Determines whether or not a given complex property type has a specified
     * value domain.
     * 
     * @param typeDef
     *            A complex type definition that resembles a GML property type.
     * @param headElem
     *            The head element indicating the type of the property value(s).
     * @return true if the property value is substitutable for the head element;
     *         false otherwise.
     */
    public static boolean propertyHasValueDomain(
            XSComplexTypeDefinition typeDef, XSElementDeclaration headElem) {
        boolean hasValueDomain = false;
        XSTerm term = typeDef.getParticle().getTerm();
        // expect sequence compositor here
        if (term.getType() == XSConstants.MODEL_GROUP) {
            XSModelGroup group = (XSModelGroup) term;
            XSParticle particle = (XSParticle) group.getParticles().item(0);
            if (particle.getTerm().getType() == XSConstants.ELEMENT_DECLARATION) {
                XSElementDeclaration propValue = (XSElementDeclaration) particle
                        .getTerm();
                Set<XSElementDeclaration> headElems = new HashSet<XSElementDeclaration>();
                getHeadElements(headElems, propValue);
                if (headElems.contains(headElem)) {
                    hasValueDomain = true;
                }
            }
        }
        return hasValueDomain;
    }

    /**
     * Identifies the set of (head) elements for which the given element can
     * substitute.
     * 
     * @param headElems
     *            The set to be populated with head element declarations. The
     *            set will always contain the given element declaration, which
     *            could serve as a head element itself.
     * @param propValue
     *            A global element declaration that is presumably affiliated
     *            with some substitution group.
     */
    public static void getHeadElements(Set<XSElementDeclaration> headElems,
            XSElementDeclaration propValue) {
        if (propValue.getScope() != XSConstants.SCOPE_GLOBAL) {
            return;
        }
        headElems.add(propValue);
        if (null != propValue.getSubstitutionGroupAffiliation()) {
            getHeadElements(headElems,
                    propValue.getSubstitutionGroupAffiliation());
        }
    }

    /**
     * Returns a collection of topology element declarations in the application
     * namespace(s). These elements may substitute for gml:AbstractTopology.
     * 
     * @param model
     *            An XSModel object representing the application schema.
     * @return A List containing all (global) element declarations belonging to
     *         the substitution group having gml:AbstractTopology as its head.
     * 
     * @see "ISO 19136:2007, 21.5.2.1"
     */
    public static List<XSElementDeclaration> getTopologyDeclarations(
            XSModel model) {
        XSElementDeclaration gmlAbstractTopo = model.getElementDeclaration(
                GML32.ABSTRACT_TOPO, GML32.NS_NAME);
        List<XSElementDeclaration> topoElems = getElementsByAffiliation(model,
                gmlAbstractTopo);
        removeGmlElementsFromList(topoElems);
        return topoElems;
    }

    /**
     * Returns a collection of temporal element declarations in the application
     * namespace(s). These elements may substitute for gml:AbstractTimeObject.
     * 
     * @param model
     *            An XSModel object representing the application schema.
     * @return A List containing all (global) element declarations belonging to
     *         the substitution group having gml:AbstractTimeObject as its head.
     * 
     * @see "ISO 19136:2007, 21.6.2.1"
     */
    public static List<XSElementDeclaration> getTimeObjectDeclarations(
            XSModel model) {
        XSElementDeclaration gmlAbstractTime = model.getElementDeclaration(
                GML32.ABSTRACT_TIME, GML32.NS_NAME);
        List<XSElementDeclaration> timeElems = getElementsByAffiliation(model,
                gmlAbstractTime);
        removeGmlElementsFromList(timeElems);
        return timeElems;
    }

    /**
     * Returns a collection of CRS declarations in the application namespace(s).
     * These elements (which may substitute for gml:AbstractCRS) are typically
     * declared whenever a standard CRS type must be extended or restricted.
     * 
     * @param model
     *            An XSModel object representing the application schema.
     * @return A List containing all (global) element declarations belonging to
     *         the substitution group having gml:AbstractCRS as its head.
     * 
     * @see "ISO 19136:2007, 21.6.2.1"
     */
    public static List<XSElementDeclaration> getCRSDeclarations(XSModel model) {
        XSElementDeclaration gmlAbstractCRS = model.getElementDeclaration(
                GML32.ABSTRACT_CRS, GML32.NS_NAME);
        List<XSElementDeclaration> crsElems = getElementsByAffiliation(model,
                gmlAbstractCRS);
        removeGmlElementsFromList(crsElems);
        return crsElems;
    }

    /**
     * Returns a collection of coverage elements in the application
     * namespace(s). These elements may substitute for gml:AbstractCoverage.
     * 
     * Note that in the latest GML schema gml:AbstractContinuousCoverage, unlike
     * gml:AbstractDiscreteCoverage, cannot substitute for gml:AbstractCoverage.
     * 
     * @param model
     *            An XSModel object representing the application schema.
     * @return A List containing all (global) element declarations belonging to
     *         the substitution group having gml:AbstractCoverage as its head.
     * 
     * @see "ISO 19136:2007, 21.8.3"
     */
    public static List<XSElementDeclaration> getCoverageDeclarations(
            XSModel model) {
        XSElementDeclaration gmlAbstractCoverage = model.getElementDeclaration(
                GML32.ABSTRACT_COVERAGE, GML32.NS_NAME);
        List<XSElementDeclaration> coverageElems = getElementsByAffiliation(
                model, gmlAbstractCoverage);
        removeGmlElementsFromList(coverageElems);
        return coverageElems;
    }

    /**
     * Returns a collection of observation elements declared in the application
     * namespace(s). These elements may substitute for gml:Observation.
     * 
     * @param model
     *            An XSModel object representing the application schema.
     * @return A List containing all (global) element declarations belonging to
     *         the substitution group having gml:Observation as its head.
     * 
     * @see "ISO 19136:2007, 21.9.3"
     */
    public static List<XSElementDeclaration> getObservationDeclarations(
            XSModel model) {
        XSElementDeclaration gmlObservation = model.getElementDeclaration(
                GML32.OBSERVATION, GML32.NS_NAME);
        List<XSElementDeclaration> observationElems = getElementsByAffiliation(
                model, gmlObservation);
        removeGmlElementsFromList(observationElems);
        return observationElems;
    }

    /**
     * Returns a collection of definition elements declared in the application
     * namespace(s), including dictionaries. These elements may substitute for
     * gml:Definition.
     * 
     * @param model
     *            An XSModel object representing the application schema.
     * @return A List containing all (global) element declarations belonging to
     *         the substitution group having gml:Definition as its head.
     * 
     * @see "ISO 19136:2007, 21.10.3-4"
     */
    public static List<XSElementDeclaration> getDefinitionDeclarations(
            XSModel model) {
        XSElementDeclaration gmlDefinition = model.getElementDeclaration(
                GML32.DEFINITION, GML32.NS_NAME);
        List<XSElementDeclaration> defnElems = getElementsByAffiliation(model,
                gmlDefinition);
        removeGmlElementsFromList(defnElems);
        return defnElems;
    }

    /**
     * Returns a collection of element declarations that may substitute
     * (directly or indirectly) for the specified element.
     * 
     * @param model
     *            An XSModel object incorporating application schema components.
     * @param head
     *            The head of the substitution group.
     * @return A List containing all element declarations having the designated
     *         substitution group affiliation. The list may be empty; it will
     *         certainly be empty if the head element is null.
     */
    public static List<XSElementDeclaration> getElementsByAffiliation(
            XSModel model, XSElementDeclaration head) {
        List<XSElementDeclaration> elemDecls = new ArrayList<XSElementDeclaration>();
        if (null == head) {
            return elemDecls;
        }
        XSObjectList subGroupMembers = model.getSubstitutionGroup(head);
        for (Object xsObject : subGroupMembers) {
            XSElementDeclaration subGroupMember = (XSElementDeclaration) xsObject;
            elemDecls.add(subGroupMember);
        }
        return elemDecls;
    }

    /**
     * Returns a list of globally-scoped (top-level) element declarations whose
     * type definition either matches the given type or derives from it by
     * extension.
     * 
     * @param model
     *            An XSModel object incorporating application schema components.
     * @param typeDef
     *            A simple or complex type defining the content model of the
     *            desired elements. It may be an ancestor type.
     * @return A List containing globally-scoped element declarations in an
     *         application namespace. The list is empty if no matching
     *         declarations are found.
     */
    public static List<XSElementDeclaration> getGlobalElementsByType(
            XSModel model, XSTypeDefinition typeDef) {
        List<XSElementDeclaration> elems = new ArrayList<XSElementDeclaration>();
        Set<String> nsNames = getApplicationNamespaces(model);
        for (String nsName : nsNames) {
            // Globally scoped element declarations
            XSNamedMap elemDecls = model.getComponentsByNamespace(
                    XSConstants.ELEMENT_DECLARATION, nsName);
            for (int i = 0; i < elemDecls.getLength(); i++) {
                XSElementDeclaration elemDecl = (XSElementDeclaration) elemDecls
                        .item(i);
                if (elemDecl.getTypeDefinition().equals(typeDef)
                        || elemDecl.getTypeDefinition().derivedFromType(
                                typeDef, XSConstants.DERIVATION_EXTENSION)) {
                    elems.add(elemDecl);
                }
            }
        }
        return elems;
    }

    /**
     * Returns a list of locally-scoped element declarations whose type
     * definition either matches the given type or derives from it by extension.
     * The scope of these declarations is some complex type definition that may
     * be accessed using the
     * {@link XSElementDeclaration#getEnclosingCTDefinition()
     * getEnclosingCTDefinition} method. The type definitions to be examined may
     * be restricted using a component filter.
     * 
     * @param model
     *            An XSModel object incorporating application schema components.
     * @param typeDef
     *            A simple or complex type defining the content model of the
     *            desired elements. It may be an ancestor type.
     * @param typeFilter
     *            A schema component filter that determines which type
     *            definitions will be examined; if {@code null}, all complex
     *            types defined in application namespaces will be inspected.
     * @return A List containing locally-scoped element declarations in an
     *         application namespace. The list is empty if no matching
     *         declarations are found.
     */
    @SuppressWarnings("unchecked")
    public static List<XSElementDeclaration> getLocalElementsByType(
            XSModel model, XSTypeDefinition typeDef,
            SchemaComponentFilter typeFilter) {
        List<XSElementDeclaration> elemsByType = new ArrayList<XSElementDeclaration>();
        Set<String> nsNames = getApplicationNamespaces(model);
        for (String nsName : nsNames) {
            Map<QName, XSObject> complexTypes = new HashMap<QName, XSObject>(
                    model.getComponentsByNamespace(
                            XSTypeDefinition.COMPLEX_TYPE, nsName));
            if (null != typeFilter) {
                complexTypes = typeFilter.doFilter(complexTypes);
            }
            for (XSObject xsObj : complexTypes.values()) {
                XSComplexTypeDefinition type = (XSComplexTypeDefinition) xsObj;
                List<XSElementDeclaration> containedElems = getAllElementsInParticle(type
                        .getParticle());
                for (XSElementDeclaration elemDecl : containedElems) {
                    if (elemDecl.getTypeDefinition().equals(typeDef)
                            || elemDecl.getTypeDefinition().derivedFromType(
                                    typeDef, XSConstants.DERIVATION_EXTENSION)) {
                        elemsByType.add(elemDecl);
                    }
                }
            }
        }
        return elemsByType;
    }

    /**
     * Returns all element declarations contained in the given particle schema
     * component. The {term} property of a particle is one of a model group, a
     * wildcard, or an element declaration. Note that a model group (denoted by
     * the "all", "choice", or "sequence" compositor) can contain other model
     * groups.
     * 
     * @param particle
     *            An XSParticle representing a particle component.
     * @return A List of element declarations contained directly or indirectly
     *         in the particle; the components are listed in document order.
     */
    @SuppressWarnings("rawtypes")
    public static List<XSElementDeclaration> getAllElementsInParticle(
            XSParticle particle) {
        List<XSElementDeclaration> elems = new ArrayList<XSElementDeclaration>();
        if (null != particle) { // mixed or element-only content model
            switch (particle.getTerm().getType()) {
            case XSConstants.ELEMENT_DECLARATION:
                XSElementDeclaration elemDecl = (XSElementDeclaration) particle
                        .getTerm();
                elems.add(elemDecl);
                break;
            case XSConstants.MODEL_GROUP:
                XSModelGroup group = (XSModelGroup) particle.getTerm();
                for (Iterator itr = group.getParticles().iterator(); itr
                        .hasNext();) {
                    XSParticle xsParticle = (XSParticle) itr.next();
                    elems.addAll(getAllElementsInParticle(xsParticle));
                }
                break;
            default:
                // ignore wildcard term
            }
        }
        return elems;
    }

    /**
     * Returns all constituent particles corresponding to element declarations.
     * The {term} property of a particle is one of a model group, a wildcard, or
     * an element declaration.
     * 
     * @param particle
     *            A particle schema component to inspect.
     * @return A List containing particles that have an element declaration as
     *         its term.
     */
    @SuppressWarnings("unchecked")
    public static List<XSParticle> getAllElementParticles(XSParticle particle) {
        List<XSParticle> particles = new ArrayList<XSParticle>();
        XSTerm xsTerm = particle.getTerm();
        switch (xsTerm.getType()) {
        case XSConstants.ELEMENT_DECLARATION:
            particles.add(particle);
            break;
        case XSConstants.MODEL_GROUP:
            XSModelGroup group = (XSModelGroup) xsTerm;
            for (Iterator<XSParticle> itr = group.getParticles().iterator(); itr
                    .hasNext();) {
                XSParticle xsParticle = itr.next();
                particles.addAll(getAllElementParticles(xsParticle));
            }
            break;
        default:
            // ignore wildcard term
        }
        return particles;
    }

    /**
     * Returns a collection of complex type definitions referenced by global
     * elements declared in all application namespaces. The resulting set may
     * include anonymous types.
     * 
     * @param model
     *            An XSModel object.
     * @return A Set containing XSComplexTypeDefinition components.
     */
    public static Set<XSComplexTypeDefinition> getReferencedComplexTypeDefinitions(
            XSModel model) {
        Set<XSComplexTypeDefinition> typeDefs = new HashSet<XSComplexTypeDefinition>();
        Set<String> appNamespaces = getApplicationNamespaces(model);
        for (String nsName : appNamespaces) {
            XSNamedMap elemDecls = model.getComponentsByNamespace(
                    XSConstants.ELEMENT_DECLARATION, nsName);
            for (int i = 0; i < elemDecls.getLength(); i++) {
                XSElementDeclaration elemDecl = (XSElementDeclaration) elemDecls
                        .item(i);
                XSTypeDefinition typeDef = elemDecl.getTypeDefinition();
                if (typeDef.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE) {
                    typeDefs.add((XSComplexTypeDefinition) typeDef);
                }
            }
        }
        return typeDefs;
    }

    /**
     * Returns a collection of top-level complex types defined in all
     * application namespaces.
     * 
     * @param model
     *            An XSModel object.
     * @return A Set containing XSComplexTypeDefinition components.
     */
    @SuppressWarnings("unchecked")
    public static Set<XSComplexTypeDefinition> getGlobalComplexTypeDefinitions(
            XSModel model) {
        Set<XSComplexTypeDefinition> typeDefs = new HashSet<XSComplexTypeDefinition>();
        for (String nsName : getApplicationNamespaces(model)) {
            XSNamedMap components = model.getComponentsByNamespace(
                    XSTypeDefinition.COMPLEX_TYPE, nsName);
            Collection<XSComplexTypeDefinition> values = components.values();
            typeDefs.addAll(values);
        }
        return typeDefs;
    }

    /**
     * Returns a list of application-specific namespace names declared in a
     * schema. These are absolute URIs with non-standard authority components or
     * scheme names.
     * 
     * @param model
     *            An XSModel object incorporating application schema components.
     * @return A Set of Strings denoting the target namespace(s) used by the
     *         application schema.
     */
    public static Set<String> getApplicationNamespaces(XSModel model) {
        StringList nsNames = model.getNamespaces();
        Set<String> appNamespaces = new HashSet<String>();
        for (int i = 0; i < nsNames.getLength(); i++) {
            String nsName = nsNames.item(i);
            if (nsName.startsWith("http://www.opengis.net/gml")
                    || nsName.startsWith("http://www.w3.org/")
                    || nsName.startsWith("http://www.isotc211.org/")) {
                continue;
            }
            appNamespaces.add(nsName);
        }
        return appNamespaces;
    }

    /**
     * Returns the set of type definitions that are derived from the given base
     * type using the specified method.
     * 
     * @param model
     *            An XSModel object containing schema components.
     * @param baseType
     *            The base (ancestor) type.
     * @param derivationMethod
     *            A bit combination representing a subset of {
     *            DERIVATION_RESTRICTION, DERIVATION_EXTENSION,
     *            DERIVATION_UNION, DERIVATION_LIST }.
     * @return A Set containing zero or more simple or complex type definitions
     *         (XSTypeDefinition objects).
     */
    public static Set<XSTypeDefinition> getDerivedTypeDefinitions(
            XSModel model, XSTypeDefinition baseType, short derivationMethod) {
        Set<XSTypeDefinition> typeDefs = new HashSet<XSTypeDefinition>();
        for (String nsName : getApplicationNamespaces(model)) {
            XSNamedMap components = model.getComponentsByNamespace(
                    XSConstants.TYPE_DEFINITION, nsName);
            for (int i = 0; i < components.getLength(); i++) {
                XSTypeDefinition typeDef = (XSTypeDefinition) components
                        .item(i);
                if (typeDef.derivedFromType(baseType, derivationMethod)) {
                    typeDefs.add(typeDef);
                }
            }
        }
        return typeDefs;
    }

    /**
     * Removes elements declared in the standard GML namespace (
     * {@value org.opengis.cite.iso19136.general.GML32#NS_NAME}), leaving only
     * those residing in an application namespace.
     * 
     * @param elemDecls
     *            A List of element declarations.
     */
    static void removeGmlElementsFromList(List<XSElementDeclaration> elemDecls) {
        Iterator<XSElementDeclaration> itr = elemDecls.iterator();
        while (itr.hasNext()) {
            XSElementDeclaration decl = itr.next();
            if (decl.getNamespace().equals(GML32.NS_NAME)) {
                itr.remove();
            }
        }
    }
}
