/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.async;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.impl.ApplicationInstanceAsyncManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationInstance;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.paasmanager.util.TaskNotificator;

/**
 * Unit test suite for ApplicationInstanceAsyncManagerImpl
 * 
 * @author jesus.movilla
 * 
 */
public class ApplicationInstanceAsyncManagerImplTest {

	/*
	 * private ApplicationInstanceManager applicationInstanceManager; private
	 * EnvironmentInstanceDao environmentInstanceDao; private
	 * SystemPropertiesProvider propertiesProvider; private TaskManager
	 * taskManager; private TaskNotificator taskNotificator;
	 * 
	 * private String vdc = "VDC"; private String callback = "callback"; private
	 * Task task;
	 * 
	 * @Before public void setUp() throws Exception { task= mock(Task.class);
	 * 
	 * taskNotificator = mock (TaskNotificator.class); taskManager = mock
	 * (TaskManager.class);
	 * when(taskManager.updateTask(any(Task.class))).thenReturn(task);
	 * 
	 * environmentInstanceDao = mock (EnvironmentInstanceDao.class);
	 * when(environmentInstanceDao.load(any(String.class))).thenReturn( new
	 * EnvironmentInstance());
	 * 
	 * applicationInstanceManager = mock (ApplicationInstanceManager.class);
	 * when(applicationInstanceManager.install(any(String.class),
	 * any(EnvironmentInstance.class), any(ApplicationRelease.class)))
	 * .thenReturn(new ApplicationInstance());
	 * 
	 * }
	 * 
	 * @Test public void InstallWhenEverithingIsOk() throws Exception {
	 * 
	 * ApplicationInstanceAsyncManagerImpl manager = new
	 * ApplicationInstanceAsyncManagerImpl();
	 * 
	 * manager.setApplicationInstanceManager(applicationInstanceManager);
	 * manager.setEnvironmentInstanceDao(environmentInstanceDao);
	 * manager.setPropertiesProvider(propertiesProvider);
	 * manager.setTaskManager(taskManager);
	 * manager.setTaskNotificator(taskNotificator);
	 * 
	 * manager.install(vdc, "environmentInstanceName", new ApplicationRelease(),
	 * task, callback); }
	 */
}
