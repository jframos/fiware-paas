package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentDao;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentTypeDao;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;

import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentSearchCriteria;

public class EnvironmentManagerImpl implements EnvironmentManager {

	//private ProductReleaseDao productReleaseDao;
	private EnvironmentDao environmentDao;
	private EnvironmentTypeDao environmentTypeDao;
	private TierManager tierManager;
//	private TierDao tierDao;
//	private ProductReleaseManager  productReleaseManager;
	
	public Environment create(Environment environment) throws 
		InvalidEnvironmentRequestException {
		
		Environment environmentDB = new Environment();
		try {
			environmentDB = environmentDao.load(environment.getName());
			String errorMessage = "The Environment Object " 
				+ environment.getName()  + " already exist in Database";
		   throw new InvalidEnvironmentRequestException (errorMessage);
			
		} catch (EntityNotFoundException e) {}
		
		if (environment.getTiers() == null || environment.getTiers().size() == 0)
		{
			throw new InvalidEnvironmentRequestException ("There is not any tier associated to the Enviornment. It is no possible to create the enviroment" +
					environment.getName()  );
		}
		
		if (environment.getName() == null)
		{
			throw new InvalidEnvironmentRequestException ("There is not any name associated to the Enviornment. It is no possible to create the enviroment" +
					environment.getName()  );
		}
		
		if (environment.getEnvironmentType() == null)
		{
			//throw new InvalidEnvironmentRequestException ("There is not any environment type associated to the Enviornment. It is no possible to create the enviroment" +
				//	environment.getName()  );
		}
	
		for (Tier tier: environment.getTiers()){
			Tier tierDB = null;
			try{
				
				tierDB = tierManager.create(tier.getName(), tier.getImage(),tier.getFlavour(),
					tier.getProductReleases(), 
					tier.getInitial_number_instances(),
					tier.getMaximum_number_instances(), 
					tier.getMinimum_number_instances());
				
				environmentDB.addTier(tierDB);
				
			}catch (InvalidEntityException e){
				throw new InvalidEnvironmentRequestException (e.getMessage());
			}
		} 

		environmentDB.setName(environment.getName());
		
		if ( environment.getOvf()!= null )
			environmentDB.setOvf(environment.getOvf());
		
		EnvironmentType environmentType = new EnvironmentType();
			
	
	/*	try {
			if ()
			environmentType = environmentTypeDao.load(environment
					.getEnvironmentType().getName());
		} catch (EntityNotFoundException e) {
			try {
				environmentType = environmentTypeDao.create(environment
						.getEnvironmentType());
			} catch (AlreadyExistsEntityException e1) {
				String errorMessage = "The EnvironmentType Object " 
						+ environmentType.getName() + "is already in Database";
				throw new InvalidEnvironmentRequestException (errorMessage);
			} catch (InvalidEntityException e2) {
				String errorMessage = " The EnvironmentType Object " 
					+ environmentType.getName() + "is invaled" + e2.getMessage();
			throw new InvalidEnvironmentRequestException (errorMessage);
			} 
		}*/
		//environmentDB.setEnvironmentType(environmentType);

		try {
				environmentDB = environmentDao.create(environmentDB);
			} catch (InvalidEntityException iee) {
				String errorMessage = "The Environment Object " 
						+ environment.getName() + " is " +
								"NOT valid: "+ iee.getMessage();
				throw new InvalidEnvironmentRequestException (errorMessage);
			} catch (AlreadyExistsEntityException aee) {
				String errorMessage = "The Environment Object " 
						+ environment.getName()  + " already exist in Database";
				throw new InvalidEnvironmentRequestException (errorMessage);
			}
			catch (Exception aee) {
				String errorMessage = "The Environment Object " 
					+ environment.getName() + " is " +
							"NOT valid" ;
				throw new InvalidEnvironmentRequestException (errorMessage);
			}

		
		return environmentDB;
	}

	public void destroy(Environment environment) throws InvalidEntityException {
		
		List<Tier> tiers = environment.getTiers();

		
		if (tiers!=null && tiers.size()>0){
			environment.setTiers(null);
			
			try {
				environment = environmentDao.update(environment);
			} catch (InvalidEntityException e) {
				// TODO Auto-generated catch block
				throw new InvalidEntityException (e.getMessage());
			}
		for (Tier tier: tiers)
		{
			try {
				tierManager.delete(tier);
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				throw new InvalidEntityException (e.getMessage());
			}
			
		}
	  }
		

		environmentDao.remove(environment);
	}

	public Environment load(String name) throws EntityNotFoundException {
		return environmentDao.load(name);
	}

	public List<Environment> findAll() {
		return environmentDao.findAll();
	}

	public List<Environment> findByCriteria(EnvironmentSearchCriteria criteria) {
		return null;
	}

    
    /**
     * @param environmentDao
     *            the environmentDao to set
     */
    public void setEnvironmentDao(EnvironmentDao environmentDao) {
        this.environmentDao = environmentDao;
    }

    /**
     * @param environmentTypeDao
     *            the environmentTypeDao to set
     */
    public void setEnvironmentTypeDao(EnvironmentTypeDao environmentTypeDao) {
        this.environmentTypeDao = environmentTypeDao;
    }
     
    public void setTierManager(TierManager tierManager) {
        this.tierManager = tierManager;
    }

	public Environment update(Environment environment) throws EntityNotFoundException, InvalidEntityException {
		return environmentDao.update(environment);
	}

}
