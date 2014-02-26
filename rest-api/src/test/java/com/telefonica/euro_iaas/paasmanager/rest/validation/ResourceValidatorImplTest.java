/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.validation;

import org.junit.Before;

import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException;

import static org.junit.Assert.fail;




public class ResourceValidatorImplTest {
	
	ResourceValidatorImpl resourceValidator ;

	
	@Before
	public void setUp (){
		resourceValidator = new ResourceValidatorImpl();	
	}

	public void shouldValidateNullName() throws AlreadyExistEntityException {
        try {
        	resourceValidator.validateName(null);
        	fail("should fail because the name is null ");
		} catch (InvalidEntityException e) {
		    
		}
	 }
	
	public void shouldValidateNullDescription() throws AlreadyExistEntityException {
        try {
        	resourceValidator.validateDescription(null);
        	fail("should fail because the description is null ");
		} catch (InvalidEntityException e) {
		    
		}
	 }
	
	public void shouldValidateEmptyName() throws AlreadyExistEntityException {
        try {
        	resourceValidator.validateName("");
        	fail("should fail because the name is empty");
		} catch (InvalidEntityException e) {
		    
		}
	 }
	
	public void shouldValidateEmptyDescription() throws AlreadyExistEntityException {
        try {
        	resourceValidator.validateDescription("");
        	fail("should fail because the description is empty");
		} catch (InvalidEntityException e) {
		    
		}
	 }
	
	public void shouldValidateStrangeCharacterinName() throws AlreadyExistEntityException {
        try {
        	resourceValidator.validateName("name.name");
        	fail("should fail because the name has strange characters");
		} catch (InvalidEntityException e) {
		    
		}
	 }
	
	public void shouldValidateStrangeCharacterinDescription() throws AlreadyExistEntityException {
        try {
        	resourceValidator.validateDescription("name.name");
        	fail("should fail because the description has strange characters");
		} catch (InvalidEntityException e) {
		    
		}
	 }
	
	public void shouldValidateNameTooLong() throws AlreadyExistEntityException {
        try {
        	resourceValidator.validateName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        	fail("should fail because the name is too long");
		} catch (InvalidEntityException e) {
		    
		}
	 }
	
	public void shouldValidateDescriptionTooLong() throws AlreadyExistEntityException {
        try {
        	resourceValidator.validateDescription("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
		           "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        	fail("should fail because the description is too long");
		} catch (InvalidEntityException e) {
		    
		}
	 }
	
}