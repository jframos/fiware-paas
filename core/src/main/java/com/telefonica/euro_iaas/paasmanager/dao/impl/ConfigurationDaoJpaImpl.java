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
import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ConfigurationDao;
import com.telefonica.euro_iaas.paasmanager.model.Configuration;

@Transactional(propagation = Propagation.REQUIRED)
public class ConfigurationDaoJpaImpl extends AbstractBaseDao<Configuration, Long> implements ConfigurationDao {

    @Override
    public Configuration create(Configuration configuration) throws AlreadyExistsEntityException {
        return super.create(configuration);
    }

    public List<Configuration> findAll() {
        return super.findAll(Configuration.class);
    }

    public Configuration load(Long id) throws EntityNotFoundException {
        return super.loadByField(Configuration.class, "id", id);
    }

    @Override
    public void remove(Configuration configuration) {
        super.remove(configuration);
    }

    @Override
    public Configuration update(Configuration configuration) {
        return super.update(configuration);
    }

}
