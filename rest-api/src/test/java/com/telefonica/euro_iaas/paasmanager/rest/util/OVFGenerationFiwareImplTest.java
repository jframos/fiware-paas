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

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

/**
 * @author jesus.movilla
 * 
 */
public class OVFGenerationFiwareImplTest extends TestCase {

    private OVFGenerationFiwareImpl ovfGenerationImpl;
    private SystemPropertiesProvider systemPropertiesProvider;

    EnvironmentInstanceDto environmentInstanceDto;
    TierInstanceDto tierInstanceDto;

    List<ProductInstance> productInstances = new ArrayList<ProductInstance>();
    List<TierInstanceDto> tierInstancesDto = new ArrayList<TierInstanceDto>();

    ProductInstanceDto pInstance1, pInstance2;
    TierInstanceDto tierInstanceDto1, tierInstanceDto2;

    ProductReleaseDto pReleaseDto1, pReleaseDto2;

    @Override
    @Before
    public void setUp() throws Exception {

        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        when(systemPropertiesProvider.getProperty(any(String.class)))
        .thenReturn("./../core/src/main/resources/");
        ovfGenerationImpl = new OVFGenerationFiwareImpl();
        ovfGenerationImpl.setSystemPropertiesProvider(systemPropertiesProvider);

        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key3", "value3");

        List<Attribute> privateAttributes1 = new ArrayList<Attribute>();
        privateAttributes1.add(attr1);
        privateAttributes1.add(attr2);

        List<Attribute> privateAttributes2 = new ArrayList<Attribute>();
        privateAttributes2.add(attr2);
        privateAttributes2.add(attr3);

        pReleaseDto1 = new ProductReleaseDto();
        pReleaseDto1.setProductName("nameTest");
        pReleaseDto1.setVersion("versionTest");

        pReleaseDto2 = new ProductReleaseDto();
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

        tierInstanceDto1 = new TierInstanceDto("tierInstaceName1",
                new TierDto(), 1, pInstances1, "fqnTierInstance1");
        tierInstanceDto2 = new TierInstanceDto("tierInstanceName2",
                new TierDto(), 1, pInstances2, "fqnTierInstance2");

        tierInstancesDto.add(tierInstanceDto1);
        tierInstancesDto.add(tierInstanceDto2);

        environmentInstanceDto = new EnvironmentInstanceDto("envInstanceName",
                new EnvironmentDto(), tierInstancesDto, "vdc");
    }

    @Test
    public void testCreateOvf() throws Exception {

        // String ovf = ovfGenerationImpl.createOvf(environmentInstanceDto);
        // System.out.println("ovf=" + ovf);

    }

    @Test
    public void testCreateOvfFromEnvironmentFiware() throws Exception {

        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key3", "value3");

        List<Attribute> privateAttributes1 = new ArrayList<Attribute>();
        privateAttributes1.add(attr1);
        privateAttributes1.add(attr2);

        List<Attribute> privateAttributes2 = new ArrayList<Attribute>();
        privateAttributes2.add(attr2);
        privateAttributes2.add(attr3);

        pReleaseDto1 = new ProductReleaseDto();
        pReleaseDto1.setProductName("nameTest");
        pReleaseDto1.setVersion("versionTest");

        pReleaseDto2 = new ProductReleaseDto();
        pReleaseDto2.setProductName("nameTest");
        pReleaseDto2.setVersion("versionTest");

        List<ProductReleaseDto> p1Releases = new ArrayList<ProductReleaseDto>();
        p1Releases.add(pReleaseDto1);

        List<ProductReleaseDto> p2Releases = new ArrayList<ProductReleaseDto>();
        p2Releases.add(pReleaseDto2);

        TierDto tier1 = new TierDto("tiertomcat", 1, 1, 1, p1Releases, "1",
                "image", "icono", "security_group", "keypair", "yes");
        TierDto tier2 = new TierDto("tierpostgres", 1, 1, 1, p2Releases, "1",
                "image", "icono", "security_group", "keypair", "yes");

        List<TierDto> tiersDto = new ArrayList<TierDto>();
        tiersDto.add(tier1);
        tiersDto.add(tier2);

        EnvironmentDto environment = new EnvironmentDto(tiersDto,
                "environmentname", "description");

        String ovf = ovfGenerationImpl.createOvf(environment);
        // System.out.println("ovf=" + ovf);

    }

    @Test
    public void testCreateOvfFromEnvironmentNoFiware() throws Exception {

        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key3", "value3");

        List<Attribute> privateAttributes1 = new ArrayList<Attribute>();
        privateAttributes1.add(attr1);
        privateAttributes1.add(attr2);

        List<Attribute> privateAttributes2 = new ArrayList<Attribute>();
        privateAttributes2.add(attr2);
        privateAttributes2.add(attr3);

        pReleaseDto1 = new ProductReleaseDto();
        pReleaseDto1.setProductName("nameTest");
        pReleaseDto1.setVersion("versionTest");

        pReleaseDto2 = new ProductReleaseDto();
        pReleaseDto2.setProductName("nameTest");
        pReleaseDto2.setVersion("versionTest");

        List<ProductReleaseDto> p1Releases = new ArrayList<ProductReleaseDto>();
        p1Releases.add(pReleaseDto1);

        List<ProductReleaseDto> p2Releases = new ArrayList<ProductReleaseDto>();
        p2Releases.add(pReleaseDto2);

        TierDto tier1 = new TierDto("tiertomcat", 1, 1, 1, p1Releases);
        TierDto tier2 = new TierDto("tierpostgres", 1, 1, 1, p2Releases);
        tier1.setImage("image");
        tier2.setImage("image2");

        List<TierDto> tiersDto = new ArrayList<TierDto>();
        tiersDto.add(tier1);
        tiersDto.add(tier2);

        EnvironmentDto environment = new EnvironmentDto(tiersDto,
                "environmentname", "description");

        String ovf = ovfGenerationImpl.createOvf(environment);
        // System.out.println("ovf=" + ovf);

    }

    @Test
    public void testCreateOvfMongo() throws Exception {

        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key3", "value3");

        List<Attribute> privateAttributes1 = new ArrayList<Attribute>();
        privateAttributes1.add(attr1);
        privateAttributes1.add(attr2);

        List<Attribute> privateAttributes2 = new ArrayList<Attribute>();
        privateAttributes2.add(attr2);
        privateAttributes2.add(attr3);

        pReleaseDto2 = new ProductReleaseDto();
        pReleaseDto2.setProductName("nameTest");
        pReleaseDto2.setVersion("versionTest");

        List<ProductReleaseDto> p2Releases = new ArrayList<ProductReleaseDto>();
        p2Releases.add(pReleaseDto2);

        /*
         * <tierDtos> <minimum_number_instances>1</minimum_number_instances>
         * <initial_number_instances>1</initial_number_instances>
         * <maximum_number_instances>1</maximum_number_instances>
         * <name>mongoconfig</name> <flavour>2</flavour>
         * <image>945689be-1231-4414-8c3e-09bcee4e8e63</image>
         * <keypair>testpaas</keypair> <floatingip>false</floatingip>
         * <productReleaseDtos> <productName>mongodbconfig</productName>
         * <version>2.2.3</version> </productReleaseDtos> </tierDtos>
         */

        ProductReleaseDto pReleaseDto1 = new ProductReleaseDto();
        pReleaseDto1.setProductName("mongodbconfig");
        pReleaseDto1.setVersion("2.2.3");
        List<ProductReleaseDto> p1Releasesmongoconfig = new ArrayList<ProductReleaseDto>();
        p1Releasesmongoconfig.add(pReleaseDto1);
        TierDto tiermongoconfig = new TierDto("tiermongoconfig", 1, 1, 1,
                p1Releasesmongoconfig, "2",
                "945689be-1231-4414-8c3e-09bcee4e8e63", "icono", "testpaas",
        "false");
        tiermongoconfig.setSecurityGroup("security_group");

        List<TierDto> tiersDto = new ArrayList<TierDto>();
        tiersDto.add(tiermongoconfig);

        /*
         * <tierDtos> <minimum_number_instances>1</minimum_number_instances>
         * <initial_number_instances>1</initial_number_instances>
         * <maximum_number_instances>5</maximum_number_instances>
         * <name>mongoshard</name> <flavour>2</flavour>
         * <image>945689be-1231-4414-8c3e-09bcee4e8e63</image>
         * <keypair>testpaas</keypair> <floatingip>false</floatingip>
         * <productReleaseDtos> <productName>mongodbshard</productName>
         * <version>2.2.3</version> </productReleaseDtos> </tierDtos>
         */

        ProductReleaseDto pReleaseDto2 = new ProductReleaseDto();
        pReleaseDto2.setProductName("mongodbshard");
        pReleaseDto2.setVersion("2.2.3");
        List<ProductReleaseDto> p1Releasesmongoshard = new ArrayList<ProductReleaseDto>();
        p1Releasesmongoshard.add(pReleaseDto2);

        TierDto tiermongoshard = new TierDto("tiermongoshard", 6, 1, 1,
                p1Releasesmongoshard, "2",
                "945689be-1231-4414-8c3e-09bcee4e8e63", "icono", "testpaas",
        "false");
        tiermongoshard.setSecurityGroup("security_group");

        tiersDto.add(tiermongoshard);

        /*
         * <tierDtos> <minimum_number_instances>1</minimum_number_instances>
         * <initial_number_instances>1</initial_number_instances>
         * <maximum_number_instances>1</maximum_number_instances>
         * <name>contextbroker</name> <flavour>2</flavour>
         * <image>945689be-1231-4414-8c3e-09bcee4e8e63</image>
         * <keypair>testpaas</keypair> <floatingip>true</floatingip>
         * <productReleaseDtos> <productName>mongos</productName>
         * <version>2.2.3</version> </productReleaseDtos> <productReleaseDtos>
         * <productName>contextbroker</productName> <version>1.0.0</version>
         * </productReleaseDtos> </tierDtos>
         */

        ProductReleaseDto p1Releasecontextbroker = new ProductReleaseDto();
        p1Releasecontextbroker.setProductName("mongos");
        p1Releasecontextbroker.setVersion("2.2.3");
        ProductReleaseDto p1Releasecontextbroker2 = new ProductReleaseDto();
        p1Releasecontextbroker2.setProductName("tiercontextbroker");
        p1Releasecontextbroker2.setVersion("1.0.0");
        List<ProductReleaseDto> p1Releasescontextbroker = new ArrayList<ProductReleaseDto>();
        p1Releasescontextbroker.add(p1Releasecontextbroker);
        p1Releasescontextbroker.add(p1Releasecontextbroker2);

        TierDto tiercontextbroker = new TierDto("contextbroker ", 1, 1, 1,
                p1Releasescontextbroker, "2",
                "945689be-1231-4414-8c3e-09bcee4e8e63", "icono", "testpaas",
        "false");
        tiercontextbroker.setSecurityGroup("security_group");


        tiersDto.add(tiercontextbroker);

        EnvironmentDto environment = new EnvironmentDto(tiersDto,
                "environmentname", "description");

        String ovf = ovfGenerationImpl.createOvf(environment);
        System.out.println("ovf=" + ovf);

    }

    @Test
    public void testCreateOvfTierNoproduct() throws Exception {

        TierDto tiercontextbroker = new TierDto("contextbroker ", 1, 1, 1,
                null, "2",
                "945689be-1231-4414-8c3e-09bcee4e8e63", "icono", "testpaas",
        "false");
        tiercontextbroker.setSecurityGroup("security_group");

        List<TierDto> tiersDto = new ArrayList<TierDto>();
        tiersDto.add(tiercontextbroker);

        EnvironmentDto environment = new EnvironmentDto(tiersDto,
                "environmentname", "description");

        String ovf = ovfGenerationImpl.createOvf(environment);
        System.out.println("ovf=" + ovf);
    }

}
