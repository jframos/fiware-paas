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

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.NetworkManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;
import com.telefonica.euro_iaas.paasmanager.rest.validation.TierResourceValidator;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * default Tier implementation.
 * 
 * @author Henar Mu�oz
 */
@Path("/catalog/org/{org}/vdc/{vdc}/environment/{environment}/tier")
@Component
@Scope("request")
public class TierResourceImpl implements TierResource {

    private TierManager tierManager;
    
    private NetworkManager networkManager;

    private EnvironmentManager environmentManager;

    private SystemPropertiesProvider systemPropertiesProvider;

    private TierResourceValidator tierResourceValidator;

    private ProductReleaseDao productReleaseDao;

    private static Logger log = Logger.getLogger(TierResourceImpl.class);

    public void delete(String org, String vdc, String envName, String tierName) throws APIException {
        ClaudiaData claudiaData = new ClaudiaData(org, vdc, envName);
        log.debug("Deleting tier " + tierName + " from env " + envName);

        try {
            tierResourceValidator.validateDelete(vdc, envName, tierName);

            if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
                claudiaData.setUser(getCredentials());
            }

            Tier tier = tierManager.load(tierName, vdc, envName);

            Environment environment = environmentManager.load(envName, vdc);
            environment.deleteTier(tier);
            environmentManager.update(environment);
            tierManager.delete(claudiaData, tier);
        } catch (Exception e) {
            log.error("Error deleting the tier " + e.getMessage());
            throw new APIException(e);
        }

        // throw new WebApplicationException(e, 500);

    }

    public List<TierDto> findAll(Integer page, Integer pageSize, String orderBy, String orderType, String vdc, String environment) {
        TierSearchCriteria criteria = new TierSearchCriteria();
        Environment env = null;
        try {
            env = environmentManager.load(environment, vdc);

        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        }

        criteria.setEnvironment(env);

        if (page != null && pageSize != null) {
            criteria.setPage(page);
            criteria.setPageSize(pageSize);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            criteria.setOrderBy(orderBy);
        }
        if (!StringUtils.isEmpty(orderType)) {
            criteria.setOrderBy(orderType);
        }

        try {

            List<TierDto> tierDto = new ArrayList<TierDto>();
            List<Tier> tiers = tierManager.findByCriteria(criteria);

            for (Tier tier : tiers) {
                tierDto.add(tier.toDto());
            }
            return tierDto;

        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        }

    }

    private PaasManagerUser getCredentials() {
        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
            return (PaasManagerUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } else {
            return null;
        }
    }

    public void insert(String org, String vdc, String environmentName, TierDto tierDto) throws APIException {

        log.debug("Insert tier " + tierDto.getName() + " from env " + environmentName);
        ClaudiaData claudiaData = new ClaudiaData(org, vdc, environmentName);
        claudiaData.setUser(getCredentials());

        try {
            tierResourceValidator.validateCreate(claudiaData, tierDto, vdc, environmentName);
        } catch (Exception ex) {
            throw new APIException(ex);
        }

        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
            claudiaData.setUser(getCredentials());
        }
        log.debug("From tier dto " + tierDto + "  product " + tierDto.getProductReleaseDtos() + " nets "
                + tierDto.getNetworksDto());
        Tier tier = tierDto.fromDto(vdc, environmentName);
        log.debug("to tier " + tier + "  product " + tier.getProductReleases() + " nets " + tier.getNetworks());

        try {
            Environment environment = environmentManager.load(environmentName, vdc);
            Tier newTier = tierManager.create(claudiaData, environmentName, tier);
            environment.addTier(newTier);
            environmentManager.update(environment);
        } catch (Exception ex) {
            throw new APIException(ex);
        }
    }

    public TierDto load(String vdc, String envName, String name) throws APIException {
        try {
            Tier tier = tierManager.load(name, vdc, envName);

            return tier.toDto();

        } catch (EntityNotFoundException e) {
            throw new APIException(e);
        }
    }

    public void setEnvironmentManager(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public void setTierManager(TierManager tierManager) {
        this.tierManager = tierManager;
    }

    public void setTierResourceValidator(TierResourceValidator tierResourceValidator) {
        this.tierResourceValidator = tierResourceValidator;
    }
    
    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    public void update(String org, String vdc, String environmentName, String tierName, TierDto tierDto) throws APIException {
        log.debug("Update tier " + tierName + " from env " + environmentName);
        ClaudiaData claudiaData = new ClaudiaData(org, vdc, environmentName);

        try {
            tierResourceValidator.validateUpdate(vdc, environmentName,tierName, tierDto);
            log.debug("Validated tier " + tierDto.getName() + " from env " + environmentName);

            if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
                claudiaData.setUser(getCredentials());
            }

            Tier newtier = tierDto.fromDto(vdc, environmentName);

            Environment environment = environmentManager.load(environmentName, vdc);
            List<Tier> tiers = new ArrayList();
            for (Tier tier : environment.getTiers()) {
                tiers.add(tier);
            }
            environment.setTiers(null);
            environmentManager.update(environment);
            for (Tier tier : tiers) {
                if (tier.getName().equals(newtier.getName())) {
                    log.debug("load tier " + tierDto.getName());
                    tier = tierManager.load(tierDto.getName(), vdc, environmentName);
                    tierManager.updateTier( tier, newtier);

                }
                environment.addTier(tier);
                environmentManager.update(environment);
            }

            log.debug("update tier " + tierDto.getName());

            // environmentManager.update(environment);

        } catch (Exception e) {
            throw new APIException(e);
        }
    }

    

}
