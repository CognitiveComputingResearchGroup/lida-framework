package edu.memphis.ccrg.lida.framework.shared;


import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.strategies.NoDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.NoExciteStrategy;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;

public class DefaultElementFactoryTest {

	private static ElementFactory factory;
	
	@Before
	public void setUp() throws Exception {
		factory = ElementFactory.getInstance();
	}
	
	@Test
	public void checkHasDefaultElementTypes(){
		assertTrue(factory.containsNodeType("NodeImpl"));
		assertTrue(factory.getNode("NodeImpl") instanceof NodeImpl);
		assertTrue(factory.containsNodeType("PamNodeImpl"));
		assertTrue(factory.getNode("PamNodeImpl") instanceof PamNodeImpl);
		assertTrue(factory.containsNodeType("NoDecayPamNode"));
		
		PamNode n = (PamNode) factory.getNode("NoDecayPamNode");
		assertTrue(n.getBaseLevelDecayStrategy() instanceof NoDecayStrategy);
		assertTrue(n.getBaseLevelExciteStrategy() instanceof NoExciteStrategy);
		
		assertTrue(factory.containsLinkType("LinkImpl"));
		assertTrue(factory.containsLinkType("PamLinkImpl"));
		assertTrue(factory.containsLinkType("NoDecayPamLink"));
		
		assertTrue(factory.containsStrategy("noDecay"));
		assertTrue(factory.containsStrategy("noExcite"));
	}
}