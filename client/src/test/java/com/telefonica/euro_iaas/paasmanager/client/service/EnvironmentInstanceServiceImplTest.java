package com.telefonica.euro_iaas.paasmanager.client.service;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.paasmanager.client.PaasManagerClient;
import com.telefonica.euro_iaas.paasmanager.client.services.EnvironmentInstanceService;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.Task;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import org.junit.Before;
import org.junit.Test;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test suite for EnvironmentInstanceServiceImpl.
 * 
 * @author Jesus M. Movilla
 */
public class EnvironmentInstanceServiceImplTest {

    EnvironmentInstanceService environmentInstanceService;
    EnvironmentDto environmentDto;
    PaasManagerClient paasManagerClient;

    String BASE_URL = "http://localhost:8080/paasmanager/rest";
    String MYME_TYPE = "application/xml";
    String vdc = "VDC";
    String org = "ORG";
    String callback = null;

    @Before
    public void setUp() throws Exception {
        environmentInstanceService = mock(EnvironmentInstanceService.class);
        when(
                environmentInstanceService.create(any(String.class), any(String.class), any(EnvironmentDto.class),
                        any(String.class))).thenReturn(new Task());

        environmentDto = new EnvironmentDto();

        environmentDto.setName("environmentDtoName");
        environmentDto.setEnvironmentType(new EnvironmentType("EnvironmentTypeName", "EnvironmentTypeDescription"));

        OS os = new OS("osType", "OSname", "OSdescription", "OSversion");
        List<OS> supportedOOSS = new ArrayList<OS>();
        supportedOOSS.add(os);

        List<ProductReleaseDto> productReleaseDtos = new ArrayList<ProductReleaseDto>();
        ProductReleaseDto productReleaseDto = new ProductReleaseDto();
        productReleaseDto.setProductDescription("productReleaseDescription");
        productReleaseDto.setProductName("productreleaseName");
        productReleaseDto.setVersion("1.0");
        // productReleaseDto.setProductType(new ProductType("productTypeName",
        // "productTypeDescription"));
        // productReleaseDto.setSupportedOS(supportedOOSS);

        productReleaseDtos.add(productReleaseDto);

        List<TierDto> tierDtos = new ArrayList<TierDto>();

        TierDto tierDto = new TierDto();
        tierDto.setInitialNumberInstances(1);
        tierDto.setMaximumNumberInstances(1);
        tierDto.setMinimumNumberInstances(1);
        tierDto.setName("tierName");
        tierDto.setProductReleaseDtos(productReleaseDtos);
        environmentDto.setTierDtos(tierDtos);
    }

    @Test
    public void testCreateWhenEverithingIsOk() throws Exception {
        paasManagerClient = new PaasManagerClient();
        // environmentInstanceService
        // = paasManagerClient.getEnvironmentInstanceService(BASE_URL, MYME_TYPE);
        Task task = environmentInstanceService.create(org, vdc, environmentDto, callback);

    }
}
