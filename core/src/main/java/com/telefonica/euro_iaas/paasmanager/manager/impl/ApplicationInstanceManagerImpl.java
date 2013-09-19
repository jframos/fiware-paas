/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationInstanceDao;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationReleaseDao;
import com.telefonica.euro_iaas.paasmanager.dao.ApplicationTypeDao;
import com.telefonica.euro_iaas.paasmanager.dao.ArtifactDao;
import com.telefonica.euro_iaas.paasmanager.dao.ArtifactTypeDao;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.exception.ApplicationTypeNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.installator.ProductInstallator;
import com.telefonica.euro_iaas.paasmanager.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationInstance;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationType;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.ArtifactType;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ApplicationInstanceSearchCriteria;


/**
 * Application Instance Manager operations: install
 * @author Jesus M. Movilla
 */
public class ApplicationInstanceManagerImpl implements
		ApplicationInstanceManager {

	private ApplicationReleaseDao applicationReleaseDao;
	private ApplicationInstanceDao applicationInstanceDao;
	private EnvironmentInstanceDao environmentInstanceDao;
	private ProductReleaseDao productReleaseDao;
	private ArtifactDao artifactDao;
	private ArtifactTypeDao artifactTypeDao;
	private ApplicationTypeDao applicationTypeDao;
	private ProductInstallator productInstallator;
	
	 /**
     * Install the applicationIntance on a EnvironmetInstance
     * Precondition: 1) The EnvironmentInstance is already installed
     * 				 2) The ApplicationRelease is described in the request
     * @param vdc the vdc where the instance will be installed
     * @param environmentInstance the environmentInstance on which the application
     * is going to be installed
     * @param application the application to be installed
     * @throws ProductReleaseNotFoundException if ProuctRelease provided is not in
     * the paas-manager database
	 * @throws ApplicationTypeNotFoundException if ApplicationType provided in
	 * applicatonRelease object is not in the paas-manager database
	 * @throws InvalidEntityException if data in applicationRelease is not valid
	 * (artifacts, and applciatonRelease objects)
	 * @throws AlreadyExistsEntityException if artifcat/applicationRelease are
	 * already in the paas-manager database
	 * @return the installed applicationInstance
     */
	public ApplicationInstance install(String org, String vdc,
		EnvironmentInstance environmentInstance, 
		ApplicationRelease applicationRelease) 
		throws ProductReleaseNotFoundException, InvalidEntityException, 
		AlreadyExistsEntityException, ApplicationTypeNotFoundException, 
		ProductInstallatorException {

		//Insert/Load the ApplicationRelease
	//	application = insertApplicationReleaseDB (application);
		
		//Check if the list of produdctReleases that appears in each artifact
		// is included in the list of productInstances from the environment
		if (!(canInstall(environmentInstance, applicationRelease.getArtifacts())))
			throw new InvalidEntityException("The Products included in " +
					"ApplicationRelease does NOT correspond with the " +
					"productInstances present in the Environment Instance");
		
		//Install The applicationRelease=n-Artifacts
		for (int i=0; i < applicationRelease.getArtifacts().size(); i++){
			// Install the artifact in the product instance associated
			//Obtain the VMs from EnvInstance where productRelease is installed
			ProductInstance productInstance 
				= getProductInstanceFromEnvironment( 
						applicationRelease.getArtifacts().get(i), 
						environmentInstance);			
			productInstallator.installArtifact(productInstance, applicationRelease.getArtifacts().get(i));
			
		}
		
		//Create the Corresponding ApplicationInstance Object
		ApplicationInstance applicationInstance = new ApplicationInstance(
				applicationRelease,
				environmentInstance);
		
		applicationInstance.setVdc(vdc);
		applicationInstance.setStatus(Status.ARTEFACT_DEPLOYED);
		
		
		//Insert ApplicationInstance in DB
		try
		{
		
		applicationInstance = insertApplicationInstanceDB(applicationInstance);
		} 
		catch (InvalidEntityException e3) {
			throw new InvalidEntityException("Error to insert the application instante in the database");
		} catch (AlreadyExistsEntityException e3) {
			throw new InvalidEntityException("Error to insert the application instante in the database");
		}
		

		return applicationInstance;
	}

	public ApplicationInstance load(String vdc, String name) 
			throws EntityNotFoundException {
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
	
	
	public List<ApplicationInstance> findByCriteria(
		ApplicationInstanceSearchCriteria criteria) {	
		return applicationInstanceDao.findByCriteria(criteria);
	}
	
	public void uninstall(String org, String vdc, EnvironmentInstance environmentInstance,
			ApplicationInstance applicationInstance) throws ProductInstallatorException
	{
		//Install The applicationRelease=n-Artifacts
		for (int i=0; i < applicationInstance.getApplicationRelease().getArtifacts().size(); i++){
			// Install the artifact in the product instance associated
			//Obtain the VMs from EnvInstance where productRelease is installed
			ProductInstance productInstance 
				= getProductInstanceFromEnvironment( 
						applicationInstance.getApplicationRelease().getArtifacts().get(i), 
						environmentInstance);			
			productInstallator.uninstallArtifact(productInstance, applicationInstance.getApplicationRelease().getArtifacts().get(i));
			
		}
		
		applicationInstanceDao.remove(applicationInstance);
		
		
	}
	
	/**
	 * Store the applicationInstance Object in database
	 * @param applicationInstance
	 * @return the stored applicatonInstance
	 * @throws EntityNotFoundException 
	 * @throws AlreadyExistsEntityException 
	 * @throws InvalidEntityException 
	 */
	private ApplicationInstance storeApplicationInstance (
			ApplicationInstance applicationInstance,
			EnvironmentInstance environmentInstance, String vdc ) throws InvalidEntityException, 
			AlreadyExistsEntityException, EntityNotFoundException {
		
		
		try {
			applicationInstance = applicationInstanceDao.load(
					applicationInstance.getName() + "-" + environmentInstance.getName());
		//	applicationInstance.setVdc(vdc);
		//	applicationInstance.setStatus(Status.INSTALLED);
			applicationInstance = applicationInstanceDao.update(applicationInstance);
		
		} catch (EntityNotFoundException e){
		//	applicationInstance= new ApplicationInstance (applicationRelease, 
			//		environmentInstance);
			//applicationInstance.setVdc(vdc);
			//applicationInstance.setStatus(Status.INSTALLED);
			applicationInstance = applicationInstanceDao.create(applicationInstance);
		}
		
		return applicationInstance;

		
	}
	
	/**
	 * Insert/update ApplicationRelease in the paas-manager database
	 * @param application
	 * @return
	 * @throws ProductReleaseNotFoundException
	 * @throws ApplicationTypeNotFoundException
	 * @throws InvalidEntityException
	 * @throws AlreadyExistsEntityException
	 */
	private ApplicationInstance insertApplicationInstanceDB (
		ApplicationInstance application) throws ProductReleaseNotFoundException, 
		ApplicationTypeNotFoundException, InvalidEntityException, 
		AlreadyExistsEntityException {
		
		ApplicationInstance applicationInstance = null;
		Artifact artifact;
		ArtifactType artifactType;
		ApplicationType applicationType;
		List<Artifact> artifactsDB = new ArrayList ();
		List<ProductRelease> allProductReleases = new ArrayList<ProductRelease>();
		List<ProductRelease> productReleases = new ArrayList<ProductRelease>();

				
		List<Artifact> artifactsIn = new ArrayList<Artifact>();
				
		try {
			applicationInstance = applicationInstanceDao.load(
					application.getName());
		} catch (EntityNotFoundException e) {
			artifactsIn = application.getApplicationRelease().getArtifacts();
					
			for (int i=0; i< artifactsIn.size(); i++){
				ProductRelease productRelease = artifactsIn.get(i).getProductRelease();			
				try {
					productRelease = productReleaseDao.load(productRelease.getProduct()+"-"+productRelease.getVersion());
				} catch (EntityNotFoundException e1) {
					throw new ProductReleaseNotFoundException(productRelease);
				}
				
			
						
				try {
					artifact = artifactDao.load(artifactsIn.get(i).getName());
				} catch (EntityNotFoundException e1) {

							
					try {
						artifact = artifactDao.create(
							new Artifact(artifactsIn.get(i).getName(),
									artifactsIn.get(i).getPath(),
								null, productRelease));
						artifactsDB.add(artifact);
					} catch (InvalidEntityException e3) {
						throw new InvalidEntityException (e3);
					} catch (AlreadyExistsEntityException e3) {
						throw new AlreadyExistsEntityException (e3);
					}
				}

						
				if (!isPRInAllProductReleases(productRelease, allProductReleases)) {
					productReleases.add(productRelease);
				}
				allProductReleases.add(productRelease);			
			}

			ApplicationRelease applicationRelease = null;
					
			try {
				applicationRelease = applicationReleaseDao.create(
					new ApplicationRelease(
						application.getApplicationRelease().getName(), 
						application.getApplicationRelease().getVersion(),
						application.getApplicationRelease().getDescription(),
						null,
					
						application.getApplicationRelease().getTransitableReleases(),
						artifactsDB));		
			} catch (InvalidEntityException e3) {
				throw new InvalidEntityException (e3);
			} catch (AlreadyExistsEntityException e3) {
				throw new AlreadyExistsEntityException (e3);
			}
			
			try {
				applicationInstance = applicationInstanceDao.create(
					new ApplicationInstance (
							applicationRelease, 
						application.getEnvironmentInstance(),
						application.getVdc(),
						application.getStatus()));		
			} catch (InvalidEntityException e3) {
				throw new InvalidEntityException (e3);
			} catch (AlreadyExistsEntityException e3) {
				throw new AlreadyExistsEntityException (e3);
			}
		}
		return applicationInstance;
	}
	/**
	 * Insert/update ApplicationRelease in the paas-manager database
	 * @param application
	 * @return
	 * @throws ProductReleaseNotFoundException
	 * @throws ApplicationTypeNotFoundException
	 * @throws InvalidEntityException
	 * @throws AlreadyExistsEntityException
	 */
	private ApplicationRelease insertApplicationReleaseDB (
		ApplicationRelease application) throws ProductReleaseNotFoundException, 
		ApplicationTypeNotFoundException, InvalidEntityException, 
		AlreadyExistsEntityException {
		
		//ApplicationRelease Recovery
		ApplicationRelease applicationRelease = null;
		Artifact artifact;
		ArtifactType artifactType;
		ApplicationType applicationType;
		List<ProductRelease> allProductReleases = new ArrayList<ProductRelease>();
		List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
		List<Artifact> artifactsDB = new ArrayList<Artifact>();
				
		List<Artifact> artifactsIn = new ArrayList<Artifact>();
				
		try {
			applicationRelease = applicationReleaseDao.load(
					application.getName() + "-" + application.getVersion());
		} catch (EntityNotFoundException e) {
			artifactsIn = application.getArtifacts();
					
			for (int i=0; i< artifactsIn.size(); i++){
				ProductRelease productRelease = artifactsIn.get(i).getProductRelease();			
				try {
					productRelease = productReleaseDao.load(productRelease.getName());
				} catch (EntityNotFoundException e1) {
					throw new ProductReleaseNotFoundException(productRelease);
				}
						
				try {
					artifact = artifactDao.load(artifactsIn.get(i).getName());
				} catch (EntityNotFoundException e1) {
					//Insert Artifact with ArtifactType and ProductRelease in BBDD
					try {
						artifactType = 	artifactTypeDao.load(artifactsIn.get(i)
							.getArtifactType().getName());
					} catch (EntityNotFoundException e2) {
						throw new ApplicationTypeNotFoundException (e2);
					}
							
					try {
						artifact = artifactDao.create(
							new Artifact(artifactsIn.get(i).getName(),
									artifactsIn.get(i).getPath(),
								artifactType, productRelease));
					} catch (InvalidEntityException e3) {
						throw new InvalidEntityException (e3);
					} catch (AlreadyExistsEntityException e3) {
						throw new AlreadyExistsEntityException (e3);
					}
				}
						
				artifactsDB.add(artifact);
						
				if (!isPRInAllProductReleases(productRelease, allProductReleases)) {
					productReleases.add(productRelease);
				}
				allProductReleases.add(productRelease);			
			}
					
			//Insert applcationRelease 
			applicationType = application.getApplicationType();
			try {
				applicationType = applicationTypeDao.load(applicationType.getName());
			} catch (EntityNotFoundException enfe) {
				throw new ApplicationTypeNotFoundException (enfe);
			}
					
			try {
				applicationRelease = applicationReleaseDao.create(
					new ApplicationRelease (
						application.getName(), 
						application.getVersion(),
						application.getDescription(),
						applicationType,
					
						application.getTransitableReleases(),
						artifactsDB));		
			} catch (InvalidEntityException e3) {
				throw new InvalidEntityException (e3);
			} catch (AlreadyExistsEntityException e3) {
				throw new AlreadyExistsEntityException (e3);
			}
		}
		return applicationRelease;
	}
	
	/**
	 * Build the ProductInstance to be installed
	 * @param vm
	 * @param appRelease
	 * @return the productInstance to be installed
	 */
	private ProductInstance buildProductInstance (String vdc, TierInstance tierInstance,
			Artifact artifact, ApplicationRelease appRelease) {
		
		String name = artifact.getName();
		ProductRelease pRelease = new ProductRelease(name, appRelease.getVersion());
		pRelease.setDescription(appRelease.getDescription() + " ArtifactDesc="
				+ artifact.getName());
		List<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute ("location", artifact.getPath()));
		pRelease.setAttributes(attributes);
		
		pRelease.setSupportedOOSS(artifact.getProductRelease().getSupportedOOSS());
		pRelease.setProductType(artifact.getProductRelease().getProductType());
		
		//ApplicationInstance
		ProductInstance productInstance = new ProductInstance(
				pRelease, Status.UNINSTALLED, vdc);
		
		return productInstance;
	}
	
	/**
	 * Get the VMs where the artifact/ProductRelease should be installed
	 * @param artifact
	 * @param envInstance
	 * @return the VMs where the artifact/ProductRelease should be installed
	 */
	private List<VM> getVMsFromEnvironment (Artifact artifact, 
		EnvironmentInstance envInstance) {
		
		List<VM> vms = new ArrayList<VM>();
		String pReleaseName = artifact.getProductRelease().getProduct();
		
		List<TierInstance> tierInstances = envInstance.getTierInstances();
		for (int i=0; i < tierInstances.size(); i++) {
			if (isProductInTierInstance(pReleaseName, tierInstances.get(i))){
				vms.add(tierInstances.get(i).getVM());
			}		
		}
		return vms;
	}
	
	/**
	 * Get the VMs where the artifact/ProductRelease should be installed
	 * @param artifact
	 * @param envInstance
	 * @return the VMs where the artifact/ProductRelease should be installed
	 */
	private ProductInstance getProductInstanceFromEnvironment (Artifact artifact, 
		EnvironmentInstance envInstance) {
		
	//	
		ProductInstance productInstance = null;
		String pReleaseName = artifact.getProductRelease().getProduct();
		
		List<TierInstance>  tierInstances= envInstance.getTierInstances();
		// For each tier instance
		for (TierInstance tierInstance: tierInstances) {
			// Check product instnaces where to install the artifacts
			for (ProductInstance deployedProductInstance: tierInstance.getProductInstances())
			{
				if (deployedProductInstance.getProductRelease().getProduct().equals(
						artifact.getProductRelease().getProduct()))
					
				{
					productInstance= deployedProductInstance;
				}
			}
			
		}
		return productInstance;
	}
	/**
	 * Tells if the corresponding product is present in the corresponding 
	 * TierInstance
	 * @param productName
	 * @param tierInstance
	 * @return true/false
	 */
	private boolean isProductInTierInstance(String productName, 
			TierInstance tierInstance){
		for (int i = 0; i < tierInstance.getProductInstances().size(); i++){
			if (tierInstance.getProductInstances().get(i).getProductRelease()
					.getProduct().equals(productName)){
				return true;
			}
		}
		return false;
	}
	
	private boolean isPRInAllProductReleases(ProductRelease productRelease, 
			List<ProductRelease> allProductReleases){	
		for (int i =0; i<allProductReleases.size(); i++){
			if (productRelease.getProduct()
					.equals(allProductReleases.get(i).getProduct())){
						return true;
					}
		}
		return false;
	}
	/**
	 * Check if any productRelease coming in the Application Release Object is
	 * not in the EnvironmentInstance. In this case the method return false
	 * @param envInstance
	 * @param applicationRelease
	 * @return true/false
	 */
	private boolean canInstall(EnvironmentInstance envInstance,
		List<Artifact> applicationArtifact) {
		
		List<ProductRelease> productReleasesApp = new ArrayList<ProductRelease>();
		for(Artifact artifact: applicationArtifact){
			productReleasesApp.add(artifact.getProductRelease());
		}
		
		List<ProductRelease> productReleasesEnv = new ArrayList<ProductRelease>();
		for(int i= 0; i<envInstance.getTierInstances().size(); i++){
			TierInstance tInstance = envInstance.getTierInstances().get(i);
			
			for (int j=0; j < tInstance.getProductInstances().size(); j++) {
				ProductInstance pInstance = tInstance.getProductInstances().get(j);
				productReleasesEnv.add(pInstance.getProductRelease());				
			}
				
		}
		
		for (int i=0; i<productReleasesApp.size(); i++){
			if (!(isProductReleaseinProductList(productReleasesApp.get(i),
					productReleasesEnv)))
				return false;
		}
		return true;
		
	}
	
	/**
	 * Evaluates if a productRelease is included in a productReleaseList by
	 * looking at the productRelease Name.
	 * @param pRelease
	 * @param productList
	 * @return true/false
	 */
	private boolean isProductReleaseinProductList(ProductRelease pRelease,
			List<ProductRelease> productList) {
		
		boolean answer = true;
		List<String> pnameList = new ArrayList<String>();
		
		for (int i= 0; i<productList.size(); i++){
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
    public void setApplicationInstanceDao(
    		ApplicationInstanceDao applicationInstanceDao) {
        this.applicationInstanceDao = applicationInstanceDao;
    }

    /**
     * @param environmentInstanceDao
     *            the environmentInstanceDao to set
     */
    public void setEnvironmentInstanceDao(
    		EnvironmentInstanceDao environmentInstanceDao) {
        this.environmentInstanceDao = environmentInstanceDao;
    }
    
    /**
     * @param artifactTypeDao
     *            the artifactTypeDao to set
     */
    public void setArtifactTypeDao(
    		ArtifactTypeDao artifactTypeDao) {
        this.artifactTypeDao = artifactTypeDao;
    }
    
    /**
     * @param artifactDao
     *            the artifactDao to set
     */
    public void setArtifactDao(
    		ArtifactDao artifactDao) {
        this.artifactDao = artifactDao;
    }
    
    /**
     * @param applicationTypeDao
     *            the applicationTypeDao to set
     */
    public void setApplicationTypeDao(
    		ApplicationTypeDao applicationTypeDao) {
        this.applicationTypeDao = applicationTypeDao;
    }
    
    /**
     * @param productInstallator
     *            the productInstallator to set
     */
    public void setProductInstallator(ProductInstallator productInstallator) {
        this.productInstallator = productInstallator;
    }
    
 }
