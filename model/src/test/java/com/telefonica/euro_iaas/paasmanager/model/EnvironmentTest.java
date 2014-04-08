/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        Tier tier2 = new Tier("name", new Integer(1), new Integer(1), new Integer(1), productReleases, "4",
                "image2", "icone2");

        Set<Tier> tiers = new HashSet<Tier>();
        System.out.println (tiers.add(tier));
        System.out.println (tiers.add(tier2));

        Environment envResult = new Environment();
        envResult = new Environment();
        envResult.setName("environemntName");
        envResult.addTier(tier2);
        envResult.addTier(tier);
       // envResult.setTiers(tiers);

        EnvironmentDto envDto = envResult.toDto();
        Environment env2 = envDto.fromDto();
        assertEquals(env2, envResult);

        assertEquals(envResult.getTiers().size(), 2);

        

    }

}
