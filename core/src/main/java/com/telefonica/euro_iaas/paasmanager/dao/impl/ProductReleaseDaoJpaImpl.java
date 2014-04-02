/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductReleaseSearchCriteria;

@Transactional(propagation = Propagation.REQUIRED)
public class ProductReleaseDaoJpaImpl extends AbstractBaseDao<ProductRelease, String> implements ProductReleaseDao {

    public List<ProductRelease> findAll() {
        return super.findAll(ProductRelease.class);
    }

    public ProductRelease load(String name) throws EntityNotFoundException {
        try {
            return findByProductReleaseWithMetadataAndAtt(name);
        } catch (Exception e) {
            try {
                return findByProductReleaseWithAtt(name);
            } catch (Exception e2) {

                try {
                    return loadProductReleaseWithMetadata(name);
                } catch (Exception e3) {

                    return super.loadByField(ProductRelease.class, "name", name);
                }
            }

        }

    }

    public ProductRelease load(String product, String version, String tierName) throws EntityNotFoundException {
        try {
            return this.findByNameAndVdcAndTierWithMetadataAndAtt(product + "-" + version, tierName);
        } catch (Exception e) {
           // return this.findByNameAndVdcAndTierWithAtt(product + "-" +  version, tierName);
            try {
                return findByProductReleaseTierWithAtt(product + "-" + version, tierName);
            } catch (Exception e2) {

                try {
                    return loadProductReleaseTierWithMetadata(product + "-" + version, tierName);
                } catch (Exception e3) {
                    return super.loadByField(ProductRelease.class, "name", product + "-" + version);
                }
            }

        }

    }
    public List<ProductRelease> findByCriteria(ProductReleaseSearchCriteria criteria) {
        // Session session = (Session) getEntityManager().getDelegate();
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(ProductRelease.class);

        if (criteria.getProductName() != null) {
            baseCriteria.add(Restrictions.eq("name", criteria.getProductName()));
        }

        List<ProductRelease> productReleases = setOptionalPagination(criteria, baseCriteria).list();

        if (criteria.getOSType() != null) {
            productReleases = filterByOSType(productReleases, criteria.getOSType());
        }

        return productReleases;
    }

    /*
     * public ProductRelease load(String product, String version) throws EntityNotFoundException { return load(product +
     * "-"+ version); }
     */

    /*
     * public ProductRelease load(String product, String version) throws EntityNotFoundException { return
     * super.loadByField(ProductRelease.class, "name", product + "-"+ version); }
     */

    public ProductRelease load(String product, String version) throws EntityNotFoundException {
        return load(product + "-" + version);

    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.dao.TierDao#findByTierId(java.lang .String)
     */

    private ProductRelease findByProductReleaseWithAtt(String name) throws EntityNotFoundException {
        Query query = getEntityManager().createQuery(
                "select p from ProductRelease p left join " + " fetch p.attributes where p.name = :name");
        query.setParameter("name", name);
        ProductRelease productRelease = null;
        try {
            productRelease = (ProductRelease) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No ProductRelease found in the database with id: " + name + " Exception: "
                    + e.getMessage();
            throw new EntityNotFoundException(ProductRelease.class, "name", name);
        }
        return productRelease;
    }

    public ProductRelease loadProductReleaseWithMetadata(String name) throws EntityNotFoundException {

        Query query = getEntityManager().createQuery(
                "select p from ProductRelease p left join " + " fetch p.metadatas where p.name = :name");
        query.setParameter("name", name);
        ProductRelease productRelease = null;
        try {
            productRelease = (ProductRelease) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No ProductRelease found in the database with id: " + name + " Exception: "
                    + e.getMessage();
            throw new EntityNotFoundException(ProductRelease.class, "name", name);
        }
        return productRelease;
    }

    public ProductRelease loadProductReleaseTierWithMetadata(String name, String tierName) throws EntityNotFoundException {

        Query query = getEntityManager().createQuery(
                "select p from ProductRelease p left join " + " fetch p.metadatas where p.name = :name "
                                + "and p.tierName=:tierName");
        query.setParameter("name", name);
        query.setParameter("tierName", tierName);
        ProductRelease productRelease = null;
        try {
            productRelease = (ProductRelease) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No ProductRelease found in the database with id: " + name + " Exception: "
                    + e.getMessage();
            throw new EntityNotFoundException(ProductRelease.class, "name", name);
        }
        return productRelease;
    }
    private ProductRelease findByProductReleaseWithMetadataAndAtt(String name) throws EntityNotFoundException {

        Query query = getEntityManager()
                .createQuery(
                        "select p from ProductRelease p left join"
                                + " fetch p.attributes as attributes left join fetch p.metadatas as metadatas " +
                                "where p.name = :name");
        query.setParameter("name", name);
        ProductRelease productRelease = null;
        try {
            productRelease = (ProductRelease) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No ProductRelease found in the database with id: " + name + " Exception: "
                    + e.getMessage();
            throw new EntityNotFoundException(ProductRelease.class, "name", name);
        }
        return productRelease;
    }

    private ProductRelease findByProductReleaseTierWithAtt(String name, String tierName) throws EntityNotFoundException {
        Query query = getEntityManager().createQuery(
                "select p from ProductRelease p left join " + " fetch p.attributes where p.name = :name "
                                + "and p.tierName=:tierName");
        query.setParameter("name", name);
        query.setParameter("tierName", tierName);
        ProductRelease productRelease = null;
        try {
            productRelease = (ProductRelease) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No ProductRelease found in the database with id: " + name + " Exception: "
                    + e.getMessage();
            throw new EntityNotFoundException(ProductRelease.class, "name", name);
        }
        return productRelease;
    }
    
    private ProductRelease findByNameAndVdcAndTierWithAtt(String name, String tierName) 
                    throws EntityNotFoundException {
        Query query = getEntityManager().createQuery( "select p from ProductRelease p left join " + 
                        "fetch  p.attributes as attributes where p.name = :name "
                        + "and p.tierName=:tierName");
        query.setParameter("name", name);
        query.setParameter("tierName", tierName);
        ProductRelease productRelease = null;
        try {
            productRelease = (ProductRelease) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No ProductRelease found in the database with name: " + name +
                            " and tierName " + tierName;
            throw new EntityNotFoundException(Tier.class, e.getMessage(), message);
        }
        return productRelease;
    }
    
    private ProductRelease findByNameAndVdcAndTierWithMetadataAndAtt(String name, String tierName) 
                    throws EntityNotFoundException {
        Query query = getEntityManager().createQuery( "select p from ProductRelease p left join " + 
                        "fetch p.attributes as attributes left join fetch p.metadatas as metadatas where p.name = :name "
                        + "and p.tierName=:tierName");
        query.setParameter("name", name);
        query.setParameter("tierName", tierName);
        ProductRelease productRelease = null;
        try {
            productRelease = (ProductRelease) query.getSingleResult();
        } catch (NoResultException e) {
            String message = " No ProductRelease found in the database with name: " + name 
                            + " and tierName " + tierName;
            throw new EntityNotFoundException(Tier.class, e.getMessage(), message);
        }
        return productRelease;
    }
    /**
     * Filter the result by product release.
     */
    private List<ProductRelease> filterByOSType(List<ProductRelease> productReleases, String osType) {
        List<ProductRelease> result = new ArrayList<ProductRelease>();
        for (ProductRelease productRelease : productReleases) {
            for (OS os : productRelease.getSupportedOOSS()) {
                if (os.getOsType().equals(osType)) {
                    result.add(productRelease);
                }
            }

        }
        return result;
    }
}
