/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
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

        Network net = new Network("net", "vdc");
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

        NetworkInstance networkInstance = new NetworkInstance("net", "vdc");
        networkInstance.setIdNetwork("ID");

        Tier tier = new Tier("tier", maximum, minimum, initial, null, "2", "image", "icono", "keypair", "yes", null);

        TierInstance tierInst = new TierInstance();
        tierInst.setName("tier");
        tierInst.addNetworkInstance(networkInstance);
        tierInst.setTier(tier);
        tier.setRegion("RegionOne");

        // When
        JSONObject jsonObject = JSONObject.fromObject(tierInst.toJson());

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
