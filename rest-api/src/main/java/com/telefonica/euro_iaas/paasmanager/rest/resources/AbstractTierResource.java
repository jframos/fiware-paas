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
 * Provides a rest api to works with Abstract Tier.
 * @author Henar Mu�oz Frutos
 */
public interface AbstractTierResource {

    /**
     * Add the selected Tier for the abstract enviornmetn into the PaaS Manager catalog.
     * 
     * @param TierDto
     *            <ol>
     *            <li>The TierDto: contains the information about the tier</li>
     *            </ol>
     * @param org
     *            The organization where the abstract template belongs
     * @param environment
     *            The environment where this tier belongs to
     */

    @POST
    @Path("/")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void insert(@PathParam("org") String org, @PathParam("environment") String environment, TierDto TierDto)
            throws APIException;

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
     * @throws APIException 
     */

    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<TierDto> findAll(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType,
            @PathParam("environment") String environment) throws APIException;

    /**
     * Retrieve the selected Tier.
     */

    @GET
    @Path("/{tierName}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    TierDto load(@PathParam("org") String org, @PathParam("environment") String environment,
            @PathParam("tierName") String tierName) throws APIException;

    /**
     * Delete the Tier in DB.
     */

    @DELETE
    @Path("/{tierName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void delete(@PathParam("org") String org, @PathParam("environment") String environment,
            @PathParam("tierName") String tierName) throws APIException;

    /**
     * Update the Tier in DB
     * 
     * @param TierDto
     *            the product name
     */

    @PUT
    @Path("/{tierName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void update(@PathParam("org") String org, @PathParam("environment") String environment, TierDto TierDto)
            throws APIException;

}
