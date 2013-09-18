package com.telefonica.euro_iaas.paasmanager.client.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.client.PaasManagerClient;
import com.telefonica.euro_iaas.paasmanager.client.services.EnvironmentInstanceService;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;

/**
 * Unit test suite for EnvironmentInstanceServiceImpl.
 *
 * @author Jesus M. Movilla
 *
 */
public class EnvironmentInstanceServiceImplTest {

	
	EnvironmentInstanceService environmentInstanceService;
	EnvironmentDto environmentDto;
	PaasManagerClient paasManagerClient;
	
	String BASE_URL = "http://localhost:8080/paasmanager/rest";
	String MYME_TYPE= "application/xml";
	String vdc = "VDC";
	String callback = null;
	
	@Before
    public void setUp() throws Exception {
		environmentInstanceService = mock(EnvironmentInstanceService.class);
        when(environmentInstanceService.create(
                any(String.class), any(EnvironmentDto.class), any(String.class)))
                	.thenReturn(new Task());
        
		environmentDto = new EnvironmentDto();
		
		environmentDto.setName("environmentDtoName");
		environmentDto.setEnvironmentType(new EnvironmentType(
				"EnvironmentTypeName",
				"EnvironmentTypeDescription"));
		
		OS os = new OS("osType","OSname", "OSdescription", "OSversion");
		List<OS> supportedOOSS = new ArrayList<OS>();
		supportedOOSS.add(os);
		
		List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
		ProductRelease productRelease = new ProductRelease();
		productRelease.setDescription("productReleaseDescription");
		productRelease.setName("productreleaseName");
		productRelease.setVersion("1.0");
		productRelease.setProductType(new ProductType("productTypeName",
				"productTypeDescription"));
		productRelease.setSupportedOOSS(supportedOOSS);
		
		productReleases.add(productRelease);
		
		List<Tier> tiers = new ArrayList<Tier>();
		
		Tier tier = new Tier();
		tier.setInitial_number_instances(1);
		tier.setMaximum_number_instances(1);
		tier.setMinimum_number_instances(1);
		tier.setName("tierName");
		tier.setProductReleases(productReleases);
		environmentDto.setTiers(tiers);
	}
	
	@Test
    public void testCreateWhenEverithingIsOk() throws Exception {		
		paasManagerClient = new PaasManagerClient();
		//environmentInstanceService
			//= paasManagerClient.getEnvironmentInstanceService(BASE_URL, MYME_TYPE);		
		Task task = environmentInstanceService.create(vdc, environmentDto, callback);
		
	}
}
