package com.telefonica.euro_iaas.paasmanager.installator;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.SDC_SERVER_MEDIATYPE;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.SDC_SERVER_URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.installator.sdc.util.SDCUtil;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.client.SDCClient;

public class ProductInstallatorDummySdcImpl implements ProductInstallator {

	private SDCClient sDCClient;
    private SystemPropertiesProvider systemPropertiesProvider;
    private SDCUtil sDCUtil;
	
	

	public void installArtifact(ProductInstance productInstance,
			Artifact artifact) throws ProductInstallatorException {
		// TODO Auto-generated method stub

	}

	public void uninstall(ProductInstance productInstance) {
		// TODO Auto-generated method stub

	}
    // //////////// I.O.C /////////////
    /**
     * @param sDCClient
     *            the sDCClient to set
     */
    public void setSDCClient(SDCClient sDCClient) {
        this.sDCClient = sDCClient;
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public void setSDCUtil(SDCUtil sDCUtil) {
        this.sDCUtil = sDCUtil;
    }

	public void uninstallArtifact(ProductInstance productInstance,
			Artifact artifact) throws ProductInstallatorException {
		// TODO Auto-generated method stub
		
	}

	public ProductInstance install(TierInstance tierInstance,
			ProductRelease productRelease) throws ProductInstallatorException {
			        
    ProductInstance productInstance = new ProductInstance ();
    productInstance.setStatus(Status.INSTALLED);
    productInstance.setName(tierInstance.getName()+"_"+productRelease.getProduct()+"_"+productRelease.getVersion());
    productInstance.setProductRelease(productRelease);
  //  productInstance.setTierInstance(tierInstance);
    productInstance.setVdc(tierInstance.getVdc());
    return productInstance;

   }

	public void configure(ProductInstance productInstance,
			 List<Attribute> properties) {
		// TODO Auto-generated method stub
		
	}


}
