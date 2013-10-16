package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
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

}
