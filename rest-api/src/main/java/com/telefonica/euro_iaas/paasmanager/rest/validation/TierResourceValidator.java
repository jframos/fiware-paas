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
    void validateCreate(ClaudiaData claudiaData, TierDto EnvironmentDto, String vdc, String environmentName) throws InvalidEntityException,
            AlreadyExistEntityException, InfrastructureException, QuotaExceededException;
    
    void validateCreateAbstract (TierDto tierDto, String environmentName) throws InvalidEntityException, AlreadyExistEntityException;

    void validateUpdate(String vdc, String environmentName, String tierName, TierDto EnvironmentDto) throws InvalidEntityException, EntityNotFoundException;

    void validateDelete(String vdc, String environmentName, String tierName)
            throws InvalidEntityException, EntityNotFoundException;

    void validateTiersDependencies(String name, String vdc, Set<TierDto> set) throws InvalidEntityException;
}
