package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.EnvironmentInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

/**
 * default Environment implementation
 * 
 * @author Henar Muñoz
 * 
 */
@Path("/catalog/org/{org}/environment")
@Component
@Scope("request")
public class AbstractEnvironmentResourceImpl implements
		AbstractEnvironmentResource {

	@InjectParam("environmentManager")
	private EnvironmentManager environmentManager;

	private SystemPropertiesProvider systemPropertiesProvider;

	public void delete(String org, String envName)
			throws EnvironmentInstanceNotFoundException, InvalidEntityException {
		ClaudiaData claudiaData = new ClaudiaData(org, null, null);

		if (systemPropertiesProvider.getProperty(
				SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
			//claudiaData.setUser(getCredentials());
		}
		try {
			Environment env = environmentManager.load(envName);
			environmentManager.destroy(claudiaData, env);

		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(e, 404);
		} catch (InfrastructureException e) {
			throw new WebApplicationException(e, 500);
		}

	}

	public List<EnvironmentDto> findAll(String org, Integer page,
			Integer pageSize, String orderBy, String orderType) {
		EnvironmentSearchCriteria criteria = new EnvironmentSearchCriteria();

		criteria.setOrg(org);

		if (page != null && pageSize != null) {
			criteria.setPage(page);
			criteria.setPageSize(pageSize);
		}
		if (!StringUtils.isEmpty(orderBy)) {
			criteria.setOrderBy(orderBy);
		}
		if (!StringUtils.isEmpty(orderType)) {
			criteria.setOrderBy(orderType);
		}

		List<Environment> env = environmentManager.findByCriteria(criteria);

		List<EnvironmentDto> envsDto = new ArrayList<EnvironmentDto>();
		for (int i = 0; i < env.size(); i++) {
			EnvironmentDto envDto = new EnvironmentDto();

			if (env.get(i).getName() != null)
				envDto.setName(env.get(i).getName());

			if (env.get(i).getDescription() != null)
				envDto.setDescription(env.get(i).getDescription());

			if (env.get(i).getTiers() != null)
				envDto.setTierDtos(convertToTierDtos(env.get(i).getTiers()));

			envsDto.add(envDto);

		}
		return envsDto;
	}

	public void insert(String org, EnvironmentDto environmentDto) {
		ClaudiaData claudiaData = new ClaudiaData(org, null, null);

		if (systemPropertiesProvider.getProperty(
				SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
			//claudiaData.setUser(getCredentials());
		}
		try {
			environmentManager.load(environmentDto.getName());
			throw new WebApplicationException(new AlreadyExistEntityException(
					"The enviornment " + environmentDto.getName()
							+ " already exists"), 500);

		} catch (EntityNotFoundException e1) {
			Environment environment = new Environment();
			environment.setName(environmentDto.getName());
			environment.setDescription(environmentDto.getDescription());

			environment.setOrg(org);

			if (environmentDto.getTierDtos() != null)
				environment.setTiers(convertToTiers(environmentDto
						.getTierDtos()));
			try {
				environmentManager.create(claudiaData, environment);
			} catch (InvalidEnvironmentRequestException e) {
				// TODO Auto-generated catch block
				throw new WebApplicationException(e, 500);
			}
		}

	}

	public EnvironmentDto load(String org, String name)
			throws EnvironmentInstanceNotFoundException {
		try {
			Environment envInstance = environmentManager.load(name);
			EnvironmentDto envDto = convertToDto(envInstance);

			return envDto;
		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(e, 404);
		}
	}

	private EnvironmentDto convertToDto(Environment envInstance) {
		EnvironmentDto envInstanceDto = new EnvironmentDto();

		if (envInstance.getName() != null)
			envInstanceDto.setName(envInstance.getName());
		if (envInstance.getDescription() != null)
			envInstanceDto.setDescription(envInstance.getDescription());

		if (envInstance.getTiers() != null)
			envInstanceDto
					.setTierDtos(convertToTierDtos(envInstance.getTiers()));

		return envInstanceDto;
	}

	/**
	 * Convert a list of tierDtos to a list of Tiers
	 * 
	 * @return
	 */
	private List<Tier> convertToTiers(List<TierDto> tierDtos) {
		List<Tier> tiers = new ArrayList<Tier>();
		for (int i = 0; i < tierDtos.size(); i++) {
			Tier tier = tierDtos.get(i).fromDto();

			tiers.add(tier);
		}
		return tiers;
	}

	/**
	 * Convert a list of tierDtos to a list of Tiers
	 * 
	 * @return
	 */
	private List<TierDto> convertToTierDtos(List<Tier> tiers) {
		List<TierDto> tierDtos = new ArrayList<TierDto>();
		for (int i = 0; i < tiers.size(); i++) {
			TierDto tierDto = tiers.get(i).toDto();

			tierDtos.add(tierDto);
		}
		return tierDtos;
	}

	/**
	 * Method to convert Tier to TierDto
	 * 
	 * @param tier
	 * @return
	 */
	/*
	 * private Tier convertToTier(TierDto tierDto) {
	 * 
	 * List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
	 * Tier tier = new Tier();
	 * 
	 * if (tierDto.getName() != null) tier.setName(tierDto.getName()); if
	 * (tierDto.getInitialNumberNnstances() != null)
	 * tier.setInitial_number_instances(tierDto.getInitialNumberNnstances()); if
	 * (tierDto.getMaximum_number_instances() != null)
	 * tier.setMaximum_number_instances(tierDto.getMaximum_number_instances());
	 * if (tierDto.getMinimum_number_instances() != null)
	 * tier.setMinimum_number_instances(tierDto.getMinimum_number_instances());
	 * 
	 * for (int i=0; i < tierDto.getProductReleaseDtos().size(); i++) {
	 * 
	 * ProductRelease pRelease = new ProductRelease (); ProductReleaseDto
	 * pReleaseDto = tierDto.getProductReleaseDtos().get(i);
	 * 
	 * pRelease.setProduct(pReleaseDto.getProductName());
	 * pRelease.setVersion(pReleaseDto.getVersion());
	 * 
	 * if (pReleaseDto.getProductDescription()!= null)
	 * pRelease.setProduct(pReleaseDto.getProductDescription());
	 * 
	 * productReleases.add(pRelease); }
	 * 
	 * tier.setProductReleases(productReleases);
	 * 
	 * return tier; }
	 */

	/**
	 * Method to convert Tier to TierDto
	 * 
	 * @param tier
	 * @return
	 */
	/*
	 * private TierDto convertToTierDto(Tier tier) {
	 * 
	 * List<ProductReleaseDto> productReleasesDto = new
	 * ArrayList<ProductReleaseDto>(); TierDto tierDto = new TierDto();
	 * 
	 * 
	 * if (tier.getName() != null) { tierDto.setName(tier.getName()); } if
	 * (tier.getInitialNumberNnstances() != null) {
	 * tierDto.setInitial_number_instances(tier.getInitialNumberNnstances()); }
	 * if (tier.getMaximum_number_instances() != null) {
	 * tierDto.setMaximum_number_instances(tier.getMaximum_number_instances());
	 * } if (tier.getMinimum_number_instances() != null) {
	 * tierDto.setMinimum_number_instances(tier.getMinimum_number_instances());
	 * } if (tier.getIcono() != null) { tierDto.setIcono(tier.getIcono()); }
	 * 
	 * if (tier.getImage() != null) { tierDto.setImage(tier.getImage()); } if
	 * (tier.getFlavour() != null) { tierDto.setFlavour(tier.getFlavour()); } if
	 * (tier.getFloatingip() != null) {
	 * tierDto.setFloatingip(tier.getFloatingip()); } if (tier.getKeypair() !=
	 * null) { tierDto.setKeypair(tier.getKeypair()); } if
	 * (tier.getSecurity_group() != null) {
	 * tierDto.setSecurity_group(tier.getSecurity_group()); }
	 * 
	 * for (int i=0; i < tier.getProductReleases().size(); i++) {
	 * 
	 * ProductReleaseDto pReleaseDto = new ProductReleaseDto (); ProductRelease
	 * pRelease = tier.getProductReleases().get(i);
	 * 
	 * pReleaseDto.setProductName(pRelease.getProduct());
	 * pReleaseDto.setVersion(pRelease.getVersion());
	 * 
	 * if (pRelease.getDescription()!= null)
	 * pReleaseDto.setProductDescription(pRelease.getDescription());
	 * 
	 * productReleasesDto.add(pReleaseDto); }
	 * 
	 * tierDto.setProductReleaseDtos(productReleasesDto); return tierDto; }
	 */

	public void setEnvironmentManager(EnvironmentManager environmentManager) {
		this.environmentManager = environmentManager;
	}

	/**
	 * @param systemPropertiesProvider
	 *            the systemPropertiesProvider to set
	 */
	public void setSystemPropertiesProvider(
			SystemPropertiesProvider systemPropertiesProvider) {
		this.systemPropertiesProvider = systemPropertiesProvider;
	}

	public PaasManagerUser getCredentials() {
		if (systemPropertiesProvider.getProperty(
				SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE"))
			return (PaasManagerUser) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
		else
			return null;
	}
}
