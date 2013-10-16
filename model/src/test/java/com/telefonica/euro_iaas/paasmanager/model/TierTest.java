package com.telefonica.euro_iaas.paasmanager.model;

import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import junit.framework.TestCase;
import org.junit.Test;

public class TierTest extends TestCase {

    @Test
    public void testCreateEnvironment() throws Exception {

        Integer minimum = new Integer(1);
        Integer initial = new Integer(2);
        Integer maximum = new Integer(1);

        Tier tier = new Tier("tier", maximum, minimum, initial, null, "2", "image", "icono", "keypair", "yes", null);

        SecurityGroup securityGroup = new SecurityGroup("nanme", "description");
        securityGroup.addRule(new Rule("ipProtocol", "fromPort", "toPort", "sourceGroup", "cidr"));
        tier.setSecurityGroup(securityGroup);

        TierDto tier2 = tier.toDto();
        Tier tier3 = tier2.fromDto();

        assertEquals(tier.getSecurityGroup().getRules().size(), 1);
        assertEquals(tier.getInitialNumberInstances(), initial);
        assertEquals(tier.getMaximumNumberInstances(), maximum);
        assertEquals(tier.getMinimumNumberInstances(), minimum);
        assertEquals(tier3.equals(tier), true);

    }

}
