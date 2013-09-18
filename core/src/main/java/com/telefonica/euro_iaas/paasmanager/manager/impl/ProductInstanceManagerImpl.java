package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.paasmanager.manager.ProductInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.util.ProductInstallator;
import com.telefonica.euro_iaas.paasmanager.dao.ProductInstanceDao;

public class ProductInstanceManagerImpl implements ProductInstanceManager {

    private ProductInstanceDao productInstanceDao;
    private ProductInstallator productInstallator;
    
    @Override
	public ProductInstance install(VM vm, String vdc, ProductRelease productRelease,
			List<Attribute> attributes) throws InvalidEntityException {
		
    	ProductInstance productInstance 
    		= getProductToInstall(productRelease, vm, vdc, attributes);
		
    	productInstallator.install(productInstance);
    	        
        return  productInstanceDao.update(productInstance);
	}

	@Override
	public void uninstall(ProductInstance productInstance) {
		// TODO Auto-generated method stub

	}

	@Override
	public ProductInstance load(String vdc, Long id)
			throws EntityNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductInstance loadByCriteria(ProductInstanceSearchCriteria criteria)
			throws EntityNotFoundException, NotUniqueResultException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductInstance> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductInstance> findByCriteria(
			ProductInstanceSearchCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}
	
	 /**
     * Creates or find the product instance in installation operation.
     * @param product
     * @param vm
     * @return
     */
    private ProductInstance getProductToInstall(ProductRelease productRelease, VM vm,
            String vdc, List<Attribute> attributes) {
        ProductInstance productInstance;
        try {
            ProductInstanceSearchCriteria criteria =
                    new ProductInstanceSearchCriteria();
            criteria.setVm(vm);
            criteria.setProductReleaseName(productRelease.getName());
            
            productInstance = productInstanceDao.findUniqueByCriteria(criteria);
            
        } catch (NotUniqueResultException e) {
        	productInstance = new ProductInstance(productRelease,
        			Status.UNINSTALLED, vm, vdc);

        }
        return productInstance;
    }

	
    // //////////// I.O.C /////////////
    /**
     * @param productInstanceDao
     *            the productInstanceDao to set
     */
    public void setProductInstanceDao(ProductInstanceDao productInstanceDao) {
        this.productInstanceDao = productInstanceDao;
    }

    /**
     * @param productInstallator
     *            the productInstallator to set
     */
    public void setProductInstallator(ProductInstallator productInstallator) {
        this.productInstallator = productInstallator;
    }
}
