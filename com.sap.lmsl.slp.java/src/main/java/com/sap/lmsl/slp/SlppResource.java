/**
 * Created by Apache CXF WadlToJava code generator
**/
package com.sap.lmsl.slp;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

public interface SlppResource {

    @GET
    @Produces("application/xml")
    @Path("/metadata")
    Metadata getMetadata();

    @GET
    @Produces("application/xml")
    @Path("/monitor")
    Monitor getMonitor();

    @GET
    @Produces("application/xml")
    @Path("/monitor/{taskId}")
    Task getMonitortaskId(@PathParam("taskId") String taskId);

    @GET
    @Produces("application/xml")
    @Path("/monitor/{taskId}/logs")
    Logs getMonitortaskIdlogs(@PathParam("taskId") String taskId);

    @GET
    @Produces("application/xml")
    @Path("/monitor/{taskId}/error")
    Error getMonitortaskIderror(@PathParam("taskId") String taskId);

    @GET
    @Produces("application/xml")
    @Path("/tasklist")
    Tasklist getTasklist();

    @GET
    @Produces("application/xml")
    @Path("/tasklist/{taskId}")
    Task getTasklisttaskId(@PathParam("taskId") String taskId);

    @GET
    @Produces("application/xml")
    @Path("/tasklist/{taskId}/logs")
    Logs getTasklisttaskIdlogs(@PathParam("taskId") String taskId);

    @GET
    @Produces("application/xml")
    @Path("/tasklist/{taskId}/error")
    Error getTasklisttaskIderror(@PathParam("taskId") String taskId);

    @GET
    @Produces("application/xml")
    @Path("/config")
    Config getConfig();

    @POST
    @Consumes("application/xml")
    @Produces("application/xml")
    @Path("/config")
    Config postConfig(Config config);

    @GET
    @Produces("application/xml")
    @Path("/config/{parameterId}")
    Parameter getConfigparameterId(@PathParam("parameterId") String parameterId);

    @GET
    @Produces("application/xml")
    @Path("/actions")
    Actions getActions();

    @GET
    @Produces("application/xml")
    @Path("/actions/{actionId}")
    Action getActionsactionId(@PathParam("actionId") String actionId);

    @POST
    @Path("/actions/{actionId}")
    void postActionsactionId(@PathParam("actionId") String actionId);

    @GET
    @Produces("application/xml")
    @Path("/roadmap")
    Roadmap getRoadmap();

    @GET
    @Produces("application/xml")
    @Path("/roadmap/{taskId}")
    Task getRoadmaptaskId(@PathParam("taskId") String taskId);

    @GET
    @Produces("application/xml")
    @Path("/roadmap/{taskId}/logs")
    Logs getRoadmaptaskIdlogs(@PathParam("taskId") String taskId);

    @GET
    @Produces("application/xml")
    @Path("/roadmap/{taskId}/error")
    Error getRoadmaptaskIderror(@PathParam("taskId") String taskId);

    @GET
    @Produces("application/xml")
    @Path("/logs")
    Logs getLogs();

    @GET
    @Produces("application/xml")
    @Path("/logs/{logId}")
    Log getLogslogId(@PathParam("logId") String logId);

    @GET
    @Produces("application/xml")
    @Path("/error")
    Error getError();

    @GET
    @Produces("application/xml")
    @Path("/dialogs")
    Dialogs getDialogs();

    @GET
    @Produces("application/xml")
    @Path("/dialogs/{dialogId}")
    Dialog getDialogsdialogId(@PathParam("dialogId") String dialogId);

    @GET
    @Produces("application/xml")
    @Path("/dialogs/{metaDialogURL}")
    MetaDialogs getDialogsmetaDialogURL(@PathParam("metaDialogURL") String metaDialogURL);

    @GET
    @Produces("application/xml")
    @Path("/versions")
    Versions getVersions();

    @GET
    @Produces("application/xml")
    @Path("/versions/{versionId}")
    ComponentVersion getVersionsversionId(@PathParam("versionId") String versionId);

    @GET
    @Produces("application/xml")
    @Path("/breakpoints")
    Breakpoints getBreakpoints();

    @POST
    @Consumes("application/xml")
    @Produces("application/xml")
    @Path("/breakpoints")
    Breakpoints postBreakpoints(Breakpoints breakpoints);

    @GET
    @Produces("application/xml")
    @Path("/breakpoints/{breakpointId}")
    Breakpoint getBreakpointsbreakpointId(@PathParam("breakpointId") String breakpointId);

    @GET
    @Produces("application/xml")
    @Path("/currentBreakpoints")
    Breakpoints getCurrentBreakpoints();

    @GET
    @Produces("application/xml")
    @Path("/currentBreakpoints/{currentBreakpointId}")
    Breakpoint getCurrentBreakpointscurrentBreakpointId(@PathParam("currentBreakpointId") String currentBreakpointId);

    @GET
    @Produces("application/xml")
    @Path("/properties")
    Properties getProperties();

    @GET
    @Produces("application/xml")
    @Path("/properties/{propertyId}")
    Property getPropertiespropertyId(@PathParam("propertyId") String propertyId);

}