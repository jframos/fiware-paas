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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.QuotaExceededException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.EnvironmentInstanceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Task.TaskStates;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstancePDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.EnvironmentInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil;
import com.telefonica.euro_iaas.paasmanager.rest.util.OVFGeneration;
import com.telefonica.euro_iaas.paasmanager.rest.validation.EnvironmentInstanceResourceValidator;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * Default EnvironmentInstanceResource implementation.
 * 
 * @author Jesus M. Movilla
 */
@Path("/envInst/org/{org}/vdc/{vdc}/environmentInstance")
@Component
@Scope("request")
public class EnvironmentInstanceResourceImpl implements EnvironmentInstanceResource {

    public static final int ERROR_NOT_FOUND = 404;

    private EnvironmentInstanceAsyncManager environmentInstanceAsyncManager;

    private EnvironmentInstanceManager environmentInstanceManager;

    private TaskManager taskManager;

    private EnvironmentInstanceResourceValidator validator;

    private ExtendedOVFUtil extendedOVFUtil;

    private OVFGeneration ovfGeneration;

    private SystemPropertiesProvider systemPropertiesProvider;

    private static Logger log = Logger.getLogger(EnvironmentInstanceResourceImpl.class);

    /**
     * @return
     */
    // TODO duplicated code with EnvironmentResourceImpl
    public PaasManagerUser getCredentials() {
        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
            return (PaasManagerUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } else {
            return null;
        }

    }

    /**
     * @throws InvalidEnvironmentRequestException
     * @throws AlreadyExistsEntityException
     * @throws InvalidEntityException
     * @throws EntityNotFoundException
     */
    public Task create(String org, String vdc, EnvironmentInstanceDto environmentInstanceDto, String callback)
            throws InvalidEnvironmentRequestException, EntityNotFoundException, InvalidEntityException,
            AlreadyExistsEntityException, InfrastructureException, InvalidOVFException, QuotaExceededException {

        log.warn("Desploy an environment instance " + environmentInstanceDto.getBlueprintName() + " from environmetn "
                + environmentInstanceDto.getEnvironmentDto());

        Task task = null;

        try {
            validator.validateCreate(environmentInstanceDto, systemPropertiesProvider);
        } catch (InvalidEnvironmentRequestException e) {
            log.error("The environment instance is not valid " + e.getMessage());
            throw new InvalidEntityException(e);
        }
        ClaudiaData claudiaData = new ClaudiaData(org, vdc, environmentInstanceDto.getBlueprintName());
        PaasManagerUser paasManagerUser = getCredentials();
        claudiaData.setUser(paasManagerUser);
        paasManagerUser.setTenantId(vdc);

        validator.validateQuota(claudiaData, environmentInstanceDto);

        EnvironmentInstance environmentInstance = environmentInstanceDto.fromDto();
        Environment environment = environmentInstance.getEnvironment();
        log.debug("Environment name " + environment.getName() + " " + environment.getVdc() + " " + environment.getOrg()
                + " ");
        for (Tier tier : environment.getTiers()) {
            log.debug("Tier " + tier.getName() + " image " + tier.getImage());
        }
        environmentInstance.setVdc(vdc);

        environmentInstance.setStatus(Status.INIT);
        String payload = ovfGeneration.createOvf(environmentInstanceDto);
        environmentInstance.getEnvironment().setOvf(payload);
        environmentInstance.setDescription(environmentInstanceDto.getDescription());
        environmentInstance.setBlueprintName(environmentInstanceDto.getBlueprintName());

        log.debug("EnvironmentInstance name " + environmentInstance.getBlueprintName() + " vdc "
                + environmentInstance.getVdc() + "  description " + environmentInstance.getDescription() + "  status "
                + environmentInstance.getStatus() + " environment  " + environmentInstance.getEnvironment().getName());

        if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
            claudiaData.setUser(extendedOVFUtil.getCredentials());
        }

        task = createTask(MessageFormat.format("Create environment {0}", environmentInstance.getBlueprintName()), vdc,
                environmentInstance.getBlueprintName());

        environmentInstance.setTaskId("" + task.getId());

        environmentInstanceAsyncManager.create(claudiaData, environmentInstance, task, callback);
        return task;
    }

    /**
	 * 
	 */
    public List<EnvironmentInstancePDto> findAll(Integer page, Integer pageSize, String orderBy, String orderType,
            List<Status> status, String vdc) {

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

        List<EnvironmentInstance> envInstances = environmentInstanceManager.findByCriteria(criteria);

        List<EnvironmentInstance> environmentInstances = filterEqualTiers(envInstances);

        List<EnvironmentInstancePDto> envInstancesDto = new ArrayList<EnvironmentInstancePDto>();
        for (int i = 0; i < environmentInstances.size(); i++) {
            envInstancesDto.add(environmentInstances.get(i).toPDtos());
        }
        return envInstancesDto;
    }

    /**
     * 
     */
    public EnvironmentInstancePDto load(String vdc, String name) {
        EnvironmentInstanceSearchCriteria criteria = new EnvironmentInstanceSearchCriteria();
        criteria.setVdc(vdc);
        criteria.setEnviromentName(name);

        List<EnvironmentInstance> envInstances = environmentInstanceManager.findByCriteria(criteria);

        List<EnvironmentInstance> environmentInstances = filterEqualTiers(envInstances);

        if (envInstances == null || envInstances.size() == 0) {
            throw new WebApplicationException(new EntityNotFoundException(Environment.class, "EnvironmeniInstance "
                    + name + " not found", ""), ERROR_NOT_FOUND);
        } else {
            // EnvironmentInstancePDto envInstanceDto = envInstances.get(0).toPDto();
            EnvironmentInstancePDto envInstanceDto = environmentInstances.get(0).toPDto();
            return envInstanceDto;
        }

        /*
         * try { EnvironmentInstance envInstance = environmentInstanceManager.load( vdc, name); // return
         * envInstance.toDto(); log.info(envInstance.toPDto()); return envInstance.toPDto(); } catch
         * (EntityNotFoundException e) { throw new WebApplicationException(e.getCause(), 404); }
         */
    }

    public Task destroy(String org, String vdc, String name, String callback) {
        try {

            EnvironmentInstance environmentInstance = environmentInstanceManager.loadForDelete(vdc, name);

            ClaudiaData claudiaData = new ClaudiaData(org, vdc, name);

            if (systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM).equals("FIWARE")) {
                claudiaData.setUser(extendedOVFUtil.getCredentials());
            }

            Task task = createTask(MessageFormat.format("Destroying EnvironmentInstance {0} ", name), vdc, name);
            environmentInstanceAsyncManager.destroy(claudiaData, environmentInstance, task, callback);
            return task;
        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e.getCause(), 404);
        }
    }

    /**
     * createTask
     * 
     * @param description
     * @param vdc
     * @return
     */
    private Task createTask(String description, String vdc, String environment) {
        Task task = new Task(TaskStates.RUNNING);
        task.setDescription(description);
        task.setVdc(vdc);
        task.setEnvironment(environment);
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
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

    public void setEnvironmentInstanceAsyncManager(EnvironmentInstanceAsyncManager environmentInstanceAsyncManager) {
        this.environmentInstanceAsyncManager = environmentInstanceAsyncManager;
    }

    public void setEnvironmentInstanceManager(EnvironmentInstanceManager environmentInstanceManager) {
        this.environmentInstanceManager = environmentInstanceManager;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    private List<EnvironmentInstance> filterEqualTiers(List<EnvironmentInstance> envInstances) {
        List<EnvironmentInstance> result = new ArrayList<EnvironmentInstance>();

        for (EnvironmentInstance envInstance : envInstances) {
            List<Tier> tierResult = new ArrayList<Tier>();
            EnvironmentInstance environmentInstance = new EnvironmentInstance();

            Environment environment = envInstance.getEnvironment();
            List<Tier> tiers = environment.getTiers();
            for (int i = 0; i < tiers.size(); i++) {
                Tier tier = tiers.get(i);
                List<Tier> tierAux = new ArrayList<Tier>();
                for (int j = i + 1; j < tiers.size(); j++) {
                    tierAux.add(tiers.get(j));
                }
                if (!tierAux.contains(tier))
                    tierResult.add(tier);
            }
            environment.setTiers(tierResult);
            environmentInstance.setEnvironment(environment);
            environmentInstance.setBlueprintName(envInstance.getBlueprintName());
            environmentInstance.setDescription(envInstance.getDescription());
            environmentInstance.setName(envInstance.getName());
            environmentInstance.setStatus(envInstance.getStatus());
            environmentInstance.setTaskId(envInstance.getTaskId());
            environmentInstance.setTierInstances(envInstance.getTierInstances());
            result.add(environmentInstance);
        }
        return result;
    }
}
