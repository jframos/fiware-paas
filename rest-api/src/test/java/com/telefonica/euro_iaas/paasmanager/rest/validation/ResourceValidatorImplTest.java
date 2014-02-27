/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.validation;


import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException;

import static org.junit.Assert.fail;




public class ResourceValidatorImplTest {
	
	ResourceValidatorImpl resourceValidator ;

	
	@Before
	public void setUp (){
		resourceValidator = new ResourceValidatorImpl();	
	}

	public void shouldValidateNullName() throws AlreadyExistEntityException, InvalidEntityException {
        resourceValidator.validateName(null);
	 }
	
	public void shouldValidateNullDescription() throws AlreadyExistEntityException,InvalidEntityException {
        resourceValidator.validateDescription(null);
	 }
	@Test(expected = InvalidEntityException.class)
	public void shouldValidateEmptyName() throws AlreadyExistEntityException, InvalidEntityException {
        resourceValidator.validateName("");

	 }
	@Test(expected = InvalidEntityException.class)
	public void shouldValidateEmptyDescription() throws AlreadyExistEntityException, InvalidEntityException {
        resourceValidator.validateDescription("");

	 }
	@Test(expected = InvalidEntityException.class)
	public void shouldValidateStrangeCharacterinName() throws AlreadyExistEntityException,InvalidEntityException {
        resourceValidator.validateName("name.name");

	 }
	@Test(expected = InvalidEntityException.class)
	public void shouldValidateNameTooLong() throws AlreadyExistEntityException, InvalidEntityException {
        resourceValidator.validateName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

	 }
	@Test(expected = InvalidEntityException.class)
	public void shouldValidateDescriptionTooLong() throws AlreadyExistEntityException, InvalidEntityException {
        resourceValidator.validateDescription("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
		    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaajjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjdddddddddddddddddddddddddddddddddddddddjjjjjjjjaaaaaaaaaaaaaaaaaaa"+
		    "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"+
		    "ddddddddddddddddddddddddd");

	}
	
}

