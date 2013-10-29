/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Limits;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.util.OpenStackUtil;

public class OpenStackQuotaImplTest {

    @Test
    public void shouldGetLimitsFromExistingTenantId() throws OpenStackException {

        // given
        OpenStackQuotaClientImpl openStackQuotaImpl = new OpenStackQuotaClientImpl();

        ClaudiaData claudiaData = new ClaudiaData("org", "tenantId", "service");
        OpenStackUtil openStackUtil = mock(OpenStackUtil.class);
        // when
        String response = "responseJson";
        when(openStackUtil.getAbsoluteLimits(any(PaasManagerUser.class))).thenReturn(response);

        Limits limits = openStackQuotaImpl.getLimits(claudiaData);

        // then
        assertNotNull(limits);
        verify(openStackUtil).getAbsoluteLimits(any(PaasManagerUser.class));
    }
}
