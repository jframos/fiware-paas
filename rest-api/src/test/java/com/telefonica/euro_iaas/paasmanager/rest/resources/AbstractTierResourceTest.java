package com.telefonica.euro_iaas.paasmanager.rest.resources;

import java.util.ArrayList;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.rest.validation.TierResourceValidator;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractTierResourceTest extends TestCase {

    public AbstractTierResourceImpl tierResource;
    public TierManager tierManager;
    public SystemPropertiesProvider systemPropertiesProvider;
    public EnvironmentManager environmentManager;
    public TierResourceValidator validator;

    @Before
    public void setUp() throws Exception {
        tierResource = new AbstractTierResourceImpl();
        tierManager = mock(TierManager.class);
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        environmentManager = mock(EnvironmentManager.class);
        validator = mock(TierResourceValidator.class);
        tierResource.setTierManager(tierManager);
        tierResource.setSystemPropertiesProvider(systemPropertiesProvider);
        tierResource.setEnvironmentManager(environmentManager);
        tierResource.setTierResourceValidator(validator);

        Mockito.doNothing()
                .doThrow(new RuntimeException())
                .when(validator)
                .validateCreate(any(TierDto.class), any(String.class), any(String.class),
                        any(SystemPropertiesProvider.class));

        List<ProductRelease> productRelease = new ArrayList<ProductRelease>();
        productRelease.add(new ProductRelease("test", "0.1"));
        Tier tier = new Tier("tiername", new Integer(1), new Integer(1), new Integer(1), productRelease);
        tier.setImage("image");
        tier.setIcono("icono");
        tier.setFlavour("flavour");
        tier.setFloatingip("floatingip");
        tier.setKeypair("keypair");

        Environment environment = new Environment("name", null, "description");

        when(tierManager.create(any(ClaudiaData.class), any(String.class), any(Tier.class))).thenReturn(tier);
        when(environmentManager.load(any(String.class), any(String.class))).thenReturn(environment);
        when(environmentManager.update(any(Environment.class))).thenReturn(environment);

        when(tierManager.load(any(String.class), any(String.class), any(String.class))).thenThrow(
                new EntityNotFoundException(Tier.class, "", tier));
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("FIWARE2");

    }

    @Test
    public void testInsertTier() throws Exception {

        Environment environment = new Environment("name", null, "description");

        try {
            when(environmentManager.load(any(String.class))).thenReturn(environment);
        } catch (EntityNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        List<ProductReleaseDto> productReleaseDto = new ArrayList<ProductReleaseDto>();
        productReleaseDto.add(new ProductReleaseDto("test", "0.1"));
        TierDto tierDto = new TierDto("tiername", new Integer(1), new Integer(1), new Integer(1), productReleaseDto);
        tierDto.setImage("image");
        tierDto.setIcono("icono");
        tierDto.setFlavour("flavour");
        tierDto.setFloatingip("floatingip");
        tierDto.setKeypair("keypair");

        List<TierDto> tiers = new ArrayList<TierDto>();
        tiers.add(tierDto);

        tierResource.insert("org", "name", tierDto);

    }

    @Test
    public void testInsertTierNoProducts() throws Exception {

        Environment environment = new Environment("name", null, "description");

        try {
            when(environmentManager.load(any(String.class))).thenReturn(environment);
        } catch (EntityNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        TierDto tierDto = new TierDto("tiername", new Integer(1), new Integer(1), new Integer(1), null);
        tierDto.setImage("image");
        tierDto.setIcono("icono");
        tierDto.setFlavour("flavour");
        tierDto.setFloatingip("floatingip");
        tierDto.setKeypair("keypair");

        List<TierDto> tiers = new ArrayList<TierDto>();
        tiers.add(tierDto);

        tierResource.insert("org", "name", tierDto);

    }

}
