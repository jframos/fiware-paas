/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.TierInstanceDao;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.Service;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierInstanceSearchCriteria;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class TierInstanceDaoJpaImpl extends AbstractBaseDao<TierInstance, String> implements TierInstanceDao {

    @PersistenceContext(unitName = "paasmanager", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public List<TierInstance> findAll() {
        return super.findAll(TierInstance.class);
    }

    public TierInstance load(String name) throws EntityNotFoundException {
        return super.loadByField(TierInstance.class, "name", name);
    }

    // TODO
    public List<TierInstance> findByCriteria(TierInstanceSearchCriteria criteria) throws EntityNotFoundException {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(TierInstance.class);

        if (!StringUtils.isEmpty(criteria.getVdc())) {
            baseCriteria.add(Restrictions.eq(Tier.VDC_FIELD, criteria.getVdc()));
        }

        List<TierInstance> tierInstances = setOptionalPagination(criteria, baseCriteria).list();

        if (criteria.getProductInstance() != null) {
            tierInstances = filterByProductInstance(tierInstances, criteria.getProductInstance());
        }

        if (criteria.getService() != null) {
            tierInstances = filterByService(tierInstances, criteria.getService());
        }
        if (criteria.getEnvironmentInstance() != null && criteria.getVdc() != null) {
            tierInstances = filterByEnvironmentInstanceVDC(tierInstances, criteria.getEnvironmentInstance(),
                    criteria.getVdc());
        }

        return tierInstances;
    }

    private List<TierInstance> filterByEnvironmentInstanceVDC(List<TierInstance> tierInstances,
            EnvironmentInstance environmentInstance, String vdc) throws EntityNotFoundException {
        List<TierInstance> result = new ArrayList<TierInstance>();
        // Tenemos que tener en enviromentInstance

        // ahora comprobamos que ese enviromentInstance corresponda
        // al vdc que queremos
        if (environmentInstance.getVdc().equals(vdc)) {
            result = environmentInstance.getTierInstances();
        }

        return result;
    }

    /**
     * Filter the result by environment instance
     * 
     * @param tierInstances
     * @param productInstanceInput
     * @return tierInstances
     */
    private List<TierInstance> filterByProductInstance(List<TierInstance> tierInstances,
            ProductInstance productInstanceInput) {

        List<TierInstance> result = new ArrayList<TierInstance>();

        for (TierInstance tierInstance : tierInstances) {
            List<ProductInstance> productInstances = tierInstance.getProductInstances();

            for (ProductInstance pInstance : productInstances) {
                if (pInstance.getName().equals(productInstanceInput.getName()))
                    result.add(tierInstance);
            }
        }
        return result;
    }

    /**
     * Filter the result by service
     * 
     * @param tierInstances
     * @param productInstanceInput
     * @return tierInstances
     */
    private List<TierInstance> filterByService(List<TierInstance> tierInstances, Service serviceInput) {
        List<TierInstance> result = new ArrayList<TierInstance>();
        /*
         * for (TierInstance tierInstance : tierInstances) { List<Service> services = tierInstance.getServices(); for
         * (Service serv : services) { if (serv.getName().equals(serviceInput.getName())) result.add(tierInstance); } }
         */
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.dao.TierInstanceDao#findByTierInstanceId (java.lang.Long)
     */

    public TierInstance findByTierInstanceId(Long tierInstanceId) throws EntityNotFoundException {
        Query query = entityManager.createQuery("select p from TierInstance p join "
                + "fetch p.productInstances where p.id = :id");
        query.setParameter("id", tierInstanceId);
        TierInstance tierInstance = null;
        try {
            tierInstance = (TierInstance) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No TierInstance found in the database with name: " + Long.toString(tierInstanceId);
            throw new EntityNotFoundException(null, e.getMessage(), message);
        }
        return tierInstance;
    }

    public TierInstance findByTierInstanceName(String tierInstanceName) throws EntityNotFoundException {
        Query query = entityManager.createQuery("select p from TierInstance p join "
                + "fetch p.productInstances where p.name = :name");
        query.setParameter("name", tierInstanceName);
        TierInstance tierInstance = null;
        try {
            tierInstance = (TierInstance) query.getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException(TierInstance.class, "name", tierInstanceName);

        }
        return tierInstance;
    }

}
