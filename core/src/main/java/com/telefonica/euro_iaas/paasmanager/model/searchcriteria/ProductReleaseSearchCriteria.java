/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;

/**
 * Provides some criteria to search ProductInstance entities.
 * 
 * @author Jesus M. Movilla
 */
public class ProductReleaseSearchCriteria extends AbstractSearchCriteria {

    /**
     * The productName.
     */
    private String productName;

    /**
     * The osType.
     */
    private String osType;

    /**
     * Default constructor
     */
    public ProductReleaseSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param productName
     */
    public ProductReleaseSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType,
            String productName) {
        super(page, pageSize, orderBy, orderType);
        this.productName = productName;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param productName
     */
    public ProductReleaseSearchCriteria(String orderBy, String orderType, String productName) {
        super(orderBy, orderType);
        this.productName = productName;
    }

    /**
     * @param page
     * @param pagesize
     * @param product
     */
    public ProductReleaseSearchCriteria(Integer page, Integer pageSize, String productName) {
        super(page, pageSize);
        this.productName = productName;
    }

    /**
     * @param vm
     */
    public ProductReleaseSearchCriteria(String productName) {
        this.productName = productName;
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

    /**
     * @return the osType
     */
    public String getOSType() {
        return osType;
    }

    /**
     * @param osType
     *            the osTypeto set
     */
    public void setOSType(String osType) {
        this.osType = osType;
    }
}
