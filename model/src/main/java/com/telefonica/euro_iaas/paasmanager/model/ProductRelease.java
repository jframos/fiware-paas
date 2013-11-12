/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * A product release is a concrete version of a given product.
 * 
 * @author Jesus M. Movilla
 */

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "ProductRelease")
public class ProductRelease {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    @XmlTransient
    private Long id;

    /*
     * @Id
     * @GeneratedValue(strategy = GenerationType.AUTO)
     * @XmlTransient private Long id;
     */

    @Column(nullable = false, length = 256)
    private String name;
    @Column(nullable = false, length = 256)
    private String product;
    @Column(nullable = false, length = 256)
    private String version;

    @Column(length = 2048)
    private String description;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Attribute> attributes = null;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Metadata> metadatas;

    // @OneToMany(cascade = CascadeType.ALL)
    // private List<Attribute> attributesPort;

    @XmlTransient
    @ManyToMany
    private List<ProductRelease> transitableReleases = null;

    @ManyToMany
    @JoinTable(name = "productRelease_has_ooss")
    private List<OS> supportedOOSS = null;

    @Column(nullable = true)
    private Boolean withArtifact = false;

    @ManyToOne
    private ProductType productType = null;

    /**
     * Constructor.
     */
    public ProductRelease() {
      //  attributes = new ArrayList<Attribute>();
        metadatas = new HashSet<Metadata>();

    }

    /**
     * Constructor.
     * 
     * @param product
     * @param version
     * @param object2
     * @param list
     * @param product2
     * @param object
     */
    public ProductRelease(String product, String version) {
        this.name = product + "-" + version;
        this.product = product;
        this.version = version;
      //  attributes = new ArrayList<Attribute>();
        metadatas = new HashSet<Metadata>();
    }

    /**
     * @param name
     * @param version
     * @param description
     * @param attributes
     */
    public ProductRelease(String name, String version, String description, Set<Attribute> attributes) {
        this.product = name;
        this.version = version;
        this.name = product + "-" + version;
        this.description = description;
        this.attributes = attributes;
     //   attributes = new ArrayList<Attribute>();
        metadatas = new HashSet<Metadata>();
    }

    /**
     * @param product
     * @param version
     * @param description
     * @param attributes
     * @param transitableReleases
     * @param supportedOOSS
     * @param withArtifact
     * @param productType
     */
    public ProductRelease(String product, String version, String description, List<Attribute> attributes,
            List<ProductRelease> transitableReleases, List<OS> supportedOOSS, Boolean withArtifact,
            ProductType productType) {
        this.name = product + "-" + version;
        this.product = product;
        this.version = version;
        this.description = description;
        // this.attributes = attributes;
        this.transitableReleases = transitableReleases;
        this.supportedOOSS = supportedOOSS;
        this.withArtifact = withArtifact;
        this.productType = productType;
     //   attributes = new ArrayList<Attribute>();
        metadatas = new HashSet<Metadata>();
    }

    /**
     * Add an attribute to the product release.
     * 
     * @param attribute
     */
    public void addAttribute(Attribute attribute) {
        if (attributes == null) {
            attributes = new HashSet<Attribute>();
        }
        attributes.add(attribute);
    }

    /**
     * Add a metadata.
     * 
     * @param metadata
     */
    public void addMetadata(Metadata metadata) {
        if (metadatas == null) {
            metadatas = new HashSet<Metadata>();
        }
        metadatas.add(metadata);
    }

    /**
     * Add a transitable release.
     * 
     * @param transitableRelease
     *            the new release.
     */
    public void addTransitableRelease(ProductRelease transitableRelease) {
        if (transitableReleases == null) {
            transitableReleases = new ArrayList<ProductRelease>();
        }
        transitableReleases.add(transitableRelease);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ProductRelease other = (ProductRelease) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    private JSONObject formatJsonArray(JSONObject productJson) {
        String stringProductJson = productJson.toString();
        if (stringProductJson.contains("\"attributes\":{")) {
            stringProductJson = stringProductJson.replace("\"}}", "\"}]}");
            stringProductJson = stringProductJson.replace("\"attributes\":{", "\"attributes\":[{");

        }
        return JSONObject.fromObject(stringProductJson);
    }

    /**
     * the json.
     * 
     * @param jsonNode
     */
    public void fromSdcJson(JSONObject jsonNode) {
        version = jsonNode.getString("version");
        product = jsonNode.getJSONObject("product").getString("name");
        name = product + "-" + version;
        if (jsonNode.containsKey("releaseNotes")) {
            description = jsonNode.getString("releaseNotes");
        }

        JSONObject productJson = jsonNode.getJSONObject("product");
        // Attributes
        if (productJson.containsKey("attributes")) {
            Set<Attribute> attributes = new HashSet<Attribute>();
            productJson = formatJsonArray(productJson);
            JSONArray attributtesJsonArray = productJson.getJSONArray("attributes");
            for (int i = 0; i < attributtesJsonArray.size(); i++) {
                JSONObject object = attributtesJsonArray.getJSONObject(i);
                Attribute attribute = new Attribute();
                attribute.fromJson(object);
                attributes.add(attribute);
            }
            setAttributes(attributes);
        }

        // SSOO
        if (jsonNode.containsKey("supportedOOSS")) {
            JSONArray ssooJsonArray = jsonNode.getJSONArray("supportedOOSS");
            List<OS> ooss = new ArrayList<OS>();
            for (int i = 0; i < ssooJsonArray.size(); i++) {
                JSONObject object = ssooJsonArray.getJSONObject(i);
                OS os = new OS();
                os.fromJson(object);
                ooss.add(os);
            }
            setSupportedOOSS(ooss);
        }
    }

    /**
     * Get the attribute with the key.
     * 
     * @param key
     * @return
     */
    public Attribute getAttribute(String key) {
        if (attributes == null) {
            return null;
        }
        for (Attribute attribute : attributes) {
            if (attribute.getKey().equals(key)) {
                return attribute;
            }
        }
        return null;
    }

    /**
     * @return the attributes
     */
    public Set<Attribute> getAttributes() {
    	if (attributes == null) {
    		attributes = new HashSet<Attribute> ();
    	}
        return attributes;
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
     * Get the metadata from the key.
     * 
     * @param key
     * @return
     */
    public Metadata getMetadata(String key) {
        if (metadatas == null) {
            return null;
        }
        for (Metadata metadata : metadatas) {
            if (metadata.getKey().equals(key)) {
                return metadata;
            }
        }
        return null;
    }

    /**
     * @return the metadatas
     */
    public Set<Metadata> getMetadatas() {
    	if (metadatas == null) {
    		metadatas = new HashSet<Metadata> ();
    	}
        return metadatas;
    }

    /**
     * @return the id
     */
    public String getName() {
        return name;
    }

    /**
     * @return the product
     */
    public String getProduct() {
        return product;
    }

    /**
     * @return the productType
     */
    public ProductType getProductType() {
        return productType;
    }

    /**
     * @return the supportedOOSS
     */
    public List<OS> getSupportedOOSS() {
        return supportedOOSS;
    }

    /**
     * @return the transitableReleases
     */
    public List<ProductRelease> getTransitableReleases() {
        return transitableReleases;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return the withArtifact
     */
    public Boolean getWithArtifact() {
        return withArtifact;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param metadatas
     *            the metadatas to set
     */
    public void setMetadatas(Set<Metadata> metadatas) {
        this.metadatas = metadatas;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param product
     *            the product to set
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * @param productType
     *            the productType to set
     */
    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    /**
     * @param supportedOOSS
     *            the supportedOOSS to set
     */
    public void setSupportedOOSS(List<OS> supportedOOSS) {
        this.supportedOOSS = supportedOOSS;
    }

    /**
     * @param transitableReleases
     *            the transitableReleases to set
     */
    public void setTransitableReleases(List<ProductRelease> transitableReleases) {
        this.transitableReleases = transitableReleases;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @param withArtifact
     *            the withArtifact to set
     */
    public void setWithArtifact(Boolean withArtifact) {
        this.withArtifact = withArtifact;
    }

    /**
     * the dto representation.
     * 
     * @return
     */
    public ProductReleaseDto toDto() {
        ProductReleaseDto pReleaseDto = new ProductReleaseDto();

        pReleaseDto.setProductName(getProduct());
        pReleaseDto.setVersion(getVersion());

        if (getDescription() != null) {
            pReleaseDto.setProductDescription(getDescription());
        }

        if (getAttributes() != null) {
            pReleaseDto.setPrivateAttributes(getAttributes());
        }

        return pReleaseDto;
    }

}
