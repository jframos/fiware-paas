/*

  (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights
  Reserved.

  The copyright to the software program(s) is property of Telefonica I+D.
  The program(s) may be used and or copied only with the express written
  consent of Telefonica I+D or in accordance with the terms and conditions
  stipulated in the agreement/contract under which the program(s) have
  been supplied.

*/
package com.telefonica.euro_iaas.paasmanager.installator;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.REC_SERVER_MEDIATYPE;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.REC_SERVER_URL;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.ENVELOPE_TEMPLATE_LOCATION;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductReconfigurationException;
import com.telefonica.euro_iaas.paasmanager.installator.rec.client.RECManagerClient;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.RECACService;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.RECPICService;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.RECServiceService;
import com.telefonica.euro_iaas.paasmanager.installator.rec.services.RECVMService;
import com.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils;
import com.telefonica.euro_iaas.paasmanager.manager.TierInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.paasmanager.model.Artifact;
import com.telefonica.euro_iaas.paasmanager.model.Attribute;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductRelease;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.util.OVFUtils;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;


/**
 * @author jesus.movilla
 *
 */
public class ProductInstallatorRECManagerImpl implements ProductInstallator {

	
	private SystemPropertiesProvider systemPropertiesProvider;
	
	private TierInstanceManager tierInstanceManager;
    
    private RECServiceService recServiceService;
    private RECVMService recVMService;
    private RECPICService recPICService;
    private RECACService recACService;
    
    private VappUtils vappUtils;
    private OVFUtils ovfUtils;
    
    private static Logger log = Logger.getLogger(ProductInstallatorRECManagerImpl.class);
    
	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.util.ProductInstallator#install(com.telefonica.euro_iaas.paasmanager.model.ProductInstance)
	 */
	public void configure(ProductInstance productInstance,
			 List<Attribute> properties) throws EntityNotFoundException, 
			 ProductInstallatorException, ProductReconfigurationException {
		
		String baseUrl = systemPropertiesProvider.getProperty(REC_SERVER_URL);
		String mediaType =  systemPropertiesProvider.getProperty(REC_SERVER_MEDIATYPE);
		String tierInstanceName = productInstance.getName().split("_",2)[0];
		TierInstance tierInstance = tierInstanceManager.load(tierInstanceName);
		String vmOVF = tierInstance.getVM().getVmOVF();
		String vapp = tierInstance.getVM().getVapp();
		
		//vapp =fromOVFtoRECFormat(vapp);
		
		RECManagerClient client = new RECManagerClient();
		
		String serviceFile = systemPropertiesProvider
				.getProperty(ENVELOPE_TEMPLATE_LOCATION);
		
		String appId, picId;
		String envelopeVapp = null;
		String recVapp = null;
		String recVMname;
		List<String> recPICs = new ArrayList<String>();

		
		try {
			String serviceName = ovfUtils.getServiceName(vmOVF);

			//From where?
			//appId = vappUtils.getAppId(vapp);
			appId = serviceName;
			envelopeVapp = vappUtils
					.getEnvelopeTypeSegment(serviceFile, appId);
			String password = vappUtils.getPassword(vapp);
			String login = vappUtils.getLogin(vapp);
			String ip = tierInstance.getVM().getIp();
			vmOVF = fromOVFtoRECFormat(vmOVF);
			recVapp = vappUtils.getRECVapp(
					vmOVF, ip, login, password);
			recVMname = ovfUtils.getRECVMNameFromProductSection(recVapp);
			recPICs = vappUtils.getPICProductSections(
					vmOVF);

		} catch (InvalidOVFException e) {
			String msg =" Error obtainng data from vapp/ovf. Desc : " + e.getMessage();
			log.error (msg);
			throw new ProductInstallatorException(msg);
		}
		
		recServiceService = client.getRECServiceService(baseUrl, mediaType);
		recServiceService.configureService(envelopeVapp, appId, null);
		
		//Sacar Vapp from productInstance
		if (recVapp != null) {
			recVMService = client.getRECVMService(baseUrl, mediaType);
			recVMService.configureVM(recVapp, recVMname,
			appId);
		}
		
		//Sacar PIC ProducIntance from productInstance
		recPICService = client.getRECPICService(baseUrl, mediaType);
		
		for (int i= 0; i < recPICs.size(); i++) {
			try {
				picId = vappUtils.getPicId(recPICs.get(i));
			} catch (InvalidOVFException e) {
				String msg =" Error obtaining picId from ovf";
				log.error (msg);
				throw new ProductInstallatorException(msg);
			}
			recPICService.configurePIC(recPICs.get(i), appId, picId,
					recVMname);
		}

		
	}
    
    
	public ProductInstance install(TierInstance tierInstance,
			ProductRelease productRelease)
			throws ProductInstallatorException {
		
		String baseUrl = systemPropertiesProvider.getProperty(REC_SERVER_URL);
		String mediaType =  systemPropertiesProvider.getProperty(REC_SERVER_MEDIATYPE);
		String vmOVF = tierInstance.getVM().getVmOVF();
		String vapp = tierInstance.getVM().getVapp();
		
		//vapp =fromOVFtoRECFormat(vapp);
		
		RECManagerClient client = new RECManagerClient();
		
		String serviceFile = systemPropertiesProvider
				.getProperty(ENVELOPE_TEMPLATE_LOCATION);
		
		String appId, acId;
		String picId = null;
		String envelopeVapp = null;
		String recVapp = null;
		String recVMname;
		List<String> recACs = new ArrayList<String>();
		
		try {
			String serviceName = ovfUtils.getServiceName(vmOVF);

			appId = serviceName;
			envelopeVapp = vappUtils
					.getEnvelopeTypeSegment(serviceFile, appId);
			String password = vappUtils.getPassword(vapp);
			String login = vappUtils.getLogin(vapp);
			String ip = tierInstance.getVM().getIp();
			vmOVF = fromOVFtoRECFormat(vmOVF);
			recVapp = vappUtils.getRECVapp(
					vmOVF, ip, login, password);
			recVMname = ovfUtils.getRECVMNameFromProductSection(recVapp);

		} catch (InvalidOVFException e) {
			String msg =" Error obtainng data from vapp/ovf. Desc : " + e.getMessage();
			log.error (msg);
			throw new ProductInstallatorException(msg);
		}
		
		recServiceService = client.getRECServiceService(baseUrl, mediaType);
		recServiceService.createService(envelopeVapp, appId, null);
		
		//Sacar Vapp from productInstance
		if (recVapp != null) {
			recVMService = client.getRECVMService(baseUrl, mediaType);
			recVMService.createVM(recVapp, recVMname,
			appId);
		}
		
		//Sacar PIC ProducIntance from productInstance
		recPICService = client.getRECPICService(baseUrl, mediaType);
		
		for (int j=0; j < productRelease.getAttributes().size(); j++) {
			Attribute attribute = productRelease.getAttributes().get(j);
				
			if (attribute.getKey().equals(VappUtils.KEYATTRIBUTE_VALUE_ID)){
				picId = attribute.getValue();	
			}
		}	
		
		String recPIC = vappUtils.getPICProductSection(picId, vmOVF);

		recPICService.createPIC(recPIC, appId, picId, recVMname);	
		//Installing ACS associated to PIC(i)
		recACs= vappUtils.getACProductSectionsByPicId(vmOVF, picId);
			
		recACService = client.getRECACService(baseUrl, mediaType);		
		for (int j= 0; j < recACs.size(); j++) {
			try {
				acId = vappUtils.getAcId(recACs.get(j));
				picId = vappUtils.getPicIdFromAC(recACs.get(j));
			} catch (InvalidOVFException e) {
					String msg =" Error obtaining data acId/picId from ovf ";
					log.error (msg);
					throw new ProductInstallatorException(msg);
			}		
			recACService.createAC(recACs.get(j), appId, picId, recVMname, acId);
		}		
		
		ProductInstance productInstance = new ProductInstance ();
	    productInstance.setStatus(Status.INSTALLED);
	    productInstance.setName(tierInstance.getName()+"_"+productRelease.getProduct()+"_"+productRelease.getVersion());
	    productInstance.setProductRelease(productRelease);
	  //  productInstance.setTierInstance(tierInstance);
	    productInstance.setVdc(tierInstance.getVdc());
		
		return productInstance;
	}
	
	/* (non-Javadoc)
	 * @see com.telefonica.euro_iaas.paasmanager.installator.ProductInstallator#uninstall(com.telefonica.euro_iaas.paasmanager.model.ProductInstance)
	 */
	public void uninstall(ProductInstance productInstance) {
		// TODO Auto-generated method stub
		
	}
	
	private String fromOVFtoRECFormat (String vapp) {
		
		vapp = vapp.replace ("VirtualSystem ovf:id", "VirtualSystem xmlns:ovfenvelope=\"http://schemas.dmtf.org/ovf/envelope/1\" ovfenvelope:id");
		vapp = vapp.replace( "ovf:VirtualSystem", "ovfenvelope:VirtualSystem");
		vapp = vapp.replace( "ovf:id", "ovfenvelope:id");
		vapp = vapp.replace( "ovf:Info", "ovfenvelope:Info");
		vapp = vapp.replace( "ovfenvelope:ovfenvelope:Info", "ovfenvelope:Info");
		
		vapp = vapp.replace( "ovf:OperatingSystemSection", "ovfenvelope:OperatingSystemSection");
		vapp = vapp.replace( "<Info>", "<ovfenvelope:Info>");
		vapp = vapp.replace( "<Description>", "<ovfenvelope:Description>");
		vapp = vapp.replace( "</Info>", "</ovfenvelope:Info>");
		vapp = vapp.replace( "</Description>", "</ovfenvelope:Description>");
		
		return vapp;
	}
    // //////////// I.O.C /////////////
    /**
     * @param sDCClient
     *            the sDCClient to set
     */
    
    /**
     * @param systemPropertiesProvider
     *            the systemPropertiesProvider to set
     */
    public void setSystemPropertiesProvider(SystemPropertiesProvider systemPropertiesProvider) {
        this.systemPropertiesProvider = systemPropertiesProvider;
    }
    
    /**
     * @param vappUtils
     *            the vappUtils to set
     */
    public void setVappUtils(VappUtils vappUtils) {
        this.vappUtils = vappUtils;
    }

    /**
     * @param OVFUtils
     *            the OVFUtils to set
     */
    public void setOvfUtils(OVFUtils ovfUtils) {
        this.ovfUtils = ovfUtils;
    }

	
	public void installArtifact(ProductInstance productInstance,
			Artifact artifact) throws ProductInstallatorException {
		// TODO Auto-generated method stub
		
	}

	public void uninstallArtifact(ProductInstance productInstance,
			Artifact artifact) throws ProductInstallatorException {
		// TODO Auto-generated method stub
		
	}


}
