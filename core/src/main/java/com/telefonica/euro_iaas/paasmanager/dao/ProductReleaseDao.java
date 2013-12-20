/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductReleaseSearchCriteria;

/**
 * Defines the methods needed to persist ProductRelease objects.
 * 
 * @author Jesus M. Movilla
 */
public interface ProductReleaseDao extends BaseDAO<ProductRelease, String> {
    /**
     * Find the product releases that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ProductRelease> findByCriteria(ProductReleaseSearchCriteria criteria);
    
    /**
     * 
     * @param name
     * @return
     */
    ProductRelease loadProductReleaseWithMetadata(String name) throws EntityNotFoundException;

}
