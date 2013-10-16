package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.AlreadyExistsEntityException;
import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.dao.EnvironmentInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.rest.validation.EnvironmentInstanceResourceValidatorImpl;
import com.telefonica.euro_iaas.paasmanager.rest.validation.TierResourceValidator;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        validator.validateCreate(environmentInstanceDto, systemPropertiesProvider);
    }

    @Test
    public void testCreateEnviornmentInstanceNoBlueprintName() throws Exception {
        EnvironmentInstanceDto environmentInstanceDto = new EnvironmentInstanceDto();
        environmentInstanceDto.setDescription("description");
        environmentInstanceDto.setEnvironmentDto(environment.toDto());
        boolean exception = false;
        try {
            validator.validateCreate(environmentInstanceDto, systemPropertiesProvider);
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

        boolean exception = false;
        try {
            validator.validateCreate(environmentInstanceDto, systemPropertiesProvider);
        } catch (InvalidEnvironmentRequestException e) {
            exception = true;
        }
        assertTrue(exception);

    }

    @Test
    public void testCreateEnviornmentInstanceNoEnvironment() throws InvalidEnvironmentRequestException,
            EntityNotFoundException, InvalidEntityException, AlreadyExistsEntityException, InfrastructureException,
            InvalidOVFException {
        EnvironmentInstanceDto environmentInstanceDto = new EnvironmentInstanceDto();
        environmentInstanceDto.setDescription("description");
        environmentInstanceDto.setBlueprintName("BlueprintName");
        boolean exception = false;
        try {
            validator.validateCreate(environmentInstanceDto, systemPropertiesProvider);
        } catch (InvalidEnvironmentRequestException e) {
            exception = true;
        }
        assertTrue(exception);

    }

}
