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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * Utilities for reading an XML file.
 * 
 * @author Javier Snaider, Ryan J. McCall
 * 
 */
public class XmlUtils {

	private static final Logger logger = Logger.getLogger(XmlUtils.class
			.getCanonicalName());

	/**
	 * Validates specified XML file with specified XML schema file
	 * 
	 * @param xmlFile
	 *            name of xml file
	 * @param schemaFile
	 *            name of schema file
	 * @return true if the xml is valid under specified schema.
	 */
	public static boolean validateXmlFile(String xmlFile, String schemaFile) {
		boolean result = false;
		// 1. Lookup a factory for the W3C XML Schema language
		SchemaFactory factory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");

		// 2. Compile the schema.
		// Here the schema is loaded from a java.io.File, but you could use
		// a java.net.URL or a javax.xml.transform.Source instead.
		File schemaLocation = new File(schemaFile);
//		InputStream is = ClassLoader.getSystemResourceAsStream("edu/memphis/ccrg/lida/framework/initialization/config/NewXMLSchema.xsd");

		Schema schema;
		try {
			schema = factory.newSchema(schemaLocation);
//			schema = factory.newSchema(new StreamSource(is));
		} catch (SAXException ex) {
			logger.log(Level.WARNING, "The Schema file is not valid. "
					+ ex.getMessage());
			ex.printStackTrace();
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
			result = true;
		} catch (SAXException ex) {
			logger.log(Level.WARNING, xmlFile + " is not valid because "
					+ ex.getMessage());
		} catch (IOException ex) {
			logger.log(Level.WARNING, xmlFile + " is not a valid file."
					+ ex.getMessage());
		}
		return result;
	}

	/**
	 * Returns text value of first element in specified element with specified
	 * tag.
	 * 
	 * @param ele
	 *            Dom element
	 * @param tagName
	 *            name of xml tag
	 * @return text value of element with specified xml tag
	 */
	public static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		List<Element> nl = getChildren(ele, tagName);
		if (nl != null && nl.size()!=0) {
			Element el = (Element) nl.get(0);
			textVal = getValue(el).trim();
		}

		return textVal;
	}

	/**
	 * Returns int value of first element inside specified element with
	 * specified tag.
	 * 
	 * @param ele
	 *            Dom element
	 * @param tagName
	 *            name of xml tag
	 * @return int value of element with specified xml tag
	 */
	public static int getIntValue(Element ele, String tagName) {
		// in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele, tagName));
	}

	/**
	 * Returns boolean value of first element inside specified element with
	 * specified tag.
	 * 
	 * @param ele
	 *            Dom element
	 * @param tagName
	 *            name of xml tag
	 * @return boolean value of element with specified xml tag
	 */
	public static boolean getBooleanValue(Element ele, String tagName) {
		return Boolean.parseBoolean(getTextValue(ele, tagName));
	}

	/**
	 * Reads and creates a Properties from specified Element
	 * 
	 * @param moduleElement
	 *            Dom element
	 * @return Properties
	 */
	public static Properties getParams(Element moduleElement) {
		Properties prop = new Properties();
		List<Element> nl = getChildren(moduleElement, "param");
		if (nl != null) {
			for (Element param : nl) {
				String name = param.getAttribute("name");
				String value = (getValue(param)).trim();
				prop.setProperty(name, value);
			}
		}
		return prop;
	}

	/**
	 * Reads typed params from Xml element and returns them in a map.
	 * 
	 * @param moduleElement
	 *            Dom {@link Element}
	 * @return parameters indexed by name.
	 */
	public static Map<String, Object> getTypedParams(Element moduleElement) {
		Map<String, Object> prop = new HashMap<String, Object>();
		List<Element> nl = getChildren(moduleElement, "param");
		if (nl != null) {
			for (Element param : nl) {
				String name = param.getAttribute("name");
				String type = param.getAttribute("type");
				String sValue = (getValue(param)).trim();
				Object value = sValue;

				if (type == null || "string".equalsIgnoreCase(type)) {
					value = sValue;
				} else if ("int".equalsIgnoreCase(type)) {
					try {
						value = Integer.parseInt(sValue);
					} catch (NumberFormatException e) {
						value = sValue;
						logger.log(Level.FINE, e.toString(), TaskManager
								.getCurrentTick());
					}
				} else if ("double".equalsIgnoreCase(type)) {
					try {
						value = Double.parseDouble(sValue);
					} catch (NumberFormatException e) {
						value = sValue;
						logger.log(Level.FINE, e.toString(), TaskManager
								.getCurrentTick());
					}
				} else if ("boolean".equalsIgnoreCase(type)) {
					value = Boolean.parseBoolean(sValue);
				}
				prop.put(name, value);
			}
		}
		return prop;
	}

	/**
	 * Returns child of specified Element with specified name.
	 * 
	 * @param parent
	 *            an {@link Element}
	 * @param name
	 *            name of child specified
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
	 * 
	 * @param parent
	 *            Element
	 * @return child's string value.
	 */
	public static String getValue(Element parent) {
		for (Node child = parent.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (child instanceof Text) {
				return child.getNodeValue().trim();
			}
		}
		return null;
	}

	/**
	 * Gets the children values of specified element with specified tag name.
	 * 
	 * @param element
	 *            Parent {@link Element}
	 * @param name
	 *            specified tag name
	 * @return values of children
	 */
	public static List<String> getChildrenValues(Element element, String name) {
		List<String> vals = new ArrayList<String>();
		List<Element> nl = getChildren(element, name);
		if (nl != null) {
			for (Element el : nl) {
				String value = (getValue(el)).trim();
				vals.add(value);
			}
		}
		return vals;
	}

	/**
	 * Returns all children with specified name.
	 * 
	 * @param parent
	 *            {@link Element}
	 * @param name
	 *            name of sought children
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

	/**
	 * Returns the Elements with name childName, in the group groupName
	 * inside the specified Element e
	 * @param e {@link Element}
	 * @param groupName name of the group
	 * @param childName name of children Elements returned
	 * @return
	 */
	public static List<Element> getCollectionElements(Element e, String groupName,
			String childName) {
		Element groupElement = getChild(e, groupName);
		if (groupElement != null) {
			List<Element> list = getChildren(groupElement, childName);
			return list;
		}
		return null;
	}

	/**
	 * Verifies and parses specified xml file into a {@link Document}.
	 * @param fileName the name of the file to parse
	 * @param schemaFilePath path to the schema file
	 * @return the DOM {@link Document} of the file fileName
	 */
	public static Document parseXmlFile(String fileName, String schemaFilePath) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document dom = null;
		try {
			db = dbf.newDocumentBuilder();
			if (validateXmlFile(fileName, schemaFilePath)){
				// parse using builder to get DOM representation of the XML file
				dom = db.parse(fileName);
			}else{
				logger.log(Level.WARNING, "Xml file invalid, file: " +fileName+" was not parsed");
			}
		}catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(),e);
		}
		return dom;
	}
}
