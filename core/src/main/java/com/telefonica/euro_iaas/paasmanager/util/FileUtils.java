package com.telefonica.euro_iaas.paasmanager.util;

import com.telefonica.euro_iaas.paasmanager.exception.FileUtilsException;

public interface FileUtils {
	public String readFile(String path, String parentPath)
			throws FileUtilsException;

	public void generationFile(String filename, String text)
			throws FileUtilsException;

	public String readFile(String property) throws FileUtilsException;

}
