/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
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
	public void setUp () throws  com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException {
		environmentInstanceResourceValidator = new EnvironmentInstanceResourceValidatorImpl();
		resourceValidator = mock(ResourceValidator.class);
		environmentInstanceResourceValidator.setResourceValidator(resourceValidator);
  
		Mockito.doNothing().when(resourceValidator).validateName(anyString());
		Mockito.doNothing().when(resourceValidator).validateDescription(anyString());
	}

    @Test
    public void shouldValidateInstanceNumberOnCreateWithoutException() throws Exception {
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
            throws Exception {
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
    public void shouldReturnValidateOKWhenLimitsValuesDontExist() throws 
             AlreadyExistEntityException, InfrastructureException, com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException {
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
            throws  AlreadyExistEntityException,
            InfrastructureException, com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException {
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
            throws AlreadyExistEntityException,
            InfrastructureException, com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException {
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
    public void shouldValidateTierWithValidTierDto() throws Exception {
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
    public void shouldThrowExceptionWithMaxSecurityGroupsAreExceeded() throws InfrastructureException, com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException
             {
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
