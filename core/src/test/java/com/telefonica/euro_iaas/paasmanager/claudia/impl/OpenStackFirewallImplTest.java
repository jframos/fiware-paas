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
import static org.mockito.Mockito.any;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Limits;
import com.telefonica.euro_iaas.paasmanager.model.Rule;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.util.OpenStackRegion;
import com.telefonica.euro_iaas.paasmanager.util.OpenStackUtil;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public class OpenStackFirewallImplTest {
    private OpenstackFirewallingClientImpl openstackFirewallingClientImpl;
    private OpenStackRegion openStackRegion;
    public static String REGION ="region";
    public static String VDC ="vdc";
    public static String TOKEN ="token";
    @Before
    public void setUp () {
        openstackFirewallingClientImpl = new OpenstackFirewallingClientImpl ();
        openStackRegion = mock(OpenStackRegion.class);
        openstackFirewallingClientImpl.setOpenStackRegion(openStackRegion);
        SystemPropertiesProvider systemPropertiesProvider = mock (SystemPropertiesProvider.class);
        openstackFirewallingClientImpl.setSystemPropertiesProvider(systemPropertiesProvider);
    }

    @Test
    public void shouldGetLimitsFromExistingTenantId() throws OpenStackException, InfrastructureException {

        // given
        SecurityGroup securityGroup = new SecurityGroup("nanme", "description");
        securityGroup.addRule(new Rule("ipProtocol", "fromPort", "toPort", "sourceGroup", "cidr"));
        when(openStackRegion.getNovaEndPoint(any(String.class), any(String.class))).thenReturn("http..");
        // then
       // openstackFirewallingClientImpl.deploySecurityGroup(REGION, TOKEN, VDC, securityGroup);
       
    }
}
