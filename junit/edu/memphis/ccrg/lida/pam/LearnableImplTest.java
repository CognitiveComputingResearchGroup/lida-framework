package edu.memphis.ccrg.lida.pam;

import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import junit.framework.TestCase;

public class LearnableImplTest extends TestCase {
	
	private PamNode node1;
	private ElementFactory factory = ElementFactory.getInstance();
	
	@Override
	public void setUp() throws Exception {		
		node1 = (PamNode) factory.getNode("PamNodeImpl");
	}
	
	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#getBaseLevelActivation()}
	 */
	@Test
	public void testGetBaselevelActivation() {
		node1.setId(1);			
		node1.setBaseLevelActivation(0.3);
		assertEquals("Problem with GetBaselevelActivation", 0.3,node1.getBaseLevelActivation());
	}
	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#setBaseLevelActivation(double)}
	 */
	@Test
	public void testSetBaselevelActivation() {
		node1.setId(1);			
		node1.setBaseLevelActivation(0.3);
		assertEquals("Problem with SetBaselevelActivation", 0.3,node1.getBaseLevelActivation());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#getTotalActivation()}.
	 */
	@Test
	public void testGetTotalActivation() {
		node1.setId(1);			
		node1.setBaseLevelActivation(0.3);
		node1.setActivation(0.4);
		assertEquals("Problem with GetTotalActivation", 0.7,node1.getTotalActivation());
	}

}
