/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.manager;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;

/**
 * @author jesus.movilla
 *
 */
public interface ProductReleaseManager {

  /**
     * Find the ProductRelease using the given id.
     * @param name the product identifier
     * @return the productRelease
     * @throws EntityNotFoundException if the product instance does not exists
     */
	ProductRelease load(String name) throws EntityNotFoundException;

    /**
     * Retrieve all ProductRelease created in the system.
     * @return the existent product instances.
     */
    List<ProductRelease> findAll();

   /**
     * Retrieve a Product release for a given product and version.
     * @param product the product
     * @param version the version
     * @return the product release that match with the criteria
     * @throws EntityNotFoundException if the product release does not exists
     */
    ProductRelease load(String productName, String productVersion)
        throws EntityNotFoundException;

   
}
