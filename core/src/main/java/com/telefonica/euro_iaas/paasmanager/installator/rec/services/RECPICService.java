/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.installator.rec.services;

import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReconfigurationException;

/**
 * @author jesus.movilla
 */
public interface RECPICService {

    // void createPICs(VM vm, String appId) throws ProductInstallatorException;
    void createPIC(String vapp, String appId, String picId, String vmName) throws ProductInstallatorException;

    void configurePIC(String vapp, String appId, String picId, String vmName) throws ProductReconfigurationException,
            ProductInstallatorException;

}
