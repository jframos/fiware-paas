package com.telefonica.euro_iaas.paasmanager.manager.async;


import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;


public interface TierInstanceAsyncManager {


	

	/**
	 *  Create a new Tier Instance
	 * 
	 * @param claudiaData
	 * @param tierInstance
	 * @param envInstance
	 * @param task
	 * @param callback
	 */
	public void create (ClaudiaData claudiaData, TierInstance tierInstance, 
			EnvironmentInstance envInstance, Task task, String callback);

	/**
	 * Delete a Tier Instance
	 * @param claudiaData
	 * @param tierInstance
	 * @param task
	 * @param callback
	 */
	public void delete(ClaudiaData claudiaData, TierInstance tierInstance,
			EnvironmentInstance envInstance,Task task, String callback);

	/**
	 * Reconfigure a Tier Instance
	 * @param claudiaData
	 * @param tierInstance
	 * @param envInstance
	 * @param task
	 * @param callback
	 */
	public void update(ClaudiaData claudiaData, TierInstance tierInstance,
			EnvironmentInstance envInstance, Task task, String callback);
	
   /**
    * Delete an Instance of an Environment
    * @param environmentInstance
    * @param org
    * @param vdc
    * @param task
    * @param callback
    */
 //   void destroy(EnvironmentInstance environmentInstance, String org, String vdc, 
    	//	Task task, String callback);
}
