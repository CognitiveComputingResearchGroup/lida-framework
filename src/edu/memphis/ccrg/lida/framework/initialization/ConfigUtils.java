/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigUtils {
	
	private static Logger logger=Logger.getLogger(ConfigUtils.class.getCanonicalName());
	
	public static Properties loadProperties(String filename){
		Properties properties = new Properties();
		if(filename!=null){
			try {
				properties.load(new BufferedReader(new FileReader(filename)));
			} catch (FileNotFoundException e) {
				throw new IllegalArgumentException();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error reading properties {0}", filename);
				properties=null;
			}
			}else{
				logger.log(Level.WARNING, "Properties File not specified");			
				properties=null;
			}
		return properties;
	}
}//class
