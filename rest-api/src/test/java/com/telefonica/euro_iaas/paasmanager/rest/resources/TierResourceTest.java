package com.telefonica.euro_iaas.paasmanager.rest.resources;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import junit.framework.TestCase;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidSecurityGroupRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.manager.impl.EnvironmentManagerImpl;

import com.telefonica.euro_iaas.paasmanager.model.Environment;

import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentType;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.SecurityGroup;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.ProductReleaseDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.rest.resources.EnvironmentResourceImpl;
import com.telefonica.euro_iaas.paasmanager.rest.validation.EnvironmentResourceValidator;
import com.telefonica.euro_iaas.paasmanager.rest.validation.EnvironmentResourceValidatorImpl;
import com.telefonica.euro_iaas.paasmanager.rest.validation.TierResourceValidator;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public class TierResourceTest extends TestCase {

	public TierResourceImpl tierResource;
	public TierManager tierManager;
	public SystemPropertiesProvider systemPropertiesProvider;
	public EnvironmentManager environmentManager;
	public TierResourceValidator validator;

	@Before
	public void setUp() throws Exception {
		tierResource = new TierResourceImpl();
		tierManager = mock(TierManager.class);
		systemPropertiesProvider = mock(SystemPropertiesProvider.class);
		environmentManager = mock(EnvironmentManager.class);
		validator = mock(TierResourceValidator.class);
		tierResource.setTierManager(tierManager);
		tierResource.setSystemPropertiesProvider(systemPropertiesProvider);
		tierResource.setEnvironmentManager(environmentManager);

		Mockito.doNothing().doThrow(new RuntimeException()).when(validator)
				.validateCreate(any(TierDto.class), any(String.class),any(String.class),
						any(SystemPropertiesProvider.class));

		List<ProductRelease> productRelease = new ArrayList<ProductRelease>();
		productRelease.add(new ProductRelease("test", "0.1"));
		Tier tier = new Tier("tiername", new Integer(1), new Integer(1),
				new Integer(1), productRelease);
		tier.setImage("image");
		tier.setIcono("icono");
		tier.setFlavour("flavour");
		tier.setFloatingip("floatingip");
		tier.setKeypair("keypair");

		Environment environment = new Environment("name", null, "description");

		when(tierManager.create(any(ClaudiaData.class), any(String.class),any(Tier.class)))
				.thenReturn(tier);
		when(environmentManager.load(any(String.class), any(String.class)))
				.thenReturn(environment);
		when(environmentManager.update(any(Environment.class))).thenReturn(
				environment);

		when(tierManager.load(any(String.class), any(String.class),any(String.class))).thenThrow(
				new EntityNotFoundException(Tier.class, "", tier));
		when(systemPropertiesProvider.getProperty(any(String.class)))
				.thenReturn("FIWARE2");

	}

	@Test
	public void testInsertTier() {

		List<ProductReleaseDto> productReleaseDto = new ArrayList<ProductReleaseDto>();
		productReleaseDto.add(new ProductReleaseDto("test", "0.1"));
		TierDto tierDto = new TierDto("tiername", new Integer(1),
				new Integer(1), new Integer(1), productReleaseDto);
		tierDto.setImage("image");
		tierDto.setIcono("icono");
		tierDto.setFlavour("flavour");
		tierDto.setFloatingip("floatingip");
		tierDto.setKeypair("keypair");

		List<TierDto> tiers = new ArrayList<TierDto>();
		tiers.add(tierDto);

		/*
		 * try { tierResource.insert("org", "vdc", "environment",tierDto); }
		 * catch (InvalidEntityException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (AlreadyExistEntityException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (EntityNotFoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (InvalidSecurityGroupRequestException e)
		 * { // TODO Auto-generated catch block e.printStackTrace(); }
		 */

	}

	@Test
	public void testTierNoProducts() throws Exception {

		TierDto tierDto = new TierDto("tiername", new Integer(1),
				new Integer(1), new Integer(1), null);
		tierDto.setImage("image");
		tierDto.setIcono("icono");
		tierDto.setFlavour("flavour");
		tierDto.setFloatingip("floatingip");
		tierDto.setKeypair("keypair");

		List<TierDto> tiers = new ArrayList<TierDto>();
		tiers.add(tierDto);

		// tierResource.insert("org", "vdc", "environment",tierDto);

	}

}
