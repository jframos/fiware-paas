/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.installator.rec.services;

import org.restlet.data.Response;

import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReconfigurationException;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;

/**
 * @author jesus.movilla
 * 
 */
public interface RECVMService {

	void createVM(String recVapp, String recVMname, String appId)
			throws ProductInstallatorException;

	void configureVM(String recVapp, String recVMname, String appId)
			throws ProductReconfigurationException;
}
