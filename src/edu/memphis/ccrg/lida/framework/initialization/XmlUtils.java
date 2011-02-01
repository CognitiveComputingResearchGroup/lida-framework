/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.framework.initialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public class XmlUtils {
	
	private static final Logger logger = Logger.getLogger(XmlUtils.class.getCanonicalName());
	
	public static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = getValue(el);
		}

		return textVal;
	}

	/**
	 * Calls getTextValue and returns a int value
	 * @param ele 
	 * @param tagName 
	 * @return 
	 */
	public static int getIntValue(Element ele, String tagName) {
		// in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele, tagName));
	}

	/**
	 * Calls getTextValue and returns a boolean value
	 * @param ele 
	 * @param tagName 
	 * @return 
	 */
	public static boolean getBooleanValue(Element ele, String tagName) {
		return Boolean.parseBoolean(getTextValue(ele, tagName));
	}

	public static Properties getParams(Element moduleElement) {
		Properties prop = new Properties();
		NodeList nl = moduleElement.getElementsByTagName("param");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element param = (Element) nl.item(i);
				String name = param.getAttribute("name");
				String value = (getValue(param)).trim();
				prop.setProperty(name, value);
			}
		}
		return prop;
	}

	public static Map<String,Object> getTypedParams(Element moduleElement) {
		Map<String,Object> prop = new HashMap<String,Object>();
		NodeList nl = moduleElement.getElementsByTagName("param");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element param = (Element) nl.item(i);
				String name = param.getAttribute("name");
				String type = param.getAttribute("type");
				String sValue = (getValue(param)).trim();
				Object value=sValue;
				
				if(type==null ||"string".equalsIgnoreCase(type)){
					value=sValue;
				}else if("int".equalsIgnoreCase(type)){
					try{
					value=Integer.parseInt(sValue);
					}catch(NumberFormatException e){
						value=sValue;
						logger.log(Level.FINE, e.toString(), LidaTaskManager.getCurrentTick());
					}
				}else if("double".equalsIgnoreCase(type)){
					try{
					value=Double.parseDouble(sValue);
					}catch(NumberFormatException e){
						value = sValue;
						logger.log(Level.FINE, e.toString(), LidaTaskManager.getCurrentTick());
					}
				}else if("boolean".equalsIgnoreCase(type)){
						value=Boolean.parseBoolean(sValue);
				}
				
				prop.put(name, value);
			}
		}
		return prop;
	}

	public static Element getChild(Element parent, String name) {
		for (Node child = parent.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (child instanceof Element && name.equals(child.getNodeName())) {
				return (Element) child;
			}
		}
		return null;
	}
	
	public static String getValue(Element parent) {
		for (Node child = parent.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (child instanceof Text) {
				return child.getNodeValue();
			}
		}
		return null;
	}
	public static List<String> getChildrenValues(Element element, String name) {
		List<String> vals = new ArrayList<String>();
		NodeList nl = element.getElementsByTagName(name);
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				String value = (getValue(el)).trim();
				vals.add(value);
			}
		}
		return vals;
	}
	public static List<Element> getChildren(Element parent, String name) {
		List<Element> nl = new ArrayList<Element>();
		for (Node child = parent.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (child instanceof Element && name.equals(child.getNodeName())) {
				nl.add((Element) child);
			}
		}
		return nl;
	}

}
