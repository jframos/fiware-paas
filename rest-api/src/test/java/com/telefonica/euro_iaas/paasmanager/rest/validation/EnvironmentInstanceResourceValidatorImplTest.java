/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.claudia.QuotaClient;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.QuotaExceededException;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.Limits;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public class EnvironmentInstanceResourceValidatorImplTest {
	EnvironmentInstanceResourceValidatorImpl environmentInstanceResourceValidator;
	ResourceValidator resourceValidator;
	
	@Before
	public void setUp () throws InvalidEntityException {
		environmentInstanceResourceValidator = new EnvironmentInstanceResourceValidatorImpl();
		resourceValidator = mock(ResourceValidator.class);
		environmentInstanceResourceValidator.setResourceValidator(resourceValidator);
  
		Mockito.doNothing().when(resourceValidator).validateName(anyString());
		Mockito.doNothing().when(resourceValidator).validateDescription(anyString());
	}

    @Test
    public void shouldValidateInstanceNumberOnCreateWithoutException() throws InvalidEnvironmentRequestException,
            InvalidEntityException, AlreadyExistEntityException, QuotaExceededException, InfrastructureException {
        // given
        
        QuotaClient quotaClient = mock(QuotaClient.class);
        ((EnvironmentInstanceResourceValidatorImpl) environmentInstanceResourceValidator).setQuotaClient(quotaClient);
        ClaudiaData claudiaData = mock(ClaudiaData.class);

        EnvironmentInstanceDto environmentInstanceDto = new EnvironmentInstanceDto();
        List<TierInstanceDto> listTiers = new ArrayList(2);
        TierInstanceDto tierInstanceDto = new TierInstanceDto();
        listTiers.add(tierInstanceDto);
        environmentInstanceDto.setTierInstances(listTiers);
        TierDto tierDto = new TierDto();
        tierInstanceDto.setTierDto(tierDto);
        tierDto.setFloatingip("true");
        tierDto.setInitialNumberInstances(2);
        tierDto.setRegion("region");
        Limits limits = new Limits();
        limits.setMaxTotalFloatingIps(10);
        limits.setMaxTotalInstances(10);
        limits.setTotalFloatingIpsUsed(1);
        limits.setTotalInstancesUsed(1);

        // when
        when(quotaClient.getLimits(claudiaData, "region")).thenReturn(limits);
        environmentInstanceResourceValidator.validateQuota(claudiaData, environmentInstanceDto);

        // then
        verify(quotaClient).getLimits(claudiaData, "region");
    }

    @Test
    public void shouldValidateInstanceNumberOnCreateAndReturnQuotaExceedByFloatingsIps()
            throws InvalidEnvironmentRequestException, InvalidEntityException, AlreadyExistEntityException,
            InfrastructureException {
        // given
       
        QuotaClient quotaClient = mock(QuotaClient.class);
        ((EnvironmentInstanceResourceValidatorImpl) environmentInstanceResourceValidator).setQuotaClient(quotaClient);
        ClaudiaData claudiaData = mock(ClaudiaData.class);

        EnvironmentInstanceDto environmentInstanceDto = new EnvironmentInstanceDto();
        List<TierInstanceDto> listTiers = new ArrayList(2);
        TierInstanceDto tierInstanceDto = new TierInstanceDto();
        listTiers.add(tierInstanceDto);
        environmentInstanceDto.setTierInstances(listTiers);
        TierDto tierDto = new TierDto();
        tierInstanceDto.setTierDto(tierDto);
        tierDto.setFloatingip("true");
        tierDto.setInitialNumberInstances(2);
        tierDto.setRegion("region");
        Limits limits = new Limits();
        limits.setMaxTotalFloatingIps(2);
        limits.setMaxTotalInstances(10);
        limits.setTotalFloatingIpsUsed(2);
        limits.setTotalInstancesUsed(1);

        // when
        when(quotaClient.getLimits(claudiaData, "region")).thenReturn(limits);
        try {
            environmentInstanceResourceValidator.validateQuota(claudiaData, environmentInstanceDto);
            fail();
        } catch (QuotaExceededException e) {
            // then
            assertEquals("max number of floating IPs exceeded: 2", e.getMessage());
            verify(quotaClient).getLimits(claudiaData, "region");
        }

    }

    @Test
    public void shouldReturnValidateOKWhenLimitsValuesDontExist() throws InvalidEnvironmentRequestException,
            InvalidEntityException, AlreadyExistEntityException, InfrastructureException {
        // given
        
        QuotaClient quotaClient = mock(QuotaClient.class);
        ((EnvironmentInstanceResourceValidatorImpl) environmentInstanceResourceValidator).setQuotaClient(quotaClient);
        ClaudiaData claudiaData = mock(ClaudiaData.class);

        EnvironmentInstanceDto environmentInstanceDto = new EnvironmentInstanceDto();
        List<TierInstanceDto> listTiers = new ArrayList(2);
        TierInstanceDto tierInstanceDto = new TierInstanceDto();
        listTiers.add(tierInstanceDto);
        environmentInstanceDto.setTierInstances(listTiers);
        TierDto tierDto = new TierDto();
        tierInstanceDto.setTierDto(tierDto);
        tierDto.setFloatingip("true");
        tierDto.setRegion("region");
        tierDto.setInitialNumberInstances(2);
        Limits limits = new Limits();

        // when
        when(quotaClient.getLimits(claudiaData, "region")).thenReturn(limits);

        try {
            environmentInstanceResourceValidator.validateQuota(claudiaData, environmentInstanceDto);
        } catch (QuotaExceededException e) {
            fail("should not fail because limits are negative");
        }

    }

    @Test
    public void shouldReturnValidateOKWhenLimitMaxTotalInstancesValuesDontExistEgEssexInstance()
            throws InvalidEnvironmentRequestException, InvalidEntityException, AlreadyExistEntityException,
            InfrastructureException {
        // given
        
        QuotaClient quotaClient = mock(QuotaClient.class);
        ((EnvironmentInstanceResourceValidatorImpl) environmentInstanceResourceValidator).setQuotaClient(quotaClient);
        ClaudiaData claudiaData = mock(ClaudiaData.class);

        EnvironmentInstanceDto environmentInstanceDto = new EnvironmentInstanceDto();
        List<TierInstanceDto> listTiers = new ArrayList(2);
        TierInstanceDto tierInstanceDto = new TierInstanceDto();
        listTiers.add(tierInstanceDto);
        environmentInstanceDto.setTierInstances(listTiers);
        TierDto tierDto = new TierDto();
        tierInstanceDto.setTierDto(tierDto);
        tierDto.setFloatingip("true");
        tierDto.setInitialNumberInstances(2);
        tierDto.setRegion("region");
        Limits limits = new Limits();
        limits.setMaxTotalInstances(10);

        // when
        when(quotaClient.getLimits(claudiaData, "region")).thenReturn(limits);

        try {
            environmentInstanceResourceValidator.validateQuota(claudiaData, environmentInstanceDto);
        } catch (QuotaExceededException e) {
            fail("should not fail because limits are negative");
        }

    }

    @Test
    public void shouldValidateInstanceNumberOnCreateAndReturnQuotaExceedByInstancesUsed()
            throws InvalidEnvironmentRequestException, InvalidEntityException, AlreadyExistEntityException,
            InfrastructureException {
        // given
       
        QuotaClient quotaClient = mock(QuotaClient.class);
        ((EnvironmentInstanceResourceValidatorImpl) environmentInstanceResourceValidator).setQuotaClient(quotaClient);
        ClaudiaData claudiaData = mock(ClaudiaData.class);

        EnvironmentInstanceDto environmentInstanceDto = new EnvironmentInstanceDto();
        List<TierInstanceDto> listTiers = new ArrayList(2);
        TierInstanceDto tierInstanceDto = new TierInstanceDto();
        listTiers.add(tierInstanceDto);
        environmentInstanceDto.setTierInstances(listTiers);
        TierDto tierDto = new TierDto();
        tierInstanceDto.setTierDto(tierDto);
        tierDto.setFloatingip("true");
        tierDto.setInitialNumberInstances(2);
        tierDto.setRegion("region");
        Limits limits = new Limits();
        limits.setMaxTotalFloatingIps(10);
        limits.setMaxTotalInstances(2);
        limits.setTotalFloatingIpsUsed(2);
        limits.setTotalInstancesUsed(2);

        // when
        when(quotaClient.getLimits(claudiaData, "region")).thenReturn(limits);
        try {
            environmentInstanceResourceValidator.validateQuota(claudiaData, environmentInstanceDto);
            fail();
        } catch (QuotaExceededException e) {
            // then
            assertEquals("max number of instances exceeded: 2", e.getMessage());
            verify(quotaClient).getLimits(claudiaData, "region");
        }

    }

    @Test
    public void shouldValidateTierWithValidTierDto() throws InvalidEnvironmentRequestException {
        // given
               TierDto tierDto = new TierDto();
        tierDto.setName("name");
        tierDto.setFlavour("flavour");
        tierDto.setImage("image");
        tierDto.setInitialNumberInstances(new Integer(1));
        tierDto.setMaximumNumberInstances(new Integer(4));
        tierDto.setMinimumNumberInstances(new Integer(1));
        // when
        environmentInstanceResourceValidator.validateTier(tierDto);

        // then
        assertTrue(true);
    }

    @Test
    public void shouldThrowExceptionWithMaxSecurityGroupsAreExceeded() throws InfrastructureException,
            InvalidEnvironmentRequestException {
        // given
       
        QuotaClient quotaClient = mock(QuotaClient.class);
        ((EnvironmentInstanceResourceValidatorImpl) environmentInstanceResourceValidator).setQuotaClient(quotaClient);
        ClaudiaData claudiaData = mock(ClaudiaData.class);

        EnvironmentInstanceDto environmentInstanceDto = new EnvironmentInstanceDto();
        List<TierInstanceDto> listTiers = new ArrayList(2);
        TierInstanceDto tierInstanceDto = new TierInstanceDto();
        listTiers.add(tierInstanceDto);
        environmentInstanceDto.setTierInstances(listTiers);
        TierDto tierDto = new TierDto();
        tierInstanceDto.setTierDto(tierDto);
        tierDto.setFloatingip("true");
        tierDto.setInitialNumberInstances(2);
        tierDto.setRegion("region");
        tierDto.setSecurityGroup("kk");
        Limits limits = new Limits();
        limits.setMaxSecurityGroups(10);
        limits.setTotalSecurityGroups(10);
        limits.setMaxTotalFloatingIps(10);
        limits.setMaxTotalInstances(10);
        limits.setTotalFloatingIpsUsed(2);
        limits.setTotalInstancesUsed(2);

        // when
        when(quotaClient.getLimits(claudiaData, "region")).thenReturn(limits);
        try {
            environmentInstanceResourceValidator.validateQuota(claudiaData, environmentInstanceDto);
            fail();
        } catch (QuotaExceededException e) {
            // then
            assertEquals("max number of security groups exceeded: 10", e.getMessage());
            verify(quotaClient).getLimits(claudiaData, "region");
        }
    }

}
