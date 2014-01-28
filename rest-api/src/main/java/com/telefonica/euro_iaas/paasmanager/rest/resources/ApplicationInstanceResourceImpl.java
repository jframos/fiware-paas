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

import com.sun.jersey.api.core.InjectParam;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ApplicationInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReleaseNotFoundException;
import com.telefonica.euro_iaas.paasmanager.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.ApplicationReleaseManager;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.ApplicationInstanceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.EnvironmentInstanceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationInstance;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationType;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Task.TaskStates;
import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ArtifactDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ApplicationInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil;
import com.telefonica.euro_iaas.paasmanager.rest.validation.ApplicationInstanceResourceValidator;

/**
 * Default ApplicationInstanceResource implementation.
 * 
 * @author Jesus M. Movilla
 */
@Path("/envInst/org/{org}/vdc/{vdc}/environmentInstance/{environmentInstance}/applicationInstance")
@Component
@Scope("request")
public class ApplicationInstanceResourceImpl implements ApplicationInstanceResource {

    private static Logger log = Logger.getLogger(ApplicationInstanceResourceImpl.class.getName());

    @InjectParam("applicationInstanceAsyncManager")
    private ApplicationInstanceAsyncManager applicationInstanceAsyncManager;
    @InjectParam("applicationInstanceManager")
    private ApplicationInstanceManager applicationInstanceManager;
    @InjectParam("applicationReleaseManager")
    private ApplicationReleaseManager applicationReleaseManager;
    @InjectParam("environmentInstanceAsyncManager")
    private EnvironmentInstanceAsyncManager environmentInstanceAsyncManager;
    @InjectParam("taskManager")
    private TaskManager taskManager;
    @InjectParam("environmentInstanceManager")
    private EnvironmentInstanceManager environmentInstanceManager;

    private ApplicationInstanceResourceValidator validator;
    private ExtendedOVFUtil extendedOVFUtil;

    public Task install(String org, String vdc, String environmentInstance,
            ApplicationReleaseDto applicationReleaseDto, String callback) throws InvalidApplicationReleaseException,
            ProductReleaseNotFoundException, ApplicationInstanceNotFoundException {
        log.debug("Install application " + applicationReleaseDto.getApplicationName() + " in environment " + environmentInstance);
        Task task = null;

        validator.validateInstall(vdc, environmentInstance, applicationReleaseDto);

        ApplicationRelease applicationRelease = new ApplicationRelease();

        if (applicationReleaseDto.getApplicationName() != null) {
            applicationRelease.setName(applicationReleaseDto.getApplicationName());
        }

        if (applicationReleaseDto.getArtifactsDto() != null)
            applicationRelease.setArtifacts(this.convertToArtifact(applicationReleaseDto.getArtifactsDto()));

        if (applicationReleaseDto.getVersion() != null)
            applicationRelease.setVersion(applicationReleaseDto.getVersion());

        if (applicationReleaseDto.getApplicationType() != null) {
            ApplicationType appType = new ApplicationType();
            appType.setName(applicationReleaseDto.getApplicationType());
            applicationRelease.setApplicationType(appType);
        }

        task = createTask(
                MessageFormat.format("Deploying application {0} in environment instance {1}",
                        applicationRelease.getName(), environmentInstance), vdc);

        applicationInstanceAsyncManager.install(org, vdc, environmentInstance, applicationRelease, task, callback);

        return task;

    }

    public List<ApplicationInstance> findAll(Integer page, Integer pageSize, String orderBy, String orderType,
            List<Status> status, String vdc, String environmentInstance, String productInstanceName,
            String applicationName) {

        ApplicationInstanceSearchCriteria criteria = new ApplicationInstanceSearchCriteria();

        criteria.setVdc(vdc);
        criteria.setEnvironmentInstance(environmentInstance);
        // criteria.setApplicationName(applicationName);
        // criteria.setProductInstanceName(productInstanceName);
        // criteria.setStatus(status);

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

        List<ApplicationInstance> appInstances = applicationInstanceManager.findByCriteria(criteria);

        return appInstances;

    }

    public Task uninstall(String org, String vdc, String environmentName, String applicationName, String callback) {
        try {

            ApplicationInstance appInstance = applicationInstanceManager.load(vdc, applicationName);

            EnvironmentInstance envInstance = environmentInstanceManager.load(vdc, environmentName);

            Task task = createTask(MessageFormat.format("Uninstalling application Instance {0} ", applicationName), vdc);
            applicationInstanceAsyncManager.uninstall(org, vdc, environmentName, applicationName, task, callback);
            return task;

        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        }
    }

    /**
     * Find an applicationinstance by name and vdc
     * 
     * @param vdc
     *            , the vdc the app belongs to
     * @param name
     *            , the applicationInstanceName
     * @return the applicationInstance
     */
    public ApplicationInstance load(String vdc, String name) {
        try {
            ApplicationInstance appInstance = applicationInstanceManager.load(vdc, name);
            return appInstance;

        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(e, 404);
        }
    }

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
    public void setValidator(ApplicationInstanceResourceValidator validator) {
        this.validator = validator;
    }

    /**
     * @param extendedOVFUtil
     *            the extendedOVFUtil to set
     */
    public void setExtendedOVFUtil(ExtendedOVFUtil extendedOVFUtil) {
        this.extendedOVFUtil = extendedOVFUtil;
    }

    public List<Artifact> convertToArtifact(List<ArtifactDto> artifactsDto) {
        List<Artifact> artifacts = new ArrayList();

        for (ArtifactDto artifactDto : artifactsDto) {
            Artifact artifact = new Artifact();
            artifact.setName(artifactDto.getName());
            if (artifactDto.getPath() != null)
                artifact.setPath(artifactDto.getPath());
            if (artifactDto.getProductReleaseDto() != null) {
                artifact.setProductRelease(convertProductRelease(artifactDto.getProductReleaseDto()));
            }

        }
        return artifacts;
    }

    public ProductRelease convertProductRelease(ProductReleaseDto productReleaseDto) {
        ProductRelease productRelease = new ProductRelease();
        productRelease.setProduct(productReleaseDto.getProductName());
        productRelease.setVersion(productReleaseDto.getVersion());
        return productRelease;
    }

}
