package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentDao;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentSearchCriteria;

public class EnvironmentDaoJpaImpl 
	extends AbstractBaseDao<Environment, String> implements EnvironmentDao {

	public List<Environment> findAll() {
		return super.findAll(Environment.class);
	}

	@Override
	public Environment load(String arg0) throws EntityNotFoundException {
        return super.loadByField(Environment.class, "name", arg0);
	}

	@Override
	public List<Environment> findByCriteria(EnvironmentSearchCriteria criteria) {
		Session session = (Session) getEntityManager().getDelegate();
	    Criteria baseCriteria = session.createCriteria(Environment.class);
	    
	   /*if (criteria.getTier() != null) {
	    	baseCriteria.add(Restrictions.eq("tier", criteria.getTier()));
	    }*/
	    
	    List<Environment> environments = setOptionalPagination(criteria, 
	        		baseCriteria).list();
	    
	    if (criteria.getTier() != null) {
	    	environments = filterByTier(environments, criteria.getTier());
	    }
	        
	    return environments;
	}
	
	 /**
     * Filter the result by tier
     *
     * @param environments
     * @param tier
     * @return environments
     */
    private List<Environment> filterByTier(
            List<Environment> environments, Tier tierInput) {
        List<Environment> result = new ArrayList<Environment>();
        for (Environment environment : environments) {
            for (Tier tier : environment.getTiers()) {
            	if (tier.getName().equals(tierInput.getName())) {
                    result.add(environment);
                }
            }
        	
        }
        return result;
    }

}
