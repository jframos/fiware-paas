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
import com.telefonica.euro_iaas.paasmanager.dao.TierDao;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierSearchCriteria;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;

public class TierDaoJpaImpl extends AbstractBaseDao<Tier, String> implements TierDao {

	@PersistenceContext(unitName = "paasmanager", type=PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	
	public List<Tier> findAll() {
		return super.findAll(Tier.class);
	}

	public Tier load(String name) throws EntityNotFoundException {
        return super.loadByField(Tier.class, "name", name);
	}

	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.dao.TierDao#findByTierId(java.lang.String)
	 */
	public Tier findByTierId(Long tierId) throws EntityNotFoundException {
		Query query = entityManager.createQuery("select p from Tier p join " 
				+ "fetch p.productReleases where p.id = :id" );
		query.setParameter("id", tierId);
		Tier tier = null;
		try {
			tier = (Tier) query.getSingleResult();
		 } catch (NoResultException  e) {
			 String message = " No Tier found in the database with name: " +
					 Long.toString(tierId);
			 throw new EntityNotFoundException (null, e.getMessage(), message);
		 }
		 return tier;
	}
	
	public List<Tier> findByCriteria(TierSearchCriteria criteria) throws EntityNotFoundException {
		Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session
                .createCriteria(Tier.class);
       
        List<Tier> tierInstances = setOptionalPagination(
                criteria, baseCriteria).list();
        
        

        if (criteria.getEnvironment() != null ){
        	tierInstances = filterByEnvironment(tierInstances, criteria.getEnvironment());
        }

        return tierInstances;
	}
	
	private List<Tier> filterByEnvironment(List<Tier> tiers, Environment environment) throws EntityNotFoundException {
		List<Tier> filterTiers = new ArrayList ();
		for (Tier tier: tiers)
		{
			if (environment.getTiers().contains(tier))
				filterTiers.add(tier);
		}
		return filterTiers;
	}

}
