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
import com.telefonica.euro_iaas.paasmanager.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidSecurityGroupRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.NetworkDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.rest.exception.APIException;
import com.telefonica.euro_iaas.paasmanager.rest.validation.TierResourceValidator;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public class TierResourceTest extends TestCase {

    public TierResourceImpl tierResource;
    public TierManager tierManager;
    public SystemPropertiesProvider systemPropertiesProvider;
    public EnvironmentManager environmentManager;
    public TierResourceValidator tierResourceValidator;
    public static String vdc ="VDC";
    public static String org ="ORG";
    public static String env ="env";

    @Before
    public void setUp() throws Exception {
        tierResource = new TierResourceImpl();
        tierManager = mock(TierManager.class);
        systemPropertiesProvider = mock(SystemPropertiesProvider.class);
        environmentManager = mock(EnvironmentManager.class);
        tierResourceValidator = mock(TierResourceValidator.class);
        ProductReleaseDao productReleaseDao = mock(ProductReleaseDao.class);
        tierResource.setTierManager(tierManager);
        tierResource.setSystemPropertiesProvider(systemPropertiesProvider);
        tierResource.setEnvironmentManager(environmentManager);
     
        tierResource.setProductReleaseDao(productReleaseDao);
        when(systemPropertiesProvider.getProperty(any(String.class))).thenReturn("FsIWARE");
        
       
        
        
        Mockito.doNothing().when(tierResourceValidator).validateCreate(any(ClaudiaData.class), any(TierDto.class), any(String.class), any(String.class));
        Mockito.doNothing().when(tierResourceValidator).validateDelete(any(String.class), any(String.class), any(String.class)); 
        Mockito.doNothing().when(tierResourceValidator).validateUpdate(any(String.class), any(String.class), any(String.class), any(TierDto.class));
        tierResource.setTierResourceValidator(tierResourceValidator);

        List<ProductRelease> productReleases = new ArrayList<ProductRelease>();
        productReleases.add(new ProductRelease("test", "0.1"));
        Tier tier = new Tier("tiername", new Integer(1), new Integer(1), new Integer(1), productReleases);
        tier.setImage("image");
        tier.setIcono("icono");
        tier.setFlavour("flavour");
        tier.setFloatingip("floatingip");
        tier.setKeypair("keypair");

        Environment environment = new Environment("name", null, "description");
        environment.addTier(tier);
        
        
        ProductRelease productRelease =new ProductRelease("test", "0.1");
        when(productReleaseDao.load(any(String.class))).thenReturn(productRelease);

        when(environmentManager.load(any(String.class))).thenReturn(environment);

        

    }

    @Test
    public void testInsertTier() throws APIException, InvalidEntityException, InvalidSecurityGroupRequestException, InfrastructureException, EntityNotFoundException, AlreadyExistsEntityException {

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
        
        when(tierManager.create(any(ClaudiaData.class), any(String.class), any(Tier.class))).thenReturn(tierDto.fromDto(vdc));
        
        tierResource.insert(org, vdc, env, tierDto);

    }
    
    @Test
    public void testInsertTierWithNetwork() throws APIException, InvalidEntityException, InvalidSecurityGroupRequestException, InfrastructureException, EntityNotFoundException, AlreadyExistsEntityException {

        List<ProductReleaseDto> productReleaseDto = new ArrayList<ProductReleaseDto>();
        productReleaseDto.add(new ProductReleaseDto("test", "0.1"));
        NetworkDto net = new NetworkDto("net");
        TierDto tierDto = new TierDto("tiername", new Integer(1), new Integer(1), new Integer(1), productReleaseDto);
        tierDto.setImage("image");
        tierDto.setIcono("icono");
        tierDto.setFlavour("flavour");
        tierDto.setFloatingip("floatingip");
        tierDto.setKeypair("keypair");
        tierDto.addNetworkDto(net);

        List<TierDto> tiers = new ArrayList<TierDto>();
        tiers.add(tierDto);
        
        when(tierManager.create(any(ClaudiaData.class), any(String.class), any(Tier.class))).thenReturn(tierDto.fromDto(vdc));
        
        tierResource.insert(org, vdc, env, tierDto);

    }

    @Test
    public void testTierNoProducts() throws Exception {

        TierDto tierDto = new TierDto("tiername", new Integer(1), new Integer(1), new Integer(1), null);
        tierDto.setImage("image");
        tierDto.setIcono("icono");
        tierDto.setFlavour("flavour");
        tierDto.setFloatingip("floatingip");
        tierDto.setKeypair("keypair");

        List<TierDto> tiers = new ArrayList<TierDto>();
        tiers.add(tierDto);
        when(tierManager.create(any(ClaudiaData.class), any(String.class), any(Tier.class))).thenReturn(tierDto.fromDto(vdc));
        tierResource.insert(org, vdc, env, tierDto);

    }
    
    @Test(expected=APIException.class)
    public void testTierException() throws APIException, InvalidEntityException, InvalidSecurityGroupRequestException, InfrastructureException, EntityNotFoundException, AlreadyExistsEntityException  {

        TierDto tierDto = new TierDto("", new Integer(1), new Integer(1), new Integer(1), null);
        tierDto.setImage("image");
        tierDto.setIcono("icono");
        tierDto.setFlavour("flavour");
        tierDto.setFloatingip("floatingip");
        tierDto.setKeypair("keypair");
        when(tierManager.create(any(ClaudiaData.class), any(String.class), any(Tier.class))).thenReturn(tierDto.fromDto(vdc));
        tierResource.insert(org, vdc, env, tierDto);
    }
    
    @Test
    public void testDeleteTier() throws APIException, InvalidEntityException, InvalidSecurityGroupRequestException, InfrastructureException, EntityNotFoundException, AlreadyExistsEntityException {

        List<ProductReleaseDto> productReleaseDto = new ArrayList<ProductReleaseDto>();
        productReleaseDto.add(new ProductReleaseDto("test", "0.1"));
        TierDto tierDto = new TierDto("tiername22", new Integer(1), new Integer(1), new Integer(1), productReleaseDto);
        tierDto.setImage("image");
        tierDto.setIcono("icono");
        tierDto.setFlavour("flavour");
        tierDto.setFloatingip("floatingip");
        tierDto.setKeypair("keypair");
        when(tierManager.load( any(String.class), any(String.class),any(String.class))).thenReturn(tierDto.fromDto(vdc));
        tierResource.delete(org, vdc, env, tierDto.getName());
    }
    
    @Test
    public void testDeleteTierWithNetwork() throws APIException, InvalidEntityException, InvalidSecurityGroupRequestException, InfrastructureException, EntityNotFoundException, AlreadyExistsEntityException {

        List<ProductReleaseDto> productReleaseDto = new ArrayList<ProductReleaseDto>();
        productReleaseDto.add(new ProductReleaseDto("test", "0.1"));
        NetworkDto net = new NetworkDto("net");
        TierDto tierDto = new TierDto("tiername22", new Integer(1), new Integer(1), new Integer(1), productReleaseDto);
        tierDto.setImage("image");
        tierDto.setIcono("icono");
        tierDto.setFlavour("flavour");
        tierDto.setFloatingip("floatingip");
        tierDto.setKeypair("keypair");
        tierDto.addNetworkDto(net);
        when(tierManager.load( any(String.class), any(String.class),any(String.class))).thenReturn(tierDto.fromDto(vdc));
        tierResource.delete(org, vdc, env, tierDto.getName());
    }
    
    @Test
    public void testUpdateTier() throws APIException, InvalidEntityException, InvalidSecurityGroupRequestException, InfrastructureException, EntityNotFoundException, AlreadyExistsEntityException {

        List<ProductReleaseDto> productReleaseDto = new ArrayList<ProductReleaseDto>();
        productReleaseDto.add(new ProductReleaseDto("test", "0.1"));
        TierDto tierDto = new TierDto("tiername22", new Integer(1), new Integer(1), new Integer(1), productReleaseDto);
        tierDto.setImage("image");
        tierDto.setIcono("icono");
        tierDto.setFlavour("flavour");
        tierDto.setFloatingip("floatingip");
        tierDto.setKeypair("keypair");
        when(tierManager.create(any(ClaudiaData.class), any(String.class), any(Tier.class))).thenReturn(tierDto.fromDto(vdc));
        
        when(tierManager.load( any(String.class), any(String.class),any(String.class))).thenReturn(tierDto.fromDto(vdc));
        tierResource.insert(org, vdc, env, tierDto);
        
        TierDto tierDto2 = new TierDto("tiername22", new Integer(1), new Integer(1), new Integer(1), productReleaseDto);
        tierDto2.setImage("image2");
        tierDto2.setIcono("icono");
        tierDto2.setFlavour("flavour");
        tierDto2.setFloatingip("floatingip");
        tierDto2.setKeypair("keypair");
        tierResource.update(org, vdc, env, tierDto.getName(), tierDto2);
        TierDto tier= tierResource.load(vdc, env, tierDto2.getName());
        assertEquals(tier.getImage(), tierDto2.getImage());

    }
    
    @Test(expected=APIException.class)
    public void testUpdateTierDifferent() throws APIException, EntityNotFoundException {

        List<ProductReleaseDto> productReleaseDto = new ArrayList<ProductReleaseDto>();
        productReleaseDto.add(new ProductReleaseDto("test", "0.1"));
        TierDto tierDto = new TierDto("tiername3", new Integer(1), new Integer(1), new Integer(1), productReleaseDto);
        tierDto.setImage("image");
        tierDto.setIcono("icono");
        tierDto.setFlavour("flavour");
        tierDto.setFloatingip("floatingip");
        tierDto.setKeypair("keypair");

        when(tierManager.load( any(String.class), any(String.class),any(String.class))).thenReturn(tierDto.fromDto(vdc));
        
        TierDto tierDto2 = new TierDto("tiername4", new Integer(1), new Integer(1), new Integer(1), productReleaseDto);
        tierResource.update(org, vdc, env, tierDto.getName(), tierDto2);

    }

}
