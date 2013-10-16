package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.BaseDAO;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ApplicationReleaseSearchCriteria;

/**
 * Defines the methods needed to persist ApplicationRelease objects.
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */
public interface ApplicationReleaseDao extends BaseDAO<ApplicationRelease, String> {

    /**
     * Find the application releases that match with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     */
    List<ApplicationRelease> findByCriteria(ApplicationReleaseSearchCriteria criteria);

}
