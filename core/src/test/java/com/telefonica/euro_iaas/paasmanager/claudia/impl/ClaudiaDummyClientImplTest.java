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
