package com.telefonica.euro_iaas.paasmanager.manager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.paasmanager.exception.NotUniqueResultException;
import com.telefonica.euro_iaas.paasmanager.manager.impl.ProductInstanceManagerImpl;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.OS;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.ProductType;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.model.searchcriteria.ProductInstanceSearchCriteria;
import com.telefonica.euro_iaas.paasmanager.util.ProductInstallator;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;


/**
 * Unit test suite for ProductInstanceManagerImpl.
 *
 * @author Jesus M. Movilla
 *
 */
public class ProductInstanceManagerImplTest {

    private ProductInstanceDao productInstanceDao;
    private ProductInstallator productInstallator;
    
    private ProductInstance expectedProductInstance;
    private ProductRelease productRelease;
    private OS os;
    private VM host = new VM("fqn","ip","hostname", "domain");
    

    @Before
    public void setUp() throws Exception {
    	productInstallator = mock(ProductInstallator.class);
        when(productInstallator.install(
                any(ProductInstance.class))).thenReturn(expectedProductInstance);
        
        os = new OS("os1", "1", "os1 description", "v1");
        host.setOsType(os.getOsType());
       
        ProductType productType = new ProductType ("type A", "Type A desc");
        
        productRelease = new ProductRelease(
                "productPrueba", "1.0", "Product Prueba desc", null,
                null, Arrays.asList(os), true, productType);
       
       expectedProductInstance = new ProductInstance(
                productRelease, Status.INSTALLED, host, "vdc");

        productInstanceDao = mock(ProductInstanceDao.class);
        when(productInstanceDao.create(any(ProductInstance.class))).thenReturn(
        		expectedProductInstance);
        when(productInstanceDao.update(any(ProductInstance.class))).thenReturn(
        		expectedProductInstance);
        when(productInstanceDao.findUniqueByCriteria(
                any(ProductInstanceSearchCriteria.class)))
                .thenThrow(new NotUniqueResultException());
    }

    @Test
    public void testInstallWhenEverithingIsOk() throws Exception {
        ProductInstanceManagerImpl manager = new ProductInstanceManagerImpl();
        manager.setProductInstanceDao(productInstanceDao);
        manager.setProductInstallator(productInstallator);

        ProductInstance installedProduct = manager.install(
                host, "vdc", productRelease, new ArrayList<Attribute>());
        // make verifications
        assertEquals(expectedProductInstance, installedProduct);

        verify(productInstallator, times(1)).install(
                any(ProductInstance.class));
        
        // only one product will be installed, the other one causes error.
        verify(productInstanceDao, times(1)).update(any(ProductInstance.class));
        verify(productInstanceDao, times(1)).findUniqueByCriteria(
                any(ProductInstanceSearchCriteria.class));
       
    }

/*    @Test
    public void testUninstallWhenEverithingIsOk() throws Exception {
        ProductInstanceManagerChefImpl manager = new ProductInstanceManagerChefImpl();
        manager.setProductInstanceDao(productInstanceDao);
        manager.setPropertiesProvider(propertiesProvider);
        manager.setRecipeNamingGenerator(recipeNamingGenerator);
        manager.setChefNodeDao(chefNodeDao);
        manager.setSdcClientUtils(sdcClientUtils);
        manager.setValidator(piValidator);

        manager.uninstall(expectedProduct);

        verify(recipeNamingGenerator, times(1)).getUninstallRecipe(
                any(ProductInstance.class));
        // only one prodcut will be installed, the other one causes error.
        verify(productInstanceDao, times(0)).create(any(ProductInstance.class));
        verify(productInstanceDao, times(2)).update(any(ProductInstance.class));

        verify(chefNodeDao, times(2)).loadNode(host);
        verify(chefNodeDao, times(2)).updateNode((ChefNode)anyObject());
        verify(sdcClientUtils, times(2)).execute(host);
        verify(piValidator, times(1)).validateUninstall(expectedProduct);
    }*/
}
