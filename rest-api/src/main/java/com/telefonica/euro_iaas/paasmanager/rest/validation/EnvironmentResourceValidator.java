/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.validation;

import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public interface EnvironmentResourceValidator {

    /**
     * Validate the requets to create and EnvironmentInstance from a EnvironmentDto
     * 
     * @param EnvironmentDto
     * @throws InvalidEnvironmentRequestException
     */
    void validateCreate(EnvironmentDto EnvironmentDto, String vdc, SystemPropertiesProvider systemPropertiesProvider)
            throws InvalidEnvironmentRequestException, AlreadyExistEntityException, InvalidEntityException;

    void validateDelete(String envName, String vdc, SystemPropertiesProvider systemPropertiesProvider)
            throws InvalidEnvironmentRequestException, AlreadyExistEntityException, InvalidEntityException;

    void validateUpdate(String envName, String vdc, SystemPropertiesProvider systemPropertiesProvider)
            throws InvalidEnvironmentRequestException, AlreadyExistEntityException, InvalidEntityException;
}
