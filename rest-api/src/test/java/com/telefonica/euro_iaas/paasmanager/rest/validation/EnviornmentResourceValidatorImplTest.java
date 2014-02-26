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
import static org.mockito.Matchers.anyString;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;

import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * Test class for validation of enviornment
 * @author henar
 *
 */
public class EnviornmentResourceValidatorImplTest {
	EnvironmentResourceValidatorImpl environmentResourceValidator ;
	EnvironmentManager environmentManager;
	ResourceValidator resourceValidator;
	SystemPropertiesProvider systemPropertiesProvider;
	
	@Before
	public void setUp () throws EntityNotFoundException, com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException {
		environmentResourceValidator = new EnvironmentResourceValidatorImpl();
		resourceValidator = mock(ResourceValidator.class);
        EnvironmentManager environmentManager=mock(EnvironmentManager.class);
        environmentResourceValidator.setResourceValidator(resourceValidator);
        EnvironmentDto envDto = new EnvironmentDto ();
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        when(environmentManager.load(anyString(),anyString())).thenThrow(new com.telefonica.euro_iaas.commons.dao.EntityNotFoundException(Environment.class, "dd", envDto.fromDto()));
        environmentResourceValidator.setEnvironmentManager(environmentManager);
		Mockito.doNothing().when(resourceValidator).validateName(anyString());
		Mockito.doNothing().when(resourceValidator).validateDescription(anyString());
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
    

    


    


 
}
