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
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;

public interface EnvironmentInstanceResource {
	/**
     * Create a Instance of an Environment
     * @param vdc the vdc where the application will be installed.
     * @param environment the environment to install containing the tiers 
     * and productInstances
     * @param callback if not null, contains the url where the system shall
     * notify when the task is done
     * @return the task referencing the installed application.
     */
    @POST
    @Path("/")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Task create(@PathParam("vdc") String vdc, EnvironmentDto environmentDto,
            @HeaderParam("callback") String callback) throws EntityNotFoundException, 
			InvalidEntityException, AlreadyExistsEntityException;
    
    /**
     * Retrieve all ApplicationInstance that match with a given criteria.
     *
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query
     *            (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending
     *            (asc by default <i>nullable</i>)
     * @param status the status the product (<i>nullable</i>)
     * @param status the status the product (<i>nullable</i>)
     * @return the retrieved environment instances.
     */
    @GET
    @Path("/")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<EnvironmentInstance> findAll(
            @QueryParam("page") Integer page,
            @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy,
            @QueryParam("orderType") String orderType,
            @QueryParam("status") List<Status> status,
            @PathParam("vdc") String vdc);
    
    /**
     * Retrieve the selected environment instance.
     * @param id the instance id
     * @return the environment instance
     */
    @GET
    @Path("/{id}")
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    EnvironmentInstance load(@PathParam("name") String name);
    
    /**
     * Destroy an Instance of an Environment.
     *
     * @param id the installable instance id
     * @param callback if not empty, contains the url where the result of the
     * async operation will be sent
     * @return the task.
     */
    @DELETE
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task destroy(@PathParam("vdc") String vdc, @PathParam("id") Long id,
            @HeaderParam("callback") String callback);
}
