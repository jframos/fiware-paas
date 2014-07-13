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

/**
 * Provides the way to find some tasks stored in the system related to Tier instance resource.
 */
public interface TierInstanceResource {

    /**
     * Creates a new TierInstance.
     */
    @POST
    // @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task insert(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environmentInstance") String environment, TierDto tierDto,
            @HeaderParam("callback") String callback) throws APIException;

    /**
     * Update a TierInstance.
     */
    @PUT
    // @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    Task update(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environmentInstance") String environment, TierInstanceDto tierInstanceDto,
            @HeaderParam("callback") String callback);

    /**
     * Find all Tier instances of a environment instance.
     */
    @GET
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
