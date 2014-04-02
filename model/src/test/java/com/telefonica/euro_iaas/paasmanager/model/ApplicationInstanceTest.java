/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ArtifactDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstancePDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for environmetn instances.
 * 
 * @author henar
 */
public class ApplicationInstanceTest extends TestCase {
    
    public static String ARTIFACT_NAME ="ARTIFACT_NAME";
    public static String ARTIFACT_PATH ="ARTIFACT_PATH";
    public static String APP_NAME ="APP_NAME";
    public static String APP_VERSION="APP_VERSION";
    public static String PRODUCT_NAME="PRODUCT_NAME";
    public static String PRODUCT_VERSION="PRODUCT_VERSION";

    private EnvironmentInstance envIns = null;
    private Environment envResult;

    @Override
    @Before
    public void setUp() throws Exception {

        ProductRelease productRelease = new ProductRelease("product", "2.0");
        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        productReleases.add(productRelease);

        Tier tier = new Tier();
        tier.setInitialNumberInstances(new Integer(1));
        tier.setMaximumNumberInstances(new Integer(5));
        tier.setMinimumNumberInstances(new Integer(1));
        tier.setName("tierName");
        tier.setProductReleases(productReleases);
        tier.setFlavour("3");
        tier.setFloatingip("true");
        tier.setImage("image");

        Tier tier2 = new Tier("tierName2", new Integer(1), new Integer(1), new Integer(1), productReleases, "2",
                "image", "icone");

        Set<Tier> tiers = new HashSet<Tier>();
        tiers.add(tier);
        tiers.add(tier2);

        envResult = new Environment();
        envResult = new Environment();
        envResult.setName("environemntName");
        envResult.setTiers(tiers);

        envIns = new EnvironmentInstance("blue", "description");

        envIns.setEnvironment(envResult);

        VM vm = new VM("fqn", "ip", "hostname", "domain");
        ProductInstance productInstance = new ProductInstance();
        productInstance.setName("produInst");
        productInstance.setProductRelease(productRelease);
        TierInstance tierInstance = new TierInstance(tier, "ovf", "tierInstance", vm);
        List<ProductInstance> lProductInstance = new ArrayList<ProductInstance>();
        lProductInstance.add(productInstance);
        tierInstance.setProductInstances(lProductInstance);
        List<TierInstance> tieInstances = new ArrayList<TierInstance>();
        tieInstances.add(tierInstance);

        envIns.setTierInstances(tieInstances);
    }

    @Test
    public void testCreateArtifact() throws Exception {
        ProductRelease productRelease = new ProductRelease (PRODUCT_NAME, PRODUCT_VERSION);
        Artifact artifact = new Artifact (ARTIFACT_NAME, ARTIFACT_PATH, productRelease);
        

        assertEquals(artifact.getName(), ARTIFACT_NAME);
        assertEquals(artifact.getPath(), ARTIFACT_PATH);
        assertEquals(artifact.getProductRelease().getProduct(), PRODUCT_NAME);
        assertEquals(artifact.getProductRelease().getVersion(), PRODUCT_VERSION);
    }
    
    @Test
    public void testConvertArtifactFromDto() throws Exception {
        ProductRelease productRelease = new ProductRelease (PRODUCT_NAME, PRODUCT_VERSION);
        ArtifactDto artifactDto = new ArtifactDto (ARTIFACT_NAME, ARTIFACT_PATH, productRelease.toDto());
        Artifact artifact = artifactDto.fromDto();
        

        assertEquals(artifact.getName(), ARTIFACT_NAME);
        assertEquals(artifact.getPath(), ARTIFACT_PATH);
        assertEquals(artifact.getProductRelease().getProduct(), PRODUCT_NAME);
        assertEquals(artifact.getProductRelease().getVersion(), PRODUCT_VERSION);
    }
    
    @Test
    public void testConvertApplicationReleaseFromDto() throws Exception {
        ProductRelease productRelease = new ProductRelease (PRODUCT_NAME, PRODUCT_VERSION);
        ArtifactDto artifactDto = new ArtifactDto (ARTIFACT_NAME, ARTIFACT_PATH, productRelease.toDto());
        ArrayList<ArtifactDto> artifactsDto = new ArrayList<ArtifactDto> ();
        artifactsDto.add(artifactDto);

        ApplicationReleaseDto applicationReleaseDto = new ApplicationReleaseDto (APP_NAME, APP_VERSION, artifactsDto);
        ApplicationRelease appRelease = applicationReleaseDto.fromDto();
        Artifact artifact = appRelease.getArtifacts().get(0);
        
        assertEquals(appRelease.getName(), APP_NAME);
        assertEquals(appRelease.getVersion(), APP_VERSION);
        assertEquals(appRelease.getArtifacts().size(), 1);
        assertEquals(artifact.getName(), ARTIFACT_NAME);
        assertEquals(artifact.getPath(), ARTIFACT_PATH);
        assertEquals(artifact.getProductRelease().getProduct(), PRODUCT_NAME);
        assertEquals(artifact.getProductRelease().getVersion(), PRODUCT_VERSION);
    }

   

}
