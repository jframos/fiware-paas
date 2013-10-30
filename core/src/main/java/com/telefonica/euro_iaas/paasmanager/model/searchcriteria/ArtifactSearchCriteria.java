/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.searchcriteria;

import com.telefonica.euro_iaas.commons.dao.AbstractSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;

public class ArtifactSearchCriteria extends AbstractSearchCriteria {

    private ProductRelease productRelease;

    /**
     * Default constructor
     */
    public ArtifactSearchCriteria() {
    }

    /**
     * @param page
     * @param pagesize
     * @param orderBy
     * @param orderType
     * @param vm
     * @param productRelease
     */
    public ArtifactSearchCriteria(Integer page, Integer pageSize, String orderBy, String orderType,
            ProductRelease productRelease) {
        super(page, pageSize, orderBy, orderType);
        this.productRelease = productRelease;
    }

    /**
     * @param orderBy
     * @param orderType
     * @param vm
     * @param productRelease
     */
    public ArtifactSearchCriteria(String orderBy, String orderType, ProductRelease productRelease) {
        super(orderBy, orderType);
        this.productRelease = productRelease;
    }

    /**
     * @param page
     * @param pagesize
     * @param vm
     * @param artifactName
     * @param productRelease
     */
    public ArtifactSearchCriteria(Integer page, Integer pageSize, ProductRelease productRelease) {
        super(page, pageSize);
        this.productRelease = productRelease;
    }

    /**
     * @param productRelease
     */
    public ArtifactSearchCriteria(ProductRelease productRelease) {
        this.productRelease = productRelease;
    }

    /**
     * @return the productRelease
     */
    public ProductRelease getProductRelease() {
        return productRelease;
    }

    /**
     * @param ProductRelease
     *            the productRelease to set
     */
    public void setProductRelease(ProductRelease productRelease) {
        this.productRelease = productRelease;
    }

}
