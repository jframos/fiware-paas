/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.SubNetworkDao;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;

/**
 * @author Henar Munoz
 */
@Transactional(propagation = Propagation.REQUIRED)
public class SubNetworkDaoJpaImpl extends AbstractBaseDao<SubNetwork, String> implements SubNetworkDao {

    /**
     * find all networks.
     * 
     * @return network list
     */
    public List<SubNetwork> findAll() {
        return super.findAll(SubNetwork.class);
    }

    /**
     * Loads the subnet.
     */
    public SubNetwork load(String arg0) throws EntityNotFoundException {
        return super.loadByField(SubNetwork.class, "name", arg0);
    }

}
