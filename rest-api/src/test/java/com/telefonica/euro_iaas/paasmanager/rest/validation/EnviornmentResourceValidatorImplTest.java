/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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
        environmentResourceValidator.validateDelete(envDto.getName(), "vdc", systemPropertiesProvider);


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
