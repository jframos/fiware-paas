/**
 * 
 */
package com.telefonica.euro_iaas.paasmanager.dao.sdc.impl;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.paasmanager.exception.SdcException;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.dao.impl.ChefNodeDaoRestImpl;
import com.telefonica.euro_iaas.sdc.util.MixlibAuthenticationDigester;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author jesus.movilla
 *
 */
public class ProductReleaseSdcDaoImplTest {

   
    /**
     * Tests the findAll functionality
     * @throws SdcException 
     */
    @Test
    public void testFindAll() throws SdcException {
        //given
        ProductReleaseSdcDaoImpl productReleaseSdcDaoImpl = new ProductReleaseSdcDaoImpl();
        SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        String products = "{\"product\":[{\"name\":\"tomcat\",\"description\":\"tomcat J2EE container\",\"attributes\":{\"key\":\"clave\",\"value\":\"valor\"}},{\"name\":\"nodejs\",\"description\":\"nodejs\"},{\"name\":\"mysql\",\"description\":\"mysql\"},{\"name\":\"git\",\"description\":\"git\"},{\"name\":\"mongodbshard\",\"description\":\"mongodbshard\"},{\"name\":\"mongos\",\"description\":\"mongos\"},{\"name\":\"mongodbconfig\",\"description\":\"mongodbconfig\"},{\"name\":\"contextbroker\",\"description\":\"contextbroker\"},{\"name\":\"postgresql\",\"description\":\"db manager\",\"attributes\":[{\"key\":\"username\",\"value\":\"postgres\",\"description\":\"The administrator usename\"},{\"key\":\"password\",\"value\":\"postgres\",\"description\":\"The administrator password\"}]},{\"name\":\"haproxy\",\"description\":\"balancer\",\"attributes\":[{\"key\":\"key1\",\"value\":\"value1\",\"description\":\"keyvaluedesc1\"},{\"key\":\"key2\",\"value\":\"value2\",\"description\":\"keyvaluedesc2\"},{\"key\":\"sdccoregroupid\",\"value\":\"app_server_role\",\"description\":\"idcoregroup\"}]},{\"name\":\"test\",\"description\":\"test\",\"attributes\":{\"key\":\"clave\",\"value\":\"valor\"}},{\"name\":\"mediawiki\",\"description\":\"MediaWiki Product\",\"attributes\":[{\"key\":\"wikiname\",\"value\":\"Wiki to be shown\",\"description\":\"The name of the wiki\"},{\"key\":\"path\",\"value\":\"/demo\",\"description\":\"The url context to be displayed\"}]}]}";
        String productReleasesList = "{\"productRelease\":{\"releaseNotes\":\"Tomcat server 6\",\"version\":\"6\",\"product\":{\"name\":\"tomcat\",\"description\":\"tomcat J2EE container\",\"attributes\":{\"key\":\"clave\",\"value\":\"valor\"}},\"supportedOOSS\":[{\"description\":\"Ubuntu 10.04\",\"name\":\"Ubuntu\",\"osType\":\"94\",\"version\":\"10.04\"},{\"description\":\"Debian 5\",\"name\":\"Debian\",\"osType\":\"95\",\"version\":\"5\"},{\"description\":\"Centos 2.9\",\"name\":\"Centos\",\"osType\":\"76\",\"version\":\"2.9\"}]}}\"";
        
        productReleaseSdcDaoImpl.setSystemPropertiesProvider(systemPropertiesProvider);
        
        Client client = mock(Client.class);
        ClientResponse clientResponse = mock(ClientResponse.class);
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);
        
        // when
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.SDC_SERVER_URL)).thenReturn("kk");
        when(client.resource(anyString())).thenReturn(webResource);
        when(webResource.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        //when(builder.type(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.get(ClientResponse.class)).thenReturn(clientResponse);
        when(clientResponse.getEntity(String.class)).thenReturn(products);
        

        List<ProductRelease> productReleases = productReleaseSdcDaoImpl.findAll();
        //then
        assertNotNull(productReleases);
        
    }
}
