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

import com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException;
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
     * @throws InvalidEntityException
     */
    void validateCreatePayload(String payload) throws InvalidEntityException;

    /**
     * Validate the requets to create and EnvironmentInstance from a EnvironmentDto.
     * 
     *
     * @param environmentDto
     * @param claudiaData
     * @throws InvalidEnvironmentRequestException
     * @throws InvalidEntityException
     */
    void validateCreate(EnvironmentInstanceDto environmentDto, SystemPropertiesProvider systemPropertiesProvider,
        ClaudiaData claudiaData)throws  QuotaExceededException, InvalidEntityException;

    /**
     * Validate quota.
     * 
     * @param claudiaData
     * @param environmentInstanceDto
     * @throws InvalidEntityException
     */
    void validateQuota(ClaudiaData claudiaData, EnvironmentInstanceDto environmentInstanceDto)
        throws QuotaExceededException, InvalidEntityException;
}
