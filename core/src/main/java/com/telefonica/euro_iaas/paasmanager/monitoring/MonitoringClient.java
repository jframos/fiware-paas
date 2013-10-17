/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.monitoring;

import com.telefonica.euro_iaas.paasmanager.exception.MonitoringException;

public interface MonitoringClient {

    /**
     * @param fqn
     * @return vmResponse
     * @throws MonitoringException
     */
    String getMonitoringStatus(String fqn);

    /**
     * @param fqn
     * @return void
     */
    public void stopMonitoring(String fqn);

    /**
     * @param fqn
     * @return void
     */
    public void startMonitoring(String fqn, String producto);

}
