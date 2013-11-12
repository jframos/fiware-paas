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

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.TierDao;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierSearchCriteria;

public class TierDaoJpaImpl extends AbstractBaseDao<Tier, String> implements TierDao {

    @PersistenceContext(unitName = "paasmanager", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public List<Tier> findAll() {
        return super.findAll(Tier.class);
    }

    /*
     * public Tier load(String name) throws EntityNotFoundException { TierSearchCriteria criteria = new
     * TierSearchCriteria(); criteria.setName(name); List<Tier> tiers = findByCriteria(criteria); if (tiers.size() != 1)
     * { throw new EntityNotFoundException(Tier.class, "name", name); } Tier tier = tiers.get(0); return tier; }
     */

    public Tier load(String name) throws EntityNotFoundException {
        try {
            return findByName(name);
        } catch (Exception e) {
            return super.loadByField(Tier.class, "name", name);
        }
    }

    public Tier load(String name, String vdc, String environmentName) throws EntityNotFoundException {
        try {
            return this.findByNameAndVdcAndEnvironment(name, vdc, environmentName);
        } catch (Exception e) {
            return findByNameAndVdcAndEnvironmentNoProduct(name, vdc, environmentName);
        }

    }

    /*
     * public Tier load(String name, String vdc, String environmentName) throws EntityNotFoundException {
     * TierSearchCriteria criteria = new TierSearchCriteria(); criteria.setVdc(vdc); criteria.setName(name);
     * criteria.setEnvironmentName(environmentName); List<Tier> tiers = findByCriteria(criteria); if (tiers.size() != 1)
     * { throw new EntityNotFoundException(Tier.class, "name", name); } Tier tier = tiers.get(0); return tier; }
     */

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.dao.TierDao#findByTierId(java.lang .String)
     */
    /*
     * public Tier findByTierId(Long tierId) throws EntityNotFoundException { Query query =
     * entityManager.createQuery("select p from Tier p join " + "fetch p.productReleases where p.id = :id");
     * query.setParameter("id", tierId); Tier tier = null; try { tier = (Tier) query.getSingleResult(); } catch
     * (NoResultException e) { String message = " No Tier found in the database with name: " + Long.toString(tierId);
     * throw new EntityNotFoundException(Tier.class, e.getMessage(), message); } return tier; }
     */

    private Tier findByName(String name) throws EntityNotFoundException {
        Query query = entityManager.createQuery("select p from Tier p join "
                + "fetch p.productReleases where p.name = :name");
        query.setParameter("name", name);
        Tier tier = null;
        try {
            tier = (Tier) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No Tier found in the database with name: " + name;
            throw new EntityNotFoundException(Tier.class, e.getMessage(), message);
        }
        return tier;
    }

    private Tier findByNameAndVdc(String name, String vdc) throws EntityNotFoundException {
        Query query = entityManager.createQuery("select p from Tier p join "
                + "fetch p.productReleases where p.name = :name and p.vdc =:vdc");
        query.setParameter("name", name);
        query.setParameter("vdc", vdc);
        Tier tier = null;
        try {
            tier = (Tier) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No Tier found in the database with name: " + name + " vdc " + vdc;
            throw new EntityNotFoundException(Tier.class, e.getMessage(), message);
        }

        return tier;
    }

    private Tier findByNameAndVdcNoProducts(String name, String vdc) throws EntityNotFoundException {
        Query query = entityManager.createQuery("select p from Tier p where p.name = :name and p.vdc =:vdc");
        query.setParameter("name", name);
        query.setParameter("vdc", vdc);
        Tier tier = null;
        try {
            tier = (Tier) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No Tier found in the database with name: " + name + " vdc " + vdc + " no products";
            throw new EntityNotFoundException(Tier.class, e.getMessage(), message);
        }
        return tier;

    }

    private Tier findByNameAndVdcAndEnvironment(String name, String vdc, String environmentname)
            throws EntityNotFoundException {
        Query query = entityManager.createQuery("select p from Tier p join "
                + "fetch p.productReleases where (p.name = :name and p.vdc =:vdc) "
                + "and p.environmentname=:environmentname");
        query.setParameter("name", name);
        query.setParameter("vdc", vdc);
        query.setParameter("environmentname", environmentname);
        Tier tier = null;
        try {
            tier = (Tier) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No Tier found in the database with name: " + name + " vdc " + vdc
                    + " and environmentname " + environmentname;
            throw new EntityNotFoundException(Tier.class, e.getMessage(), message);
        }

        return tier;
    }

    private Tier findByNameAndVdcAndEnvironmentNoProduct(String name, String vdc, String environmentname)
            throws EntityNotFoundException {
        Query query = entityManager
                .createQuery("select p from Tier p where (p.name = :name and p.vdc =:vdc) and p.environmentname= :environmentname");
        query.setParameter("name", name);
        query.setParameter("vdc", vdc);
        query.setParameter("environmentname", environmentname);
        Tier tier = null;
        try {
            tier = (Tier) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No Tier found in the database with name: " + name + " vdc " + vdc
                    + " no products and environmentname " + environmentname;
            throw new EntityNotFoundException(Tier.class, e.getMessage(), message);
        }
        return tier;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Tier> findByCriteria(TierSearchCriteria criteria) throws EntityNotFoundException {
        // Session session = (Session) getEntityManager().getDelegate();
        Session session = (Session) entityManager.getDelegate();
        Criteria baseCriteria = session.createCriteria(Tier.class);

        if (!StringUtils.isEmpty(criteria.getVdc())) {
            baseCriteria.add(Restrictions.eq(Tier.VDC_FIELD, criteria.getVdc()));
        }
        if (!StringUtils.isEmpty(criteria.getName())) {
            baseCriteria.add(Restrictions.eq(Tier.NAME_FIELD, criteria.getName()));
        }
        if (!StringUtils.isEmpty(criteria.getEnvironmentName())) {
            baseCriteria.add(Restrictions.eq(Tier.ENVIRONMENT_NAME_FIELD, criteria.getEnvironmentName()));
        }

        List<Tier> tiers = setOptionalPagination(criteria, baseCriteria).list();

        if (criteria.getEnvironment() != null) {
            tiers = filterByEnvironment(tiers, criteria.getEnvironment());
        }

        return tiers;
    }

    private List<Tier> filterByEnvironment(List<Tier> tiers, Environment environment) throws EntityNotFoundException {
        List<Tier> filterTiers = new ArrayList();
        for (Tier tier : tiers) {
            if (environment.getTiers().contains(tier))
                filterTiers.add(tier);
        }
        return filterTiers;
    }

    @Override
    public Tier loadTierWithProductReleaseAndMetadata(String tierName, String vdc, String environmentName)
            throws EntityNotFoundException {
        Tier tier = findByNameAndVdcAndEnvironment(tierName, vdc, environmentName);

        for (ProductRelease productRelease : tier.getProductReleases()) {

            productRelease.getMetadatas();
        }

        return tier;
    }
}
