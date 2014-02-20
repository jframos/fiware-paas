/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
