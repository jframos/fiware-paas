package com.telefonica.euro_iaas.paasmanager.util;

import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;

public interface SdcToPaasModel {

	/*
	 * Converting SDC ProductInstance To Paas ProductInstance
	 */
	ProductInstance getPassProductInstance(
			com.telefonica.euro_iaas.sdc.model.ProductInstance productInstance);

	/*
	 * Converting SDC ProductRelease To Paas ProductRelease
	 */
	ProductRelease getPassProductRelease(
			com.telefonica.euro_iaas.sdc.model.ProductRelease productRelease);
}
