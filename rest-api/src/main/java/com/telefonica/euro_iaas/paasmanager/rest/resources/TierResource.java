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

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidSecurityGroupRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;

/*
 * Provides a rest api to works with Environment.
 * @author Jesus M. Movilla
 */
public interface TierResource {

    /**
     * Add the selected Environment in to the SDC's catalog. If the Environment already exists, then add the new
     * Release.
     * 
     * @param TierDto
     *            <ol>
     *            <li>The TierDto: contains the information about the product</li>
     *            </ol>
     * @return the Tier.
     * @throws AlreadyExistEntityException
     * @throws InvalidEntityException
     * @throws EntityNotFoundException
     * @throws InvalidSecurityGroupRequestException
     * @throws InfrastructureException
     * @throws AlreadyExistsProductReleaseException
     *             if the Product Release exists
     * @throws InvalidProductReleaseException
     *             if the Product Release is invalid due to either OS, Product or Product Release
     * @throws InvalidMultiPartRequestException
     *             when the MUltipart object in the request is null, or its size is different to three or the type sof
     *             the different parts are not ProductReleaseDto, File and File
     */

    @POST
    @Path("/")
    // @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void insert(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environment") String environment, TierDto TierDto) throws InvalidEntityException,
            AlreadyExistEntityException, EntityNotFoundException, InvalidSecurityGroupRequestException,
            InfrastructureException;

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
            @QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType,
            @PathParam("environment") String environment);

    /**
     * Retrieve the selected Tier.
     * 
     * @param name
     *            the Tier name
     * @return the Tier.
     * @throws TierNotFoundException
     *             if the Tier does not exist
     */

    @GET
    @Path("/{tierName}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    TierDto load(@PathParam("vdc") String vdc, @PathParam("environment") String environment,
            @PathParam("tierName") String tierName) throws EntityNotFoundException;

    /**
     * Delete the Tier in BBDD,
     * 
     * @param name
     *            the env name
     * @throws InvalidEntityException
     * @throws TierNotFoundException
     *             if the Tier does not exists
     * @throws ProductReleaseStillInstalledException
     * @thorws ProductReleaseInApplicationReleaseException
     */

    @DELETE
    @Path("/{tierName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void delete(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environment") String environment, @PathParam("tierName") String tierName)
            throws EntityNotFoundException, InvalidEntityException;

    /**
     * Update the Tier in BBDD,
     * 
     * @param TierDto
     *            the product name
     * @throws TierNotFoundException
     *             if the Tier does not exists
     * @throws InvalidTierException
     *             if the Tier is not valid
     * @throws ProductReleaseNotFoundException
     *             if the ProductRelease does not exists
     * @throws ProductReleaseNotFoundException
     *             if the ProductRelease does not exists
     */

    @PUT
    @Path("/{tierName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void update(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environment") String environment, TierDto TierDto) throws EntityNotFoundException,
            InvalidEntityException, ProductReleaseNotFoundException;

}
