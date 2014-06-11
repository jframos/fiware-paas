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

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.dao.impl.ProductInstanceDaoJpaImpl;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;

/**
 * Unit test for InstanceDaoJpaImpl
 * 
 * @author Jesus M. Movilla
 */
public class ProductInstanceDaoJpaImplTest {

    private ProductInstanceDao productInstanceDao;
    private OSDao osDao;
    private ProductReleaseDao productReleaseDao;
    private ProductRelease productRelease;

    public final static String PINSTANCE_NAME = "instanceName";
    public final static String PINSTANCE2_NAME = "instance2Name";
    public final static String PINSTANCE_VERSION = "instanceVersion";



    @Before
    public void setUp() {
        productReleaseDao = mock(ProductReleaseDao.class);
        productRelease = new ProductRelease("product", "version");

    }

    /**
     * Test the create and load method
     */
    @Test
    public void testCreate() throws Exception {

        TierInstance tierinstance = new TierInstance();
        ProductInstanceDaoJpaImpl productInstanceDao = new ProductInstanceDaoJpaImpl();
        ProductInstance productInstance = new ProductInstance(productRelease, Status.INSTALLING, "vdcTest");
        productInstance.setName(PINSTANCE_NAME);

    }
}
