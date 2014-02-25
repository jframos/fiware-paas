/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.QuotaExceededException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.Metadata;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public class EnviornmentResourceValidatorImplTest {
	EnvironmentResourceValidatorImpl environmentResourceValidator ;
	EnvironmentManager environmentManager;
	SystemPropertiesProvider systemPropertiesProvider;
	
	@Before
	public void setUp () throws EntityNotFoundException {
		environmentResourceValidator = new EnvironmentResourceValidatorImpl();
        EnvironmentManager environmentManager=mock(EnvironmentManager.class);
        EnvironmentDto envDto = new EnvironmentDto ();
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        when(environmentManager.load(anyString(),anyString())).thenThrow(new com.telefonica.euro_iaas.commons.dao.EntityNotFoundException(Environment.class, "dd", envDto.fromDto()));
        environmentResourceValidator.setEnvironmentManager(environmentManager);
		
	}

    @Test
    public void shouldValidateEnvironmentDataOK() throws Exception {
        // given
         
        EnvironmentDto envDto = new EnvironmentDto ();
        envDto.setName("name");
        envDto.setDescription("description");
        
        ClaudiaData claudiaData = mock(ClaudiaData.class);

        environmentResourceValidator.validateCreate(claudiaData, envDto, "vdc", systemPropertiesProvider);
       
    }
    
    public void shouldValidateNullNameEnvironment() throws AlreadyExistEntityException, InvalidEntityException {
        // given
         
        EnvironmentDto envDto = new EnvironmentDto();
        envDto.setDescription("description");
        
        ClaudiaData claudiaData = mock(ClaudiaData.class);

        try {
       	 environmentResourceValidator.validateCreate(claudiaData, envDto, "vdc", systemPropertiesProvider);
       } catch (InvalidEnvironmentRequestException e) {
           fail("should not fail because the name is null ");
       }

    }
    

    public void shouldValidateNullDescriptionEnvironment() throws AlreadyExistEntityException, InvalidEntityException  {
        // given
         
        EnvironmentDto envDto = new EnvironmentDto();
        envDto.setName("name");
        
        ClaudiaData claudiaData = mock(ClaudiaData.class);

        try {
        	 environmentResourceValidator.validateCreate(claudiaData, envDto, "vdc", systemPropertiesProvider);
        } catch (InvalidEnvironmentRequestException e) {
            fail("should not fail because the descrption is null ");
        }
      
    }
    
    public void shouldValidateEmptyDescriptionEnvironment() throws AlreadyExistEntityException, InvalidEntityException  {
        // given
         
        EnvironmentDto envDto = new EnvironmentDto();
        envDto.setName("name");
        envDto.setDescription("");
        
        ClaudiaData claudiaData = mock(ClaudiaData.class);

        try {
        	 environmentResourceValidator.validateCreate(claudiaData, envDto, "vdc", systemPropertiesProvider);
        } catch (InvalidEnvironmentRequestException e) {
            fail("should not fail because the description is empty ");
        }
     
    }
    

    public void shouldValidateEmptyNameEnvironment() throws AlreadyExistEntityException, InvalidEntityException  {
        // given
         
        EnvironmentDto envDto = new EnvironmentDto();
        envDto.setName("");
        envDto.setDescription("descrption");
        
        ClaudiaData claudiaData = mock(ClaudiaData.class);
        try {
        	 environmentResourceValidator.validateCreate(claudiaData, envDto, "vdc", systemPropertiesProvider);
        } catch (InvalidEnvironmentRequestException e) {
            fail("should not fail because the name is empty ");
        }

    }
    

    public void shouldValidateStrangeCharacteresEnvironment() throws AlreadyExistEntityException, 
    InvalidEntityException  {
        // given
         
        EnvironmentDto envDto = new EnvironmentDto();
        envDto.setName("name.name");
        envDto.setDescription("descrption");
        
        ClaudiaData claudiaData = mock(ClaudiaData.class);
        try {
        	 environmentResourceValidator.validateCreate(claudiaData, envDto, "vdc", systemPropertiesProvider);
        } catch (InvalidEnvironmentRequestException e) {
            fail("should not fail because there is a '.' in the name ");
        }
       
     
    }
    
    public void shouldValidateNameTooLong() throws AlreadyExistEntityException, 
    InvalidEntityException  {
        // given
         
        EnvironmentDto envDto = new EnvironmentDto();
        envDto.setName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
        		"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        envDto.setDescription("descrption");
        
        ClaudiaData claudiaData = mock(ClaudiaData.class);
        try {
        	 environmentResourceValidator.validateCreate(claudiaData, envDto, "vdc", systemPropertiesProvider);
        } catch (InvalidEnvironmentRequestException e) {
            fail("should not fail because the name is too long");
        }
       
     
    }

 
}
