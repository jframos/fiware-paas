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
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;

/*
 * Provides a rest api to works with Environment.
 * @author Jesus M. Movilla
 */
public interface TierResource {

    /**
     * Add the selected Environment in to the SDC's catalog. If the Environment already exists, then add the new
     * Release.
     */

    @POST
    @Path("/")
    // @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void insert(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environment") String environment, TierDto TierDto) throws APIException;

    /**
     * Retrieve all Tiers available created in the system.
     * 
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by default <i>nullable</i>)
     * @return the Tiers.
     */

    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<TierDto> findAll(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType, @PathParam("vdc") String vdc, 
            @PathParam("environment") String environment);

    /**
     * Retrieve the selected Tier.
     */

    @GET
    @Path("/{tierName}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    TierDto load(@PathParam("vdc") String vdc, @PathParam("environment") String environment,
            @PathParam("tierName") String tierName) throws APIException;

    /**
     * Delete the Tier in DB.
     */

    @DELETE
    @Path("/{tierName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void delete(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environment") String environment, @PathParam("tierName") String tierName) throws APIException;

    /**
     * Update the Tier in DB.
     */

    @PUT
    @Path("/{tierName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void update(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environment") String environment, @PathParam("tierName") String tierName, TierDto TierDto) throws APIException;

}
