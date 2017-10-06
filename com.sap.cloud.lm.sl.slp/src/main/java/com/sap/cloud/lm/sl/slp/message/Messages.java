package com.sap.cloud.lm.sl.slp.message;

/**
 * A collection of string constants used for exception and logging messages.
 */
public final class Messages {

    // Progress messages:
    public static final String EXECUTING_ACTIVITI_TASK = "Executing Activiti task \"{1}\" of process \"{0}\"";

    // SL Management Protocol:
    public static final String GET_SLMP_METADATA = "Get SL Management Protocol metadata";
    public static final String GET_SLMP_SERVICES = "List SL Management Protocol services";
    public static final String GET_SLMP_SERVICE = "Get SL Management Protocol service";
    public static final String GET_SLMP_SERVICE_PROCESS = "Get SL Management Protocol service process";
    public static final String DELETE_SLMP_SERVICE_PROCESS = "Delete SL Management Protocol service process";
    public static final String GET_SLMP_PROCESS = "Get SL Management Protocol process";
    public static final String GET_SLMP_SERVICE_PROCESSES = "List SL Management Protocol service processes";
    public static final String GET_SLMP_SERVICE_VERSIONS = "Get SL Management Protocol service versions";
    public static final String CREATE_SLMP_PROCESS = "Create SL Management Protocol process";
    public static final String UPLOAD_SLMP_SERVICE_FILE = "Upload SL Management Protocol file";
    public static final String UPLOAD_SLMP_SERVICE_FILES = "Upload SL Management Protocol files";
    public static final String DELETE_SLMP_SERVICE_FILES = "Delete SL Management Protocol files";
    public static final String DELETE_SLMP_SERVICE_FILE = "Delete SL Management Protocol file";

    // SL Process Protocol:
    public static final String GET_SLPP_METADATA = "Get SL Process Protocol metadata";
    public static final String GET_SLPP_TASKLIST = "Get SL Process Protocol tasklist";
    public static final String GET_SLPP_TASKLIST_TASK_ID = "Get SL Process Protocol task by id";
    public static final String GET_SLPP_TASKLIST_TASK_ID_ERROR = "Get SL Process Protocol error by task id";
    public static final String GET_SLPP_MONITOR = "Get SL Process Protocol monitor";
    public static final String GET_SLPP_MONITOR_TASK_ID = "Get SL Process Protocol monitor task by id";
    public static final String GET_SLPP_MONITOR_TASK_ID_ERROR = "Get SL Process Protocol monitor task error by id";
    public static final String GET_SLPP_ACTIONS = "Get SL Process Protocol actions";
    public static final String GET_SLPP_CONFIG = "Get SL Process Protocol configuration";
    public static final String POST_SLPP_CONFIG = "Post SL Process Protocol configuration";
    public static final String GET_SLPP_CONFIG_PARAMETER = "Get SL Process Protocol configuration parameter";
    public static final String GET_SLPP_ERROR = "Get SL Process Protocol error";
    public static final String GET_SLPP_ACTION = "Get SL Process Protocol action";
    public static final String GET_SLPP_LOGS = "Get SL Process Protocol logs";
    public static final String GET_SLPP_LOG = "Get SL Process Protocol log";
    public static final String GET_SLPP_LOG_CONTENT = "Get SL Process Protocol log content";
    public static final String EXECUTE_SLPP_ACTION = "Execute SL Process Protocol action";

    // Exception messages:
    public static final String FAILED_TO_ADD_TASK_EXTENSION_ELEMENTS = "Failed to add extension elements to SL Protocol Task element";
    public static final String SERVICE_METADATA_MUST_NOT_BE_NULL = "Service metadata to register may not be null";
    public static final String PARAMETER_TYPE_NOT_SUPPORTED = "Parameter type \"{0}\" is not supported";
    public static final String UNRECOGNIZED_PARAMETERS = "Parameters with identifiers {0} are not supported";
    public static final String REQUIRED_PARAMETERS_ARE_MISSING = "Required parameters {0} are missing and do not have default values";
    public static final String ERROR_EXECUTING_REST_API_CALL = "Error while executing REST API call: {0}";
    public static final String AMBIGUOUS_UNFINISHED_TASK_IDS = "Multiple steps identified as unfinished: {0}";
    public static final String PROCESS_WAS_ABORTED = "Process was aborted";
    public static final String ABORT_OPERATION_TIMED_OUT = "Abort operation timed out";
    public static final String PROCESS_STEP_NOT_REACHED_BEFORE_TIMEOUT = "Step \"{0}\" of process \"{1}\" not reached before timeout";
    public static final String PARAMETER_VALUE_IS_NOT_INTEGER = "The value \"{0}\" of parameter with id \"{1}\" is not an integer number";
    public static final String PARAMETER_VALUE_IS_NOT_BOOLEAN = "The value \"{0}\" of parameter with id \"{1}\" is not valid. Valid values are 'true' and 'false'";
    public static final String PARAMETER_CANNOT_BE_NULL = "'parameter' can't be 'null'";
    public static final String TUPLE_ENTRY_SHOULD_HAVE_ONLY_ONE_PARAMETER_INSIDE = "The 'tupleEntry' should have only one parameter inside";
    public static final String PARAMETER_VALUE_IS_INVALID = "The value \"{0}\" of parameter with id \"{1}\" is not valid";
    public static final String TABLE_VALUE_CANNOT_BE_NULL = "'tablevalue' can't be 'null'";
    public static final String VALUE_OF_TUPLE_ELEMENT_CANNOT_BE_NULL = "The 'value' element of a SLP Tuple element can't be 'null'";

    // Audit log messages:

    // ERROR log messages:
    public static final String UNEXPECTED_ERROR = "Unexpected error: {0}";
    public static final String SAVING_ERROR_MESSAGE_FAILED = "Saving error message failed";
    public static final String EXCEPTION_CAUGHT = "Exception caught";
    public static final String ACTIVITI_JOB_RETRY_FAILED = "Activiti job retry failed";

    // WARN log messages:
    public static final String RETRYING_PROCESS_ABORT = "Abort of process \"{0}\" failed due to an optimistic locking exception. Retrying abort...";
    public static final String ACTUAL_PROCESS_STATE_DIFFERS_FROM_CALCULATED_STATE = "Actual process state {0} is different from calculated process state {1}";
    public static final String COULD_NOT_PERSIST_LOGS_FILE = "Could not persist logs file: {0}";

    // INFO log messages:

    // DEBUG log messages:
    public static final String REMOVING_ALL_APPENDERS_FROM_LOGGER = "Removing all appenders from logger \"{0}\"...";
    public static final String CREATING_APPENDER = "Creating appender for logger \"{0}\"...";
    public static final String SET_SUCCESSFULLY = "Variable \"{0}\" set successfully";
    public static final String REMOVING_ALL_LOGGERS_FOR_PROCESS = "Removing loggers {1} for process \"{0}\"...";
    public static final String SETTING_DEFAULT_VALUE_FOR_PARAMETER = "Setting default value \"{0}\" for not specified parameter \"{1}\"";
    public static final String SETTING_VARIABLE = "Setting variable \"{0}\" to \"{1}\"...";
    public static final String STEP_FINISHED = "Step \"{0}\" finished";
    public static final String CREATING_LOGGER = "Creating logger \"{0}\"...";
    public static final String MAX_UPLOAD_SIZE_EXCEEDED = "Cannot upload file, size is bigger than the configured maximum upload size \"{0}\" bytes";

    // TRACE log messages:

}
