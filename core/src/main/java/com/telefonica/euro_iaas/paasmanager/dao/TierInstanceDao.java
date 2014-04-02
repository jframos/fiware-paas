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
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierInstanceSearchCriteria;

/**
 * Defines the methods needed to persist EnvironmentInstace objects.
 * 
 * @author Jesus M. Movilla
 */
public interface TierInstanceDao extends BaseDAO<TierInstance, String> {
    /**
     * Find the environment that matches with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     * @throws EntityNotFoundException
     */
    List<TierInstance> findByCriteria(TierInstanceSearchCriteria criteria) throws EntityNotFoundException;

    /**
     * @param tierInstanceId
     * @return
     * @throws EntityNotFoundException
     */
    TierInstance findByTierInstanceId(Long tierInstanceId) throws EntityNotFoundException;

    /**
     * @param tierInstanceName
     * @return
     * @throws EntityNotFoundException
     */
    public TierInstance findByTierInstanceName(String tierInstanceName) throws EntityNotFoundException;

}
