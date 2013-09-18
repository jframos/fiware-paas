package com.telefonica.euro_iaas.paasmanager.rest.validation;

import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationInstanceDto;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.paasmanager.exception.EnvironmentInstanceNotFoundException;
/**
 * Defines the methods to validate the selected operation is valid for the given
 * Application InstaceResource.
 * @author Jesus M. Movilla
 *
 */
public interface ApplicationInstanceResourceValidator {

    /**
     * Verify if the ApplicationInstanceDto is valid  could be inserted
     * @param ApplicationInstanceDto 
     * @throws  EnvironmentInstanceNotFoundException, 
     * InvalidApplicationReleaseException
     */
    void validateInstall (ApplicationInstanceDto applicationInstanceDto)
    	throws InvalidApplicationReleaseException, 
    		EnvironmentInstanceNotFoundException;
}
