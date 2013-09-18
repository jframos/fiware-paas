package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;

/**
 * DTO for application Instance to receive rest request
 * @author  Jesus M. Movilla
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationInstanceDto {

    private String applicationName;
    private String version;
    private List<ProductReleaseDto> products;
    private List<Attribute> attributes;

    /**
     */
    public ApplicationInstanceDto() {
    }
    /**
     * @param applicationName
     * @param vm
     * @param environmentInstace
     */
    public ApplicationInstanceDto(String applicationName, String version,
    		List<ProductReleaseDto> products) {
        this.version = version;
        this.applicationName = applicationName;
        this.products = products;
    }
    /**
     * @return the applicationName
     */
    public String getApplicationName() {
        return applicationName;
    }
    /**
     * @param applicationName the applicationName to set
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
    /**
     * @return the products
     */
    public List<ProductReleaseDto> getProducts() {
        return products;
    }
    /**
     * @param products the products to be set
     */
    public void setProducts(List<ProductReleaseDto> products) {
        this.products = products;
    }
    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }
    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }
    /**
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }
    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

}

