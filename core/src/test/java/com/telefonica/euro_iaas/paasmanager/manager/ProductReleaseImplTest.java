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

package com.telefonica.euro_iaas.paasmanager.manager;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.manager.impl.ProductReleaseManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * @author jesus.movilla
 */
public class ProductReleaseImplTest extends TestCase {

    private ProductReleaseManagerImpl productReleaseManager;

    private ProductReleaseDao productReleaseDao;

    @Override
    @Before
    public void setUp() throws Exception {

        productReleaseManager = new ProductReleaseManagerImpl();
        productReleaseDao = mock(ProductReleaseDao.class);

        productReleaseManager.setProductReleaseDao(productReleaseDao);

    }

    @Test
    public void testCreateProductRelease() throws EntityNotFoundException {
        ProductRelease productRelease2 = new ProductRelease("product", "2.0");
        productRelease2.addAttribute(new Attribute("openports", "8080"));

     //   productReleaseManager.load("test-0.1", "token", "tenant");
    }

}
