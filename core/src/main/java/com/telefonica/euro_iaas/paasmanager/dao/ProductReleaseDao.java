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
     * @param criteria the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ProductRelease> findByCriteria(ProductReleaseSearchCriteria criteria);

    /**
     * Find the product release that match with the given criteria.
     * @param productName the productName
     * @param version the release version
     * @return the element that match with the criteria.
     * @throws EntityNotFoundException
     *                      if there is no product with the given version
     */
    ProductRelease load(String productName, String version)
        throws EntityNotFoundException;

}
