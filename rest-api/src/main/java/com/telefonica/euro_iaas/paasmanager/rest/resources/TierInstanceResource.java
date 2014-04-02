/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;
import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;

public interface TierInstanceResource {

    /**
     * Creates a new TierInstance.
     */

    @POST
    @Path("/")
    // @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task insert(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environmentInstance") String environment, TierDto tierDto,
            @HeaderParam("callback") String callback) throws APIException;

    @PUT
    @Path("/")
    // @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Task update(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environmentInstance") String environment, TierInstanceDto tierInstanceDto,
            @HeaderParam("callback") String callback);

    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<TierInstanceDto> findAll(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType,
            @QueryParam("status") List<Status> status, @PathParam("vdc") String vdc,
            @PathParam("environmentInstance") String environmentInstance);

    /**
     * Retrieve the selected tierInstance.
     */

    @GET
    @Path("/{name}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    TierInstanceDto load(@PathParam("vdc") String vdc, @PathParam("environmentInstance") String environmentInstance,
            @PathParam("name") String name);

    /**
     * Create an environment Instance from a payload.
     */

    @DELETE
    @Path("/{tierInstance}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task removeTierInstance(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environmentInstance") String environmentInstance,
            @PathParam("tierInstance") String tierInstances, @HeaderParam("callback") String callback)
            throws APIException;

}
