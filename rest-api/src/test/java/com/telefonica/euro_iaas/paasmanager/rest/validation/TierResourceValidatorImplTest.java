/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.rest.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidEnvironmentRequestException;
import com.telefonica.euro_iaas.paasmanager.manager.TierManager;
import com.telefonica.euro_iaas.paasmanager.model.Metadata;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.dto.TierDto;

public class TierResourceValidatorImplTest {

    @Test
    public void shouldReturnTrueWhenAllDependenciesForProductExists() {
        // given
        TierResourceValidatorImpl tierResourceValidator = new TierResourceValidatorImpl();

        Map<String, String> productsNameMap = new HashMap<String, String>();
        productsNameMap.put("tomcat-6", "");
        productsNameMap.put("mysql", "");
        productsNameMap.put("mongos", "");
        productsNameMap.put("mongoshard", "");
        productsNameMap.put("mongoconfig", "");
        List<String> contextBrokerDependencies = new ArrayList<String>(2);
        contextBrokerDependencies.add("mongos");
        contextBrokerDependencies.add("mongoshard");
        contextBrokerDependencies.add("mongoconfig");

        // when
        boolean result = tierResourceValidator.checkTierProductsInDependencyList(contextBrokerDependencies,
                productsNameMap);
        // then
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenNotExistADependencyForAproduct() {
        // given
        TierResourceValidatorImpl tierResourceValidator = new TierResourceValidatorImpl();

        Map<String, String> productsNameMap = new HashMap<String, String>();
        productsNameMap.put("tomcat-6", "");
        productsNameMap.put("mysql", "");
        List<String> contextBrokerDependencies = new ArrayList<String>(2);
        contextBrokerDependencies.add("mongos");

        // when
        boolean result = tierResourceValidator.checkTierProductsInDependencyList(contextBrokerDependencies,
                productsNameMap);
        // then
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenDependenciesListIsEmptyBecauseAProductNeedAnotherProduct() {
        // given
        TierResourceValidatorImpl tierResourceValidator = new TierResourceValidatorImpl();

        Map<String, String> productsNameMap = new HashMap<String, String>();
        List<String> contextBrokerDependencies = new ArrayList<String>(2);
        contextBrokerDependencies.add("mongos");

        // when
        boolean result = tierResourceValidator.checkTierProductsInDependencyList(contextBrokerDependencies,
                productsNameMap);
        // then
        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueWhenDependenciesListIsEmptyAndProductHasntDependencies() {
        // given
        TierResourceValidatorImpl tierResourceValidator = new TierResourceValidatorImpl();

        Map<String, String> productsNameMap = new HashMap<String, String>();
        List<String> contextBrokerDependencies = new ArrayList<String>(2);

        // when
        boolean result = tierResourceValidator.checkTierProductsInDependencyList(contextBrokerDependencies,
                productsNameMap);
        // then
        assertTrue(result);
    }

    @Test
    public void shouldCreateAListWithDependenciesFromMetadataProducts() {
        // given
        TierResourceValidatorImpl tierResourceValidator = new TierResourceValidatorImpl();
        List<Tier> tiers = new ArrayList<Tier>();
        Tier tier1 = new Tier();
        tiers.add(tier1);
        List<ProductRelease> products = new ArrayList<ProductRelease>(2);
        tier1.setProductReleases(products);
        ProductRelease product1 = new ProductRelease();
        product1.setName("mongo");
        product1.setVersion("1");
        ProductRelease product2 = new ProductRelease();
        product2.setName("mongoshared");
        product2.setVersion("1");
        products.add(product1);
        products.add(product2);

        Tier tier2 = new Tier();
        tiers.add(tier2);
        List<ProductRelease> products2 = new ArrayList<ProductRelease>(2);
        tier2.setProductReleases(products2);
        ProductRelease product3 = new ProductRelease();
        product3.setName("contexbroker");
        product3.setVersion("1.0");
        Set<Metadata> metadatas = new HashSet<Metadata>(2);
        product3.setMetadatas(metadatas);
        products2.add(product3);
        Metadata metadataDep = new Metadata();
        metadatas.add(metadataDep);
        metadataDep.setKey("dep");
        metadataDep.setValue("mongo-1");

        // when
        List<String> dependenciesList = tierResourceValidator.createDependenciesForTiers(tiers);

        // then
        assertNotNull(dependenciesList);
        assertEquals(1, dependenciesList.size());
    }

    @Test
    public void shouldCreateAListOfProducts() {
        // given
        TierResourceValidatorImpl tierResourceValidator = new TierResourceValidatorImpl();
        List<Tier> tiers = new ArrayList<Tier>();
        Tier tier1 = new Tier();
        tiers.add(tier1);
        List<ProductRelease> products = new ArrayList<ProductRelease>(2);
        tier1.setProductReleases(products);
        ProductRelease product1 = new ProductRelease();
        product1.setName("mongo");
        product1.setVersion("1");
        ProductRelease product2 = new ProductRelease();
        product2.setName("mongoshared");
        product2.setVersion("1");
        products.add(product1);
        products.add(product2);

        Tier tier2 = new Tier();
        tiers.add(tier2);
        List<ProductRelease> products2 = new ArrayList<ProductRelease>(2);
        tier2.setProductReleases(products2);
        ProductRelease product3 = new ProductRelease();
        product3.setName("contexbroker");
        product3.setVersion("1.0");
        Set<Metadata> metadatas = new HashSet<Metadata>(2);
        product3.setMetadatas(metadatas);
        products2.add(product3);
        Metadata metadataDep = new Metadata();
        metadatas.add(metadataDep);
        metadataDep.setKey("dep");
        metadataDep.setValue("mongo-1");

        // when
        Map<String, String> productNameList = tierResourceValidator.createProductList(tiers);

        // then
        assertNotNull(productNameList);
        assertEquals(3, productNameList.size());

    }

    @Test
    public void shouldValidateATierDependencies() throws EntityNotFoundException, InvalidEnvironmentRequestException {
        // given
        TierResourceValidatorImpl tierResourceValidator = new TierResourceValidatorImpl();
        Set<TierDto> tierDTOlist = new HashSet<TierDto>(2);
        TierDto tierDTO = new TierDto();
        tierDTO.setName("tier1");
        tierDTOlist.add(tierDTO);
        String vdc = "vdc";
        String environmentName = "env";
        TierManager tierManager = mock(TierManager.class);
        tierResourceValidator.setTierManager(tierManager);
        Tier tier = new Tier();

        // when
        when(tierManager.loadTierWithProductReleaseAndMetadata(tierDTO.getName(), environmentName, vdc)).thenReturn(
                tier);
        tierResourceValidator.validateTiersDependencies(environmentName, vdc, tierDTOlist);
        // then
    }

    @Test(expected = InvalidEnvironmentRequestException.class)
    public void shouldThrowExceptionInValidateATierWithDependencyThatNotExist() throws EntityNotFoundException,
            InvalidEnvironmentRequestException {
        // given
        TierResourceValidatorImpl tierResourceValidator = new TierResourceValidatorImpl();
        Set<TierDto> tierDTOlist = new HashSet<TierDto>(2);
        TierDto tierDTO = new TierDto();
        tierDTO.setName("tier1");
        tierDTOlist.add(tierDTO);
        String vdc = "vdc";
        String environmentName = "env";
        TierManager tierManager = mock(TierManager.class);
        tierResourceValidator.setTierManager(tierManager);
        Tier tier = new Tier();
        List<ProductRelease> productReleaseList = new ArrayList<ProductRelease>(2);
        ProductRelease productRelease = new ProductRelease();
        productRelease.setName("tomcat");
        Set<Metadata> metadatas = new HashSet<Metadata>(2);
        Metadata metadata = new Metadata();
        metadata.setKey("dep");
        metadata.setValue("mysql");
        metadatas.add(metadata);
        productRelease.setMetadatas(metadatas);
        productReleaseList.add(productRelease);
        tier.setProductReleases(productReleaseList);

        // when
        when(tierManager.loadTierWithProductReleaseAndMetadata(tierDTO.getName(), environmentName, vdc)).thenReturn(
                tier);
        tierResourceValidator.validateTiersDependencies(environmentName, vdc, tierDTOlist);
        // then
    }
}
