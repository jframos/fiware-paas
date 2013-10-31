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
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.claudia.QuotaClient;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.QuotaExceededException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.rest.validation.EnvironmentInstanceResourceValidatorImpl;
import com.telefonica.euro_iaas.paasmanager.rest.validation.TierResourceValidator;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public class EnvironmetnInstanceValidatorTest extends TestCase {
    EnvironmentInstanceResourceValidatorImpl validator;
    TierResourceValidator tierResourceValidator;
    EnvironmentInstanceDao environmentInstanceDao;
    Environment environment;
    SystemPropertiesProvider systemPropertiesProvider;

    @Before
    public void setUp() throws Exception {
        validator = new EnvironmentInstanceResourceValidatorImpl();
        tierResourceValidator = mock(TierResourceValidator.class);
        environmentInstanceDao = mock(EnvironmentInstanceDao.class);
        validator.setEnvironmentInstanceDao(environmentInstanceDao);
        validator.setTierResourceValidator(tierResourceValidator);

        ProductRelease productRelease = new ProductRelease("product", "2.0");
        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        productReleases.add(productRelease);

        Tier tier = new Tier("name", new Integer(1), new Integer(1), new Integer(1), productReleases, "flavour",
                "image", "icono", "keypair", "floatingip", "payload");

        List<Tier> tiers = new ArrayList<Tier>();
        tiers.add(tier);
        tiers.add(tier);

        environment = new Environment();
        environment.setName("environemntName");
        environment.setDescription("description");

        environment.setTiers(tiers);
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        Mockito.doThrow(new EntityNotFoundException(EnvironmentInstance.class, "dd", tiers))
                .when(environmentInstanceDao).load(any(String.class));

        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("FIWARE");

    }

    @Test
    public void testCreateEnviornmentInstance() throws Exception {
        EnvironmentInstanceDto environmentInstanceDto = new EnvironmentInstanceDto();
        environmentInstanceDto.setDescription("description");
        environmentInstanceDto.setEnvironmentDto(environment.toDto());
        environmentInstanceDto.setBlueprintName("blueprintName");
        ClaudiaData claudiaData = mock(ClaudiaData.class);
        QuotaClient quotaClient = mock(QuotaClient.class);
        validator.setQuotaClient(quotaClient);

        // when
        validator.validateCreate(environmentInstanceDto, systemPropertiesProvider, claudiaData);

    }

    @Test
    public void testCreateEnviornmentInstanceNoBlueprintName() throws Exception {
        EnvironmentInstanceDto environmentInstanceDto = new EnvironmentInstanceDto();
        environmentInstanceDto.setDescription("description");
        environmentInstanceDto.setEnvironmentDto(environment.toDto());
        boolean exception = false;
        ClaudiaData claudiaData = mock(ClaudiaData.class);

        try {
            validator.validateCreate(environmentInstanceDto, systemPropertiesProvider, claudiaData);
        } catch (InvalidEnvironmentRequestException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    public void testCreateEnviornmentInstanceNoDescription() throws Exception {
        EnvironmentInstanceDto environmentInstanceDto = new EnvironmentInstanceDto();
        environmentInstanceDto.setEnvironmentDto(environment.toDto());
        environmentInstanceDto.setBlueprintName("blueprintName");

        ClaudiaData claudiaData = mock(ClaudiaData.class);

        boolean exception = false;
        try {
            validator.validateCreate(environmentInstanceDto, systemPropertiesProvider, claudiaData);
        } catch (InvalidEnvironmentRequestException e) {
            exception = true;
        }
        assertTrue(exception);

    }

    @Test
    public void testCreateEnviornmentInstanceNoEnvironment() throws InvalidEnvironmentRequestException,
            EntityNotFoundException, InvalidEntityException, AlreadyExistsEntityException, InfrastructureException,
            InvalidOVFException, QuotaExceededException {
        EnvironmentInstanceDto environmentInstanceDto = new EnvironmentInstanceDto();
        environmentInstanceDto.setDescription("description");
        environmentInstanceDto.setBlueprintName("BlueprintName");
        boolean exception = false;
        ClaudiaData claudiaData = mock(ClaudiaData.class);

        try {
            validator.validateCreate(environmentInstanceDto, systemPropertiesProvider, claudiaData);
        } catch (InvalidEnvironmentRequestException e) {
            exception = true;
        }
        assertTrue(exception);

    }

}
