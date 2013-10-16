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

import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.EnvironmentInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;

/*
 * Provides a rest api to works with Environment.
 * @author Jesus M. Movilla
 */
public interface EnvironmentResource {

    /**
     * Add the selected Environment in to the SDC's catalog. If the Environment already exists, then add the new
     * Release.
     * 
     * @param EnvironmentDto
     *            <ol>
     *            <li>The EnvironmentDto: contains the information about the product</li>
     *            </ol>
     * @return the environment.
     * @throws InvalidEnvironmentRequestException
     * @throws InvalidEntityException
     * @throws AlreadyExistEntityException
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
    void insert(@PathParam("org") String org, @PathParam("vdc") String vdc, EnvironmentDto environmentDto)
            throws InvalidEnvironmentRequestException, AlreadyExistEntityException, InvalidEntityException;

    /**
     * Retrieve all Environments available created in the system.
     * 
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by default <i>nullable</i>)
     * @return the Environments.
     */

    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<EnvironmentDto> findAll(@PathParam("org") String org, @PathParam("vdc") String vdc,
            @QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType);

    /**
     * Retrieve the selected Environment.
     * 
     * @param name
     *            the environment name
     * @return the environment.
     * @throws EnvironmentNotFoundException
     *             if the environment does not exist
     */

    @GET
    @Path("/{envName}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    EnvironmentDto load(@PathParam("org") String org, @PathParam("vdc") String vdc, @PathParam("envName") String name)
            throws EnvironmentInstanceNotFoundException;

    /**
     * Delete the Environment in BBDD,
     * 
     * @param name
     *            the env name
     * @throws InvalidEntityException
     * @throws AlreadyExistEntityException
     * @throws InvalidEnvironmentRequestException
     * @throws EnvironmentNotFoundException
     *             if the Environment does not exists
     * @throws ProductReleaseStillInstalledException
     * @thorws ProductReleaseInApplicationReleaseException
     */

    @DELETE
    @Path("/{envName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void delete(@PathParam("org") String org, @PathParam("vdc") String vdc, @PathParam("envName") String envName)
            throws EnvironmentInstanceNotFoundException, InvalidEntityException, InvalidEnvironmentRequestException,
            AlreadyExistEntityException;

    /**
     * Update the Environment in BBDD,
     * 
     * @param EnvironmentDto
     *            the product name
     * @throws EnvironmentNotFoundException
     *             if the Environment does not exists
     * @throws InvalidEnvironmentException
     *             if the Environment is not valid
     * @throws ProductReleaseNotFoundException
     *             if the ProductRelease does not exists
     * @throws ProductReleaseNotFoundException
     *             if the ProductRelease does not exists
     */

    /*
     * @PUT
     * @Path("/{envName}")
     * @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
     * @Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) Environment update(EnvironmentDto
     * environmentDto) throws EnvironmentNotFoundException, InvalidEnvironmentException,
     * ProductReleaseNotFoundException, ProductNotFoundException;
     */
}
