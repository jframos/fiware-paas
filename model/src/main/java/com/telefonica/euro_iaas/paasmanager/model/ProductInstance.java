package com.telefonica.euro_iaas.paasmanager.model;




import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;

@Entity
public class ProductInstance extends InstallableInstance
	implements Comparable<ProductInstance>{
	
	@ManyToOne
    private ProductRelease productRelease;
    
	/*@JoinColumn(name = "tierInstance_id", referencedColumnName = "id")
	@ManyToOne(targetEntity = TierInstance.class, optional = true, fetch = FetchType.LAZY)
	@XmlTransient
	private TierInstance tierInstance;*/
	
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
    public ProductInstance(ProductRelease productRelease, Status status, 
            String vdc) {
        super(status);
        this.productRelease = productRelease;
        setVdc(vdc);
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
	


	public int compareTo(ProductInstance arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
    
}