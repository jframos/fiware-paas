package com.telefonica.euro_iaas.paasmanager.dao;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.dao.impl.TierDaoJpaImpl;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

/**
 * Unit test for TierDaoJpaImplTest
 * 
 * @author Jesus M. Movilla
 */
public class TierDaoJpaImplTest extends AbstractJpaDaoTest {

    private ProductReleaseDao productReleaseDao;
    private OSDao osDao;

    private ServiceDao serviceDao;
    private ProductTypeDao productTypeDao;

    public final static String TIER_NAME = "TierName";
    public final static Integer MAXIMUM_INSTANCES = 8;
    public final static Integer MINIMUM_INSTANCES = 1;
    public final static Integer INITIAL_INSTANCES = 1;

    /**
     * Test the create and load method
     */
    public void testCreate1() throws Exception {
        System.out.println("Inserting TierObject1 in DB");

        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();

        productReleases.add(new ProductRelease());

        Tier tier = new Tier(TIER_NAME, MAXIMUM_INSTANCES, MINIMUM_INSTANCES, INITIAL_INSTANCES, productReleases);

        TierDaoJpaImpl tierDao = new TierDaoJpaImpl();

        // Tier createdTier = tierDao.create(tier);

        // assertNotNull(createdTier.getName());
        // assertEquals(createdTier.getName(), tier.getName());

    }

    /**
     * Test the create and load method
     */
    public void testCreate2() throws Exception {
    }

    // System.out.println("Inserting TierObject2 in DB");

    // List<ProductRelease> productReleases2 = productReleaseDao.findAll();

    /*
     * Tier tier = new Tier(TIER_NAME, MAXIMUM_INSTANCES, MINIMUM_INSTANCES, INITIAL_INSTANCES, productReleases2);
     * TierDaoJpaImpl tierDao = new TierDaoJpaImpl (); // Tier createdTier = tierDao.create(tier); //
     * assertNotNull(createdTier.getName()); // assertEquals(createdTier.getName(), tier.getName()); } /** Test the
     * create and load method
     */
    public void testFindAllAndUpdate() throws Exception {
        /*
         * TierDaoJpaImpl tierDao = new TierDaoJpaImpl (); Integer initial_number_instances = 3; assertEquals(0,
         * tierDao.findAll().size()); testCreate1(); List<Tier> tiers = tierDao.findAll(); assertEquals(1,
         * tiers.size()); Tier tier = tiers.get(0); tier.setInitial_number_instances(3); tierDao.update(tier);
         * assertEquals(initial_number_instances, tierDao.load(tier.getName()).getInitial_number_instances());
         * tierDao.remove(tier); assertEquals(0, tierDao.findAll().size());
         */
    }

}
