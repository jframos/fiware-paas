/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.IPNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.NetworkNotRetrievedException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import org.springframework.security.core.GrantedAuthority;

public class OpenstackDummyImplTest {

    public static void main(String args[]) {

        OpenstackDummyImplTest open = new OpenstackDummyImplTest();

        String vdc = "6571e3422ad84f7d828ce2f30373b3d4";
        ClaudiaData data = new ClaudiaData("org", vdc, "service");

        Collection<? extends GrantedAuthority> dd = new ArrayList();
        PaasManagerUser manUser = new PaasManagerUser("dd", "f9f2ae5abf9e4723a89f5f2f684c74da", dd);
        manUser.setTenantId(vdc);
        data.setUser(manUser);

        open.test1(data, vdc, manUser);

    }

    public void test1(ClaudiaData data, String vdc, PaasManagerUser manUser) {

        OpenstackDummyImpl openstack = new OpenstackDummyImpl();
        Tier tierInstance = new Tier();
        VM vm = new VM();
        vm.setFqn("fqn");

        try {
            openstack.deployVM(data, tierInstance, 1, vm);

            System.out.println("IP " + openstack.obtainIPFromFqn("org", vdc, "", "", manUser));
        } catch (InfrastructureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IPNotRetrievedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClaudiaResourceNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NetworkNotRetrievedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
