package com.sap.cloud.lm.sl.slp.resources;

import java.text.MessageFormat;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.common.util.ResponseRenderer;
import com.sap.cloud.lm.sl.slp.ServiceRegistry;
import com.sap.cloud.lm.sl.slp.activiti.ActivitiProcess;
import com.sap.cloud.lm.sl.slp.activiti.ActivitiService;
import com.sap.cloud.lm.sl.slp.activiti.ActivitiServiceFactory;
import com.sap.cloud.lm.sl.slp.message.Messages;
import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;

import virusscanner.VirusScannerException;

public abstract class ActivitiSlpBaseResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiSlpBaseResource.class);

    private final String DEFAULT_SPACE = "DEFAULT";

    @Inject
    protected ActivitiServiceFactory activitiServiceFactory;

    protected abstract String getAuthenticatedUser();

    protected abstract void ensureUserIsAuthorized(ServiceMetadata serviceMetadata, String action);

    /**
     * Can be implemented in subclasses to ensure space isolation (for isolating different users or other tenants) The default
     * implementation always returns null.
     * 
     * @return
     * @throws SLException
     */
    protected String getSpace() throws SLException {
        return DEFAULT_SPACE;
    }

    protected String getSpaceForProcess(String processId) throws SLException {
        return getSpace();
    }

    protected WebApplicationException logAndReturnWebApplicationException(Exception e, String action) {
        if (e instanceof WebApplicationException) {
            return (WebApplicationException) e;
        }
        String errorMessage = MessageFormat.format(Messages.ERROR_EXECUTING_REST_API_CALL, action);
        if (e instanceof VirusScannerException || e instanceof SLException) {
            errorMessage = e.getMessage();
        }
        LOGGER.error(errorMessage, e);
        return new WebApplicationException(ResponseRenderer.renderResponseForStatus(Status.INTERNAL_SERVER_ERROR, errorMessage));
    }

    protected ServiceMetadata getServiceMetadata(String serviceId) {
        ServiceMetadata serviceMetadata = ServiceRegistry.getInstance().getService(serviceId);
        if (serviceMetadata == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return serviceMetadata;
    }

    protected ActivitiProcess getActivitiProcess(ServiceMetadata serviceMetadata, String processId, String action) throws SLException {
        ActivitiService activitiService = activitiServiceFactory.createActivitiService(serviceMetadata);
        ActivitiProcess process = activitiService.getActivitiProcess(getSpaceForProcess(processId), processId);
        if (process == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return process;
    }

    protected Configuration getConfiguration() {
        return new DefaultConfiguration();
    }

}
