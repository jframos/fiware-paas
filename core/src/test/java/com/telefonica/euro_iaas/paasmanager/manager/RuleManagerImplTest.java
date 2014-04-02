/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.manager;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.claudia.FirewallingClient;
import com.telefonica.euro_iaas.paasmanager.dao.RuleDao;
import com.telefonica.euro_iaas.paasmanager.manager.impl.RuleManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.Rule;

/**
 * @author jesus.movilla
 */
public class RuleManagerImplTest {

    private RuleManagerImpl ruleManager;
    private RuleDao ruleDao;
    private FirewallingClient firewallingClient = null;

    @Before
    public void setUp() throws Exception {

        ruleManager = new RuleManagerImpl();
        ruleDao = mock(RuleDao.class);
        ruleManager.setRuleDao(ruleDao);
        firewallingClient = mock(FirewallingClient.class);
        ruleManager.setFirewallingClient(firewallingClient);

    }

    @Test
    public void testCreteRule() throws Exception {

        Rule rule = new Rule("TCP", "8080", "8080", "", "0.0.0.0/0");
        String region = "region";
        String token = "1234567689";
        String vdc = "6c23123kdn";

        Mockito.doThrow(new EntityNotFoundException(Rule.class, "test", rule)).when(ruleDao).load(any(String.class));

        when(firewallingClient.deployRule(eq(region), eq(token), eq(vdc), any(Rule.class))).thenReturn("Id");
        when(ruleDao.create(any(Rule.class))).thenReturn(rule);
        Rule rule2 = ruleManager.create(region, token, vdc, rule);

    }

    @Test
    public void testDeleteRule() throws Exception {

        Rule rule = new Rule("TCP", "8080", "8080", "", "0.0.0.0/0");
        String region = "region";
        String token = "1234567689";
        String vdc = "6c23123kdn";

        Mockito.doNothing().doThrow(new RuntimeException()).when(firewallingClient)
                .destroyRule(eq(region), eq(token), eq(vdc), any(Rule.class));
        when(ruleDao.create(any(Rule.class))).thenReturn(rule);
        ruleManager.destroy(region, token, vdc, rule);

    }

}
