package com.sap.cloud.lm.sl.persistence.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.sap.cloud.lm.sl.persistence.model.FileEntry;
import com.sap.cloud.lm.sl.persistence.processors.DefaultFileDownloadProcessor;

public class ProcessLogsService extends DatabaseFileService {
    private static final String TABLE_NAME = "process_log";

    private static ProcessLogsService instance;

    public static ProcessLogsService getInstance() {
        if (instance == null) {
            instance = new ProcessLogsService();
        }
        return instance;
    }

    public ProcessLogsService() {
        super(TABLE_NAME);
    }

    public List<String> getLogNames(String space, String processId) throws FileStorageException {
        List<String> result = new ArrayList<String>();
        List<FileEntry> logFiles = listFiles(space, processId);
        for (FileEntry logFile : logFiles) {
            result.add(logFile.getName());
        }
        return result;
    }

    public String getLogContent(String space, String processId, String logName) throws FileStorageException {
        final StringBuilder builder = new StringBuilder();
        String logId = findFileId(space, processId, logName);

        FileContentProcessor streamProcessor = new FileContentProcessor() {
            @Override
            public void processFileContent(InputStream is) throws IOException {
                builder.append(IOUtils.toString(is));
            }
        };
        processFileContent(new DefaultFileDownloadProcessor(space, logId, streamProcessor));
        return builder.toString();
    }

    public String findFileId(String space, String processId, String fileName) throws FileStorageException {
        List<FileEntry> listFiles = listFiles(space, processId);
        for (FileEntry fileEntry : listFiles) {
            if (fileEntry.getName().equals(fileName)) {
                return fileEntry.getId();
            }
        }
        return null;
    }

}
