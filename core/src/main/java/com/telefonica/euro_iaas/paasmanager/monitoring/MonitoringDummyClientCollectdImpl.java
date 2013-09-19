package com.telefonica.euro_iaas.paasmanager.monitoring;

import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public class MonitoringDummyClientCollectdImpl implements MonitoringClient {

	private final static String MONITORING_ON = "ON";
	private SystemPropertiesProvider systemPropertiesProvider;
	
    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }
	
	public String getMonitoringStatus(String fqn){
		String state = MONITORING_ON;
		return state;
	}

	public void stopMonitoring(String fqn){

	}

	public void startMonitoring(String fqn){

	}
	

}
