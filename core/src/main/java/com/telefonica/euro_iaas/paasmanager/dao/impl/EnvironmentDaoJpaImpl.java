package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

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

	@PersistenceContext(unitName = "paasmanager", type=PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	
	public List<Environment> findAll() {
		return super.findAll(Environment.class);
	}

	public Environment load(String envName) throws EntityNotFoundException {        
		return super.loadByField(Environment.class, "name", envName);
		//return findByEnvironmentName(envName);
	}

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
	
	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.dao.TierDao#findByTierId(java.lang.String)
	 */
	private Environment findByEnvironmentName(String envName) throws EntityNotFoundException {
		Query query = entityManager.createQuery("select p from Environment p join " 
				+ "fetch p.tiers where p.name = :name" );
		query.setParameter("name", envName);
		Environment environment = null;
		try {
			environment = (Environment) query.getSingleResult();
		 } catch (NoResultException  e) {
			 String message = " No Environment found in the database with name: " +
					 envName;
			 throw new EntityNotFoundException (Environment.class, "name", envName);
		 }
		 return environment;
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
