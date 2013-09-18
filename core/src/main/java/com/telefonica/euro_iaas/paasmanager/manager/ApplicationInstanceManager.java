/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.manager;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.ApplicationTypeNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationInstance;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;

public interface ApplicationInstanceManager {

	   /**
     * Install a list of products in a given vm.
     * @param vm the vm where  instance will be running in
     * @param vdc the vdc where the instance will be installed
     * @param product the product to install
     * @param attributes the configuration
     *
     * @return the of installed product.
	 * @throws InvalidEntityException 
	 * @throws AlreadyExistsEntityException 
	 * @throws ApplicationTypeNotFoundException 
     */
    ApplicationInstance install(String vdc, 
    		EnvironmentInstance environmentInstance, 
    		ApplicationRelease application) throws 
    		ProductReleaseNotFoundException, InvalidEntityException, 
    		AlreadyExistsEntityException, ApplicationTypeNotFoundException; 
    }
