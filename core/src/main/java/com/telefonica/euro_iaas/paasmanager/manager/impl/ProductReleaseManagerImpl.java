/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.OSDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.dao.sdc.ProductReleaseSdcDao;
import com.telefonica.euro_iaas.paasmanager.exception.SdcException;
import com.telefonica.euro_iaas.paasmanager.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import org.apache.log4j.Logger;

/**
 * @author jesus.movilla
 */
public class ProductReleaseManagerImpl implements ProductReleaseManager {

    private ProductReleaseDao productReleaseDao;
    private ProductReleaseSdcDao productReleaseSdcDao;
    private OSDao osDao;
    private static Logger log = Logger.getLogger(ProductReleaseManagerImpl.class);

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.manager.ProductManager#load(java .lang.String)
     */
    public ProductRelease load(String name) throws EntityNotFoundException {
        // return productReleaseDao.load(name);
        ProductRelease productRelease = null;
        String product = name.split("-")[0];
        String version = name.split("-")[1];

        try {
            productRelease = productReleaseDao.load(name);
        } catch (EntityNotFoundException e) {
            // Buscar en el SDC y si existe darlo de alta en el paas-manager
            try {
                ProductRelease pRelease = productReleaseSdcDao.load(product, version);

                List<OS> ssoo = new ArrayList<OS>();
                ssoo = pRelease.getSupportedOOSS();

                if (ssoo != null) {
                    List<OS> supportedOOSS = new ArrayList<OS>();
                    for (int j = 0; j < ssoo.size(); j++) {

                        OS so = null;
                        try {
                            so = osDao.load(ssoo.get(j).getOsType());
                        } catch (EntityNotFoundException e1) {
                            try {
                                so = osDao.create(ssoo.get(j));
                            } catch (InvalidEntityException e2) {
                                String msg = "Invalid Entity OSType: " + ssoo.get(j).getOsType();
                                log.warn(msg);
                                throw new EntityNotFoundException(OS.class, "osType", ssoo.get(j).getOsType());
                            } catch (AlreadyExistsEntityException e3) {
                                String msg = "Already Exist OSType: " + ssoo.get(j).getOsType();
                                log.warn(msg);
                                throw new EntityNotFoundException(OS.class, "osType", ssoo.get(j).getOsType());
                            }
                        }
                        supportedOOSS.add(so);
                    }
                    pRelease.setSupportedOOSS(supportedOOSS);
                }

                try {
                    productRelease = create(pRelease);
                } catch (InvalidEntityException e3) {
                    String msg = "Invalid Entity " + name;
                    log.warn(msg);
                    throw new EntityNotFoundException(ProductRelease.class, "name", name);
                } catch (AlreadyExistsEntityException e4) {
                    String msg = "Already Exist " + name;
                    log.warn(msg);
                    throw new EntityNotFoundException(ProductRelease.class, "name", name);
                }
            } catch (EntityNotFoundException e5) {
                String msg = "No product release NOT Found in SDC neither in PaasManager: " + name;
                log.warn(msg);
                throw new EntityNotFoundException(ProductRelease.class, "name", name);
            } catch (SdcException e6) {
                String msg = "SDC failure at loading ProductRelease " + name;
                log.warn(msg);
                throw new EntityNotFoundException(ProductRelease.class, "name", name);
            }
        }

        return productRelease;
    }

    public ProductRelease create(ProductRelease productRelease) throws InvalidEntityException,
            AlreadyExistsEntityException {
        log.debug("Create product release " + productRelease.getName() + " " + productRelease.getVersion());
        productRelease = productReleaseDao.create(productRelease);
        return productRelease;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.manager.ProductManager#findAll()
     */
    public List<ProductRelease> findAll() {
        // TODO Auto-generated method stub
        return productReleaseDao.findAll();
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.manager.ProductManager#load(com.
     * telefonica.euro_iaas.sdc.model.Product, java.lang.String)
     */
    public ProductRelease load(String productName, String productVersion) throws EntityNotFoundException {

        return productReleaseDao.load(productName + "-" + productVersion);
    }

    /**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

    /**
     * @param productReleaseSdcDao
     *            the productReleaseSdcDao to set
     */
    public void setProductReleaseSdcDao(ProductReleaseSdcDao productReleaseSdcDao) {
        this.productReleaseSdcDao = productReleaseSdcDao;
    }

    /**
     * @param osDao
     *            the osDao to set
     */
    public void setOsDao(OSDao osDao) {
        this.osDao = osDao;
    }

}
