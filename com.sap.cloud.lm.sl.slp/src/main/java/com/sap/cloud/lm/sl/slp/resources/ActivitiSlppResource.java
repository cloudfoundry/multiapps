package com.sap.cloud.lm.sl.slp.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.sap.cloud.lm.sl.common.SLException;
import com.sap.cloud.lm.sl.slp.activiti.ActivitiProcess;
import com.sap.cloud.lm.sl.slp.activiti.SlpObjectFactory;
import com.sap.cloud.lm.sl.slp.message.Messages;
import com.sap.cloud.lm.sl.slp.model.ServiceMetadata;
import com.sap.lmsl.slp.Action;
import com.sap.lmsl.slp.Actions;
import com.sap.lmsl.slp.Breakpoint;
import com.sap.lmsl.slp.Breakpoints;
import com.sap.lmsl.slp.ComponentVersion;
import com.sap.lmsl.slp.Config;
import com.sap.lmsl.slp.Dialog;
import com.sap.lmsl.slp.Dialogs;
import com.sap.lmsl.slp.Error;
import com.sap.lmsl.slp.Log;
import com.sap.lmsl.slp.Logs;
import com.sap.lmsl.slp.MetaDialogs;
import com.sap.lmsl.slp.Metadata;
import com.sap.lmsl.slp.Monitor;
import com.sap.lmsl.slp.Parameter;
import com.sap.lmsl.slp.Properties;
import com.sap.lmsl.slp.Property;
import com.sap.lmsl.slp.Roadmap;
import com.sap.lmsl.slp.SlppResource;
import com.sap.lmsl.slp.Task;
import com.sap.lmsl.slp.Tasklist;
import com.sap.lmsl.slp.Versions;

public abstract class ActivitiSlppResource extends ActivitiSlpBaseResource implements SlppResource {

    @PathParam("serviceId")
    protected String serviceId;

    @PathParam("processId")
    protected String processId;

    protected String getServiceId() {
        return serviceId;
    }

    protected String getProcessId() {
        return processId;
    }

    @Override
    public Metadata getMetadata() {
        String action = Messages.GET_SLPP_METADATA;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(serviceMetadata, action);
            return SlpObjectFactory.createSlppMetadata();
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, Messages.GET_SLPP_METADATA);
        }
    }

    @Override
    public Monitor getMonitor() {
        String action = Messages.GET_SLPP_MONITOR;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(serviceMetadata, action);
            return getActivitiProcess(serviceMetadata, action).getMonitor();
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Task getMonitortaskId(String taskId) {
        String action = Messages.GET_SLPP_MONITOR_TASK_ID;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(serviceMetadata, action);
            return getActivitiProcess(serviceMetadata, action).getMonitorTaskById(taskId);
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Error getMonitortaskIderror(String taskId) {
        String action = Messages.GET_SLPP_MONITOR_TASK_ID_ERROR;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(serviceMetadata, action);
            return getActivitiProcess(serviceMetadata, action).getMonitorTaskErrorById(taskId);
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Logs getMonitortaskIdlogs(String taskId) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public Tasklist getTasklist() {
        String action = Messages.GET_SLPP_TASKLIST;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(serviceMetadata, action);
            return getActivitiProcess(serviceMetadata, action).getTasklist();
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Task getTasklisttaskId(String taskId) {
        String action = Messages.GET_SLPP_TASKLIST_TASK_ID;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(serviceMetadata, action);
            return getActivitiProcess(serviceMetadata, action).getTaskById(taskId);
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Error getTasklisttaskIderror(String taskId) {
        String action = Messages.GET_SLPP_TASKLIST_TASK_ID_ERROR;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(serviceMetadata, action);
            return getActivitiProcess(serviceMetadata, action).getErrorByTaskId(taskId);
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Logs getTasklisttaskIdlogs(String arg0) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public Config getConfig() {
        String action = Messages.GET_SLPP_CONFIG;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(serviceMetadata, action);
            return getActivitiProcess(serviceMetadata, action).getConfig();
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Config postConfig(Config config) {
        String action = Messages.POST_SLPP_CONFIG;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(serviceMetadata, action);
            return getActivitiProcess(serviceMetadata, action).setConfig(config);
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Parameter getConfigparameterId(String parameterId) {
        String action = Messages.GET_SLPP_CONFIG_PARAMETER;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(serviceMetadata, action);
            return getActivitiProcess(serviceMetadata, action).getConfigParameter(parameterId);
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Actions getActions() {
        String action = Messages.GET_SLPP_ACTIONS;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(serviceMetadata, action);
            return getActivitiProcess(serviceMetadata, action).getActions();
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Action getActionsactionId(String actionId) {
        String action = Messages.GET_SLPP_ACTION;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(serviceMetadata, action);
            ActivitiProcess process = getActivitiProcess(serviceMetadata, action);
            return findAction(process, actionId);
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public void postActionsactionId(String actionId) {
        String action = Messages.EXECUTE_SLPP_ACTION;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(serviceMetadata, action);
            ActivitiProcess process = getActivitiProcess(serviceMetadata, action);
            Action slpAction = findAction(process, actionId);

            // TODO: log also concrete action
            auditLogAboutToPerformAction(action);

            process.executeAction(getAuthenticatedUser(), slpAction.getId());

            auditLogActionPerformed(action, true);
        } catch (Exception e) {
            auditLogActionPerformed(action, false);
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Roadmap getRoadmap() {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public Task getRoadmaptaskId(String arg0) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public Error getRoadmaptaskIderror(String arg0) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public Logs getRoadmaptaskIdlogs(String arg0) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public Logs getLogs() {
        String action = Messages.GET_SLPP_LOGS;
        try {
            ServiceMetadata metadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(metadata, action);
            return getActivitiProcess(metadata, action).getLogs(getSpaceForProcess(getProcessId()));
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Log getLogslogId(String logId) {
        String action = Messages.GET_SLPP_LOG;
        try {
            ServiceMetadata metadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(metadata, action);
            return getActivitiProcess(metadata, action).getLog(getSpaceForProcess(getProcessId()), logId);
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("logs/{logId}/content")
    public String getLogContents(@PathParam("logId") final String logId) throws SLException {
        String action = Messages.GET_SLPP_LOG_CONTENT;
        try {
            ServiceMetadata metadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(metadata, action);
            return getActivitiProcess(metadata, action).getLogContent(getSpaceForProcess(getProcessId()), logId);
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Error getError() {
        String action = Messages.GET_SLPP_ERROR;
        try {
            ServiceMetadata serviceMetadata = getServiceMetadata(getServiceId());
            ensureUserIsAuthorized(serviceMetadata, action);
            return getActivitiProcess(serviceMetadata, action).getError();
        } catch (Exception e) {
            throw logAndReturnWebApplicationException(e, action);
        }
    }

    @Override
    public Dialogs getDialogs() {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public Dialog getDialogsdialogId(String arg0) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    // FIXME: The two REST methods getDialogsdialogId() and getDialogsmetaDialogURL(), that are
    // defines in SlppResource have same
    // @Path and same @Consumes and @Produces mime types. This breaks jersey servlet initialization.
    // The SlppResource should be fixed in a path to com.sap.lmsl.slp.
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public MetaDialogs getDialogsmetaDialogURL(String arg0) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public Versions getVersions() {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public ComponentVersion getVersionsversionId(String arg0) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public Breakpoints getBreakpoints() {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public Breakpoints postBreakpoints(Breakpoints arg0) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public Breakpoint getBreakpointsbreakpointId(String arg0) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public Breakpoints getCurrentBreakpoints() {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public Breakpoint getCurrentBreakpointscurrentBreakpointId(String arg0) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public Properties getProperties() {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public Property getPropertiespropertyId(String arg0) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    private ActivitiProcess getActivitiProcess(ServiceMetadata serviceMetadata, String action) throws SLException {
        return getActivitiProcess(serviceMetadata, getProcessId(), action);
    }

    private static Action findAction(ActivitiProcess process, String actionId) {
        Action action = process.getAction(actionId);
        if (action == null)
            throw new WebApplicationException(Status.NOT_FOUND);
        return action;
    }

    protected abstract void auditLogActionPerformed(String action, boolean success);

    protected abstract void auditLogAboutToPerformAction(String action);

}
