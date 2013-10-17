/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.manager;

import static org.mockito.Mockito.mock;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.manager.impl.ProductReleaseManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;

/**
 * @author jesus.movilla
 * 
 */
public class ProductReleaseImplTest extends TestCase {

	private ProductReleaseManagerImpl productReleaseManager;

	private ProductReleaseDao productReleaseDao;

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

		productReleaseManager.load("test-0.1");
	}

}
