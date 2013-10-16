/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.manager.async;

import com.telefonica.euro_iaas.paasmanager.model.ApplicationInstance;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;

public interface ApplicationInstanceAsyncManager {

	/**
	 * Install an applicationRelease on an already existent EnvironmentInstance
	 * 
	 * @param org
	 *            the org where the instance will be installed
	 * @param vdc
	 *            the vdc where the instance will be installed
	 * @param environmentInstanceName
	 *            on which applicationRelease is going to be deployed
	 * @param applicationRelease
	 *            to be deployed
	 * @param task
	 *            the task which contains the information about the async
	 *            execution
	 * @param callback
	 *            if not empty, contains the url where the result of the
	 *            execution will be sent
	 */
	void install(String org, String vdc, String environmentInstanceName,
			ApplicationRelease applicationRelease, Task task, String callback);

	void uninstall(String org, String vdc, String envInstance,
			String appInstance, Task task, String callback);

}
