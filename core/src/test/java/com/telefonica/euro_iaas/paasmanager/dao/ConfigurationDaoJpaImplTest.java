/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.model.Configuration;

public class ConfigurationDaoJpaImplTest extends AbstractJpaDaoTest {

    private final String KEY = "key";
    private final String KEY2 = "key2";
    private final String VALUE = "value";

    private ConfigurationDao configurationDao;

    public void setConfigurationDao(ConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
    }

    /**
     * Test the create and load method
     */
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

        Configuration foundConfiguration = configurationDao
        .load(createdConfiguration.getId());
        assertEquals(createdConfiguration, foundConfiguration);
    }

    /**
     * Test the find, remove and upload method
     */
    /*
     * public void testFindAllAndUpdate() throws Exception { assertEquals(0,
     * configurationDao.findAll().size()); testCreate(); List<Configuration>
     * configurations = configurationDao.findAll(); assertEquals(1,
     * configurations.size()); Configuration configuration =
     * configurations.get(0); configuration.setKey(KEY2);
     * configurationDao.update(configuration); assertEquals(KEY2,
     * configurationDao.load(configuration.getId()).getKey());
     * configurationDao.remove(configuration); assertEquals(0,
     * configurationDao.findAll().size()); }
     */

    /*
     * public void testSuma() throws Exception { String suma =
     * configurationDao.suma("a", "b"); assertEquals ("ab" , suma); }
     */

    /**
     * @param configurationDao
     *            the configurationDao to set
     */
    /*
     * public void setApplicationTypeDao(ConfigurationDao configurationDao) {
     * this.configurationDao = configurationDao; }
     */
}
