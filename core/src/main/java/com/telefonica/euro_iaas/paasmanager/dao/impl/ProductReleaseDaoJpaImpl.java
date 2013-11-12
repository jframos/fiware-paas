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
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductReleaseSearchCriteria;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class ProductReleaseDaoJpaImpl extends AbstractBaseDao<ProductRelease, String> implements ProductReleaseDao {

    @PersistenceContext(unitName = "paasmanager", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public List<ProductRelease> findAll() {
        return super.findAll(ProductRelease.class);
    }

    public ProductRelease load(String name) throws EntityNotFoundException {
        try {
            return findByProductReleaseName(name);
        } catch (Exception e) {
            return super.loadByField(ProductRelease.class, "name", name);
        }
        // return super.loadByField(ProductRelease.class, "name", name);
    }

    /*
     * public ProductRelease load(String name) throws EntityNotFoundException { ProductReleaseSearchCriteria criteria =
     * new ProductReleaseSearchCriteria(); criteria.setProductName(name); List<ProductRelease> productReleases =
     * findByCriteria(criteria); if (productReleases.size() != 1) { throw new
     * EntityNotFoundException(ProductRelease.class, "name", name); } ProductRelease pRelease = productReleases.get(0);
     * return pRelease; //return super.loadByField(ProductRelease.class, "name", name); }
     */

    public List<ProductRelease> findByCriteria(ProductReleaseSearchCriteria criteria) {
        // Session session = (Session) getEntityManager().getDelegate();
        Session session = (Session) entityManager.getDelegate();
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
        // return super.loadByField(ProductRelease.class, "name", product + "-"
        // + version);

        try {
            return findByProductReleaseName(product + "-" + version);
        } catch (Exception e) {
            return load(product + "-" + version);
        }
        // return findByProductReleaseName(product + "-" + version)
    }

    /*
     * (non-Javadoc)
     * @see com.telefonica.euro_iaas.paasmanager.dao.TierDao#findByTierId(java.lang .String)
     */
    private ProductRelease findByProductReleaseName(String name) throws EntityNotFoundException {
        Query query = entityManager.createQuery("select p from ProductRelease p join "
                + "left fetch p.attributes where p.name = :name");
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
