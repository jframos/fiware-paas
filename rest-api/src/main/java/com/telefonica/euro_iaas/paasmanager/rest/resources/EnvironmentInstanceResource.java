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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.QuotaExceededException;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstancePDto;

public interface EnvironmentInstanceResource {

    /**
     * Create a Instance of an Environment from an environmentDto
     * 
     * @param vdc
     * @param environmentDto
     * @param callback
     * @return
     * @throws EntityNotFoundException
     * @throws InvalidEntityException
     * @throws AlreadyExistsEntityException
     * @throws InvalidEnvironmentRequestException
     */
    /*
     * @POST
     * @Path("/")
     * @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
     * @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) Task create(@PathParam("vdc") String vdc,
     * EnvironmentDto environmentDto,
     * @HeaderParam("callback") String callback) throws EntityNotFoundException, InvalidEntityException,
     * AlreadyExistsEntityException, InvalidEnvironmentRequestException;
     */

    /**
     * Create an Environment Instance from a payload
     * 
     * @param vdc
     * @param org
     * @param envInstanceDto
     * @param callback
     * @return the task that informs how the environment creation process evolves
     * @throws InvalidEnvironmentRequestException
     * @throws EntityNotFoundException
     * @throws InvalidEntityException
     * @throws AlreadyExistsEntityException
     */
    @POST
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task create(@PathParam("org") String org, @PathParam("vdc") String vdc, EnvironmentInstanceDto envInstanceDto,
            @HeaderParam("callback") String callback) throws InvalidEnvironmentRequestException,
            EntityNotFoundException, InvalidEntityException, AlreadyExistsEntityException, InfrastructureException,
            InvalidOVFException, QuotaExceededException;

    /*
     * @POST
     * @Path("/")
     * @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
     * @Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) Task create(@PathParam("org") String org,
     * @PathParam("vdc") String vdc, EnvInstDto envInstDto, @HeaderParam("callback") String callback) throws
     * InvalidEnvironmentRequestException, EntityNotFoundException, InvalidEntityException,
     * AlreadyExistsEntityException, InfrastructureException, InvalidOVFException;
     */
    /**
     * Retrieve all EnvironmentInstance that match with a given criteria.
     * 
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by default <i>nullable</i>)
     * @param status
     *            the status the product (<i>nullable</i>)
     * @param status
     *            the status the product (<i>nullable</i>)
     * @return the retrieved environment instances.
     */
    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<EnvironmentInstancePDto> findAll(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType,
            @QueryParam("status") List<Status> status, @PathParam("vdc") String vdc);

    /**
     * Retrieve all EnvironmentInstanceOvs that match with a given criteria.
     * 
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by default <i>nullable</i>)
     * @param status
     *            the status the product (<i>nullable</i>)
     * @param status
     *            the status the product (<i>nullable</i>)
     * @return the retrieved environment instances.
     */
    /*
     * @GET
     * @Path("/")
     * @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) List<EnvironmentInstanceOvfDto> findAll(
     * @QueryParam("page") Integer page,
     * @QueryParam("pageSize") Integer pageSize,
     * @QueryParam("orderBy") String orderBy,
     * @QueryParam("orderType") String orderType,
     * @QueryParam("status") List<Status> status,
     * @PathParam("vdc") String vdc);
     */

    /**
     * Retrieve the selected Environment instance.
     * 
     * @param name
     *            the instance name
     * @param vdc
     *            the vdc
     * @return the environment instance
     */
    @GET
    @Path("/{name}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    EnvironmentInstancePDto load(@PathParam("vdc") String vdc, @PathParam("name") String name);

    /**
     * Retrieve the selected EnvironmentInstance payload.
     * 
     * @param name
     *            the instance name
     * @param vdc
     *            the vdc
     * @return the environment instance
     */
    /*
     * @GET
     * @Path("/{name}")
     * @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) EnvironmentInstanceOvfDto
     * load(@PathParam("vdc") String vdc,
     * @PathParam("name") String name);
     */

    /**
     * Destroy an Instance of an Environment.
     * 
     * @param callback
     *            if not empty, contains the url where the result of the async operation will be sent
     * @return the task.
     */
    @DELETE
    @Path("/{name}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task destroy(@PathParam("org") String org, @PathParam("vdc") String vdc, @PathParam("name") String name,
            @HeaderParam("callback") String callback);
}
