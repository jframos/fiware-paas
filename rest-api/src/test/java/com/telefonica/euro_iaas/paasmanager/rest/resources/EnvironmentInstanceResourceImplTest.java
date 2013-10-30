/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.telefonica.euro_iaas.paasmanager.manager.ProductReleaseManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.EnvironmentInstanceAsyncManager;
import com.telefonica.euro_iaas.paasmanager.manager.async.TaskManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Task.TaskStates;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.rest.util.ExtendedOVFUtil;
import com.telefonica.euro_iaas.paasmanager.rest.util.OVFGeneration;
import com.telefonica.euro_iaas.paasmanager.rest.util.OVFMacro;
import com.telefonica.euro_iaas.paasmanager.rest.validation.EnvironmentInstanceResourceValidator;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public class EnvironmentInstanceResourceImplTest {

    public EnvironmentInstanceResourceImpl environmentInstanceResource;
    public EnvironmentInstanceOvfResourceImpl environmentOvfInstanceResource;

    public SystemPropertiesProvider systemPropertiesProvider;
    public EnvironmentInstanceAsyncManager environmentInstanceAsyncManager;
    public EnvironmentInstanceResourceValidator environmentInstanceResourceValidator;
    public OVFGeneration ovfGeneration;
    public ExtendedOVFUtil extendedOVFUtil;
    public ProductReleaseManager productReleaseManager;
    public OVFMacro ovfMacro;
    public String vdc = "vdc";
    public String org = "org";
    public String callback = "callback";
    public Environment environment;
    public List<Tier> tiers;
    public TaskManager taskManager;
    public Task task;

    @Before
    public void setUp() throws Exception {
        environmentInstanceResource = new EnvironmentInstanceResourceImpl();
        environmentOvfInstanceResource = new EnvironmentInstanceOvfResourceImpl();
        environmentInstanceResourceValidator = mock(EnvironmentInstanceResourceValidator.class);
        extendedOVFUtil = mock(ExtendedOVFUtil.class);
        ovfGeneration = mock(OVFGeneration.class);
        productReleaseManager = mock(ProductReleaseManager.class);
        environmentInstanceAsyncManager = mock(EnvironmentInstanceAsyncManager.class);
        ovfMacro = mock(OVFMacro.class);
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        taskManager = mock(TaskManager.class);

        environmentInstanceResource.setValidator(environmentInstanceResourceValidator);
        environmentInstanceResource.setExtendedOVFUtil(extendedOVFUtil);
        environmentInstanceResource.setOvfGeneration(ovfGeneration);

        environmentInstanceResource.setSystemPropertiesProvider(systemPropertiesProvider);
        environmentInstanceResource.setEnvironmentInstanceAsyncManager(environmentInstanceAsyncManager);
        environmentInstanceResource.setTaskManager(taskManager);
        environmentOvfInstanceResource.setValidator(environmentInstanceResourceValidator);
        environmentOvfInstanceResource.setExtendedOVFUtil(extendedOVFUtil);
        environmentOvfInstanceResource.setEnvironmentInstanceAsyncManager(environmentInstanceAsyncManager);
        environmentOvfInstanceResource.setOvfMacro(ovfMacro);
        environmentOvfInstanceResource.setTaskManager(taskManager);
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("");
        Mockito.doNothing().when(environmentInstanceAsyncManager)
                .create(any(ClaudiaData.class), any(EnvironmentInstance.class), any(Task.class), any(String.class));

        // environmentOvfInstanceResource.setOvfGeneration(ovfGeneration);
        // environmentOvfInstanceResource.setProductReleaseManager(productReleaseManager);
        // environmentOvfInstanceResource.setSystemPropertiesProvider(systemPropertiesProvider);
        ProductRelease productRelease = new ProductRelease("product", "2.0");
        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        productReleases.add(productRelease);

        Tier tier = new Tier("name", new Integer(1), new Integer(1), new Integer(1), productReleases, "flavour",
                "image", "icono", "keypair", "floatingip", "payload");

        tiers = new ArrayList<Tier>();
        tiers.add(tier);
        tiers.add(tier);

        environment = new Environment();
        environment.setName("environemntName");
        environment.setDescription("description");

        environment.setTiers(tiers);

        task = new Task(TaskStates.RUNNING);
        task.setDescription("description");
        task.setVdc(vdc);
        task.setEnvironment(environment.getName());

        when(taskManager.createTask(any(Task.class))).thenReturn(task);

        ProductInstance productInstance = new ProductInstance();
        productInstance.setProductRelease(productRelease);
        // productInstance.setVm(vms.get(0));
        productInstance.setStatus(Status.INSTALLED);
        productInstance.setName("name");
        productInstance.setVdc("vdc");

        List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
        productInstances.add(productInstance);

        TierInstance tierInstance = new TierInstance();
        tierInstance.setName("nametierInstance");
        tierInstance.setTier(tier);
        tierInstance.setVdc("vdc");
        tierInstance.setStatus(Status.INSTALLED);
        tierInstance.setProductInstances(productInstances);

        List<TierInstance> tierInstances = new ArrayList<TierInstance>();
        tierInstances.add(tierInstance);

    }

    @Test
    public void testCreateEnviornmentInstance() throws Exception {

        when(extendedOVFUtil.isVirtualServicePayload(any(String.class))).thenReturn(false);
        when(extendedOVFUtil.getEnvironmentName(any(String.class))).thenReturn("servicename");
        when(ovfMacro.resolveMacros(any(Environment.class))).thenReturn(environment);
        when(extendedOVFUtil.getTiers(any(String.class), any(String.class))).thenReturn(tiers);
        Mockito.doNothing().when(environmentInstanceResourceValidator).validateCreatePayload(any(String.class));

        String payload = "ovf";

        environmentOvfInstanceResource.create(org, vdc, payload, callback);
        ClaudiaData data = new ClaudiaData(org, vdc, "servicename");
        data.setUser(null);

        EnvironmentInstance envInst = new EnvironmentInstance("servicename", "description", environment);
        verify(environmentInstanceAsyncManager, times(1)).create(any(ClaudiaData.class),
                any(EnvironmentInstance.class), any(Task.class), any(String.class));

    }

    @Test
    public void testCreateOvfEnviornmentInstance() throws Exception {

        // Given
        EnvironmentInstanceDto environmentInstanceDto = new EnvironmentInstanceDto();
        environmentInstanceDto.setDescription("description");
        environmentInstanceDto.setBlueprintName("BlueprintName");
        environmentInstanceDto.setEnvironmentDto(environment.toDto());

        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
        Authentication authentication = mock(Authentication.class);
        PaasManagerUser paasManagerUser = mock(PaasManagerUser.class);

        // When
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.CLOUD_SYSTEM)).thenReturn("FIWARE");
        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(paasManagerUser);
        environmentInstanceResource.create(org, vdc, environmentInstanceDto, callback);

        // Then
        verify(environmentInstanceAsyncManager, times(1)).create(any(ClaudiaData.class),
                any(EnvironmentInstance.class), any(Task.class), any(String.class));
        verify(systemPropertiesProvider, times(2)).getProperty(SystemPropertiesProvider.CLOUD_SYSTEM);
        verify(context).getAuthentication();
        verify(authentication).getPrincipal();

    }
}
