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


import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;

import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;

import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentInstanceSearchCriteria;

import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * Test class for validation of enviornment
 * @author henar
 *
 */
public class EnviornmentResourceValidatorImplTest {
	EnvironmentResourceValidatorImpl environmentResourceValidator ;
	EnvironmentManager environmentManager;
	EnvironmentInstanceManager environmentInstanceManager;
	ResourceValidator resourceValidator;
	TierResourceValidator tierResourceValidator;
	SystemPropertiesProvider systemPropertiesProvider;
	
	@Before
	public void setUp () throws EntityNotFoundException, com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException, AlreadyExistEntityException {
		environmentResourceValidator = new EnvironmentResourceValidatorImpl();
		resourceValidator = mock(ResourceValidator.class);
        environmentManager=mock(EnvironmentManager.class);
        tierResourceValidator=mock(TierResourceValidator.class);
        environmentResourceValidator.setResourceValidator(resourceValidator);
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        environmentInstanceManager=mock(EnvironmentInstanceManager.class);
        environmentResourceValidator.setEnvironmentManager(environmentManager);
        environmentResourceValidator.setEnvironmentInstanceManager(environmentInstanceManager);
        environmentResourceValidator.setTierResourceValidator(tierResourceValidator);
		Mockito.doNothing().when(resourceValidator).validateName(anyString());
		Mockito.doNothing().when(resourceValidator).validateDescription(anyString());
		Mockito.doNothing().when(tierResourceValidator).validateCreateAbstract(any(TierDto.class), anyString());

	}

    @Test
    public void shouldValidateEnvironmentDataOK() throws Exception {
        // given
         
        EnvironmentDto envDto = new EnvironmentDto ();
        envDto.setName("name");
        envDto.setDescription("description");
        when(environmentManager.load(anyString(),anyString())).thenThrow
            (new EntityNotFoundException(Environment.class, "dd", envDto.fromDto()));
        ClaudiaData claudiaData = mock(ClaudiaData.class);

        environmentResourceValidator.validateCreate(claudiaData, envDto, "vdc");
       
    }
    
    @Test
    public void shouldValidateAbstractEnv() throws EntityNotFoundException  {
        
        EnvironmentDto envDto = new EnvironmentDto();
        envDto.setName("name");
        envDto.setDescription("description");
        when(environmentManager.load(anyString(),anyString())).
            thenThrow(new EntityNotFoundException(Environment.class, "dd", envDto.fromDto()));
        
        try {
            environmentResourceValidator.validateAbstractCreate(envDto);
        } catch (Exception e) {
           fail();
        }

    }
    
    @Test
    public void shouldValidateDeleteEnv() throws EntityNotFoundException, InvalidEntityException  {
        
        EnvironmentDto envDto = new EnvironmentDto();
        envDto.setName("name");
        envDto.setDescription("description");
        TierDto tierDTO = new TierDto();
        tierDTO.setName("tier1");
        tierDTO.setInitialNumberInstances(new Integer(1));
        tierDTO.setMaximumNumberInstances(new Integer(1));
        tierDTO.setMinimumNumberInstances(new Integer(1));
        tierDTO.setImage("image");
        tierDTO.setFlavour("flavor");
        Set<TierDto> ltiers = new HashSet<TierDto>();
        ltiers.add(tierDTO);
        envDto.setTierDtos(ltiers);
        when(environmentManager.load(anyString(),anyString())).thenReturn(envDto.fromDto());
        when(environmentInstanceManager.findByCriteria(any(EnvironmentInstanceSearchCriteria.class))).thenReturn(new ArrayList<EnvironmentInstance>());        
        environmentResourceValidator.validateDelete(envDto.getName(), "vdc");


    }
    
    @Test
    public void shouldValidateAbstractEnvWitTiers() throws EntityNotFoundException, AlreadyExistEntityException, InvalidEntityException  {
        
        EnvironmentDto envDto = new EnvironmentDto();
        envDto.setName("name");
        envDto.setDescription("description");
        TierDto tierDTO = new TierDto();
        tierDTO.setName("tier1");
        tierDTO.setInitialNumberInstances(new Integer(1));
        tierDTO.setMaximumNumberInstances(new Integer(1));
        tierDTO.setMinimumNumberInstances(new Integer(1));
        tierDTO.setImage("image");
        tierDTO.setFlavour("flavor");
        Set<TierDto> ltiers = new HashSet<TierDto>();
        ltiers.add(tierDTO);
        envDto.setTierDtos(ltiers);
        when(environmentManager.load(anyString(),anyString())).
        thenThrow(new EntityNotFoundException(Environment.class, "dd", envDto.fromDto()));
        
        environmentResourceValidator.validateAbstractCreate(envDto);


    }
    
    @Test(expected=AlreadyExistEntityException.class)
    public void shouldValidateAbstractEnvAlreadyExists() throws EntityNotFoundException, AlreadyExistEntityException, InvalidEntityException  {
        
        EnvironmentDto envDto = new EnvironmentDto();
        envDto.setName("name");
        envDto.setDescription("description");
        when(environmentManager.load(anyString(),anyString())).thenReturn(envDto.fromDto());
        environmentResourceValidator.validateAbstractCreate(envDto);
   

    }
    

    


    


 
}
