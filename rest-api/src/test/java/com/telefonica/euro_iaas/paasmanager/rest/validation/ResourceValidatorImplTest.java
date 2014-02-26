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
public class ResourceValidatorImplTest {
	ResourceValidatorImpl resourceValidator;
	
	@Before
    public void setUp () {
		resourceValidator = new ResourceValidatorImpl ();
	}

	@Test
    public void shouldValidateNullName() throws AlreadyExistEntityException {
        // given
        try {
        	resourceValidator.validateName(null);
        	fail("should fail because the name is null ");
       } catch (InvalidEntityException e) {
           
       }

    }
    
	@Test
    public void shouldValidateNullDescription() throws AlreadyExistEntityException  {
        // given
        try {
          	resourceValidator.validateDescription(null);
          	fail("should fail because the description is null ");
        } catch (InvalidEntityException e) {
           
        }
      
    }
	@Test
    public void shouldValidateEmptyDescription() throws AlreadyExistEntityException  {
        
    	try {
          	resourceValidator.validateDescription("");
          	fail("should fail because the description is empty ");
        } catch (InvalidEntityException e) {
            
        }
     
    }
    
	@Test
    public void shouldValidateEmptyName() throws AlreadyExistEntityException  {
        // given
    	try {
          	resourceValidator.validateName("");
          	fail("should fail because the description is empty ");
        } catch (InvalidEntityException e) {
            
        }

    }
    
	@Test
    public void shouldValidateStrangeCharacteresEnvironment() throws AlreadyExistEntityException
     {
        // given
    	try {
          	resourceValidator.validateName("ddddd.d");
          	fail("should fail because the name has strange characteres ");
        } catch (InvalidEntityException e) {
            
        }
       
     
    }
	@Test
    public void shouldValidateNameTooLong() throws AlreadyExistEntityException, 
    InvalidEntityException  {
        // given
    	try {
          	resourceValidator.validateName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
          	fail("should fail because the name is too long ");
        } catch (InvalidEntityException e) {
            
        }
       
     
    }
	@Test
    public void shouldValidateDescriptionTooLong() throws AlreadyExistEntityException, 
    InvalidEntityException  {
        // given
    	try {
          	resourceValidator.validateDescription("ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"
          			+ "dddddddddaaaaaaaaaaaaaddddddddddddddddddddddddddddddddddddddddddddddddaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
          			+ "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
          	fail("should fail because the description is too long ");
        } catch (InvalidEntityException e) {
            
        }
       
     
    }

 
}
