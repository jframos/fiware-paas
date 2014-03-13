/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;

/**
 * DTO to receive the complete information when a product release is going to be installed.
 * 
 * @author Jesus M. Movilla
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductInstanceDto {

    private ProductReleaseDto productReleaseDto;

    private Set<Attribute> attributes;
    private String vdc;
    private String name;
    private String taskId;

    /**
     */
    public ProductInstanceDto() {
    }

    public ProductInstanceDto(ProductReleaseDto productReleaseDto) {
        this.productReleaseDto = productReleaseDto;

    }

    /**
     * @return the product
     */
    public ProductReleaseDto getProductReleaseDto() {
        return productReleaseDto;
    }

    public void setProductReleaseDto(ProductReleaseDto productReleaseDto) {
        this.productReleaseDto = productReleaseDto;
    }

    /**
     * @return the attributes
     */
    public Set<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the vdc
     */
    public String getVdc() {
        return vdc;
    }

    /**
     * @param vdc
     *            the vdc to set
     */
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
