package com.telefonica.euro_iaas.paasmanager.util;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.SDC_SERVER_MEDIATYPE;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.SDC_SERVER_URL;

import java.util.ArrayList;


import com.telefonica.euro_iaas.paasmanager.dao.ProductInstanceDao;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;

import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;

public class ProductInstallatorSdcImpl implements ProductInstallator {

	private SDCClient sdcClient;
    private SystemPropertiesProvider systemPropertiesProvider;
    
    
	@Override
	public ProductInstance install(ProductInstance productInstance) {
		
		String sdcServerUrl = systemPropertiesProvider.getProperty(SDC_SERVER_URL);
		String sdcMediaType =  systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE);
		    
		//SDCClient client = new SDCClient();
        com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService service =
        		sdcClient.getProductInstanceService(sdcServerUrl, sdcMediaType);
        
        //From Paasmanager ProductRelease To SDC ProductInstanceDto
        ProductInstanceDto productInstanceDto = new ProductInstanceDto();
        productInstanceDto.setVm(
        		new com.telefonica.euro_iaas.sdc.model.dto.VM(
        				productInstance.getVm().getFqn(), 
        				productInstance.getVm().getIp(),
        				productInstance.getVm().getHostname(), 
        				productInstance.getVm().getDomain(), 
        				productInstance.getVm().getOsType()));
        
        productInstanceDto.setProduct(
        		new ProductReleaseDto(
        				productInstance.getProductRelease().getName(), 
        				productInstance.getProductRelease().getDescription(),
        				productInstance.getProductRelease().getVersion(), null, 
        				null, 
        				null, null));
        
        productInstanceDto.setAttributes(
        		new ArrayList<com.telefonica.euro_iaas.sdc.model.Attribute>());
        
        for (Attribute attribute : productInstance.getPrivateAttributes()) {
        	productInstanceDto.getAttributes().add(
                    new com.telefonica.euro_iaas.sdc.model.Attribute(
                    		attribute.getKey(), attribute.getValue()));
        }
        
        //Installing product with SDC
        productInstance.setStatus(Status.INSTALLING);
        service.install(productInstance.getVdc(), productInstanceDto, null);
        productInstance.setStatus(Status.INSTALLED);
        
        return productInstance;

	}

	@Override
	public void uninstall(ProductInstance productInstance) {
		
		String sdcServerUrl = systemPropertiesProvider.getProperty(SDC_SERVER_URL);
		String sdcMediaType =  systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE);
		
		//SDCClient client = new SDCClient();
        com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService productService =
        		sdcClient.getProductInstanceService(sdcServerUrl, sdcMediaType);
        
        //TODO check if the product to be uninstalled supports a current application instance:
        /*List<InstallableInstanceDto> applicationInstances =         
        		supportedApplicationInstanceInstalled(fqn, instance);
        
       if (applicationInstances.size() > 0)
        	throw new SupportedApplicationInstanceInstalledException(applicationInstances, instance);
        else*/
        	productService.uninstall(productInstance.getVdc(), productInstance.getId(), null);    
		
	}
	
    // //////////// I.O.C /////////////
    /**
     * @param sdcClient
     *            the sdcClient to set
     */
    public void setSDCClient(SDCClient sdcClient) {
        this.sdcClient = sdcClient;
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

}
