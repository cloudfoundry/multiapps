package com.sap.cloud.lm.sl.slp.resources;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.persistence.model.FileEntry;
import com.sap.cloud.lm.sl.persistence.services.AbstractFileService;
import com.sap.cloud.lm.sl.persistence.services.DatabaseFileService;
import com.sap.cloud.lm.sl.persistence.services.FileStorageException;
import com.sap.cloud.lm.sl.slp.ServiceRegistry;
import com.sap.cloud.lm.sl.slp.activiti.ActivitiFacade;
import com.sap.cloud.lm.sl.slp.activiti.ActivitiService;
import com.sap.cloud.lm.sl.slp.activiti.SlpObjectFactory;
import com.sap.cloud.lm.sl.slp.message.Messages;
import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;
import com.sap.lmsl.slp.Files;
import com.sap.lmsl.slp.Metadata;
import com.sap.lmsl.slp.Parameter;
import com.sap.lmsl.slp.Parameters;
import com.sap.lmsl.slp.Process;
import com.sap.lmsl.slp.Processes;
import com.sap.lmsl.slp.Service;
import com.sap.lmsl.slp.Services;
import com.sap.lmsl.slp.SlmpResource;
import com.sap.lmsl.slp.Versions;

public abstract class ActivitiSlmpResource extends ActivitiSlpBaseResource implements SlmpResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiSlmpResource.class);

    private static final String TARGET_PLATFORM_PARAMETER = "targetPlatform";
    private static final String DEPLOY_TARGET_PARAMETER = "deployTarget";

    @Inject
    // The @Named annotation is needed for the Jersey-Spring integration in order to identify which
    // FileService managed instance (FileService or ProgressMessageService) to inject.
    @Named("fileService")
    private AbstractFileService fileService;

    @Inject
    private ActivitiFacade activitiFacade;

    @Context
    private HttpServletRequest request;

    @GET
    public Response fetchCsrfToken() {
        return Response.status(200).build();
    }

    @Override
    public Metadata getMetadata() {
        String action = Messages.GET_SLMP_METADATA;
        ensureUserIsAuthorized(null, action);
        try {
            return SlpObjectFactory.createSlmpMetadata();
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Services getServices() {
        String action = Messages.GET_SLMP_SERVICES;
        ensureUserIsAuthorized(null, action);
        try {
            return ActivitiService.getServices(getActivitiServices());
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Service getServicesserviceId(String serviceId) {
        String action = Messages.GET_SLMP_SERVICE;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(serviceId);
            ensureUserIsAuthorized(serviceMetadata, action);
            return activitiServiceFactory.createActivitiService(serviceMetadata).getService();
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Processes getServicesserviceIdprocesses(String serviceId) {
        String action = Messages.GET_SLMP_SERVICE_PROCESSES;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(serviceId);
            ensureUserIsAuthorized(serviceMetadata, action);
            ActivitiService activitiService = activitiServiceFactory.createActivitiService(serviceMetadata);
            return activitiService.getProcesses(getSpace());
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Produces(value = { "application/xml" })
    @javax.ws.rs.Path(value = "/services/{serviceId}/processes/{processId}")
    public Process getServicesserviceIdprocessesprocessId(@javax.ws.rs.PathParam(value = "serviceId") String serviceId,
        @javax.ws.rs.PathParam(value = "processId") String processId) {
        String action = Messages.GET_SLMP_SERVICE_PROCESS;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(serviceId);
            ensureUserIsAuthorized(serviceMetadata, action);
            ActivitiService activitiService = activitiServiceFactory.createActivitiService(serviceMetadata);
            Process process = activitiService.getProcess(getSpaceForProcess(processId), processId);
            if (process == null)
                throw new WebApplicationException(Status.NOT_FOUND);
            return process;
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Process postServicesserviceIdprocesses(String serviceId, Process process) {
        String action = Messages.CREATE_SLMP_PROCESS;
        try {
            // TODO: audit log also start-up parameters
            auditLogAboutToPerformAction(action, serviceId);
            ServiceMetadata serviceMetadata = getServiceMetadata(serviceId);
            ensureUserIsAuthorized(serviceMetadata, action);
            ActivitiService activitiService = activitiServiceFactory.createActivitiService(serviceMetadata);

            List<Parameter> parameters = process.getParameters().getParameter();
            renameTargetPlatformParameter(parameters);

            Process createdProcess = activitiService.startProcess(getSpace(), getAuthenticatedUser(), parameters,
                getAdditionalVariablesForProcessStart());

            auditLogActionPerformed(action, serviceId, true);

            return createdProcess;
        } catch (Exception e) {
            auditLogActionPerformed(action, serviceId, false);
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    protected Map<String, Object> getAdditionalVariablesForProcessStart() {
        return new HashMap<String, Object>();
    }

    @Override
    public void deleteServicesserviceIdprocessesprocessId(String serviceId, String processId) {
        String action = Messages.DELETE_SLMP_SERVICE_PROCESS;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(serviceId);
            ensureUserIsAuthorized(serviceMetadata, action);
            getActivitiProcess(serviceMetadata, processId, action).delete(getAuthenticatedUser());
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Parameters getServicesserviceIdparameters(String arg0) {
        throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
    }

    @Override
    public Versions getServicesserviceIdversions(String serviceId) {
        String action = Messages.GET_SLMP_SERVICE_VERSIONS;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(serviceId);
            ensureUserIsAuthorized(serviceMetadata, action);
            Versions versions = SlpObjectFactory.createVersions();
            for (String version : serviceMetadata.getVersions()) {
                versions.getComponentVersion().add(
                    SlpObjectFactory.createComponentVersion(String.format("%s_VERSIONS_%s", serviceId, version), serviceId, version));
            }
            return versions;
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Files getServicesserviceIdfiles(String serviceId) {
        String action = Messages.GET_SLMP_SERVICES;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(serviceId);
            ensureUserIsAuthorized(serviceMetadata, action);
            List<FileEntry> uploadedFiles = getFileService().listFiles(getSpace(), serviceId);
            return SlpObjectFactory.createFiles(uploadedFiles.toArray(new FileEntry[uploadedFiles.size()]));
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Files postServicesserviceIdfiles(String serviceId) {
        String action = Messages.UPLOAD_SLMP_SERVICE_FILES;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(serviceId);
            ensureUserIsAuthorized(serviceMetadata, action);

            List<FileEntry> uploadedFiles = uploadFiles(serviceId);

            return SlpObjectFactory.createFiles(uploadedFiles.toArray(new FileEntry[uploadedFiles.size()]));
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    private List<FileEntry> uploadFiles(String serviceId) throws FileUploadException, IOException, FileStorageException, SLException {
        String space = getSpace();
        ServletFileUpload upload = getFileUploadServlet();
        long maxUploadSize = getConfiguration().getMaxUploadSize();
        upload.setSizeMax(maxUploadSize);

        List<FileEntry> uploadedFiles = new ArrayList<FileEntry>();
        FileItemIterator fileItemIterator = null;
        try {
            fileItemIterator = upload.getItemIterator(getRequest());
        } catch (SizeLimitExceededException ex) {
            throw new SLException(MessageFormat.format(Messages.MAX_UPLOAD_SIZE_EXCEEDED, maxUploadSize));
        }
        while (fileItemIterator.hasNext()) {
            FileItemStream item = fileItemIterator.next();
            if (item.isFormField()) {
                continue; // ignore simple (non-file) form fields
            }

            InputStream in = null;
            try {
                in = item.openStream();
                FileEntry entry = getFileService().addFile(space, serviceId, item.getName(),
                    getConfiguration().getFileUploadProcessor(item.getName()), in);
                uploadedFiles.add(entry);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
        return uploadedFiles;
    }

    @Override
    public Files putServicesserviceIdfilesfileId(String serviceId, String fileId, InputStream fileStream) {
        String action = Messages.UPLOAD_SLMP_SERVICE_FILE;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(serviceId);
            ensureUserIsAuthorized(serviceMetadata, action);
            FileEntry storeFile = getFileService().addFile(getSpace(), serviceId, fileId, getConfiguration().getFileUploadProcessor(fileId),
                fileStream);
            return SlpObjectFactory.createFiles(storeFile);
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        } finally {
            try {
                fileStream.close();
            } catch (IOException e) {
                String errorMessage = MessageFormat.format(Messages.ERROR_EXECUTING_REST_API_CALL, action);
                LOGGER.error(errorMessage, e);
            }
        }
    }

    @Override
    public void deleteServicesserviceIdfiles(String serviceId) {
        String action = Messages.DELETE_SLMP_SERVICE_FILES;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(serviceId);
            ensureUserIsAuthorized(serviceMetadata, action);
            getFileService().deleteAll(getSpace(), serviceId);
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public void deleteServicesserviceIdfilesfileId(String serviceId, String fileId) {
        String action = Messages.DELETE_SLMP_SERVICE_FILE;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(serviceId);
            ensureUserIsAuthorized(serviceMetadata, action);
            getFileService().deleteFile(getSpace(), fileId);
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Processes getProcesses() {
        // TODO Implement this resource
        throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
    }

    @Override
    public Process getProcessesprocessId(String processId) {
        String action = Messages.GET_SLMP_PROCESS;
        try {
            String serviceId = activitiFacade.getServiceId(processId);
            if (serviceId == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            ServiceMetadata serviceMetadata = getServiceMetadata(serviceId);
            ensureUserIsAuthorized(serviceMetadata, action);
            return getActivitiProcess(serviceMetadata, processId, action).getProcess();
        } catch (SLException e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public void deleteProcessesprocessId(String arg0) {
        throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
    }

    @Override
    public Service getProcessesprocessIdservice(String arg0) {
        // TODO Implement this resource
        throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
    }

    @Path("/runs/{serviceId}/{processId}")
    public abstract ActivitiSlppResource getProcess(@PathParam("serviceId") String serviceId, @PathParam("processId") String processId);

    private List<ActivitiService> getActivitiServices() {
        List<ActivitiService> activitiServices = new ArrayList<ActivitiService>();
        Set<ServiceMetadata> servicesMetadata = ServiceRegistry.getInstance().getServices();
        for (ServiceMetadata serviceMetadata : servicesMetadata) {
            if (isServiceAccessibleForCurrentUser(serviceMetadata)) {
                activitiServices.add(activitiServiceFactory.createActivitiService(serviceMetadata));
            }
        }
        return activitiServices;
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    @Deprecated
    private void renameTargetPlatformParameter(List<Parameter> parameters) {
        for (Parameter parameter : parameters) {
            if (parameter.getId().equals(TARGET_PLATFORM_PARAMETER)) {
                parameter.setId(DEPLOY_TARGET_PARAMETER);
                break;
            }
        }
    }

    protected ServletFileUpload getFileUploadServlet() {
        return new ServletFileUpload();
    }

    protected abstract boolean isServiceAccessibleForCurrentUser(ServiceMetadata serviceMetadata);

    protected abstract void auditLogAboutToPerformAction(String action, String serviceId);

    protected abstract void auditLogActionPerformed(String action, String serviceId, boolean success);

    public AbstractFileService getFileService() {
        if (fileService == null) {
            fileService = DatabaseFileService.getInstance();
        }
        return fileService;
    }

}
