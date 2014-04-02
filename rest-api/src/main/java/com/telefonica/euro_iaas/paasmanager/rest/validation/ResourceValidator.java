/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.rest.validation;


import com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException;



/**
 * Validator.
 * @author henar
 *
 */
public interface ResourceValidator {


	
	/**
	 * It validates a name.
	 * @param name
	 * @throws InvalidEntityException
	 */
	void validateName(String name) throws InvalidEntityException;
	/**
	 * It validates a description.
	 * @param name
	 * @throws InvalidEntityException
	 */
	void validateDescription(String name) throws InvalidEntityException;
}
