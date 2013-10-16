/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.manager;

import com.telefonica.euro_iaas.paasmanager.claudia.ClaudiaClient;
import com.telefonica.euro_iaas.paasmanager.claudia.util.ClaudiaUtil;
import com.telefonica.euro_iaas.paasmanager.manager.impl.InfrastructureManagerClaudiaImpl;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.dto.PaasManagerUser;
import com.telefonica.euro_iaas.paasmanager.util.ClaudiaResponseAnalyser;
import com.telefonica.euro_iaas.paasmanager.util.SystemPropertiesProvider;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jesus.movilla
 */
public class InfrastructureManagerImplTest extends TestCase {

    private String vdc, org;
    private int number_vms;
    private SystemPropertiesProvider propertiesProvider;
    private ClaudiaClient claudiaClient;
    private ClaudiaUtil claudiaUtil;
    private ClaudiaResponseAnalyser claudiaResponseAnalyser;

    private String vdcResponse = "vdcResponse";

    private String vdcNotFoundResponse = "ElementNotFound";

    private String serviceResponse = "serviceResponse";

    // private String ovfname = "Case01-01-initial-vapp-creation.xml";
    private String ovfname = "4caastovfexample.xml";
    private String ovf = "ovf";
    private PaasManagerUser user;
    private ClaudiaData claudiaData;
    private InfrastructureManagerClaudiaImpl manager;

    @Before
    public void setUp() throws Exception {

        /*
         * vdc = "paasmanagerVDC"; org = "ORG"; number_vms = 2; claudiaData = new ClaudiaData(org, vdc); //Taking ovf
         * from a file InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(ovfname); BufferedReader
         * reader = new BufferedReader(new InputStreamReader(is)); StringBuffer ruleFile = new StringBuffer(); String
         * actualString; while ((actualString = reader.readLine()) != null) {
         * ruleFile.append(actualString).append("\n"); } // user = new PaasManagerUser("user", "paasword", null);
         * //claudiaData.setUser(user); // ovf = ruleFile.toString(); // System.out.println("ovf: " + ovf); //
         * vdcResponseTask = new Task(); vdcResponseTask.setState(TaskStates.SUCCESS); vdcResponseTask.setResource
         * ("http://10.95.171.89:8080/rest-api-management/", "resourceType"); serviceResponseTask = new Task();
         * serviceResponseTask.setState(TaskStates.SUCCESS); serviceResponseTask.
         * setResource("http://10.95.171.89:8080/rest-api-management/", "resourceType"); vmResponseTask = new Task();
         * vmResponseTask.setState(TaskStates.SUCCESS); vmResponseTask.setResource
         * ("http://10.95.171.89:8080/rest-api-management/", "resourceType"); propertiesProvider =
         * mock(SystemPropertiesProvider.class); when(propertiesProvider
         * .getProperty(NEOCLAUDIA_SERVICE)).thenReturn("paasmanagerService"); when
         * (propertiesProvider.getProperty(NEOCLAUDIA_VDC_CPU)).thenReturn("12" );
         * when(propertiesProvider.getProperty(NEOCLAUDIA_VDC_MEM)).thenReturn ("14");
         * when(propertiesProvider.getProperty(NEOCLAUDIA_VDC_DISK)).thenReturn ("16");
         * when(propertiesProvider.getProperty(NEOCLAUDIA_ORG)).thenReturn ("EUROPIAAS-VC1");
         * when(propertiesProvider.getProperty(NEOCLAUDIA_OVFSERVICE_LOCATION )).thenReturn("empty.ovf");
         * when(propertiesProvider.getProperty(VM_NAME_PREFIX )).thenReturn("paasManagerVM"); claudiaClient =
         * mock(ClaudiaClient.class); //when(claudiaClient.browseVDC(any(String.class), any(String.class), user))
         * //.thenReturn(vdcResponse); when(claudiaClient.browseVDC(any(ClaudiaData .class))).thenReturn(vdcResponse);
         * when(claudiaClient.deployVDC(any(ClaudiaData.class), any(String.class), any(String.class),
         * any(String.class))).thenReturn("OK"); when(claudiaClient.browseService
         * (any(ClaudiaData.class))).thenReturn(vdcResponse); when(claudiaClient.deployService(any(ClaudiaData.class),
         * any(String.class))) .thenReturn("OK"); when(claudiaClient.deployVM(any(String.class), any(String.class),
         * any(String.class), any(String.class), user, any(String.class))) .thenReturn("OK");
         * when(claudiaClient.deployVM(any(ClaudiaData.class),any(Tier.class))) .thenReturn(claudiaData);
         * when(claudiaClient.obtainIPFromFqn(any(String.class), any(String.class), any(String.class),any(String.class),
         * user)) .thenReturn("10.95.171.34"); claudiaUtil = mock (ClaudiaUtil.class); claudiaResponseAnalyser =
         * mock(ClaudiaResponseAnalyser.class); when(claudiaResponseAnalyser.getTaskUrl(any(String.class)))
         * .thenReturn("OK"); when(claudiaResponseAnalyser.getTaskStatus(any(String.class))) .thenReturn("success");
         */

        manager = new InfrastructureManagerClaudiaImpl();
        manager.setSystemPropertiesProvider(propertiesProvider);
        manager.setClaudiaClient(claudiaClient);
        manager.setClaudiaUtil(claudiaUtil);
        manager.setClaudiaResponseAnalyser(claudiaResponseAnalyser);

    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetVMsIsOk() throws Exception {
        InfrastructureManagerClaudiaImpl manager = new InfrastructureManagerClaudiaImpl();

        manager.setSystemPropertiesProvider(propertiesProvider);
        manager.setClaudiaClient(claudiaClient);
        manager.setClaudiaUtil(claudiaUtil);
        manager.setClaudiaResponseAnalyser(claudiaResponseAnalyser);

        // List<VM> vms = manager.getVMs(vdc, number_vms);
        // assertEquals(number_vms, vms.size());
    }

    @Test
    public void testGetVMsIsOkCreateVDC() throws Exception {
        InfrastructureManagerClaudiaImpl manager = new InfrastructureManagerClaudiaImpl();

        manager.setSystemPropertiesProvider(propertiesProvider);
        manager.setClaudiaClient(claudiaClient);
        manager.setClaudiaUtil(claudiaUtil);
        manager.setClaudiaResponseAnalyser(claudiaResponseAnalyser);

        // List<VM> vms = manager.getVMs(vdc, number_vms);
        // assertEquals(number_vms, vms.size());
    }

    @Test
    public void testCreateEnvironment() throws Exception {

        // List<VM> vms = manager.createEnvironment(ovf, org, vdc);

    }

}
