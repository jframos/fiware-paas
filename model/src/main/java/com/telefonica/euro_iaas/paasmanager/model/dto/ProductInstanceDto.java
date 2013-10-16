package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.List;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;

/**
 * DTO to receive the complete information when a product release is going to be installed.
 * 
 * @author Jesus M. Movilla
 */
// @XmlRootElement
// @XmlAccessorType(XmlAccessType.FIELD)
public class ProductInstanceDto {

    private ProductReleaseDto productReleaseDto;

    private List<Attribute> attributes;
    private String vdc;
    private String name;
    private String taskId;

    /**
     */
    public ProductInstanceDto() {
    }

    /**
     * @param product
     * @param vm
     */
    public ProductInstanceDto(ProductReleaseDto productReleaseDto) {
        this.productReleaseDto = productReleaseDto;

    }

    /**
     * @return the product
     */
    public ProductReleaseDto getProductReleaseDto() {
        return productReleaseDto;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProductReleaseDto(ProductReleaseDto productReleaseDto) {
        this.productReleaseDto = productReleaseDto;
    }

    /**
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
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

    /**
     * @param vdc
     *            the vdc to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
