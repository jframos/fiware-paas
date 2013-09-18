package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.OS;

public class ProductReleaseDaoJpaImpl 
	extends AbstractBaseDao<ProductRelease, String> implements ProductReleaseDao {

	@Override
	public List<ProductRelease> findAll() {
		return super.findAll(ProductRelease.class);
	}

	@Override
	public ProductRelease load(String arg0) throws EntityNotFoundException {
        return super.loadByField(ProductRelease.class, "id", arg0);
	}

	@Override
	public List<ProductRelease> findByCriteria(
			ProductReleaseSearchCriteria criteria) {
		Session session = (Session) getEntityManager().getDelegate();
	    Criteria baseCriteria = session.createCriteria(ProductRelease.class);
	    
	    if (criteria.getProductName() != null) {
	    	baseCriteria.add(Restrictions.eq("name", criteria.getProductName()));
	    }
	    
	    List<ProductRelease> productReleases = setOptionalPagination(criteria, 
	        		baseCriteria).list();
	        
        if (criteria.getOSType() != null) {
        	productReleases = filterByOSType(productReleases, criteria.getOSType());
        }
       
	    return productReleases;
	}

	@Override
	public ProductRelease load(String productName, String version)
			throws EntityNotFoundException {
        Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session.createCriteria(
                ProductRelease.class);
        baseCriteria.add(Restrictions.eq("name", productName));
        baseCriteria.add(Restrictions.eq("version", version));

        ProductRelease release = (ProductRelease) baseCriteria.uniqueResult();
        if (release == null) {
            String[] keys = {"productName", "version"};
            Object[] values = {productName, version};
            throw new EntityNotFoundException(ProductRelease.class,
                    keys, values);
        }
        return release;
	}

	 /**
     * Filter the result by product release
     *
     * @param applications
     * @param product Release
     * @return
     */
    private List<ProductRelease> filterByOSType(
            List<ProductRelease> productReleases, String osType) {
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
