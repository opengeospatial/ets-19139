package org.opengis.cite.iso19139;

/**
 * Defines keys used to access localized messages for assertion errors. The
 * messages are stored in Properties files that are encoded in ISO-8859-1
 * (Latin-1). For some languages the {@code native2ascii} tool must be used to
 * process the files and produce escaped Unicode characters.
 */
public class ErrorMessageKeys {

    public static final String NOT_SCHEMA_VALID = "NotSchemaValid";
    public static final String EMPTY_STRING = "EmptyString";
    public static final String XPATH_RESULT = "XPathResult";
    public static final String NAMESPACE_NAME = "NamespaceName";
    public static final String LOCAL_NAME = "LocalName";
    public static final String XML_ERROR = "XMLError";
    public static final String XPATH_ERROR = "XPathError";
    public static final String MISSING_INFOSET_ITEM = "MissingInfosetItem";
    public static final String UNEXPECTED_STATUS = "UnexpectedStatus";
    public static final String UNEXPECTED_MEDIA_TYPE = "UnexpectedMediaType";
    public static final String MISSING_ENTITY = "MissingEntity";
    public static final String SCHEMA_ERROR = "SchemaError";
    public static final String XSD_INVALID = "XMLSchemaNotValid";
    public static final String RELATIVE_NS = "RelativeNamespace";
    public static final String UNEXPECTED_NS = "UnexpectedNamespace";
    public static final String IMPORT_FULL_GML = "ImportFullGML";
    public static final String NO_GML_DEFS = "GMLDefsNotFound";
    public static final String NOT_UCC_NAME = "NotUCCName";
    public static final String NOT_LCC_NAME = "NotLCCName";
    public static final String NOT_ABSTRACT_NAME = "NotAbstractName";
    public static final String MISSING_TYPE_SUFFIX = "MissingTypeSuffix";
    public static final String PROP_IS_GML_OBJ = "PropertyIsGMLObject";
    public static final String PROP_HAS_ID_ATTR = "PropertyHasIDAttr";
    public static final String ATTRIB_REQUIRED = "AttribDeclRequired";
    public static final String ATTRIB_PROHIBITED = "AttribDeclProhibited";
    public static final String ELEM_DECL_REQUIRED = "ElemDeclRequired";
    public static final String DISALLOWED_SUBSTITUTION = "DisallowedSubstitution";
    public static final String SEQ_EXPECTED = "SequenceExpected";
    public static final String MODEL_GRP_EXPECTED = "ModelGroupExpected";
    public static final String TOO_MANY_PARTS = "TooManyParticles";
    public static final String SUBSTITUTION_ERROR = "SubstitutionError";
    public static final String NO_USERDEF_GEOM = "NoUserDefinedGeomTypes";
    public static final String EXT_BOUNDARY_ORIENT = "ExteriorBoundaryOrientation";
    public static final String INT_BOUNDARY_ORIENT = "InteriorBoundaryOrientation";
    public static final String META_PROP_VALUE = "MetadataPropertyValue";
}
