/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;

/**
 * @author jesus.movilla
 */
public interface ApplicationReleaseResource {

    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    List<ApplicationRelease> findAll(@QueryParam("artifactName") String artifactName, @QueryParam("page") Integer page,
            @QueryParam("pageSize") Integer pageSize, @QueryParam("orderBy") String orderBy,
            @QueryParam("orderType") String orderType);

    /**
     * Retrieve the selected Application Releases
     * 
     * @param name
     *            the application Release name
     * @return the loaded application.
     * @throws EntityNotFoundException
     *             if the osInstance does not exists
     */
    @GET
    @Path("/{appName}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    ApplicationRelease load(@PathParam("appName") String name) throws EntityNotFoundException;

}
