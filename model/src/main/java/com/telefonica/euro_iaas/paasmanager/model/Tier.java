package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;

/**
 * Represents an artifact to be installed on a ProductRelease
 * 
 * @author Jesus M. Movilla
 * @version $Id: $
 */
@Entity
public class Tier {

	public final static String NAME_FIELD = "name";
	public final static String VDC_FIELD = "vdc";
	public final static String ENVIRONMENT_NAME_FIELD = "environmentname";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 256)
	private String name = "";
	private String vdc = "";
	private String environmentname = "";

	private String flavour = "";
	private String image = "";
	private String icono = "";
	// private String securityGroupName;
	private String keypair = "";
	private String floatingip = "false";

	private Integer maximumNumberInstances = new Integer(0);
	private Integer minimumNumberInstances = new Integer(0);
	private Integer initialNumberInstances = new Integer(0);

	@Column(length = 90000)
	private String payload;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tier_has_productReleases", joinColumns = { @JoinColumn(name = "tier_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "productRelease_ID", nullable = false, updatable = false) })
	private List<ProductRelease> productReleases;

	@ManyToOne(fetch = FetchType.EAGER)
	private SecurityGroup securityGroup;

	/**
	 * Default Constructor
	 */
	public Tier() {
	}

	public Tier(String name) {
		this.name = name;
	}

	/**
	 * @param name
	 * @param maximum_number_instances
	 * @param minimum_number_instances
	 * @param initial_number_instances
	 * @param productReleases
	 */
	public Tier(String name, Integer maximumNumberInstances,
			Integer minimumNumberInstances, Integer initialNumberInstances,
			List<ProductRelease> productReleases, String flavour, String image,
			String icono) {
		this.name = name;
		this.maximumNumberInstances = maximumNumberInstances;
		this.minimumNumberInstances = minimumNumberInstances;
		this.initialNumberInstances = initialNumberInstances;
		this.productReleases = productReleases;
		this.flavour = flavour;
		this.image = image;
		this.icono = icono;
	}

	public Tier(String name, Integer maximumNumberInstances,
			Integer minimumNumberInstances, Integer initialNumberInstances,
			List<ProductRelease> productReleases, String flavour, String image,
			String icono, String keypair, String floatingip, String payload) {
		this.name = name;
		this.maximumNumberInstances = maximumNumberInstances;
		this.minimumNumberInstances = minimumNumberInstances;
		this.initialNumberInstances = initialNumberInstances;
		this.productReleases = productReleases;

		this.flavour = flavour;
		this.image = image;
		this.icono = icono;
		// this.securityGroupName = securityGroupName;
		this.keypair = keypair;
		this.floatingip = floatingip;
		this.payload = payload;
	}

	public Tier(String name, Integer maximumNumberInstances,
			Integer minimumNumberInstances, Integer initialNumberInstances,
			List<ProductRelease> productReleases) {
		this.name = name;
		this.maximumNumberInstances = maximumNumberInstances;
		this.minimumNumberInstances = minimumNumberInstances;
		this.initialNumberInstances = initialNumberInstances;
		this.productReleases = productReleases;

	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	public void setEnviromentName(String environmentname) {
		this.environmentname = environmentname;
	}
	
	

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public String getVdc() {
		return vdc;
	}

	public void setVdc(String vdc) {
		this.vdc = vdc;
	}

	/**
	 * @return the maximum_number_instances
	 */
	public Integer getMaximumNumberInstances() {
		return maximumNumberInstances;
	}

	/**
	 * @return the minimum_number_instances
	 */
	public Integer getMinimumNumberInstances() {
		return minimumNumberInstances;
	}

	/**
	 * @return the initial_number_instances
	 */
	public Integer getInitialNumberInstances() {
		return initialNumberInstances;
	}

	/**
	 * @return the productReleases
	 */
	public List<ProductRelease> getProductReleases() {
		return productReleases;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setFlavour(String flavour) {
		this.flavour = flavour;
	}

	public String getFlavour() {
		return flavour;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return this.image;
	}

	/**
	 * @param maximum_number_instances
	 *            the maximum_number_instances to set
	 */
	public void setMaximumNumberInstances(Integer maximumNumberInstances) {
		this.maximumNumberInstances = maximumNumberInstances;
	}

	/**
	 * @param minimum_number_instances
	 *            the minimum_number_instances to set
	 */
	public void setMinimumNumberInstances(Integer minimumNumberInstances) {
		this.minimumNumberInstances = minimumNumberInstances;
	}

	/**
	 * @param initial_number_instances
	 *            the initial_number_instances to set
	 */
	public void setInitialNumberInstances(Integer initialNumberInstances) {
		this.initialNumberInstances = initialNumberInstances;
	}

	/**
	 * @param productReleases
	 *            the productReleases to set
	 */
	public void setProductReleases(List<ProductRelease> productReleases) {
		this.productReleases = productReleases;
	}

	public void addProductRelease(ProductRelease productRelease) {
		if (this.productReleases == null) {
			productReleases = new ArrayList<ProductRelease>();
		}

		productReleases.add(productRelease);
	}

	public void removeProductRelease(ProductRelease productRelease) {

		productReleases.remove(productRelease);
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public String getIcono() {
		return this.icono;
	}

	/*
	 * public void setSecurity_group(String securityGroupName) {
	 * this.securityGroupName = securityGroupName; }
	 * 
	 * public String getSecurity_group() { return this.securityGroupName; }
	 */

	public void setKeypair(String keypair) {
		this.keypair = keypair;
	}

	public String getKeypair() {
		return this.keypair;
	}

	public void setFloatingip(String floatingip) {
		this.floatingip = floatingip;
	}

	public String getFloatingip() {
		return this.floatingip;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getPayload() {
		return this.payload;
	}

	public SecurityGroup getSecurityGroup() {
		return this.securityGroup;
	}

	public void setSecurityGroup(SecurityGroup securityGroup) {
		this.securityGroup = securityGroup;
	}
	
	public String getProductNameBalanced ()
	{
		if (getProductReleases() != null)
		{
			for (ProductRelease productRelease: getProductReleases()) {
				Attribute attBalancer = productRelease.getAttribute("balancer");
				return attBalancer.getValue();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		Tier other = (Tier) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public TierDto toDto() {
		List<ProductReleaseDto> productReleasesDto = new ArrayList<ProductReleaseDto>();
		TierDto tierDto = new TierDto();
		tierDto.setName(getName());
		tierDto.setInitialNumberInstances(getInitialNumberInstances());
		tierDto.setMaximumNumberInstances(getMaximumNumberInstances());
		tierDto.setMinimumNumberInstances(getMinimumNumberInstances());
		tierDto.setIcono(getIcono());
		tierDto.setFlavour(getFlavour());
		tierDto.setImage(getImage());
		if (this.getSecurityGroup() != null)
			tierDto.setSecurity_group(this.getSecurityGroup().getName());
		tierDto.setKeypair(getKeypair());
		tierDto.setFloatingip(getFloatingip());

		if (getProductReleases() != null) {
			for (ProductRelease pRelease : getProductReleases()) {

				ProductReleaseDto pReleaseDto = new ProductReleaseDto();
				pReleaseDto.setProductName(pRelease.getProduct());
				pReleaseDto.setVersion(pRelease.getVersion());

				if (pRelease.getDescription() != null) {
					pReleaseDto
							.setProductDescription(pRelease.getDescription());
				}
				if (!productReleasesDto.contains(pReleaseDto)) {
				productReleasesDto.add(pReleaseDto);
				}
			}
		}

		tierDto.setProductReleaseDtos(productReleasesDto);
		return tierDto;
	}

}
