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

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * default Environment implementation
 * 
 * @author Henar Mu�oz
 */
@Path("/catalog/productRelease")
@Component
@Scope("request")
public class ProductReleaseResourceDBImpl implements ProductReleaseDBResource {

    @InjectParam("productReleaseManager")
    private ProductReleaseManager productReleaseManager;

    private SystemPropertiesProvider systemPropertiesProvider;
    private static Logger log = Logger.getLogger(ProductReleaseResourceDBImpl.class);

    public void insert(ProductReleaseDto productReleaseDto) {
        log.debug("Create product release " + productReleaseDto.getProductName() + " " + productReleaseDto.getVersion());
        ProductRelease productRelease = new ProductRelease(productReleaseDto.getProductName(),
                productReleaseDto.getVersion(), productReleaseDto.getProductDescription(),
                productReleaseDto.getPrivateAttributes());

        try {
            productReleaseManager.load(productReleaseDto.getProductName(), productReleaseDto.getVersion());
            log.info("The product release  " + productReleaseDto.getProductName() + " "
                    + productReleaseDto.getVersion() + " already exists");
        } catch (EntityNotFoundException e) {
            try {
                if (productReleaseDto.getPrivateAttributes() == null) {
                    Set<Attribute> atts = new HashSet<Attribute>();
                    atts.add(new Attribute("openports", "80", "The port opens"));
                    productRelease.setAttributes(atts);

                }
                productRelease = productReleaseManager.create(productRelease);
            } catch (InvalidEntityException e2) {
                log.error("Error inserting the product release  " + e2.getMessage());
                throw new WebApplicationException(e2, 500);
            } catch (AlreadyExistsEntityException e2) {
                log.error("Error inserting the product release " + e2.getMessage());
                throw new WebApplicationException(e2, 500);
            }

        }

    }

    public void delete(String productReleaseName) throws APIException {
        // TODO Auto-generated method stub

    }

}
