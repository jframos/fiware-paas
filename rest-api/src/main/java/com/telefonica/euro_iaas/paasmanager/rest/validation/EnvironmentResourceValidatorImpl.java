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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.QuotaExceededException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * * @author Henar Munoz
 */
public class EnvironmentResourceValidatorImpl implements EnvironmentResourceValidator {

    private static Logger log = LoggerFactory.getLogger(EnvironmentResourceValidatorImpl.class);
    private TierResourceValidator tierResourceValidator;
    private EnvironmentManager environmentManager;
    private EnvironmentInstanceManager environmentInstanceManager;
    private ResourceValidator resourceValidator;

    /**
     * 
     */
    public void validateCreate(ClaudiaData claudiaData, EnvironmentDto environmentDto, String vdc)
            throws AlreadyExistEntityException, InvalidEntityException {

        try {
            environmentManager.load(environmentDto.getName(), vdc);
            log.error("The environment " + environmentDto.getName() + " already exists");
            throw new AlreadyExistEntityException("The environment " + environmentDto.getName() + " already exists");

        } catch (EntityNotFoundException e1) {
            resourceValidator.validateName(environmentDto.getName());
            resourceValidator.validateDescription(environmentDto.getDescription());
        }

        if (environmentDto.getTierDtos() != null) {

            for (TierDto tierDto : environmentDto.getTierDtos()) {
                log.info("Validating " + tierDto.getName());
                try {
                    tierResourceValidator.validateCreate(claudiaData, tierDto, vdc, environmentDto.getName());
                } catch (InfrastructureException e) {
                    throw new InvalidEntityException(e);
                } catch (QuotaExceededException e) {
                    throw new InvalidEntityException(e);
                }
            }
        }

    }

    /**
     * 
     */
    public void validateAbstractCreate(EnvironmentDto environmentDto) throws AlreadyExistEntityException,
            InvalidEntityException {

        try {
            environmentManager.load(environmentDto.getName(), "");
            log.error("The environment " + environmentDto.getName() + " already exists");
            throw new AlreadyExistEntityException("The environment " + environmentDto.getName() + " already exists");

        } catch (EntityNotFoundException e1) {
            resourceValidator.validateName(environmentDto.getName());
            resourceValidator.validateDescription(environmentDto.getDescription());
        }

        if (environmentDto.getTierDtos() != null) {

            for (TierDto tierDto : environmentDto.getTierDtos()) {
                log.info("Validating " + tierDto.getName());
                tierResourceValidator.validateCreateAbstract(tierDto, environmentDto.getName());
            }
        }

    }

    public void validateDelete(String environmentName, String vdc) throws EntityNotFoundException,
            InvalidEntityException {
        Environment environment = null;

        environment = environmentManager.load(environmentName, vdc);

        if (validateEnvironmentInstance(environment, vdc)) {
            throw new InvalidEntityException("The environment is being used by an environment instance");
        }

    }

    public void validateUpdate(String environmentName, String vdc, SystemPropertiesProvider systemPropertiesProvider)
            throws InvalidEntityException, EntityNotFoundException {

        Environment environment = null;

        environment = environmentManager.load(environmentName, vdc);

        if (validateEnvironmentInstance(environment, vdc)) {
            throw new InvalidEntityException("The environment is being used by an env instance");
        }

    }

    private boolean validateEnvironmentInstance(Environment env, String vdc) {
        EnvironmentInstanceSearchCriteria criteria = new EnvironmentInstanceSearchCriteria();

        criteria.setVdc(vdc);
        criteria.setEnvironment(env);

        List<EnvironmentInstance> envInstances = environmentInstanceManager.findByCriteria(criteria);

        if (envInstances != null && envInstances.size() != 0) {
            return true;
        }
        return false;
    }

    public void setTierResourceValidator(TierResourceValidator tierResourceValidator) {
        this.tierResourceValidator = tierResourceValidator;
    }

    public void setEnvironmentManager(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

    public void setEnvironmentInstanceManager(EnvironmentInstanceManager environmentInstanceManager) {
        this.environmentInstanceManager = environmentInstanceManager;
    }

    public void setResourceValidator(ResourceValidator resourceValidator) {
        this.resourceValidator = resourceValidator;
    }

}
