package com.telefonica.euro_iaas.paasmanager.manager.impl;

import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_OVFSERVICE_LOCATION;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDC_CPU;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDC_DISK;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.NEOCLAUDIA_VDC_MEM;
import static com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider.VM_DEPLOYMENT_DELAY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.telefonica.claudia.smi.URICreation;
import com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient;
import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaRetrieveInfoException;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidOVFException;
import com.telefonica.euro_iaas.paasmanager.exception.ProductInstallatorException;
import com.telefonica.euro_iaas.paasmanager.installator.rec.util.VappUtils;
import com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Environment;
import com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance;
import com.telefonica.euro_iaas.paasmanager.model.ProductInstance;
import com.telefonica.euro_iaas.paasmanager.model.Template;
import com.telefonica.euro_iaas.paasmanager.model.Tier;
import com.telefonica.euro_iaas.paasmanager.model.TierInstance;
import com.telefonica.euro_iaas.paasmanager.model.dto.VM;
import com.telefonica.euro_iaas.paasmanager.monitoring.MonitoringClient;
import com.telefonica.euro_iaas.paasmanager.util.ClaudiaResponseAnalyser;
import com.telefonica.euro_iaas.paasmanager.util.OVFUtils;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;

public class InfrastructureManagerClaudiaImpl implements InfrastructureManager {

	private static final long POLLING_INTERVAL = 10000;

	private SystemPropertiesProvider systemPropertiesProvider;
	private ClaudiaClient claudiaClient;
	private MonitoringClient monitoringClient;
	private ClaudiaResponseAnalyser claudiaResponseAnalyser;
	private ClaudiaUtil claudiaUtil;
	private OVFUtils ovfUtils;
	private VappUtils vappUtils;
	private static final String type = "application/xml";

	/** The log. */
	private static Logger log = Logger
			.getLogger(InfrastructureManagerClaudiaImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager#
	 * createEnvironment(java.lang.String)
	 */
	public List<VM> createEnvironment(EnvironmentInstance envInstance, Tier tier,
			String ovf, ClaudiaData claudiaData) throws InfrastructureException {

		List<VM> vms = new ArrayList<VM>();

		String service = null;
		try {
			service = ovfUtils.getServiceName(ovf);
			claudiaData.setService(service);
		} catch (InvalidOVFException e) {
			String errorMessage = "Error getting the service name from the "
					+ "incoming ovf xml-structured. Description. "
					+ e.getMessage();
			log.error(errorMessage);
			throw new InfrastructureException(errorMessage);
		}

		// Deploy MVs
		deployVDC(claudiaData);
		insertService(claudiaData);

		List<String> ovfSingleVM;
		try {
			ovfSingleVM = ovfUtils.getOvfsSingleVM(ovf);
		} catch (InvalidOVFException e) {
			String errorMessage = "Error splitting up the main ovf in single"
					+ "VM ovfs. Description. " + e.getMessage();
			log.error(errorMessage);
			throw new InfrastructureException(errorMessage);
		}

		for (int i = 0; i < ovfSingleVM.size(); i++) {

			String vmOVF = ovfSingleVM.get(i);
			VM vm = deployVM(claudiaData, tier, vmOVF, 1);
			vms.add(vm);
		}
		envInstance.setVapp(browseService(claudiaData));

		return vms;
	}

	public EnvironmentInstance createInfrasctuctureEnvironmentInstance(
			Environment environment, String ovf, ClaudiaData claudiaData)
			throws InfrastructureException {

		EnvironmentInstance environmentInstance = new EnvironmentInstance();
		environmentInstance.setEnvironment(environment);

		String service = null;
		try {
			service = ovfUtils.getServiceName(ovf);
			
		} catch (InvalidOVFException e) {
			String errorMessage = "Error getting the service name from the "
					+ "incoming ovf xml-structured. Description. "
					+ e.getMessage();
			log.error(errorMessage);
			service = claudiaData.getVdc() + "-" + environment.getName();
			//throw new InfrastructureException(errorMessage);
		}
		
		environmentInstance.setName(service);
		claudiaData.setService(service);

		// Deploy MVs
		deployVDC(claudiaData);
		insertService(claudiaData);

		List<String> ovfSingleVM = null;
		try {
			ovfSingleVM = ovfUtils.getOvfsSingleVM(ovf);
		} catch (InvalidOVFException e) {
			String errorMessage = "Error splitting up the main ovf in single"
					+ "VM ovfs. Description. " + e.getMessage();
			log.error(errorMessage);
		//	throw new InfrastructureException(errorMessage);
		}
		int numberTier = 0;
		for (Tier tier : environment.getTiers()) {
			for (int numReplica = 1; numReplica <= tier
					.getInitial_number_instances(); numReplica++) {
				claudiaData.setVm(tier.getName());
				
				TierInstance tierInstance = new TierInstance();
				VM vm = null;
				if (ovfSingleVM == null || ovfSingleVM.size()==0)
				 vm = deployVM(claudiaData, tier, null, numReplica);
				else
					vm = deployVM(claudiaData, tier, ovfSingleVM.get(numberTier), numReplica);
				tierInstance.setVM(vm);
				tierInstance.setNumberReplica(numReplica);
				tierInstance.setVdc(claudiaData.getVdc());
				tierInstance.setTier(tier);
				if (ovfSingleVM!= null && ovfSingleVM.size () == 0 
						&& ovfSingleVM.get(numberTier) != null)
					tierInstance.setOvf(ovfSingleVM.get(numberTier));
				
				tierInstance.setName(environment.getName() + "-"
						+ tier.getName() + "-" + numReplica);
				// tierInstance.setEnvironmentInstance(environmentInstance);
				environmentInstance.addTierInstance(tierInstance);

			}
			numberTier++;
		}

		environmentInstance.setVapp(browseService(claudiaData));

		return environmentInstance;
	}

	public VM deployVM(ClaudiaData claudiaData, Tier tier, String vmOVF, int replicaNumber)
			throws InfrastructureException {

		VM vm = null;
		if (vmOVF == null) {
			String errorMessage = "The VEE OVF could not be procesed, "
					+ "the OVF is null";
			log.warn(errorMessage);
			//throw new InfrastructureException(errorMessage);
		}
		String simpleVmOVF = vmOVF;
		if (vmOVF != null)
		{
			while (simpleVmOVF.contains("ovfenvelope:ProductSection"))
				simpleVmOVF = deleteProductSection(simpleVmOVF);
			simpleVmOVF = changeInitialResources(simpleVmOVF);
			if(replicaNumber!=1)
				simpleVmOVF = deleteRules(simpleVmOVF);
		}
		
		//deployVM(claudiaData, vmOVF);
		deployVM(claudiaData, tier, simpleVmOVF);
		// Conseguir el nombre de la maquina
		String vmName = null;
		String vAppReplica = null;
		String ip = null;
		String fqn = null;
		String networks = null;

		try {
			if (tier == null || tier.getName()==null)
				vmName = getVMNameFromSingleOVF(vmOVF);
			
		//	String osType = ovfUtils.getOSType(vmOVF);

			vAppReplica = claudiaClient.browseVMReplica(claudiaData, ""
					+ replicaNumber);
			log.info("vAppReplica replica " + vAppReplica);

			ip = vappUtils.getIP(vAppReplica);
			log.info("IP replica " + ip);
			
			
			// To-Do
			// networks = vappUtils.getNetworks(vAppReplica);

			fqn = getFQNPaas(claudiaData, replicaNumber);
			log.info("fqn replica " + fqn);
			// Meter un desfase configurable a eliminar después de la demo
			introduceDelay(Long.valueOf(systemPropertiesProvider
					.getProperty(VM_DEPLOYMENT_DELAY)));

			// Inserting VApp

			monitoringClient.startMonitoring(fqn);
			vm = new VM(fqn, ip, "" + replicaNumber, null, null, vmOVF,
					vAppReplica);
			vm = new VM(fqn, ip, vmName, null, null, vmOVF, vAppReplica);
			vm.setNetworks(networks);

		} catch (InvalidOVFException e) {
			String errorMessage = "Error obteining the VM name or OsType "
					+ "from the sing VM ovf. Description. " + e.getMessage();
			log.error(errorMessage);
			throw new InfrastructureException(errorMessage);

		} catch (ClaudiaResourceNotFoundException e) {
			String errorMessage = "Error browsing the replica which org: "
					+ claudiaData.getOrg() + ", vdc: " + claudiaData.getVdc()
					+ ", service: " + claudiaData.getService() + ", VM name:"
					+ vmName + ", service: " + claudiaData.getService() + ", "
					+ "number of replica: 1. Description. " + e.getMessage();
			log.error(errorMessage);
			throw new InfrastructureException(errorMessage);

		} catch (ProductInstallatorException e) {
			String errorMessage = "Error getting the IP from de Vapp: "
					+ vAppReplica + ". Description. " + e.getMessage();
			log.error(errorMessage);
			throw new InfrastructureException(errorMessage);

		}

		return vm;
	}

	public String ImageScalability(ClaudiaData claudiaData)
			throws InfrastructureException {

		String scaleResponse;
		try {
			scaleResponse = claudiaClient.createImage(claudiaData);
		} catch (ClaudiaRetrieveInfoException e) {
			String errorMessage = "Error creating teh image of the VM with the "
					+ "fqn: "
					+ claudiaData.getFqn()
					+ ". Descrption. "
					+ e.getMessage();
			log.error(errorMessage);
			throw new InfrastructureException(errorMessage);
		}
		return scaleResponse;
	}

	public String StartStopScalability(ClaudiaData claudiaData, boolean b)
			throws InfrastructureException {
		String scalalility = claudiaClient.OnOffScalability(claudiaData,
				claudiaData.getService(), b);
		return scalalility;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager#
	 * deleteEnvironment
	 * (com.telefonica.euro_iaas.paasmanager.model.EnvironmentInstance)
	 */
	public void deleteEnvironment(ClaudiaData claudiaData,
			EnvironmentInstance envInstance) throws InfrastructureException {

		// Sacar todas la VMs del EnvironmentInstance y borrarlas
		List<VM> vms = new ArrayList<VM>();
		List<TierInstance> tierInstances = envInstance.getTierInstances();

		if (tierInstances == null)
			return;
		for (int i = 0; i < tierInstances.size(); i++) {
			TierInstance tierInstance = tierInstances.get(i);
			vms.add(tierInstance.getVM());
		}

		String fqn = envInstance.getTierInstances().get(0).getVM().getFqn();
		String service = URICreation.getService(fqn);

		claudiaData.setService(service);
		claudiaData.setFqn(fqn);

		// Delete all VM
		for (int i = 0; i < vms.size(); i++) {
			claudiaClient.undeployVMReplica(vms.get(i).getFqn(), "1");
			monitoringClient.stopMonitoring(vms.get(i).getFqn());
		}
	}

	public void deleteVMReplica(ClaudiaData claudiaData,
			TierInstance tierInstance) throws InfrastructureException {

		String fqn = getFQNPaas(claudiaData, tierInstance.getNumberReplica());
		claudiaData.setFqn(fqn);

		claudiaClient.undeployVMReplica(fqn, tierInstance.getNumberReplica()+"");
		monitoringClient.stopMonitoring(fqn);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telefonica.euro_iaas.paasmanager.manager.InfrastructureManager#
	 * cloneTemplate(java.lang.String)
	 */
	@Async
	public TierInstance cloneTemplate(String templateName)
			throws InfrastructureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Async
	public Template createTemplate(TierInstance tierInstance)
			throws InfrastructureException {
		// TODO Auto-generated method stub
		return null;
	}

	private String getFQNPaas(ClaudiaData claudiaData, int replica) {
		return claudiaData.getOrg().replace("_", ".") + ".customers."
				+ claudiaData.getVdc() + ".services."
				+ claudiaData.getService() + ".vees." + claudiaData.getVm()
				+ ".replicas." + replica;
	}

	/**
	 * 
	 * @param taskUrl
	 * @throws InfrastructureException
	 */
	private void checkTaskResponse(ClaudiaData claudiaData, String taskUrl)
			throws InfrastructureException {
		while (true) {
			String claudiaTask;
			try {
				claudiaTask = claudiaUtil.getClaudiaResource(
						claudiaData.getUser(), taskUrl, MediaType.WILDCARD);

				if (claudiaTask.contains("success")) {
					try {
						Thread.sleep(POLLING_INTERVAL);
					} catch (InterruptedException e) {
						String errorThread = "Thread Interrupted Exception "
								+ "during polling";
						log.warn(errorThread);
						throw new InfrastructureException(errorThread);
					}
					break;
				} else if (claudiaTask.contains("error")) {
					String errorMessage = "Error checking task " + taskUrl;
					log.error(errorMessage);
					throw new InfrastructureException(errorMessage);
				}
			} catch (ClaudiaRetrieveInfoException e1) {
				String errorMessage = "Error checking task " + taskUrl;
				log.error(errorMessage);
				throw new InfrastructureException(errorMessage);
			} catch (ClaudiaResourceNotFoundException e) {
				String errorMessage = "Error checking task " + taskUrl;
				log.error(errorMessage);
				throw new InfrastructureException(errorMessage);
			}
			try {
				Thread.sleep(POLLING_INTERVAL);
			} catch (InterruptedException e) {
				String errorMessage = "Interrupted Exception during polling";
				log.warn(errorMessage);
				throw new InfrastructureException(errorMessage);
			}
		}
	}

	/**
	 * Create an VDC if it is not created.
	 * 
	 * @param org
	 * @param vdc
	 * @throws InfrastructureException
	 */
	private void deployVDC(ClaudiaData claudiaData)
			throws InfrastructureException {
		// VDC
		try {
			claudiaClient.browseVDC(claudiaData);
		} catch (ClaudiaResourceNotFoundException e) {

			String deployVDCResponse = claudiaClient.deployVDC(claudiaData,
					systemPropertiesProvider.getProperty(NEOCLAUDIA_VDC_CPU),
					systemPropertiesProvider.getProperty(NEOCLAUDIA_VDC_MEM),
					systemPropertiesProvider.getProperty(NEOCLAUDIA_VDC_DISK));
			String vdcTaskUrl = claudiaResponseAnalyser
					.getTaskUrl(deployVDCResponse);

			if (claudiaResponseAnalyser.getTaskStatus(deployVDCResponse)
					.equals("error")) {
				String errorMes = "Error deploying VDC " + claudiaData.getVdc();
				log.error(errorMes);
				throw new InfrastructureException(errorMes);
			}
			if (!(claudiaResponseAnalyser.getTaskStatus(deployVDCResponse)
					.equals("success")))
				checkTaskResponse(claudiaData, vdcTaskUrl);
		}
	}

	/**
	 * Cretae a Service
	 * 
	 * @param org
	 * @param vdc
	 * @param service
	 * @throws InfrastructureException
	 */
	private void insertService(ClaudiaData claudiaData)
			throws InfrastructureException {

		// Service
		String serviceResponse;
		try {
			serviceResponse = claudiaClient.browseService(claudiaData);
		} catch (ClaudiaResourceNotFoundException e) {

			String deployServiceResponse = claudiaClient.deployService(
					claudiaData, systemPropertiesProvider
							.getProperty(NEOCLAUDIA_OVFSERVICE_LOCATION));

			String serviceTaskUrl = claudiaResponseAnalyser
					.getTaskUrl(deployServiceResponse);

			if (claudiaResponseAnalyser.getTaskStatus(deployServiceResponse)
					.equals("error")) {
				String errorMesServ = "Error deploying Service "
						+ claudiaData.getService();
				log.error(errorMesServ);
				throw new InfrastructureException(errorMesServ);
			}

			if (!(claudiaResponseAnalyser.getTaskStatus(deployServiceResponse)
					.equals("success")))
				checkTaskResponse(claudiaData, serviceTaskUrl);
		}
	}

	/**
	 * deloy a VM from an ovf
	 * 
	 * @param org
	 * @param vdc
	 * @param service
	 * @param vmOVF
	 * @throws InfrastructureException
	 */
	private void deployVM(ClaudiaData claudiaData, Tier tier, String vmOVF)
			throws InfrastructureException {

		String deployVMResponse = claudiaClient.deployVM(claudiaData, vmOVF);
		String taskUrl = claudiaResponseAnalyser.getTaskUrl(deployVMResponse);

		if (claudiaResponseAnalyser.getTaskStatus(deployVMResponse).equals(
				"error")) {
			String errorMsgVM = "Error deploying VM ";
			log.error(errorMsgVM);
			throw new InfrastructureException(errorMsgVM);
		}

		if (!(claudiaResponseAnalyser.getTaskStatus(deployVMResponse)
				.equals("success")))
			checkTaskResponse(claudiaData, taskUrl);
	}

	/**
	 * deloy a VM from an ovf
	 * 
	 * @param org
	 * @param vdc
	 * @param service
	 * @throws InfrastructureException
	 */
	private String browseService(ClaudiaData claudiaData)
			throws InfrastructureException {

		String browseServiceResponse = null;
		try {
			browseServiceResponse = claudiaClient.browseService(claudiaData);
		} catch (ClaudiaResourceNotFoundException crnfe) {
			String errorMessage = "Resource associated to org:"
					+ claudiaData.getOrg() + " vdc:" + claudiaData.getVdc()
					+ " service:" + claudiaData.getService()
					+ " Error Description: " + crnfe.getMessage();
			log.error(errorMessage);
			throw new InfrastructureException(errorMessage);
		} catch (Exception e) {
			String errorMessage = "Unknown exception when retriving vapp "
					+ " associated to org:" + claudiaData.getOrg() + " vdc:"
					+ claudiaData.getVdc() + " service:"
					+ claudiaData.getService() + " Error Description: "
					+ e.getMessage();
			log.error(errorMessage);
			throw new InfrastructureException(errorMessage);
		}
		return browseServiceResponse;
	}

	
	public String updateVmOvf(String ovf, String imageName) {

		if (imageName != null) {

			String[] part_inicio = ovf.split("<References>", 2);
			String[] part_final = part_inicio[1].split("</References>", 2);
			String[] part_middle = part_final[0].split(" ");
			part_middle[2] = "ovf:href=\"" + imageName + "\"";
			String middle = "";
			for (int i = 0; part_middle.length > i; i++) {
				middle = middle + " " + part_middle[i];
			}
			ovf = part_inicio[0] + "<References>" + middle + "</References>"
					+ part_final[1];
			// cambiamos el operating sistem Section
			String[] part_inicio2 = ovf.split("<ovf:OperatingSystemSection", 2);
			// Tenemos la prat_inicio[0] bien
			String[] part_final2 = part_inicio2[1].split(
					"</ovf:OperatingSystemSection>", 2);
			String[] part_middle2 = part_final2[0].split("<Description>", 2);
			String middle2 = part_middle2[0] + "<Description>" + imageName
					+ "</Description>";

			ovf = part_inicio2[0] + "<ovf:OperatingSystemSection" + middle2
					+ "</ovf:OperatingSystemSection>" + part_final2[1];

			// Ahora hay que cambiar los valores de la VM
		}

		String ovfFinal = changeInitialResources(ovf);

		while (ovfFinal.contains("ovfenvelope:ProductSection"))
			ovfFinal = deleteProductSection(ovfFinal);
		while (ovfFinal.contains("rsrvr:GovernanceRuleSection"))
			ovfFinal = deleteRules(ovfFinal);

		return ovfFinal;
	}

	private String deleteRules(String ovf) {
		String[] part_inicio = ovf.split("<rsrvr:GovernanceRuleSection", 2);
		String[] part_final = part_inicio[1].split(
				"</rsrvr:GovernanceRuleSection>", 2);
		String ovfNew = part_inicio[0] + part_final[1];
		return ovfNew;
	}

	public String deleteProductSection(String ovf) {
		String[] part_inicio = ovf.split("<ovfenvelope:ProductSection", 2);
		String[] part_final = part_inicio[1].split(
				"</ovfenvelope:ProductSection>", 2);
		String ovfNew = part_inicio[0] + part_final[1];
		return ovfNew;
	}

	private String changeInitialResources(String ovf) {
		String[] part_inicio = ovf.split("<ovf:VirtualSystem ovf:id=", 2);
		String[] part_final = part_inicio[1].split(">", 2);
		// Modificamos part final[0]
		String[] part_middle = part_final[0].split(" ", 2);// por un lado lo que
															// es, y por otro a
															// cambiar
		String middle = part_middle[0]
				+ "  rsrvr:initial=\"1\" rsrvr:max=\"1\" rsrvr:min=\"1\">";
		String ovfChanged = part_inicio[0] + "<ovf:VirtualSystem ovf:id="
				+ middle + part_final[1];
		return ovfChanged;
	}

	/**
	 * 
	 * @param ovf
	 * @return
	 */
	private String getVMNameFromSingleOVF(String ovf)
			throws InvalidOVFException {

		String vmname = null;
		log.info("ovf= " + ovf);
		try {
			Document doc = claudiaUtil.stringToDom(ovf);
			Node virtualSystem = doc.getElementsByTagName(
					ovfUtils.VIRTUAL_SYSTEM_TAG).item(0);

			vmname = virtualSystem.getAttributes()
					.getNamedItem(ovfUtils.VIRTUAL_SYSTEM_ID).getTextContent();
		} catch (SAXException e) {
			throw new InvalidOVFException(e.getMessage());
		} catch (ParserConfigurationException e) {
			throw new InvalidOVFException(e.getMessage());
		} catch (IOException e) {
			throw new InvalidOVFException(e.getMessage());
		} catch (Exception e) {
			throw new InvalidOVFException(e.getMessage());
		}

		return vmname;
	}

	/**
	 * Introducing some delay after vm deployment (only for old claudia)
	 * 
	 * @param delay
	 * @throws InfrastructureException
	 */
	private void introduceDelay(Long delay) throws InfrastructureException {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			String errorThread = "Thread Interrupted Exception "
					+ "during delay after vm deployment";
			log.warn(errorThread);
			throw new InfrastructureException(errorThread);
		}
	}

	/**
	 * @param systemPropertiesProvider
	 *            the systemPropertiesProvider to set
	 * @param monitoringClient
	 *            the monitoringClient to set
	 */
	public void setMonitoringClient(MonitoringClient monitoringClient) {
		this.monitoringClient = monitoringClient;
	}

	public void setSystemPropertiesProvider(
			SystemPropertiesProvider systemPropertiesProvider) {
		this.systemPropertiesProvider = systemPropertiesProvider;
	}

	/**
	 * @param claudiaUtil
	 *            the claudiaUtil to set
	 */
	public void setClaudiaUtil(ClaudiaUtil claudiaUtil) {
		this.claudiaUtil = claudiaUtil;
	}

	/**
	 * @param claudiaClient
	 *            the claudiaClient to set
	 */

	public void setClaudiaClient(ClaudiaClient claudiaClient) {
		this.claudiaClient = claudiaClient;
	}

	/**
	 * @param ClaudiaResponseAnalyser
	 *            the ClaudiaResponseAnalyser to set
	 */
	public void setClaudiaResponseAnalyser(
			ClaudiaResponseAnalyser claudiaResponseAnalyser) {
		this.claudiaResponseAnalyser = claudiaResponseAnalyser;
	}

	/**
	 * @param OVFUtils
	 *            the OVFUtils to set
	 */
	public void setOvfUtils(OVFUtils ovfUtils) {
		this.ovfUtils = ovfUtils;
	}

	/**
	 * @param VappUtils
	 *            the VappUtils to set
	 */
	public void setVappUtils(VappUtils vappUtils) {
		this.vappUtils = vappUtils;
	}

	public List<VM> createEnvironment(EnvironmentInstance envInstance,
			String ovf, ClaudiaData claudiaData) throws InfrastructureException {
		// TODO Auto-generated method stub
		return null;
	}


}
