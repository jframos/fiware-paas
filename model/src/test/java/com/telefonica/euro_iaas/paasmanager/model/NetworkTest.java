package com.telefonica.euro_iaas.paasmanager.model;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.model.dto.NetworkDto;

/**
 * Network Test.
 * @author henar
 *
 */

public class NetworkTest extends TestCase {

    public static String NETWORK_NAME ="name";
    public static String SUBNETWORK_NAME ="subname";
    public static String CIDR ="10.100.1.0/24";

    public static String NETWORK_STRING = "{"
        + " \"network\":{"
        + "    \"name\": \"" + NETWORK_NAME + "\","
        + "    \"admin_state_up\": false,"
        + "    \"shared\": false"
        + "  }"
        + "}";


    @Override
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Create network test.
     * @throws Exception
     */

    @Test
    public void testCreateNetwork() throws Exception {

        Network network = new Network(NETWORK_NAME);
        assertEquals(network.getNetworkName(), NETWORK_NAME);
        assertEquals(network.getSubNets().size(), 0);
        assertEquals(network.toJson(), NETWORK_STRING);

    }

    /**
     * Create network and subnet test.
     * @throws Exception
     */
    @Test
    public void testCreateNetworkAndSubNet() throws Exception {

        Network network = new Network(NETWORK_NAME);
        SubNetwork subnet = new SubNetwork(SUBNETWORK_NAME, "" + network.getSubNetCounts());
        network.addSubNet(subnet);
        assertEquals(network.getNetworkName(), NETWORK_NAME);
        assertEquals(network.getSubNets().size(), 1);
        assertEquals(network.getSubNets().get(0).getName(), SUBNETWORK_NAME);
        assertEquals(network.getSubNets().get(0).getCidr(), CIDR);
    }

    @Test
    public void testFromDto() throws Exception {

        NetworkDto networkDto = new NetworkDto (NETWORK_NAME, SUBNETWORK_NAME);
        Network net = networkDto.fromDto();

        assertEquals(net.getNetworkName(), NETWORK_NAME);
        assertEquals(net.getSubNets().size(), 1);
        assertEquals(net.getSubNets().get(0).getName(), SUBNETWORK_NAME);
        assertEquals(net.getSubNets().get(0).getCidr(), CIDR);
    }

}
