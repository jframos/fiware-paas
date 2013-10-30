/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.validation;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.QuotaExceededException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 */
public interface EnvironmentInstanceResourceValidator {

    /**
     * Validate the request to create an EnvironmentInstance from a payload.
     * 
     * @param payload
     * @throws InvalidEnvironmentRequestException
     */
    void validateCreatePayload(String payload) throws InvalidEnvironmentRequestException;

    /**
     * Validate the requets to create and EnvironmentInstance from a EnvironmentDto.
     * 
     * @param EnvironmentDto
     * @throws InvalidEnvironmentRequestException
     */
    void validateCreate(EnvironmentInstanceDto EnvironmentDto, SystemPropertiesProvider systemPropertiesProvider)
            throws InvalidEnvironmentRequestException;

    /**
     * Validate quota.
     * 
     * @param claudiaData
     * @param environmentInstanceDto
     */
    void validateQuota(ClaudiaData claudiaData, EnvironmentInstanceDto environmentInstanceDto)
            throws InvalidEnvironmentRequestException, QuotaExceededException;
}
