package com.telefonica.euro_iaas.paasmanager.util;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.SDC_SERVER_URL;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.SDC_SERVER_MEDIATYPE;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.SDC_SERVER_URL;


import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductInstanceSearchCriteria;

import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.client.services.impl.ProductInstanceServiceImpl;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.sdc.model.Task;

/**
 * Unit test suite for ProductInstallatorSdcImpl.
 *
 * @author Jesus M. Movilla
 *
 */
public class ProductInstallatorSdcImplTest {

    private SystemPropertiesProvider systemPropertiesProvider;
    private SDCClient sdcClient;
    private ProductInstanceService service;
    
    private ProductInstance expectedProductInstance;
    private ProductRelease productRelease;
    private OS os;
    private Attribute attribute;
    private VM host = new VM("fqn","ip","hostname", "domain");
    private Task value;
    
    @Before
    public void setUp() throws Exception {
    	systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        when(systemPropertiesProvider.getProperty(SDC_SERVER_URL))
        	.thenReturn("url");
        when(systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE))
    		.thenReturn("MediaType");
 
        value = mock(Task.class);
        service = mock(ProductInstanceService.class);
        when(service.install(Mockito.anyString(), 
        	Mockito.any(ProductInstanceDto.class), Mockito.anyString())).thenReturn(value);
        
        sdcClient = mock(SDCClient.class);
        when(sdcClient.getProductInstanceService(Mockito.anyString(), 
        		Mockito.anyString())).thenReturn(service);
         
        os = new OS("os1", "1", "os1 description", "v1");
        host.setOsType(os.getOsType());
	    
        attribute = new Attribute("key", "value");
        
        ProductType productType = new ProductType ("type A", "Type A desc");
        
        productRelease = new ProductRelease(
                "productPrueba", "1.0", "Product Prueba desc", 
                Arrays.asList(attribute),
	            null, Arrays.asList(os), true, productType);
        
        expectedProductInstance = new ProductInstance(
        		productRelease, Status.INSTALLED, host, "vdc");
        
        expectedProductInstance.setPrivateAttributes(Arrays.asList(attribute));
   }

	@Test
	public void testInstallWhenEverithingIsOk() throws Exception {
		ProductInstallatorSdcImpl installator = new ProductInstallatorSdcImpl();
		installator.setSDCClient(sdcClient);
		installator.setSystemPropertiesProvider(systemPropertiesProvider);

	    ProductInstance installedProduct = installator.install(expectedProductInstance);
	    // make verifications
	    assertEquals(expectedProductInstance, installedProduct);

	    verify(systemPropertiesProvider, times(1)).getProperty(SDC_SERVER_URL);
	    verify(systemPropertiesProvider, times(1)).getProperty(SDC_SERVER_MEDIATYPE);    
	    
	    // only one product will be installed, the other one causes error.
	    //verify(productInstanceDao, times(1)).update(any(ProductInstance.class));
	    //verify(productInstanceDao, times(1)).findUniqueByCriteria(
	    		//any(ProductInstanceSearchCriteria.class));
	}
}
