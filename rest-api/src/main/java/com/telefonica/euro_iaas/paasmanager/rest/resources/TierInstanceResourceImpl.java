/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidTierInstanceRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TierInstanceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Task.TaskStates;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TaskSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.TierInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil;
import com.telefonica.euro_iaas.paasmanager.rest.validation.TierInstanceResourceValidator;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * Default TierInstanceResource implementation.
 * 
 * @author bmmanso
 */
@Path("/envInst/org/{org}/vdc/{vdc}/environmentInstance/{environmentInstance}/tierInstance")
@Component
@Scope("request")
public class TierInstanceResourceImpl implements TierInstanceResource {

    private TierInstanceAsyncManager tierInstanceAsyncManager;

    private TierInstanceManager tierInstanceManager;

    private TaskManager taskManager;

    private EnvironmentInstanceManager environmentInstanceManager;

    private TierManager tierManager;

    private SystemPropertiesProvider systemPropertiesProvider;

    private TierInstanceResourceValidator validatorTierInstance;
    private ExtendedOVFUtil extendedOVFUtil;

    private static Logger log = Logger.getLogger(TierInstanceResourceImpl.class);

    /**
     * @throws EntityNotFoundException
     */
    public List<TierInstanceDto> findAll(Integer page, Integer pageSize, String orderBy, String orderType,
            List<Status> status, String vdc, String environmentInstance) {

        TierInstanceSearchCriteria criteria = new TierInstanceSearchCriteria();
        EnvironmentInstance envInstance = null;
        try {
            envInstance = environmentInstanceManager.load(vdc, environmentInstance);

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
            List<TierInstance> tierInstances = tierInstanceManager.findByCriteria(criteria);

            for (int i = 0; i < tierInstances.size(); i++) {

                tierInstancesDto.add(tierInstances.get(i).toDto());

            }
            return tierInstancesDto;

        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        }

    }

    public TierInstanceDto load(String vdc, String environmentInstanceName, String name) {
        log.debug("Loading tierinstance " + name + " from environment " + environmentInstanceName);
        TierInstanceSearchCriteria criteria = new TierInstanceSearchCriteria();
        try {
            EnvironmentInstance envInstance = environmentInstanceManager.load(vdc, environmentInstanceName);
            criteria.setEnvironmentInstance(envInstance);
            criteria.setVdc(vdc);
            TierInstance tierInstance = tierInstanceManager.load(name);
            return tierInstance.toDto();
        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        }

    }

    public Task update(String org, String vdc, String environmentInstance, TierInstanceDto tierInstanceDto,
            String callback) {
        ClaudiaData claudiaData = new ClaudiaData(org, vdc, environmentInstance);
        claudiaData.setUser(extendedOVFUtil.getCredentials());

        Task task = null;
        try {
            String tierName = tierInstanceDto.getTierDto().getName();
            String replicaNumber = tierInstanceDto.getReplicaNumber() + "";
            String tierInstanceName = getTierInstanceName(environmentInstance, tierName, replicaNumber);
            EnvironmentInstance envInstance = environmentInstanceManager.load(vdc, environmentInstance);
            validatorTierInstance.validateScaleDownTierInstance(org, vdc, envInstance, tierInstanceName);
            TierInstance tierInstance = tierInstanceManager.load(tierInstanceName);

            task = createTask(MessageFormat.format("Scale reconfigure Tier Instance {0}", tierInstance.getName()), vdc,
                    environmentInstance, tierInstanceName);
            tierInstanceAsyncManager.update(claudiaData, tierInstance, envInstance, task, callback);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new WebApplicationException(e, 500);
        }
        return task;
    }

    public Task removeTierInstance(String org, String vdc, String environmentInstance, String tierInstanceName,
            String callback) throws EntityNotFoundException, InvalidTierInstanceRequestException,
            InvalidEnvironmentRequestException {

        ClaudiaData claudiaData = new ClaudiaData(org, vdc, environmentInstance);
        claudiaData.setUser(extendedOVFUtil.getCredentials());

        Task task = null;

        try {
            EnvironmentInstance envInstance = environmentInstanceManager.load(vdc, environmentInstance);
            validatorTierInstance.validateScaleDownTierInstance(org, vdc, envInstance, tierInstanceName);
            TierInstance tierInstance = tierInstanceManager.load(tierInstanceName);

            if (!(isExecutiongTask(vdc, environmentInstance, tierInstance.getTier().getName()))) {
                if (tierInstance.getNumberReplica() > tierInstance.getTier().getMinimumNumberInstances()) {
                    task = createTask(MessageFormat.format("Scale Down Tier Instance {0}", tierInstance.getName()),
                            vdc, environmentInstance, tierInstanceName);
                    tierInstanceAsyncManager.delete(claudiaData, tierInstance, envInstance, task, callback);
                } else {
                    throw new WebApplicationException(404);
                }
            } else {
                task = createTask(
                        MessageFormat.format("Already in progress: Scale Down Tier Instance {0}",
                                tierInstance.getName()), vdc, environmentInstance, tierInstance.getName());
                task.setStatus(TaskStates.CANCELLED);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new WebApplicationException(e, 500);
        }

        return task;
    }

    public Task insert(String org, String vdc, String environmentName, TierDto tierDto, String callback)
            throws InvalidTierInstanceRequestException, InvalidEnvironmentRequestException, InvalidEntityException,
            EntityNotFoundException {

        log.debug("Insert tierinstance " + tierDto.getName() + " from environment " + environmentName);
        ClaudiaData claudiaData = new ClaudiaData(org, vdc, environmentName);
        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
            claudiaData.setUser(extendedOVFUtil.getCredentials());
        }

        Task task = null;

        EnvironmentInstance envInstance = environmentInstanceManager.load(vdc, environmentName);

        TierInstance tierInstance = new TierInstance();
        tierInstance.setVdc(vdc);
        Tier tier = tierManager.load(tierDto.getName(), vdc, envInstance.getEnvironment().getName());
        // Tier tier = tierDto.fromDto();
        /*
         * List<ProductRelease> lProductRelease = new ArrayList <ProductRelease> (); if
         * (tierDto.getProductReleaseDtos()!= null){ for (ProductReleaseDto productReleaseDto:
         * tierDto.getProductReleaseDtos()) { lProductRelease.add(productReleaseDto.fromDto()); } }
         * tier.setProductReleases(lProductRelease);
         */

        tierInstance.setTier(tier);

        // Obtain replica
        int replica = obtainReplicaNumber(envInstance, tier) + 1;
        tierInstance.setNumberReplica(replica);
        tierInstance.setName(envInstance.getBlueprintName() + "-" + tier.getName() + "-" + replica);
        log.debug("New instance " + envInstance.getName() + "-" + tier.getName() + "-" + replica);

        validatorTierInstance.validateScaleUpTierInstance(org, vdc, envInstance, tierInstance.getName());

        tierInstance.setOvf(getOVF(envInstance, tierInstance.getTier().getName()));
        tierInstance.setProductInstances(getProductFirst(envInstance, tierInstance.getTier().getName()));

        if (!(isExecutiongTask(vdc, environmentName, tierInstance.getTier().getName()))) {
            log.debug("Number instances " + tierInstance.getNumberReplica() + " "
                    + tierInstance.getTier().getMaximumNumberInstances());
            if (tierInstance.getNumberReplica() <= tierInstance.getTier().getMaximumNumberInstances()) {
                task = createTask(MessageFormat.format("Scale Up Tier Instance {0}", tierInstance.getName()), vdc,
                        environmentName, tierInstance.getName());
                log.debug("Creating tier instance " + tierInstance.getName() + " asyncronous");
                tierInstanceAsyncManager.create(claudiaData, tierInstance, envInstance, task, callback);
            } else {
                log.error("It is not possible to scale. " + "The number maximun of instances has been got");
                throw new WebApplicationException(new InvalidTierInstanceRequestException(
                        "It is not possible to scale. " + "The number maximun of instances has been got"), 500);
            }
        } else {
            log.error("Scaling action in progress. Cancelled");
            task = createTask(
                    MessageFormat.format("Already in progress: Scale Up Tier Instance {0}", tierInstance.getName()),
                    vdc, environmentName, tierInstance.getName());
            task.setStatus(TaskStates.CANCELLED);
        }

        return task;
    }

    private boolean isExecutiongTask(String vdc, String environmentInstance, String name) {

        TaskSearchCriteria criteria = new TaskSearchCriteria();
        criteria.setEnvironment(environmentInstance);
        criteria.setVdc(vdc);
        criteria.setTier(name);
        List<Task> lTask = null;
        try {
            lTask = taskManager.findByCriteria(criteria);
        } catch (Exception e) {
            return false;
        }
        boolean execution = false;
        for (Task tarea : lTask) {
            TaskStates status = tarea.getStatus();
            if ((status.equals(TaskStates.RUNNING)) || (status.equals(TaskStates.PENDING))
                    || (status.equals(TaskStates.QUEUED))) {
                execution = true;
                break;
            }
        }
        return execution;
    }

    private String getOVF(EnvironmentInstance environmentInstance, String tierName) throws EntityNotFoundException {
        TierInstance rTierInstance = getTierInstanceFromTier(environmentInstance, tierName);

        if (rTierInstance == null)
            return "";
        else
            return rTierInstance.getOvf();
        /*
         * String name = getTierInstanceName(environmentInstanceName,tierName, replicaNumber); TierInstance
         * tierInstanceFirst = tierInstanceManager.load(name); return tierInstanceFirst.getOvf();
         */

    }

    private List<ProductInstance> getProductFirst(EnvironmentInstance environmentInstance, String tierName)
            throws EntityNotFoundException {

        TierInstance tierInstanceFirst = getTierInstanceFromTier(environmentInstance, tierName);
        return tierInstanceFirst.getProductInstances();
    }

    private String getTierInstanceName(String environmentInstanceName, String tierName, String replicaNumber) {

        return (environmentInstanceName + "-" + tierName + "-" + replicaNumber);
    }

    /**
     * Setter of the validator Tier Instance
     * 
     * @param validatorTierInstance
     */
    public void setValidatorTierInstance(TierInstanceResourceValidator validatorTierInstance) {
        this.validatorTierInstance = validatorTierInstance;
    }

    /**
     * Setter of the Extended OVF Util
     * 
     * @param extendedOVFUtil
     */

    public void setExtendedOVFUtil(ExtendedOVFUtil extendedOVFUtil) {
        this.extendedOVFUtil = extendedOVFUtil;
    }

    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public void setEnvironmentInstanceManager(EnvironmentInstanceManager environmentInstanceManager) {
        this.environmentInstanceManager = environmentInstanceManager;
    }

    public void setTierInstanceAsyncManager(TierInstanceAsyncManager tierInstanceAsyncManager) {
        this.tierInstanceAsyncManager = tierInstanceAsyncManager;
    }

    public void setTierInstanceManager(TierInstanceManager tierInstanceManager) {
        this.tierInstanceManager = tierInstanceManager;
    }

    public void setTierManager(TierManager tierManager) {
        this.tierManager = tierManager;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * createTask
     * 
     * @param description
     * @param vdc
     * @return
     */
    private Task createTask(String description, String vdc, String enviromentName, String tier) {
        Task task = new Task(TaskStates.RUNNING);
        task.setDescription(description);
        task.setVdc(vdc);
        task.setEnvironment(enviromentName);
        task.setTier(tier);
        return taskManager.createTask(task);
    }

    private int obtainReplicaNumber(EnvironmentInstance environmentInstance, Tier tier) {
        int count = 0;
        if (environmentInstance.getTierInstances() != null) {
            for (TierInstance tierInstance : environmentInstance.getTierInstances()) {
                if (tierInstance.getTier().getName().equals(tier.getName())) {
                    count++;
                }
            }
        }
        return count;
    }

    private TierInstance getTierInstanceFromTier(EnvironmentInstance environmentInstance, String tierName) {
        TierInstance rTierInstance = null;
        if (environmentInstance.getTierInstances() != null) {
            for (TierInstance tierInstance : environmentInstance.getTierInstances()) {
                if (tierInstance.getTier().getName().equals(tierName)) {
                    rTierInstance = tierInstance;
                }
            }
        }
        return rTierInstance;
    }

}
