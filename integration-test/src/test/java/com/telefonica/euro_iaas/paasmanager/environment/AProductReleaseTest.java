/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.environment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;

/**
 * Integration Tests for Product Release entity
 * 
 * @author henar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContextTest.xml" })
@ActiveProfiles("dummy")
public class AProductReleaseTest {

    @Autowired
    private ProductReleaseDao productReleaseDao;

    @Test
    public void testProductReleasesNotAttributes() throws Exception {

        ProductRelease productTomcat = new ProductRelease("mysql", "2", "tomcat 7", null);

        productTomcat = productReleaseDao.create(productTomcat);
        assertNotNull(productTomcat);
        assertEquals(productTomcat.getProduct(), "mysql");
        assertEquals(productTomcat.getVersion(), "2");

        List<ProductRelease> productReleases = productReleaseDao.findAll();
        assertNotNull(productReleases);

        ProductRelease productRelease = productReleaseDao.load("mysql-2");
        assertNotNull(productRelease);
        assertEquals(productRelease.getProduct(), "mysql");
        assertEquals(productRelease.getVersion(), "2");

    }

    @Test
    public void testProductReleasesWithAttributes() throws Exception {

        List<ProductRelease> productReleases = productReleaseDao.findAll();
        assertNotNull(productReleases);

        int number = productReleases.size();

        List<Attribute> attHenar = new ArrayList<Attribute>();
        attHenar.add(new Attribute("henar", "henar", "henar"));

        ProductRelease productHenar = new ProductRelease("henar", "0.1", "henar 0.1", attHenar);

        productHenar = productReleaseDao.create(productHenar);
        assertNotNull(productHenar);
        assertEquals(productHenar.getProduct(), "henar");
        assertEquals(productHenar.getVersion(), "0.1");

        productReleases = productReleaseDao.findAll();
        assertNotNull(productReleases);
        assertEquals(productReleases.size(), number + 1);

        ProductRelease productRelease = productReleaseDao.load("henar-0.1");
        assertNotNull(productRelease);
        assertEquals(productRelease.getProduct(), "henar");
        assertEquals(productRelease.getVersion(), "0.1");


    }

}
