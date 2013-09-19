package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.dao.TierDao;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierSearchCriteria;

public class TierManagerImpl implements
		TierManager {
	
	private TierDao tierDao;
	
	private ProductReleaseDao productReleaseDao;
	
	public Tier update(Tier tier)
			throws EntityNotFoundException, InvalidEntityException,
			AlreadyExistsEntityException {
		
		if (tier.getId()!= null)
			tier = tierDao.load(tier.getName());
		else {
			tier = tierInsertBD (tier);
		}
		return tier;
	}
	
	
	private Tier tierInsertBD(Tier tier) 
			throws EntityNotFoundException, InvalidEntityException, 
			AlreadyExistsEntityException {
		
		if (tier.getId() != null)
			tier = tierDao.load(tier.getName());
		else
			tier = tierDao.create(tier);
		
		//lo que añado yo
		
		List<ProductRelease> productReleases = tier.getProductReleases();
		List<ProductRelease> productReleasesBD = new ArrayList<ProductRelease>();
		ProductRelease productRelease = null;
		
				
		String name = "";
		for (int i = 0; i < productReleases.size(); i++) {
			if (productReleases.get(i).getId() != null)
				productRelease = productReleaseDao.load(productReleases.get(i).getName());			
			else
				productRelease = productReleaseDao.create(productReleases.get(i));
			
			productReleasesBD.add(productRelease);
			name = name + productRelease.getAttributes().get(i).getValue() + "-" ;
			
		}
		tier.setProductReleases(productReleases);
		tier.setInitial_number_instances(tier.getInitial_number_instances());
		tier.setMaximum_number_instances(tier.getMaximum_number_instances());
		tier.setMinimum_number_instances(tier.getMinimum_number_instances());
		tier.setName(name);
		
		
		tier = tierDao.create(tier);
		
		return tier;
		
		
	}
    
	/**
     * @param tierDao
     *            the tierDao to set
     */
    public void setTierDao(TierDao tierDao) {
        this.tierDao = tierDao;
    }
 

    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }

    public Tier create(Tier tier) throws InvalidEntityException {
    	
    	if (tier.getId() != null)
    	{
			try {
				tier = tierDao.load(tier.getName());
				return tier;
			} catch (EntityNotFoundException e) {
				
				throw new InvalidEntityException("Error to create the Tier " + tier.getName()+ " : " + e.getMessage());
			
			}
    	}
    
    	return create (tier.getName(),tier.getImage(),tier.getFlavour(),tier.getProductReleases(),tier.getInitial_number_instances(), 
    			tier.getMaximum_number_instances(), tier.getMinimum_number_instances());
    			
    }

	public Tier create(String name, String image, String flavour, List<ProductRelease> productReleases , int initial_number_instances, int maximum_number_instances,
			int minimum_number_instances) throws InvalidEntityException {
		Tier tier = new Tier ();
		
		tier.setInitial_number_instances(initial_number_instances);
		tier.setMaximum_number_instances(maximum_number_instances);
		tier.setMinimum_number_instances(minimum_number_instances);
		tier.setImage(image);
		tier.setFlavour(flavour);
		tier.setName(name);
		

		for (int j=0; j<productReleases.size(); j++){							
			try {
				ProductRelease productRelease = productReleases.get(j);
			//	tier.removeProductRelease(productRelease);
				if (productRelease.getId()== null)
				{
				    if (productRelease.getName() != null)
				    	productRelease = productReleaseDao
						.load(productRelease.getName());
				    else
					  productRelease = productReleaseDao
						.load(productRelease.getProduct()+"-"+productRelease.getVersion());
				}
				tier.addProductRelease(productRelease);
			}
			
			catch (Exception e) {
				String errorMessage = "The ProductRelease Object " 
						+ productReleases.get(j).getProduct()+"-"+productReleases.get(j).getVersion() + " is " +
								"NOT present in Database";
				throw new InvalidEntityException (errorMessage);
			}
		} // for product release
			try {
				tier = tierDao.load(tier.getName());
			} catch (EntityNotFoundException e) {
				try {
					tier = tierDao.create(tier);
				}
				catch (Exception e2)
				{
					String errorMessage = "The Tier  " + tier.getName() + "  cannot be created " + e2.getMessage();
				   throw new InvalidEntityException (errorMessage);
				}
			}
			return tier;
	}
	
	public List<Tier> findByCriteria(TierSearchCriteria criteria) throws EntityNotFoundException
	{
		return tierDao.findByCriteria(criteria);
	}


	public List<Tier> findAll() {
		return tierDao.findAll();
	}
	
	

	public Tier load(String name) throws EntityNotFoundException {
		return tierDao.load(name);
	}


	public void delete(Tier tier) throws EntityNotFoundException {
		try
		{
		tier = load(tier.getName());
	
		}
		catch (EntityNotFoundException e)
		{
			//throw new EntityNotFoundException (Tier.class, e.getMessage(), e);
			return;
		}
		try
		{
		tierDao.remove(tier);
		}
		catch (Exception e)
		{
			//throw new EntityNotFoundException (Tier.class, e.getMessage(), e);
			return;
		}
		
		
		
		
	}


	public List<Tier> findByEnvironment(Environment environment) {
		// TODO Auto-generated method stub
		return null;
	}
    




}
