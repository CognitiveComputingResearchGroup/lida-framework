package edu.memphis.ccrg.lida.framework.initialization;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

public class XmlUtilsTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testValidateXmlFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTextValue() {
		fail("Not yet implemented");
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
		Document dom = XmlUtils.parseXmlFile("configs/agent.xml","edu/memphis/ccrg/lida/framework/initialization/config/LidaXMLSchema.xsd");
		assertNotNull (dom);
		
//		dom = XmlUtils.parseXmlFile("agent.xml","");
//		assertTrue (dom == null);
//		
//		dom = XmlUtils.parseXmlFile("testData/agentbad.xml","");
//		assertTrue (dom == null);	
	}

}
