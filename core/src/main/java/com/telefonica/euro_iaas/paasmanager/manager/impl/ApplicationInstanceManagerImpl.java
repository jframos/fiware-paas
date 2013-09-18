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
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.exception.ApplicationTypeNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.paasmanager.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationInstance;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationType;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.ArtifactType;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;


/**
 * Application Instance Manager operations: install
 * @author Jesus M. Movilla
 */
public class ApplicationInstanceManagerImpl implements
		ApplicationInstanceManager {

	private ApplicationReleaseDao applicationReleaseDao;
	private ApplicationInstanceDao applicationInstanceDao;
	private ProductReleaseDao productReleaseDao;
	private ArtifactDao artifactDao;
	private ArtifactTypeDao artifactTypeDao;
	private ApplicationTypeDao applicationTypeDao;
	
	
	 /**
     * Install the applicationIntance on a EnvironmetInstane
     * @param vdc the vdc where the instance will be installed
     * @param environmentInstance the environmentInstance on which the application
     * is ging to be installed
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
	@Override
	public ApplicationInstance install(String vdc,
			EnvironmentInstance environmentInstance,
			ApplicationRelease application) 
					throws ProductReleaseNotFoundException, 
					InvalidEntityException, 
					AlreadyExistsEntityException, 
					ApplicationTypeNotFoundException {

		//¿Validation?
		//The EnvironmentInstance exists and has been loaded from Database
		//ApplicationRelease should be loaded from database or inserted in database
		//Installation of the application on the environment
				// 1.) From Application extract the artifacts 
				//     and the products on which they should be installed 
				// 2.) Check the environment and extract the list of productInstances
				// 3.) The list of products from 1 should be the same as the productInstances
				// 4.) Contact the SDC to install the application (problem: The application
				//     should be assigned to a VM if we use SDC.... Maybe the SDC can install
				//     the application as a set of products (sofwtare that goes in a VM 
				// 	   can be a product
				// Insert the applicationInstance in the database
		
		//ProductInstancesRecovery		
		HashMap hm 
			= getProductInstancesFromEnvironmentInstance(environmentInstance);
		
		//ApplicationRelease Recovery
		ApplicationRelease applicationRelease = null;
		Artifact artifact;
		ArtifactType artifactType;
		ApplicationType applicationType;
		List<ProductRelease> allProductReleases = new ArrayList<ProductRelease>();
		List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
		List<Artifact> artifactsDB = new ArrayList<Artifact>();
		
		List<Artifact> artifacts = new ArrayList<Artifact>();
		
		try {
			applicationRelease = applicationReleaseDao.load(application.getId());
		} catch (EntityNotFoundException e) {
			artifacts = application.getArtifacts();
			
			for (int i=0; i< artifacts.size(); i++){
				ProductRelease productRelease = artifacts.get(i).getProductRelease();			
				try {
					productRelease = productReleaseDao.load(productRelease.getName());
				} catch (EntityNotFoundException e1) {
					throw new ProductReleaseNotFoundException(productRelease);
				}
				
				try {
					artifact = artifactDao.load(artifacts.get(i).getName());
				} catch (EntityNotFoundException e1) {
					//Insert Artifact with ArtifactType and ProductRelease in BBDD
					try {
						artifactType = 	artifactTypeDao.load(artifacts.get(i)
										.getArtifactType().getName());
					} catch (EntityNotFoundException e2) {
						throw new ApplicationTypeNotFoundException (e2);
					}
					
					try {
						artifact = artifactDao.create(
							new Artifact(artifacts.get(i).getName(),
								artifacts.get(i).getPath(),
								artifactType, productRelease));
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
							application.getAttributes(),
							application.getTransitableReleases(),
							artifacts));
			
			} catch (InvalidEntityException e3) {
				throw new InvalidEntityException (e3);
			} catch (AlreadyExistsEntityException e3) {
				throw new AlreadyExistsEntityException (e3);
			}
			// Insert ApplicationRelease in database
			e.printStackTrace();
		}
		
		
		//
		return null;
	}

	
	private boolean isPRInAllProductReleases(ProductRelease productRelease, 
			List<ProductRelease> allProductReleases){
		
		for (int i =0; i<allProductReleases.size(); i++){
			if (productRelease.getName()
					.equals(allProductReleases.get(i).getName())){
						return true;
					}
		}
		return false;
	}
	
	private HashMap getProductInstancesFromEnvironmentInstance (
			EnvironmentInstance envInstance) {
		
		List<TierInstance> tierInstances = envInstance.getTierInstances();
	
		HashMap hm = new HashMap();
		for (int i=0; i< tierInstances.size(); i++){
			hm.put(tierInstances.get(i).getName(), 
				tierInstances.get(i).getProductInstances());
		}
		return hm;
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
    
}
