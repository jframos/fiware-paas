/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstancePDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class EnvironmentInstanceTest extends TestCase {

    EnvironmentInstance envIns = null;
    Environment envResult;

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

        List<Tier> tiers = new ArrayList<Tier>();
        tiers.add(tier);
        tiers.add(tier2);

        envResult = new Environment();
        envResult = new Environment();
        envResult.setName("environemntName");
        envResult.setEnvironmentType(new EnvironmentType("Generic", "Generic"));
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
    public void testCreateEnvironment() throws Exception {

        assertEquals(envIns.getEnvironment().getTiers().size(), 2);
        assertEquals(envIns.getEnvironment().getTiers().get(0).getName(), "tierName");
        assertEquals(envIns.getTierInstances().size(), 1);
        assertEquals(envIns.getTierInstances().get(0).getProductInstances().size(), 1);
        assertEquals(envIns.getTierInstances().get(0).getProductInstances().get(0).getName(), "produInst");
        assertEquals(envIns.getTierInstances().get(0).getProductInstances().get(0).getProductRelease().getProduct(),
                "product");

        EnvironmentInstanceDto envInstDto = envIns.toDto();
        envInstDto.setEnvironmentDto(envResult.toDto());
        EnvironmentInstance envInst2 = envInstDto.fromDto();

    }

    @Test
    public void testCreateEnvironmentToPDto() throws Exception {

        EnvironmentInstancePDto enviromentIsntanceDto = envIns.toPDto();

        assertEquals(enviromentIsntanceDto.getTiers().size(), envResult.getTiers().size());
        assertEquals(enviromentIsntanceDto.getTiers().get(0).getProductReleaseDtos().size(), 1);
        assertEquals(enviromentIsntanceDto.getTiers().get(0).getTierInstances().size(), 1);
        assertEquals(enviromentIsntanceDto.getTiers().get(0).getTierInstances().get(0).getTierInstanceName(),
                "tierInstance");
        assertEquals(enviromentIsntanceDto.getTiers().get(0).getTierInstances().get(0).getVM().getFqn(), "fqn");

    }

    @Test
    public void testCreateEnvironmentToPDtoNoVM() throws Exception {
        envIns.getTierInstances().get(0).setVM(null);

        EnvironmentInstancePDto enviromentIsntanceDto = envIns.toPDto();

        assertEquals(enviromentIsntanceDto.getTiers().size(), envResult.getTiers().size());
        assertEquals(enviromentIsntanceDto.getTiers().get(0).getProductReleaseDtos().size(), 1);
        assertEquals(enviromentIsntanceDto.getTiers().get(0).getTierInstances().size(), 1);
        assertEquals(enviromentIsntanceDto.getTiers().get(0).getTierInstances().get(0).getTierInstanceName(),
                "tierInstance");

    }

}
