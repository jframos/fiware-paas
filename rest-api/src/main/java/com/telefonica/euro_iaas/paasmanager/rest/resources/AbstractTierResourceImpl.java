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
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;
import com.telefonica.euro_iaas.paasmanager.rest.validation.TierResourceValidator;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * default Tier implementation
 * 
 * @author Henar Mu�oz
 */
@Path("/catalog/org/{org}/environment/{environment}/tier")
@Component
@Scope("request")
public class AbstractTierResourceImpl implements AbstractTierResource {

    private TierManager tierManager;

    private EnvironmentManager environmentManager;

    private SystemPropertiesProvider systemPropertiesProvider;

    private TierResourceValidator tierResourceValidator;

    private ProductReleaseDao productReleaseDao;

    private static Logger log = Logger.getLogger(AbstractTierResourceImpl.class);

    public void delete(String org, String envName, String tierName) throws APIException {
        ClaudiaData claudiaData = new ClaudiaData(org, "", envName);
        log.debug("Deleting tier " + tierName + " from env " + envName);

        try {
            tierResourceValidator.validateDelete("", envName, tierName);
            log.debug("Tier validated correctly to be deleted");

            if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
                claudiaData.setUser(getCredentials());
            }

            Tier tier = tierManager.load(tierName, "", envName);

            Environment environment = environmentManager.load(envName);
            environment.deleteTier(tier);
            environmentManager.update(environment);
            tierManager.delete(claudiaData, tier);
        } catch (Exception e) {
            log.error("Error deleting the tier " + e.getMessage());
            throw new APIException(e);
        }

    }

    public List<TierDto> findAll(Integer page, Integer pageSize, String orderBy, String orderType, String environment) throws APIException {
        TierSearchCriteria criteria = new TierSearchCriteria();
        Environment env = null;
        try {
            env = environmentManager.load(environment);

        } catch (EntityNotFoundException e) {
            throw new APIException(e, 404);
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
            throw new APIException(e);
        }

    }

    public void insert(String org, String environmentName, TierDto tierDto) throws APIException {

        log.debug("Insert tier " + tierDto.getName() + " from env " + environmentName + " with product release "
                + tierDto.getProductReleaseDtos()  + " with nets " + tierDto.getNetworksDto());
        ClaudiaData claudiaData = new ClaudiaData(org, "", environmentName);

        try {
            tierResourceValidator.validateCreateAbstract(tierDto, environmentName);

            if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
                claudiaData.setUser(getCredentials());
            }
            Tier tier = tierDto.fromDto("", environmentName);
            log.debug("vdc " + claudiaData.getVdc());

            Environment environment = environmentManager.load(environmentName);
            Tier newTier = tierManager.create(claudiaData, environmentName, tier);
            environment.addTier(newTier);
            environmentManager.update(environment);
        } catch (Exception e) {
            throw new APIException(e);
        }
    }

    public TierDto load(String org, String envName, String name) throws APIException {
        try {
            Tier tier = tierManager.load(name, "", envName);

            return tier.toDto();

        } catch (EntityNotFoundException e) {
            throw new APIException(e);
        }
    }

    public void update(String org, String environmentName, String tierName, TierDto tierDto) throws APIException {
        log.debug("Update tier " + tierName + " from env " + environmentName + "with product release " + 
        		    tierDto.getProductReleaseDtos() + " with nets " + tierDto.getNetworksDto() );
        ClaudiaData claudiaData = new ClaudiaData(org, "", environmentName);

        try {
            tierResourceValidator.validateUpdate( "", environmentName,tierName, tierDto);
            log.debug("Validated tier " + tierDto.getName() + " from env " + environmentName);

            if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
                claudiaData.setUser(getCredentials());
            }

            Tier newtier = tierDto.fromDto("", environmentName);

            Environment environment = environmentManager.load(environmentName);
            List<Tier> tiers = new ArrayList();
            for (Tier tier : environment.getTiers()) {
                tiers.add(tier);
            }
            environment.setTiers(null);
            environmentManager.update(environment);
            for (Tier tier : tiers) {
                if (tier.getName().equals(newtier.getName())) {
                    
                    tier = tierManager.loadTierWithNetworks(tierDto.getName(), "", environmentName);
                    log.debug("load tier " + tierDto.getName() + " with nets " + tier.getName() + " " + tier.getNetworks());
                    tierManager.updateTier(tier, newtier);

                }
                environment.addTier(tier);
                environmentManager.update(environment);
            }
            log.debug("updated tier " + tierDto.getName());

        } catch (Exception e) {
            throw new APIException(e);
        }
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

    public void setEnvironmentManager(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

    public void setTierResourceValidator(TierResourceValidator tierResourceValidator) {
        this.tierResourceValidator = tierResourceValidator;
    }

    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

    private PaasManagerUser getCredentials() {
        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE"))
            return (PaasManagerUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        else
            return null;
    }

}
