package com.telefonica.euro_iaas.paasmanager.model;



import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;


import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;

@Entity
public class ProductInstance extends InstallableInstance
	implements Comparable<ProductInstance>{

    @ManyToOne
    private ProductRelease productRelease;
	
    @Embedded
    private VM vm;
   	
    //private List<Attribute> attributes;
    /**
	 * Default Constructor
	 */
	public ProductInstance() {
		super();
	}

	   /**
     * <p>Constructor for ProductInstance.</p>
     *
     * @param application a {@link com.telefonica.euro_iaas.sdc.model.Product} object.
     * @param status a {@link com.telefonica.euro_iaas.sdc.model.ProductInstance.Status} object.
     */
    public ProductInstance(ProductRelease productRelease, Status status, VM vm,
            String vdc) {
        super(status);
        this.productRelease = productRelease;
        this.vm = vm;
        setVdc(vdc);
		setName();
    }


	/**
	 * @return the productRelease
	 */
	public ProductRelease getProductRelease() {
		return productRelease;
	}

	/**
	 * @param productRelease the productRelease to set
	 */
	public void setProductRelease(ProductRelease productRelease) {
		this.productRelease = productRelease;
	}

	/**
	 * @return the vm
	 */
	public VM getVm() {
		return vm;
	}

	/**
	 * @param vm the vm to set
	 */
	public void setVm(VM vm) {
		this.vm = vm;
	}

	/**
	 * @return the attributes
	 */
	/*public List<Attribute> getAttributes() {
		return attributes;
	}*/

	/**
	 * @param attributes the attributes to set
	 */
	/*public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}*/

	@Override
    public int compareTo(ProductInstance o) {
        return this.getProductRelease().getName().compareTo(
                o.getProductRelease().getName());
    }
	
	/*
	 * setting the name as fuction of applicationRelease.name 
	 * and environmentInstance.name
	 */
	private void setName(){
		this.name = productRelease.getName() + "-" + vm.getFqn();
	}

    
}