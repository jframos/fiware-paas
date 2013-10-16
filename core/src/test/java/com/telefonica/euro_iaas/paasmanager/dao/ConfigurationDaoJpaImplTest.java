package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.model.Configuration;

public class ConfigurationDaoJpaImplTest extends AbstractJpaDaoTest {

	private String KEY = "key";
	private String KEY2 = "key2";
	private String VALUE = "value";

	private ConfigurationDao configurationDao;

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

	public void setConfigurationDao(ConfigurationDao configurationDao) {
		this.configurationDao = configurationDao;
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
