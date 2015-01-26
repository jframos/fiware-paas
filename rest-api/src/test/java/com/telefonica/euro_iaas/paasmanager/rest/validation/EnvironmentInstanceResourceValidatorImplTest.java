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

import com.telefonica.euro_iaas.paasmanager.claudia.QuotaClient;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.QuotaExceededException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Limits;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;

/**
 * Test the EnvironmentInstanceResourceValidatorImpl class.
 */
public class EnvironmentInstanceResourceValidatorImplTest {
    private EnvironmentInstanceResourceValidatorImpl environmentInstanceResourceValidator;
    private ResourceValidator resourceValidator;

    /**
     * Initialize the Unit Test.
     * 
     * @throws com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException
     */
    @Before
    public void setUp() throws com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException {
        environmentInstanceResourceValidator = new EnvironmentInstanceResourceValidatorImpl();
        resourceValidator = mock(ResourceValidator.class);
        environmentInstanceResourceValidator.setResourceValidator(resourceValidator);

        Mockito.doNothing().when(resourceValidator).validateName(anyString());
        Mockito.doNothing().when(resourceValidator).validateDescription(anyString());
    }

    /**
     * Test the instance number on the creation operation without any exception.
     * 
     * @throws Exception
     */
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

    /**
     * Test the creation of an instance when we have no more floating IPs
     * available.
     * 
     * @throws Exception
     */
    @Test
    public void shouldValidateInstanceNumberOnCreateAndReturnQuotaExceedByFloatingsIps() throws Exception {
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

    /**
     * Test that the validation return OK when there is no limits assigned.
     * 
     * @throws AlreadyExistEntityException
     * @throws InfrastructureException
     * @throws com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException
     */
    @Test
    public void shouldReturnValidateOKWhenLimitsValuesDontExist() throws AlreadyExistEntityException,
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

    /**
     * Test the validation of resources when the maximum limits of instances do
     * not exist.
     * 
     * @throws AlreadyExistEntityException
     * @throws InfrastructureException
     * @throws com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException
     */
    @Test
    public void shouldReturnValidateOKWhenLimitMaxTotalInstancesValuesDontExistEgEssexInstance()
            throws AlreadyExistEntityException, InfrastructureException,
            com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException {

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

    /**
     * Test that the new instance cannot be created due to quota exceeded.
     * 
     * @throws AlreadyExistEntityException
     * @throws InfrastructureException
     * @throws com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException
     */
    @Test
    public void shouldValidateInstanceNumberOnCreateAndReturnQuotaExceedByInstancesUsed()
            throws AlreadyExistEntityException, InfrastructureException,
            com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException {

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

    /**
     * Test that the information associated to a tier is valid.
     * 
     * @throws Exception
     */
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

    /**
     * Test that it can launch an exception when the maximum number of security
     * group was reached.
     * 
     * @throws InfrastructureException
     * @throws com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException
     */
    @Test
    public void shouldThrowExceptionWithMaxSecurityGroupsAreExceeded() throws InfrastructureException,
            com.telefonica.euro_iaas.paasmanager.exception.InvalidEntityException {

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
