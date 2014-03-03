/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.paasmanager.dao.ArtifactDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.exception.ApplicationTypeNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.paasmanager.installator.ProductInstallator;
import com.telefonica.euro_iaas.paasmanager.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationInstance;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ApplicationInstanceSearchCriteria;


/**
 * Application Instance Manager operations: install
 * 
 * @author Henar Mu�oz
 */
public class ApplicationInstanceManagerImpl implements ApplicationInstanceManager {

    private ApplicationReleaseDao applicationReleaseDao;
    private ApplicationInstanceDao applicationInstanceDao;
    private ProductReleaseDao productReleaseDao;
    private ArtifactDao artifactDao;
    private ProductInstallator productInstallator;
    private static Logger log = Logger.getLogger(ApplicationInstanceManagerImpl.class.getName());

    /**
     * Install the applicationIntance on a EnvironmetInstance Precondition: 1) The EnvironmentInstance is already
     * installed 2) The ApplicationRelease is described in the request
     * 
     * @param vdc
     *            the vdc where the instance will be installed
     * @param environmentInstance
     *            the environmentInstance on which the application is going to be installed
     * @param application
     *            the application to be installed
     * @throws ProductReleaseNotFoundException
     *             if ProuctRelease provided is not in the paas-manager database
     * @throws ApplicationTypeNotFoundException
     *             if ApplicationType provided in applicatonRelease object is not in the paas-manager database
     * @throws InvalidEntityException
     *             if data in applicationRelease is not valid (artifacts, and applciatonRelease objects)
     * @throws AlreadyExistsEntityException
     *             if artifcat/applicationRelease are already in the paas-manager database
     * @return the installed applicationInstance
     */
    public ApplicationInstance install(String org, String vdc, EnvironmentInstance environmentInstance,
            ApplicationRelease applicationRelease) throws ProductReleaseNotFoundException, InvalidEntityException,
            AlreadyExistsEntityException, ApplicationTypeNotFoundException, ProductInstallatorException {

        log.info("Install aplication " + applicationRelease.getName() + " " +
                applicationRelease.getVersion() + " on "
                + " enviornment " + environmentInstance + " with number of artifacts " + applicationRelease.getArtifacts().size() );
        
        if (!(canInstall(environmentInstance, applicationRelease.getArtifacts()))) {
            throw new InvalidEntityException("The Products included in "
                    + "ApplicationRelease does NOT correspond with the "
                    + "productInstances present in the Environment Instance");
        }

        // Install The applicationRelease=n-Artifacts
        for (Artifact artifact : applicationRelease.getArtifacts()) {
            log.debug ("Installing artifact " + artifact.getName() + " version " + artifact.getPath() + " product " + artifact.getProductRelease());
            // Install the artifact in the product instance associated
            // Obtain the VMs from EnvInstance where productRelease is installed
            ProductInstance productInstance = getProductInstanceFromEnvironment(artifact, environmentInstance);
            log.debug("Installing artifact " + artifact.getName() );
            productInstallator.installArtifact(productInstance, artifact);

        }

        // Create the Corresponding ApplicationInstance Object
        ApplicationInstance applicationInstance = new ApplicationInstance(applicationRelease, environmentInstance);

        applicationInstance.setVdc(vdc);
        applicationInstance.setStatus(Status.ARTEFACT_DEPLOYED);

        // Insert ApplicationInstance in DB
        try {

            applicationInstance = insertApplicationInstanceDB(applicationInstance);
        } catch (InvalidEntityException e3) {
            throw new InvalidEntityException("Error to insert the application instante in the database: "
                    + e3.getMessage());
        } catch (AlreadyExistsEntityException e3) {
            throw new InvalidEntityException("Error to insert the application instante in the database: "
                    + e3.getMessage());
        }

        return applicationInstance;
    }

    public ApplicationInstance load(String vdc, String name) throws EntityNotFoundException {
        ApplicationInstance instance = applicationInstanceDao.load(name);
        if (!instance.getVdc().equals(vdc)) {
            throw new EntityNotFoundException(EnvironmentInstance.class, "vdc", vdc);
        }
        return instance;
    }

    /**
     * Find all applicationInstances in teh database
     */
    public List<ApplicationInstance> findAll() {
        return applicationInstanceDao.findAll();
    }

    public List<ApplicationInstance> findByCriteria(ApplicationInstanceSearchCriteria criteria) {
        return applicationInstanceDao.findByCriteria(criteria);
    }

    public void uninstall(String org, String vdc, EnvironmentInstance environmentInstance,
            ApplicationInstance applicationInstance) throws ProductInstallatorException {
        // Install The applicationRelease=n-Artifacts
        for (int i = 0; i < applicationInstance.getApplicationRelease().getArtifacts().size(); i++) {
            // Install the artifact in the product instance associated
            // Obtain the VMs from EnvInstance where productRelease is installed
            ProductInstance productInstance = getProductInstanceFromEnvironment(applicationInstance
                    .getApplicationRelease().getArtifacts().get(i), environmentInstance);
            productInstallator.uninstallArtifact(productInstance, applicationInstance.getApplicationRelease()
                    .getArtifacts().get(i));

        }

        applicationInstanceDao.remove(applicationInstance);

    }

    /**
     * Insert/update ApplicationRelease in the paas-manager database
     * 
     * @param application
     * @return
     * @throws ProductReleaseNotFoundException
     * @throws ApplicationTypeNotFoundException
     * @throws InvalidEntityException
     * @throws AlreadyExistsEntityException
     */
    private ApplicationInstance insertApplicationInstanceDB(ApplicationInstance application)
            throws ProductReleaseNotFoundException, ApplicationTypeNotFoundException, InvalidEntityException,
            AlreadyExistsEntityException {
        log.debug ("Inser application " + application.getName() + " in DB");

        ApplicationInstance applicationInstance = null;
        Artifact artifact;
        List<Artifact> artifactsDB = new ArrayList();
        List<ProductRelease> allProductReleases = new ArrayList<ProductRelease>();
        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();

        List<Artifact> artifactsIn = new ArrayList<Artifact>();

        try {
            applicationInstance = applicationInstanceDao.load(application.getName());
        } catch (EntityNotFoundException e) {
            artifactsIn = application.getApplicationRelease().getArtifacts();

            for (int i = 0; i < artifactsIn.size(); i++) {
                ProductRelease productRelease = artifactsIn.get(i).getProductRelease();
                try {
                    productRelease = productReleaseDao.load(productRelease.getProduct() + "-"
                            + productRelease.getVersion());
                } catch (EntityNotFoundException e1) {
                    throw new ProductReleaseNotFoundException(productRelease, e1.getMessage());
                }

                try {
                    artifact = artifactDao.load(artifactsIn.get(i).getName());
                } catch (EntityNotFoundException e1) {

                    try {
                        artifact = artifactDao.create(new Artifact(artifactsIn.get(i).getName(), artifactsIn.get(i)
                                .getPath(), productRelease));
                        artifactsDB.add(artifact);
                    } catch (InvalidEntityException e3) {
                        throw new InvalidEntityException(e3);
                    } catch (AlreadyExistsEntityException e3) {
                        throw new AlreadyExistsEntityException(e3);
                    }
                }

                if (!isPRInAllProductReleases(productRelease, allProductReleases)) {
                    productReleases.add(productRelease);
                }
                allProductReleases.add(productRelease);
            }

            ApplicationRelease applicationRelease = null;

            try {
                applicationRelease = applicationReleaseDao.create(new ApplicationRelease(application
                        .getApplicationRelease().getName(), application.getApplicationRelease().getVersion(),
                        application.getApplicationRelease().getDescription(), null,

                        application.getApplicationRelease().getTransitableReleases(), artifactsDB));
            } catch (InvalidEntityException e3) {
                throw new InvalidEntityException(e3);
            } catch (AlreadyExistsEntityException e3) {
                throw new AlreadyExistsEntityException(e3);
            }

            try {
                applicationInstance = applicationInstanceDao.create(new ApplicationInstance(applicationRelease,
                        application.getEnvironmentInstance(), application.getVdc(), application.getStatus()));
            } catch (InvalidEntityException e3) {
                throw new InvalidEntityException(ApplicationInstance.class, e3);
            } catch (AlreadyExistsEntityException e3) {
                throw new AlreadyExistsEntityException(ApplicationInstance.class, e3);
            }
        }
        return applicationInstance;
    }

    /**
     * Get the VMs where the artifact/ProductRelease should be installed
     * 
     * @param artifact
     * @param envInstance
     * @return the VMs where the artifact/ProductRelease should be installed
     */
    private ProductInstance getProductInstanceFromEnvironment(Artifact artifact, EnvironmentInstance envInstance) {

        //
        ProductInstance productInstance = null;

        List<TierInstance> tierInstances = envInstance.getTierInstances();
        // For each tier instance
        for (TierInstance tierInstance : tierInstances) {
            // Check product instnaces where to install the artifacts
            for (ProductInstance deployedProductInstance : tierInstance.getProductInstances()) {
                if (deployedProductInstance.getProductRelease().getProduct()
                        .equals(artifact.getProductRelease().getProduct()))

                {
                    productInstance = deployedProductInstance;
                }
            }

        }
        return productInstance;
    }

    private boolean isPRInAllProductReleases(ProductRelease productRelease, List<ProductRelease> allProductReleases) {
        for (int i = 0; i < allProductReleases.size(); i++) {
            if (productRelease.getProduct().equals(allProductReleases.get(i).getProduct())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if any productRelease coming in the Application Release Object is not in the EnvironmentInstance. In this
     * case the method return false
     * 
     * @param envInstance
     * @param applicationRelease
     * @return true/false
     */
    private boolean canInstall(EnvironmentInstance envInstance, List<Artifact> applicationArtifact) {

        if (applicationArtifact==null)
        {
            log.debug ("There is not any product release to install");
            return false;
        }
        List<ProductRelease> productReleasesApp = new ArrayList<ProductRelease>();
        for (Artifact artifact : applicationArtifact) {
            productReleasesApp.add(artifact.getProductRelease());
        }

        List<ProductRelease> productReleasesEnv = new ArrayList<ProductRelease>();
        for (int i = 0; i < envInstance.getTierInstances().size(); i++) {
            TierInstance tInstance = envInstance.getTierInstances().get(i);

            for (int j = 0; j < tInstance.getProductInstances().size(); j++) {
                ProductInstance pInstance = tInstance.getProductInstances().get(j);
                productReleasesEnv.add(pInstance.getProductRelease());
            }

        }

        for (int i = 0; i < productReleasesApp.size(); i++) {
            if (!(isProductReleaseinProductList(productReleasesApp.get(i), productReleasesEnv))) {
                log.info("The artifacts cannot be deployed in the environmetn instance, since the product release " +
                        productReleasesApp.get(i) + " is not in the environment" );
                return false;
            }
        }
        log.info("The artifacts can be deployed in the environmetn instance");
        return true;

    }

    /**
     * Evaluates if a productRelease is included in a productReleaseList by looking at the productRelease Name.
     * 
     * @param pRelease
     * @param productList
     * @return true/false
     */
    private boolean isProductReleaseinProductList(ProductRelease pRelease, List<ProductRelease> productList) {

        List<String> pnameList = new ArrayList<String>();

        for (int i = 0; i < productList.size(); i++) {
            pnameList.add(productList.get(i).getProduct());
        }
        return pnameList.contains(pRelease.getProduct());
    }

    // //////////// I.O.C /////////////
    /**
     * @param applicationReleaseDao
     *            the applicationReleaseDao to set
     */
    public void setApplicationReleaseDao(ApplicationReleaseDao applicationReleaseDao) {
        this.applicationReleaseDao = applicationReleaseDao;
    }

    /**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

    /**
     * @param applicationInstanceDao
     *            the applicationInstanceDao to set
     */
    public void setApplicationInstanceDao(ApplicationInstanceDao applicationInstanceDao) {
        this.applicationInstanceDao = applicationInstanceDao;
    }

    /**
     * @param artifactDao
     *            the artifactDao to set
     */
    public void setArtifactDao(ArtifactDao artifactDao) {
        this.artifactDao = artifactDao;
    }

    /**
     * @param productInstallator
     *            the productInstallator to set
     */
    public void setProductInstallator(ProductInstallator productInstallator) {
        this.productInstallator = productInstallator;
    }

}
