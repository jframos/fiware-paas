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

import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Limits;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.util.OpenStackUtil;

public class OpenStackQuotaImplTest {

    @Test
    public void shouldGetLimitsFromExistingTenantId() throws OpenStackException, InfrastructureException {

        // given
        OpenStackQuotaClientImpl openStackQuotaImpl = new OpenStackQuotaClientImpl();

        ClaudiaData claudiaData = new ClaudiaData("org", "tenantId", "service");
        OpenStackUtil openStackUtil = mock(OpenStackUtil.class);
        openStackQuotaImpl.setOpenStackUtil(openStackUtil);
        // when
        String response = "{\"limits\": {\"rate\": [{\"regex\": \".*\", \"limit\": [{\"next-available\": \"2013-10-29T15:17:15Z\", \"unit\": \"MINUTE\", \"verb\": \"POST\", \"remaining\": 7, \"value\": 10}, {\"next-available\": \"2013-10-29T15:18:22Z\", \"unit\": \"MINUTE\", \"verb\": \"PUT\", \"remaining\": 10, \"value\": 10}, {\"next-available\": \"2013-10-29T15:15:55Z\", \"unit\": \"MINUTE\", \"verb\": \"DELETE\", \"remaining\": 98, \"value\": 100}], \"uri\": \"*\"}, {\"regex\": \"^/servers\", \"limit\": [{\"next-available\": \"2013-10-29T15:18:22Z\", \"unit\": \"DAY\", \"verb\": \"POST\", \"remaining\": 50, \"value\": 50}], \"uri\": \"*/servers\"}, {\"regex\": \".*changes-since.*\", \"limit\": [{\"next-available\": \"2013-10-29T15:18:22Z\", \"unit\": \"MINUTE\", \"verb\": \"GET\", \"remaining\": 3, \"value\": 3}], \"uri\": \"*changes-since*\"}, {\"regex\": \"^/os-fping\", \"limit\": [{\"next-available\": \"2013-10-29T15:18:22Z\", \"unit\": \"HOUR\", \"verb\": \"GET\", \"remaining\": 12, \"value\": 12}], \"uri\": \"*/os-fping\"}], \"absolute\": {\"maxServerMeta\": 128, \"maxPersonality\": 5, \"maxImageMeta\": 128, \"maxPersonalitySize\": 10240, \"maxSecurityGroupRules\": 20, \"maxTotalKeypairs\": 100, \"totalRAMUsed\": 4096, \"totalInstancesUsed\": 2, \"maxSecurityGroups\": 10, \"totalFloatingIpsUsed\": 0, \"maxTotalCores\": 20, \"totalSecurityGroupsUsed\": 0, \"maxTotalFloatingIps\": 10, \"maxTotalInstances\": 10, \"totalCoresUsed\": 2, \"maxTotalRAMSize\": 51200}}}";
        when(openStackUtil.getAbsoluteLimits(any(PaasManagerUser.class))).thenReturn(response);

        Limits limits = openStackQuotaImpl.getLimits(claudiaData);

        // then
        assertNotNull(limits);
        verify(openStackUtil).getAbsoluteLimits(any(PaasManagerUser.class));
        assertEquals(new Integer(10), limits.getMaxTotalFloatingIps());
        assertEquals(new Integer(0), limits.getTotalFloatingIpsUsed());
        assertEquals(new Integer(10), limits.getMaxTotalInstances());
        assertEquals(new Integer(2), limits.getTotalInstancesUsed());
    }
}
