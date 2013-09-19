/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

 */
package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentTypeDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.EnvironmentInstanceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.VirtualServiceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.model.Task.TaskStates;
import com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil;
import com.telefonica.euro_iaas.paasmanager.rest.util.OVFGeneration;
import com.telefonica.euro_iaas.paasmanager.rest.validation.EnvironmentInstanceResourceValidator;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * Default EnvironmentInstanceResource implementation.
 * 
 * @author Jesus M. Movilla
 * 
 */
@Path("/envInst/org/{org}/vdc/{vdc}/environmentInstance")
@Component
@Scope("request")
public class EnvironmentInstanceResourceImpl implements
		EnvironmentInstanceResource {

	@InjectParam("environmentInstanceAsyncManager")
	private EnvironmentInstanceAsyncManager environmentInstanceAsyncManager;
	@InjectParam("environmentInstanceManager")
	private EnvironmentInstanceManager environmentInstanceManager;

	@InjectParam("taskManager")
	private TaskManager taskManager;

	private EnvironmentInstanceResourceValidator validator;

	private ExtendedOVFUtil extendedOVFUtil;

	private OVFGeneration ovfGeneration;
	
	private EnvironmentTypeDao environmentTypeDao;
	
	private SystemPropertiesProvider systemPropertiesProvider;

	
	/**
	 * @throws InvalidEnvironmentRequestException
	 * @throws AlreadyExistsEntityException
	 * @throws InvalidEntityException
	 * @throws EntityNotFoundException
	 * 
	 */
	public Task create(String org, String vdc, 
			EnvironmentDto environmentInstanceDto, String callback)
			throws InvalidEnvironmentRequestException, EntityNotFoundException,
			InvalidEntityException, AlreadyExistsEntityException,
			InfrastructureException, InvalidOVFException {

		Task task = null;
		ClaudiaData claudiaData = new ClaudiaData(org, vdc);
	
		
		EnvironmentType environmentType;
			
		Environment environment = new Environment();
	//	environment.setName(extendedOVFUtil.getEnvironmentName(payload));
	//	environment.setTiers(extendedOVFUtil.getTiers(payload));
		
		environment.setName(environmentInstanceDto.getName());
		environment.setTiers(converTo(environmentInstanceDto.getTierDtos()));


		try {
			environmentType = environmentTypeDao.load("Generic");
		} catch (EntityNotFoundException e) {
			environmentType = environmentTypeDao
					.create(new EnvironmentType("Generic", "Generic"));
		}
		environment.setEnvironmentType(environmentType);
		String payload = ovfGeneration.createOvf(environmentInstanceDto);
		environment.setOvf(payload);
		//String envInstanceName = extendedOVFUtil.getEnvironmentName(payload);
		String envInstanceName = vdc + "-" + environmentInstanceDto.getName();
			
		claudiaData.setService(envInstanceName);
	/*	Collection<? extends GrantedAuthority> dd = new ArrayList () ;
		PaasManagerUser manUser  = new PaasManagerUser(vdc, "6dc359940557476fa31c62a928b73368", dd);
		manUser.setTenantId(vdc);
		claudiaData.setUser(manUser);*/
		
		if (systemPropertiesProvider.getProperty(
				SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
				claudiaData.setUser(extendedOVFUtil.getCredentials());
		}
		
		task = createTask(MessageFormat.format("Create environment {0}",
				environment.getName()), vdc);

		environmentInstanceAsyncManager.create(claudiaData, environment, 
				task, callback);
		return task;
	}


	

	/**
	 * 
	 */
	public List<EnvironmentInstanceDto> findAll(Integer page, Integer pageSize,
			String orderBy, String orderType, List<Status> status, String vdc) {

		EnvironmentInstanceSearchCriteria criteria = new EnvironmentInstanceSearchCriteria();

		criteria.setVdc(vdc);
		criteria.setStatus(status);

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

		List<EnvironmentInstance> envInstances = environmentInstanceManager
				.findByCriteria(criteria);

		List<EnvironmentInstanceDto> envInstancesDto = new ArrayList<EnvironmentInstanceDto>();
		for (int i = 0; i < envInstances.size(); i++) {
			EnvironmentInstanceDto envInstanceDto = new EnvironmentInstanceDto();

			if (envInstances.get(i).getName() != null)
				envInstanceDto.setEnvironmentInstanceName(envInstances.get(i)
						.getName());
			/*if (envInstances.get(i).getEnvironment() != null) {
				EnvironmentDto envDto = convertToDto(envInstances.get(i)
						.getEnvironment());
				envInstanceDto.setEnvironmentDto(envDto);
			}*/

			if (envInstances.get(i).getTierInstances() != null)
				envInstanceDto.setTierInstances(convertToDto(envInstances
						.get(i).getTierInstances()));

			if (envInstances.get(i).getVdc() != null)
				envInstanceDto.setVdc(envInstances.get(i).getVdc());

			if (envInstances.get(i).getPrivateAttributes() != null)
				envInstanceDto.setAttributes(envInstances.get(i)
						.getPrivateAttributes());

			/*
			 * if (envInstances.get(i).getVapp()!= null) envInstanceDto.setVapp(
			 * envInstances.get(i).getVapp());
			 */

			envInstancesDto.add(envInstanceDto);

		}
		return envInstancesDto;
	}

	/**
     * 
     */
	public EnvironmentInstanceDto load(String vdc, String name) {
		try {
			EnvironmentInstance envInstance = environmentInstanceManager.load(
					vdc, name);

			EnvironmentInstanceDto envInstanceDto = new EnvironmentInstanceDto();

			if (envInstance.getName() != null)
				envInstanceDto
						.setEnvironmentInstanceName(envInstance.getName());
			/*if (envInstance.getEnvironment() != null) {
				EnvironmentDto envDto = convertToDto(envInstance
						.getEnvironment());
				envInstanceDto.setEnvironmentDto(envDto);
			}*/

			if (envInstance.getTierInstances() != null)
				envInstanceDto.setTierInstances(convertToDto(envInstance
						.getTierInstances()));

			if (envInstance.getVdc() != null)
				envInstanceDto.setVdc(envInstance.getVdc());

			if (envInstance.getPrivateAttributes() != null)
				envInstanceDto
						.setAttributes(envInstance.getPrivateAttributes());

			return envInstanceDto;

		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(e, 404);
		}
	}

	public Task destroy(String org, String vdc, String name, String callback) {

		try {
			EnvironmentInstance environmentInstance 
				= environmentInstanceManager.loadForDelete(vdc, name);
			
			ClaudiaData claudiaData = new ClaudiaData(org,vdc,
					environmentInstance.getName());
				
			Task task = createTask(MessageFormat.format(
					"Destroying EnvironmentInstance {0} ", name), vdc);
			environmentInstanceAsyncManager.destroy(claudiaData, 
					environmentInstance, task, callback);
			return task;
		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(e, 404);
		}
	}

	/**
	 * createTask
	 * 
	 * @param description
	 * @param vdc
	 * @return
	 */
	private Task createTask(String description, String vdc) {
		Task task = new Task(TaskStates.RUNNING);
		task.setDescription(description);
		task.setVdc(vdc);
		return taskManager.createTask(task);
	}

	/**
	 * @param validator
	 *            the validator to set
	 */
	public void setValidator(EnvironmentInstanceResourceValidator validator) {
		this.validator = validator;
	}

	/**
	 * @param extendedOVFUtil
	 *            the extendedOVFUtil to set
	 */
	public void setExtendedOVFUtil(ExtendedOVFUtil extendedOVFUtil) {
		this.extendedOVFUtil = extendedOVFUtil;
	}

	/**
	 * @param ovfGeneration
	 *            the ovfGeneration to set
	 */
	public void setOvfGeneration(OVFGeneration ovfGeneration) {
		this.ovfGeneration = ovfGeneration;
	}

	/**
	 * @param environmentTypeDao
	 *            the environmentTypeDao to set
	 */
	public void setEnvironmentTypeDao(EnvironmentTypeDao environmentTypeDao) {
		this.environmentTypeDao = environmentTypeDao;
	}
	
	 /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
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

	private TierInstanceDto convertToDto(TierInstance tierInstance) {
		TierInstanceDto tierInstanceDto = new TierInstanceDto();

		if (tierInstance.getName() != null)
			tierInstanceDto.setTierInstanceName(tierInstance.getName());
		if (tierInstance.getTier() != null)
			tierInstanceDto.setTierDto(convertToTierDto(tierInstance.getTier()));

		if (tierInstance.getNumberReplica() != 0)
			tierInstanceDto.setReplicaNumber(tierInstance.getNumberReplica());

		/*if (tierInstance.getProductInstances() != null)
			tierInstanceDto.setProductInstances(tierInstance
					.getProductInstances());*/

		if (tierInstance.getPrivateAttributes() != null)
			tierInstanceDto.setAttributes(tierInstance.getPrivateAttributes());

		return tierInstanceDto;
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
	 * 
	 * @param tierInstances
	 * @return
	 */
	private List<TierInstanceDto> convertToDto(List<TierInstance> tierInstances) {
		List<TierInstanceDto> tierInstancesDto = new ArrayList<TierInstanceDto>();

		for (TierInstance tierInstance : tierInstances) {
			TierInstanceDto tierInstanceDto = convertToDto(tierInstance);
			tierInstancesDto.add(tierInstanceDto);
		}
		return tierInstancesDto;

	}
	
    private List<Tier> converTo(List<TierDto> tierDtos) {
		
		List<Tier> tiers = new ArrayList ();
		for (TierDto tierDto: tierDtos)
		{
			Tier tier = new Tier ();
			tier.setInitial_number_instances(tierDto.getInitial_number_instances());
			tier.setMaximum_number_instances(tierDto.getMaximum_number_instances());
			tier.setMinimum_number_instances(tierDto.getMinimum_number_instances());
			tier.setImage(tierDto.getImage());
			tier.setFlavour(tierDto.getFlavour());
			if (tierDto.getProductReleaseDtos() != null)
			  tier.setProductReleases(converToProductRelease(tierDto.getProductReleaseDtos()));
			tier.setName(tierDto.getName());
			tiers.add(tier);
		}
		return tiers;
	}
    
 private List<ProductRelease> converToProductRelease(List<ProductReleaseDto> produtReleaseDtos) {
		
		List<ProductRelease> produtReleases = new ArrayList ();
		for (ProductReleaseDto produtReleaseDto: produtReleaseDtos)
		{
			ProductRelease productRelease = new ProductRelease ();
			productRelease.setVersion(produtReleaseDto.getVersion());
			productRelease.setProduct(produtReleaseDto.getProductName());
			
			produtReleases.add(productRelease);
			
			
		}
		return produtReleases;
	}


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
}
