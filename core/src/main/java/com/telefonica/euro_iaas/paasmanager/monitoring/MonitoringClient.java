package com.telefonica.euro_iaas.paasmanager.monitoring;

import com.telefonica.euro_iaas.paasmanager.exception.MonitoringException;



public interface MonitoringClient {
	
	/**
    * 
    * @param fqn
    * @return vmResponse
	 * @throws MonitoringException 
    */
	String getMonitoringStatus(String fqn);
	
	/**
	* 
	* @param fqn
	* @return void
	*/
	public void stopMonitoring(String fqn);
    

    /**
     * 
     * @param fqn
     * @return void
     */
	public void startMonitoring(String fqn);
	

}
