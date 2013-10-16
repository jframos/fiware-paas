/*
 * (c) Copyright 2011 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved. The copyright to the software
 * program(s) is property of Telefonica I+D. The program(s) may be used and or copied only with the express written
 * consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the agreement/contract under
 * which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.installator.rec.client;

/**
 * @author jesus.movilla
 */
public class ClientConstants {

    // public static final String BASE_SERVICE_PATH = "/service";
    public static final String BASE_SERVICE_PATH = "/applications";
    public static final String BASE_VM_PATH = "/applications/{0}/vms";
    public static final String BASE_PIC_PATH = "/applications/{0}/vms/{1}/pics";
    public static final String BASE_AC_PATH = "/applications/{0}/vms/{1}/pics/{2}/acs";
    public static final String BASE_VIRTUALSERVICE_PATH = "/applications/{0}/vms/{1}/pics/{2}/acs";
}
