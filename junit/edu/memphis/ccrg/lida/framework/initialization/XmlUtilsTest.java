package edu.memphis.ccrg.lida.framework.initialization;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.w3c.dom.Document;


public class XmlUtilsTest {
	@Test
	public void testParseXmlFile() {
		Document dom = XmlUtils.parseXmlFile("configs/agent.xml","");
		assertNotNull (dom);
		
		dom = XmlUtils.parseXmlFile("agent.xml","");
		assertTrue (dom == null);
		
		dom = XmlUtils.parseXmlFile("testData/agentbad.xml","");
		assertTrue (dom == null);		
	}


}
