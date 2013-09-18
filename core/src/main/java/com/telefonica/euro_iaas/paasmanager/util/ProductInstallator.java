package com.telefonica.euro_iaas.paasmanager.util;

import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;

public interface ProductInstallator {
	
	/*
	 * Operation that installs the productInstance
	 */
	ProductInstance install (ProductInstance productInstance);

	/*
	 * Operation that installs the productInstance
	 */
	void uninstall (ProductInstance productInstance);
}
