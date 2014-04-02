/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
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
