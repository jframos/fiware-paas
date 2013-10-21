/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * default Environment implementation
 * 
 * @author Henar Mu�oz
 */
@Path("/catalog/org/{org}/vdc/{vdc}/environment/{environment}/tier/{tier}/productRelease")
@Component
@Scope("request")
public class ProductReleaseResourceImpl implements ProductReleaseResource {

    @InjectParam("productReleaseManager")
    private ProductReleaseManager productReleaseManager;
    @InjectParam("tierManager")
    private TierManager tierManager;

    private SystemPropertiesProvider systemPropertiesProvider;

    public void delete(String org, String vdc, String envName, String tierName, String productReleaseName)
            throws EntityNotFoundException {
        ClaudiaData claudiaData = new ClaudiaData(org, vdc, envName);

        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
            claudiaData.setUser(getCredentials());
        }

        try {

            ProductRelease productRelease = productReleaseManager.load(productReleaseName);

            Tier tier = tierManager.load(tierName, vdc, envName);
            tier.removeProductRelease(productRelease);
            tierManager.update(tier);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new WebApplicationException(e, 500);
        }

    }

    public void insert(String org, String vdc, String environmentName, String tierName,
            ProductReleaseDto productReleaseDto) {

        ClaudiaData claudiaData = new ClaudiaData(org, vdc, environmentName);
        ProductRelease productRelease = null;

        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
            claudiaData.setUser(getCredentials());
        }

        try {
            productRelease = productReleaseManager.load(productReleaseDto.getProductName() + "-"
                    + productReleaseDto.getVersion());
        } catch (EntityNotFoundException e1) {

            throw new WebApplicationException(e1, 500);
        }

        try {

            Tier tier = tierManager.load(tierName, vdc, environmentName);
            tier.addProductRelease(productRelease);
            tierManager.addSecurityGroupToProductRelease(claudiaData, tier, productRelease);
            tierManager.update(tier);

        } catch (InvalidEntityException e) {
            throw new WebApplicationException(e, 500);
        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 500);
        } catch (Exception e) {
            throw new WebApplicationException(e, 500);
        }

    }

    public ProductReleaseDto load(String org, String vdc, String environment, String tier, String name)
            throws EntityNotFoundException {
        try {
            ProductRelease productRelease = productReleaseManager.load(name);

            return convertToDto(productRelease);

        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        }
    }

    private ProductReleaseDto convertToDto(ProductRelease productRelease) {

        ProductReleaseDto productReleaseDto = new ProductReleaseDto();

        if (productRelease.getName() != null)
            productReleaseDto.setProductName(productRelease.getName());
        if (productRelease.getVersion() != null)
            productRelease.setVersion(productRelease.getVersion());

        return productReleaseDto;
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public PaasManagerUser getCredentials() {
        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE"))
            return (PaasManagerUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        else
            return null;
    }

    public List<ProductReleaseDto> findAll(Integer page, Integer pageSize, String orderBy, String orderType,
            String environment, String tier) {
        // TODO Auto-generated method stub
        return null;
    }

}
