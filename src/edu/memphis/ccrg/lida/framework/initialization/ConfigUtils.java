package edu.memphis.ccrg.lida.framework.initialization;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigUtils {
	
	private static Logger logger=Logger.getLogger("lida.framework.initialization.ConfigUtils");
	
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
