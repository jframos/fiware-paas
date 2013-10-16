package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.List;

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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;

/**
 * A product release is a concrete version of a given product.
 * 
 * @author Jesus M. Movilla
 * 
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
	 * 
	 * @GeneratedValue(strategy = GenerationType.AUTO)
	 * 
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
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Attribute> attributes;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Metadata> metadatas;
	
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

	// @ManyToMany(fetch = FetchType.LAZY, mappedBy = "productReleases")
	// private List<Tier> tiers;

	public ProductRelease() {

	}

	/**
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
	public ProductRelease(String product, String version, String description,
			List<Attribute> attributes,
			List<ProductRelease> transitableReleases, List<OS> supportedOOSS,
			Boolean withArtifact, ProductType productType) {
		this.name = product + "-" + version;
		this.product = product;
		this.version = version;
		this.description = description;
		// this.attributes = attributes;
		this.transitableReleases = transitableReleases;
		this.supportedOOSS = supportedOOSS;
		this.withArtifact = withArtifact;
		this.productType = productType;
	}

	public ProductRelease(String name, String version, String description,
			List<Attribute> attributes) {
		this.product = name;
		this.version = version;
		this.name = product + "-" + version;
		this.description = description;
		this.attributes = attributes;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product
	 *            the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	/*
	 * public void setAttributesPort(List<Attribute> attributes) {
	 * this.attributesPort = attributes; }
	 */

	/**
	 * @return the attributes
	 */
	/*
	 * public List<Attribute> getAttributesPorts() { return attributesPort; }
	 * 
	 * public void addAttributeport(Attribute attributeport) { if
	 * (attributesPort == null) { attributesPort = new ArrayList<Attribute>(); }
	 * attributesPort.add(attributeport); }
	 */

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the metadatas
	 */
	public List<Metadata> getMetadatas() {
		return metadatas;
	}

	/**
	 * @param metadatas
	 *            the metadatas to set
	 */
	public void setMetadatas(List<Metadata> metadatas) {
		this.metadatas = metadatas;
	}
	
	/**
	 * @return the transitableReleases
	 */
	public List<ProductRelease> getTransitableReleases() {
		return transitableReleases;
	}

	/**
	 * @param transitableReleases
	 *            the transitableReleases to set
	 */
	public void setTransitableReleases(List<ProductRelease> transitableReleases) {
		this.transitableReleases = transitableReleases;
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

	/**
	 * @return the supportedOOSS
	 */
	public List<OS> getSupportedOOSS() {
		return supportedOOSS;
	}

	/**
	 * @param supportedOOSS
	 *            the supportedOOSS to set
	 */
	public void setSupportedOOSS(List<OS> supportedOOSS) {
		this.supportedOOSS = supportedOOSS;
	}

	/**
	 * @return the withArtifact
	 */
	public Boolean getWithArtifact() {
		return withArtifact;
	}

	/**
	 * @param withArtifact
	 *            the withArtifact to set
	 */
	public void setWithArtifact(Boolean withArtifact) {
		this.withArtifact = withArtifact;
	}

	/**
	 * @return the productType
	 */
	public ProductType getProductType() {
		return productType;
	}

	/**
	 * @param productType
	 *            the productType to set
	 */
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public Attribute getAttribute(String key) {
		if (attributes == null)
			return null;
		for (Attribute attribute : attributes) {
			if (attribute.getKey().equals(key))
				return attribute;
		}
		return null;
	}

	public Metadata getMetadata(String key) {
		if (metadatas == null)
			return null;
		for (Metadata metadata : metadatas) {
			if (metadata.getKey().equals(key))
				return metadata;
		}
		return null;
	}
	
	public void addAttribute(Attribute attribute) {
		if (attributes == null)
			attributes = new ArrayList();
		attributes.add(attribute);
	}

	public void addMetadata(Metadata metadata) {
		if (metadatas == null)
			metadatas = new ArrayList();
		metadatas.add(metadata);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductRelease other = (ProductRelease) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public ProductReleaseDto toDto() {
		ProductReleaseDto pReleaseDto = new ProductReleaseDto();

		pReleaseDto.setProductName(getProduct());
		pReleaseDto.setVersion(getVersion());
		
		
		if (getDescription() != null)
			pReleaseDto.setProductDescription(getDescription());

		if (getAttributes() != null)
			pReleaseDto.setPrivateAttributes(getAttributes());

		return pReleaseDto;
	}
	
	@SuppressWarnings("unchecked")
	public void fromSdcJson(JSONObject jsonNode) {
		version = jsonNode.getString("version");
		product = jsonNode.getJSONObject("product").getString("name");
		name = product + "-" + version;
		if (jsonNode.containsKey("releaseNotes"))
			description = jsonNode.getString("releaseNotes");
		
		JSONObject productJson = jsonNode.getJSONObject("product");
		//Attributes
		if (productJson.containsKey("attributes")){
			List<Attribute> attributes = new ArrayList<Attribute>();
			productJson = formatJsonArray(productJson);
			JSONArray attributtesJsonArray = productJson.getJSONArray("attributes");
			for(int i = 0; i < attributtesJsonArray.size(); i++)	{
				JSONObject object = attributtesJsonArray.getJSONObject(i);
				Attribute attribute = new Attribute();
				attribute.fromJson(object);
				attributes.add(attribute);
			}
			setAttributes(attributes);
		}
		
		//SSOO
		if (jsonNode.containsKey("supportedOOSS")){
			JSONArray ssooJsonArray = jsonNode.getJSONArray("supportedOOSS");
			List<OS> ooss = new ArrayList<OS>();
			for(int i = 0; i < ssooJsonArray.size(); i++)	{
				JSONObject object = ssooJsonArray.getJSONObject(i);
				OS os = new OS();
				os.fromJson(object);
				ooss.add(os);
			}
			setSupportedOOSS(ooss);
		}
	}
	
	private JSONObject formatJsonArray(JSONObject productJson){
		String stringProductJson = productJson.toString();
		if (stringProductJson.contains("\"attributes\":{")){
			stringProductJson = stringProductJson.replace("\"}}","\"}]}");
			stringProductJson = stringProductJson.replace("\"attributes\":{","\"attributes\":[{");
			
		}
		return JSONObject.fromObject(stringProductJson);
	}

}
