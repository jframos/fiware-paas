/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.NetworkDao;
import com.telefonica.euro_iaas.paasmanager.model.Network;

/**
 * @author Henar Munoz
 * 
 */
public class NetworkDaoJpaImpl extends AbstractBaseDao<Network, String> implements
NetworkDao {

    /*
     * find all networks.
     * @return network list
     */
    public List<Network> findAll() {
        return super.findAll(Network.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telefonica.euro_iaas.commons.dao.BaseDAO#load(java.io.Serializable)
     */
    public Network load(String arg0) throws EntityNotFoundException {
        return super.loadByField(Network.class, "name", arg0);
    }

}
