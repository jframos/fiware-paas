package com.telefonica.euro_iaas.paasmanager.model.searchcriteria;

import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;


/**
 * Provides some criteria to search Instance entities.
 *
 * @author Jesus M. Movilla
 *
 */
public class ProductInstanceSearchCriteria extends AbstractSearchCriteria {

    /**
     * The status of the instance (<i>this criteria return a list
     * of entities<i>).
     */
    private List<Status> status;
    private VM vm;
    private String productReleaseName;

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
     * @param productReleaseName
     */
    public ProductInstanceSearchCriteria(Integer page, Integer pageSize,
            String orderBy, String orderType, List<Status> status,
            VM vm, String productReleaseName) {
        super(page, pageSize, orderBy, orderType);
        this.status = status;
        this.vm = vm;
        this.productReleaseName = productReleaseName;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param status
     * @param vm
     * @param productReleaseName
     */
    public ProductInstanceSearchCriteria(String orderBy, String orderType,
    		List<Status> status,
            VM vm, String productReleaseName) {
        super(orderBy, orderType);
        this.status = status;
        this.vm = vm;
        this.productReleaseName = productReleaseName;
    }

    /**
     * @param page
     * @param pagesize
     * @param status
     * @param vm
     * @param productReleaseName
     */
    public ProductInstanceSearchCriteria(Integer page, Integer pageSize,
    		List<Status> status,
            VM vm, String productReleaseName) {
        super(page, pageSize);
        this.status = status;
        this.vm = vm;
        this.productReleaseName = productReleaseName;
    }

    /**
     * @param status
     * @param vm
     * @param productReleaseName
     */
    public ProductInstanceSearchCriteria(List<Status> status, VM vm, 
    		String productReleaseName) {
        this.status = status;
        this.vm = vm;
        this.productReleaseName = productReleaseName;
    }

    /**
     * @return the status
     */
    public List<Status> getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(List<Status> status) {
        this.status = status;
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
     * @return the productReleaseName
     */
    public String getProductReleaseName() {
        return productReleaseName;
    }

    /**
     * @param productReleaseName the productReleaseName to set
     */
    public void setProductReleaseName(String productReleaseName) {
        this.productReleaseName = productReleaseName;
    }

}
