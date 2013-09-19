package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.exception.NotUniqueResultException;

/**
 * Defines the methods needed to persist Instance objects.
 *
 * @author Jesus M. Movilla
 */
public interface ProductInstanceDao extends BaseDAO<ProductInstance, String> {
    /**
     * Find the environment that matches with the given criteria.
     * @param criteria the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ProductInstance> findByCriteria(ProductInstanceSearchCriteria criteria);

    /**
     * Find the unique product instances that match with the given criteria.
     * @param criteria the search criteria
     * @return the element that match with the criteria.
     * @throws NotUniqueResultException if the number of elements that match
     *  with the criteria is different to one.
     */
    ProductInstance findUniqueByCriteria(ProductInstanceSearchCriteria criteria)
    throws NotUniqueResultException;
    
   
}