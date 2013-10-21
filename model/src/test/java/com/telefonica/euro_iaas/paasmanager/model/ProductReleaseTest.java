/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import junit.framework.TestCase;
import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.Test;

/**
 * @author jesus.movilla
 */
public class ProductReleaseTest extends TestCase {

    private ProductRelease productRelease;
    private JSONObject productReleaseJson, productReleaseJsonNoAttributes, productReleaseJsonOneAttribute;
    private JSONObject productReleaseJsonNoReleaseNotesNoSSOO;

    @Before
    public void setUp() throws Exception {

        productRelease = new ProductRelease();
        /*
         * String productReleaseString = "{\"rules\":[" +
         * "{\"from_port\":22,\"group\":{},\"ip_protocol\":\"tcp\",\"to_port\":22,\"parent_group_id\":6,\"ip_range\":{\"cidr\":\"0.0.0.0/0\"},\"id\":10},"
         * +
         * "{\"from_port\":8080,\"group\":{},\"ip_protocol\":\"tcp\",\"to_port\":8080,\"parent_group_id\":6,\"ip_range\":{\"cidr\":\"0.0.0.0/0\"},\"id\":11}]"
         * + ",\"tenant_id\":\"ebe6d9ec7b024361b7a3882c65a57dda\"," + "\"id\":6," + "\"name\":\"namedefault\"," +
         * "\"description\":\"default\"" + "} ";
         */

        String productReleaseStringNoAttributes = "{" + "\"releaseNotes\":\"Tomcat server 7\"," + "\"version\":\"7\","
                + "\"product\":" + "{" + "\"name\":\"tomcat\"," + "\"description\":\"tomcat J2EE container\"" +
                /*
                 * "\"attributes\":" + "[" +
                 * "{\"key\":\"port\",\"value\":\"8080\",\"description\":\"The listen port\"}," +
                 * "{\"key\":\"ssl_port\",\"value\":\"8443\",\"description\":\"The ssl listen port\"}," +
                 * "{\"key\":\"ssl_port\",\"value\":\"8443\",\"description\":\"The ssl listen port\"}," +
                 * "{\"key\":\"id_web_server\",\"value\":\"default\",\"description\":\"The id web server\"}," +
                 * "{\"key\":\"sdcgroupid\",\"value\":\"id_web_server\",\"description\":\"sdcgroupid\"}" + "]" +
                 */
                "}," + "\"supportedOOSS\":" + "["
                + "{\"description\":\"Ubuntu 10.04\",\"name\":\"Ubuntu\",\"osType\":\"94\",\"version\":\"10.04\"},"
                + "{\"description\":\"Debian 5\",\"name\":\"Debian\",\"osType\":\"95\",\"version\":\"5\"},"
                + "{\"description\":\"Centos 2.9\",\"name\":\"Centos\",\"osType\":\"76\",\"version\":\"2.9\"}" + "]"
                + "}";

        String productReleaseString = "{" + "\"releaseNotes\":\"Tomcat server 7\"," + "\"version\":\"7\","
                + "\"product\":" + "{" + "\"name\":\"tomcat\"," + "\"description\":\"tomcat J2EE container\","
                + "\"attributes\":" + "["
                + "{\"key\":\"port\",\"value\":\"8080\",\"description\":\"The listen port\"},"
                + "{\"key\":\"ssl_port\",\"value\":\"8443\",\"description\":\"The ssl listen port\"},"
                + "{\"key\":\"ssl_port\",\"value\":\"8443\",\"description\":\"The ssl listen port\"},"
                + "{\"key\":\"id_web_server\",\"value\":\"default\",\"description\":\"The id web server\"},"
                + "{\"key\":\"sdcgroupid\",\"value\":\"id_web_server\",\"description\":\"sdcgroupid\"}" + "]" + "},"
                + "\"supportedOOSS\":" + "["
                + "{\"description\":\"Ubuntu 10.04\",\"name\":\"Ubuntu\",\"osType\":\"94\",\"version\":\"10.04\"},"
                + "{\"description\":\"Debian 5\",\"name\":\"Debian\",\"osType\":\"95\",\"version\":\"5\"},"
                + "{\"description\":\"Centos 2.9\",\"name\":\"Centos\",\"osType\":\"76\",\"version\":\"2.9\"}" + "]"
                + "}";

        String productReleaseStringOneAttribute = "{"
                + "\"releaseNotes\":\"mysql 1.2.4\","
                + "\"version\":\"1.2.4\","
                + "\"product\":"
                + "{"
                + "\"name\":\"mysql\","
                + "\"description\":\"mysql\","
                + "\"attributes\":"
                +
                // "[" +
                "{\"key\":\"port\",\"value\":\"8080\",\"description\":\"The listen port\"}"
                +
                // "]" +
                "}," + "\"supportedOOSS\":" + "[" + "{" + "\"description\":\"Ubuntu 10.04\"," + "\"name\":\"Ubuntu\","
                + "\"osType\":\"94\"," + "\"version\":\"10.04\"" + "}," + "{" + "\"description\":\"Debian 5\","
                + "\"name\":\"Debian\"," + "\"osType\":\"95\"," + "\"version\":\"5\"" + "}," + "{"
                + "\"description\":\"Centos 2.9\"," + "\"name\":\"Centos\"," + "\"osType\":\"76\",\"version\":\"2.9\""
                + "}" + "]" + "}";

        String productReleaseNoReleaseNotesNoSSOO = "{" + "\"version\":\"1.0\"," + "\"product\":" + "{"
                + "\"name\":\"henar10\"," + "\"attributes\":{\"key\":\"ssl_port5\",\"value\":\"8443\"}" + "}" + "}";

        productReleaseJson = JSONObject.fromObject(productReleaseString);
        productReleaseJsonNoAttributes = JSONObject.fromObject(productReleaseStringNoAttributes);
        System.out.println("productReleaseStringOneAttribute:***********");
        System.out.println(productReleaseStringOneAttribute);
        productReleaseJsonOneAttribute = JSONObject.fromObject(productReleaseStringOneAttribute);
        productReleaseJsonNoReleaseNotesNoSSOO = JSONObject.fromObject(productReleaseNoReleaseNotesNoSSOO);
    }

    @Test
    public void testFromJson() throws Exception {
        productRelease.fromSdcJson(productReleaseJson);
        assertEquals(productRelease.getDescription(), "Tomcat server 7");
        assertEquals(productRelease.getVersion(), "7");
        assertEquals(productRelease.getProduct(), "tomcat");
        assertEquals(productRelease.getName(), "tomcat-7");
    }

    @Test
    public void testFromJsonNoAttributes() throws Exception {
        productRelease.fromSdcJson(productReleaseJsonNoAttributes);
        assertEquals(productRelease.getDescription(), "Tomcat server 7");
        assertEquals(productRelease.getVersion(), "7");
        assertEquals(productRelease.getProduct(), "tomcat");
        assertEquals(productRelease.getName(), "tomcat-7");
    }

    @Test
    public void testOneAttributeFromJson() throws Exception {
        productRelease.fromSdcJson(productReleaseJsonOneAttribute);
        assertEquals(productRelease.getDescription(), "mysql 1.2.4");
        assertEquals(productRelease.getVersion(), "1.2.4");
        assertEquals(productRelease.getProduct(), "mysql");
        assertEquals(productRelease.getName(), "mysql-1.2.4");
    }

    @Test
    public void testFromJsonNoReleaseNotesNoSSOO() throws Exception {
        productRelease.fromSdcJson(productReleaseJsonNoReleaseNotesNoSSOO);
        assertEquals(productRelease.getVersion(), "1.0");
        assertEquals(productRelease.getProduct(), "henar10");
        assertEquals(productRelease.getName(), "henar10-1.0");
    }
}
