/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.installator;

import java.util.List;
import java.util.Set;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.installator.sdc.util.SDCUtil;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;

public class ProductInstallatorDummySdcImpl implements ProductInstallator {

    private SDCClient sDCClient;
    private SystemPropertiesProvider systemPropertiesProvider;
    private SDCUtil sDCUtil;

    public void installArtifact(ClaudiaData claudiaData,ProductInstance productInstance, Artifact artifact) throws ProductInstallatorException {
        // TODO Auto-generated method stub

    }

    public void uninstall(ClaudiaData claudiaData,ProductInstance productInstance) throws ProductInstallatorException {
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

    public void uninstallArtifact(ClaudiaData claudiaData,ProductInstance productInstance, Artifact artifact)
            throws ProductInstallatorException {
        // TODO Auto-generated method stub

    }

    public ProductInstance install(ClaudiaData claudiaData, String envName, TierInstance tierInstance,
            ProductRelease productRelease, Set<Attribute> attributes) throws ProductInstallatorException {

        ProductInstance productInstance = new ProductInstance();
        productInstance.setStatus(Status.INSTALLED);
        productInstance.setName(tierInstance.getName() + "_" + productRelease.getProduct() + "_"
                + productRelease.getVersion());
        productInstance.setProductRelease(productRelease);
        // productInstance.setTierInstance(tierInstance);
        productInstance.setVdc(tierInstance.getVdc());
        return productInstance;

    }

    public void configure(ClaudiaData data, ProductInstance productInstance, List<Attribute> properties) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @seecom.telefonica.euro_iaas.paasmanager.installator.ProductInstallator# deleteNode(java.lang.String,
     * java.lang.String)
     */
    public void deleteNode(ClaudiaData claudiaData,String vdc, String nodeName) throws ProductInstallatorException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.installator.ProductInstallator#loadNode(java.lang.String,
     * java.lang.String)
     */
    public ChefClient loadNode(ClaudiaData claudiaData,String vdc, String nodeName) throws ProductInstallatorException, EntityNotFoundException {

        return null;
    }

}
