package edu.memphis.ccrg.lida.framework.initialization;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

public class XmlUtilsTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
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
		String xml = "	<parent>"
			+ "<child1>hi</child1> \n"
				+ "<child1>no hi</child1> \n"
				+ "<child2>two</child2> \n"
				+ "<child3>"
				+ "<subchild>sub</subchild>"
				+ "</child3>"
				+ "</parent>";

		Document dom = XmlUtils.parseXmlString(xml);
		Element element = dom.getDocumentElement();

		String val= XmlUtils.getTextValue(element, "child1");
		assertEquals("hi", val);

		val= XmlUtils.getTextValue(element, "child2");
		assertEquals("two", val);

		val= XmlUtils.getTextValue(element, "child3");
		assertEquals("", val);

	}

	@Test
	public void testGetIntValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBooleanValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetParams() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTypedParams() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetChild() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetChildrenValues() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetChildren() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCollectionElements() {
		fail("Not yet implemented");
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

}
