/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import junit.framework.TestCase;
import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.Test;

/**
 * @author jesus.movilla
 */
public class SecurityGroupTest extends TestCase {

    private SecurityGroup securityGroup;
    private JSONObject secGroupJson;

    @Before
    public void setUp() throws Exception {

        securityGroup = new SecurityGroup();
        String secGroupString = "{\"rules\":["
                + "{\"from_port\":22,\"group\":{},\"ip_protocol\":\"tcp\",\"to_port\":22,\"parent_group_id\":6,\"ip_range\":{\"cidr\":\"0.0.0.0/0\"},\"id\":10},"
                + "{\"from_port\":8080,\"group\":{},\"ip_protocol\":\"tcp\",\"to_port\":8080,\"parent_group_id\":6,\"ip_range\":{\"cidr\":\"0.0.0.0/0\"},\"id\":11}]"
                + ",\"tenant_id\":\"ebe6d9ec7b024361b7a3882c65a57dda\"," + "\"id\":6," + "\"name\":\"namedefault\","
                + "\"description\":\"default\"" + "} ";

        secGroupJson = JSONObject.fromObject(secGroupString);
    }

    @Test
    public void testFromJson() throws Exception {
        securityGroup.fromJson(secGroupJson);
        assertEquals(securityGroup.getDescription(), "default");
        assertEquals(securityGroup.getIdSecurityGroup(), "6");
        assertEquals(securityGroup.getName(), "namedefault");
        assertEquals(securityGroup.getRules().size(), 2);
        assertEquals(securityGroup.getRules().get(0).getFromPort(), "22");
        assertEquals(securityGroup.getRules().get(1).getToPort(), "8080");
    }
    
    @Test
    public void equalRules() throws Exception {
        Rule rule = new Rule("TCP", "80", "80", "", "0.0.0.0/0");
        Rule rule2 = new Rule("TCP", "80", "80", "", "0.0.0.0/0");
        assertEquals(rule.equals(rule2), true);
        
        rule.setIdParent("1");
        rule.setIdParent("2");
        assertEquals(rule.equals(rule2), false);
    }
}
