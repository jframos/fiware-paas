/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.util;

import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import org.junit.Before;

/**
 * @author jesus.movilla
 */
public class OpenStackUtilImplTest {

    private String payload;
    private PaasManagerUser user;
    private String name;

    @Before
    public void setUp() throws Exception {
        name = "pruebatest";

        user.setTenantId("tenantId");
        user.setToken("token");

        payload = "{\"server\": \n" + "{\"name\": \"" + name + "\", "
                + "\"imageRef\": \"44dcdba3-a75d-46a3-b209-5e9035d2435e\", " + "\"flavorRef\": \"2\"}}";

    }

    /*
     * @Test public void testgetCredentials() throws Exception { // POST to http://130.206.80.63:35357/v2.0/tokens //
     * Payload: {"auth": {"tenantName": "jesusproject", // "passwordCredentials":{"username": "jesus", "password":
     * "susje"}}} // Response extract tenantId and tokenId }
     */

    /*
     * @Test public void testcreateServer() throws Exception { OpenStackUtilImpl openStackUtilImpl = new
     * OpenStackUtilImpl(); openStackUtilImpl.createServer(payload, user); }
     */

    /*
     * @Test public void testdeleteServer() throws Exception { OpenStackUtilImpl openStackUtilImpl = new
     * OpenStackUtilImpl(); openStackUtilImpl.deleteServer("serverId", user); }
     */
}
