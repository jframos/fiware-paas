/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.dao.sdc.impl;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.telefonica.euro_iaas.paasmanager.exception.SdcException;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

import static org.junit.Assert.assertEquals;
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
     * Tests the findAllProducts functionality
     * @throws SdcException 
     */
    @Test
    public void testFindAllProducts() throws SdcException {
        //given
        ProductReleaseSdcDaoImpl productReleaseSdcDaoImpl = new ProductReleaseSdcDaoImpl();
        SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        String jsonProducts = "{\"product\":[{\"name\":\"tomcat\",\"description\":\"tomcat J2EE container\",\"attributes\":{\"key\":\"clave\",\"value\":\"valor\"}},{\"name\":\"nodejs\",\"description\":\"nodejs\"},{\"name\":\"mysql\",\"description\":\"mysql\"},{\"name\":\"git\",\"description\":\"git\"},{\"name\":\"mongodbshard\",\"description\":\"mongodbshard\"},{\"name\":\"mongos\",\"description\":\"mongos\"},{\"name\":\"mongodbconfig\",\"description\":\"mongodbconfig\"},{\"name\":\"contextbroker\",\"description\":\"contextbroker\"},{\"name\":\"postgresql\",\"description\":\"db manager\",\"attributes\":[{\"key\":\"username\",\"value\":\"postgres\",\"description\":\"The administrator usename\"},{\"key\":\"password\",\"value\":\"postgres\",\"description\":\"The administrator password\"}]},{\"name\":\"haproxy\",\"description\":\"balancer\",\"attributes\":[{\"key\":\"key1\",\"value\":\"value1\",\"description\":\"keyvaluedesc1\"},{\"key\":\"key2\",\"value\":\"value2\",\"description\":\"keyvaluedesc2\"},{\"key\":\"sdccoregroupid\",\"value\":\"app_server_role\",\"description\":\"idcoregroup\"}]},{\"name\":\"test\",\"description\":\"test\",\"attributes\":{\"key\":\"clave\",\"value\":\"valor\"}},{\"name\":\"mediawiki\",\"description\":\"MediaWiki Product\",\"attributes\":[{\"key\":\"wikiname\",\"value\":\"Wiki to be shown\",\"description\":\"The name of the wiki\"},{\"key\":\"path\",\"value\":\"/demo\",\"description\":\"The url context to be displayed\"}]}]}";
        //String productReleasesList = "{\"productRelease\":{\"releaseNotes\":\"Tomcat server 6\",\"version\":\"6\",\"product\":{\"name\":\"tomcat\",\"description\":\"tomcat J2EE container\",\"attributes\":{\"key\":\"clave\",\"value\":\"valor\"}},\"supportedOOSS\":[{\"description\":\"Ubuntu 10.04\",\"name\":\"Ubuntu\",\"osType\":\"94\",\"version\":\"10.04\"},{\"description\":\"Debian 5\",\"name\":\"Debian\",\"osType\":\"95\",\"version\":\"5\"},{\"description\":\"Centos 2.9\",\"name\":\"Centos\",\"osType\":\"76\",\"version\":\"2.9\"}]}}\"";
        InputStream inputStream = IOUtils.toInputStream(jsonProducts);
        Client client = mock(Client.class);
                    
        productReleaseSdcDaoImpl.setSystemPropertiesProvider(systemPropertiesProvider);
        productReleaseSdcDaoImpl.setClient(client);
            
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);
        
        // when
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.SDC_SERVER_URL)).thenReturn("http://www.kk.com");
        when(client.resource(anyString())).thenReturn(webResource);
        when(webResource.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.type(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.get(InputStream.class)).thenReturn(inputStream);
        
        List<String> products = productReleaseSdcDaoImpl.findAllProducts();
        
        //then
        assertNotNull(products);
        
    }
    
    /**
     * Tests the findAllProducts functionality
     * @throws SdcException 
     */
    @Test
    public void testFindAllProductReleasesOfaProduct() throws SdcException {
        //given
        ProductReleaseSdcDaoImpl productReleaseSdcDaoImpl = new ProductReleaseSdcDaoImpl();
        SystemPropertiesProvider systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        String productReleasesList = "{\"productRelease\":{\"releaseNotes\":\"Tomcat server 6\",\"version\":\"6\",\"product\":{\"name\":\"tomcat\",\"description\":\"tomcat J2EE container\",\"attributes\":{\"key\":\"clave\",\"value\":\"valor\"}},\"supportedOOSS\":[{\"description\":\"Ubuntu 10.04\",\"name\":\"Ubuntu\",\"osType\":\"94\",\"version\":\"10.04\"},{\"description\":\"Debian 5\",\"name\":\"Debian\",\"osType\":\"95\",\"version\":\"5\"},{\"description\":\"Centos 2.9\",\"name\":\"Centos\",\"osType\":\"76\",\"version\":\"2.9\"}]}}";
        InputStream inputStream = IOUtils.toInputStream(productReleasesList);
        Client client = mock(Client.class);
                    
        productReleaseSdcDaoImpl.setSystemPropertiesProvider(systemPropertiesProvider);
        productReleaseSdcDaoImpl.setClient(client);
            
        WebResource webResource = mock(WebResource.class);
        WebResource.Builder builder = mock(WebResource.Builder.class);
        
        // when
        when(systemPropertiesProvider.getProperty(SystemPropertiesProvider.SDC_SERVER_URL)).thenReturn("http://www.kk.com");
        when(client.resource(anyString())).thenReturn(webResource);
        when(webResource.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.type(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.get(InputStream.class)).thenReturn(inputStream);
        
        List<ProductRelease> productReleases = productReleaseSdcDaoImpl.findAllProductReleasesOfProduct("tomcat");
        
        //then
        assertNotNull(productReleases);
        assertEquals(1, productReleases.size());
        
    }
}
