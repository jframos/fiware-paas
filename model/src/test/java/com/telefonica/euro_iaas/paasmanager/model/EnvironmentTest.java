/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class EnvironmentTest extends TestCase {

    @Override
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testCreateEnvironment() throws Exception {

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

        Environment envResult = new Environment();
        envResult = new Environment();
        envResult.setName("environemntName");
        envResult.setTiers(tiers);

        EnvironmentDto envDto = envResult.toDto();
        Environment env2 = envDto.fromDto();
        assertEquals(env2, envResult);

        assertEquals(envResult.getTiers().size(), 2);
        assertEquals(envResult.getTiers().get(0).getName(), "tierName");

    }

}
