package com.telefonica.euro_iaas.paasmanager.installator;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReconfigurationException;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.sdc.model.dto.ChefClient;

public interface ProductInstallator {

    /**
     * Operation that installs the productInstance
     * 
     * @param productInstance
     * @return
     * @throws ProductInstallatorException
     */
    // ProductInstance install (ProductInstance productInstance) throws
    // ProductInstallatorException;

    ProductInstance install(ClaudiaData claudiaData, String envName, TierInstance tierInstance,
            ProductRelease productRelease, List<Attribute> attributes) throws ProductInstallatorException;

    /**
     * Operation that installs an artefact in the productInstance
     * 
     * @param productInstance
     * @return
     * @throws ProductInstallatorException
     */
    void installArtifact(ProductInstance productInstance, Artifact artifact) throws ProductInstallatorException;

    /**
     * Operation that uninstalls an artefact in the productInstance
     * 
     * @param productInstance
     * @return
     * @throws ProductInstallatorException
     */
    void uninstallArtifact(ProductInstance productInstance, Artifact artifact) throws ProductInstallatorException;

    /*
     * Operation that installs the productInstance
     */
    void uninstall(ProductInstance productInstance) throws ProductInstallatorException;

    void configure(ClaudiaData claudiaData, ProductInstance productInstance, List<Attribute> properties)
            throws ProductInstallatorException, EntityNotFoundException, ProductReconfigurationException;

    /*
     * Operation that deletes a chefClien from the node manager (Chef server in SDC, for instance)
     */
    void deleteNode(String vdc, String nodeName) throws ProductInstallatorException;

    /*
     * Operation that load a chefClient from the node manager (Chef server in SDC, for instance)
     */
    ChefClient loadNode(String vdc, String nodeName) throws ProductInstallatorException, EntityNotFoundException;
}
