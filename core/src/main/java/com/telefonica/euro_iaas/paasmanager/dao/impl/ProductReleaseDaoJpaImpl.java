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
import org.hibernate.criterion.Restrictions;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductReleaseSearchCriteria;

public class ProductReleaseDaoJpaImpl 
	extends AbstractBaseDao<ProductRelease, String> implements ProductReleaseDao {

	@PersistenceContext(unitName = "paasmanager", type=PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public List<ProductRelease> findAll() {
		return super.findAll(ProductRelease.class);
	}

	public ProductRelease load(String name) throws EntityNotFoundException {
        return super.loadByField(ProductRelease.class, "name", name);
	}
	


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

	public ProductRelease load(String product, String version)
			throws EntityNotFoundException {
		return super.loadByField(ProductRelease.class, "name", product + "-" + version);
		//return findByProductReleaseName(product + "-" + version);
		/*Session session = (Session) getEntityManager().getDelegate();
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
        return release;*/
	}

	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.dao.TierDao#findByTierId(java.lang.String)
	 */
	private ProductRelease findByProductReleaseName(String id) throws EntityNotFoundException {
		Query query = entityManager.createQuery("select p from ProductRelease p join " 
				+ "fetch p.supportedOOSS where p.id = :id" );
		query.setParameter("id", id);
		ProductRelease productRelease = null;
		try {
			productRelease = (ProductRelease) query.getSingleResult();
		 } catch (NoResultException  e) {
			 String message = " No ProductRelease found in the database with id: " +
					 id + " Exception: " + e.getMessage();
			 System.out.println (message);
			 throw new EntityNotFoundException (ProductRelease.class, "id", id);
		 } 
		 return productRelease;
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
