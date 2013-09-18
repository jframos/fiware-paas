package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentDao;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.dao.TierDao;
import com.telefonica.euro_iaas.paasmanager.dao.TierInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager;
import com.telefonica.euro_iaas.paasmanager.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;

public class EnvironmentInstanceManagerImpl implements
		EnvironmentInstanceManager {
	
	private EnvironmentInstanceDao environmentInstanceDao;
	private EnvironmentDao environmentDao;
	private TierInstanceDao tierInstanceDao;
	private TierDao tierDao;
	private ProductReleaseDao productReleaseDao;
	private ProductInstanceDao productInstanceDao;
	
	private ProductInstanceManager productInstanceManager;
	private EnvironmentManager environmentManager;
	private InfrastructureManager infrastructureManager;
	
	@Override
	public EnvironmentInstance create(String vdc, Environment environment)
	//public EnvironmentInstance create(EnvironmentInstance environmentInstance)
	throws EntityNotFoundException, InvalidEntityException, AlreadyExistsEntityException, 
			NotUniqueResultException {
		
		//validar que el el objeto viene con el name.
		environment = environmentManager.load(environment.getName());
	
		//How many vms are required for this environment?
		List<Tier> tiers = environment.getTiers();
		int number_vms = 0;
		for (int i =0; i< tiers.size(); i++){
			number_vms = number_vms + tiers.get(i).getInitial_number_instances();
		}
	
		//Getting VMs to Install Products
		List<VM> vms = infrastructureManager.getVMs(new Integer(number_vms));
	
		List<TierInstance> tierInstances = new ArrayList<TierInstance>();
		TierInstance tierInstance = null;
		
		//Installing ProductReleases
		for (int i=0; i<tiers.size(); i++){
			List<ProductRelease> productReleases = tiers.get(i).getProductReleases();
			VM vm = vms.get(i);
			List <ProductInstance> productInstances = new ArrayList<ProductInstance>();
			for (int j=0; j<productReleases.size();j++){
				ProductInstance productInstance 
					= productInstanceManager.install(vm, vdc, productReleases.get(j), productReleases.get(j).getAttributes());
				productInstances.add(productInstance);
			}
			tierInstance = new TierInstance(tiers.get(i),productInstances);
			
			tierInstances.add(tierInstance);
		}
	
		EnvironmentInstance environmentInstance 
			= new EnvironmentInstance(environment, tierInstances);
		
		EnvironmentInstance environmentInstanceDB = null;
		
		try {
			environmentInstanceDB = environmentInstanceDao.load(environmentInstance.getName());
		} catch (EntityNotFoundException e) {
			environmentInstanceDB = insertEnvironmentInstanceDB (environmentInstance);
		}
		
		return environmentInstanceDB;
	}

	@Override
	public EnvironmentInstance load(String name) throws EntityNotFoundException {
		return environmentInstanceDao.load(name);
	}
	
	
	//PRVATE METHODS
	private EnvironmentInstance insertEnvironmentInstanceDB (
		EnvironmentInstance environmentInstance) throws InvalidEntityException, AlreadyExistsEntityException, EntityNotFoundException {
		
		Environment environment = insertEnvironmentDB(environmentInstance.getEnvironment());
		
		environmentInstance.setEnvironment(environment);
		
		List <TierInstance> tierInstances = insertTierInstancesBD (environmentInstance.getTierInstances());
		
		environmentInstance.setTierInstances(tierInstances);
		
		environmentInstance.setStatus(Status.INSTALLED);
		environmentInstance.setName(environmentInstance.getName());
		environmentInstance = environmentInstanceDao.create(environmentInstance);
		
		return environmentInstance;
	}
	
	private Environment insertEnvironmentDB (Environment environment) 
			throws InvalidEntityException, AlreadyExistsEntityException, EntityNotFoundException {	

		try {
			environment = environmentDao.load(environment.getName());
		} catch (EntityNotFoundException e) {
			
			List<Tier> tiers = insertTiersBD(environment.getTiers());
			try {
				environment.setTiers(tiers);
				environment = environmentDao.create(environment);
			} catch (InvalidEntityException e1) {

			} catch (AlreadyExistsEntityException e1) {

			}
			
		}
		return environment;
	}
	
	private List<Tier> insertTiersBD (List<Tier> tiers) throws 
		InvalidEntityException, AlreadyExistsEntityException, EntityNotFoundException {
		
		Tier tier;
		List<Tier> tiersDB = new ArrayList<Tier>();
		for (int i=0; i< tiers.size(); i++){
			if (tiers.get(i).getId() != null)
				tier = tierDao.load(tiers.get(i).getId());
			else
				tier = insertTierDB (tiers.get(i));			
			tiersDB.add(tier);
		}
		
		return tiersDB;		
	}
	
		private Tier insertTierDB (Tier tier) throws InvalidEntityException, AlreadyExistsEntityException {
		
		List<ProductRelease> productReleases = tier.getProductReleases();
		List<ProductRelease> productReleasesDB = new ArrayList<ProductRelease>(); 
		
		ProductRelease productRelease;
		
		for (int i =0; i <productReleases.size(); i++){
			
			try {
				productRelease = productReleaseDao.load(productReleases.get(i).getName());
			} catch (EntityNotFoundException e) {
				productRelease = productReleaseDao.create(productReleases.get(i));
			}
			productReleasesDB.add(productRelease);
			
		}
		
		tier.setProductReleases(productReleasesDB);
		
		if (tier.getId() != null)
			try {
				tier = tierDao.load(tier.getId());
			} catch (EntityNotFoundException e) {
				tier = tierDao.create(tier);
			}
		else
			tier = tierDao.create(tier);
				
		return tier;
	}
	
	private List<TierInstance> 	insertTierInstancesBD (List<TierInstance> tierInstances) 
			throws EntityNotFoundException, InvalidEntityException, AlreadyExistsEntityException  {
		
		TierInstance tierInstance = null;
		List<TierInstance> tierInstancesDB = new ArrayList<TierInstance>();
		for (int i=0; i<tierInstances.size(); i++){
			if (tierInstances.get(i).getId() != null){
				try {
					tierInstance = tierInstanceDao.load(tierInstances.get(i).getId());
				} catch (EntityNotFoundException e) {
					tierInstance = tierInstanceInsertBD(tierInstances.get(i));
				}
			}else 
				tierInstance = tierInstanceInsertBD(tierInstances.get(i));
			
			tierInstancesDB.add(tierInstance);
		}
		return tierInstances;
	}
	
	private TierInstance tierInstanceInsertBD(TierInstance tierInstance) 
			throws EntityNotFoundException, InvalidEntityException, 
			AlreadyExistsEntityException {
		
		Tier tier = null;
		
		if (tierInstance.getTier().getId() != null)
			tier = tierDao.load(tierInstance.getTier().getId());
		else
			tier = tierDao.create(tierInstance.getTier());
		
		tierInstance.setTier(tier);
		
		List<ProductInstance> productInstances = tierInstance.getProductInstances();
		List<ProductInstance> productInstancesBD = new ArrayList<ProductInstance>();
		ProductInstance productInstance = null;

		for (int i = 0; i < productInstances.size(); i++) {
			if (productInstances.get(i).getId() != null)
				productInstance = productInstanceDao.load(productInstances.get(i).getId());			
			else
				productInstance = productInstanceDao.create(productInstances.get(i));
			
			productInstancesBD.add(productInstance);
		}
		tierInstance.setProductInstances(productInstancesBD);
		tierInstance.setStatus(Status.INSTALLED);
		tierInstance = tierInstanceDao.create(tierInstance);
	
		return tierInstance;		
	}
		
	
    /**
     * @param environmentDao
     *            the environmentDao to set
     */
    public void setEnvironmentDao(
    		EnvironmentDao environmentDao) {
        this.environmentDao = environmentDao;
    }

    /**
     * @param tierInstanceDao
     *            the tierInstanceDao to set
     */
    public void setTierInstanceDao(
    		TierInstanceDao tierInstanceDao) {
        this.tierInstanceDao = tierInstanceDao;
    }

    /**
     * @param tierDao
     *            the tierDao to set
     */
    public void setTierDao(
    		TierDao tierDao) {
        this.tierDao = tierDao;
    }

    /**
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    public void setProductReleaseDao(
    		ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

    /**
     * @param productInstanceDao
     *            the productInstanceDao to set
     */
    public void setProductInstanceDao(ProductInstanceDao productInstanceDao) {
        this.productInstanceDao = productInstanceDao;
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
     * @param productInstanceManager
     *            the productInstanceManager to set
     */
    public void setProductInstanceManager(
            ProductInstanceManager productInstanceManager) {
        this.productInstanceManager = productInstanceManager;
    }
    
    /**
     * @param environmentManager
     *            the environmentManager to set
     */
    public void setEnvironmentManager(
    		EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }
    
    /**
     * @param infrastructureManager
     *            the infrastructureManager to set
     */
    public void setInfrastructureManager(
            InfrastructureManager infrastructureManager) {
        this.infrastructureManager = infrastructureManager;
    }
}
