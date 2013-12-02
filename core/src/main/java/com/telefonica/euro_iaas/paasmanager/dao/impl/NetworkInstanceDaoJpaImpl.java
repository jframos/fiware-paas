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
import com.telefonica.euro_iaas.paasmanager.dao.NetworkInstanceDao;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;

/**
 * @author Henar Munoz
 */
@Transactional(propagation = Propagation.REQUIRED)
public class NetworkInstanceDaoJpaImpl extends AbstractBaseDao<NetworkInstance, String> implements NetworkInstanceDao {

    /**
     * k7 find all networks.
     * 
     * @return network list
     */
    public List<NetworkInstance> findAll() {
        return super.findAll(NetworkInstance.class);
    }

    /**
     * It load the netowkr interface from its name.
     * 
     * @param name
     *            of the network instance
     */
    public NetworkInstance load(String name) throws EntityNotFoundException {

       // try {
            return findByNetworkInstanceName(name);
        //} catch (Exception e) {
          //  return super.loadByField(NetworkInstance.class, "name", name);
        //}

    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.dao.TierDao#findByTierId(java.lang .String)
     */
    private NetworkInstance findByNetworkInstanceName(String name) throws EntityNotFoundException {
        Query query = getEntityManager().createQuery(
                "select p from NetworkInstance p left join " + "fetch p.subNets where p.name = :name");
        query.setParameter("name", name);
        NetworkInstance networkInstance = null;
        try {
            networkInstance = (NetworkInstance) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No NetworkInstance found in the database with id: " + name + " Exception: "
                    + e.getMessage();
            throw new EntityNotFoundException(NetworkInstance.class, "name", name);
        }
        return networkInstance;
    }

}
