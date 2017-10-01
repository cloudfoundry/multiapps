/**
 * Created by Apache CXF WadlToJava code generator
**/
package com.sap.lmsl.slp;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.InputStream;

public interface SlmpResource {

    @GET
    @Produces("application/xml")
    @Path("/metadata")
    Metadata getMetadata();

    @GET
    @Produces("application/xml")
    @Path("/services")
    Services getServices();

    @GET
    @Produces("application/xml")
    @Path("/services/{serviceId}")
    Service getServicesserviceId(@PathParam("serviceId") String serviceId);

    @GET
    @Produces("application/xml")
    @Path("/services/{serviceId}/processes")
    Processes getServicesserviceIdprocesses(@PathParam("serviceId") String serviceId);

    @POST
    @Consumes("application/xml")
    @Produces("application/xml")
    @Path("/services/{serviceId}/processes")
    Process postServicesserviceIdprocesses(@PathParam("serviceId") String serviceId, Process process);

    @DELETE
    @Path("/services/{serviceId}/processes/{processId}")
    void deleteServicesserviceIdprocessesprocessId(@PathParam("serviceId") String serviceId, @PathParam("processId") String processId);

    @GET
    @Produces("application/xml")
    @Path("/services/{serviceId}/parameters")
    Parameters getServicesserviceIdparameters(@PathParam("serviceId") String serviceId);

    @GET
    @Produces("application/xml")
    @Path("/services/{serviceId}/versions")
    Versions getServicesserviceIdversions(@PathParam("serviceId") String serviceId);

    @GET
    @Produces("application/xml")
    @Path("/services/{serviceId}/files")
    Files getServicesserviceIdfiles(@PathParam("serviceId") String serviceId);

    @POST
    @Consumes(" multipart/form-data ")
    @Produces("application/xml")
    @Path("/services/{serviceId}/files")
    Files postServicesserviceIdfiles(@PathParam("serviceId") String serviceId);

    @DELETE
    @Path("/services/{serviceId}/files")
    void deleteServicesserviceIdfiles(@PathParam("serviceId") String serviceId);

    @PUT
    @Consumes("application/octet-stream")
    @Produces("application/xml")
    @Path("/services/{serviceId}/files/{fileId}")
    Files putServicesserviceIdfilesfileId(@PathParam("serviceId") String serviceId, @PathParam("fileId") String fileId, InputStream inputstream);

    @DELETE
    @Path("/services/{serviceId}/files/{fileId}")
    void deleteServicesserviceIdfilesfileId(@PathParam("serviceId") String serviceId, @PathParam("fileId") String fileId);

    @GET
    @Produces("application/xml")
    @Path("/processes")
    Processes getProcesses();

    @GET
    @Produces("application/xml")
    @Path("/processes/{processId}")
    Process getProcessesprocessId(@PathParam("processId") String processId);

    @DELETE
    @Path("/processes/{processId}")
    void deleteProcessesprocessId(@PathParam("processId") String processId);

    @GET
    @Produces("application/xml")
    @Path("/processes/{processId}/service")
    Service getProcessesprocessIdservice(@PathParam("processId") String processId);

}