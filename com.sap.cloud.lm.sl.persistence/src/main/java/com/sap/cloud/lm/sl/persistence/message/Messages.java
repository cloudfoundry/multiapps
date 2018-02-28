package com.sap.cloud.lm.sl.persistence.message;

/**
 * A collection of string constants used for exception and logging messages.
 */
public final class Messages {

    // Exception messages:
    public static final String NO_VIRUS_SCANNER_CONFIGURED = "Virus scanner not configured";
    public static final String FILE_UPLOAD_FAILED = "Upload of file \"{0}\" to \"{1}\" failed";
    public static final String FILE_NOT_FOUND = "File \"{0}\" not found";
    public static final String ERROR_SAVING_MESSAGE = "Error saving message for process id \"{0}\" and task id \"{1}\"";
    public static final String ERROR_UPDATING_MESSAGE = "Error updating message with id \"{0}\"";
    public static final String ERROR_DELETING_MESSAGES_BY_PROCESS_ID = "Error deleting messages for process id \"{0}\"";
    public static final String ERROR_DELETING_MESSAGES_BY_PROCESS_ID_AND_TASK_ID = "Error deleting messages for process id \"{0}\" and task id \"{1}\"";
    public static final String ERROR_GETTING_ALL_MESSAGES = "Error getting all messages";
    public static final String ERROR_GETTING_MESSAGE_BY_PROCESS_ID = "Error getting message with process with id \"{0}\"";
    public static final String ERROR_GETTING_MESSAGE_PROCESS_ID_TASK_ID = "Error getting message with process id \"{0}\" and task id \"{1}\"";
    public static final String ERROR_GETTING_MESSAGE_PROCESS_ID_STARTING_WITH = "Error getting message with process id \"{0}\" and task id starting with \"{1}\"";
    public static final String ERROR_GETTING_MESSAGE_PROCESS_ID_TASK_ID_TYPE = "Error getting messages with process id \"{0}\", task id \"{1}\" and type \"{2}\"";
    public static final String ERROR_GETTING_MESSAGE_PROCESS_ID_TYPE = "Error getting messages with process id \"{0}\" and type \"{1}\"";
    public static final String ERROR_CALCULATING_FILE_DIGEST = "Error calculating digest for file {0}: {1}";
    public static final String ERROR_FINDING_FILE_TO_UPLOAD = "Error finding file to upload with name {0}: {1}";
    public static final String ERROR_READING_FILE_CONTENT = "Error reading content of file {0}: {1}";
    public static final String ERROR_PROCESSING_CONTENT_OF_NON_EXISTING_FILE = "Could not process content of non existing file with name {0}";
    public static final String FILE_WITH_ID_DOES_NOT_EXIST = "File with id {0} and space {1} does not exist.";
    public static final String ERROR_DELETING_FILE_WITH_ID = "Error deleting file with id {0}";
    public static final String ERROR_DELETING_FILES_ATTRIBUTES = "Error deleting files attributes";

    // Audit log messages:

    // ERROR log messages:
    public static final String UPLOAD_STREAM_FAILED_TO_CLOSE = "Cannot close file upload stream";
    public static final String DELETING_LOCAL_FILE_BECAUSE_OF_INFECTION = "File \"{0}\" is infected and will be removed";

    // WARN log messages:
    public static final String COULD_NOT_CLOSE_RESULT_SET = "Could not close result set.";
    public static final String COULD_NOT_CLOSE_STATEMENT = "Could not close statement.";
    public static final String COULD_NOT_CLOSE_CONNECTION = "Could not close connection.";

    // INFO log messages:
    public static final String SCANNING_FILE = "Scanning file \"{0}\"...";
    public static final String SCANNING_FILE_SUCCESS = "File \"{0}\" is not infected";
    public static final String FAILED_TO_DELETE_FILE = "Failed to delete file {0}";
    public static final String DELETING_FILE_IN_TABLE = "File id:{0} in space: {1} will be deleted from {2}.";

    // DEBUG log messages:
    public static final String DELETING_FILE_WITH_PATH = "Deleting file with path {0}...";
    public static final String CREATING_LOGGER = "Creating logger \"{0}\"...";
    public static final String CREATING_APPENDER = "Creating appender for logger \"{0}\"...";
    public static final String REMOVING_ALL_LOGGERS_FOR_PROCESS = "Removing loggers {1} for process \"{0}\"...";
    public static final String REMOVING_ALL_APPENDERS_FROM_LOGGER = "Removing all appenders from logger \"{0}\"...";
    public static final String DELETING_FILE_ATTRIBUTES_COUNT = "Deleted file attributes count: {0}";

    // TRACE log messages:

}
