/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util;

import java.sql.Connection;

import com.telefonica.euro_iaas.paasmanager.exception.OpenStackSynchronizationException;

/**
 * @author jesus.movilla
 */
public interface OpenStackSync {

    /**
     * Synchronize the secGroups and Instances of PaasManager and OpenStack
     * 
     * @param claudiaData
     * @param onecycle
     *            (true if the thrad is just executed one cycle)
     * @throws OpenStackSynchronizationException
     */
    void syncronize(Connection connection, boolean onecycle) throws OpenStackSynchronizationException;

}
