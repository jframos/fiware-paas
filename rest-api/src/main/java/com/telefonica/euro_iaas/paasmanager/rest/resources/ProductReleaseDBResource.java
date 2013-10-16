package com.telefonica.euro_iaas.paasmanager.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;

/*
 * Provides a rest api to works with ProductRelease.
 */
public interface ProductReleaseDBResource {

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
    void insert(ProductReleaseDto ProductReleaseDto);

    @DELETE
    @Path("/{productReleaseName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void delete(@PathParam("productReleaseName") String productReleaseName) throws EntityNotFoundException;

}
