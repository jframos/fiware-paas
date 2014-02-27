/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.validation;

import java.util.List;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
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

    private static Logger log = Logger.getLogger(EnvironmentResourceValidatorImpl.class);
    private TierResourceValidator tierResourceValidator;
    private EnvironmentManager environmentManager;
    private EnvironmentInstanceManager environmentInstanceManager;
    private ResourceValidator resourceValidator;

    /**
     * 
     */
    public void validateCreate(ClaudiaData claudiaData, EnvironmentDto environmentDto, String vdc) throws AlreadyExistEntityException, InvalidEntityException {

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
    public void validateAbstractCreate(EnvironmentDto environmentDto) throws AlreadyExistEntityException, InvalidEntityException {

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
    
    public void validateDelete(String environmentName, String vdc, SystemPropertiesProvider systemPropertiesProvider)
            throws EntityNotFoundException, InvalidEnvironmentRequestException {
        Environment environment = null;

        environment = environmentManager.load(environmentName, vdc);

        if (validateEnvironmentInstance(environment, vdc)) {
            throw new InvalidEnvironmentRequestException("The environment is being used by an environment instance");
        }

    }

    public void validateUpdate(String environmentName, String vdc, SystemPropertiesProvider systemPropertiesProvider)
            throws InvalidEnvironmentRequestException, EntityNotFoundException {

        Environment environment = null;

        environment = environmentManager.load(environmentName, vdc);

        if (validateEnvironmentInstance(environment, vdc)) {
            throw new InvalidEnvironmentRequestException("The environment is being used by an env instance");
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
    public void setResourceValidator (ResourceValidator resourceValidator) {
    	this.resourceValidator = resourceValidator;
    }

}
