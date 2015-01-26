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

package com.telefonica.euro_iaas.paasmanager.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.model.dto.NetworkDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.SubNetworkDto;


/**
 * Network Test.
 *
 * @author henar
 */

public class NetworkTest extends TestCase {


    private static final String NETWORK_NAME = "name";
    private static final String SUBNETWORK_NAME = "subname";
    private static final String REGION = "region";
    private static final String ROUTER_NAME = "router";
    private static final String ID_PUBLIC_NET = "IDPUBLIC";
    private static final String CIDR = "10.100.1.0/24";
    private static final String CIDR_ID = "1";
    private static final String CIDR2 = "10.100.2.0/24";
    private static final String CIDR2_ID = "2";
    private static final String VDC = "vdc";
    private static final String ID = "8";


    private static final String SUBNETWORK_STRING = "{\"subnet\":{"
            + "      \"name\":\"" + SUBNETWORK_NAME + "\","
            + "      \"network_id\":\"" + ID + "\","
            + "      \"ip_version\":4,    \"dns_nameservers\": [\"8.8.8.8\"],       \"cidr\":\""
            + CIDR + "\""
            + "   }"
            + "}";

    private static final String ADDINTERFACE = "{\"subnet_id\": \"" + ID + "\" }";

    private static final String ROUTER_STRING = "{"
            + "    \"router\":"
            + "    {"
            + "        \"name\": \"" + ROUTER_NAME + "\","
            + "        \"admin_state_up\": true ,"
            + "        \"external_gateway_info\" : {"
            + "             \"network_id\": \"" + ID + "\""
            + "        }"
            + "    }"
            + "}";


    private static final String NETWORK_STRING = "{" + " \"network\":{" + "    \"name\": \"" + NETWORK_NAME + "\","
            + "    \"admin_state_up\": true," + "    \"shared\": false" + "  }" + "}";


    @Override
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Create network test.
     *
     * @throws Exception
     */

    @Test
    public void testCreateNetwork() throws Exception {

        Network network = new Network(NETWORK_NAME, VDC, REGION);
        assertEquals(network.getNetworkName(), NETWORK_NAME);
        assertEquals(network.getSubNets().size(), 0);

    }


    /**
     * Create network and subnet test.
     *
     * @throws Exception
     */
    @Test
    public void testCreateNetworkAndSubNet() throws Exception {

        Network network = new Network(NETWORK_NAME, VDC, REGION);

        SubNetwork subnet = new SubNetwork(SUBNETWORK_NAME, VDC, REGION);
        network.addSubNet(subnet);
        assertEquals(network.getNetworkName(), NETWORK_NAME);
        assertEquals(network.getVdc(), VDC);
        assertEquals(network.getRegion(), REGION);
        assertEquals(network.getSubNets().size(), 1);
        for (SubNetwork subNet : network.getSubNets()) {
            assertEquals(subNet.getName(), SUBNETWORK_NAME);
            assertEquals(subNet.getVdc(), VDC);
            assertEquals(subNet.getRegion(), REGION);
        }
    }

    /**
     * Create network and subnet test.
     *
     * @throws Exception
     */
    @Test
    public void testCloneSubNets() throws Exception {

        Network network = new Network();
        SubNetwork subnet = new SubNetwork(SUBNETWORK_NAME, VDC, REGION);
        network.addSubNet(subnet);

        Set<SubNetwork> subNets = network.cloneSubNets();

        assertEquals(subNets.size(), 1);
        for (SubNetwork subNet : subNets) {
            assertEquals(subNet.getName(), SUBNETWORK_NAME);
        }
    }


    /**
     * Update subNet.
     *
     * @throws Exception
     */
    @Test
    public void testContainSubNet() throws Exception {

        Network network = new Network();
        network.setNetworkName(NETWORK_NAME);
        SubNetwork subnet = new SubNetwork(SUBNETWORK_NAME, VDC, REGION);
        network.addSubNet(subnet);

        SubNetwork subnet2 = new SubNetwork(SUBNETWORK_NAME + 2, VDC, REGION);

        assertEquals(network.contains(subnet), true);
        assertEquals(network.contains(subnet2), false);

    }

    /**
     * Delete subNet.
     *
     * @throws Exception
     */
    @Test
    public void testDeleteSubNet() throws Exception {

        Network network = new Network();
        network.setNetworkName(NETWORK_NAME);
        SubNetwork subnet = new SubNetwork(SUBNETWORK_NAME, VDC, REGION);
        Set<SubNetwork> subNets = new HashSet<SubNetwork>();
        subNets.add(subnet);
        network.setSubNets(subNets);

        network.deleteSubNet(subnet);
        assertEquals(network.getSubNets().size(), 0);
    }

    /**
     * to Dto.
     *
     * @throws Exception
     */
    @Test
    public void testToDto() throws Exception {

        Network network = new Network();
        network.setNetworkName(NETWORK_NAME);
        Set<SubNetwork> subNets = new HashSet<SubNetwork>();
        SubNetwork subnet = new SubNetwork(SUBNETWORK_NAME, VDC, REGION);
        subNets.add(subnet);
        network.setSubNets(subNets);
        SubNetwork subnet2 = new SubNetwork(SUBNETWORK_NAME + 2, VDC, REGION);
        network.addSubNet(subnet2);
        NetworkDto netDto = network.toDto();
        assertEquals(netDto.getNetworkName(), NETWORK_NAME);
        assertEquals(netDto.getSubNetworkDto().size(), 2);
    }

    /**
     * Test that the creation of a network from Dto is correct.
     * @throws Exception
     */
    @Test
    public void testFromDto() throws Exception {

        NetworkDto network = new NetworkDto();
        network.setNetworkName(NETWORK_NAME);
        List<SubNetworkDto> subNets = new ArrayList<SubNetworkDto>();
        SubNetworkDto subnet = new SubNetworkDto(SUBNETWORK_NAME);
        subNets.add(subnet);
        network.setSubNetworkDto(subNets);
        SubNetworkDto subnet2 = new SubNetworkDto(SUBNETWORK_NAME + 2);
        network.addSubNetworkDto(subnet2);
        Network netDto = network.fromDto(VDC, REGION);
        assertEquals(netDto.getNetworkName(), NETWORK_NAME);
        assertEquals(netDto.getSubNets().size(), 2);
    }

    /**
     * Equals.
     *
     * @throws Exception
     */
    @Test
    public void testEquals() throws Exception {

        Network network = new Network();
        network.setNetworkName(NETWORK_NAME);
        Network network2 = new Network(NETWORK_NAME + 2, VDC, REGION);
        Network network3 = new Network(NETWORK_NAME, VDC, REGION);
        assertEquals(network.equals(network2), false);
        assertEquals(network.toDto().equals(network2.toDto()), false);
    }

    /**
     * Create a network instance test.
     *
     * @throws Exception
     */
    @Test
    public void testCreateInstanceNetwork() throws Exception {

        Network network = new Network(NETWORK_NAME, VDC, REGION);
        SubNetwork subNet = new SubNetwork(SUBNETWORK_NAME, VDC, REGION);
        network.addSubNet(subNet);
        NetworkInstance networkInstance = network.toNetworkInstance();
        assertEquals(networkInstance.getNetworkName(), NETWORK_NAME);
        assertEquals(networkInstance.toJson(), NETWORK_STRING);
        assertEquals(networkInstance.getSubNets().size(), 1);
        for (SubNetworkInstance subNet2 : networkInstance.getSubNets()) {
            assertEquals(subNet2.getName(),
                    SUBNETWORK_NAME);
        }
    }

    /**
     * It tests the creation of network, subnetwork and router.
     *
     * @throws Exception
     */
    @Test
    public void testCreateNetworkAndSubNetAndRouter() throws Exception {

        Network network = new Network(NETWORK_NAME, VDC, REGION);
        SubNetwork subnet = new SubNetwork(SUBNETWORK_NAME, VDC, REGION);
        network.addSubNet(subnet);
        RouterInstance router = new RouterInstance(ID_PUBLIC_NET, ROUTER_NAME);

        router.setIdRouter(ID);
        assertEquals(network.getNetworkName(), NETWORK_NAME);
        assertEquals(network.getSubNets().size(), 1);
        for (SubNetwork subNet : network.getSubNets()) {
            assertEquals(subNet.getName(), SUBNETWORK_NAME);
        }
    }

    /**
     * It tests the creation o subnetwork and its json representation.
     *
     * @throws Exception
     */
    @Test
    public void testCreateSubNetworkInstance() throws Exception {

        NetworkInstance network = new NetworkInstance(NETWORK_NAME, VDC, REGION);
        network.setIdNetwork(ID);
        SubNetworkInstance subNetwork = new SubNetworkInstance(SUBNETWORK_NAME, VDC, REGION);
        subNetwork.setIdSubNet(ID);
        network.addSubNet(subNetwork);
        assertEquals(subNetwork.toJson(), SUBNETWORK_STRING);
        assertEquals(network.toAddInterfaceJson(), ADDINTERFACE);

    }

    /**
     * It tests the creation of a router subnetwork and its json representation.
     *
     * @throws Exception
     */
    @Test
    public void testCreateRouter() throws Exception {

        NetworkInstance network = new NetworkInstance(NETWORK_NAME, VDC, REGION);
        network.setIdNetwork(ID);
        SubNetworkInstance subNetwork = new SubNetworkInstance(SUBNETWORK_NAME, VDC, REGION);
        subNetwork.setIdSubNet(ID);
        network.addSubNet(subNetwork);
        RouterInstance router = new RouterInstance(ID, ROUTER_NAME);
        router.setIdRouter(ID);
        network.addRouter(router);
        assertEquals(router.toJson(), ROUTER_STRING);
        assertEquals(network.toAddInterfaceJson(), ADDINTERFACE);

    }

    /**
     * Test the creation of a network from json file.
     * @throws Exception
     */
    @Test
    public void testFromJSon() throws Exception {

        String payload = "{\"status\": \"ACTIVE\", "
                + " \"external_gateway_info\": { "
                + " \"network_id\": \"080b5f2a-668f-45e0-be23-361c3a7d11d0\" "
                + " }, "
                + " \"name\": \"test-rt1\", "
                + "\"admin_state_up\": true, "
                + "\"tenant_id\": \"08bed031f6c54c9d9b35b42aa06b51c0\", "
                + "\"routes\": [], "
                + "\"shared\": false, "
                + "\"router:external\": false, "
                + "\"id\": \"5af6238b-0e9c-4c20-8981-6e4db6de2e17\""
                + "}";
        JSONObject jsonNet = new JSONObject(payload);
        NetworkInstance net = NetworkInstance.fromJson(jsonNet, "region");
        assertEquals(net.getIdNetwork(), "5af6238b-0e9c-4c20-8981-6e4db6de2e17");
        assertEquals(net.getNetworkName(), "test-rt1");
        assertEquals(net.getTenantId(), "08bed031f6c54c9d9b35b42aa06b51c0");

    }

    /**
     * Test the creation of a network from json file.
     * @throws Exception
     */
    @Test
    public void testSubNetFromJSon() throws Exception {

        String payload = "\n" +
                "\n" +
                "       {\n" +
                "           \"name\": \"dd\",\n" +
                "           \"enable_dhcp\": true,\n" +
                "           \"network_id\": \"33cb6d12-3792-4ff7-8abe-8f948ce60a4d\",\n" +
                "           \"tenant_id\": \"00000000000000000000000000000046\",\n" +
                "           \"dns_nameservers\":\n" +
                "           [\n" +
                "           ],\n" +
                "           \"allocation_pools\":\n" +
                "           [\n" +
                "               {\n" +
                "                   \"start\": \"12.1.0.2\",\n" +
                "                   \"end\": \"12.1.0.254\"\n" +
                "               }\n" +
                "           ],\n" +
                "           \"host_routes\":\n" +
                "           [\n" +
                "           ],\n" +
                "           \"ip_version\": 4,\n" +
                "           \"gateway_ip\": \"12.1.0.1\",\n" +
                "           \"cidr\": \"12.1.0.0/24\",\n" +
                "           \"id\": \"6959446a-204b-4e68-a5d2-f7a3ef5a442a\"\n" +
                "       }\n" +
                "\n";

        JSONObject jsonNet = new JSONObject(payload);
        SubNetworkInstance subnet = SubNetworkInstance.fromJson(jsonNet, "region");
        assertEquals(subnet.getIdNetwork(), "33cb6d12-3792-4ff7-8abe-8f948ce60a4d");
        assertEquals(subnet.getIdSubNet(), "6959446a-204b-4e68-a5d2-f7a3ef5a442a");
        assertEquals(subnet.getName(), "dd");
        assertEquals(subnet.getCidr(), "12.1.0.0/24");
        assertEquals(subnet.getRegion(), "region");

    }


    /**
     * It test the dto from the Network specification.
     *
     * @throws Exception
     */
    @Test
    public void testFromDtoII() throws Exception {
        NetworkDto networkDto = new NetworkDto(NETWORK_NAME);
        SubNetworkDto subNetworkDto = new SubNetworkDto(SUBNETWORK_NAME, CIDR);
        networkDto.addSubNetworkDto(subNetworkDto);
        Network net = networkDto.fromDto(VDC, REGION);

        assertEquals(net.getNetworkName(), NETWORK_NAME);
        assertEquals(net.getSubNets().size(), 1);
        for (SubNetwork subNet : net.getSubNets()) {
            assertEquals(subNet.getName(), SUBNETWORK_NAME);
        }
    }

    /**
     * Clone subnet instances.
     *
     * @throws Exception
     */
    @Test
    public void testCloneSubNetInstances() throws Exception {

        NetworkInstance network = new NetworkInstance(NETWORK_NAME, VDC, REGION);
        network.setIdNetwork(ID);
        SubNetworkInstance subNetwork = new SubNetworkInstance(SUBNETWORK_NAME, VDC, REGION);
        subNetwork.setIdSubNet(ID);
        network.addSubNet(subNetwork);

        Set<SubNetworkInstance> subNets = network.cloneSubNets();
        assertEquals(subNets.size(), 1);
        for (SubNetworkInstance subNet : subNets) {
            assertEquals(subNet.getName(), SUBNETWORK_NAME);
            assertEquals(subNet.getCidr(), CIDR);
        }
    }

    /**
     * It tests the eliminination of the subnets.
     * @throws Exception
     */
    @Test
    public void testClearSubNetInstances() throws Exception {

        NetworkInstance network = new NetworkInstance(NETWORK_NAME, VDC, REGION);
        network.setIdNetwork(ID);
        SubNetworkInstance subNetwork = new SubNetworkInstance(SUBNETWORK_NAME, VDC, REGION);
        subNetwork.setIdSubNet(ID);
        network.addSubNet(subNetwork);

        assertEquals(network.getSubNets().size(), 1);
        network.clearSubNets();
        assertEquals(network.getSubNets().size(), 0);
    }

    /**
     * Test add the same subneto.
     *
     * @throws Exception
     */
    @Test
    public void testAddtheSameSubNet() throws Exception {

        NetworkInstance network = new NetworkInstance(NETWORK_NAME, VDC, REGION);
        network.setIdNetwork(ID);
        SubNetworkInstance subNetwork = new SubNetworkInstance(SUBNETWORK_NAME, VDC, REGION, CIDR_ID);
        subNetwork.setIdSubNet(ID);
        network.addSubNet(subNetwork);
        network.addSubNet(subNetwork);

        assertEquals(network.getSubNets().size(), 1);

    }

    /**
     * Update subNet instance.
     *
     * @throws Exception
     */
    @Test
    public void testUpdateSubNetInstance() throws Exception {

        NetworkInstance network = new NetworkInstance(NETWORK_NAME, VDC, REGION);
        SubNetworkInstance subnet = new SubNetworkInstance(SUBNETWORK_NAME, VDC, REGION, CIDR_ID);
        subnet.setId(new Long(ID));
        network.addSubNet(subnet);
        SubNetworkInstance subnet2 = new SubNetworkInstance(SUBNETWORK_NAME, VDC, REGION, CIDR_ID);
        subnet2.setId(new Long(ID));
        subnet2.setIdNetwork(ID);

        network.updateSubNet(subnet2);

        assertEquals(network.getSubNets().size(), 1);
        for (SubNetworkInstance subNet : network.getSubNets()) {
            assertEquals(subNet.getName(), SUBNETWORK_NAME);
            assertEquals(subNet.getCidr(), CIDR);
            assertEquals(subNet.getIdNetwork(), ID);
        }
    }

    /**
     * Contains test.
     *
     * @throws Exception
     */
    @Test
    public void testContainSubNetIntance() throws Exception {

        NetworkInstance network = new NetworkInstance(NETWORK_NAME, VDC, REGION);
        SubNetworkInstance subnet = new SubNetworkInstance(SUBNETWORK_NAME, VDC, REGION, CIDR_ID);
        network.addSubNet(subnet);

        SubNetworkInstance subnet2 = new SubNetworkInstance(SUBNETWORK_NAME + 2, VDC, REGION, CIDR2_ID);

        assertEquals(network.contains(subnet), true);
        assertEquals(network.contains(subnet2), false);

    }

    /**
     * Delete subNet.
     *
     * @throws Exception
     */
    @Test
    public void testDeleteSubNetInstance() throws Exception {

        NetworkInstance network = new NetworkInstance(NETWORK_NAME, VDC, REGION);
        SubNetworkInstance subnet = new SubNetworkInstance(SUBNETWORK_NAME, VDC, REGION, CIDR_ID);
        Set<SubNetworkInstance> subNets = new HashSet<SubNetworkInstance>();
        subNets.add(subnet);
        network.setSubNets(subNets);

        network.removes(subnet);
        assertEquals(network.getSubNets().size(), 0);
    }

    /**
     * Equals.
     *
     * @throws Exception
     */
    @Test
    public void testEqualsInstance() throws Exception {

        NetworkInstance network = new NetworkInstance(NETWORK_NAME, VDC, REGION);
        NetworkInstance network2 = new NetworkInstance(NETWORK_NAME + 2, VDC, REGION);
        NetworkInstance network3 = new NetworkInstance(NETWORK_NAME, VDC, REGION);
        assertEquals(network.equals(network2), false);
        assertEquals(network.equals(network3), true);
    }

    /**
     * Test the creation of port.
     * @throws Exception
     */
    @Test
    public void testPort() throws Exception {

        Port port = new Port("name", "networkId", "tenantId", "deviceOwner", "portId");
        Port port2 = new Port("name2", "networkId", "tenantId", "deviceOwner", "2");
        assertEquals(port.getName(), "name");
        assertEquals(port.getNetworkId(), "networkId");
        assertEquals(port.equals(port2), false);
    }

    /**
     * Test the attributes federated network and range from a created network when they are not specified.
     * @throws Exception
     */
    @Test
    public void testToInstance() throws Exception {
        Network network = new Network(NETWORK_NAME, VDC, REGION);
        NetworkInstance netInst = network.toNetworkInstance();
        assertEquals(netInst.getfederatedNetwork(), false);
        assertEquals(netInst.getFederatedRange(), "0");

    }


}
