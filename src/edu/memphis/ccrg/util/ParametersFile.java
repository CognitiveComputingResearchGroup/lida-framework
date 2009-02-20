/*
 * @(#)ParametersFile.java  1.0  June 7, 2008
 *
 * Copyright 2006-2008 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */
package edu.memphis.ccrg.util;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * 
 * @author Rodrigo Silva <rsilval@acm.org>
 */
public class ParametersFile {

    private File file;
    private Hashtable<String, String> paramValueTable;
    private String[] parametersLabels = {"activation_upperbound", "activation_lowerbound", "upscale", "downscale", "selectivity", "universalactivation"};
    private double[] parametersValues;
    public static final int PARAMETERS_NUMBER = 6;

    /**
     * Constructor for the class, receives this file name.
     * @param fileName the name for this parameters file
     */
    public ParametersFile(String fileName) {
        file = new File(fileName);
        paramValueTable = new Hashtable<String, String>();
        parse();
    }

    public void parse() {
        Scanner s = null;
        String line = "";
        String[] lineComponents;
        String key = "";
        String value = "";
        int index = 0;
        try {
            s = new Scanner(new BufferedReader(new FileReader(file)));
            s.useDelimiter("\n");
            while (s.hasNext()) {
                line = s.next();
                line = line.trim();
                if (!line.startsWith("#") && line.matches(".*=.*")) {
                    lineComponents = line.split("=");
                    key = lineComponents[0].toLowerCase();
                    value = lineComponents[1].toLowerCase();
                    paramValueTable.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    public String getParameter(String key) {
        return paramValueTable.get(key.toLowerCase());
    }
    
    @Override
    public String toString() {
        String s = "";
        for (String k : paramValueTable.keySet()) {
            s += k + "=" + paramValueTable.get(k) + "\n";
        }
        return s;
    }
}