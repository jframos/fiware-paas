/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia;


import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import com.telefonica.euro_iaas.paasmanager.claudia.impl.ClaudiaClientOpenStackImpl;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.RouterInstance;
import com.telefonica.euro_iaas.paasmanager.model.SubNetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.util.OpenStackUtil;


/**
 * @author jesus.movilla
 */
public class ClaudiaClientOpenStackImplTest {

    private Tier tier;
    private ClaudiaData claudiaData;
    private OpenStackUtil openStackUtil;
    private NetworkClient networkClient;
    private ClaudiaClientOpenStackImpl claudiaClientOpenStack;

    @Before
    public void setUp() throws Exception {
        String expectedNetworks = "<?xml version='1.0' encoding='UTF-8'?>\n"
                + "<networks xmlns=\"http://openstack.org/quantum/api/v2.0\""
                + " xmlns:provider=\"http://docs.openstack.org/ext/provider/api/v1.0\""
                + " xmlns:quantum=\"http://openstack.org/quantum/api/v2.0\" "
                + "xmlns:router=\"http://docs.openstack.org/ext/quantum/router/api/v1.0\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<network><status>ACTIVE</status><subnets><subnet>e7118ac1-4aea-49f3-8ebf-383170453fd9</subnet></subnets>"
                + "<name>test</name><provider:physical_network xsi:nil=\"true\" />"
                + "<admin_state_up quantum:type=\"bool\">True</admin_state_up>"
                + "<tenant_id>08bed031f6c54c9d9b35b42aa06b51c0</tenant_id>"
                + "<provider:network_type>gre</provider:network_type>"
                + "<router:external quantum:type=\"bool\">False</router:external>"
                + "<shared quantum:type=\"bool\">False</shared><id>7a2ec35e-08ce-42bb-a4f4-cb6d775db171</id>"
                + "<provider:segmentation_id quantum:type=\"long\">1</provider:segmentation_id></network></networks>";

        String expectedSubnet = "{\"subnet\": {\"name\": \"\", \"enable_dhcp\": true,"
                + " \"network_id\": \"42699a17-2367-41bc-8155-e58b41afa361\","
                + " \"tenant_id\": \"67c979f51c5b4e89b85c1f876bdffe31\", \"dns_nameservers\": [],"
                + " \"allocation_pools\": [{\"start\": \"10.0.3.20\", \"end\": \"10.0.3.150\"}],"
                + " \"host_routes\": [], \"ip_version\": 4, \"gateway_ip\": \"10.0.3.1\","
                + " \"cidr\": \"10.0.3.0/24\", \"id\": \"e8fd2234-447f-4f2b-b340-dbe701304b00\"}}";
        String expectedRouter = "{\"router\": {\"status\": \"ACTIVE\", \"external_gateway_info\":"
                + " {\"network_id\": \"b5be6aa7-b27e-493d-a139-75e78156950f\"}, \"name\": \"another_router\","
                + " \"admin_state_up\": true, \"tenant_id\": \"67c979f51c5b4e89b85c1f876bdffe31\","
                + " \"id\": \"08b0bd71-4fc4-4192-af2c-539ad4e02d2f\"}}";
        String expectedNetwork = "{\"network\": {\"status\": \"ACTIVE\", \"subnets\": [], \"name\": \"network-demo\","
                + " \"router:external\": false, \"tenant_id\": \"67c979f51c5b4e89b85c1f876bdffe31\", \"admin_state_up\": false,"
                + " \"shared\": false, \"id\": \"d8023576-7743-4f98-8089-a1ccc60b5ec2\"}}";
        PaasManagerUser user = new PaasManagerUser("username", "myToken", new HashSet<GrantedAuthority>());
        user.setTenantName("FIWARE");
        claudiaData = new ClaudiaData("vdc", "org", "service");
        claudiaData.setUser(user);

        tier = new Tier();
        tier.setFlavour("2");
        tier.setImage("44dcdba3-a75d-46a3-b209-5e9035d2435e");

        tier.setName("prueba");
        tier.setKeypair("jesusmovilla");
        
        claudiaClientOpenStack  = new ClaudiaClientOpenStackImpl ();

        openStackUtil = mock(OpenStackUtil.class);
        networkClient = mock (NetworkClient.class);
        claudiaClientOpenStack.setNetworkClient(networkClient);
        claudiaClientOpenStack.setOpenStackUtil(openStackUtil);


        when(openStackUtil.createServer(any(String.class), anyString(), anyString(), anyString())).thenReturn(
                "response");
        when(openStackUtil.createNetwork(any(String.class), anyString(), anyString(), anyString()))
                .thenReturn(expectedNetwork);
        when(openStackUtil.createSubNet(any(SubNetworkInstance.class), anyString(), anyString(), anyString()))
                .thenReturn(expectedSubnet);
        when(openStackUtil.createRouter(any(RouterInstance.class), anyString(), anyString(), anyString())).thenReturn(
                expectedRouter);
        when(
                openStackUtil.addRouterInterface(any(String.class), any(String.class), anyString(), anyString(),
                        anyString())).thenReturn("OK");
        when(openStackUtil.getNetworks(anyString(), anyString(), anyString())).thenReturn(expectedNetworks);
    }

    @Test
    public void testDeployVMEssex() throws Exception {

        
        TierInstance tierInstance = new TierInstance();
        tierInstance.setTier(tier);
        VM vm = new VM();
        claudiaClientOpenStack.deployVM(claudiaData, tierInstance, 1, vm);
        verify(openStackUtil).createServer(any(String.class), anyString(), anyString(), anyString());

    }
    
    @Test
    public void testDeployVMEGrizzlyNetwork() throws Exception {

        TierInstance tierInstance = new TierInstance();
        Network network = new Network("NETWORK");
        tier.addNetwork(network);
        tierInstance.setTier(tier);
        tierInstance.addNetworkInstance(network.toNetworkInstance());
        
        VM vm = new VM();
        claudiaClientOpenStack.deployVM(claudiaData, tierInstance, 1, vm);
        verify(openStackUtil).createServer(any(String.class), any(String.class), any(String.class), any(String.class));

    }
    
    @Test
    public void testDeployVMEGrizzlyNotNetwork() throws Exception {

        TierInstance tierInstance = new TierInstance();
        tierInstance.setTier(tier);
        
        Network network = new Network("NETWORK");
        NetworkInstance netInst = network.toNetworkInstance();
        netInst.setShared(false);
        netInst.setTenantId(claudiaData.getVdc());
        List<NetworkInstance> networkInstances = new ArrayList<NetworkInstance> ();
        networkInstances.add(netInst);

        VM vm = new VM();
        when(networkClient.loadAllNetwork(any(ClaudiaData.class), any(String.class))).thenReturn(networkInstances);
        claudiaClientOpenStack.deployVM(claudiaData, tierInstance, 1, vm);
        
        verify(openStackUtil).createServer(any(String.class), any(String.class), any(String.class), any(String.class));

    }
    
    
    @Test
    public void testDeployVMEGrizzlyNotNetwork2() throws Exception {

        TierInstance tierInstance = new TierInstance();
        tierInstance.setTier(tier);
        
        Network network = new Network("NETWORK");
        NetworkInstance netInst = network.toNetworkInstance();
        netInst.setShared(true);
        List<NetworkInstance> networkInstances = new ArrayList<NetworkInstance> ();
        networkInstances.add(netInst);

        VM vm = new VM();
        NetworkInstance netInst2 = network.toNetworkInstance();
        netInst2.setShared(false);
        netInst2.setDefaultNet(true);
        when(networkClient.loadAllNetwork(any(ClaudiaData.class), any(String.class))).thenReturn(networkInstances);
        when(networkClient.deployDefaultNetwork(any(ClaudiaData.class), any(String.class))).thenReturn(netInst2);
        
        claudiaClientOpenStack.deployVM(claudiaData, tierInstance, 1, vm);
        assertEquals(tierInstance.getNetworkInstances().size(), 1);
        verify(openStackUtil).createServer(any(String.class), any(String.class), any(String.class), any(String.class));

    }

}
