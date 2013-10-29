/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.Service;

/**
 * Provides some criteria to search TierInstance entities.
 * 
 * @author Jesus M. Movilla
 */
public class TierInstanceSearchCriteria extends AbstractSearchCriteria {

    private ProductInstance productInstance;
    private Service service;
    private String vdc;
    private EnvironmentInstance environmentInstance;

    /**
     * Default constructor
     */
    public TierInstanceSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param productInstance
     * @param environment
     */
    public TierInstanceSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType,
            ProductInstance productInstance, Service service, EnvironmentInstance environmentInstance, String vdc) {
        super(page, pageSize, orderBy, orderType);
        this.productInstance = productInstance;
        this.service = service;
        this.environmentInstance = environmentInstance;
        this.vdc = vdc;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param productInstance
     * @param environment
     */
    public TierInstanceSearchCriteria(String orderBy, String orderType, ProductInstance productInstance, Service service) {
        super(orderBy, orderType);
        this.productInstance = productInstance;
        this.service = service;
    }

    /**
     * @param page
     * @param pagesize
     * @param productInstance
     * @param environment
     */
    public TierInstanceSearchCriteria(Integer page, Integer pageSize, ProductInstance productInstance, Service service) {
        super(page, pageSize);
        this.productInstance = productInstance;
        this.service = service;
    }

    /**
     * @param instance
     * @param environment
     */
    public TierInstanceSearchCriteria(ProductInstance productInstance, Service service) {
        this.productInstance = productInstance;
        this.service = service;
    }

    /**
     * @return the productInstance
     */
    public ProductInstance getProductInstance() {
        return productInstance;
    }

    /**
     * @param ProductRelease
     *            the ProductRelease to set
     */
    public void setProductInstance(ProductInstance productInstance) {
        this.productInstance = productInstance;
    }

    /**
     * @return the service
     */
    public Service getService() {
        return service;
    }

    /**
     * @param service
     *            the service to set
     */
    public void setService(Service service) {
        this.service = service;
    }

    public String getVdc() {
        return vdc;
    }

    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    public EnvironmentInstance getEnvironmentInstance() {
        return environmentInstance;
    }

    public void setEnvironmentInstance(EnvironmentInstance environmentInstance) {
        this.environmentInstance = environmentInstance;
    }

}
