/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.model.dto;

import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;

/**
 * DTO to receive rest request with the product releases objects.
 * 
 * @author Jesus M. Movilla *
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductReleaseDto {

    private String productName;
    private String productDescription;
    private String version;
    private Set<Attribute> attributes;


    /**
     */
    public ProductReleaseDto() {
    }

    /**
     * @param productName
     * @param version
     * @param releaseNotes
     * @param privateAttributes
     * @param supportedOS
     * @param transitableReleases
     */
    public ProductReleaseDto(String productName, String productDescription, String version) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.version = version;
    }

    public ProductReleaseDto(String productName, String version) {
        this.productName = productName;
        this.version = version;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /*
     * public String getReleaseNotes() { return releaseNotes; } public void setReleaseNotes(String releaseNotes) {
     * this.releaseNotes = releaseNotes; }
     */
    public Set<Attribute> getPrivateAttributes() {
        return attributes;
    }

    public void setPrivateAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    /*
     * public List<OS> getSupportedOS() { return supportedOS; } public void setSupportedOS(List<OS> supportedOS) {
     * this.supportedOS = supportedOS; } public List<ProductRelease> getTransitableReleases() { return
     * transitableReleases; } public void setTransitableReleases(List<ProductRelease> transitableReleases) {
     * this.transitableReleases = transitableReleases; }
     */

    public ProductRelease fromDto() {
        ProductRelease productRelease = new ProductRelease(this.getProductName(), this.getVersion());
        return productRelease;
    }

}
