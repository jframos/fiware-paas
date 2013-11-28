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

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentDao;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentSearchCriteria;

public class EnvironmentDaoJpaImpl extends AbstractBaseDao<Environment, String> implements EnvironmentDao {

    private static final String QUERY_LOAD_BY_TWO_FIELDS = "SELECT o FROM {0} o WHERE o.{1} = :{1} and o.{2} =:{2}";

    @PersistenceContext(unitName = "paasmanager", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public Environment create(Environment environment) throws InvalidEntityException, AlreadyExistsEntityException {
        return super.create(environment);
    }

    public Environment update(Environment environment) throws InvalidEntityException {
        return super.update(environment);
    }

    public List<Environment> findAll() {
        return super.findAll(Environment.class);
    }

    public Environment load(String envName) throws EntityNotFoundException {
        try {
            // return filterEqualTiers(findByEnvironmentName(envName));
            return findByEnvironmentName(envName);
        } catch (Exception e) {
            // return filterEqualTiers(findByEnvironmentNameNoTiers (envName));
            return findByEnvironmentNameNoTiers(envName);
        }
    }


    public Environment load(String envName, String vdc) throws EntityNotFoundException {

        try {
            // return filterEqualTiers(findByEnvironmentNameVdc(envName, vdc));
            return findByEnvironmentNameVdc(envName, vdc);
        } catch (Exception e) {
            // return filterEqualTiers(findByEnvironmentNameVdc(envName, vdc));
            return findByEnvironmentNameVdcNoTiers(envName, vdc);
        }
    }

    public List<Environment> findByCriteria(EnvironmentSearchCriteria criteria) {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(Environment.class);

        /*
         * if (criteria.getTier() != null) { baseCriteria.add(Restrictions.eq("tier", criteria.getTier())); }
         */

        List<Environment> environments = setOptionalPagination(criteria, baseCriteria).list();

        if (criteria.getTier() != null) {
            environments = filterByTier(environments, criteria.getTier());
        }

        if (criteria.getOrg() != null && criteria.getVdc() != null && criteria.getEnvironmentName() != null) {
            environments = filterByEnvironment(environments, criteria.getOrg(), criteria.getVdc(),
                    criteria.getEnvironmentName());
            return environments;
        }

        if (criteria.getOrg() != null && criteria.getVdc() != null) {
            environments = filterByOrgAndVdc(environments, criteria.getOrg(), criteria.getVdc());
            return environments;
        }

        if (criteria.getOrg() != null) {
            environments = filterByOrg(environments, criteria.getOrg());
        }

        return environments;
    }


    private List<Environment> filterByOrgAndVdc(List<Environment> environments, String org, String vdc) {
        List<Environment> result = new ArrayList<Environment>();
        for (Environment environment : environments) {
            if (environment.getVdc() == null)
                continue;
            if (environment.getOrg().equals(org) && environment.getVdc().equals(vdc)) {
                result.add(environment);
            }
        }
        return result;
    }

    private List<Environment> filterByOrg(List<Environment> environments, String org) {
        List<Environment> result = new ArrayList<Environment>();
        for (Environment environment : environments) {
            if (environment.getOrg().equals(org) && environment.getVdc() == null) {
                result.add(environment);
            }
        }
        return result;

    }

    private List<Environment> filterByEnvironment(List<Environment> environments, String org, String vdc,
            String environmentName) {
        List<Environment> result = new ArrayList<Environment>();
        for (Environment environment : environments) {
            if (environment.getOrg().equals(org) && environment.getVdc() != null && environment.getVdc().equals(vdc)
                    && environment.getName() != null && environment.getName().equals(environmentName)) {
                result.add(environment);
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.dao.TierDao#findByTierId(java.lang .String)
     */
    private Environment findByEnvironmentName(String envName) throws EntityNotFoundException {
        Query query = entityManager.createQuery("select p from Environment p join "
                + "fetch p.tiers where p.name = :name");
        query.setParameter("name", envName);
        Environment environment = null;
        try {
            environment = (Environment) query.getSingleResult();
            entityManager.flush();
        } catch (NoResultException e) {
            throw new EntityNotFoundException(Environment.class, "name", envName);
        }
        return environment;
    }

    private Environment findByEnvironmentNameNoTiers(String envName) throws EntityNotFoundException {
        Query query = entityManager.createQuery("select p from Environment p where p.name = :name");
        query.setParameter("name", envName);
        Environment environment = null;
        try {
            environment = (Environment) query.getSingleResult();
            entityManager.flush();
        } catch (NoResultException e) {
            throw new EntityNotFoundException(Environment.class, "name", envName);
        }
        return environment;
    }

    private Environment findByEnvironmentNameVdc(String envName, String vdc) throws EntityNotFoundException {
        Query query = entityManager.createQuery("select p from Environment p join "
                + "fetch p.tiers where p.name = :name and p.vdc = :vdc");
        query.setParameter("name", envName);
        query.setParameter("vdc", vdc);
        Environment environment = null;
        try {
            environment = (Environment) query.getSingleResult();
            entityManager.flush();
        } catch (NoResultException e) {
            throw new EntityNotFoundException(Environment.class, "name", envName);
        }
        // return filterEqualTiers(environment);
        return environment;
    }

    private Environment findByEnvironmentNameVdcNoTiers(String envName, String vdc) throws EntityNotFoundException {
        Query query = entityManager.createQuery("select p from Environment p where p.name = :name and p.vdc = :vdc");
        query.setParameter("name", envName);
        query.setParameter("vdc", vdc);
        Environment environment = null;
        try {
            environment = (Environment) query.getSingleResult();
            entityManager.flush();
        } catch (NoResultException e) {
            throw new EntityNotFoundException(Environment.class, "name", envName);
        }
        // return filterEqualTiers(environment);
        return environment;
    }

    /**
     * Filter the result by tier
     * 
     * @param environments
     * @param tier
     * @return environments
     */
    private List<Environment> filterByTier(List<Environment> environments, Tier tierInput) {
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

  /*  private List<Environment> filterEqualTiers(List<Environment> environments) {
        List<Tier> tierResult = new ArrayList<Tier>();
        List<Environment> result = new ArrayList<Environment>();

        for (Environment environment : environments) {
            List<Tier> tiers = environment.getTiers();
            for (int i = 0; i < tiers.size(); i++) {
                Tier tier = tiers.get(i);
                List<Tier> tierAux = new ArrayList<Tier>();
                for (int j = i + 1; j < tiers.size(); j++) {
                    tierAux.add(tiers.get(j));
                }
                if (!tierAux.contains(tier))
                    tierResult.add(tier);
            }
            environment.setTiers(tierResult);
            result.add(environment);
        }
        return result;
    }*/

  /*  private Environment filterEqualTiers(Environment environment) {
        List<Tier> tierResult = new ArrayList<Tier>();
        Environment result = new Environment();
        List<String> tierString = new ArrayList<String>();
        List<Tier> tiers = environment.getTiers();

        for (int i = 0; i < tiers.size(); i++) {
            Tier tier = tiers.get(i);
            if (i == 0) {
                tierResult.add(tier);
                tierString.add(tier.getName());
            } else {
                if (!tierString.contains(tier.getName()))
                    tierResult.add(tier);
            }
        }
        result.setTiers(tierResult);

        return result;
    }*/
}
