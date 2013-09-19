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
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidTierInstanceRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TierInstanceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Task.TaskStates;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil;
import com.telefonica.euro_iaas.paasmanager.rest.validation.TierInstanceResourceValidator;

/**
 * Default TierInstanceResource implementation.
 * 
 * @author bmmanso
 * 
 */
@Path("/envInst/org/{org}/vdc/{vdc}/environmentInstance/{environmentInstance}/tierInstance")
@Component
@Scope("request")
public class TierInstanceResourceImpl implements TierInstanceResource {

	@InjectParam("tierInstanceAsyncManager")
	private TierInstanceAsyncManager tierInstanceAsyncManager;
	@InjectParam("tierInstanceManager")
	private TierInstanceManager tierInstanceManager;
	@InjectParam("taskManager")
	private TaskManager taskManager;
	@InjectParam("environmentInstanceManager")
	private EnvironmentInstanceManager environmentInstanceManager;
	@InjectParam("tierManager")
	private TierManager tierManager;



	private TierInstanceResourceValidator validatorTierInstance;
	private ExtendedOVFUtil extendedOVFUtil;



	/**
	 * @throws EntityNotFoundException
	 * 
	 */
	public List<TierInstanceDto> findAll(Integer page, Integer pageSize,
			String orderBy, String orderType, List<Status> status, String vdc,
			String environmentInstance) {

		TierInstanceSearchCriteria criteria = new TierInstanceSearchCriteria();
		EnvironmentInstance envInstance = null;
		try {
			envInstance = environmentInstanceManager.load(vdc,
					environmentInstance);

		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(e, 404);
		}

		criteria.setEnvironmentInstance(envInstance);
		criteria.setVdc(vdc);

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

			List<TierInstanceDto> tierInstancesDto = new ArrayList<TierInstanceDto>();
			List<TierInstance> tierInstances = tierInstanceManager
					.findByCriteria(criteria);

			for (int i = 0; i < tierInstances.size(); i++) {

				tierInstancesDto.add(convertToDto(tierInstances.get(i)));

			}
			return tierInstancesDto;

		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(e, 404);
		}

	}

	public TierInstanceDto load(String vdc, String environmentInstanceName,
			String name) {
		TierInstanceSearchCriteria criteria = new TierInstanceSearchCriteria();
		try {
			EnvironmentInstance envInstance = environmentInstanceManager.load(
					vdc, environmentInstanceName);
			criteria.setEnvironmentInstance(envInstance);
			criteria.setVdc(vdc);
			TierInstance tierInstance = tierInstanceManager.load(name);
			return convertToDto(tierInstance);
		} catch (EntityNotFoundException e) {
			throw new WebApplicationException(e, 404);
		}

	}
	
	public Task update(String org, String vdc, String environmentInstance,
			TierInstanceDto tierInstanceDto, String callback){
		ClaudiaData claudiaData = new ClaudiaData(org,vdc,environmentInstance);
		claudiaData.setUser(extendedOVFUtil.getCredentials());
		
		Task task = null;
		try {
			String tierName = tierInstanceDto.getTierDto().getName();
			String replicaNumber = tierInstanceDto.getReplicaNumber()+"";
			String tierInstanceName = getTierInstanceName(environmentInstance, tierName, replicaNumber);
			EnvironmentInstance envInstance = environmentInstanceManager.load(
					vdc, environmentInstance);
			validatorTierInstance.validateScaleDownTierInstance(org, vdc,
					envInstance, tierInstanceName);
			TierInstance tierInstance = tierInstanceManager.load(tierInstanceName);
			claudiaData.setOrg(org);
			claudiaData.setVm(tierInstanceDto.getTierDto().getName());
			task = createTask(MessageFormat.format(
					"Scale reconfigure Tier Instance {0}", tierInstance.getName()),
					environmentInstance);
			tierInstanceAsyncManager.update(claudiaData,
					tierInstance, envInstance, task, callback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new WebApplicationException(e, 500);
		}
		return task;
	}
	public Task removeTierInstance(String org, String vdc,
			String environmentInstance, String tierInstanceName, String callback)
			throws EntityNotFoundException,
			InvalidTierInstanceRequestException,
			InvalidEnvironmentRequestException {
		
		
			ClaudiaData claudiaData = new ClaudiaData(org,vdc,environmentInstance);
			claudiaData.setUser(extendedOVFUtil.getCredentials());
			
			Task task = null;

		try {
			EnvironmentInstance envInstance = environmentInstanceManager.load(
					vdc, environmentInstance);
			
			validatorTierInstance.validateScaleDownTierInstance(org, vdc,
					envInstance, tierInstanceName);
			
			TierInstance tierInstance = tierInstanceManager
					.load(tierInstanceName);

			claudiaData.setVm(tierInstance.getTier().getName());

			
			if(tierInstance.getNumberReplica() > tierInstance.getTier().getMinimum_number_instances()){
				task = createTask(MessageFormat.format(
						"Scale Down Tier Instance {0}", tierInstance.getName()),
						environmentInstance);
				tierInstanceAsyncManager.delete(claudiaData,
						tierInstance, envInstance, task, callback);
			} else {
				throw new WebApplicationException(404);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new WebApplicationException(e, 500);
		}

		return task;
	}
	
	
	public Task insert(String org, String vdc, String environmentInstance,
			TierInstanceDto tierInstanceDto, String callback) throws
			InvalidTierInstanceRequestException, InvalidEnvironmentRequestException {
		
		ClaudiaData claudiaData = new ClaudiaData(org,vdc,environmentInstance);
		claudiaData.setUser(extendedOVFUtil.getCredentials());
		claudiaData.setVm(tierInstanceDto.getTierDto().getName());
		
		Task task = null;
		
		try {
			EnvironmentInstance envInstance = environmentInstanceManager.load(
					vdc, environmentInstance);

			TierInstance tierInstance = new TierInstance();
			tierInstance.setVdc(vdc);
			tierInstance.setTier(tierManager.load
					(tierInstanceDto.getTierDto().getName()));
			tierInstance.setNumberReplica(tierInstanceDto.getReplicaNumber());
			tierInstance.setName(envInstance.getName() + "-"
					+ tierInstanceDto.getTierDto().getName() + "-"
					+ tierInstanceDto.getReplicaNumber());
			validatorTierInstance.validateScaleUpTierInstance(org, vdc,
					envInstance, tierInstance.getName());
			tierInstance.setOvf(getOVF(envInstance.getName(),
					tierInstance.getTier().getName(), "1"));
			tierInstance.setProductInstances(getProductFirst
					(envInstance.getName(),tierInstance.getTier().getName(), "1"));

			if(tierInstance.getNumberReplica()<= tierInstance.getTier().getMaximum_number_instances()){
				task = createTask(MessageFormat.format(
						"Scale Up Tier Instance {0}", tierInstance.getName()),
						environmentInstance);
				tierInstanceAsyncManager.create(claudiaData,
						tierInstance, envInstance, task, callback);
			} else {
				throw new WebApplicationException(404);
			}
			
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			throw new WebApplicationException(e, 404);
		}
		return task;
	}
	
	

	
	private String getOVF(String environmentInstanceName, String tierName,
			String replicaNumber) throws EntityNotFoundException {
		String name = getTierInstanceName(environmentInstanceName,tierName, replicaNumber);
		TierInstance tierInstanceFirst = tierInstanceManager.load(name);
		return tierInstanceFirst.getOvf();
	}

	private  List<ProductInstance> getProductFirst(String environmentInstanceName, String tierName,
			String replicaNumber) throws EntityNotFoundException {
		String name = getTierInstanceName(environmentInstanceName,tierName, replicaNumber);
		TierInstance tierInstanceFirst = tierInstanceManager.load(name);
		return tierInstanceFirst.getProductInstances();
	}
	
	private String getTierInstanceName(String environmentInstanceName,
			String tierName, String replicaNumber) {
		
		return (environmentInstanceName + "-"+ tierName 
				+ "-" + replicaNumber);
		 
	}


	/**
	 * 
	 * Setter of the validator Tier Instance
	 * 
	 * @param validatorTierInstance
	 */
	public void setValidatorTierInstance(
			TierInstanceResourceValidator validatorTierInstance) {
		this.validatorTierInstance = validatorTierInstance;
	}
	
	/**
	 * 
	 * Setter of the Extended OVF Util
	 * 
	 * @param extendedOVFUtil
	 */

	public void setExtendedOVFUtil(
			ExtendedOVFUtil extendedOVFUtil) {
		this.extendedOVFUtil = extendedOVFUtil;
	}
	
	

	/**
	 * createTask
	 * 
	 * @param description
	 * @param vdc
	 * @return
	 */
	private Task createTask(String description, String enviromentName) {
		Task task = new Task(TaskStates.RUNNING);
		task.setDescription(description);
		task.setVdc(enviromentName);
		return taskManager.createTask(task);
	}

	private TierInstanceDto convertToDto(TierInstance tierInstance) {
		TierInstanceDto tierInstanceDto = new TierInstanceDto();

		if (tierInstance.getName() != null)
			tierInstanceDto.setTierInstanceName(tierInstance.getName());
		if (tierInstance.getTier() != null)
			tierInstanceDto.setTierDto(convertToTierDto(tierInstance.getTier()));

		if (tierInstance.getNumberReplica() != 0)
			tierInstanceDto.setReplicaNumber(tierInstance.getNumberReplica());

		if (tierInstance.getProductInstances() != null)
			tierInstanceDto.setProductInstanceDtos(convertToProductInstancesDto(
					tierInstance.getProductInstances()));

		if (tierInstance.getPrivateAttributes() != null)
			tierInstanceDto.setAttributes(tierInstance.getPrivateAttributes());

		return tierInstanceDto;
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
			
			if (pRelease.getAttributes()!= null)
				pReleaseDto.setPrivateAttributes(pRelease.getAttributes());
			
		/*	if (pRelease.getSupportedOOSS() != null)
				pReleaseDto.setSupportedOS(pRelease.getSupportedOOSS());
			
			if (pRelease.getTransitableReleases() != null)
				pReleaseDto.setTransitableReleases(pRelease.getTransitableReleases());*/
			
			productReleasesDto.add(pReleaseDto);
		}
		
		tierDto.setProductReleaseDtos(productReleasesDto);
		return tierDto;
	}
	
	/**
	 * Convert to a List of ProductReleaseDto to a list of ProductRelease
	 * @param pReleases
	 * @return
	 */
	private List<ProductInstanceDto> convertToProductInstancesDto (
		List<ProductInstance> pInstances) {
		
		List<ProductInstanceDto> productInstancesDto 
			= new ArrayList<ProductInstanceDto>(); 
	
		for (int i=0; i < pInstances.size(); i++) {
			
			ProductInstanceDto pInstanceDto = new ProductInstanceDto ();
			ProductInstance pInstance = pInstances.get(i);
			
			pInstanceDto.setName(pInstance.getName());
			
			if (pInstance.getProductRelease()!= null)
				pInstanceDto.setProductReleaseDto(
					convertToProductReleaseDto(pInstance.getProductRelease()));
			
			if (pInstance.getPrivateAttributes()!= null)
				pInstanceDto.setAttributes(pInstance.getPrivateAttributes());
			
			productInstancesDto.add(pInstanceDto);
		}
		
		return productInstancesDto;
	}

	/**
	 * Converto To ProductReleaseDto
	 * @param pRelease
	 * @return
	 */
	private ProductReleaseDto convertToProductReleaseDto (ProductRelease pRelease) {
	
		ProductReleaseDto pReleaseDto = new ProductReleaseDto ();
		
		pReleaseDto.setProductName(pRelease.getProduct());
		pReleaseDto.setVersion(pRelease.getVersion());
		
		if (pRelease.getDescription()!= null)
			pReleaseDto.setProductDescription(pRelease.getDescription());
		
		if (pRelease.getAttributes()!= null)
			pReleaseDto.setPrivateAttributes(pRelease.getAttributes());
		
/*		if (pRelease.getSupportedOOSS() != null)
			pReleaseDto.setSupportedOS(pRelease.getSupportedOOSS());
		
		if (pRelease.getTransitableReleases() != null)
			pReleaseDto.setTransitableReleases(pRelease.getTransitableReleases());*/
		
		return pReleaseDto;
	}
}
