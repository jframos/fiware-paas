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

package com.telefonica.euro_iaas.paasmanager.rest.auth;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.rest.exception.AuthenticationConnectionException;

public class OpenStackAuthenticationTokenTest {

    @Test
    public void getCredentialsTest() throws AuthenticationConnectionException, IOException {

        OpenStackAuthenticationToken openStackAuthenticationToken;
        ArrayList<Object> params = new ArrayList<Object>();
        HttpClient httpClient;
        HttpResponse response;
        StatusLine statusLine;
        HttpEntity httpEntity;
        InputStream is;

        String payload = "<access xmlns=\"http://docs.openstack.org/identity/api/v2.0\"><token "
                + "expires=\"2015-07-09T15:16:07Z\" id=\"35b208abaf09707c5fed8e54af9a48b8\"><tenant "
                + "enabled=\"true\" id=\"00000000000000000000000000000001\" name=\"00000000000000000000000000000001\"/>"
                + "</token><serviceCatalog><endpoints><adminURL>http://130.206.80.58:8774/v2/undefined</adminURL>"
                + "<region>Trento</region><internalURL>http://130.206.80.58:8774/v2/undefined</internalURL>";

        httpClient = mock(HttpClient.class);
        response = mock(HttpResponse.class);
        statusLine = mock(StatusLine.class);
        httpEntity = mock(HttpEntity.class);
        is = IOUtils.toInputStream(payload, "UTF-8");

        params.add("url");
        params.add("tenant");
        params.add("user");
        params.add("passw");
        params.add(httpClient);
        params.add(new Long(3));

        openStackAuthenticationToken = new OpenStackAuthenticationToken(params);

        Header header = new Header() {

            @Override
            public String getValue() {
                return "Fri, 21 Nov 2014 12:30:54 GMT";
            }

            @Override
            public String getName() {
                return "Date";
            }

            @Override
            public HeaderElement[] getElements() {
                // TODO Auto-generated method stub
                return null;
            }
        };
        Header[] headers = new Header[] { header };

        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(is);
        when(httpClient.execute(any(HttpPost.class))).thenReturn(response);
        when(response.getHeaders(anyString())).thenReturn(headers);

        openStackAuthenticationToken.getCredentials();

        verify(httpClient, times(1)).execute(any(HttpPost.class));

    }

    @Test
    public void shouldExtractAndValidateData() throws ParseException {
        // given
        String keystoneURL = "http://localhost:4731/v2.0/";
        String adminUser = "admin";
        String adminPass = "aaaaaaaa";
        String adminTenant = "00000000000000000000000000001";
        Long threshold = 84000000L;

        ArrayList<Object> params = new ArrayList();
        params.add(keystoneURL);
        params.add(adminTenant);
        params.add(adminUser);
        params.add(adminPass);
        HttpClient httpClient = mock(HttpClient.class);
        params.add(httpClient);
        params.add(threshold);

        OpenStackAuthenticationToken openStackAuthenticationToken = new OpenStackAuthenticationToken(params);
        String response1 = "<access xmlns=\"http://docs.openstack.org/identity/api/v2.0\">\n"
                + "  <token expires=\"2015-07-09T15:16:07Z\" id=\"252f966e16b2514aa521fa885f0f4d03\">\n"
                + "    <tenant enabled=\"true\" id=\"00000000000000000000000000000001\" name=\"tenantname\" />\n"
                + "  </token>\n" + "  <user username=\"admin\" id=\"admin\" name=\"admin\">\n" + "    <roles_links/>\n"
                + "    <roles name=\"Member\" id=\"8db87ccbca3b4d1ba4814c3bb0d63aaf\"></roles>\n"
                + "    <roles name=\"admin\" id=\"09e95db0ea3f4495a64e95bfc64b0c56\"></roles>\n" + "  </user>\n"
                + "</access>";
        String response2 = "Tue Dec 10 12:46:50 CET 2014";

        String dateString = "Tue Dec 10 12:45:50 CET 2014";
        DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss z YYYY", Locale.ENGLISH);
        Date response3 = format.parse(dateString);

        ArrayList<Object> response = new ArrayList<>();
        response.add(response1);
        response.add(response2);
        response.add(response3);

        // when
        openStackAuthenticationToken.extractData(response);

        // then
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionInExtractDataWithTokenUnknownFormat() {
        // given
        String keystoneURL = "http://localhost:4731/v2.0/";
        String adminUser = "admin";
        String adminPass = "ddddddd";
        String adminTenant = "00000000000000000000000000001";
        Long threshold = 84000000L;

        ArrayList<Object> params = new ArrayList();
        params.add(keystoneURL);
        params.add(adminTenant);
        params.add(adminUser);
        params.add(adminPass);
        HttpClient httpClient = mock(HttpClient.class);
        params.add(httpClient);
        params.add(threshold);

        OpenStackAuthenticationToken openStackAuthenticationToken = new OpenStackAuthenticationToken(params);
        String response1 = "<access xmlns=\"http://docs.openstack.org/identity/api/v2.0\">\n"
                + "  <token expires=\"2015-07-09T15:16:07Z\">\n"
                + "    <tenant enabled=\"true\" id=\"00000000000000000000000000000001\" name=\"tenantname\" />\n"
                + "  </token>\n" + "  <user username=\"admin\" id=\"admin\" name=\"admin\">\n" + "    <roles_links/>\n"
                + "    <roles name=\"Member\" id=\"8db87ccbca3b4d1ba4814c3bb0d63aaf\"></roles>\n"
                + "    <roles name=\"admin\" id=\"09e95db0ea3f4495a64e95bfc64b0c56\"></roles>\n" + "  </user>\n"
                + "</access>";

        ArrayList<Object> response = new ArrayList<>();
        response.add(response1);

        // when
        openStackAuthenticationToken.extractData(response);

        // then
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionInExtractDataWithTenantUnknownFormat() {
        // given
        String keystoneURL = "http://localhost:4731/v2.0/";
        String adminUser = "admin";
        String adminPass = "ffffffff";
        String adminTenant = "00000000000000000000000000001";
        Long threshold = 84000000L;

        ArrayList<Object> params = new ArrayList();
        params.add(keystoneURL);
        params.add(adminTenant);
        params.add(adminUser);
        params.add(adminPass);
        HttpClient httpClient = mock(HttpClient.class);
        params.add(httpClient);
        params.add(threshold);

        OpenStackAuthenticationToken openStackAuthenticationToken = new OpenStackAuthenticationToken(params);
        String response1 = "<access xmlns=\"http://docs.openstack.org/identity/api/v2.0\">\n"
                + "  <token expires=\"2015-07-09T15:16:07Z\" id=\"252f966e16b2514aa521fa885f0f4d03\">"
                + "    <tenant enabled=\"true\" idddddd=\"00000000000000000000000000000001\" name=\"tenantname\" />\n"
                + "  </token>\n" + "  <user username=\"admin\" id=\"admin\" name=\"admin\">\n" + "    <roles_links/>\n"
                + "    <roles name=\"Member\" id=\"8db87ccbca3b4d1ba4814c3bb0d63aaf\"></roles>\n"
                + "    <roles name=\"admin\" id=\"09e95db0ea3f4495a64e95bfc64b0c56\"></roles>\n" + "  </user>\n"
                + "</access>";

        ArrayList<Object> response = new ArrayList<>();
        response.add(response1);

        // when
        openStackAuthenticationToken.extractData(response);

        // then
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionInGetCredentialsWithErrorInConnection() throws IOException {
        // given
        String keystoneURL = "http://localhost:4731/v2.0/";
        String adminUser = "admin";
        String adminPass = "kkkkkkk";
        String adminTenant = "00000000000000000000000000001";
        Long threshold = 84000000L;

        ArrayList<Object> params = new ArrayList();
        params.add(keystoneURL);
        params.add(adminTenant);
        params.add(adminUser);
        params.add(adminPass);
        HttpClient httpClient = mock(HttpClient.class);
        params.add(httpClient);
        params.add(threshold);

        OpenStackAuthenticationToken openStackAuthenticationToken = new OpenStackAuthenticationToken(params);
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);
        StatusLine statusLine = mock(StatusLine.class);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(503);
        // when

        String[] credentials = openStackAuthenticationToken.getCredentials();

        // then
    }

    @Test
    public void shouldGetCredentialsWith200afterRequestToken() throws IOException {
        // given
        String keystoneURL = "http://localhost:4731/v2.0/";
        String adminUser = "admin";
        String adminPass = "kkkkkkk";
        String adminTenant = "00000000000000000000000000001";
        Long threshold = 84000000L;

        ArrayList<Object> params = new ArrayList();
        params.add(keystoneURL);
        params.add(adminTenant);
        params.add(adminUser);
        params.add(adminPass);
        HttpClient httpClient = mock(HttpClient.class);
        params.add(httpClient);
        params.add(threshold);

        OpenStackAuthenticationToken openStackAuthenticationToken = new OpenStackAuthenticationToken(params);
        HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);
        StatusLine statusLine = mock(StatusLine.class);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        HttpEntity httpEntity = mock(HttpEntity.class);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        String responseString = "<access xmlns=\"http://docs.openstack.org/identity/api/v2.0\">\n"
                + "  <token expires=\"2015-07-09T15:16:07Z\" id=\"884753f170f3b2596315fd63b5d358aa\">\n"
                + "    <tenant enabled=\"true\" id=\"00000000000000000000000000000193\" name=\"jesuspg\" />\n"
                + "  </token>\n" + "  <user username=\"jesuspg\" id=\"jesuspg\" name=\"jesuspg\">\n"
                + "    <roles_links/>\n"
                + "    <roles name=\"Member\" id=\"8db87ccbca3b4d1ba4814c3bb0d63aab\"></roles>\n" + "  </user>\n"
                + "</access>";

        InputStream inputStream = new ByteArrayInputStream(responseString.getBytes(StandardCharsets.UTF_8));

        when(httpEntity.getContent()).thenReturn(inputStream);

        Header header1 = mock(Header.class);
        when(header1.getValue()).thenReturn("Fri, 28 Nov 2014 12:57:35 GMT");
        Header[] headerDate = new Header[1];
        headerDate[0] = header1;
        when(httpResponse.getHeaders("Date")).thenReturn(headerDate);
        // when

        String[] credentials = openStackAuthenticationToken.getCredentials();

        // then
        assertNotNull(credentials);
        assertEquals("884753f170f3b2596315fd63b5d358aa", credentials[0]);
        assertEquals("00000000000000000000000000000193", credentials[1]);
    }
}
