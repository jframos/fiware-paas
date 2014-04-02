/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;


import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.Port;
import com.telefonica.euro_iaas.paasmanager.model.RouterInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.util.OpenStackUtil;

public class OpenStackNetworkImplTest {
    OpenstackNetworkClientImpl openStackNetworkImpl = new OpenstackNetworkClientImpl();
    ClaudiaData claudiaData ;
    private OpenStackUtil openStackUtil;
    private String REGION ="region";
    @Before
    public void setUp () {
        GrantedAuthority grantedAuthority = mock(GrantedAuthority.class);
        Collection<GrantedAuthority> authorities = new HashSet();
        authorities.add(grantedAuthority);
        PaasManagerUser paasManagerUser = new PaasManagerUser("08bed031f6c54c9d9b35b42aa06b51c0", "aa", authorities);
        claudiaData = new ClaudiaData("org", "08bed031f6c54c9d9b35b42aa06b51c0", "service");
        claudiaData.setUser(paasManagerUser);
        openStackUtil = mock(OpenStackUtil.class);
        openStackNetworkImpl.setOpenStackUtil(openStackUtil);
    }

    @Test
    public void shouldListNetworks() throws OpenStackException, InfrastructureException {

        // when
        String response = "{\"networks\": [{\"status\": \"ACTIVE\", \"subnets\": [\"2b7a07f6-0b73-46a1-9327-6911c0480f49\"], \"name\": "
                + " \"dia146\", \"provider:physical_network\": null, \"admin_state_up\": true, \"tenant_id\": \"67c979f51c5b4e89b85c1f876bdffe31\", "
                + " \"provider:network_type\": \"gre\", \"router:external\": false, \"shared\": false, \"id\": \"044aecbe-3975-4318-aad2-a1232dcde47d\", "
                + " \"provider:segmentation_id\": 8}, {\"status\": \"ACTIVE\", \"subnets\": [\"e2d10e6b-33c3-400c-88d6-f905d4cd02f2\"], \"name\": \"ext-net\","
                + " \"provider:physical_network\": null, \"admin_state_up\": true, \"tenant_id\": \"08bed031f6c54c9d9b35b42aa06b51c0\", \"provider:network_type\": "
                + " \"gre\", \"router:external\": true, \"shared\": false, \"id\": \"080b5f2a-668f-45e0-be23-361c3a7d11d0\", \"provider:segmentation_id\": 1}"
                + "]}";
        when(openStackUtil.listNetworks(any(PaasManagerUser.class), anyString())).thenReturn(response);

        List<NetworkInstance> networks = openStackNetworkImpl.loadAllNetwork(claudiaData, "region");

        // then
        assertNotNull(networks);
        verify(openStackUtil).listNetworks(any(PaasManagerUser.class), anyString());
        assertEquals(2, networks.size());
        assertEquals("dia146", networks.get(0).getNetworkName());
    }
    
    @Test
    public void shouldListPortsFromNetwork() throws OpenStackException, InfrastructureException {

        // when
        String response = "{\"ports\": [ {\"status\": \"ACTIVE\",\"name\": \"\",  \"admin_state_up\": true, \"network_id\": " +
        		"\"6a609412-3f04-485c-b269-b1a9b9ecb6bf\", \"tenant_id\": \"08bed031f6c54c9d9b35b42aa06b51c0\",\"binding:vif_type\": \"ovs\","+
            "\"device_owner\": \"network:dhcp\", \"binding:capabilities\": {\"port_filter\": true}, \"mac_address\": \"fa:16:3e:54:1a:40\","+
            "\"fixed_ips\": [ {\"subnet_id\": \"601ec829-a166-4b39-9c74-166cc76f251b\", \"ip_address\": \"172.31.0.3\"}],"+
            "\"id\": \"07fd27d2-9ce1-48f3-be83-c7d2b7041a1a\", \"security_groups\": [], \"device_id\": \"dhcpfa3e6aae-2140-5176-877a-2f67684a3165-6a609412-3f04-485c-b269-b1a9b9ecb6bf\""+
            "}, {\"status\": \"ACTIVE\",\"name\": \"\",  \"admin_state_up\": true, \"network_id\": " +
                "\"6a609412-3f04-485c-b269-b1a9b9ecb6bf\", \"tenant_id\": \"08bed031f6c54c9d9b35b42aa06b51c0\",\"binding:vif_type\": \"ovs\","+
            "\"device_owner\": \"network:router_interface\", \"binding:capabilities\": {\"port_filter\": true}, \"mac_address\": \"fa:16:3e:54:1a:40\","+
            "\"fixed_ips\": [ {\"subnet_id\": \"601ec829-a166-4b39-9c74-166cc76f251b\", \"ip_address\": \"172.31.0.1\"}],"+
            "\"id\": \"07fd27d2-9ce1-48f3-be83-c7d2b7041a1a\", \"security_groups\": [], \"device_id\": \"dhcpfa3e6aae-2140-5176-877a-2f67684a3165-6a609412-3f04-485c-b269-b1a9b9ecb6bf\""+
            "}, {\"status\": \"ACTIVE\",\"name\": \"\",  \"admin_state_up\": true, \"network_id\": " +
                "\"6a609412-3f04-485c-b269-b1a9b9ecb6bf\", \"tenant_id\": \"08bed031f6c54c9d9b35b42aa06b51c0\",\"binding:vif_type\": \"ovs\","+
                "\"device_owner\": \"compute:None\", \"binding:capabilities\": {\"port_filter\": true}, \"mac_address\": \"fa:16:3e:54:1a:40\","+
                "\"fixed_ips\": [ {\"subnet_id\": \"601ec829-a166-4b39-9c74-166cc76f251b\", \"ip_address\": \"172.31.0.1\"}],"+
                "\"id\": \"07fd27d2-9ce1-48f3-be83-c7d2b7041a1a\", \"security_groups\": [], \"device_id\": \"dhcpfa3e6aae-2140-5176-877a-2f67684a3165-6a609412-3f04-485c-b269-b1a9b9ecb6bf\""+
                "}]}" ;
        System.out.print(response);
            
          

        when(openStackUtil.listPorts(any(PaasManagerUser.class), anyString())).thenReturn(response);

        List<Port> ports = openStackNetworkImpl.listPortsFromNetwork(claudiaData, "region", "6a609412-3f04-485c-b269-b1a9b9ecb6bf");

        // then
        assertNotNull(ports);
        verify(openStackUtil).listPorts(any(PaasManagerUser.class), anyString());
        assertEquals(1, ports.size());
       
    }

    
    @Test
    public void shouldLoadNotSharedNetworks() throws OpenStackException, InfrastructureException {

        // when
        String response = "{\"networks\": [{\"status\": \"ACTIVE\", \"subnets\": [\"2b7a07f6-0b73-46a1-9327-6911c0480f49\"], \"name\": "+
        " \"dia146\", \"provider:physical_network\": null, \"admin_state_up\": true, \"tenant_id\": \"67c979f51c5b4e89b85c1f876bdffe31\", "+
        " \"provider:network_type\": \"gre\", \"router:external\": false, \"shared\": true, \"id\": \"044aecbe-3975-4318-aad2-a1232dcde47d\", "+
        " \"provider:segmentation_id\": 8}, {\"status\": \"ACTIVE\", \"subnets\": [\"e2d10e6b-33c3-400c-88d6-f905d4cd02f2\"], \"name\": \"ext-net\","+
        " \"provider:physical_network\": null, \"admin_state_up\": true, \"tenant_id\": \"08bed031f6c54c9d9b35b42aa06b51c0\", \"provider:network_type\": "+
        " \"gre\", \"router:external\": true, \"shared\": false, \"id\": \"080b5f2a-668f-45e0-be23-361c3a7d11d0\", \"provider:segmentation_id\": 1}" +
         "]}";
        when(openStackUtil.listNetworks(any(PaasManagerUser.class),anyString())).thenReturn(response);

        List<NetworkInstance> networks = openStackNetworkImpl.loadNotSharedNetworks(claudiaData, REGION);

        // then
        assertNotNull(networks);

        verify(openStackUtil).listNetworks(any(PaasManagerUser.class),anyString());
        assertEquals(1, networks.size());
    }
  

    
    @Test
    public void shouldObtainDataNetwork() throws OpenStackException, InfrastructureException {


        // when
        String response = "{\"networks\": [{\"status\": \"ACTIVE\", \"subnets\": [\"2b7a07f6-0b73-46a1-9327-6911c0480f49\"], \"name\": "+
        " \"dia146\", \"provider:physical_network\": null, \"admin_state_up\": true, \"tenant_id\": \"67c979f51c5b4e89b85c1f876bdffe31\", "+
        " \"provider:network_type\": \"gre\", \"router:external\": false, \"shared\": false, \"id\": \"044aecbe-3975-4318-aad2-a1232dcde47d\", "+
        " \"provider:segmentation_id\": 8}" +
         "]}";
        when(openStackUtil.listNetworks(any(PaasManagerUser.class),anyString())).thenReturn(response);

        List<NetworkInstance> networks = openStackNetworkImpl.loadAllNetwork(claudiaData, REGION);

        // then
        assertNotNull(networks);
        verify(openStackUtil).listNetworks(any(PaasManagerUser.class),anyString());
        assertEquals(1, networks.size());
        assertEquals(networks.get(0).getAdminStateUp(), true);
        assertEquals(networks.get(0).getNetworkName(), "dia146");
        assertEquals(networks.get(0).getIdNetwork(), "044aecbe-3975-4318-aad2-a1232dcde47d");
        assertEquals(networks.get(0).getShared(), false);
        assertEquals(networks.get(0).getTenantId(), "67c979f51c5b4e89b85c1f876bdffe31");


    }
    
    @Test
    public void shouldAddNetworkToPublicRouter() throws OpenStackException, InfrastructureException {

        // when
        String response = "response";
        NetworkInstance net = new NetworkInstance ();
        when(openStackUtil.addInterfaceToPublicRouter(any(PaasManagerUser.class), any(NetworkInstance.class),anyString())).thenReturn(response);

        openStackNetworkImpl.addNetworkToPublicRouter(claudiaData, net,REGION);

        verify(openStackUtil).addInterfaceToPublicRouter(any(PaasManagerUser.class),any(NetworkInstance.class),anyString());

    }
    
    @Test
    public void shouldDeployDefaultNetwork() throws OpenStackException, InfrastructureException {

        // when
        String response = "{network: {\"status\": \"ACTIVE\", \"subnets\": [\"2b7a07f6-0b73-46a1-9327-6911c0480f49\"], \"name\": "+
        " \"network\", \"provider:physical_network\": null, \"admin_state_up\": true, \"tenant_id\": \"67c979f51c5b4e89b85c1f876bdffe31\", "+
        " \"provider:network_type\": \"gre\", \"router:external\": false, \"shared\": false, \"id\": \"044aecbe-3975-4318-aad2-a1232dcde47d\", "+
        " \"provider:segmentation_id\": 8}}" ;
        NetworkInstance net = new NetworkInstance ("network", "vdc");
        when(openStackUtil.createNetwork(any(String.class),anyString(),anyString(),anyString())).thenReturn(response);

        NetworkInstance net2 = openStackNetworkImpl.deployDefaultNetwork(claudiaData, REGION);

        verify(openStackUtil).createNetwork(any(String.class),anyString(),anyString(),anyString());
        assertNotNull(net2);
        assertEquals(net2.getNetworkName(), "network");

    }
    
    @Test
    public void shouldDeployNetwork() throws OpenStackException, InfrastructureException {

        // when
        String response = "{network: {\"status\": \"ACTIVE\", \"subnets\": [\"2b7a07f6-0b73-46a1-9327-6911c0480f49\"], \"name\": "+
        " \"network\", \"provider:physical_network\": null, \"admin_state_up\": true, \"tenant_id\": \"67c979f51c5b4e89b85c1f876bdffe31\", "+
        " \"provider:network_type\": \"gre\", \"router:external\": false, \"shared\": false, \"id\": \"044aecbe-3975-4318-aad2-a1232dcde47d\", "+
        " \"provider:segmentation_id\": 8}}" ;
         
        NetworkInstance net = new NetworkInstance ("network", "vdc");
        when(openStackUtil.createNetwork(any(String.class),anyString(),anyString(),anyString())).thenReturn(response);

        openStackNetworkImpl.deployNetwork(claudiaData, net, REGION);

        verify(openStackUtil).createNetwork(any(String.class),anyString(),anyString(),anyString());
        assertNotNull(net.getIdNetwork());


    }
    
    @Test
    public void shouldDestroyNetwork() throws OpenStackException, InfrastructureException {

        // when
        String response = "ok";
        NetworkInstance net = new NetworkInstance ("network", "vdc");
        when(openStackUtil.deleteNetwork(any(String.class),anyString(),anyString(),anyString())).thenReturn(response);

        openStackNetworkImpl.destroyNetwork(claudiaData, net, REGION);

        verify(openStackUtil).deleteNetwork(any(String.class),anyString(),anyString(),anyString());

    }
    
    @Test
    public void shouldDestroyRouter() throws OpenStackException, InfrastructureException {

        // when
        String response = "ok";
        RouterInstance net = new RouterInstance ("router");
        when(openStackUtil.deleteRouter(any(String.class),anyString(),anyString(),anyString())).thenReturn(response);
        openStackNetworkImpl.destroyRouter(claudiaData, net, REGION);

        verify(openStackUtil).deleteRouter(any(String.class),anyString(),anyString(),anyString());
    }
    
    @Test
    public void shouldDeleteNetworkToPublicRouter() throws OpenStackException, InfrastructureException {

        // when
        String response = "ok";
        NetworkInstance net = new NetworkInstance ("router", "vdc");
        when(openStackUtil.deleteInterfaceToPublicRouter(any(PaasManagerUser.class),any(NetworkInstance.class),anyString())).thenReturn(response);
        openStackNetworkImpl.deleteNetworkToPublicRouter(claudiaData, net, REGION);

        verify(openStackUtil).deleteInterfaceToPublicRouter(any(PaasManagerUser.class), any(NetworkInstance.class),anyString());
    }   

}
