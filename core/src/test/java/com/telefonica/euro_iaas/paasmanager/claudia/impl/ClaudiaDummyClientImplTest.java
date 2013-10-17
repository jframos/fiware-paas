/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import com.telefonica.euro_iaas.paasmanager.exception.FileUtilsException;
import com.telefonica.euro_iaas.paasmanager.util.FileUtilsImpl;
import org.junit.Test;

public class ClaudiaDummyClientImplTest {

    @Test
    public void testBrowseService() {
        ClaudiaDummyClientImpl manager = new ClaudiaDummyClientImpl();
        FileUtilsImpl fileUtils = new FileUtilsImpl();
        String payload = null;
        try {
            payload = fileUtils.readFile("src/main/resources/VappDummyService.xml");
        } catch (FileUtilsException e) {
            e.printStackTrace();
        }

    }

}
