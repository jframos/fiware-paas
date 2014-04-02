/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */
package com.telefonica.euro_iaas.paasmanager.util;

public interface Configuration {
    
    String TASK_PATH = "/rest/vdc/{1}/task/{0}";
  
    String ENVIRONMENT_PATH = "/rest/catalog/environment/{0}";
    String ENVIRONMENT_INSTANCE_PATH = "/rest/vdc/{0}/environmentInstance/{1}";
    String PRODUCT_INSTANCE_PATH = "/rest/vdc/{4}/product/{0}";
    String PRODUCT_RELEASE_PATH = "/rest/product/{0}";
    String APPLICATION_RELEASE_PATH = "/rest/application/{0}";
    
    String SDC_SERVER_MEDIATYPE = "application/json";

}

