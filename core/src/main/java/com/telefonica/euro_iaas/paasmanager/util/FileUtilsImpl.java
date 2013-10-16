package com.telefonica.euro_iaas.paasmanager.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public String readFile(String property) throws FileUtilsException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(property))));
        } catch (FileNotFoundException e) {

            throw new FileUtilsException("The file " + property + "is not found");
        }
        StringBuffer ruleFile = new StringBuffer();
        String actualString = null;

        try {
            while ((actualString = reader.readLine()) != null) {
                ruleFile.append(actualString + "\n");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new FileUtilsException("Error in reading the file " + property);
        }

        return ruleFile.toString();
    }
}
