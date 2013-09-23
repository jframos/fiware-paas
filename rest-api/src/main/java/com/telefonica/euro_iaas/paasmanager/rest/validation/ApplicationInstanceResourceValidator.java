package com.telefonica.euro_iaas.paasmanager.rest.validation;

import com.telefonica.euro_iaas.paasmanager.exception.ApplicationInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.paasmanager.exception.EnvironmentInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationReleaseDto;

/**
 * Defines the methods to validate the selected operation is valid for the given
 * Application InstaceResource.
 * 
 * @author Jesus M. Movilla
 * 
 */
public interface ApplicationInstanceResourceValidator {

	/**
	 * Verify if the ApplicationInstanceDto is valid could be inserted
	 * 
	 * @param ApplicationInstanceDto
	 * @throws EnvironmentInstanceNotFoundException
	 *             , InvalidApplicationReleaseException
	 */
	void validateInstall(String vdc, String environmentInstance,
			ApplicationReleaseDto applicationReleaseDto)
			throws InvalidApplicationReleaseException,
			ApplicationInstanceNotFoundException;

	/**
	 * Verify if the extendedovf is valid could be inserted
	 * 
	 * @param extendedovf
	 * @throws InvalidOVFException
	 */
	void validateInstall(String extendedovf) throws InvalidOVFException;
}
