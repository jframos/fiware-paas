/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.model;

import junit.framework.TestCase;

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


    public static String NETWORK_NAME ="name";
    public static String SUBNETWORK_NAME ="subname";
    public static String ROUTER_NAME ="router";
    public static String ID_PUBLIC_NET ="IDPUBLIC";
    public static String CIDR ="10.100.1.0/24";
    public static String CIDR_ID ="1";
    public static String ID ="id";



    public static String SUBNETWORK_STRING = "{\"subnet\":{"
        + "      \"name\":\"" + SUBNETWORK_NAME + "\","
        + "      \"network_id\":\"" + ID + "\","
        + "      \"ip_version\":4," 
        + "      \"cidr\":\"" + CIDR + "\""
        + "   }"
        + "}";

    public static String ADDINTERFACE = "{\"subnet_id\": \"" + ID+ "\" }";
    
    public static String ROUTER_STRING = "{" +
        "    \"router\":" +
        "    {" +
        "        \"name\": \"" + ROUTER_NAME + "\"," +
        "        \"admin_state_up\": true ,"+
        "        \"external_gateway_info\" : {" +
        "             \"network_id\": \"" + ID + "\"" +
        "        }" +
        "    }" +
        "}";




    public static String NETWORK_STRING = "{" + " \"network\":{" + "    \"name\": \"" + NETWORK_NAME + "\","
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

        Network network = new Network(NETWORK_NAME);
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

        Network network = new Network(NETWORK_NAME);
   
        SubNetwork subnet = new SubNetwork(SUBNETWORK_NAME, CIDR_ID);
        network.addSubNet(subnet);
        assertEquals(network.getNetworkName(), NETWORK_NAME);
        assertEquals(network.getSubNets().size(), 1);
        for (SubNetwork subNet: network.getSubNets()) {
            assertEquals(subNet.getName(), SUBNETWORK_NAME);
            assertEquals(subNet.getCidr(), CIDR);
        }
    }

    /**
     * Create a network instance test.
     * @throws Exception
     */
    @Test
    public void testCreateInstanceNetwork() throws Exception {

        Network network = new Network(NETWORK_NAME);
        SubNetwork subNet = new SubNetwork(SUBNETWORK_NAME);
        network.addSubNet(subNet);
        NetworkInstance networkInstance = network.toNetworkInstance();
        assertEquals(networkInstance.getNetworkName(), NETWORK_NAME);
        assertEquals(networkInstance.toJson(), NETWORK_STRING);
        assertEquals(networkInstance.getSubNets().size(), 1);
        for (SubNetworkInstance subNet2: networkInstance.getSubNets()) {
            assertEquals(subNet2.getName(),
            SUBNETWORK_NAME);
        }
    }
    
    /**
     * It tests the creation of network, subnetwork and router.
     * @throws Exception
     */
    @Test
    public void testCreateNetworkAndSubNetAndRouter() throws Exception {

        Network network = new Network(NETWORK_NAME);
        SubNetwork subnet = new SubNetwork(SUBNETWORK_NAME, CIDR_ID);
        network.addSubNet(subnet);
        RouterInstance router = new RouterInstance(ID_PUBLIC_NET, ROUTER_NAME);

        router.setIdRouter(ID);
        assertEquals(network.getNetworkName(), NETWORK_NAME);
        assertEquals(network.getSubNets().size(), 1);
        for (SubNetwork subNet: network.getSubNets()) {
            assertEquals(subNet.getName(), SUBNETWORK_NAME);
            assertEquals(subNet.getCidr(), CIDR);
        }
    }

    /**
     * It tests the creation o subnetwork and its json representation.
     * @throws Exception
     */
   @Test
    public void testCreateSubNetworkInstance() throws Exception {

        NetworkInstance network = new NetworkInstance(NETWORK_NAME);
        network.setIdNetwork(ID);
        SubNetworkInstance subNetwork = new SubNetworkInstance(SUBNETWORK_NAME);
        subNetwork.setIdSubNet(ID);
        network.addSubNet(subNetwork);
        assertEquals(subNetwork.toJson(), SUBNETWORK_STRING);
        assertEquals(network.toAddInterfaceJson(), ADDINTERFACE);

    }

   /**
    * It tests the creation of a router subnetwork and its json representation.
    * @throws Exception
    */
  @Test
   public void testCreateRouter() throws Exception {

       NetworkInstance network = new NetworkInstance(NETWORK_NAME);
       network.setIdNetwork(ID);
       SubNetworkInstance subNetwork = new SubNetworkInstance(SUBNETWORK_NAME);
       subNetwork.setIdSubNet(ID);
       network.addSubNet(subNetwork);
       RouterInstance router = new RouterInstance ( ID, ROUTER_NAME);
       router.setIdRouter(ID);
       network.addRouter(router);
       assertEquals(router.toJson(), ROUTER_STRING);
       assertEquals(network.toAddInterfaceJson(), ADDINTERFACE);

   }
    /**
     * It test the dto from the Network specification.
     * @throws Exception
     */
    @Test
    public void testFromDto() throws Exception {


        NetworkDto networkDto = new NetworkDto(NETWORK_NAME);
        SubNetworkDto subNetworkDto = new SubNetworkDto(SUBNETWORK_NAME, CIDR);
        networkDto.addSubNetworkDto(subNetworkDto);
        Network net = networkDto.fromDto();

        assertEquals(net.getNetworkName(), NETWORK_NAME);
        assertEquals(net.getSubNets().size(), 1);
        for (SubNetwork subNet: net.getSubNets()) {
            assertEquals(subNet.getName(), SUBNETWORK_NAME);
            assertEquals(subNet.getCidr(), CIDR);
        }
    }

}
