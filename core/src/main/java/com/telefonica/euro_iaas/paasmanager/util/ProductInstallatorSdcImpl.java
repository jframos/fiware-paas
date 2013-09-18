package com.telefonica.euro_iaas.paasmanager.util;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.SDC_SERVER_MEDIATYPE;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.SDC_SERVER_URL;

import java.util.ArrayList;


import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;

import com.telefonica.euro_iaas.sdc.client.SDCClient;

public class ProductInstallatorSdcImpl implements ProductInstallator {

	private SDCClient sDCClient;
    private SystemPropertiesProvider systemPropertiesProvider;
    
    
	@Override
	public ProductInstance install(ProductInstance productInstance) {
		
		String sdcServerUrl = systemPropertiesProvider.getProperty(SDC_SERVER_URL);
		String sdcMediaType =  systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE);
		    
		//SDCClient client = new SDCClient();
        com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService service =
        		sDCClient.getProductInstanceService(sdcServerUrl, sdcMediaType);
        
        //From Paasmanager ProductRelease To SDC ProductInstanceDto
        com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto productInstanceDto 
        	= new com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto();
        
        productInstanceDto.setVm(
        		new com.telefonica.euro_iaas.sdc.model.dto.VM(
        				productInstance.getVm().getFqn(), 
        				productInstance.getVm().getIp(),
        				productInstance.getVm().getHostname(), 
        				productInstance.getVm().getDomain(), 
        				productInstance.getVm().getOsType()));
        
        productInstanceDto.setProduct(
        		new com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto(
        				productInstance.getProductRelease().getName(), 
        				productInstance.getProductRelease().getDescription(),
        				productInstance.getProductRelease().getVersion(), null, 
        				null, 
        				null, null));
        
        if (productInstance.getVdc() != null)
        	productInstanceDto.setVdc(productInstance.getVdc());
        
        productInstanceDto.setAttributes(
        		new ArrayList<com.telefonica.euro_iaas.sdc.model.Attribute>());
        
        if (productInstance.getPrivateAttributes() != null) {
        	for (Attribute attribute : productInstance.getPrivateAttributes()) {
            	productInstanceDto.getAttributes().add(
                        new com.telefonica.euro_iaas.sdc.model.Attribute(
                        		attribute.getKey(), attribute.getValue()));
            }
        }
                
        //Installing product with SDC
        productInstance.setStatus(Status.INSTALLING);
        com.telefonica.euro_iaas.sdc.model.Task task = service.install(productInstance.getVdc(), productInstanceDto, null);
        productInstance.setStatus(Status.INSTALLED);

        return productInstance;

	}

	@Override
	public void uninstall(ProductInstance productInstance) {
		
		String sdcServerUrl = systemPropertiesProvider.getProperty(SDC_SERVER_URL);
		String sdcMediaType =  systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE);
		
		//SDCClient client = new SDCClient();
        com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService productService =
        		sDCClient.getProductInstanceService(sdcServerUrl, sdcMediaType);
        
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
     * @param sDCClient
     *            the sDCClient to set
     */
    public void setSDCClient(SDCClient sDCClient) {
        this.sDCClient = sDCClient;
    }

    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }

}
