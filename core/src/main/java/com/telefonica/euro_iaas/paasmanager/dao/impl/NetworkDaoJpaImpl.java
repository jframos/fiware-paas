/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.NetworkDao;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;

/**
 * @author Henar Munoz
 */
@Transactional(propagation = Propagation.REQUIRED)
public class NetworkDaoJpaImpl extends AbstractBaseDao<Network, String> implements NetworkDao {

    /*
     * find all networks.
     * @return network list
     */
    public List<Network> findAll() {
        return super.findAll(Network.class);
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.commons.dao.BaseDAO#load(java.io.Serializable)
     */
    public Network load(String networkName) throws EntityNotFoundException {
        try {
            return findNetworkWithSubNet(networkName);
        } catch (Exception e) {
            return this.loadByField(Network.class, "name", networkName);
        }
    }

    private Network findNetworkWithSubNet(String name) throws EntityNotFoundException {
        Query query = getEntityManager().createQuery(
                "select p from Network p left join " + " fetch p.subNets where p.name = :name");
        query.setParameter("name", name);
        Network network = null;
        try {
            network = (Network) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No network found in the database with id: " + name + " Exception: " + e.getMessage();
            throw new EntityNotFoundException(ProductRelease.class, "name", name);
        }
        return network;
    }

}
