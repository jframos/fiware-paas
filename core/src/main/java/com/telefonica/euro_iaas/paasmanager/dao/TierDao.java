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
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierSearchCriteria;

/**
 * Defines the methods needed to persist Tier objects.
 * 
 * @author Jesus M. Movilla
 */
public interface TierDao extends BaseDAO<Tier, String> {

    /**
     * @param TierId
     * @return
     * @throws EntityNotFoundException
     * @throws InvalidEntityException
     */
    Tier load(String name, String vdc, String environmentName) throws EntityNotFoundException;

    /**
     * Find the environment that matches with the given criteria.
     * 
     * @param criteria
     *            the search criteria
     * @return the list of elements that match with the criteria.
     * @throws EntityNotFoundException
     */
    List<Tier> findByCriteria(TierSearchCriteria criteria) throws EntityNotFoundException;

    /**
     * @param tierName
     * @param vdc
     * @param environmentName
     * @return
     */
    Tier loadTierWithProductReleaseAndMetadata(String tierName, String vdc, String environmentName)
            throws EntityNotFoundException;

    /**
     * Find region name from tier by security group id.
     * 
     * @param idSecurityGroup
     * @return
     */
    String findRegionBySecurityGroup(String idSecurityGroup) throws EntityNotFoundException;
}
