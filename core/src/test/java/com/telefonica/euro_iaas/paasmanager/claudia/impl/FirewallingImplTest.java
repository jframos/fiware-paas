/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Rule;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;

public class FirewallingImplTest {

    public static void main(String args[]) {

        FirewallingImplTest open = new FirewallingImplTest();

        String vdc = "6571e3422ad84f7d828ce2f30373b3d4";
        ClaudiaData data = new ClaudiaData("org", vdc, "service");

        Collection<? extends GrantedAuthority> dd = new ArrayList();
        PaasManagerUser manUser = new PaasManagerUser("dd",
                "f9f2ae5abf9e4723a89f5f2f684c74da", dd);
        manUser.setTenantId(vdc);
        data.setUser(manUser);

        // open.test1(data,vdc, manUser);
        String id = open.testCreateSecurityGroup("name4", data, vdc, manUser);
        System.out.println("ID" + id);
        String ruleId = open.testCreateRule(id, data);
        System.out.println("ruleId" + ruleId);

        open.testGetSecurityGroup(id, data);

    }

    public String testCreateRule(String idparent, ClaudiaData data) {
        OpenstackFirewallingClientImpl openstack = new OpenstackFirewallingClientImpl();
        try {

            Rule rule = new Rule("TCP", "8080", "8080", "", "0.0.0.0/0");
            rule.setIdParent(idparent);
            System.out.println(rule.toJSON());
            String ruleId = openstack.deployRule(data, rule);
            return ruleId;
        } catch (InfrastructureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String testCreateSecurityGroup(String name, ClaudiaData data,
            String vdc, PaasManagerUser manUser) {

        OpenstackFirewallingClientImpl openstack = new OpenstackFirewallingClientImpl();

        try {

            SecurityGroup sec = new SecurityGroup(name, "desc");
            String id = openstack.deploySecurityGroup(data, sec);
            return id;

        } catch (InfrastructureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void testGetSecurityGroup(String securityGroupId, ClaudiaData claudiaData) {
        OpenstackFirewallingClientImpl openstack = new OpenstackFirewallingClientImpl();
        try {
            openstack.loadSecurityGroup(claudiaData, securityGroupId);
        } catch (com.telefonica.euro_iaas.commons.dao.EntityNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
