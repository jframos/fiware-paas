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

/**
 * The Product Instance entity.
 * 
 * @author henar
 */
@Entity
public class ProductInstance extends InstallableInstance implements Comparable<ProductInstance> {

    @ManyToOne
    private ProductRelease productRelease;
    private String taskId = "";

    /**
     * Default Constructor.
     */
    public ProductInstance() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param productRelease
     * @param status
     * @param vdc
     */
    public ProductInstance(ProductRelease productRelease, Status status, String vdc) {
        super(status);
        this.productRelease = productRelease;
        setVdc(vdc);
    }

    /**
     * It compares the product instance.
     */
    public int compareTo(ProductInstance arg0) {
        // TODO Auto-generated method stub
        return 0;
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

    public void setTaskId(String id) {
        taskId = id;
    }

    /**
     * It returns the dto.
     * 
     * @return ProductInstanceDto.class
     */
    public ProductInstanceDto toDto() {
        ProductInstanceDto pInstanceDto = new ProductInstanceDto();
        pInstanceDto.setName(getName());
        pInstanceDto.setTaskId(this.taskId);

        if (getProductRelease() != null) {
            pInstanceDto.setProductReleaseDto(getProductRelease().toDto());
        }
        return pInstanceDto;

    }

}
