/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.telefonica.euro_iaas.paasmanager.model.dto.ProductInstanceDto;

@Entity
public class ProductInstance extends InstallableInstance implements Comparable<ProductInstance> {

    @ManyToOne
    private ProductRelease productRelease;
    private String taskId = "";

    /*
     * @JoinColumn(name = "tierInstance_id", referencedColumnName = "id")
     * @ManyToOne(targetEntity = TierInstance.class, optional = true, fetch = FetchType.LAZY)
     * @XmlTransient private TierInstance tierInstance;
     */

    /**
     * Default Constructor
     */
    public ProductInstance() {
        super();
    }

    /**
     * <p>
     * Constructor for ProductInstance.
     * </p>
     * 
     * @param application
     *            a {@link com.telefonica.euro_iaas.sdc.model.Product} object.
     * @param status
     *            a {@link com.telefonica.euro_iaas.sdc.model.ProductInstance.Status} object.
     */
    public ProductInstance(ProductRelease productRelease, Status status, String vdc) {
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
     * @param productRelease
     *            the productRelease to set
     */
    public void setProductRelease(ProductRelease productRelease) {
        this.productRelease = productRelease;
    }

    public int compareTo(ProductInstance arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public ProductInstanceDto toDto() {
        ProductInstanceDto pInstanceDto = new ProductInstanceDto();
        pInstanceDto.setName(getName());
        pInstanceDto.setTaskId(this.taskId);

        if (getProductRelease() != null) {
            pInstanceDto.setProductReleaseDto(getProductRelease().toDto());
        }

        /*
         * if (getPrivateAttributes()!= null){ pInstanceDto.setAttributes(getPrivateAttributes()); }
         */
        return pInstanceDto;

    }

    public void setTaskId(String id) {
        taskId = id;

    }

}
