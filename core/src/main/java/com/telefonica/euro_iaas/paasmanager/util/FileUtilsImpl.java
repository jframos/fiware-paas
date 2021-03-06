/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
                ruleFile.append("      "+ actualString + "\n");
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
        InputStream url = null;
        
        	url = this.getClass().getResourceAsStream ("/" + fileName);
          //  url = this.getClass().getResource("./" + fileName);

            reader = new BufferedReader(new InputStreamReader(url));
     
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
