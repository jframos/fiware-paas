/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OpenstackFirewallingclientImplTest {

    @Test
    public void shouldParseSecurityGroupError() {
        // given
        String response = "{\"computeFault\": {\"message\": \"The server has either erred or is incapable of performing the requested operation.\", \"code\": 500}}";
        OpenstackFirewallingClientImpl openstackFirewallingClient = new OpenstackFirewallingClientImpl();

        // when
        String message = openstackFirewallingClient.parseOpenStackError(response);

        // then
        assertEquals("The server has either erred or is incapable of performing the requested operation.", message);
    }

}
