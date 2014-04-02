/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
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

import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceOvfDto;
import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;

/**
 * @author jesus.movilla
 */
public interface EnvironmentInstanceOvfResource {

    /**
     * Create an Environment Instance from a payload.
     */
    @POST
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task create(@PathParam("org") String org, @PathParam("vdc") String vdc, String payload,
            @HeaderParam("callback") String callback) throws APIException;

    /**
     * Retrieve all EnvironmentInstanceOvfs that match with a given criteria.
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
    // @Produces( { MediaType.WILDCARD })
    List<EnvironmentInstanceOvfDto> findAll(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType,
            @QueryParam("status") List<Status> status, @PathParam("vdc") String vdc);

    /**
     * Retrieve the selected EnvironmentInstance payload.
     * 
     * @param name
     *            the instance name
     * @param vdc
     *            the vdc
     * @return the environment instance
     */
    @GET
    @Path("/{name}")
    @Produces({ MediaType.TEXT_XML })
    String load(@PathParam("vdc") String vdc, @PathParam("name") String name);

    /*
     * @Path("/{name}")
     * @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) String load(@PathParam("vdc") String vdc,
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
