/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public class AbstractEnvironmentResourceTest extends TestCase {

    public AbstractEnvironmentResourceImpl environmentResource;
    public EnvironmentManager environmentManager;
    public SystemPropertiesProvider systemPropertiesProvider;

    @Before
    public void setUp() throws Exception {
        environmentResource = new AbstractEnvironmentResourceImpl();
        environmentManager = mock(EnvironmentManager.class);
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        environmentResource.setEnvironmentManager(environmentManager);
        environmentResource.setSystemPropertiesProvider(systemPropertiesProvider);

        Environment environment = new Environment();
        environment.setName("Name");
        environment.setDescription("Description");

        List<ProductRelease> productRelease = new ArrayList<ProductRelease>();
        productRelease.add(new ProductRelease("test", "0.1"));
        Tier tier = new Tier("tiername", new Integer(1), new Integer(1), new Integer(1), productRelease);
        Set<Tier> tiers = new HashSet<Tier>();
        tiers.add(tier);
        environment.setTiers(tiers);

        when(environmentManager.create(any(ClaudiaData.class), any(Environment.class))).thenReturn(environment);
        when(environmentManager.load(any(String.class), any(String.class))).thenThrow(
                new EntityNotFoundException(Environment.class, "", environment));
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("FIWARE2");
    }

    @Test
    public void testInsertEnvironment() throws Exception {
        Environment environment = new Environment();
        environment.setName("Name");
        environment.setDescription("Description");

        EnvironmentDto environmentDto = new EnvironmentDto();
        environmentDto.setName("Name2");
        environmentDto.setDescription("Description");

        when(environmentManager.load(any(String.class))).thenThrow(
                new EntityNotFoundException(Environment.class, "", environment));

        List<ProductReleaseDto> productReleaseDto = new ArrayList<ProductReleaseDto>();
        productReleaseDto.add(new ProductReleaseDto("test", "0.1"));
        TierDto tierDto = new TierDto("tiername", new Integer(1), new Integer(1), new Integer(1), productReleaseDto);
        tierDto.setImage("image");
        tierDto.setIcono("icono");
        tierDto.setFlavour("flavour");
        Set<TierDto> tiers = new HashSet<TierDto>();
        tiers.add(tierDto);
        environmentDto.setTierDtos(tiers);

        environmentResource.insert("org", environmentDto);
    }

    @Test
    public void testInsertEnvironmentAndTiers() throws Exception {
        Environment environment = new Environment();
        environment.setName("Name");
        environment.setDescription("Description");

        EnvironmentDto environmentDto = new EnvironmentDto();
        environmentDto.setName("Name2");
        environmentDto.setDescription("Description");

        when(environmentManager.load(any(String.class))).thenThrow(
                new EntityNotFoundException(Environment.class, "", environment));

        environmentResource.insert("org", environmentDto);

    }

}
