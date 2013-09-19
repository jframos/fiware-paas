package com.telefonica.euro_iaas.paasmanager.util;

import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;

public class SdcToPaasModelImpl implements SdcToPaasModel {

	public ProductInstance getPassProductInstance(
			com.telefonica.euro_iaas.sdc.model.ProductInstance sdcProductInstance) {
		
		ProductInstance paasProductInstance = new ProductInstance();
		/*
		paasProductInstance.setName(
				sdcProductInstance.getProduct().getProduct().getName() + "-" +
				sdcProductInstance.getProduct().getVersion() +
				sdcProductInstance.getVm().getFqn() + "-");
		paasProductInstance.setProductRelease(
				getPassProductInstance(sdcProductInstance.getProduct()));*/
		
		// TODO Auto-generated method stub
		return paasProductInstance;
	}

	public ProductRelease getPassProductRelease(
			com.telefonica.euro_iaas.sdc.model.ProductRelease sdcProductRelease) {
		
		/*ProductRelease paasProductRelease= new ProductRelease();
		
		paasProductRelease.setDescription(sdcProductRelease.getReleaseNotes());
		paasProductRelease.setAttributes(
				getPassProductReleaseAttributes(sdcProductRelease.getPrivateAttributes()));
		paasProductRelease.setName(sdcProductRelease.getProduct().getName()
				+ "-" + sdcProductRelease.getVersion());
		paasProductRelease.setSupportedOOSS(
				getPassSupportedOOSS(sdcProductRelease.getSupportedOOSS()));
		paasProductRelease.setTransitableReleases(
				getPaasProductTransitableReleases(sdcProductRelease.getTransitableReleases()));
		paasProductRelease.getVersion(sdcProductRelease.);*/
		
		return null;
	}

}
