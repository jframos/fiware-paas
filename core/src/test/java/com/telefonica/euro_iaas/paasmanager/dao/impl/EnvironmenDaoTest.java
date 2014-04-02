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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentDao;
import com.telefonica.euro_iaas.paasmanager.model.Environment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" })
public class EnvironmenDaoTest {

    @Autowired
    EnvironmentDao environmentDao;

    public static String ENVIRONMENT_NAME = "ENVIRONMENT_NAME";
    public static String ORG = "org";

    /**
     * Test the create method
     */
    @Test
    public void testEnvironmentNoTiers() throws Exception {

        Environment environment = new Environment();
        environment.setName(ENVIRONMENT_NAME);
        environment.setOrg(ORG);
        environment.setDescription("description");
        environment = environmentDao.create(environment);
        assertNotNull(environment);
        assertNotNull(environment.getId());

    }

    /**
     * Test the load method
     */
    @Test
    public void testLoadNoTiers() throws Exception {

        Environment environment = new Environment();
        environment.setName(ENVIRONMENT_NAME);
        environment.setOrg(ORG);
        environment.setVdc("vdc");
        environment.setDescription("description");
        environment = environmentDao.create(environment);
        environment = environmentDao.load(environment.getName(), "vdc");
        assertNotNull(environment);
        assertNotNull(environment.getId());

    }

    /**
     * Test the load method
     * @throws AlreadyExistsEntityException 
     * @throws InvalidEntityException 
     * 
     * @throws AlreadyExistsEntityException
     * @throws InvalidEntityException
     * @throws EntityNotFoundException 
     * @throws EntityNotFoundException
     */
    @Test(expected = EntityNotFoundException.class)
    public void testDeleteNoTiers() throws InvalidEntityException, AlreadyExistsEntityException, EntityNotFoundException {

        Environment environment = new Environment();
        environment.setName(ENVIRONMENT_NAME);
        environment.setDescription("description");
        environment.setOrg(ORG);
        environment.setVdc("vdc");
        environment = environmentDao.create(environment);
        environmentDao.remove(environment);
        environmentDao.load(ENVIRONMENT_NAME, "vdc");
        fail("Should have thrown an EntityNotFoundException because the environment does not exit!");

    }

    public void setEnvironmentDao(EnvironmentDao environmentDao) {
        this.environmentDao = environmentDao;
    }

}
