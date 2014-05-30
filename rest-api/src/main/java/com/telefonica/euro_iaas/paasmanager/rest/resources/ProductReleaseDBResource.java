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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;

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
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void insert(ProductReleaseDto ProductReleaseDto);

    @DELETE
    @Path("/{productReleaseName}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    void delete(@PathParam("productReleaseName") String productReleaseName) throws APIException;

}
