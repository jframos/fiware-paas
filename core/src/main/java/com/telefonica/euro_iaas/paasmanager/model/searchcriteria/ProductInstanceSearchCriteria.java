package com.telefonica.euro_iaas.paasmanager.model.searchcriteria;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;

/**
 * Provides some criteria to search Instance entities.
 * 
 * @author Jesus M. Movilla
 */
public class ProductInstanceSearchCriteria extends AbstractSearchCriteria {

    /**
     * The status of the instance (<i>this criteria return a list of entities<i>).
     */
    private List<Status> status;
    private EnvironmentInstance environmentInstance;
    private TierInstance tierInstance;
    private String productName;
    private ProductRelease productRelease;

    /**
     * Default constructor
     */
    public ProductInstanceSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param status
     * @param vm
     * @param productRelease
     */
    public ProductInstanceSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType,
            List<Status> status, ProductRelease productRelease) {
        super(page, pageSize, orderBy, orderType);
        this.status = status;
        this.productRelease = productRelease;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param status
     * @param vm
     * @param productRelease
     */
    public ProductInstanceSearchCriteria(String orderBy, String orderType, List<Status> status,
            EnvironmentInstance environmentInstance, TierInstance tierInstance) {
        super(orderBy, orderType);
        this.status = status;
        this.environmentInstance = environmentInstance;
        this.tierInstance = tierInstance;
    }

    /**
     * @return the status
     */
    public List<Status> getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(List<Status> status) {
        this.status = status;
    }

    /**
     * @return the vm
     */
    public EnvironmentInstance getEnvironmentInstance() {
        return environmentInstance;
    }

    /**
     * @param vm
     *            the vm to set
     */
    public void setEnvironmentInstance(EnvironmentInstance environmentInstance) {
        this.environmentInstance = environmentInstance;
    }

    public TierInstance getTierInstance() {
        return tierInstance;
    }

    /**
     * @param vm
     *            the vm to set
     */
    public void setTierInstance(TierInstance tierInstance) {
        this.tierInstance = tierInstance;
    }

    /**
     * @return the productRelease
     */
    public ProductRelease getProductRelease() {
        return productRelease;
    }

    /**
     * @param productRelease
     *            the productRelease to set
     */
    public void setProductRelease(ProductRelease productRelease) {
        this.productRelease = productRelease;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName
     *            the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

}
