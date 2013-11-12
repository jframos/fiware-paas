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
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.SubNetwork;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;

public class OpenStackUtilImplTest {

    private OpenStackUtilImplTestable openStackUtil;
    private SystemPropertiesProvider systemPropertiesProvider;
    private CloseableHttpClient closeableHttpClientMock;
    private StatusLine statusLine;
    private CloseableHttpResponse httpResponse;
    private PaasManagerUser paasManagerUser;
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
    	openStackUtil =
        	new OpenStackUtilImplTestable();
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        openStackUtil.setSystemPropertiesProvider(systemPropertiesProvider);
        Collection<GrantedAuthority> authorities = new HashSet();
        paasManagerUser = new PaasManagerUser("username", "password", authorities);
        paasManagerUser.setTenantId("tenantId");
        HttpClientConnectionManager httpClientConnectionManager =
        	mock(HttpClientConnectionManager.class);
        openStackUtil.setConnectionManager(httpClientConnectionManager);

        httpResponse = mock(CloseableHttpResponse.class);
        statusLine = mock(StatusLine.class);
        closeableHttpClientMock = mock(CloseableHttpClient.class);
    }

    @Test
    public void shouldGetAbsoluteLimitsWithResponse204() throws OpenStackException, IOException {


        // when
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_NOVA_PROPERTY)).
            thenReturn("http://localhost/");
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.VERSION_PROPERTY)).
            thenReturn("v2/");
        when(closeableHttpClientMock.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(204);
        when(statusLine.getReasonPhrase()).thenReturn("ok");

        String response = openStackUtil.getAbsoluteLimits(paasManagerUser);

        // then
        assertNotNull(response);
        assertEquals("ok", response);

        verify(systemPropertiesProvider, times(2)).getProperty(anyString());
        verify(closeableHttpClientMock).execute(any(HttpUriRequest.class));
        verify(httpResponse, times(2)).getStatusLine();
        verify(statusLine).getStatusCode();
        verify(statusLine).getReasonPhrase();
    }
    
    /**
     * It adds a network interface to a public router.
     * @throws OpenStackException
     * @throws IOException
     */
    @Test
    public void shouldAddNetworkInterfacetoPublicRouter()
        throws OpenStackException, IOException {
    	// given
    	Network net = new Network("NETWORK");
    	SubNetwork subNet = new SubNetwork("SUBNET", "CIDR");
    	net.addSubNet(subNet);
    	
    	String content = " <?xml version=\"1.0\" encoding=\"UTF-8\"?> \n"
            + "<access xmlns=\"http://docs.openstack.org/identity/api/v2.0\">\n"
            + "<token expires=\"2013-11-06T12:02:42Z\" id=\"e563937547fd447985db4a9567528393\">\n"
            + "<tenant enabled=\"true\" name=\"admin\" id=\"6571e3422ad84f7d828ce2f30373b3d4\">\n"
            + "<description>Default tenant</description>   \n"
            + "</tenant>   \n"
            + "</token>   \n"
            + "</access> \n";
    	HttpEntity entity = mock(HttpEntity.class);
    	Header header = mock(Header.class);



        // when
        when(systemPropertiesProvider
            .getProperty(SystemPropertiesProvider.URL_NOVA_PROPERTY)).
            thenReturn("http://localhost/");
        when(systemPropertiesProvider.
        	getProperty(SystemPropertiesProvider.VERSION_PROPERTY)).thenReturn("v2/");

        when(closeableHttpClientMock.execute(any(HttpUriRequest.class))).
            thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpResponse.getEntity()).thenReturn(entity);
        when(httpResponse.getHeaders(any(String.class))).
            thenReturn(new Header []{header});
        when(header.getValue()).thenReturn("value");
        when(entity.getContent()).
            thenReturn(new ByteArrayInputStream(content.getBytes()));
        when(statusLine.getStatusCode()).thenReturn(200);
        when(statusLine.getReasonPhrase()).thenReturn("ok");

        String response = openStackUtil.
            addInterfaceToPublicRouter(paasManagerUser, net);

        // then
        assertNotNull(response);

        verify(systemPropertiesProvider, times(SEVEN_TIMES)).getProperty(anyString());
        verify(closeableHttpClientMock, times(TWICE)).execute(any(HttpUriRequest.class));
        verify(httpResponse, times(FOUR_TIMES)).getStatusLine();
        verify(statusLine, times(FOUR_TIMES)).getStatusCode();

    }

    /**
     * It adds a network interface to a public router.
     * @throws OpenStackException
     * @throws IOException
     */
    @Test
    public void shouldDestroyNetwork()
        throws OpenStackException, IOException {
    	// given
    	String content = " header1 \n";
    	HttpEntity entity = mock(HttpEntity.class);

        // when
        when(systemPropertiesProvider
            .getProperty(SystemPropertiesProvider.URL_NOVA_PROPERTY)).
            thenReturn("http://localhost/");
        when(systemPropertiesProvider
            .getProperty(SystemPropertiesProvider.VERSION_PROPERTY)).
            thenReturn("v2/");

        when(closeableHttpClientMock.execute(any(HttpUriRequest.class))).
            thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(statusLine.getReasonPhrase()).thenReturn("ok");
        when(httpResponse.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(
            new ByteArrayInputStream(content.getBytes()));

        String response = openStackUtil.
            deleteNetwork("networkId", paasManagerUser);

        // then
        assertNotNull(response);

        verify(systemPropertiesProvider, times(TWICE)).getProperty(anyString());
        verify(closeableHttpClientMock).execute(any(HttpUriRequest.class));
        verify(httpResponse, times(TWICE)).getStatusLine();
        verify(statusLine, times(TWICE)).getStatusCode();

    }
    
    /**
     * OpenStackUtilImplTestable.
     * @author jesus
     *
     */
    private class OpenStackUtilImplTestable extends OpenStackUtilImpl {

        public CloseableHttpClient getHttpClient() {

            return closeableHttpClientMock;
        }
    }
}
