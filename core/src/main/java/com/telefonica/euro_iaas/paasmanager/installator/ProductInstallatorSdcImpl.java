package com.telefonica.euro_iaas.paasmanager.installator;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.SDC_SERVER_MEDIATYPE;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.SDC_SERVER_URL;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.installator.sdc.util.SDCUtil;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import com.telefonica.euro_iaas.sdc.client.SDCClient;

public class ProductInstallatorSdcImpl implements ProductInstallator {

	private SDCClient sDCClient;
    private SystemPropertiesProvider systemPropertiesProvider;
    private SDCUtil sDCUtil;
    
    private static Logger log = Logger.getLogger(ProductInstallatorSdcImpl.class);
    
	public ProductInstance install(TierInstance tierInstance, ProductRelease productRelease) 
		throws ProductInstallatorException {
		
		String sdcServerUrl = systemPropertiesProvider.getProperty(SDC_SERVER_URL);
		String sdcMediaType =  systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE);
		    
		//SDCClient client = new SDCClient();
        com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService pIService =
        		sDCClient.getProductInstanceService(sdcServerUrl, sdcMediaType);
        
        com.telefonica.euro_iaas.sdc.model.Task task = null;
        
        //From Paasmanager ProductRelease To SDC ProductInstanceDto
        com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto productInstanceDto 
        	= new com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto();

        productInstanceDto.setVm(
        		new com.telefonica.euro_iaas.sdc.model.dto.VM(
        				tierInstance.getVM().getFqn(), 
        				tierInstance.getVM().getIp(),
        				tierInstance.getVM().getHostname(), 
        				tierInstance.getVM().getDomain(), 
        				tierInstance.getVM().getOsType()));
        
        productInstanceDto.setProduct(
        		new com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto(
        				productRelease.getProduct(), 
        				productRelease.getVersion(),
        				"product"));
        
        if (tierInstance.getVdc() != null)
        	productInstanceDto.setVdc(tierInstance.getVdc());
        
        productInstanceDto.setAttributes(
        		new ArrayList<com.telefonica.euro_iaas.sdc.model.Attribute>());
        
        if (productRelease.getAttributes() != null) {
        	for (Attribute attribute : productRelease.getAttributes()) {
            	productInstanceDto.getAttributes().add(
                        new com.telefonica.euro_iaas.sdc.model.Attribute(
                        		attribute.getKey(), attribute.getValue()));
            }
        }
        
        productInstanceDto.getAttributes().add(
                new com.telefonica.euro_iaas.sdc.model.Attribute(
                		"id_web_server",tierInstance.getVM().getFqn().substring(0,tierInstance.getVM().getFqn().indexOf(".vees."))));
        productInstanceDto.getAttributes().add(
                new com.telefonica.euro_iaas.sdc.model.Attribute(
                		"app_server_role", tierInstance.getVM().getFqn().substring(0,tierInstance.getVM().getFqn().indexOf(".vees."))));
                
        //Installing product with SDC
        ProductInstance productInstance = new ProductInstance ();
        productInstance.setStatus(Status.INSTALLING);
        try{ 
        	task = pIService.install(tierInstance.getVdc(), productInstanceDto, null);
        } catch (Exception e) {
        	String errorMessage = " Error invokg SDC to Install Product" + 
        			productRelease.getName() + " " + productRelease.getVersion();
        	log.error(errorMessage);
        	throw new ProductInstallatorException (errorMessage);
        }
        
        
        productInstance.setName(tierInstance.getName()+"_"+productRelease.getProduct()+"_"+productRelease.getVersion());
        productInstance.setProductRelease(productRelease);
      //  productInstance.setTierInstance(tierInstance);
        productInstance.setVdc(tierInstance.getVdc());
        
        sDCUtil.checkTaskStatus(task, productInstance.getVdc());
        
        productInstance.setStatus(Status.INSTALLED);

        return productInstance;

	}

	public void installArtifact (ProductInstance productInstance, Artifact artifact)  throws 
	ProductInstallatorException
	{
		String sdcServerUrl = systemPropertiesProvider.getProperty(SDC_SERVER_URL);
		String sdcMediaType =  systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE);
		    
		//SDCClient client = new SDCClient();
        com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService service =
        		sDCClient.getProductInstanceService(sdcServerUrl, sdcMediaType);
        
        List<com.telefonica.euro_iaas.sdc.model.Attribute> atts = new ArrayList ();
        
        
        for (Attribute att: artifact.getAttributes())
        {
        	com.telefonica.euro_iaas.sdc.model.Attribute attsdc = 
        		new com.telefonica.euro_iaas.sdc.model.Attribute (att.getKey(), att.getValue(), att.getDescription()); 
        	atts.add(attsdc);
        }
        
        com.telefonica.euro_iaas.sdc.model.Artifact sdcArtifact= new com.telefonica.euro_iaas.sdc.model.Artifact 
        (artifact.getName(), atts);
                       
        //Installing product with SDC
        productInstance.setStatus(Status.DEPLOYING_ARTEFACT);
        com.telefonica.euro_iaas.sdc.model.Task task = service.installArtifact(productInstance.getVdc(), productInstance.getName(), sdcArtifact,null);
        /*How to catch an productInstallation error */
         if (task.getStatus()== com.telefonica.euro_iaas.sdc.model.Task.TaskStates.ERROR)
        	throw new ProductInstallatorException ("Error installing artefact " + artifact.getName() + " in product instance " + 
        			 productInstance.getProductRelease().getProduct() + ". Description: "
        			+ task.getError());
        
        productInstance.setStatus(Status.ARTEFACT_DEPLOYED);

	}
	
	public void uninstallArtifact(ProductInstance productInstance,
			Artifact artifact) throws ProductInstallatorException {
		String sdcServerUrl = systemPropertiesProvider.getProperty(SDC_SERVER_URL);
		String sdcMediaType =  systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE);
		    
		//SDCClient client = new SDCClient();
        com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService service =
        		sDCClient.getProductInstanceService(sdcServerUrl, sdcMediaType);
        
        List<com.telefonica.euro_iaas.sdc.model.Attribute> atts = new ArrayList ();
        
        
        for (Attribute att: artifact.getAttributes())
        {
        	com.telefonica.euro_iaas.sdc.model.Attribute attsdc = 
        		new com.telefonica.euro_iaas.sdc.model.Attribute (att.getKey(), att.getValue(), att.getDescription()); 
        	atts.add(attsdc);
        }
        
        com.telefonica.euro_iaas.sdc.model.Artifact sdcArtifact= new com.telefonica.euro_iaas.sdc.model.Artifact 
        (artifact.getName(), atts);
                       
        //Installing product with SDC
        productInstance.setStatus(Status.UNDEPLOYING_ARTEFACT);
        com.telefonica.euro_iaas.sdc.model.Task task = service.uninstallArtifact(productInstance.getVdc(), productInstance.getName(), sdcArtifact,null);
        /*How to catch an productInstallation error */
         if (task.getStatus()== com.telefonica.euro_iaas.sdc.model.Task.TaskStates.ERROR)
        	throw new ProductInstallatorException ("Error uninstalling artefact " + artifact.getName() + " in product instance " + 
        			 productInstance.getProductRelease().getProduct() + ". Description: "
        			+ task.getError());
        
        productInstance.setStatus(Status.ARTEFACT_UNDEPLOYED);
		
	}
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
        	productService.uninstall(productInstance.getVdc(), productInstance.getName(), null);    
        	//productService.uninstall(productInstance.getVdc(), productInstance.getId(), null);    
    		
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

    public void setSDCUtil(SDCUtil sDCUtil) {
        this.sDCUtil = sDCUtil;
    }

	public void configure(ProductInstance productInstance,
			 List<Attribute> properties) throws ProductInstallatorException {
		String sdcServerUrl = systemPropertiesProvider.getProperty(SDC_SERVER_URL);
		String sdcMediaType =  systemPropertiesProvider.getProperty(SDC_SERVER_MEDIATYPE);
		    
		//SDCClient client = new SDCClient();
        com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService pIService =
        		sDCClient.getProductInstanceService(sdcServerUrl, sdcMediaType);
        
        List<com.telefonica.euro_iaas.sdc.model.Attribute> arguments = new ArrayList ();
        
     
       for (Attribute attri: properties ){
         
          com.telefonica.euro_iaas.sdc.model.Attribute att =
        		  new com.telefonica.euro_iaas.sdc.model.Attribute ();
          att.setKey(attri.getKey());
         att.setKey(attri.getValue());
          arguments.add(att);
        }
        
        
        
        com.telefonica.euro_iaas.sdc.model.Task task = null;
        
        
        //Installing product with SDC
       
        try{ 
        	task = pIService.configure(productInstance.getVdc(), productInstance.getName(), null, arguments);
        } catch (Exception e) {
        	String errorMessage = " Error invokg SDC to configure Product" + 
        	productInstance.getName();
        	log.error(errorMessage);
        	throw new ProductInstallatorException (errorMessage);
        }
        
      
        sDCUtil.checkTaskStatus(task, productInstance.getVdc());
        
        return;
		
	}





}
