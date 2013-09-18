package com.telefonica.euro_iaas.paasmanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.telefonica.euro_iaas.commons.dao.AbstractBaseDao;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.dao.TierInstanceDao;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.Service;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierInstanceSearchCriteria;

public class TierInstanceDaoJpaImpl extends AbstractBaseDao<TierInstance, Long> implements TierInstanceDao {

	@Override
	public List<TierInstance> findAll() {
		return super.findAll(TierInstance.class);
	}

	@Override
	public TierInstance load(Long arg0) throws EntityNotFoundException {
        return super.loadByField(TierInstance.class, "id", arg0);
	}

	//TODO
	@Override
	public List<TierInstance> findByCriteria(TierInstanceSearchCriteria criteria) {
		Session session = (Session) getEntityManager().getDelegate();
        Criteria baseCriteria = session
                .createCriteria(ProductInstance.class);
       
        List<TierInstance> tierInstances = setOptionalPagination(
                criteria, baseCriteria).list();
        
        if (criteria.getProductInstance() != null) {
        	tierInstances = filterByProductInstance(tierInstances, criteria.getProductInstance());
        }
        
        if (criteria.getService() != null) {
        	tierInstances = filterByService(tierInstances, criteria.getService());
        }
        return tierInstances;
	}
		
	 /**
     * Filter the result by environment instance
     *
     * @param tierInstances
     * @param productInstanceInput
     * @return tierInstances
     */
    private List<TierInstance> filterByProductInstance(
            List<TierInstance> tierInstances, ProductInstance productInstanceInput) {
        
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
    private List<TierInstance> filterByService(
            List<TierInstance> tierInstances, Service serviceInput) {
        List<TierInstance> result = new ArrayList<TierInstance>();
/*        for (TierInstance tierInstance : tierInstances) {         
        	List<Service> services = tierInstance.getServices();
        	
        	 for (Service serv : services) {
        		 if (serv.getName().equals(serviceInput.getName()))
                    result.add(tierInstance);  
        	 }
        }*/
        return result;
    }
}
