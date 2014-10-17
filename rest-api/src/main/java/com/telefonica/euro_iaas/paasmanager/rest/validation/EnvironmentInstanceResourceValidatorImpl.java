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

package com.telefonica.euro_iaas.paasmanager.rest.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telefonica.euro_iaas.paasmanager.claudia.QuotaClient;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.QuotaExceededException;
import com.telefonica.euro_iaas.paasmanager.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Limits;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * * @author jesus.movilla
 */
public class EnvironmentInstanceResourceValidatorImpl implements EnvironmentInstanceResourceValidator {

    private EnvironmentInstanceDao environmentInstanceDao;
    private TierResourceValidator tierResourceValidator;

    private QuotaClient quotaClient;
    private ProductReleaseManager productReleaseManager;
    private ResourceValidator resourceValidator;

    /** The log. */
    private static Logger log = LoggerFactory.getLogger(EnvironmentInstanceResourceValidatorImpl.class);

    /**
     * It validate the creation of an environment instance.
     */
    public void validateCreate(EnvironmentInstanceDto environmentInstanceDto,
            SystemPropertiesProvider systemPropertiesProvider, ClaudiaData claudiaData) throws InvalidEntityException,
            QuotaExceededException {

        if (environmentInstanceDto.getEnvironmentDto() == null) {
            log.error("The environment to be deployed is null ");
            throw new InvalidEntityException("The environment to be deployed is null ");
        }

        resourceValidator.validateName(environmentInstanceDto.getBlueprintName());
        resourceValidator.validateDescription(environmentInstanceDto.getDescription());

        log.debug("Validate enviornment instance blueprint " + environmentInstanceDto.getBlueprintName()
                + " description " + environmentInstanceDto.getDescription() + " environment "
                + environmentInstanceDto.getEnvironmentDto());

        EnvironmentInstanceSearchCriteria criteria = new EnvironmentInstanceSearchCriteria();

        criteria.setVdc(claudiaData.getVdc());
        criteria.setEnviromentName(environmentInstanceDto.getBlueprintName());

        List<EnvironmentInstance> envInstances = environmentInstanceDao.findByCriteria(criteria);

        if (envInstances.size() != 0) {
            throw new InvalidEntityException("The environment instance " + environmentInstanceDto.getBlueprintName()
                    + " already exists");
        }

        if (environmentInstanceDto.getEnvironmentDto().getTierDtos() == null) {
            log.error("There are no tiers " + "defined in EnvironmentDto object");
            throw new InvalidEntityException("There are no tiers " + "defined in EnvironmentDto object");
        }

        String system = systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM);
        if ("FIWARE".equals(system)) {
            for (TierDto tierDto : environmentInstanceDto.getEnvironmentDto().getTierDtos()) {
                validateTier(tierDto);
            }
            tierResourceValidator.validateTiersDependencies(environmentInstanceDto.getEnvironmentDto().getName(),
                    claudiaData.getVdc(), environmentInstanceDto.getEnvironmentDto().getTierDtos());
        }

        // Validating length of hostname (maximum =64)
        for (TierDto tierDto : environmentInstanceDto.getEnvironmentDto().getTierDtos()) {

            int hostnameLength = environmentInstanceDto.getBlueprintName().length() + tierDto.getName().length() + 5;
            if (hostnameLength > 64) {
                int exceed = hostnameLength - 64;
                String message = "Hostname is too long (over 64) exceeded by " + exceed + " characters . "
                        + "Please revise the length of " + "BluePrint Instance Name "
                        + environmentInstanceDto.getBlueprintName() + " and tierName " + tierDto.getName();
                log.error(message);
                throw new InvalidEntityException(message);
            }
        }

        validateQuota(claudiaData, environmentInstanceDto);
    }

    /**
     * It validates the tier.
     * 
     * @param tierDto
     * @throws InvalidEntityException
     */
    public void validateTier(TierDto tierDto) throws InvalidEntityException {

        if (tierDto.getMaximumNumberInstances() == null) {
            throw new InvalidEntityException("Maximum Number Instances " + "from tierDto is null");
        }
        if (tierDto.getMinimumNumberInstances() == null) {
            throw new InvalidEntityException("Minimum Number Instances " + "from tierDto is null");
        }
        if (tierDto.getInitialNumberInstances() == null) {
            throw new InvalidEntityException("Initial Number Instances " + "from tierDto is null");
        }
        if (tierDto.getName() == null) {
            throw new InvalidEntityException("Tier Name " + "from tierDto is null");
        }
        if (tierDto.getImage() == null) {
            throw new InvalidEntityException("Tier Image " + "from tierDto is null");
        }
        if (tierDto.getFlavour() == null) {
            throw new InvalidEntityException("Tier Flavour " + "from tierDto is null");
        }

    }

    /**
     * It validates the quota.
     */
    public void validateQuota(ClaudiaData claudiaData, EnvironmentInstanceDto environmentInstanceDto)
        throws QuotaExceededException, InvalidEntityException {

        Map<String, Limits> limits = new HashMap<String, Limits>();

        Integer initialNumberInstances = 0;
        Integer floatingIPs = 0;
        List<String> securityGroupList = new ArrayList<String>(2);

        if (environmentInstanceDto.getTierInstances() != null) {
            for (TierInstanceDto tierInstanceDto : environmentInstanceDto.getTierInstances()) {
                String region = tierInstanceDto.getTierDto().getRegion();
                if (!limits.containsKey(region)) {
                    try {
                        limits.put(region, quotaClient.getLimits(claudiaData, region));
                    } catch (InfrastructureException e) {
                        throw new InvalidEntityException("Failed in getLimits " + e.getMessage());
                    }
                }

                initialNumberInstances += tierInstanceDto.getTierDto().getInitialNumberInstances();
                if ("true".equals(tierInstanceDto.getTierDto().getFloatingip())) {
                    floatingIPs++;
                }
                String securityGroup = tierInstanceDto.getTierDto().getSecurityGroup();
                if (tierInstanceDto.getTierDto().getSecurityGroup() != null) {
                    if (!securityGroupList.contains(securityGroup)) {
                        securityGroupList.add(securityGroup);
                    }
                }

                Limits limitsRegion = limits.get(region);

                if (limitsRegion.checkTotalInstancesUsed()) {

                    if (initialNumberInstances + limitsRegion.getTotalInstancesUsed() > limitsRegion
                            .getMaxTotalInstances()) {
                        throw new QuotaExceededException("max number of instances exceeded: "
                                + limitsRegion.getMaxTotalInstances());
                    }
                }

                if (limitsRegion.checkTotalFloatingsIpsUsed()) {
                    if (floatingIPs + limitsRegion.getTotalFloatingIpsUsed() > limitsRegion.getMaxTotalFloatingIps()) {
                        throw new QuotaExceededException("max number of floating IPs exceeded: "
                                + limitsRegion.getMaxTotalFloatingIps());
                    }
                }

                if (limitsRegion.checkTotalSecurityGroupsUsed()) {
                    if (securityGroupList.size() + limitsRegion.getTotalSecurityGroups() > limitsRegion
                            .getMaxSecurityGroups()) {
                        throw new QuotaExceededException("max number of security groups exceeded: "
                                + limitsRegion.getMaxSecurityGroups());
                    }
                }

            }
        }
    }

    public void setEnvironmentInstanceDao(EnvironmentInstanceDao environmentInstanceDao) {
        this.environmentInstanceDao = environmentInstanceDao;
    }

    public void setTierResourceValidator(TierResourceValidator tierResourceValidator) {
        this.tierResourceValidator = tierResourceValidator;
    }

    public QuotaClient getQuotaClient() {
        return quotaClient;
    }

    public void setQuotaClient(QuotaClient quotaClient) {
        this.quotaClient = quotaClient;
    }

    public void setResourceValidator(ResourceValidator resourceValidator) {
        this.resourceValidator = resourceValidator;
    }

}
