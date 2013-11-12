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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.telefonica.euro_iaas.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.claudia.QuotaClient;
import com.telefonica.euro_iaas.paasmanager.exception.AlreadyExistEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.exception.QuotaExceededException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Limits;
import com.telefonica.euro_iaas.paasmanager.model.dto.EnvironmentInstanceDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierInstanceDto;

public class EnvironmentInstanceResourceValidatorImplTest {

    @Test
    public void shouldValidateInstanceNumberOnCreateWithoutException() throws InvalidEnvironmentRequestException,
            InvalidEntityException, AlreadyExistEntityException, QuotaExceededException, InfrastructureException {
        // given
        EnvironmentInstanceResourceValidator environmentInstanceResourceValidator = new EnvironmentInstanceResourceValidatorImpl();
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
        Limits limits = new Limits();
        limits.setMaxTotalFloatingIps(10);
        limits.setMaxTotalInstances(10);
        limits.setTotalFloatingIpsUsed(1);
        limits.setTotalInstancesUsed(1);

        // when
        when(quotaClient.getLimits(claudiaData)).thenReturn(limits);
        environmentInstanceResourceValidator.validateQuota(claudiaData, environmentInstanceDto);

        // then
        verify(quotaClient).getLimits(claudiaData);
    }

    @Test
    public void shouldValidateInstanceNumberOnCreateAndReturnQuotaExceedByFloatingsIps()
            throws InvalidEnvironmentRequestException, InvalidEntityException, AlreadyExistEntityException,
            InfrastructureException {
        // given
        EnvironmentInstanceResourceValidator environmentInstanceResourceValidator = new EnvironmentInstanceResourceValidatorImpl();
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
        Limits limits = new Limits();
        limits.setMaxTotalFloatingIps(2);
        limits.setMaxTotalInstances(10);
        limits.setTotalFloatingIpsUsed(2);
        limits.setTotalInstancesUsed(1);

        // when
        when(quotaClient.getLimits(claudiaData)).thenReturn(limits);
        try {
            environmentInstanceResourceValidator.validateQuota(claudiaData, environmentInstanceDto);
            fail();
        } catch (QuotaExceededException e) {
            // then
            assertEquals("max number of floating IPs exceeded: 2", e.getMessage());
            verify(quotaClient).getLimits(claudiaData);
        }

    }

    @Test
    public void shouldValidateInstanceNumberOnCreateAndReturnQuotaExceedByInstancesUsed()
            throws InvalidEnvironmentRequestException, InvalidEntityException, AlreadyExistEntityException,
            InfrastructureException {
        // given
        EnvironmentInstanceResourceValidator environmentInstanceResourceValidator = new EnvironmentInstanceResourceValidatorImpl();
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
        Limits limits = new Limits();
        limits.setMaxTotalFloatingIps(10);
        limits.setMaxTotalInstances(2);
        limits.setTotalFloatingIpsUsed(2);
        limits.setTotalInstancesUsed(2);

        // when
        when(quotaClient.getLimits(claudiaData)).thenReturn(limits);
        try {
            environmentInstanceResourceValidator.validateQuota(claudiaData, environmentInstanceDto);
            fail();
        } catch (QuotaExceededException e) {
            // then
            assertEquals("max number of instances exceeded: 2", e.getMessage());
            verify(quotaClient).getLimits(claudiaData);
        }

    }

    @Test
    public void shouldValidateTierWithValidTierDto() throws InvalidEnvironmentRequestException {
        // given
        EnvironmentInstanceResourceValidatorImpl environmentInstanceResourceValidatorImpl = new EnvironmentInstanceResourceValidatorImpl();
        TierDto tierDto = new TierDto();
        tierDto.setName("name");
        tierDto.setFlavour("flavour");
        tierDto.setImage("image");

        // when
        environmentInstanceResourceValidatorImpl.validateTier(tierDto);

        // then
        assertTrue(true);
    }

}
