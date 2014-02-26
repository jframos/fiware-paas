/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.validation;

import java.util.Set;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;

import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.QuotaExceededException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public interface TierResourceValidator {

    /**
     * Validate the requets to create and EnvironmentInstance from a EnvironmentDto
     * 
     * @param EnvironmentDto
     * @throws com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException 
     * @throws InvalidEnvironmentRequestException
     */
    void validateCreate(ClaudiaData claudiaData, TierDto EnvironmentDto, String vdc, String environmentName,
            SystemPropertiesProvider systemPropertiesProvider) throws InvalidEntityException,
            AlreadyExistEntityException, InfrastructureException, QuotaExceededException, com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException;

    void validateUpdate(TierDto EnvironmentDto, String vdc, String environmentName,
            SystemPropertiesProvider systemPropertiesProvider) throws InvalidEntityException, EntityNotFoundException;

    void validateDelete(String vdc, String environmentName, SystemPropertiesProvider systemPropertiesProvider)
            throws InvalidEntityException, EntityNotFoundException;

    void validateTiersDependencies(String name, String vdc, Set<TierDto> set) throws InvalidEntityException;
}
