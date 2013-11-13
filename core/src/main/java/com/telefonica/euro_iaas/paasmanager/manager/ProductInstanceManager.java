/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;
import java.util.Set;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidProductInstanceRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReconfigurationException;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductInstanceSearchCriteria;

/**
 * Defines the operations the system shall be able to do with Products
 * 
 * @author Jesus M. Movilla
 */
public interface ProductInstanceManager {

    /**
     * Install a list of products in a given vm.
     * 
     * @param vm
     * @param org
     * @param vdc
     * @param product
     * @param set
     * @return
     * @throws ProductInstallatorException
     * @throws InvalidEntityException
     * @throws NotUniqueResultException
     * @throws InvalidEntityException
     */
    ProductInstance install(TierInstance tierInstance, ClaudiaData claudiaData, String envName, ProductRelease product,
            Set<Attribute> set) throws ProductInstallatorException, InvalidProductInstanceRequestException,
            NotUniqueResultException, InvalidEntityException;

    /**
     * Configure an installed product
     * 
     * @param productInstance
     *            the installed product to configure
     * @param configuration
     *            the configuration parameters.
     * @return the configured product.
     * @throws FSMViolationException
     *             if try to make a forbidden transition
     */
    /*
     * ProductInstance configure(ProductInstance productInstance, List<Attribute> configuration) throws
     * NodeExecutionException, FSMViolationException ;
     */

    /**
     * Upgrade a ProductInstance
     * 
     * @param productInstance
     *            the installed product to upgrade
     * @param productRelease
     *            the productRelease to upgrade to.
     * @return the productInstance upgraded.
     * @throws NotTransitableException
     *             if the selected version is not compatible with the installed product
     * @throws NodeExecutionException
     *             if any error happen during the upgrade in node
     * @throws FSMViolationException
     *             if try to make a forbidden transition
     * @throws ApplicationIncompatibleException
     *             if any application which is installed on the upgraded product is not compatible with the new version
     */
    /*
     * ProductInstance upgrade(ProductInstance productInstance, ProductRelease productRelease) throws
     * NotTransitableException, NodeExecutionException, ApplicationIncompatibleException, FSMViolationException;
     */

    /**
     * Uninstall a previously installed product.
     * 
     * @param productInstance
     *            the candidate to uninstall
     * @throws NodeExecutionException
     *             if any error happen during the uninstallation in node
     * @throws ApplicationInstalledException
     *             if the product has some applications which depend on it
     * @throws FSMViolationException
     *             if try to make a forbidden transition
     */
    void uninstall(ProductInstance productInstance) throws ProductInstallatorException;

    // throws NodeExecutionException, ApplicationInstalledException,
    // FSMViolationException;

    /**
     * Find the ProductInstance using the given id.
     * 
     * @param vdc
     *            the vdc
     * @param name
     *            the productInstance name
     * @return the productInstance
     * @throws EntityNotFoundException
     *             if the product instance does not exists
     */
    ProductInstance load(String vdc, String name) throws EntityNotFoundException;

    ProductInstance load(String name) throws EntityNotFoundException;

    /**
     * Find the ProductInstance that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the productInstance
     * @throws EntityNotFoundException
     *             if the product instance does not exists
     * @throws NotUniqueResultException
     *             if there are more than a product that match with the given criteria
     */
    ProductInstance loadByCriteria(ProductInstanceSearchCriteria criteria) throws EntityNotFoundException,
            NotUniqueResultException;

    /**
     * Retrieve all ProductInstance created in the system.
     * 
     * @return the existent product instances.
     */
    List<ProductInstance> findAll();

    /**
     * Find the product instances that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ProductInstance> findByCriteria(ProductInstanceSearchCriteria criteria);

    /**
     * Updates a product instance and persist it in the database∫
     * 
     * @param productInstance
     * @return
     * @throws InvalidProductInstanceRequestException
     */
    // ProductInstance update(ProductInstance productInstance);

    ProductInstance create(ProductInstance productInstance) throws InvalidEntityException,
            AlreadyExistsEntityException, InvalidProductInstanceRequestException;

    void remove(ProductInstance productInstance);

    void configure(ClaudiaData claudiaData, ProductInstance productInstance, List<Attribute> properties)
            throws InvalidEntityException, AlreadyExistsEntityException, ProductInstallatorException,
            EntityNotFoundException, ProductReconfigurationException;

}
