/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public class OpenStackRegionImplTest {

    @Test
    public void shouldGetEndPointsForNovaAndARegionName() throws OpenStackException {
        // given

        OpenStackRegionImpl openStackRegion = new OpenStackRegionImpl();
        Client client = mock(Client.class);
        openStackRegion.setClient(client);
        SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        openStackRegion.setSystemPropertiesProvider(systemPropertiesProvider);

        String regionName = "RegionOne";
        String token = "123123232";
        String responseJSON = "{\"endpoints_links\": [], \"endpoints\": [{\"name\": \"nova\", \"adminURL\": \"http://130.206.80.63:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\", \"region\": \"RegionTestbed\", \"internalURL\": \"http://130.206.80.63:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\", \"type\": \"compute\", \"id\": \"34bb28b56ce4434f8fc2b85ca16bbda6\", \"publicURL\": \"http://130.206.80.63:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\"}, {\"name\": \"quantum\", \"adminURL\": \"http://130.206.80.63:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\", \"region\": \"RegionTestbed\", \"internalURL\": \"http://130.206.80.63:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\", \"type\": \"network\", \"id\": \"06fe0f86353441cdb5b0664fe5abf0ca\", \"publicURL\": \"http://130.206.80.63:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\"}, {\"name\": \"nova\", \"adminURL\": \"http://130.206.80.58:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\", \"region\": \"RegionOne\", \"internalURL\": \"http://130.206.80.58:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\", \"type\": \"compute\", \"id\": \"0a3419563dcd4e02b8cd8c865a3bc2ed\", \"publicURL\": \"http://130.206.80.58:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\"}, {\"name\": \"quantum\", \"adminURL\": \"http://130.206.80.58:9696/\", \"region\": \"RegionOne\", \"internalURL\": \"http://130.206.80.58:9696/\", \"type\": \"network\", \"id\": \"49d2f93d15904ff091f45b7752001057\", \"publicURL\": \"http://130.206.80.58:9696/\"}, {\"name\": \"glance\", \"adminURL\": \"http://130.206.80.58:9292/v2\", \"region\": \"RegionOne\", \"internalURL\": \"http://130.206.80.58:9292/v2\", \"type\": \"image\", \"id\": \"24b9ab9c337b4ee38548a8f2e73d291b\", \"publicURL\": \"http://130.206.80.58:9292/v2\"}, {\"name\": \"cinder\", \"adminURL\": \"http://130.206.80.58:8776/v1/67c979f51c5b4e89b85c1f876bdffe31\", \"region\": \"RegionOne\", \"internalURL\": \"http://130.206.80.58:8776/v1/67c979f51c5b4e89b85c1f876bdffe31\", \"type\": \"volume\", \"id\": \"4fd6f49485cc443d91d4e8b12b0518c5\", \"publicURL\": \"http://130.206.80.58:8776/v1/67c979f51c5b4e89b85c1f876bdffe31\"}, {\"name\": \"ec2\", \"adminURL\": \"http://130.206.80.58:8773/services/Admin\", \"region\": \"RegionOne\", \"internalURL\": \"http://130.206.80.58:8773/services/Cloud\", \"type\": \"ec2\", \"id\": \"0ca7cd26fad842e0b06a29f2c627fa43\", \"publicURL\": \"http://130.206.80.58:8773/services/Cloud\"}, {\"name\": \"keystone\", \"adminURL\": \"http://130.206.80.58:35357/v2.0\", \"region\": \"RegionOne\", \"internalURL\": \"http://130.206.80.58:5000/v2.0\", \"type\": \"identity\", \"id\": \"43c29c831357416c87f9068d95c73eca\", \"publicURL\": \"http://130.206.80.58:5000/v2.0\"}]}";
        String url = "http://domain.com/v2.0/tokens/" + token + "/endpoints";

        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);

        ClientResponse clientResponse = mock(ClientResponse.class);

        // when
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_URL)).thenReturn(
                "http://domain.com/v2.0/");
        when(client.resource(url)).thenReturn(webResource);
        when(webResource.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.get(ClientResponse.class)).thenReturn(clientResponse);
        when(clientResponse.getStatus()).thenReturn(200);
        when(clientResponse.getEntity(String.class)).thenReturn(responseJSON);

        String resultURL = openStackRegion.getNovaEndPoint(regionName, token);
        // then
        assertNotNull(resultURL);
        assertEquals("http://130.206.80.58:8774/v2/", resultURL);
    }

    @Test
    public void shouldGetEndPointsForQuantumAndARegionName() throws OpenStackException {
        // given

        OpenStackRegionImpl openStackRegion = new OpenStackRegionImpl();
        Client client = mock(Client.class);
        openStackRegion.setClient(client);
        SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        openStackRegion.setSystemPropertiesProvider(systemPropertiesProvider);

        String regionName = "RegionOne";
        String token = "123123232";
        String responseJSON = "{\"endpoints_links\": [], \"endpoints\": [{\"name\": \"nova\", \"adminURL\": \"http://130.206.80.63:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\", \"region\": \"RegionTestbed\", \"internalURL\": \"http://130.206.80.63:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\", \"type\": \"compute\", \"id\": \"34bb28b56ce4434f8fc2b85ca16bbda6\", \"publicURL\": \"http://130.206.80.63:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\"}, {\"name\": \"quantum\", \"adminURL\": \"http://130.206.80.63:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\", \"region\": \"RegionTestbed\", \"internalURL\": \"http://130.206.80.63:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\", \"type\": \"network\", \"id\": \"06fe0f86353441cdb5b0664fe5abf0ca\", \"publicURL\": \"http://130.206.80.63:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\"}, {\"name\": \"nova\", \"adminURL\": \"http://130.206.80.58:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\", \"region\": \"RegionOne\", \"internalURL\": \"http://130.206.80.58:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\", \"type\": \"compute\", \"id\": \"0a3419563dcd4e02b8cd8c865a3bc2ed\", \"publicURL\": \"http://130.206.80.58:8774/v2/67c979f51c5b4e89b85c1f876bdffe31\"}, {\"name\": \"quantum\", \"adminURL\": \"http://130.206.80.58:9696/\", \"region\": \"RegionOne\", \"internalURL\": \"http://130.206.80.58:9696/\", \"type\": \"network\", \"id\": \"49d2f93d15904ff091f45b7752001057\", \"publicURL\": \"http://130.206.80.58:9696/\"}, {\"name\": \"glance\", \"adminURL\": \"http://130.206.80.58:9292/v2\", \"region\": \"RegionOne\", \"internalURL\": \"http://130.206.80.58:9292/v2\", \"type\": \"image\", \"id\": \"24b9ab9c337b4ee38548a8f2e73d291b\", \"publicURL\": \"http://130.206.80.58:9292/v2\"}, {\"name\": \"cinder\", \"adminURL\": \"http://130.206.80.58:8776/v1/67c979f51c5b4e89b85c1f876bdffe31\", \"region\": \"RegionOne\", \"internalURL\": \"http://130.206.80.58:8776/v1/67c979f51c5b4e89b85c1f876bdffe31\", \"type\": \"volume\", \"id\": \"4fd6f49485cc443d91d4e8b12b0518c5\", \"publicURL\": \"http://130.206.80.58:8776/v1/67c979f51c5b4e89b85c1f876bdffe31\"}, {\"name\": \"ec2\", \"adminURL\": \"http://130.206.80.58:8773/services/Admin\", \"region\": \"RegionOne\", \"internalURL\": \"http://130.206.80.58:8773/services/Cloud\", \"type\": \"ec2\", \"id\": \"0ca7cd26fad842e0b06a29f2c627fa43\", \"publicURL\": \"http://130.206.80.58:8773/services/Cloud\"}, {\"name\": \"keystone\", \"adminURL\": \"http://130.206.80.58:35357/v2.0\", \"region\": \"RegionOne\", \"internalURL\": \"http://130.206.80.58:5000/v2.0\", \"type\": \"identity\", \"id\": \"43c29c831357416c87f9068d95c73eca\", \"publicURL\": \"http://130.206.80.58:5000/v2.0\"}]}";
        String url = "http://domain.com/v2.0/tokens/" + token + "/endpoints";

        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);

        ClientResponse clientResponse = mock(ClientResponse.class);

        // when
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.KEYSTONE_URL)).thenReturn(
                "http://domain.com/v2.0/");
        when(client.resource(url)).thenReturn(webResource);
        when(webResource.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.get(ClientResponse.class)).thenReturn(clientResponse);
        when(clientResponse.getStatus()).thenReturn(200);
        when(clientResponse.getEntity(String.class)).thenReturn(responseJSON);

        String resultURL = openStackRegion.getQuantumEndPoint(regionName, token);
        // then
        assertNotNull(resultURL);
        assertEquals("http://130.206.80.58:9696/", resultURL);
    }
}
