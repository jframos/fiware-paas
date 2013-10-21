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

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidTierInstanceRequestException;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;

/**
 * Default EnvironmentInstanceResource interfaz.
 * 
 * @author bmmanso
 */
public interface TierInstanceResource {

    /**
     * Create a new TierInstance
     * 
     * @param org
     * @param vdc
     * @param environment
     * @param tierInstanceDto
     * @param callback
     * @return
     * @throws InvalidTierInstanceRequestException
     * @throws InvalidEnvironmentRequestException
     * @throws InvalidEntityException
     */

    @POST
    @Path("/")
    // @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task insert(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environmentInstance") String environment, TierDto tierDto,
            @HeaderParam("callback") String callback) throws InvalidTierInstanceRequestException,
            InvalidEnvironmentRequestException, InvalidEntityException, EntityNotFoundException;

    @PUT
    @Path("/")
    // @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Task update(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environmentInstance") String environment, TierInstanceDto tierInstanceDto,
            @HeaderParam("callback") String callback);

    /**
     * @param page
     * @param pageSize
     * @param orderBy
     * @param orderType
     * @param status
     * @param vdc
     * @param environmentInstance
     * @return
     * @throws EntityNotFoundException
     */

    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<TierInstanceDto> findAll(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType,
            @QueryParam("status") List<Status> status, @PathParam("vdc") String vdc,
            @PathParam("environmentInstance") String environmentInstance);

    /**
     * Retrieve the selected tierInstance
     * 
     * @param name
     * @return
     * @throws EntityNotFoundException
     */

    @GET
    @Path("/{name}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    TierInstanceDto load(@PathParam("vdc") String vdc, @PathParam("environmentInstance") String environmentInstance,
            @PathParam("name") String name);

    /**
     * Create an environment Instance from a payload
     * 
     * @param org
     * @param vdc
     * @param environmentInstance
     * @param tierInstances
     * @param payload
     * @param callback
     * @return
     * @throws EntityNotFoundException
     * @throws InvalidTierInstanceRequestException
     * @throws InvalidEnvironmentRequestException
     */

    @DELETE
    @Path("/{tierInstance}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task removeTierInstance(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environmentInstance") String environmentInstance,
            @PathParam("tierInstance") String tierInstances, @HeaderParam("callback") String callback)
            throws EntityNotFoundException, InvalidTierInstanceRequestException, InvalidEnvironmentRequestException;

}
