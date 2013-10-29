/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Artifact Type(ex: web server, databases...
 * 
 * @author Jesus M. Movilla
 */

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ArtifactType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false, length = 256)
    private String name;

    @Column(length = 2048)
    private String description;

    // Could it be removed?
    @OneToOne
    private ProductType productType;

    /**
     * Default Constructor.
     */
    public ArtifactType() {

    }

    /**
     * @param name
     * @param description
     * @param productType
     */
    public ArtifactType(String name, String description, ProductType productType) {
        this.name = name;
        this.description = description;
        this.productType = productType;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
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
     * @return the productType
     */
    public ProductType getProductType() {
        return productType;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param productType
     *            the productType to set
     */
    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

}
