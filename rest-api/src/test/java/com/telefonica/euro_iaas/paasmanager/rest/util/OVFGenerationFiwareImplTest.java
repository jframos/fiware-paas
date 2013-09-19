/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.rest.util;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;

/**
 * @author jesus.movilla
 *
 */
public class OVFGenerationFiwareImplTest {

	SystemPropertiesProvider systemPropertiesProvider;
	
	EnvironmentInstanceDto environmentInstanceDto;
	TierInstanceDto tierInstanceDto;
	
	List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
	List<TierInstanceDto> tierInstancesDto = new ArrayList<TierInstanceDto>();
	
	ProductInstanceDto pInstance1, pInstance2;
	TierInstanceDto tierInstanceDto1, tierInstanceDto2;
	
	ProductReleaseDto pReleaseDto1, pReleaseDto2;
	
	@Before
	public void setUp() throws Exception {
		
		systemPropertiesProvider = mock(SystemPropertiesProvider.class);
		when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn(
				"D:\\TID\\desarrollo\\paas-manager-server\\core\\src\\main\\resources\\");
		
		Attribute attr1 = new Attribute ("key1", "value1");
		Attribute attr2 = new Attribute ("key2", "value2");
		Attribute attr3 = new Attribute ("key3", "value3");
		
		List<Attribute> privateAttributes1 = new ArrayList<Attribute>();
		privateAttributes1.add(attr1);
		privateAttributes1.add(attr2);
		

		List<Attribute> privateAttributes2 = new ArrayList<Attribute>();
		privateAttributes2.add(attr2);
		privateAttributes2.add(attr3);
		
		pReleaseDto1 = new ProductReleaseDto ();
		pReleaseDto1.setProductName("nameTest");
		pReleaseDto1.setVersion("versionTest");
		
		pReleaseDto2 = new ProductReleaseDto ();
		pReleaseDto2.setProductName("nameTest");
		pReleaseDto2.setVersion("versionTest");
		
		pInstance1 = new ProductInstanceDto(pReleaseDto1); 
		pInstance1.setAttributes(privateAttributes1);
		pInstance1.setName("pInstanceName1");
		
		pInstance2 = new ProductInstanceDto(pReleaseDto2); 
		pInstance2.setName("pInstanceName2");
		pInstance2.setAttributes(privateAttributes2);
		
		List<ProductInstanceDto> pInstances1 = new ArrayList<ProductInstanceDto>();
		pInstances1.add(pInstance1);
		
		List<ProductInstanceDto> pInstances2 = new ArrayList<ProductInstanceDto>();
		pInstances2.add(pInstance2);
		
		tierInstanceDto1 = new TierInstanceDto("tierInstaceName1", new TierDto(), 1,
				pInstances1, "fqnTierInstance1");
		tierInstanceDto2 = new TierInstanceDto("tierInstanceName2", new TierDto(), 1,
				pInstances2, "fqnTierInstance2");
		
		tierInstancesDto.add(tierInstanceDto1);
		tierInstancesDto.add(tierInstanceDto2);
		
		environmentInstanceDto = new EnvironmentInstanceDto("envInstanceName", 
	    		new EnvironmentDto(), tierInstancesDto, "vdc");
	}
	
	@Test
	public void testCreateOvf() throws Exception {
		OVFGenerationFiwareImpl ovfGenerationImpl = new OVFGenerationFiwareImpl();
		ovfGenerationImpl.setSystemPropertiesProvider(systemPropertiesProvider);

		String ovf = ovfGenerationImpl.createOvf(environmentInstanceDto);
		System.out.println("ovf=" + ovf);
		
	}
}
