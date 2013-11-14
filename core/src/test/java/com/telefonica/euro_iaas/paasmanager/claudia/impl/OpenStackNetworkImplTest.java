/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Limits;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.util.OpenStackUtil;

public class OpenStackNetworkImplTest {

    @Test
    public void shouldListNetworks() throws OpenStackException, InfrastructureException {

        // given
        OpenstackNetworkClientImpl openStackNetworkImpl = new OpenstackNetworkClientImpl();
 
        ClaudiaData claudiaData = new ClaudiaData("org", "tenantId", "service");
        OpenStackUtil openStackUtil = mock(OpenStackUtil.class);
        openStackNetworkImpl.setOpenStackUtil(openStackUtil);
        // when
        String response = "{\"networks\": [{\"status\": \"ACTIVE\", \"subnets\": [\"2b7a07f6-0b73-46a1-9327-6911c0480f49\"], \"name\": "+
    	" \"dia146\", \"provider:physical_network\": null, \"admin_state_up\": true, \"tenant_id\": \"67c979f51c5b4e89b85c1f876bdffe31\", "+
    	" \"provider:network_type\": \"gre\", \"router:external\": false, \"shared\": false, \"id\": \"044aecbe-3975-4318-aad2-a1232dcde47d\", "+
    	" \"provider:segmentation_id\": 8}, {\"status\": \"ACTIVE\", \"subnets\": [\"e2d10e6b-33c3-400c-88d6-f905d4cd02f2\"], \"name\": \"ext-net\","+
    	" \"provider:physical_network\": null, \"admin_state_up\": true, \"tenant_id\": \"08bed031f6c54c9d9b35b42aa06b51c0\", \"provider:network_type\": "+
    	" \"gre\", \"router:external\": true, \"shared\": false, \"id\": \"080b5f2a-668f-45e0-be23-361c3a7d11d0\", \"provider:segmentation_id\": 1}" +
    	 "]}";
        when(openStackUtil.listNetworks(any(PaasManagerUser.class))).thenReturn(response);

        List<NetworkInstance> networks = openStackNetworkImpl.loadAllNetwork(claudiaData);

        // then
        assertNotNull(networks);
        verify(openStackUtil).listNetworks(any(PaasManagerUser.class));
        assertEquals(2, networks.size());
        assertEquals("dia146", networks.get(0).getNetworkName());
    }
}
