/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.rest.validation;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
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
     * @throws InvalidEnvironmentRequestException
     */
    void validateCreate(TierDto EnvironmentDto, String vdc, String environmentName,
            SystemPropertiesProvider systemPropertiesProvider) throws InvalidEntityException,
            AlreadyExistEntityException;

    void validateUpdate(TierDto EnvironmentDto, String vdc, String environmentName,
            SystemPropertiesProvider systemPropertiesProvider) throws InvalidEntityException, EntityNotFoundException;

    void validateDelete(String vdc, String environmentName, SystemPropertiesProvider systemPropertiesProvider)
            throws InvalidEntityException, EntityNotFoundException;
}
