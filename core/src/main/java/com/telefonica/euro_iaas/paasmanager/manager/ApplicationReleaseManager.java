/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ApplicationReleaseSearchCriteria;

/**
 * @author jesus.movilla
 */
public interface ApplicationReleaseManager {

    /**
     * Find the ApplicationRelease using the given id.
     * 
     * @param name
     *            the product identifier
     * @return the productRelease
     * @throws EntityNotFoundException
     *             if the product instance does not exists
     */
    ApplicationRelease load(String name) throws EntityNotFoundException;

    /**
     * Retrieve all ApplicationRelease created in the system.
     * 
     * @return the existent product instances.
     */
    List<ApplicationRelease> findAll();

    /**
     * Retrieve the applicationReselease followiong the established criteria
     * 
     * @param criteria
     * @return
     */
    List<ApplicationRelease> findByCriteria(ApplicationReleaseSearchCriteria criteria);

}
