/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Represents an artifact to be installed on a ProductRelease.
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Artifact {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @XmlTransient
    private Long id;

    @Column(unique = true, nullable = false, length = 256)
    private String name;
    @Column(length = 2048)
    private String path;

    @ManyToOne
    private ArtifactType artifactType;

    // productrelease.id?
    @ManyToOne
    private ProductRelease productRelease;

    @OneToMany(targetEntity = Artifact.class, cascade = CascadeType.ALL)
    private List<Attribute> attributes;

    /**
     * Default Constructor.
     */
    public Artifact() {

    }

    /**
     * @param name
     * @param path
     * @param artifactType
     * @param productRelease
     */
    public Artifact(String name, String path, ArtifactType artifactType, ProductRelease productRelease) {
        this.name = name;
        this.path = path;
        this.artifactType = artifactType;
        this.productRelease = productRelease;
    }

    /**
     * Add a new attribute.
     * 
     * @param attribute
     *            the attribute
     */
    public void addAttribute(Attribute attribute) {
        if (attributes == null) {
            attributes = new ArrayList<Attribute>();
        }
        attributes.add(attribute);
    }

    /**
     * @return the artifactType
     */
    public ArtifactType getArtifactType() {
        return artifactType;
    }

    /**
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the productRelease
     */
    public ProductRelease getProductRelease() {
        return productRelease;
    }

    /**
     * @param artifactType
     *            the artifactType to set
     */
    public void setArtifactType(ArtifactType artifactType) {
        this.artifactType = artifactType;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param path
     *            the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @param productRelease
     *            the productRelease to set
     */
    public void setProductRelease(ProductRelease productRelease) {
        this.productRelease = productRelease;
    }

}
