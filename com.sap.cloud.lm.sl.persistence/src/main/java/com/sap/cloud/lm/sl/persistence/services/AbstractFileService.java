package com.sap.cloud.lm.sl.persistence.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.common.util.DigestHelper;
import com.sap.cloud.lm.sl.persistence.dialects.DatabaseDialect;
import com.sap.cloud.lm.sl.persistence.dialects.DefaultDatabaseDialect;
import com.sap.cloud.lm.sl.persistence.message.Messages;
import com.sap.cloud.lm.sl.persistence.model.FileEntry;
import com.sap.cloud.lm.sl.persistence.model.FileUpload;
import com.sap.cloud.lm.sl.persistence.processors.DefaultFileDownloadProcessor;
import com.sap.cloud.lm.sl.persistence.processors.DefaultFileUploadProcessor;
import com.sap.cloud.lm.sl.persistence.processors.FileDownloadProcessor;
import com.sap.cloud.lm.sl.persistence.processors.FileUploadProcessor;
import com.sap.cloud.lm.sl.persistence.services.SqlExecutor.StatementExecutor;
import com.sap.cloud.lm.sl.persistence.util.JdbcUtil;

import virusscanner.VirusScanner;
import virusscanner.VirusScannerException;

public abstract class AbstractFileService {

    private static final String DEFAULT_DATASOURCE_JNDI_NAME = "java:comp/env/jdbc/DefaultDB";

    private static final String SELECT_FILES_BY_NAMESPACE_AND_SPACE = "SELECT FILE_ID, SPACE, DIGEST, DIGEST_ALGORITHM, MODIFIED, FILE_NAME, NAMESPACE, FILE_SIZE FROM {0} WHERE NAMESPACE=? AND SPACE=?";
    private static final String SELECT_FILES_BY_NAMESPACE = "SELECT FILE_ID, SPACE, DIGEST, DIGEST_ALGORITHM, MODIFIED, FILE_NAME, NAMESPACE, FILE_SIZE FROM {0} WHERE NAMESPACE=?";
    private static final String SELECT_FILES_BY_SPACE = "SELECT FILE_ID, SPACE, DIGEST, DIGEST_ALGORITHM, MODIFIED, FILE_NAME, NAMESPACE, FILE_SIZE FROM {0} WHERE SPACE=?";
    private static final String SELECT_FILE_BY_ID = "SELECT FILE_ID, SPACE, DIGEST, DIGEST_ALGORITHM, MODIFIED, FILE_NAME, NAMESPACE, FILE_SIZE FROM {0} WHERE FILE_ID=? AND SPACE=?";
    private static final String DELETE_FILE_BY_ID = "DELETE FROM {0} WHERE FILE_ID=? AND SPACE=?";
    private static final String DELETE_FILES_BY_NAMESPACE = "DELETE FROM {0} WHERE NAMESPACE=? AND SPACE=?";
    private static final String INSERT_FILE = "INSERT INTO {0} (FILE_ID, SPACE, FILE_NAME, NAMESPACE, FILE_SIZE, DIGEST, DIGEST_ALGORITHM, MODIFIED) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    protected static final String DEFAULT_TABLE_NAME = "LM_SL_PERSISTENCE_FILE";

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileService.class);

    protected final String tableName;
    protected DataSource dataSource;
    protected DatabaseDialect databaseDialect;
    private VirusScanner virusScanner;

    public AbstractFileService(String tableName) {
        this.tableName = tableName;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDatabaseDialect(DatabaseDialect databaseDialect) {
        this.databaseDialect = databaseDialect;
    }

    public void setVirusScanner(VirusScanner virusScanner) {
        this.virusScanner = virusScanner;
    }

    public void init(DataSource dataSource) {
        init(dataSource, new DefaultDatabaseDialect());
    }

    public void init(DataSource dataSource, DatabaseDialect databaseDialect) {
        this.dataSource = dataSource;
        this.databaseDialect = databaseDialect;
    }

    @Deprecated
    public FileEntry addFile(String space, String namespace, String name, InputStream is, BigInteger maxUploadSize)
        throws FileStorageException {
        return addFile(space, namespace, name, is, maxUploadSize, false);
    }

    /**
     * Uploads a new file
     * 
     * @param namespace namespace to upload the file to
     * @param name name for the uploaded file
     * @param is input stream to read the content from
     * @param scanUpload
     * @return a FileEntry object representing the file upload
     * @throws FileStorageException
     */
    @Deprecated
    public FileEntry addFile(String space, String namespace, String name, InputStream is, BigInteger maxUploadSize, boolean scanUpload)
        throws FileStorageException {
        return addFile(space, namespace, name, new DefaultFileUploadProcessor(scanUpload), is);
    }

    public FileEntry addFile(String space, String name,
        FileUploadProcessor<? extends OutputStream, ? extends OutputStream> fileUploadProcessor, InputStream is)
        throws FileStorageException {
        return addFile(space, null, name, fileUploadProcessor, is);
    }

    /**
     * Uploads a new file.
     *
     * @param space
     * @param namespace namescape where the file will be uploaded
     * @param name name of the uploaded file
     * @param fileProcessor file processor
     * @param is input stream to read the content from
     * @return an object representing the file upload
     * @throws FileStorageException
     */
    public FileEntry addFile(String space, String namespace, String name,
        FileUploadProcessor<? extends OutputStream, ? extends OutputStream> fileUploadProcessor, InputStream is)
        throws FileStorageException {
        // Stream the file to a temp location and get the size and MD5 digest
        // as an alternative we can pass the original stream to the database,
        // and decorate the blob stream to calculate digest and size, but this will still require
        // two roundtrips to the database (insert of the content and then update with the digest and
        // size), which is probably inefficient
        FileUpload localFile = null;
        try {

            localFile = FileUploader.uploadFile(is, fileUploadProcessor);

            return uploadFileToDatabase(space, namespace, name, fileUploadProcessor, localFile);
        } finally {
            IOUtils.closeQuietly(is);
            if (localFile != null) {
                FileUploader.removeFile(localFile);
            }
        }
    }

    public FileEntry addFile(String space, String namespace, String name,
        FileUploadProcessor<? extends OutputStream, ? extends OutputStream> fileUploadProcessor, File existingFile)
        throws VirusScannerException, FileStorageException {
        try {
            FileUpload fileUpload = createFileUpload(existingFile);

            return uploadFileToDatabase(space, namespace, name, fileUploadProcessor, fileUpload);
        } catch (NoSuchAlgorithmException e) {
            throw new SLException(MessageFormat.format(Messages.ERROR_CALCULATING_FILE_DIGEST, existingFile.getName()), e);
        } catch (FileNotFoundException e) {
            throw new FileStorageException(MessageFormat.format(Messages.ERROR_FINDING_FILE_TO_UPLOAD, existingFile.getName()), e);
        } catch (IOException e) {
            throw new FileStorageException(MessageFormat.format(Messages.ERROR_READING_FILE_CONTENT, existingFile.getName()), e);
        }
    }

    private FileUpload createFileUpload(File existingFile) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        return new FileUpload(existingFile, BigInteger.valueOf(existingFile.length()),
            DigestHelper.computeFileChecksum(existingFile.toPath(), FileUploader.DIGEST_METHOD), FileUploader.DIGEST_METHOD);
    }

    private FileEntry uploadFileToDatabase(String space, String namespace, String name,
        FileUploadProcessor<? extends OutputStream, ? extends OutputStream> fileUploadProcessor, FileUpload fileUpload)
        throws VirusScannerException, FileStorageException {

        if (fileUploadProcessor.shouldScanFile()) {
            scanUpload(fileUpload);
        }

        FileEntry fileEntry = createFileEntry(space, namespace, name, fileUpload);

        insertFileAttributes(fileEntry);
        InputStream localStream = null;
        boolean uploadedSuccesfully = false;

        try {
            localStream = fileUpload.getInputStream();
            uploadedSuccesfully = uploadFileContent(localStream, fileEntry);
        } finally {
            IOUtils.closeQuietly(localStream);
        }

        if (!uploadedSuccesfully) {
            throw new FileStorageException(MessageFormat.format(Messages.FILE_UPLOAD_FAILED, name, namespace));
        }

        return fileEntry;
    }

    private void scanUpload(FileUpload file) throws VirusScannerException, FileStorageException {
        if (virusScanner != null)
            try {
                LOGGER.info(MessageFormat.format(Messages.SCANNING_FILE, file.getFile()));
                virusScanner.scanFile(file.getFile());
                LOGGER.info(MessageFormat.format(Messages.SCANNING_FILE_SUCCESS, file.getFile()));
            } catch (VirusScannerException e) {
                LOGGER.error(MessageFormat.format(Messages.DELETING_LOCAL_FILE_BECAUSE_OF_INFECTION, file.getFile()));
                FileUploader.removeFile(file);
                throw e;
            }
        else {
            throw new FileStorageException(Messages.NO_VIRUS_SCANNER_CONFIGURED);
        }
    }

    private FileEntry createFileEntry(String space, String namespace, String name, FileUpload localFile) {
        FileEntry fileEntry = new FileEntry();
        fileEntry.setId(UUID.randomUUID().toString());
        fileEntry.setSpace(space);
        fileEntry.setName(name);
        fileEntry.setNamespace(namespace);
        fileEntry.setSize(localFile.getSize());
        fileEntry.setDigest(localFile.getDigest());
        fileEntry.setDigestAlgorithm(localFile.getDigestAlgorithm());
        fileEntry.setModified(new Timestamp(System.currentTimeMillis()));
        return fileEntry;
    }

    private void insertFileAttributes(final FileEntry fileEntry) throws FileStorageException {
        SqlExecutor<Void> executor = new FileServiceSqlExecutor<Void>();
        try {
            executor.execute(new StatementExecutor<Void>() {
                @Override
                public Void execute(Connection connection) throws SQLException {
                    PreparedStatement stmt = null;
                    try {

                        stmt = connection.prepareStatement(getQuery(INSERT_FILE, tableName));
                        stmt.setString(1, fileEntry.getId());
                        stmt.setString(2, fileEntry.getSpace());
                        stmt.setString(3, fileEntry.getName());
                        setOrNull(stmt, 4, fileEntry.getNamespace());
                        getDatabaseDialect().setBigInteger(stmt, 5, fileEntry.getSize());
                        stmt.setString(6, fileEntry.getDigest());
                        stmt.setString(7, fileEntry.getDigestAlgorithm());
                        stmt.setTimestamp(8, new Timestamp(fileEntry.getModified().getTime()));
                        stmt.executeUpdate();
                        JdbcUtil.commit(connection);
                    } catch (SQLException e) {
                        JdbcUtil.rollback(connection);
                        throw e;
                    } finally {
                        JdbcUtil.closeQuietly(stmt);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            throw new FileStorageException(e.getMessage(), e);
        }
    }

    protected abstract boolean uploadFileContent(final InputStream inputStream, final FileEntry fileEntry) throws FileStorageException;

    public int deleteAll(final String space, final String namespace) throws FileStorageException {
        SqlExecutor<Integer> executor = new FileServiceSqlExecutor<Integer>();
        try {
            return executor.execute(new StatementExecutor<Integer>() {
                @Override
                public Integer execute(Connection connection) throws SQLException {
                    int rowsDeleted = 0;
                    PreparedStatement stmt = null;
                    try {
                        stmt = connection.prepareStatement(getQuery(DELETE_FILES_BY_NAMESPACE, tableName));
                        stmt.setString(1, namespace);
                        stmt.setString(2, space);
                        rowsDeleted = stmt.executeUpdate();
                        JdbcUtil.commit(connection);
                        return rowsDeleted;
                    } catch (SQLException e) {
                        JdbcUtil.rollback(connection);
                        throw e;
                    } finally {
                        JdbcUtil.closeQuietly(stmt);
                    }
                }
            });
        } catch (SQLException e) {
            throw new FileStorageException(e.getMessage(), e);
        }
    }

    public int deleteFile(final String space, final String id) throws FileStorageException {
        deleteFileContent(space, id);
        return deleteFileAttributes(space, id);
    }

    protected abstract void deleteFileContent(String space, String id) throws FileStorageException;

    public int deleteFileAttributes(final String space, final String id) throws FileStorageException {
        SqlExecutor<Integer> executor = new FileServiceSqlExecutor<Integer>();
        try {
            return executor.execute(new StatementExecutor<Integer>() {
                @Override
                public Integer execute(Connection connection) throws SQLException {
                    PreparedStatement stmt = null;
                    int rowsDeleted = 0;
                    try {
                        stmt = connection.prepareStatement(getQuery(DELETE_FILE_BY_ID, tableName));
                        stmt.setString(1, id);
                        stmt.setString(2, space);
                        rowsDeleted = stmt.executeUpdate();
                        JdbcUtil.commit(connection);
                        return rowsDeleted;
                    } catch (SQLException e) {
                        JdbcUtil.rollback(connection);
                        throw e;
                    } finally {
                        JdbcUtil.closeQuietly(stmt);
                    }
                }
            });
        } catch (SQLException e) {
            throw new FileStorageException(e.getMessage(), e);
        }
    }

    public List<FileEntry> listFiles(final String space, final String namespace) throws FileStorageException {
        SqlExecutor<List<FileEntry>> executor = new FileServiceSqlExecutor<List<FileEntry>>();
        try {
            return executor.execute(new StatementExecutor<List<FileEntry>>() {
                @Override
                public List<FileEntry> execute(Connection connection) throws SQLException {
                    List<FileEntry> files = new ArrayList<FileEntry>();
                    PreparedStatement stmt = null;
                    ResultSet resultSet = null;
                    try {
                        if (space == null) {
                            stmt = connection.prepareStatement(getQuery(SELECT_FILES_BY_NAMESPACE, tableName));
                            stmt.setString(1, namespace);
                        } else if (namespace == null) {
                            stmt = connection.prepareStatement(getQuery(SELECT_FILES_BY_SPACE, tableName));
                            stmt.setString(1, space);
                        } else {
                            stmt = connection.prepareStatement(getQuery(SELECT_FILES_BY_NAMESPACE_AND_SPACE, tableName));
                            stmt.setString(1, namespace);
                            stmt.setString(2, space);
                        }
                        resultSet = stmt.executeQuery();
                        while (resultSet.next()) {
                            files.add(getFileEntry(resultSet));
                        }
                    } finally {
                        JdbcUtil.closeQuietly(resultSet);
                        JdbcUtil.closeQuietly(stmt);
                    }
                    return files;
                }
            });
        } catch (SQLException e) {
            throw new FileStorageException(e.getMessage(), e);
        }
    }

    public List<FileEntry> listFiles(final String namespace) throws FileStorageException {
        return listFiles(null, namespace);
    }

    public FileEntry getFile(final String space, final String id) throws FileStorageException {
        SqlExecutor<FileEntry> executor = new FileServiceSqlExecutor<FileEntry>();
        try {
            return executor.execute(new StatementExecutor<FileEntry>() {
                @Override
                public FileEntry execute(Connection connection) throws SQLException {
                    FileEntry fileEntry = null;
                    PreparedStatement stmt = null;
                    ResultSet resultSet = null;
                    try {
                        stmt = connection.prepareStatement(getQuery(SELECT_FILE_BY_ID, tableName));
                        stmt.setString(1, id);
                        stmt.setString(2, space);
                        resultSet = stmt.executeQuery();
                        if (resultSet.next()) {
                            fileEntry = getFileEntry(resultSet);
                        }
                    } finally {
                        JdbcUtil.closeQuietly(resultSet);
                        JdbcUtil.closeQuietly(stmt);
                    }
                    return fileEntry;
                }
            });
        } catch (SQLException e) {
            throw new FileStorageException(e.getMessage(), e);
        }
    }

    private FileEntry getFileEntry(ResultSet resultSet) throws SQLException {
        FileEntry fileEntry = new FileEntry();
        fileEntry.setId(resultSet.getString(FileServiceColumnNames.FILE_ID));
        fileEntry.setDigest(resultSet.getString(FileServiceColumnNames.DIGEST));
        fileEntry.setSpace(resultSet.getString(FileServiceColumnNames.SPACE));
        fileEntry.setName(resultSet.getString(FileServiceColumnNames.FILE_NAME));
        fileEntry.setNamespace(resultSet.getString(FileServiceColumnNames.NAMESPACE));
        fileEntry.setSize(getDatabaseDialect().getBigInteger(resultSet, FileServiceColumnNames.FILE_SIZE));
        fileEntry.setDigestAlgorithm(resultSet.getString(FileServiceColumnNames.DIGEST_ALGORITHM));
        fileEntry.setModified(resultSet.getDate(FileServiceColumnNames.MODIFIED));
        return fileEntry;
    }

    @Deprecated
    public void processFileContent(final String space, final String fileId, final FileContentProcessor streamProcessor)
        throws FileStorageException {
        processFileContent(new DefaultFileDownloadProcessor(space, fileId, streamProcessor));
    }

    private void setOrNull(PreparedStatement stmt, int position, String value) throws SQLException {
        if (value == null) {
            stmt.setNull(position, Types.NULL);
        } else {
            stmt.setString(position, value);
        }
    }

    /**
     * Reads file from the storage.
     *
     * @param space
     * @param fileProcessor file processor
     * @throws FileStorageException
     */
    public abstract void processFileContent(final FileDownloadProcessor fileDownloadProcessor) throws FileStorageException;

    private DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = lookupDataSource();
        }
        return dataSource;
    }

    protected DatabaseDialect getDatabaseDialect() {
        if (databaseDialect == null) {
            databaseDialect = new DefaultDatabaseDialect();
        }
        return databaseDialect;
    }

    private static DataSource lookupDataSource() {
        try {
            return (DataSource) new InitialContext().lookup(DEFAULT_DATASOURCE_JNDI_NAME);
        } catch (NamingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    protected String getQuery(String statementTemplate, String tableName) {
        return MessageFormat.format(statementTemplate, tableName);
    }

    protected class FileServiceSqlExecutor<T> extends SqlExecutor<T> {

        @Override
        protected Connection getConnection() throws SQLException {
            return getDataSource().getConnection();
        }
    }

    public class FileServiceColumnNames {
        public static final String CONTENT = "CONTENT";
        public static final String MODIFIED = "MODIFIED";
        public static final String DIGEST_ALGORITHM = "DIGEST_ALGORITHM";
        public static final String FILE_SIZE = "FILE_SIZE";
        public static final String NAMESPACE = "NAMESPACE";
        public static final String SPACE = "SPACE";
        public static final String FILE_NAME = "FILE_NAME";
        public static final String DIGEST = "DIGEST";
        public static final String FILE_ID = "FILE_ID";
    }
}
