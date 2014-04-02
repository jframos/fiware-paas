/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import com.telefonica.euro_iaas.paasmanager.exception.FileUtilsException;

public class FileUtilsImpl implements FileUtils {
    public String readFile(String path, String parentPath) throws FileUtilsException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(parentPath, path))));
        } catch (FileNotFoundException e) {

            throw new FileUtilsException("The file " + path + "is not found");
        }
        StringBuffer ruleFile = new StringBuffer();
        String actualString = null;

        try {
            while ((actualString = reader.readLine()) != null) {
                ruleFile.append(actualString + "\n");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new FileUtilsException("Error in reading the file " + path);
        }

        return ruleFile.toString();
    }

    public void generationFile(String filename, String text) throws FileUtilsException {
        if (text == null)
            throw new FileUtilsException("No data to write in the file ");
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(filename));
            out.write(text);
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new FileUtilsException("Error in generating the file " + filename);
        }

    }

    public String readFile(String fileName) throws FileUtilsException {
        BufferedReader reader;
        URL url = null;
        try {
            url = this.getClass().getResource("/" + fileName);

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(url.toURI()))));
        } catch (FileNotFoundException e) {

            throw new FileUtilsException("The file " + fileName + " is not found");
        } catch (URISyntaxException e) {
            throw new FileUtilsException("Invalid " + url + " url");
        }
        StringBuffer ruleFile = new StringBuffer();
        String actualString;

        try {
            while ((actualString = reader.readLine()) != null) {
                ruleFile.append(actualString + "\n");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new FileUtilsException("Error in reading the file " + fileName);
        }

        return ruleFile.toString();
    }
}
