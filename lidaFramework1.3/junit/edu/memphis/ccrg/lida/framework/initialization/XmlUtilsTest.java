/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testParseXmlFile() {
		Document dom = XmlUtils
				.parseXmlFile("configs/agent.xml",
						"edu/memphis/ccrg/lida/framework/initialization/config/LidaXMLSchema.xsd");
		assertNotNull(dom);

		dom = XmlUtils
				.parseXmlFile("testData/agentbad.xml",
						"edu/memphis/ccrg/lida/framework/initialization/config/LidaXMLSchema.xsd");
		assertNull(dom);

	}

	@Test
	public void testValidateXmlFile() {
		boolean valid = XmlUtils
				.validateXmlFile("configs/agent.xml",
						"edu/memphis/ccrg/lida/framework/initialization/config/LidaXMLSchema.xsd");
		assertTrue(valid);

		valid = XmlUtils
				.validateXmlFile("testData/agentbad.xml",
						"edu/memphis/ccrg/lida/framework/initialization/config/LidaXMLSchema.xsd");
		assertFalse(valid);
	}

	@Test
	public void testGetTextValue() {
		String xml = "	<parent>" + "<child1> hi </child1> \n"
				+ "<child1>no hi</child1> \n" + "<child2> two</child2> \n"
				+ "<child4>    </child4> \n" + "<child3>"
				+ "<subchild>sub</subchild>" + "</child3>" + "</parent>";

		Document dom = XmlUtils.parseXmlString(xml);
		Element element = dom.getDocumentElement();

		String val = XmlUtils.getTextValue(element, "child1");
		assertEquals("hi", val);

		val = XmlUtils.getTextValue(element, "child2");
		assertEquals("two", val);

		val = XmlUtils.getTextValue(element, "child3");
		assertNull(val);

		val = XmlUtils.getTextValue(element, "child4");
		assertNull(val);
	}

	@Test
	public void testGetIntValue() {
		String xml = "	<parent>" + "<child1> hi </child1> \n"
				+ "<child1> 1 </child1> \n" + "<child2>2</child2> \n"
				+ "<child3>" + "<subchild>sub</subchild>" + "</child3>"
				+ "</parent>";
		Element element = XmlUtils.parseXmlString(xml).getDocumentElement();

		Integer value = XmlUtils.getIntegerValue(element, "child1");
		assertNull(value);

		xml = "	<parent>" + "<child1> 1 </child1> \n" + "<child1>2</child1> \n"
				+ "<child3>" + "<subchild>sub</subchild>" + "</child3>"
				+ "</parent>";
		element = XmlUtils.parseXmlString(xml).getDocumentElement();

		value = XmlUtils.getIntegerValue(element, "child1");
		assertEquals(new Integer(1), value);

		xml = "	<parent>" + "<child1> 1 </child1> \n" + "<child1>2</child1> \n"
				+ "<child3>" + "<subchild>sub</subchild>" + "</child3>"
				+ "</parent>";
		element = XmlUtils.parseXmlString(xml).getDocumentElement();

		value = XmlUtils.getIntegerValue(element, "child3");
		assertNull(value);

		xml = "	<parent>" + "<child1> 1 </child1> \n" + "<child1>2</child1> \n"
				+ "<child3>" + "<subchild>1</subchild>" + "</child3>"
				+ "</parent>";
		element = XmlUtils.parseXmlString(xml).getDocumentElement();

		value = XmlUtils.getIntegerValue(element, "child3");
		assertNull(value);

		xml = "	<parent>" + "<child1> 1 </child1> \n" + "<child1>2</child1> \n"
				+ "<child3>" + "<subchild>1</subchild>" + "</child3>"
				+ "</parent>";
		element = XmlUtils.parseXmlString(xml).getDocumentElement();

		value = XmlUtils.getIntegerValue(element, "subchild");
		assertNull(value);
	}

	@Test
	public void testGetBooleanValue() {
		String xml = "	<parent>" + "<child0> true </child0> \n"
				+ "<child1> 1 </child1> \n" + "<child2>false</child2> \n"
				+ "<child3>" + "<subchild>true</subchild>" + "</child3>"
				+ "</parent>";
		Element element = XmlUtils.parseXmlString(xml).getDocumentElement();

		Boolean value = XmlUtils.getBooleanValue(element, "43g");
		assertFalse(value);

		value = XmlUtils.getBooleanValue(element, "child0");
		assertTrue(value);

		value = XmlUtils.getBooleanValue(element, "child1");
		assertFalse(value);

		value = XmlUtils.getBooleanValue(element, "child2");
		assertFalse(value);

		value = XmlUtils.getBooleanValue(element, "child3");
		assertFalse(value);

		value = XmlUtils.getBooleanValue(element, "subchild");
		assertFalse(value);
	}

	@Test
	public void testGetValue() {
		String xml = "	<parent>true</parent>";
		Element element = XmlUtils.parseXmlString(xml).getDocumentElement();

		String value = XmlUtils.getValue(element);
		assertEquals("true", value);

		xml = "	<parent> 234fg48h </parent>";
		element = XmlUtils.parseXmlString(xml).getDocumentElement();

		value = XmlUtils.getValue(element);
		assertEquals("234fg48h", value);

		xml = "<parent>" + "<child0> true </child0> \n" + "</parent>";
		element = XmlUtils.parseXmlString(xml).getDocumentElement();

		value = XmlUtils.getValue(element);
		assertNull(value);

		xml = "	<parent>  </parent>";
		element = XmlUtils.parseXmlString(xml).getDocumentElement();

		value = XmlUtils.getValue(element);
		assertNull(value);

		xml = "	<parent></parent>";
		element = XmlUtils.parseXmlString(xml).getDocumentElement();

		value = XmlUtils.getValue(element);
		assertNull(value);
	}

	@Test
	public void testGetParams() {
		String xml = "<parent>"
				+ "<param name=\"pam.Upscale\" type=\"double\">.7 </param>"
				+ "<param name=\"pam.Downscale\" type=\"double\">.6 </param>"
				+ "<param name=\"pam.Selectivity\" type=\"double\">.5 </param>"
				+ "<param name=\"pam.newNodeType\" type=\"string\">PamNodeImpl</param>"
				+ "<param name=\"pam.newLinkType\" type=\"string\">PamLinkImpl</param>"
				+ "</parent>";
		Element element = XmlUtils.parseXmlString(xml).getDocumentElement();
		Map<String, Object> p = XmlUtils.getParams(element);

		assertTrue(p.get("pam.Upscale") instanceof String);
		assertEquals(".7", p.get("pam.Upscale"));
		assertEquals(".6", p.get("pam.Downscale"));
		assertEquals(".5", p.get("pam.Selectivity"));
		assertEquals("PamNodeImpl", p.get("pam.newNodeType"));
		assertEquals("PamLinkImpl", p.get("pam.newLinkType"));
	}

	@Test
	public void testGetTypedParams1() {
		String xml = "<parent>"
				+ "<param name=\"pam.Upscale\" type=\"boolean\">true </param>"
				+ "<param name=\"pam.Downscale\" type=\"int\">6 </param>"
				+ "<param name=\"pam.Selectivity\" type=\"double\">.5 </param>"
				+ "<param name=\"pam.newNodeType\" type=\"string\">PamNodeImpl</param>"
				+ "</parent>";
		Element element = XmlUtils.parseXmlString(xml).getDocumentElement();
		Map<String, Object> params = XmlUtils.getTypedParams(element);

		assertEquals(true, params.get("pam.Upscale"));
		assertEquals(6, params.get("pam.Downscale"));
		assertEquals(0.5, params.get("pam.Selectivity"));
		assertEquals("PamNodeImpl", params.get("pam.newNodeType"));
		assertEquals(null, params.get("pam.newLinkType"));
	}

	@Test
	public void testGetTypedParams2() {
		String xml = "<parent>"
				+ "<param name=\"pam.Upscale\" type=\"boolean\">2 </param>"
				+ "<param name=\"foo\" type=\"double\"> </param>"
				+ "<param name=\"pam.Downscale\" type=\"int\">.6 </param>"
				+ "<param name=\"pam.Selectivity\" type=\"double\">true </param>"
				+ "<param name=\"pam.newNodeType\" type=\"string\">1.9</param>"
				+ "</parent>";
		Element element = XmlUtils.parseXmlString(xml).getDocumentElement();
		Map<String, Object> params = XmlUtils.getTypedParams(element);

		assertEquals(false, params.get("pam.Upscale"));
		assertEquals(null, params.get("foo"));
		assertEquals(null, params.get("pam.Downscale"));
		assertEquals(null, params.get("pam.Selectivity"));
		assertEquals("1.9", params.get("pam.newNodeType"));
		assertEquals(null, params.get("pam.newLinkType"));

	}

	@Test
	public void testGetChild() {
		String xml = "	<parent>" + "<child1> hi </child1> \n"
				+ "<child1> 1 </child1> \n" + "<child2>2</child2> \n"
				+ "<child3>" + "<subchild>sub</subchild>" + "</child3>"
				+ "</parent>";
		Element element = XmlUtils.parseXmlString(xml).getDocumentElement();

		Element child = XmlUtils.getChild(element, "child1");
		assertNotNull(child);
		assertEquals("child1", child.getNodeName());
		assertEquals("hi", XmlUtils.getValue(child));

		child = XmlUtils.getChild(element, "parent");
		assertNull(child);

		child = XmlUtils.getChild(element, "child2");
		assertNotNull(child);
		assertEquals("child2", child.getNodeName());
		assertEquals("2", XmlUtils.getValue(child));

		child = XmlUtils.getChild(element, "child3");
		assertNotNull(child);
		assertEquals("child3", child.getNodeName());
		assertEquals(null, XmlUtils.getValue(child));

		child = XmlUtils.getChild(element, "subchild");
		assertNull(child);
	}

	@Test
	public void testGetChildren() {
		String xml = "	<parent>" + "<child1> hi </child1> \n"
				+ "<child1> 1 </child1> \n" + "<child2>2</child2> \n"
				+ "<child3>" + "<subchild>sub</subchild>" + "</child3>"
				+ "<child1> hi </child1> \n" + "</parent>";
		Element element = XmlUtils.parseXmlString(xml).getDocumentElement();

		List<Element> children = XmlUtils.getChildren(element, "parent");
		assertNotNull(children);
		assertEquals(0, children.size());

		children = XmlUtils.getChildren(element, "subchild");
		assertNotNull(children);
		assertEquals(0, children.size());

		children = XmlUtils.getChildren(element, "child3");
		assertNotNull(children);
		assertEquals(1, children.size());
		assertEquals("child3", children.get(0).getNodeName());
		assertEquals(null, XmlUtils.getValue(children.get(0)));

		children = XmlUtils.getChildren(element, "child2");
		assertNotNull(children);
		assertEquals(1, children.size());
		assertEquals("child2", children.get(0).getNodeName());
		assertEquals("2", XmlUtils.getValue(children.get(0)));

		children = XmlUtils.getChildren(element, "child1");
		assertNotNull(children);
		assertEquals(3, children.size());
		assertEquals("child1", children.get(0).getNodeName());
		assertEquals("hi", XmlUtils.getValue(children.get(0)));
		assertEquals("child1", children.get(1).getNodeName());
		assertEquals("1", XmlUtils.getValue(children.get(1)));
		assertEquals("child1", children.get(2).getNodeName());
		assertEquals("hi", XmlUtils.getValue(children.get(2)));
	}

	@Test
	public void testGetChildrenValues() {
		String xml = "	<parent>" + "<child1> hi </child1> \n"
				+ "<child1> 1 </child1> \n" + "<child2>2</child2> \n"
				+ "<child3>" + "<subchild>sub</subchild>" + "</child3>"
				+ "<child1> hi </child1> \n" + "</parent>";
		Element element = XmlUtils.parseXmlString(xml).getDocumentElement();

		List<String> values = XmlUtils.getChildrenValues(element, "subchild");
		assertNotNull(values);
		assertEquals(0, values.size());

		values = XmlUtils.getChildrenValues(element, "child3");
		assertEquals(1, values.size());
		assertEquals(null, values.get(0));

		values = XmlUtils.getChildrenValues(element, "child2");
		assertEquals(1, values.size());
		assertEquals("2", values.get(0));

		values = XmlUtils.getChildrenValues(element, "child1");
		assertEquals(3, values.size());
		assertEquals("hi", values.get(0));
		assertEquals("1", values.get(1));
		assertEquals("hi", values.get(2));

		values = XmlUtils.getChildrenValues(element, "parent");
		assertEquals(0, values.size());
	}

	@Test
	public void testGetChildrenInGroup() {
		String xml = "	<parent>" + "<group1> \n" + "<child1> hi </child1> \n"
				+ "<child1> 1 </child1> \n" + "<child2>2</child2> \n"
				+ "</group1>" + "<group2> \n" + "<child3>"
				+ "<subchild>sub</subchild>" + "</child3>"
				+ "<child1> hi </child1> \n" + "</group2>" + "</parent>";
		Element element = XmlUtils.parseXmlString(xml).getDocumentElement();

		List<Element> children = XmlUtils.getChildrenInGroup(element, "foo",
				"bar");
		assertNotNull(children);
		assertEquals(0, children.size());

		children = XmlUtils.getChildrenInGroup(element, "group1", "bar");
		assertNotNull(children);
		assertEquals(0, children.size());

		children = XmlUtils.getChildrenInGroup(element, "group1", "child1");
		assertNotNull(children);
		assertEquals(2, children.size());

		children = XmlUtils.getChildrenInGroup(element, "group1", "child2");
		assertEquals(1, children.size());

		children = XmlUtils.getChildrenInGroup(element, "group2", "child1");
		assertEquals(1, children.size());

		children = XmlUtils.getChildrenInGroup(element, "group2", "child3");
		assertEquals(1, children.size());

		children = XmlUtils.getChildrenInGroup(element, "child3", "subchild");
		assertEquals(0, children.size());

		children = XmlUtils.getChildrenInGroup(element, "parent", "group1");
		assertEquals(0, children.size());
	}

}