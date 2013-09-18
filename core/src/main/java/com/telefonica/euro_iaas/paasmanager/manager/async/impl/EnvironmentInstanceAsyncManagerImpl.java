package com.telefonica.euro_iaas.paasmanager.manager.async.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.EnvironmentInstanceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.paasmanager.util.TaskNotificator;

public class EnvironmentInstanceAsyncManagerImpl implements
		EnvironmentInstanceAsyncManager {
	
	private static Logger LOGGER =
		Logger.getLogger(ProductInstanceAsyncManagerImpl.class.getName());
	private ProductInstanceManager productInstanceManager;
	//private EnvironmentInstanceManager environmentInstanceManager;
	private TaskManager taskManager;
	private SystemPropertiesProvider propertiesProvider;
	private TaskNotificator taskNotificator;
	private EnvironmentManager environmentManager;
	    
	@Override
	public void create(String vdc, EnvironmentInstance environmentInstance, Task task,
			String callback) throws EntityNotFoundException {
		
		//validar que el el objeto viene con el name.
		Environment environment = environmentManager.load(
				environmentInstance.getEnvironment().getName());
		//load environment (environment should be in the database) if not exception
		//install the productInstances included in the environent->tiers
		
		//List<Tier> tiers = environment.getTiers();
		
		//List<VM> vms = 
				
		/*List<TierInstance> tierInstances = environment.getTierInstances();
		List<ProductInstance> productInstances = new ArrayList<ProductInstance> ();
		for (int i=0; i< tierInstances.size(); i++){
			List<ProductInstance> productInstancesTier 
				= tierInstances.get(i).getProductInstances();
			for(int j= 0; j < productInstancesTier.size(); j++){
				productInstances.add(productInstancesTier.get(j));				
			}
		}*/
		
		//install sequentially productInstances
		// TODO Auto-generated method stub

	}

	@Override
	public EnvironmentInstance load(Long id) throws EntityNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy(EnvironmentInstance environmentInstance, Task task,
			String callback) {
		// TODO Auto-generated method stub

	}

    //////////// I.O.C ////////////

    /**
     * @param productInstanceManager
     *            the productInstanceManager to set
     */
    public void setProductInstanceManager(
            ProductInstanceManager productInstanceManager) {
        this.productInstanceManager = productInstanceManager;
    }

    /**
     * @param taskManager
     *            the taskManager to set
     */
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(
            SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    /**
     * @param taskNotificator the taskNotificator to set
     */
    public void setTaskNotificator(TaskNotificator taskNotificator) {
        this.taskNotificator = taskNotificator;
    }
    
    /**
     * @param environmentManager
     *            the environmentManager to set
     */
    public void setEnvironmentManager(
            EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

}
