package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentSearchCriteria;

public interface EnvironmentManager {

	 /**
     * Create an environent
     * @param name
     * @param tiers
     * @param environmentType
     * @return the of installed product.
     */
    Environment create(Environment environment) throws 
			InvalidEnvironmentRequestException;

    /**
     * Configure an installed product
     * @param productInstance the installed product to configure
     * @param configuration the configuration parameters.
     * @return the configured product.
     * @throws FSMViolationException if try to make a forbidden transition
     */
    /*ProductInstance configure(ProductInstance productInstance,
            List<Attribute> configuration)
        throws NodeExecutionException, FSMViolationException ;*/

    /**
     * Upgrade a ProductInstance
     * @param productInstance the installed product to upgrade
     * @param productRelease the productRelease to upgrade to.
     * @return the productInstance upgraded.
     * @throws NotTransitableException if the selected version is not compatible
     * with the installed product
     * @throws NodeExecutionException if any error happen during the
     *  upgrade in node
     * @throws FSMViolationException if try to make a forbidden transition
     * @throws ApplicationIncompatibleException if any application which is
     * installed on the upgraded product is not compatible with the new version
     */
   /* ProductInstance upgrade(ProductInstance productInstance,
            ProductRelease productRelease) throws NotTransitableException,
            NodeExecutionException, ApplicationIncompatibleException,
            FSMViolationException;*/

    /**
     * Destroy a previously creted environment.
     * @param environment the candidate to environment
     */
    void destroy(Environment environment) throws InvalidEntityException;
        //throws NodeExecutionException, ApplicationInstalledException,
        //FSMViolationException;

    /**
     * Find the Environment using the given name.
     * @param name the name
     * @return the environment
     * @throws EntityNotFoundException if the product instance does not exists
     */
    Environment load(String name) throws EntityNotFoundException;
    
    Environment update(Environment name) throws EntityNotFoundException, InvalidEntityException;

   /**
     * Retrieve all Environment created in the system.
     * @return the existent environments.
     */
    List<Environment> findAll();

    /**
     * Find the environments that match with the given criteria.
     * @param criteria the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<Environment> findByCriteria(EnvironmentSearchCriteria criteria);

    /**
     * Updates a product instance and persist it in the database
     * @param productInstance
     * @return
     */
    //ProductInstance update(ProductInstance productInstance);
}
