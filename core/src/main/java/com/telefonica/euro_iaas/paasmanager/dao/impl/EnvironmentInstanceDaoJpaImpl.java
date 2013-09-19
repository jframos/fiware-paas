package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentInstanceSearchCriteria;

public class EnvironmentInstanceDaoJpaImpl 
	extends AbstractBaseDao<EnvironmentInstance, String> implements EnvironmentInstanceDao {

	@PersistenceContext(unitName = "paasmanager", type=PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public List<EnvironmentInstance> findAll() {
		return super.findAll(EnvironmentInstance.class);
	}

	public EnvironmentInstance loadForDelete(String  name) throws EntityNotFoundException {
        return findByEnvironmentInstanceName(name);
		//return super.loadByField(EnvironmentInstance.class, "name", name);
	}
	
	public EnvironmentInstance load(String  name) throws EntityNotFoundException {
      
		return super.loadByField(EnvironmentInstance.class, "name", name);
	}

	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao#findByCriteria(com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentInstanceSearchCriteria)
	 */
	public List<EnvironmentInstance> findByCriteria(
			EnvironmentInstanceSearchCriteria criteria) {
		
		Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session
                .createCriteria(EnvironmentInstance.class);
        
        if (criteria.getStatus() != null && !criteria.getStatus().isEmpty()) {
            Criterion statusCr = null;
            for (Status status : criteria.getStatus()) {
                statusCr = addStatus(statusCr, status);
            }
            baseCriteria.add(statusCr);
        }

       if (!StringUtils.isEmpty(criteria.getVdc())) {
            baseCriteria.add(Restrictions.eq(EnvironmentInstance.VDC_FIELD,
                    criteria.getVdc()));
        }
                
       List<EnvironmentInstance> environmentInstances = setOptionalPagination(
                criteria, baseCriteria).list();
        
     // TODO sarroyo: try to do this filter using hibernate criteria.
        /*if (criteria.getApplicatonRelease() != null) {
        	applicationInstances = filterByApplicationRelease(
        			applicationInstances, 
        			criteria.getApplicatonRelease());
        }*/

        return environmentInstances;
	}
	
    private Criterion addStatus(Criterion statusCr, Status status) {
        SimpleExpression expression = Restrictions.eq("status", status);
        if (statusCr == null) {
            statusCr = expression;
        } else {
            statusCr = Restrictions.or(statusCr, expression);
        }
        return statusCr;
    }

	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.dao.TierDao#findByTierId(java.lang.String)
	 */
	private EnvironmentInstance findByEnvironmentInstanceName(String envInstanceName)  
		throws EntityNotFoundException {
		
		Query query = entityManager.createQuery("select p from EnvironmentInstance" +
				" p join fetch p.tierInstances where p.name = :name" );
		query.setParameter("name", envInstanceName);
		EnvironmentInstance environmentInstance = null;
		try {
			environmentInstance = (EnvironmentInstance) query.getSingleResult();
		 } catch (NoResultException  e) {
			 String message = " No EnvironmentInstance found in the database " +
			 		"with name: " +	 envInstanceName;			 
			 System.out.println(message);
			 throw new EntityNotFoundException (EnvironmentInstance.class, "name", envInstanceName);
		 }
		 return environmentInstance;
	}

}
