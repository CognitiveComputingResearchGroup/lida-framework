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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * Utilities for reading an XML file.
 * @author Javier Snaider, Ryan J. McCall
 *
 */
public class XmlUtils {
	
	private static final Logger logger = Logger.getLogger(XmlUtils.class.getCanonicalName());

	/**
	 * Validates specified XML file with specified XML schema file
	 * @param xmlFile name of xml file
	 * @param schemaFile name of schema file
	 * @return true if the xml is valid under specified schema.
	 */
	public static boolean validateXmlFile(String xmlFile, String schemaFile){
		boolean result = false;
        // 1. Lookup a factory for the W3C XML Schema language
        SchemaFactory factory = 
            SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        
        // 2. Compile the schema. 
        // Here the schema is loaded from a java.io.File, but you could use 
        // a java.net.URL or a javax.xml.transform.Source instead.
        File schemaLocation = new File(schemaFile);
        Schema schema;
		try {
			schema = factory.newSchema(schemaLocation);
		} catch (SAXException ex) {
        	logger.log(Level.WARNING, "The Schema file is not valid. " +ex.getMessage());
        	return false;
		}
    
        // 3. Get a validator from the schema.
        Validator validator = schema.newValidator();
        
        // 4. Parse the document you want to check.
        Source source = new StreamSource(xmlFile);
        
        // 5. Check the document
        try {
            validator.validate(source);
            logger.log(Level.INFO, xmlFile + " is valid.");
            result= true;
        }
        catch (SAXException ex) {
        	logger.log(Level.WARNING,xmlFile + " is not valid because " + ex.getMessage());
        } catch (IOException ex) {
        	logger.log(Level.WARNING,xmlFile + " is not a valid file." + ex.getMessage());
		}  
        return result;
	}
	
	/**
	 * Returns text value of first element in specified element with specified tag.
	 * @param ele Dom element
	 * @param tagName name of xml tag
	 * @return text value of element with specified xml tag
	 */
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
	 * Returns int value of first element inside specified element with specified tag.
	 * @param ele Dom element
	 * @param tagName name of xml tag
	 * @return int value of element with specified xml tag
	 */
	public static int getIntValue(Element ele, String tagName) {
		// in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele, tagName));
	}

	/**
	 * Returns boolean value of first element inside specified element with specified tag.
	 * @param ele Dom element
	 * @param tagName name of xml tag
	 * @return boolean value of element with specified xml tag
	 */
	public static boolean getBooleanValue(Element ele, String tagName) {
		return Boolean.parseBoolean(getTextValue(ele, tagName));
	}

	/**
	 * Reads and creates a Properties from specified Element
	 * @param moduleElement Dom element
	 * @return Properties 
	 */
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

	/**
	 * Reads typed params from Xml element and returns them in a map.
	 * @param moduleElement Dom {@link Element}
	 * @return parameters indexed by name.
	 */
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

	/**
	 * Returns child of specified Element with specified name.
	 * @param parent an {@link Element}
	 * @param name name of child specified
	 * @return child or null
	 */
	public static Element getChild(Element parent, String name) {
		for (Node child = parent.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (child instanceof Element && name.equals(child.getNodeName())) {
				return (Element) child;
			}
		}
		return null;
	}
	
	/**
	 * Returns NodeValue of first child found that is a Text.
	 * @param parent Element
	 * @return child's string value.
	 */
	public static String getValue(Element parent) {
		for (Node child = parent.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (child instanceof Text) {
				return child.getNodeValue();
			}
		}
		return null;
	}
	
	/**
	 * Gets the children values of specified element with specified tag name.
	 * @param element Parent {@link Element}
	 * @param name specified tag name
	 * @return values of children 
	 */
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
	/**
	 * Returns all children with specified name.
	 * @param parent {@link Element}
	 * @param name name of sought children
	 * @return list of all children found.
	 */
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
