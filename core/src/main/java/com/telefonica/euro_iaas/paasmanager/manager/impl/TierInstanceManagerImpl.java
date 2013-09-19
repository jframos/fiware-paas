package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.TierInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidProductInstanceRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReconfigurationException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager;
import com.telefonica.euro_iaas.paasmanager.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierInstanceSearchCriteria;


public class TierInstanceManagerImpl implements
		TierInstanceManager {
	
	private TierInstanceDao tierInstanceDao;
	private TierManager tierManager;
	private ProductInstanceManager productInstanceManager ;
	private InfrastructureManager infrastructureManager;
	private ProductReleaseManager productReleaseManager;
	private EnvironmentInstanceManager environmentInstanceManager;
	
	private static Logger log = Logger.getLogger(TierInstanceManagerImpl.class);
	
	public TierInstance update(TierInstance tierInstance)
			throws EntityNotFoundException, InvalidEntityException,
			AlreadyExistsEntityException {
		
		if (tierInstance.getId()!= null)
			tierInstance = tierInstanceDao.update(tierInstance);
			
		else {
			tierInstance = create (tierInstance);
		}
		return tierInstance;
	}
	
	
	public void update(ClaudiaData claudiaData, TierInstance tierInstance,
			EnvironmentInstance envInstance) 
					throws ProductInstallatorException, InvalidEntityException, 
					AlreadyExistsEntityException, EntityNotFoundException, ProductReconfigurationException {
		
		if ((tierInstance.getTier().getProductReleases() != null)
				&& (tierInstance.getTier().getProductReleases().size() != 0)) {

			for (ProductInstance productInstance : tierInstance.getProductInstances()){
				
				List <Attribute> atributes = getPICAttributesOVF(tierInstance.getVM().getVmOVF());
				productInstanceManager.configure(productInstance, 
						atributes);
			}
		}
	}
	
	private List<Attribute> getPICAttributesOVF(String vmOVF) {
		List <Attribute> picAtributes = new ArrayList<Attribute>();
		String first, key, value, property;
		String[] lValue, lKey;

		if(vmOVF.contains("ovfenvelope:value=\"PIC\"")){
			first = vmOVF.split("ovfenvelope:value=\"PIC\"",2)[1];
			property = first.split("Product Specific Attributes</ovfenvelope:Category>",2)[1];
			while(property.contains("<ovfenvelope:Property")){
				lValue = property.split("ovfenvelope:value=\"",2)[1].split("\"", 2);
				value = lValue[0];
				lKey = lValue[1].split("key=\"",2)[1].split("\"", 2);
				key = lKey[0];
				property = lKey[1];
				Attribute attrib = new Attribute(key, value);
				picAtributes.add(attrib);
			}
			return picAtributes;
		}else
			return null;
	}

	public void create(ClaudiaData claudiaData,
			TierInstance tierInstance, EnvironmentInstance envInstance) throws InfrastructureException, 
			EntityNotFoundException, 
			InvalidEntityException, AlreadyExistsEntityException, 
			NotUniqueResultException, InvalidProductInstanceRequestException {
	
		// Needed to recover the number of replicas TOBEDONE
		int replicaNumber = tierInstance.getNumberReplica();
		
		VM vm = infrastructureManager.deployVM(claudiaData, tierInstance.getTier(),
				tierInstance.getOvf(), replicaNumber);
		
		tierInstance.setVM(vm);
		
		String[] name =tierInstance.getName().split("-", 3);
		try{
			int replicaNumberAnt = (Integer.parseInt(name[2]))-1;
			if(replicaNumberAnt > 0){
				String antName = name[0] + "-" + name[1] + "-" + (replicaNumberAnt);
				TierInstance tierInstanceAnt = load(antName);
				tierInstance.setProductInstances(tierInstanceAnt.getProductInstances());
			}
		}catch (IllegalArgumentException e){
			log.error("Cannot parse the number of replica in the reconfigutation option: " +e);
		}

		tierInstance.setStatus(Status.INSTALLED);
		TierInstance tierInstanceNew = create(tierInstance);
		envInstance.addTierInstance(tierInstanceNew);
		environmentInstanceManager.update(envInstance);

	}
	
	
	public void delete(ClaudiaData claudiaData,	TierInstance tierInstance, 
			EnvironmentInstance envInstance) throws InfrastructureException, 
			InvalidEntityException, EntityNotFoundException{

		infrastructureManager.deleteVMReplica(claudiaData, tierInstance);
		envInstance.removeTierInstance(tierInstance);
		environmentInstanceManager.update(envInstance);
		remove(tierInstance);
			
	}
	
	
	
	public List<TierInstance> findByCriteria(TierInstanceSearchCriteria criteria) 
			throws EntityNotFoundException {

		return tierInstanceDao.findByCriteria(criteria);
	}
	
	public List<TierInstance> findByEnvironment(String vdc, 
			EnvironmentInstance environmentInstance) 
					throws EntityNotFoundException {
		TierInstanceSearchCriteria criteria = new TierInstanceSearchCriteria();
		criteria.setEnvironmentInstance(environmentInstance);
		criteria.setVdc(vdc);

		List<TierInstance> tierInstances = 
				findByCriteria(criteria);
		
		return tierInstances;
	}
	
	
	
	


	public TierInstance load (String name) throws EntityNotFoundException {
		return tierInstanceDao.load(name);
	}
	   
    /**
     * @param tierInstanceDao
     *            the tierInstanceDao to set
     */
    public void setTierInstanceDao(TierInstanceDao tierInstanceDao) {
        this.tierInstanceDao = tierInstanceDao;
    }

	public TierInstance load(long id) throws EntityNotFoundException {
		return tierInstanceDao.findByTierInstanceId(id);
	}
	


	public TierInstance loadByName(String name) throws EntityNotFoundException {
		return tierInstanceDao.findByTierInstanceName(name);
	}
    /**
     * @param infrastructureManager
     *            the infrastructureManager to set
     *            
     *            <property name="tierInstanceDao" ref="tierInstanceDao"/>
     
      
       
      
     */
    public void setInfrastructureManager(
            InfrastructureManager infrastructureManager) {
        this.infrastructureManager = infrastructureManager;
    }

    public void setEnvironmentInstanceManager(
    		EnvironmentInstanceManager environmentInstanceManager) {
        this.environmentInstanceManager = environmentInstanceManager;
    }
    
    public void setProductInstanceManager(
    		ProductInstanceManager productInstanceManager) {
        this.productInstanceManager = productInstanceManager;
    }
    

    public void setTierManager(
    		TierManager tierManager) {
        this.tierManager = tierManager;
    }

	public void remove(TierInstance tierInstance)  throws  InvalidEntityException {

		
		try {
			tierInstance = load(tierInstance.getName());
		} catch (EntityNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<ProductInstance> productInstances= tierInstance.getProductInstances();
		tierInstance.setProductInstances(null);
		try {
			tierInstanceDao.update(tierInstance);
		} catch (InvalidEntityException e) {
			// TODO Auto-generated catch block
			throw new InvalidEntityException( "Error to delete the Tier Instance " + tierInstance.getName());
		}
		if (productInstances != null && productInstances.size()>0)
		{
		for (ProductInstance productInstance: productInstances )
		{
			try
			{
			productInstanceManager.remove(productInstance);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		}
		
		tierInstanceDao.remove(tierInstance);
		
	}

	public TierInstance create(TierInstance tierInstance) throws  InvalidEntityException {
        
		TierInstance tierInstanceDB = new TierInstance();
		
		try
		{
		   tierInstanceDao.load(tierInstance.getName());
		   throw new InvalidEntityException( "Error to create the Tier Instance " + tierInstance.getName() +". It already exists. ");
		} catch (EntityNotFoundException e) {

		}
	
		Tier tierDB = null;
		try
		{
		   tierDB = tierManager.load(tierInstance.getTier().getName());
		} catch (EntityNotFoundException e) {
			
			throw new InvalidEntityException( "Error to load the Tier " + tierInstance.getTier().getName() +" : " +e.getMessage());
		
		}
		tierInstanceDB.setTier(tierDB);

		
		if (tierInstance.getProductInstances() != null)
		{
		for (ProductInstance productInstance: tierInstance.getProductInstances()) {
			
			ProductInstance productInstanceDB = null;
			try
			{
				productInstanceDB = productInstanceManager.load(productInstance.getName());
			} catch (EntityNotFoundException e) {
				try {
					productInstanceDB = productInstanceManager.create(productInstance);
				} catch (InvalidEntityException e1) {
					
					throw new InvalidEntityException("Error to create the product instance " + productInstance.getName() + " : " + e1.getMessage());
				} catch (AlreadyExistsEntityException e1) {
					
					throw new InvalidEntityException("Error to create the product instance " + productInstance.getName() + " : " + e1.getMessage());
				} catch (InvalidProductInstanceRequestException e1) {
					// TODO Auto-generated catch block
					throw new InvalidEntityException("Error to create the product instance " + productInstance.getName() + " : " + e1.getMessage());
				}

			}
			tierInstanceDB.addProductInstance(productInstanceDB);
					
			
		  }
		}
	
		tierInstanceDB.setName(tierInstance.getName());
		tierInstanceDB.setVM(tierInstance.getVM());
		tierInstanceDB.setOvf(tierInstance.getOvf());

		tierInstanceDB.setNumberReplica(tierInstance.getNumberReplica());

		try {
			tierInstance = tierInstanceDao.create(tierInstanceDB);
		} catch (AlreadyExistsEntityException e) {
			// TODO Auto-generated catch block
			throw new InvalidEntityException("Error to create the tier instance " + tierInstanceDB.getName() + " : " + e.getMessage());
		}
		
		return tierInstance;
	}   
}