package org.cloudfoundry.multiapps.common;

public class Messages {

    private Messages() {
    }

    // Exception messages:
    public static final String CANNOT_CONVERT_JSON_STREAM_TO_MAP = "Error while converting JSON input stream to map: {0}";
    public static final String CANNOT_CONVERT_JSON_STRING_TO_MAP = "Error while converting JSON string \"{0}\" to map: {1}";
    public static final String CANNOT_CONVERT_JSON_STREAM_TO_LIST = "Error while converting JSON input stream to list: {0}";
    public static final String CANNOT_CONVERT_JSON_STRING_TO_LIST = "Error while converting JSON string \"{0}\" to list: {1}";
    public static final String CANNOT_PARSE_JSON_STRING_TO_TYPE = "Error while parsing JSON string \"{0}\" to type \"{1}\" ";
    public static final String ERROR_PARSING_YAML_STREAM = "Error while parsing YAML stream: {0}";
    public static final String ERROR_PARSING_YAML_STRING = "Error while parsing YAML string: %n%s%n%s";
    public static final String COULD_NOT_PARSE_BOOLEAN_FLAG = "Cannot parse \"{0}\" flag - expected a boolean format.";
    public static final String COULD_NOT_PARSE_MAP_FLAG = "Cannot parse \"{0}\" flag - expected a map format.";
    public static final String COULD_NOT_CONSTRUCT_YAML_CONVERTER_0_BECAUSE_OF_1 = "Could not construct YAML converter \"{0}\": {1}";
    public static final String INVALID_JSON_SERIALIZATION_STRATEGY_PROVIDED_0 = "Invalid JSON serialization strategy provided: \"{0}\"";
    // Audit log messages:

    // ERROR log messages:

    // WARN log messages:

    // INFO log messages:

    // DEBUG log messages:

    // TRACE log messages:

}
