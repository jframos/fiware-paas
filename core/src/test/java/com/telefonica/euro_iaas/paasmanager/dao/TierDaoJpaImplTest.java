/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

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

        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();

        productReleases.add(new ProductRelease());

        Tier tier = new Tier(TIER_NAME, MAXIMUM_INSTANCES, MINIMUM_INSTANCES, INITIAL_INSTANCES, productReleases);

        TierDaoJpaImpl tierDao = new TierDaoJpaImpl();

    }

}
