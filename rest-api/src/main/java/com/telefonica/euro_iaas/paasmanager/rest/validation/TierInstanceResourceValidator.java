/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.rest.validation;

import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidTierInstanceRequestException;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;

/**
 * @author bmmanso
 * 
 */
public interface TierInstanceResourceValidator {

	/**
	 * Validate the request to create an TierInstance
	 * 
	 * @param org
	 * @param vdc
	 * @param environmentInstance
	 * @param tierInstance
	 * @throws InvalidTierInstanceRequestException
	 * @throws InvalidEnvironmentRequestException
	 */
	public void validateScaleUpTierInstance(String org, String vdc,
			EnvironmentInstance environmentInstance, String tierInstance)
			throws InvalidTierInstanceRequestException,
			InvalidEnvironmentRequestException;
	
	public void validateScaleDownTierInstance(String org, String vdc,
			EnvironmentInstance environmentInstance, String tierInstance)
			throws InvalidTierInstanceRequestException,
			InvalidEnvironmentRequestException;

}
