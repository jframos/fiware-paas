package com.telefonica.euro_iaas.paasmanager.manager.impl;

import java.util.List;
import java.util.NoSuchElementException;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
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
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;

public class ProductInstanceManagerImpl implements ProductInstanceManager {

    private ProductInstanceDao productInstanceDao;
    private ProductInstallator productInstallator;
    private ProductReleaseDao productReleaseDao;
    
    @Override
	public ProductInstance install(VM vm, String vdc, ProductRelease productRelease,
			List<Attribute> attributes) throws InvalidEntityException, NotUniqueResultException {
		
    	//¿Validate if there is one ProductInstance in BBDD and the status is OK?
    	ProductInstance productInstance;

    	productInstance = getProductToInstall(productRelease, vm, vdc, attributes);
    	
    	productInstallator.install(productInstance);  	
    	
    	productInstance = updateProductInstance(productInstance);
        
    	return  productInstance;
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
	 * @throws NotUniqueResultException, 
     */
    private ProductInstance getProductToInstall(ProductRelease productRelease, VM vm,
            String vdc, List<Attribute> attributes) throws NotUniqueResultException {
        ProductInstance productInstance = null;

        ProductInstanceSearchCriteria criteria =
                    new ProductInstanceSearchCriteria();
         criteria.setVm(vm);
         criteria.setProductRelease(productRelease);
         criteria.setProductName(productRelease.getName());
         
        ProductRelease pRelease;
		
        try {
        	pRelease = productReleaseDao.load(productRelease.getName());
		} catch (EntityNotFoundException e1) {
			pRelease = new ProductRelease (
					productRelease.getName(),
					productRelease.getVersion());
			if (productRelease.getDescription()!= null)
				pRelease.setDescription(productRelease.getDescription());
			if (productRelease.getAttributes()!= null)
				pRelease.setAttributes(productRelease.getAttributes());
			if (productRelease.getProductType()!=null)
				pRelease.setProductType(productRelease.getProductType());
			if (productRelease.getSupportedOOSS() != null)
				pRelease.setSupportedOOSS(productRelease.getSupportedOOSS());
			if (productRelease.getTransitableReleases()!=null)
				pRelease.setTransitableReleases(productRelease.getTransitableReleases());
			
		}
        
        try {
			productInstance = productInstanceDao.findUniqueByCriteria(criteria);
			productInstance.setProductRelease(pRelease);	
			productInstance.setVm(vm);
			productInstance.setVdc(vdc);
			
		} catch (NoSuchElementException nsee) {						
			productInstance = new ProductInstance (pRelease, 
					Status.INSTALLING, 
					vm, vdc);			
    	}
        //productInstance.setName(pRelease.getName() + "-" + pRelease.getVersion() 
			//	+ "-" + vm.getHostname() + "-" + vm.getDomain());
        return productInstance;
    }
    
    
    private ProductInstance updateProductInstance (ProductInstance productInstance) {
    	
    	ProductRelease productRelease = productInstance.getProductRelease();
    	
   		try {
			productRelease = productReleaseDao.load(productRelease.getName());
		} catch (EntityNotFoundException e) {
				try {
					productRelease = productReleaseDao.create(productRelease);
					productInstance.setProductRelease(productRelease);
				} catch (InvalidEntityException e1) {

				} catch (AlreadyExistsEntityException e1) {

				}				
		}

   		try {
			productInstance = productInstanceDao.load(productInstance.getId());
		} catch (EntityNotFoundException e) {
			try {
				productInstance = productInstanceDao.create(productInstance);
			} catch (InvalidEntityException e1) {

			} catch (AlreadyExistsEntityException e1) {

			}
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
     * @param productReleaseDao
     *            the productReleaseDao to set
     */
    public void setProductReleaseDao(ProductReleaseDao productReleaseDao) {
        this.productReleaseDao = productReleaseDao;
    }
    
    /**
     * @param productInstallator
     *            the productInstallator to set
     */
    public void setProductInstallator(ProductInstallator productInstallator) {
        this.productInstallator = productInstallator;
    }
}
