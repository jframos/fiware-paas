/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.environment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.QuotaExceededException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.ApplicationRelease;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ArtifactDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;
import com.telefonica.euro_iaas.paasmanager.rest.resources.ApplicationInstanceResource;
import com.telefonica.euro_iaas.paasmanager.rest.resources.EnvironmentInstanceResource;
import com.telefonica.euro_iaas.paasmanager.rest.resources.EnvironmentResource;
import com.telefonica.euro_iaas.paasmanager.rest.resources.TierInstanceResource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContextTest.xml" })
@ActiveProfiles("dummy")
public class ApplicationInstanceITest {

    @Autowired
    private EnvironmentResource environmentResource;

    @Autowired
    private EnvironmentInstanceResource environmentInstanceResource;

    @Autowired
    private ProductReleaseDao productReleaseDao;

    @Autowired
    private EnvironmentManager environmentManager;

    @Autowired
    private EnvironmentInstanceManager environmentInstanceManager;
    
    @Autowired
    private ApplicationInstanceResource applicationInstanceResource;
    
    
    

    String org = "FIWARE";
    String vdc = "6571e3422ad84f7d828ce2f30373b3d4";
    String PRODUCT_NAME = "productApp";
    String PRODUCT_VERSION = "VERSION"; 
    String ENVIRONMENT_NAME = "ENVIRONMENTNAME";
    String DESCRIPTION = "VERSION";
    String TIER_NAME = "TIERNAME";
    String BLUEPRINT_NAME = "BLUEPRINTNAME";
    public static String ARTIFACT_NAME ="ARTIFACTNAME";
    public static String ARTIFACT_PATH ="ARTIFACTPATH";
    public static String APP_NAME ="APPNAME";
    public static String APP_VERSION="APPVERSION";

    

    @Test
    public void testCreateApplicationInstance() throws Exception {

        ProductRelease product = new ProductRelease(PRODUCT_NAME, PRODUCT_VERSION,DESCRIPTION, null);
        product = productReleaseDao.create(product);


        Environment environmentBk = new Environment();
        environmentBk.setName(ENVIRONMENT_NAME);
        environmentBk.setDescription(DESCRIPTION);
        Tier tierbk = new Tier(TIER_NAME, new Integer(1), new Integer(1), new Integer(1), null);
        tierbk.addProductRelease(product);
        environmentBk.addTier(tierbk);
        environmentResource.insert(org, vdc, environmentBk.toDto());


        EnvironmentInstanceDto envInst = new EnvironmentInstanceDto();
        envInst.setBlueprintName(BLUEPRINT_NAME);
        envInst.setDescription(DESCRIPTION);
        envInst.setEnvironmentDto(environmentBk.toDto());
        List<TierInstanceDto> tierInstanceDtos = new ArrayList(2);
        TierInstanceDto tierInstanceDto = new TierInstanceDto();
        TierDto tierDto = new TierDto();
        tierDto.setInitialNumberInstances(1);
        tierInstanceDto.setTierDto(tierDto);
        tierInstanceDtos.add(tierInstanceDto);
        envInst.setTierInstances(tierInstanceDtos);

        Task task = environmentInstanceResource.create(org, vdc, envInst, "");

        Thread.sleep(5000);

        assertEquals(Task.TaskStates.RUNNING, task.getStatus());
        
        ProductRelease productRelease = new ProductRelease (PRODUCT_NAME, PRODUCT_VERSION);
        ArtifactDto artifactDto = new ArtifactDto (ARTIFACT_NAME, ARTIFACT_PATH, productRelease.toDto());
        ArrayList<ArtifactDto> artifactsDto = new ArrayList<ArtifactDto> ();
        artifactsDto.add(artifactDto);

        ApplicationReleaseDto applicationReleaseDto = new ApplicationReleaseDto (APP_NAME, APP_VERSION, artifactsDto);
        ApplicationRelease appRelease = applicationReleaseDto.fromDto();
        
        applicationInstanceResource.install(org, vdc, BLUEPRINT_NAME, applicationReleaseDto, "");

        

    }

   
}
