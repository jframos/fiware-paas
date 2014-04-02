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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;

/*
 * Provides a rest api to works with ProductRelease.
 */
public interface ProductReleaseResource {

    /**
     * Add the selected product release in to the SDC's catalog. If the Environment already exists, then add the new
     * Release.
     * 
     * @param ProductReleaseDto
     *            <ol>
     *            <li>The TierDto: contains the information about the product</li>
     *            </ol>
     */

    @POST
    @Path("/")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void insert(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environment") String environment, @PathParam("tier") String tier,
            ProductReleaseDto ProductReleaseDto);

    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<ProductReleaseDto> findAll(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType,
            @PathParam("environment") String environment, @PathParam("tier") String tier);

    /**
     * Retrieve the selected Tier.
     */

    @GET
    @Path("/{productReleaseName}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    ProductReleaseDto load(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environment") String environment, @PathParam("tier") String tier,
            @PathParam("productReleaseName") String productReleaseName) throws APIException;

    /**
     * Delete the Tier in database.
     */

    @DELETE
    @Path("/{productReleaseName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void delete(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @PathParam("environment") String environment, @PathParam("tier") String tier,
            @PathParam("productReleaseName") String productReleaseName) throws APIException;

}
