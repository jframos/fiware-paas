/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidProductInstanceRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReconfigurationException;
import com.telefonica.euro_iaas.paasmanager.installator.ProductInstallator;
import com.telefonica.euro_iaas.paasmanager.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductInstanceSearchCriteria;

public class ProductInstanceManagerImpl implements ProductInstanceManager {

    private ProductInstanceDao productInstanceDao;
    private ProductInstallator productInstallator;
    private ProductReleaseManager productReleaseManager;

    private static Logger log = Logger.getLogger(ProductInstanceManagerImpl.class);

    public ProductInstance install(TierInstance tierInstance, ClaudiaData claudiaData, String envName,
            ProductRelease productRelease, Set<Attribute> attributes) throws ProductInstallatorException,
            InvalidProductInstanceRequestException, NotUniqueResultException, InvalidEntityException {
        log.debug("Installing software " + productRelease.getProduct() + " in tier Instance " + tierInstance.getName()
                + " in vdc " + claudiaData.getVdc());

        ProductInstance productInstance = productInstallator.install(claudiaData, envName, tierInstance,
                productRelease, attributes);
        if (productInstance.getVdc() == null) {
            productInstance.setVdc(claudiaData.getVdc());
        }
        try {
            productInstance = create(productInstance);
        } catch (AlreadyExistsEntityException e) {
            log.error("The product instance " + productInstance.getName() + " already exists " + e.getMessage());
            throw new InvalidProductInstanceRequestException("Error to i" + e.getMessage(), e);
        }

        return productInstance;
    }

    public void uninstall(ProductInstance productInstance) throws ProductInstallatorException {
        log.debug("UnInstalling software " + productInstance.getProductRelease().getProduct());
        productInstallator.uninstall(productInstance);

    }

    public ProductInstance load(String vdc, String name) throws EntityNotFoundException {
        return productInstanceDao.load(name);

    }

    public ProductInstance loadByCriteria(ProductInstanceSearchCriteria criteria) throws EntityNotFoundException,
            NotUniqueResultException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<ProductInstance> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<ProductInstance> findByCriteria(ProductInstanceSearchCriteria criteria) {
        return productInstanceDao.findByCriteria(criteria);
    }

    public ProductInstance create(ProductInstance productInstance, String tierInstance) throws InvalidEntityException,
            AlreadyExistsEntityException {
        ProductRelease productRelease = productInstance.getProductRelease();
        if (productInstance.getName() == null)
            productInstance.setName(tierInstance + "_" + productRelease.getProduct() + "_"
                    + productRelease.getVersion());

        if (productRelease.getId() == null)
            try {
                productRelease = productReleaseManager.load(productRelease.getProduct() + "-"
                        + productRelease.getVersion());
            } catch (EntityNotFoundException e) {
                // TODO Auto-generated catch block
                throw new InvalidEntityException("Error to load the product release for persist the product Instance "
                        + productInstance.getName() + " : " + e.getMessage());
            }

        if (productInstance.getId() == null)
            try {
                productInstanceDao.create(productInstance);
            } catch (InvalidEntityException e) {
                // TODO Auto-generated catch block
                throw new InvalidEntityException("Error to persist the product Instance " + productInstance.getName()
                        + " : " + e.getMessage());
            } catch (AlreadyExistsEntityException e) {
                // TODO Auto-generated catch block
                throw new AlreadyExistsEntityException("Error to persist the product Instance "
                        + productInstance.getName() + " : " + e.getMessage());
            }
        return productInstance;

    }

    // //////////// I.O.C /////////////
    /**
     * @param productInstanceDao
     *            the productInstanceDao to set
     */
    public void setProductInstanceDao(ProductInstanceDao productInstanceDao) {
        this.productInstanceDao = productInstanceDao;
    }

    /**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    public void setProductReleaseManager(ProductReleaseManager productReleaseManager) {
        this.productReleaseManager = productReleaseManager;
    }

    /**
     * @param productInstallator
     *            the productInstallator to set
     */
    public void setProductInstallator(ProductInstallator productInstallator) {
        this.productInstallator = productInstallator;
    }

    public ProductInstance load(String name) throws EntityNotFoundException {
        return productInstanceDao.load(name);
    }

    public void remove(ProductInstance productInstance) {
        productInstanceDao.remove(productInstance);

    }

    public ProductInstance create(ProductInstance productInstance) throws InvalidEntityException,
            AlreadyExistsEntityException, InvalidProductInstanceRequestException {
        ProductRelease productRelease = null;
        try {

            productRelease = productReleaseManager.load(productInstance.getProductRelease().getProduct() + "-"
                    + productInstance.getProductRelease().getVersion());
            productInstance.setProductRelease(productRelease);
        } catch (EntityNotFoundException e) {
            String errorMessage = "The Product Release Object " + productRelease.getId() + " is " + "NOT valid";
            throw new InvalidProductInstanceRequestException(errorMessage);
        }

        try {
            productInstance = productInstanceDao.load(productInstance.getName());
        } catch (EntityNotFoundException e) {
            try {

                productInstance = productInstanceDao.create(productInstance);

            } catch (InvalidEntityException iee) {
                String errorMessage = "The Product Instance Object " + productInstance.getName() + " is " + "NOT valid";
                throw new InvalidProductInstanceRequestException(errorMessage);
            } catch (AlreadyExistsEntityException aee) {
                String errorMessage = "The ProductRelease Object " + productInstance.getName()
                        + " already exist in Database";
                throw new InvalidProductInstanceRequestException(errorMessage);
            } catch (Exception aee) {
                String errorMessage = "The ProductRelease Object " + productInstance.getName()
                        + " already exist in Database " + aee.getMessage();
                throw new InvalidProductInstanceRequestException(errorMessage);
            }
        }

        return productInstance;
    }

    public void configure(ClaudiaData claudiaData, ProductInstance productInstance, List<Attribute> properties)
            throws InvalidEntityException, AlreadyExistsEntityException, ProductInstallatorException,
            EntityNotFoundException, ProductReconfigurationException {
        log.debug("Configuring software " + productInstance.getProductRelease().getProduct() + " "
                + productInstance.getName());
        productInstallator.configure(claudiaData, productInstance, properties);

    }

}
