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

package com.telefonica.euro_iaas.paasmanager.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.paasmanager.model.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-test-db-config.xml", "classpath:/spring-dao-config.xml" })
public class ConfigurationDaoJpaImplTest {

    private final String KEY = "key";
    private final String KEY2 = "key2";
    private final String VALUE = "value";

    @Autowired
    ConfigurationDao configurationDao;

    public void setConfigurationDao(ConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
    }

    /**
     * Test the create and load method
     */
    @Test
    public void testCreate() throws Exception {
        Configuration configuration = new Configuration(KEY, VALUE);
        assertNull(configuration.getId());

        List<Configuration> configurations = configurationDao.findAll();

        Configuration createdConfiguration;
        if (configurations.size() == 0)
            createdConfiguration = configurationDao.create(configuration);
        else
            createdConfiguration = configurations.get(0);

        assertNotNull(createdConfiguration.getId());
        assertEquals(configuration.getKey(), createdConfiguration.getKey());
        assertEquals(configuration.getValue(), createdConfiguration.getValue());

        Configuration foundConfiguration = configurationDao.load(createdConfiguration.getId());
        assertEquals(createdConfiguration, foundConfiguration);
    }

    /**
     * Test the find, remove and upload method
     */
    /*
     * public void testFindAllAndUpdate() throws Exception { assertEquals(0, configurationDao.findAll().size());
     * testCreate(); List<Configuration> configurations = configurationDao.findAll(); assertEquals(1,
     * configurations.size()); Configuration configuration = configurations.get(0); configuration.setKey(KEY2);
     * configurationDao.update(configuration); assertEquals(KEY2,
     * configurationDao.load(configuration.getId()).getKey()); configurationDao.remove(configuration); assertEquals(0,
     * configurationDao.findAll().size()); }
     */

    /*
     * public void testSuma() throws Exception { String suma = configurationDao.suma("a", "b"); assertEquals ("ab" ,
     * suma); }
     */

    /**
     * @param configurationDao
     *            the configurationDao to set
     */
    /*
     * public void setApplicationTypeDao(ConfigurationDao configurationDao) { this.configurationDao = configurationDao;
     * }
     */
}
