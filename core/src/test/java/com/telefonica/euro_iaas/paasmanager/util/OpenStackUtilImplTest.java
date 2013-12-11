/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.SubNetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;

public class OpenStackUtilImplTest {

    private OpenStackUtilImplTestable openStackUtil;
    private SystemPropertiesProvider systemPropertiesProvider;
    private CloseableHttpClient closeableHttpClientMock;
    private StatusLine statusLine;
    private CloseableHttpResponse httpResponse;
    private PaasManagerUser paasManagerUser;

    private OpenStackRegion openStackRegion;
    final int TWICE = 2;
    final int SEVEN_TIMES = 7;
    final int FOUR_TIMES = 4;
    private static int http_code_accepted = 202;
    /**
     * HTTP code for accepted requests.
     */
    private static int http_code_ok = 200;
    /**
     * HTTP code for created requests.
     */
    private static int http_code_created = 201;
    /**
     * HTTP code for no content response.
     */
    private static int http_code_deleted = 204;

    @Before
    public void setUp() {
        openStackUtil = new OpenStackUtilImplTestable();
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        openStackUtil.setSystemPropertiesProvider(systemPropertiesProvider);
        GrantedAuthority grantedAuthority = mock(GrantedAuthority.class);
        Collection<GrantedAuthority> authorities = new HashSet();
        authorities.add(grantedAuthority);
        paasManagerUser = new PaasManagerUser("user", "aa", authorities);
        paasManagerUser.setToken("1234567891234567989");

        HttpClientConnectionManager httpClientConnectionManager = mock(HttpClientConnectionManager.class);
        openStackUtil.setConnectionManager(httpClientConnectionManager);

        httpResponse = mock(CloseableHttpResponse.class);
        statusLine = mock(StatusLine.class);
        closeableHttpClientMock = mock(CloseableHttpClient.class);
        openStackRegion = mock(OpenStackRegion.class);
        openStackUtil.setOpenStackRegion(openStackRegion);
    }

    @Test
    public void shouldGetAbsoluteLimitsWithResponse204() throws OpenStackException, IOException {

        // when

        when(openStackRegion.getNovaEndPoint(anyString(), anyString())).thenReturn("http://localhost/v2.0");

        when(closeableHttpClientMock.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(204);
        when(statusLine.getReasonPhrase()).thenReturn("ok");

        String response = openStackUtil.getAbsoluteLimits("region", "token", "vdc");

        // then
        assertNotNull(response);
        assertEquals("ok", response);

        verify(openStackRegion).getNovaEndPoint(anyString(), anyString());
        verify(closeableHttpClientMock).execute(any(HttpUriRequest.class));
        verify(httpResponse, times(3)).getStatusLine();
        verify(statusLine, times(2)).getStatusCode();
        verify(statusLine).getReasonPhrase();
    }

    @Test
    public void shouldLoadSubNetwork() throws OpenStackException, IOException {

        SubNetworkInstance subNet = new SubNetworkInstance("SUBNET", "CIDR");
        subNet.setIdSubNet("ID");

        // when
        when(openStackRegion.getQuantumEndPoint(anyString(), anyString())).thenReturn("http://localhost/v2.0/");
        when(closeableHttpClientMock.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(204);
        when(statusLine.getReasonPhrase()).thenReturn("ok");

        String response = openStackUtil.getSubNetworkDetails(subNet.getIdSubNet(), "region", "token", "vdc");

        // then
        assertNotNull(response);
        assertEquals("ok", response);

        verify(openStackRegion).getQuantumEndPoint(anyString(), anyString());
        verify(closeableHttpClientMock).execute(any(HttpUriRequest.class));
        verify(httpResponse, times(3)).getStatusLine();
        verify(statusLine, times(2)).getStatusCode();
        verify(statusLine).getReasonPhrase();
    }

    @Test
    public void shouldDeleteSubNetwork() throws OpenStackException, IOException {

        SubNetworkInstance subNet = new SubNetworkInstance("SUBNET", "CIDR");
        subNet.setIdSubNet("ID");

        // when
        when(openStackRegion.getNovaEndPoint(anyString(), anyString())).thenReturn("http://localhost/v2.0/");
        when(closeableHttpClientMock.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(204);
        when(statusLine.getReasonPhrase()).thenReturn("ok");

        openStackUtil.deleteSubNetwork(subNet.getIdSubNet(), "region", "token", "vdc");

        verify(openStackRegion).getQuantumEndPoint(anyString(), anyString());
        verify(closeableHttpClientMock).execute(any(HttpUriRequest.class));
        verify(httpResponse, times(3)).getStatusLine();
        verify(statusLine, times(2)).getStatusCode();
        verify(statusLine).getReasonPhrase();
    }

    @Test
    public void shouldDeleteNetwork() throws OpenStackException, IOException {

        NetworkInstance net = new NetworkInstance("NETWORK");
        net.setIdNetwork("ID");

        // when
        when(openStackRegion.getNovaEndPoint(anyString(), anyString())).thenReturn("http://localhost/v2.0/");
        when(closeableHttpClientMock.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(204);
        when(statusLine.getReasonPhrase()).thenReturn("ok");

        openStackUtil.deleteSubNetwork(net.getIdNetwork(), "region", "token", "vdc");

        verify(openStackRegion).getQuantumEndPoint(anyString(), anyString());
        verify(closeableHttpClientMock).execute(any(HttpUriRequest.class));
        verify(httpResponse, times(3)).getStatusLine();
        verify(statusLine, times(2)).getStatusCode();
        verify(statusLine).getReasonPhrase();
    }

    /**
     * It adds a network interface to a public router.
     * 
     * @throws OpenStackException
     * @throws IOException
     */
    @Test
    public void shouldAddNetworkInterfacetoPublicRouter() throws OpenStackException, IOException {
        // given
        NetworkInstance net = new NetworkInstance("NETWORK");
        SubNetworkInstance subNet = new SubNetworkInstance("SUBNET", "CIDR");
        net.addSubNet(subNet);

        String content = " <?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
                + "<access xmlns=\"http://docs.openstack.org/identity/api/v2.0\">\n"
                + "<token expires=\"2013-11-06T12:02:42Z\" id=\"e563937547fd447985db4a9567528393\">\n"
                + "<tenant enabled=\"true\" name=\"admin\" id=\"6571e3422ad84f7d828ce2f30373b3d4\">\n"
                + "<description>Default tenant</description>   \n" + "</tenant>   \n" + "</token>   \n"
                + "</access> \n";
        HttpEntity entity = mock(HttpEntity.class);
        Header header = mock(Header.class);

        // when
        when(openStackRegion.getQuantumEndPoint(anyString(), anyString())).thenReturn("http://localhost/v2.0/");
        when(closeableHttpClientMock.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getEntity()).thenReturn(entity);
        when(httpResponse.getHeaders(any(String.class))).thenReturn(new Header[] { header });
        when(header.getValue()).thenReturn("value");
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes()));
        when(statusLine.getStatusCode()).thenReturn(200);
        when(statusLine.getReasonPhrase()).thenReturn("ok");

        String response = openStackUtil.addInterfaceToPublicRouter(paasManagerUser, net, "token");

        // then
        assertNotNull(response);

        verify(openStackRegion).getQuantumEndPoint(anyString(), anyString());
        verify(systemPropertiesProvider, times(5)).getProperty(anyString());
        verify(closeableHttpClientMock, times(TWICE)).execute(any(HttpUriRequest.class));
        verify(httpResponse, times(5)).getStatusLine();
        verify(statusLine, times(5)).getStatusCode();

    }

    /**
     * It deletes a network interface to a public router.
     * 
     * @throws OpenStackException
     * @throws IOException
     */
    @Test
    public void shouldDeleteNetworkInterfacetoPublicRouter() throws OpenStackException, IOException {
        // given
        NetworkInstance net = new NetworkInstance("NETWORK");
        SubNetworkInstance subNet = new SubNetworkInstance("SUBNET", "CIDR");
        net.addSubNet(subNet);

        HttpEntity entity = mock(HttpEntity.class);
        Header header = mock(Header.class);
        String content = " <?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
                + "<access xmlns=\"http://docs.openstack.org/identity/api/v2.0\">\n"
                + "<token expires=\"2013-11-06T12:02:42Z\" id=\"e563937547fd447985db4a9567528393\">\n"
                + "<tenant enabled=\"true\" name=\"admin\" id=\"6571e3422ad84f7d828ce2f30373b3d4\">\n"
                + "<description>Default tenant</description>   \n" + "</tenant>   \n" + "</token>   \n"
                + "</access> \n";
        String region = "RegionOne";

        // when

        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.PUBLIC_ROUTER_ID)).thenReturn("ID");

        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes()));
        when(closeableHttpClientMock.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getEntity()).thenReturn(entity);
        when(httpResponse.getHeaders(any(String.class))).thenReturn(new Header[] { header });
        when(header.getValue()).thenReturn("value");

        when(statusLine.getStatusCode()).thenReturn(200);
        when(statusLine.getReasonPhrase()).thenReturn("ok");

        String response = openStackUtil.deleteInterfaceToPublicRouter(paasManagerUser, net, region);

        // then
        assertNotNull(response);

        verify(systemPropertiesProvider, times(5)).getProperty(anyString());
        verify(closeableHttpClientMock, times(TWICE)).execute(any(HttpUriRequest.class));
        verify(httpResponse, times(5)).getStatusLine();
        verify(statusLine, times(5)).getStatusCode();

    }

    /**
     * It adds a network interface to a public router.
     * 
     * @throws OpenStackException
     * @throws IOException
     */
    @Test
    public void shouldDestroyNetwork() throws OpenStackException, IOException {
        // given
        String content = " header1 \n";
        HttpEntity entity = mock(HttpEntity.class);

        // when
        when(openStackRegion.getQuantumEndPoint(anyString(), anyString())).thenReturn("http://localhost/v2.0/");

        when(closeableHttpClientMock.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(statusLine.getReasonPhrase()).thenReturn("ok");
        when(httpResponse.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes()));

        String response = openStackUtil.deleteNetwork("networkId", "region", "token", "vdc");

        // then
        assertNotNull(response);

        verify(openStackRegion).getQuantumEndPoint(anyString(), anyString());
        verify(closeableHttpClientMock).execute(any(HttpUriRequest.class));
        verify(httpResponse, times(3)).getStatusLine();
        verify(statusLine, times(3)).getStatusCode();

    }

    /**
     * It adds a network interface to a public router.
     * 
     * @throws OpenStackException
     * @throws IOException
     */
    @Test
    public void shouldListNetworks() throws OpenStackException, IOException {
        // given
        String content = "{\"networks\": [{\"status\": \"ACTIVE\", \"subnets\": [\"2b7a07f6-0b73-46a1-9327-6911c0480f49\"], \"name\": "
                + " \"dia146\", \"provider:physical_network\": null, \"admin_state_up\": true, \"tenant_id\": \"67c979f51c5b4e89b85c1f876bdffe31\", "
                + " \"provider:network_type\": \"gre\", \"router:external\": false, \"shared\": false, \"id\": \"044aecbe-3975-4318-aad2-a1232dcde47d\", "
                + " \"provider:segmentation_id\": 8}, {\"status\": \"ACTIVE\", \"subnets\": [\"e2d10e6b-33c3-400c-88d6-f905d4cd02f2\"], \"name\": \"ext-net\","
                + " \"provider:physical_network\": null, \"admin_state_up\": true, \"tenant_id\": \"08bed031f6c54c9d9b35b42aa06b51c0\", \"provider:network_type\": "
                + " \"gre\", \"router:external\": true, \"shared\": false, \"id\": \"080b5f2a-668f-45e0-be23-361c3a7d11d0\", \"provider:segmentation_id\": 1}"
                + "]}";
        HttpEntity entity = mock(HttpEntity.class);

        // when
        when(openStackRegion.getQuantumEndPoint(anyString(), anyString())).thenReturn("http://localhost/v2.0/");

        when(closeableHttpClientMock.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(statusLine.getReasonPhrase()).thenReturn("ok");
        when(httpResponse.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes()));

        String response = openStackUtil.listNetworks("region", "token", "vdc");

        // then
        assertNotNull(response);

        verify(openStackRegion).getQuantumEndPoint(anyString(), anyString());
        verify(closeableHttpClientMock).execute(any(HttpUriRequest.class));
        verify(httpResponse, times(3)).getStatusLine();
        verify(statusLine, times(3)).getStatusCode();

    }

    /**
     * OpenStackUtilImplTestable.
     * 
     * @author jesus
     */
    private class OpenStackUtilImplTestable extends OpenStackUtilImpl {

        public CloseableHttpClient getHttpClient() {

            return closeableHttpClientMock;
        }
    }
}
