/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.dao;


/**
 * Unit test for EnvironmentDaoJpaImplTest
 * 
 * @author Jesus M. Movilla
 * 
 */
public class EnvironmentDaoJpaImplTest extends AbstractJpaDaoTest {

    private ProductReleaseDao productReleaseDao;
    private OSDao osDao;
    //private TierDao tierDao;
    private ProductTypeDao productTypeDao;
    private ServiceDao serviceDao;
    private EnvironmentDao environmentDao;
    //private EnvironmentTypeDao environmentTypeDao;

    public final static String ENVIRONMENT_NAME = "EnvironmentName";
    public final static String ENVIRONMENT2_NAME = "Environment2Name";


    /*public void testFilterbyTiers() throws Exception {

		EnvironmentDaoJpaImpl environmentDaoImpl = new EnvironmentDaoJpaImpl();
		Environment environment1 = new Environment ();
		Environment environment2 = new Environment ();
		Environment environment3 = new Environment ();
		Environment environment4 = new Environment ();

		List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
		productReleases.add(new ProductRelease());

		Tier tier1 = new Tier("TIER_NAME", 1, 1,	1, productReleases);
		Tier tier2 = new Tier("TIER_NAME", 1, 1,	1, productReleases);
		Tier tier3 = new Tier("TIER_NAME", 1, 1,	1, productReleases);
		Tier tier4 = new Tier("TIER_NAME4", 2, 1,	1, productReleases);

		List<Tier> tiers1 = new ArrayList<Tier>();
		tiers1.add(tier1);

		List<Tier> tiers2 = new ArrayList<Tier>();
		tiers2.add(tier1);
		tiers2.add(tier2);

		List<Tier> tiers3 = new ArrayList<Tier>();
		tiers3.add(tier1);
		tiers3.add(tier2);
		tiers3.add(tier3);

		List<Tier> tiers4 = new ArrayList<Tier>();
		tiers4.add(tier1);
		tiers4.add(tier4);
		//tiers4.add(tier3);

		environment1.setTiers(tiers1);
		Environment fiterEnvironment = environmentDaoImpl.filterEqualTiers(environment1);
		assertEquals(1, fiterEnvironment.getTiers().size());

		environment2.setTiers(tiers2);
		Environment fiterEnvironment2 = environmentDaoImpl.filterEqualTiers(environment2);
		assertEquals(1, fiterEnvironment2.getTiers().size());

		environment3.setTiers(tiers3);
		Environment fiterEnvironment3 = environmentDaoImpl.filterEqualTiers(environment3);
		assertEquals(1, fiterEnvironment3.getTiers().size());

		environment4.setTiers(tiers4);
		Environment fiterEnvironment4 = environmentDaoImpl.filterEqualTiers(environment4);
		assertEquals(2, fiterEnvironment4.getTiers().size());
	}*/

    /**
     * Test the updateOvf
     * @throws Exception
     */
    /*@Test
	public void testcreate() throws Exception {
		System.out.println("*************Iniciando testcreate**********************************************");
		EnvironmentTypeDaoJpaImpl environmentTypeDao = new EnvironmentTypeDaoJpaImpl();
		TierDaoJpaImpl tierDao = new TierDaoJpaImpl();

		System.out.println("*************Creating tiers**********************************************");
		TierDaoJpaImplTest tierDaoJpaImplTest = new TierDaoJpaImplTest();
		tierDaoJpaImplTest.testCreate1();

		System.out.println("*************Getting tiers**********************************************");
		List <Tier> tiers = new ArrayList<Tier> ();
		tiers = tierDao.findAll();

		System.out.println("*************Creating envType**********************************************");
		EnvironmentTypeDaoJpaImplTest environmentTypeDaoJpaImplTest = new
				EnvironmentTypeDaoJpaImplTest();
		environmentTypeDaoJpaImplTest.setEnvironmentTypeDao(environmentTypeDao);

		EnvironmentType environmentType = environmentTypeDaoJpaImplTest.create(new
				EnvironmentType ("Env3Name", "Env3Desc"));

		System.out.println("*************Creating environment**********************************************");
		Environment environment = new Environment (ENVIRONMENT2_NAME ,
 			environmentType , tiers);

		Environment createdEnvironment = environmentDao.create(environment);
	}*/

    /*@Test
	public void testUpdteOvf() throws Exception {
		EnvironmentTypeDaoJpaImpl environmentTypeDao = new EnvironmentTypeDaoJpaImpl();
		TierDaoJpaImpl tierDao = new TierDaoJpaImpl();

		List <Tier> tiers = new ArrayList<Tier> ();
		Tier tier = tierDao.findAll().get(0);

		EnvironmentTypeDaoJpaImplTest environmentTypeDaoJpaImplTest = new
				EnvironmentTypeDaoJpaImplTest();
		environmentTypeDaoJpaImplTest.setEnvironmentTypeDao(environmentTypeDao);

		List<EnvironmentType> environmentTypes = environmentTypeDao.findAll();

		EnvironmentType environmentType;
		if (environmentTypes.size() < 2)
			environmentType = environmentTypeDaoJpaImplTest.create(new
					EnvironmentType ("Env3Name", "Env3Desc"));
		else environmentType =
					environmentTypes.get(1);

		Environment environment = new Environment (ENVIRONMENT2_NAME ,
 			environmentType , tiers);

		Environment createdEnvironment = environmentDao.create(environment);
		assertNotNull(createdEnvironment.getName());
		assertEquals(createdEnvironment.getName(), environment.getName());

		createdEnvironment.setOvf("ovf");
		Environment updatedEnvironment = environmentDao.updateOvf(createdEnvironment);
		assertEquals(createdEnvironment.getTiers().size(), updatedEnvironment.getTiers().size());
	}*/

    /**
     * Test the create and load method
     */
    /*
     * public void testCreate1() throws Exception {
     * 
     * 
     * Tier tier = tierDao.findAll().get(0); List <Tier> tiers = new
     * ArrayList<Tier> ();
     * 
     * 
     * 
     * List<EnvironmentType> environmentTypes = environmentTypeDao.findAll();
     * 
     * EnvironmentType environmentType;
     * 
     * 
     * Environment environment = new Environment (ENVIRONMENT_NAME ,
     * environmentType , tiers);
     * 
     * Environment createdEnvironment = environmentDao.create(environment);
     * assertNotNull(createdEnvironment.getName());
     * assertEquals(createdEnvironment.getName(), environment.getName()); }
     * 
     * public void testCreate2() throws Exception {
     * 
     * Tier tier = tierDao.findAll().get(0); List <Tier> tiers = new
     * ArrayList<Tier> ();
     * 
     * EnvironmentTypeDaoJpaImplTest environmentTypeDaoJpaImplTest = new
     * EnvironmentTypeDaoJpaImplTest();
     * environmentTypeDaoJpaImplTest.setEnvironmentTypeDao(environmentTypeDao);
     * 
     * List<EnvironmentType> environmentTypes = environmentTypeDao.findAll();
     * 
     * EnvironmentType environmentType; if (environmentTypes.size() < 2)
     * environmentType = environmentTypeDaoJpaImplTest.create(new
     * EnvironmentType ("Env3Name", "Env3Desc")); else environmentType =
     * environmentTypes.get(1);
     * 
     * Environment environment = new Environment (ENVIRONMENT2_NAME ,
     * environmentType , tiers);
     * 
     * Environment createdEnvironment = environmentDao.create(environment);
     * assertNotNull(createdEnvironment.getName());
     * assertEquals(createdEnvironment.getName(), environment.getName()); }
     * 
     * /** Test the create and load method
     */
    /*
     * public void testFindAllAndUpdate() throws Exception { assertEquals(0,
     * environmentDao.findAll().size()); testCreate1(); List<Environment>
     * environments = environmentDao.findAll(); assertEquals(1,
     * environments.size()); Environment environment = environments.get(0);
     * environmentDao.update(environment); environmentDao.remove(environment);
     * assertEquals(0, environmentDao.findAll().size()); }
     * 
     * /** Test the recovery of a full environment object
     */
    /*
     * public void testRecoverFullEnvironmentObject() throws Exception {
     * System.out
     * .println("**********testRecoverFullEnvironmentObject.START***");
     * testCreate1(); //Environment environment =
     * environmentDao.load(ENVIRONMENT_NAME); Environment environment =
     * environmentDao.findAll().get(0); List<Tier> tiers =
     * environment.getTiers(); //assertEquals(1, tiers.size());
     * 
     * for (int i=0; i < tiers.size(); i++) { List<ProductRelease>
     * productReleases = tiers.get(i).getProductReleases(); assertEquals(1,
     * productReleases.size()); System.out.println(" tier[" + i + "]" +
     * tiers.get(i).getName());
     * 
     * for (int j=0; j < productReleases.size(); j++) { ProductRelease pRelease
     * = productReleases.get(j); System.out.println(" pRelease[" + j + "]" +
     * pRelease.getProduct()); List<OS> supportedOOSS =
     * pRelease.getSupportedOOSS(); assertEquals(1, supportedOOSS.size());
     * 
     * for (int k=0; k < supportedOOSS.size(); k++) { System.out.println(" OS["
     * + k + "]" + supportedOOSS.get(k).getName()); }
     * 
     * } } assertEquals(1, environmentDao.findAll().size());
     * System.out.println("**********testRecoverFullEnvironmentObject.FINISH***"
     * );
     * 
     * }
     * 
     * public void testFindByCriteria() throws Exception { testCreate1();
     * testCreate2();
     * 
     * List<Environment> environments = environmentDao.findAll();
     * EnvironmentSearchCriteria criteria = new EnvironmentSearchCriteria();
     * 
     * //findByCriteria environments = environmentDao.findByCriteria(criteria);
     * assertEquals(2, environments.size());
     * 
     * }
     */
}