package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.EnvironmentInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;

import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

/**
 * default Environment implementation
 * 
 * @author Henar Muñoz
 * 
 */
@Path("/catalog/environment")
@Component
@Scope("request")
public class EnvironmentResourceImpl implements EnvironmentResource {

	@InjectParam("environmentManager")
	private EnvironmentManager environmentManager;

	public void delete(String envName)
			throws EnvironmentInstanceNotFoundException, InvalidEntityException {
		try {
			Environment env = environmentManager.load(envName);
			environmentManager.destroy(env);

		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(e, 404);
		}

	}

	public List<EnvironmentDto> findAll(Integer page, Integer pageSize,
			String orderBy, String orderType) {
		List<Environment> environments = environmentManager.findAll();
		List<EnvironmentDto> enviromentsDto = new ArrayList();
		for (Environment env : environments) {
			EnvironmentDto envDto = convertToDto(env);
			enviromentsDto.add(envDto);

		}
		return enviromentsDto;
	}

	public void insert(EnvironmentDto environmentDto) {
		// Falta validar que existe un name, environmetType y unos Tiers
		// validator.validateInsert(multiPart);
		Environment environment = new Environment();
		environment.setName(environmentDto.getName());
		environment.setEnvironmentType(environmentDto.getEnvironmentType());
		environment.setTiers(convertToTiers(environmentDto.getTierDtos()));
		try {
			environmentManager.create(environment);
		} catch (InvalidEnvironmentRequestException e) {
			// TODO Auto-generated catch block
			throw new WebApplicationException(e, 500);
		}

	}

	public EnvironmentDto load(String name)
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
		if (envInstance.getEnvironmentType() != null)
			envInstanceDto.setEnvironmentType(envInstance.getEnvironmentType());

		if (envInstance.getTiers() != null)
			envInstanceDto.setTierDtos(convertToTierDtos(envInstance.getTiers()));

		return envInstanceDto;
	}
	
	/**
	 * Convert a list of tierDtos to a list of Tiers
	 * @return
	 */
	private List<Tier> convertToTiers (List<TierDto> tierDtos){	
		List<Tier> tiers = new ArrayList<Tier>();	
		for (int i=0; i < tierDtos.size(); i++){
			Tier tier = convertToTier(tierDtos.get(i));
			
			tiers.add(tier);
		}
		return tiers;
	}
	
	/**
	 * Convert a list of tierDtos to a list of Tiers
	 * @return
	 */
	private List<TierDto> convertToTierDtos (List<Tier> tiers){	
		List<TierDto> tierDtos = new ArrayList<TierDto>();	
		for (int i=0; i < tiers.size(); i++){
			TierDto tierDto = convertToTierDto(tiers.get(i));
			
			tierDtos.add(tierDto);
		}
		return tierDtos;
	}
	/**
	 * Method to convert Tier to TierDto
	 * @param tier
	 * @return
	 */
	private Tier convertToTier(TierDto tierDto) {
		
		List<ProductRelease> productReleases 
			= new ArrayList<ProductRelease>(); 
		Tier tier = new Tier();
		
		if (tierDto.getName() != null)
			tier.setName(tierDto.getName());	
		if (tierDto.getInitial_number_instances() != null)
			tier.setInitial_number_instances(tierDto.getInitial_number_instances());
		if (tierDto.getMaximum_number_instances() != null)
			tier.setMaximum_number_instances(tierDto.getMaximum_number_instances());
		if (tierDto.getMinimum_number_instances() != null)
			tier.setMinimum_number_instances(tierDto.getMinimum_number_instances());
		
		for (int i=0; i < tierDto.getProductReleaseDtos().size(); i++) {
			
			ProductRelease pRelease = new ProductRelease ();
			ProductReleaseDto pReleaseDto = tierDto.getProductReleaseDtos().get(i);
			
			pRelease.setProduct(pReleaseDto.getProductName());
			pRelease.setVersion(pReleaseDto.getVersion());
			
			if (pReleaseDto.getProductDescription()!= null)
				pRelease.setProduct(pReleaseDto.getProductDescription());
			
		/*	if (pReleaseDto.getPrivateAttributes()!= null)
				pRelease.setAttributes(pReleaseDto.getPrivateAttributes());
			
			if (pReleaseDto.getSupportedOS() != null)
				pRelease.setSupportedOOSS(pReleaseDto.getSupportedOS());
			
			if (pReleaseDto.getTransitableReleases() != null)
				pRelease.setTransitableReleases(pReleaseDto.getTransitableReleases());*/
			
			productReleases.add(pRelease);
		}
		
		tier.setProductReleases(productReleases);
		
		return tier;
	}
	
	
	/**
	 * Method to convert Tier to TierDto
	 * @param tier
	 * @return
	 */
	private TierDto convertToTierDto(Tier tier) {
		
		List<ProductReleaseDto> productReleasesDto 
			= new ArrayList<ProductReleaseDto>(); 
		TierDto tierDto = new TierDto();
		
		if (tier.getName() != null)
			tierDto.setName(tier.getName());	
		if (tier.getInitial_number_instances() != null)
			tierDto.setInitial_number_instances(tier.getInitial_number_instances());
		if (tier.getMaximum_number_instances() != null)
			tierDto.setMaximum_number_instances(tier.getMaximum_number_instances());
		if (tier.getMinimum_number_instances() != null)
			tierDto.setMinimum_number_instances(tier.getMinimum_number_instances());
		
		for (int i=0; i < tier.getProductReleases().size(); i++) {
			
			ProductReleaseDto pReleaseDto = new ProductReleaseDto ();
			ProductRelease pRelease = tier.getProductReleases().get(i);
			
			pReleaseDto.setProductName(pRelease.getProduct());
			pReleaseDto.setVersion(pRelease.getVersion());
			
			if (pRelease.getDescription()!= null)
				pReleaseDto.setProductDescription(pRelease.getDescription());
			
	/*		if (pRelease.getAttributes()!= null)
				pReleaseDto.setPrivateAttributes(pRelease.getAttributes());
			
			if (pRelease.getSupportedOOSS() != null)
				pReleaseDto.setSupportedOS(pRelease.getSupportedOOSS());
			
			if (pRelease.getTransitableReleases() != null)
				pReleaseDto.setTransitableReleases(pRelease.getTransitableReleases());*/
			
			productReleasesDto.add(pReleaseDto);
		}
		
		tierDto.setProductReleaseDtos(productReleasesDto);
		return tierDto;
	}
}
