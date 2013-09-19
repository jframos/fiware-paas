/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;

/**
 * @author jesus.movilla
 *
 */
public class ProductReleaseManagerImpl implements ProductReleaseManager {

	private ProductReleaseDao productReleaseDao;
	
	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.manager.ProductManager#load(java.lang.String)
	 */
	public ProductRelease load(String name) throws EntityNotFoundException {
		return productReleaseDao.load(name);
	}

	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.manager.ProductManager#findAll()
	 */
	public List<ProductRelease> findAll() {
		// TODO Auto-generated method stub
		return productReleaseDao.findAll();
	}

	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.manager.ProductManager#load(com.telefonica.euro_iaas.sdc.model.Product, java.lang.String)
	 */
	public ProductRelease load(String productName, String productVersion)
			throws EntityNotFoundException {
		
		return productReleaseDao.load(productName +"-"+ productVersion);
	}

	/**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }
}
