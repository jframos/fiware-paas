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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;

/**
 * Represents an artifact to be installed on a ProductRelease.
 *
 * @author Henar Mu�oz
 * @version $Id: $
 */

@SuppressWarnings("serial")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ArtifactDto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @XmlTransient
    private Long id;

    @Column(unique = true, nullable = false, length = 256)
    private String name;
    @Column(length = 2048)
    private String path;

    // productrelease.id?
    private ProductReleaseDto productReleaseDto;

    private List<Attribute> attributes;

    /**
     * Default Constructor.
     */
    public ArtifactDto() {

    }

    /**
     * @param name
     * @param path
     * @param productReleaseDto
     */
    public ArtifactDto(String name, String path, ProductReleaseDto productReleaseDto) {
        this.name = name;
        this.path = path;
        this.productReleaseDto = productReleaseDto;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the productRelease
     */
    public ProductReleaseDto getProductReleaseDto() {
        return productReleaseDto;
    }

    /**
     * @param productReleaseDto the productRelease to set
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
     * @param attributes the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * Add a new attribute.
     *
     * @param attribute the attribute
     */
    public void addAttribute(Attribute attribute) {
        if (attributes == null) {
            attributes = new ArrayList<Attribute>();
        }
        attributes.add(attribute);
    }

    /**
     * @return the attributes as a Map
     */
    public Map<String, String> getMapAttributes() {
        Map<String, String> atts = new HashMap<String, String>();
        for (Attribute att : attributes) {
            atts.put(att.getKey(), att.getValue());
        }
        return atts;
    }

    /**
     * Create a new Artifact with the information of the class.
     * @return
     */
    public Artifact fromDto() {
        Artifact artifact = new Artifact();
        artifact.setName(this.getName());
        if (this.getPath() != null) {
            artifact.setPath(this.getPath());
        }
        if (this.getProductReleaseDto() != null) {
            artifact.setProductRelease(this.getProductReleaseDto().fromDto());
        }
        return artifact;
    }

    /**
     * Constructs a <code>String</code> with all attributes
     * in name = value format.
     *
     * @return a <code>String</code> representation
     * of this object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("[[ArtifactDto]");
        sb.append("[id = ").append(this.id).append("]");
        sb.append("[name = ").append(this.name).append("]");
        sb.append("[path = ").append(this.path).append("]");
        sb.append("[productReleaseDto = ").append(this.productReleaseDto).append("]");
        sb.append("[attributes = ").append(this.attributes).append("]");
        sb.append("]");
        return sb.toString();
    }


}
