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

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;

public class OpenStackUtilImplTest {

    private CloseableHttpClient closeableHttpClientMock;

    @Before
    public void setUp() {
        closeableHttpClientMock = mock(CloseableHttpClient.class);
    }

    @Test
    public void shouldGetAbsoluteLimitsWithResponse204() throws OpenStackException, IOException {
        // given
        OpenStackUtilImplTestable openStackUtil = new OpenStackUtilImplTestable();
        SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        openStackUtil.setSystemPropertiesProvider(systemPropertiesProvider);
        Collection<GrantedAuthority> authorities = new HashSet();
        PaasManagerUser paasManagerUser = new PaasManagerUser("username", "password", authorities);
        paasManagerUser.setTenantId("tenantId");
        HttpClientConnectionManager httpClientConnectionManager = mock(HttpClientConnectionManager.class);
        openStackUtil.setConnectionManager(httpClientConnectionManager);

        CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        // when
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.URL_NOVA_PROPERTY)).thenReturn(
                "http://localhost/");
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.VERSION_PROPERTY)).thenReturn("v2/");

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

    private class OpenStackUtilImplTestable extends OpenStackUtilImpl {

        public CloseableHttpClient getHttpClient() {

            return closeableHttpClientMock;
        }
    }
}
