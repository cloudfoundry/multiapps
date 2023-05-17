package org.cloudfoundry.multiapps.mta;

public class Messages {

    private Messages() {
    }

    // Exception messages:
    public static final String ERROR_SIZE_OF_FILE_EXCEEDS_CONFIGURED_MAX_SIZE_LIMIT = "The size \"{0}\" of mta file \"{1}\" exceeds the configured max size limit \"{2}\"";
    public static final String ERROR_RETRIEVING_MTA_MODULE_CONTENT = "Error retrieving content of MTA module \"{0}\"";
    public static final String ERROR_RETRIEVING_MTA_ARCHIVE_MANIFEST = "Error retrieving MTA archive manifest";
    public static final String REQUIRED_ELEMENT_IS_MISSING = "Required element \"{0}\" for object \"{1}\" is missing";
    public static final String VALUE_NOT_UNIQUE = "Value \"{0}\" for key \"{1}\" not unique for object \"{2}\"";
    public static final String UNABLE_TO_PARSE_VERSION_REQUIREMENT = "Unable to parse version requirement \"{0}\". Please provide a valid semantic version.";
    public static final String UNABLE_TO_PARSE_VERSION = "Unable to parse version \"{0}\"";
    public static final String UNSUPPORTED_RESOURCE_TYPE = "Unsupported resource type \"{0}\" for platform type \"{1}\"";
    public static final String UNSUPPORTED_MODULE_TYPE = "Unsupported module type \"{0}\" for platform type \"{1}\"";
    public static final String UNRESOLVED_MODULE_REQUIRED_DEPENDENCY = "Unresolved required dependency \"{0}\" for module \"{1}\"";
    public static final String UNRESOLVED_RESOURCE_REQUIRED_DEPENDENCY = "Unresolved required dependency \"{0}\" for resource \"{1}\"";
    public static final String UNRESOLVED_PROPERTIES = "Unresolved mandatory properties: {0}";
    public static final String UNRESOLVED_PARAMETERS = "Unresolved mandatory parameters: {0}";
    public static final String UNKNOWN_MODULE_IN_MTAEXT = "Unknown module \"{0}\" in extension descriptor \"{1}\"";
    public static final String UNKNOWN_REQUIRED_DEPENDENCY_IN_MTAEXT = "Unknown required dependency \"{0}\" for module \"{1}\" in extension descriptor \"{2}\"";
    public static final String UNKNOWN_PROVIDED_DEPENDENCY_IN_MTAEXT = "Unknown provided dependency \"{0}\" for module \"{1}\" in extension descriptor \"{2}\"";
    public static final String UNKNOWN_RESOURCE_IN_MTAEXT = "Unknown resource \"{0}\" in extension descriptor \"{1}\"";
    public static final String UNABLE_TO_RESOLVE = "Unable to resolve \"{0}\"";
    public static final String DETECTED_CIRCULAR_REFERENCE = "Circular reference detected in \"{0}\"";
    public static final String CANNOT_FIND_ARCHIVE_ENTRY = "Cannot find archive entry \"{0}\"";
    public static final String ERROR_RETRIEVING_ARCHIVE_ENTRY = "Error while retrieving archive entry \"{0}\"";
    public static final String NULL_VALUE_FOR_KEY = "Null value for key \"{0}\"";
    public static final String MISSING_REQUIRED_KEY = "Missing required key \"{0}\"";
    public static final String ERROR_EMPTY_DEPLOYMENT_DESCRIPTOR = "Empty deployment descriptor";
    public static final String ERROR_EMPTY_EXTENSION_DESCRIPTOR = "Empty extension descriptor";
    public static final String NULL_CONTENT = "Null content";
    public static final String INVALID_CONTENT_TYPE = "Invalid content type, expected \"{0}\" but got \"{1}\"";
    public static final String INVALID_TYPE_FOR_KEY = "Invalid type for key \"{0}\", expected \"{1}\" but got \"{2}\"";
    public static final String VALUE_TOO_LONG = "Invalid value for key \"{0}\", maximum length is {1}";
    public static final String INVALID_STRING_VALUE_FOR_KEY = "Invalid value for key \"{0}\", matching failed at \"{1}\"";
    public static final String CANNOT_BUILD_EXTENSION_DESCRIPTOR_CHAIN_BECAUSE_DESCRIPTORS_0_HAVE_AN_UNKNOWN_PARENT = "Cannot build extension descriptor chain, because the following descriptors have an unknown parent: {0}";
    public static final String MULTIPLE_EXTENSION_DESCRIPTORS_EXTEND_THE_PARENT_0 = "Multiple extension descriptors extend the parent \"{0}\". This is not allowed. The extension descriptors must form a chain.";
    public static final String EXTENSION_DESCRIPTORS_MUST_HAVE_THE_SAME_MAJOR_SCHEMA_VERSION_AS_THE_DEPLOYMENT_DESCRIPTOR_BUT_0_DO_NOT = "Extension descriptors must have the same major schema version as the deployment descriptor, but the following do not: {0}";
    public static final String CANNOT_MODIFY_ELEMENT = "Cannot modify {0} \"{1}\" in extension descriptor \"{2}\"";
    public static final String UNSUPPORTED_VERSION = "Version \"{0}\" is not supported";
    public static final String COULD_NOT_FIND_REQUIRED_PROPERTY = "Could not find required property \"{0}\"";
    public static final String MULTIPLE_HARD_MODULES_DETECTED = "Modules, \"{0}\" and \"{1}\", have hard circular dependencies";
    public static final String CIRCULAR_DEPLOYMENT_DEPENDENCIES_DETECTED = "Modules, \"{0}\" and \"{1}\", both depend on each others for deployment";
    public static final String CIRCULAR_RESOURCE_DEPENDENCIES_DETECTED = "Resources \"{0}\" and \"{1}\" both depend on each other for processing";
    public static final String RESOURCE_DOES_NOT_EXIST = "Resource \"{0}\" does not exist. Cannot create dependency of non-existent resource.";
    public static final String SELF_REQUIRED_RESOURCE = "Resource \"{0}\" is required by itself for processing";
    public static final String ILLEGAL_REFERENCES_DETECTED = "Module/Resource \"{0}\" does not contain a required dependency for \"{1}\", but contains references to its properties";
    public static final String COULD_NOT_FIND_ELEMENT_IN_SCHEMA = "Could not find element \"{0}\" in schema for {1}";
    public static final String MANDATORY_ELEMENT_HAS_NO_VALUE = "The {0} \"{1}\" is not optional and has no value.";
    public static final String INCOMPATIBLE_TYPES = "Type {0} is not compatible with type {1}";
    public static final String ENTITIES_FROM_CLASS_0_WITH_SCHEMA_VERSION_1_DO_NOT_SUPPORT_THIS_OPERATION_AT_LEAST_2_IS_REQUIRED = "Entities from class \"{0}\" with schema version \"{1}\" do not support this operation. At least \"{2}\" is required.";
    public static final String DIFFERENT_TYPE_PROVIDED_INSTEAD_OF_LIST = "Invalid type provided for \"{0}\": Expected a list of elements but another type was provided";
    public static final String INAPPROPRIATE_USE_OF_SELECTIVE_DEPLOY = "Cannot use selective deploy on resource \"{0}\" since it's dependent resources are not included for selective deploy.";

    // Audit log messages:

    // ERROR log messages:

    // WARN log messages:

    // INFO log messages:

    // DEBUG log messages:

    // TRACE log messages:

}
