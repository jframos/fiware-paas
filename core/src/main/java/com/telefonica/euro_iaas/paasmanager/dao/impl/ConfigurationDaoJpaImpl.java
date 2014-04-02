/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
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
