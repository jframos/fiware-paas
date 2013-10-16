/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.ApplicationTypeNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationInstance;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ApplicationInstanceSearchCriteria;

public interface ApplicationInstanceManager {

    /**
     * Install a list of applications in a given environment
     * 
     * @param org
     *            the org where the instance will be installed
     * @param vdc
     *            the vdc where the instance will be installed
     * @param environmentInstance
     *            the environmentInstance where the instance will be installed
     * @param application
     *            the application to install
     * @return the of installed product.
     * @throws InvalidEntityException
     * @throws AlreadyExistsEntityException
     * @throws ApplicationTypeNotFoundException
     */
    ApplicationInstance install(String org, String vdc, EnvironmentInstance environmentInstance,
            ApplicationRelease application) throws ProductReleaseNotFoundException, InvalidEntityException,
            AlreadyExistsEntityException, ApplicationTypeNotFoundException, ProductInstallatorException;

    /**
     * Find all ApplicationInstances
     * 
     * @return the applicationInstances
     */
    List<ApplicationInstance> findAll();

    /**
     * Find the application instances that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ApplicationInstance> findByCriteria(ApplicationInstanceSearchCriteria criteria);

    /**
     * Load an applicationInstance
     * 
     * @param vdc
     * @param name
     * @return the applicationInstance
     */
    ApplicationInstance load(String vdc, String name) throws EntityNotFoundException;

    /**
     * UnInstall an list in a given environment
     * 
     * @param org
     *            the org where the instance will be uninstalled
     * @param vdc
     *            the vdc where the instance will be uninstalled
     * @param environmentInstanceName
     *            the environmentInstance where the instance will be installed
     * @param application
     *            the application to install
     * @return the of installed product.
     * @throws InvalidEntityException
     * @throws AlreadyExistsEntityException
     * @throws ApplicationTypeNotFoundException
     */
    void uninstall(String org, String vdc, EnvironmentInstance environmentInstanceName,
            ApplicationInstance applicationInstance) throws ProductInstallatorException;

}
