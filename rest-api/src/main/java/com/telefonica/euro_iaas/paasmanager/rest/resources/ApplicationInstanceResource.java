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

import com.telefonica.euro_iaas.paasmanager.exception.ApplicationInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationReleaseDto;

/**
 * Provides a rest api to works with ApplicationInstances
 * 
 * @author Jesus M. Movilla
 */
public interface ApplicationInstanceResource {

    /**
     * Install a list of application in a given host running on the selected products.
     * 
     * @param org
     *            , the org environment belongs to
     * @param vdc
     *            the vdc where the application will be installed.
     * @param environmentInstanceName
     * @param application
     *            the application to install containing the , the appName and the environment Intance where the
     *            application is going to be installed.
     * @param callback
     *            if not null, contains the url where the system shall notify when the task is done
     * @throws InvalidApplicationReleaseException
     *             , ApplicationInstanceNotFoundException, ProductReleaseNotFoundException
     * @return the task referencing the installed application.
     */
    @POST
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task install(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environmentInstance") String environmentInstance, ApplicationReleaseDto applicationReleaseDto,
            @HeaderParam("callback") String callback) throws InvalidApplicationReleaseException,
            ApplicationInstanceNotFoundException, ProductReleaseNotFoundException;

    /**
     * Find the applications according to the criteria specified in the request
     * 
     * @param page
     * @param pageSize
     * @param orderBy
     * @param orderType
     * @param status
     * @param vdc
     * @param environmentInstance
     * @param productInstance
     * @param applicationName
     * @return
     * @throws ApplicationInstanceNotFoundException
     */
    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<ApplicationInstance> findAll(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType,
            @QueryParam("status") List<Status> status, @PathParam("vdc") String vdc,
            @PathParam("environmentInstance") String environmentInstance,
            @PathParam("productInstance") String productInstance, @QueryParam("applicationName") String applicationName)
            throws ApplicationInstanceNotFoundException;

    /**
     * Retrieve the selected application instance.
     * 
     * @param name
     *            the applicationInstanceName
     * @param vdc
     * @return the application instance
     */
    @GET
    @Path("/{name}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    ApplicationInstance load(@PathParam("vdc") String vdc, @PathParam("name") String name);

    /**
     * Uninstall a previously installed instance.
     * 
     * @param id
     *            the installable instance id
     * @param callback
     *            if not empty, contains the url where the result of the async operation will be sent
     * @return the task.
     */
    @DELETE
    @Path("/{name}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task uninstall(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environmentInstance") String environmentInstance, @PathParam("name") String name,
            @HeaderParam("callback") String callback);

}
