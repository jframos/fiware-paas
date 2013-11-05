/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

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

    public String getMonitoringStatus(String fqn) {
        String state = MONITORING_ON;
        return state;
    }

    public void stopMonitoring(String fqn) {

    }

    public void startMonitoring(String fqn, String producto) {
        // TODO Auto-generated method stub

    }

}
