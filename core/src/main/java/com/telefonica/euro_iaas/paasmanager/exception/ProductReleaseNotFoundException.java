/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.exception;

import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;


/**
 * Exception thrown when the product release is not found.
 *
 * @author Jesus M. Movilla
 * @version $Id: $
 */
@SuppressWarnings("serial")
public class ProductReleaseNotFoundException extends Exception {

	private ProductRelease productRelease;
	
	public ProductReleaseNotFoundException() {
		super();
	}

	public ProductReleaseNotFoundException(String msg) {
		super(msg);
	}

	public ProductReleaseNotFoundException(ProductRelease productRelease) {
		this.productRelease = productRelease;
	}
	
	public ProductReleaseNotFoundException(Throwable e) {
		super(e);
	}

	public ProductReleaseNotFoundException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * @return the productRelease
	 */
	public ProductRelease getProductRelease() {
		return productRelease;
	}

	/**
	 * @param productRelease the productRelease to set
	 */
	public void setProductRelease(ProductRelease productRelease) {
		this.productRelease = productRelease;
	}
}
 
