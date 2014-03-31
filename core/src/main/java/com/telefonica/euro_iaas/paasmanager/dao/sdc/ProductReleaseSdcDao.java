/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.sdc;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.SdcException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;

/**
 * @author jesus.movilla
 */
public interface ProductReleaseSdcDao {

    /**
     * Load all product Release present in the SDC.
     * 
     * @return
     * @throws SdcException
     */
    List<ProductRelease> findAll(String token, String tenant) throws SdcException;

    /**
     * Load a ProductRelease from the SDC by Name.
     * 
     * @param name
     * @return
     * @throws EntityNotFoundException
     */
    ProductRelease load(String product, String version,  ClaudiaData data) throws EntityNotFoundException, SdcException;

    /**
     * Load all products from SDC.
     * @return a list with the product names present in SDC
     * @throws SdcException
     */
    List<String> findAllProducts(String token, String tenant) throws SdcException;
    
    /**
     * Load all productRelease of a product from SDC.
     * @return a list with the product names present in SDC
     * @throws SdcException
     */
    List<ProductRelease> findAllProductReleasesOfProduct(String pName, String token, String tenant) throws SdcException;
}
