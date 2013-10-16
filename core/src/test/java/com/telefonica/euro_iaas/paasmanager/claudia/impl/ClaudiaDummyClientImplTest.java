package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.Test;

import com.telefonica.euro_iaas.paasmanager.exception.ClaudiaResourceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.FileUtilsException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.util.FileUtilsImpl;

public class ClaudiaDummyClientImplTest {

	@Test
	public void testBrowseService() {
		ClaudiaDummyClientImpl manager = new ClaudiaDummyClientImpl();
		FileUtilsImpl fileUtils = new FileUtilsImpl();
		String payload = null;
		try {
			payload = fileUtils
					.readFile("src/main/resources/VappDummyService.xml");
		} catch (FileUtilsException e) {
			e.printStackTrace();
		}

	}

}
