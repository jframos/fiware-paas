package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.paasmanager.dao.TierDao;
import com.telefonica.euro_iaas.paasmanager.dao.TierInstanceDao;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;

public class TierInstanceManagerImpl implements
		TierInstanceManager {
	
	private TierInstanceDao tierInstanceDao;
	private TierDao tierDao;
	private ProductInstanceDao productInstanceDao;
	
	@Override
	public TierInstance update(TierInstance tierInstance)
			throws EntityNotFoundException, InvalidEntityException,
			AlreadyExistsEntityException {
		
		if (tierInstance.getId()!= null)
			tierInstance = tierInstanceDao.load(tierInstance.getId());
		else {
			tierInstance = tierInstanceInsertBD (tierInstance);
		}
		return tierInstance;
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
		String name = "";
		for (int i = 0; i < productInstances.size(); i++) {
			if (productInstances.get(i).getId() != null)
				productInstance = productInstanceDao.load(productInstances.get(i).getId());			
			else
				productInstance = productInstanceDao.create(productInstances.get(i));
			
			productInstancesBD.add(productInstance);
			name = name + productInstance.getName() + "-" ;
		}
		tierInstance.setProductInstances(productInstancesBD);
		tierInstance.setName(tier.getName() + "-" + name);
		tierInstance = tierInstanceDao.create(tierInstance);
		
		return tierInstance;
		
		
	}
    
	/**
     * @param tierDao
     *            the tierDao to set
     */
    public void setTierDao(TierDao tierDao) {
        this.tierDao = tierDao;
    }
 
    /**
     * @param productInstanceDao
     *            the productInstanceDao to set
     */
    public void setProductInstanceDao(ProductInstanceDao productInstanceDao) {
        this.productInstanceDao = productInstanceDao;
    }
    
    /**
     * @param tierInstanceDao
     *            the tierInstanceDao to set
     */
    public void setTierInstanceDao(TierInstanceDao tierInstanceDao) {
        this.tierInstanceDao = tierInstanceDao;
    }

}
