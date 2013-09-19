package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierSearchCriteria;

/**
 * default Environment implementation
 * 
 * @author Henar Muñoz
 * 
 */
@Path("/catalog/environment/{environment}/tier")
@Component
@Scope("request")
public class TierResourceImpl implements TierResource {

	@InjectParam("tierManager")
	private TierManager tierManager;
	@InjectParam("environmentManager")
	private EnvironmentManager environmentManager;

	public void delete(String envName, String tierName)
			throws EntityNotFoundException {

		try {

			Tier tier = tierManager.load(tierName);

			Environment environment = environmentManager.load(envName);
			environment.deleteTier(tier);
			environmentManager.update(environment);

			tierManager.delete(tier);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new WebApplicationException(e, 500);
		}

	}

	public List<TierDto> findAll(Integer page, Integer pageSize,
			String orderBy, String orderType, String environment) {
		TierSearchCriteria criteria = new TierSearchCriteria();
		Environment env = null;
		try {
			env = environmentManager.load(environment);

		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(e, 404);
		}

		criteria.setEnvironment(env);

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

		try {

			List<TierDto> tierDto = new ArrayList<TierDto>();
			List<Tier> tiers = tierManager.findByCriteria(criteria);

			for (Tier tier : tiers) {
				tierDto.add(convertToDto(tier));
			}
			return tierDto;

		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(e, 404);
		}

	}

	public void insert(String environmentName, TierDto tierDto) {
		// Falta validar que existe un name, environmetType y unos Tiers
		// validator.validateInsert(multiPart);
		Tier tier = convertFromDto(tierDto);

		try {
			Environment environment = environmentManager.load(environmentName);
			Tier newTier = tierManager.create(tier);
			environment.addTier(newTier);
			environmentManager.update(environment);
		} catch (InvalidEntityException e) {
			throw new WebApplicationException(e, 500);
		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(e, 500);
		}

	}

	public TierDto load(String environment, String name)
			throws EntityNotFoundException {
		try {
			Tier tier = tierManager.load(name);

			TierDto tierDto = new TierDto();
			
			return convertToDto(tier);

		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(e, 404);
		}
	}

	private TierDto convertToDto(Tier tier) {
		
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
			
		/*	if (pRelease.getAttributes()!= null)
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
	
	private Tier convertFromDto(TierDto tierDto) {
		
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
				pRelease.setDescription(pReleaseDto.getProductDescription());
			
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
}
