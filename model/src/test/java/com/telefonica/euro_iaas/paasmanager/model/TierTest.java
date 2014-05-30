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

import junit.framework.TestCase;
import net.sf.json.JSONObject;

import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;

/**
 * Test for Tier entity.
 * 
 * @author henar
 */
public class TierTest extends TestCase {

    /**
     * Test the creation of the enviornment.
     * 
     * @throws Exception
     */
    @Test
    public void testTier() throws Exception {

        Integer minimum = new Integer(1);
        Integer initial = new Integer(2);
        Integer maximum = new Integer(1);

        Tier tier = new Tier("tier", maximum, minimum, initial, null, "2", "image", "icono", "keypair", "yes", null);
        tier.setRegion("regionOne");

        SecurityGroup securityGroup = new SecurityGroup("nanme", "description");
        securityGroup.addRule(new Rule("ipProtocol", "fromPort", "toPort", "sourceGroup", "cidr"));
        tier.setSecurityGroup(securityGroup);

        TierDto tierDto = tier.toDto();
        Tier tier3 = tierDto.fromDto("vdc", "env");

        assertEquals(tier.getSecurityGroup().getRules().size(), 1);
        assertEquals(tier.getInitialNumberInstances(), initial);
        assertEquals(tier.getMaximumNumberInstances(), maximum);
        assertEquals(tier.getMinimumNumberInstances(), minimum);
        assertEquals(tier3.equals(tier), true);

    }

    @Test
    public void testTierWithNetwork() throws Exception {

        Integer minimum = new Integer(1);
        Integer initial = new Integer(2);
        Integer maximum = new Integer(1);

        Tier tier = new Tier("tier", maximum, minimum, initial, null, "2", "image", "icono", "keypair", "yes", null);

        Network net = new Network("net", "vdc", "region");
        tier.addNetwork(net);

        for (Network netOut : tier.getNetworks()) {
            assertEquals(netOut.getNetworkName(), "net");
        }

    }

    @Test
    public void testTierInstance() throws Exception {

        // Given
        Integer minimum = new Integer(1);
        Integer initial = new Integer(2);
        Integer maximum = new Integer(1);

        NetworkInstance networkInstance = new NetworkInstance("net", "vdc", "region");
        networkInstance.setIdNetwork("ID");

        Tier tier = new Tier("tier", maximum, minimum, initial, null, "2", "image", "icono", "keypair", "yes", null);

        TierInstance tierInst = new TierInstance();
        tierInst.setName("tier");
        tierInst.addNetworkInstance(networkInstance);
        tierInst.setTier(tier);
        tier.setRegion("RegionOne");

        // When
        JSONObject jsonObject = JSONObject.fromObject(tierInst.toJson(null));

        // Then
        assertEquals("keypair", JSONObject.fromObject(jsonObject.get("server")).get("key_name"));
        assertEquals("2", JSONObject.fromObject(jsonObject.get("server")).get("flavorRef"));
        assertEquals("image", JSONObject.fromObject(jsonObject.get("server")).get("imageRef"));
        assertEquals("tier", JSONObject.fromObject(jsonObject.get("server")).get("name"));
        assertEquals("RegionOne", JSONObject
                .fromObject(JSONObject.fromObject(jsonObject.get("server")).get("metadata")).get("region"));
        assertEquals("ID",
                JSONObject.fromObject(JSONObject.fromObject(jsonObject.get("server")).getJSONArray("networks").get(0))
                        .get("uuid"));

    }

}
