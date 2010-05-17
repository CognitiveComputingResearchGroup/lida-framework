/**
 * 
 */
package edu.memphis.ccrg.lida.pam;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Siminder Kaur
 *
 */
public class PamNodeImplTest extends TestCase{
	
	PamNodeImpl node1,node2;
	
	@Before
	public void setUp() throws Exception {
		node1 = new PamNodeImpl();
		node2 = new PamNodeImpl();				
	}
	
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#equals(java.lang.Object)}.
	 */
	@Test
	public void testEquals() {
		node1.setId(1);
		node2.setId(1);
		assertEquals("Problem with equals", node1,node2);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#synchronize()}.
	 */
	@Test
	public void testSynchronize() {
		node1.setId(1);		
		node1.setBaselevelActivation(0.1);
		node1.setActivation(0.2);
		
		node1.synchronize();
		assertEquals("Problem with Synchronize", 0.3,node1.getActivation());		
	}
	@Test
	public void testSynchronize2() {
		node1.setId(1);		
		node1.setBaselevelActivation(0.1);
		node1.setActivation(1.0);
		
		node1.synchronize();
		assertEquals("Problem with Synchronize", 1.0,node1.getActivation());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#isOverThreshold()}.
	 */
	@Test
	public void testIsOverThreshold() {
		node1.setId(1);		
		node1.setBaselevelActivation(0.1);
		node1.setActivation(0.4);
		node1.setSelectionThreshold(0.5);
		node1.synchronize();
		
		assertEquals("Problem with IsOverThreshold", true,node1.isOverThreshold());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#setSelectionThreshold(double)}.
	 */
	@Test
	public void testSetSelectionThreshold() {
		node1.setId(1);		
		node1.setBaselevelActivation(0.1);
		node1.setSelectionThreshold(0.3);
		
		assertEquals("Problem with SetSelectionThreshold", 0.3,node1.getSelectionThreshold());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#setValue(java.util.Map)}.
	 */
	@Test
	public void testSetValue() {
		node1.setId(1);	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("importance", 0.4);
		map.put("baselevelactivation", 0.2);
		node1.setValue(map);
		
		assertEquals("Problem with SetValue", 0.4,node1.getImportance());
		assertEquals("Problem with SetValue", 0.2,node1.getBaselevelActivation());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#getSelectionThreshold()}.
	 */
	@Test
	public void testGetSelectionThreshold() {
		node1.setId(1);			
		node1.setSelectionThreshold(0.3);
		
		assertEquals("Problem with GetSelectionThreshold", 0.3,node1.getSelectionThreshold());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#getBaselevelActivation()}.
	 */
	@Test
	public void testGetBaselevelActivation() {
		node1.setId(1);			
		node1.setBaselevelActivation(0.3);
		
		assertEquals("Problem with GetBaselevelActivation", 0.3,node1.getBaselevelActivation());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#setBaselevelActivation(double)}.
	 */
	@Test
	public void testSetBaselevelActivation() {
		node1.setId(1);			
		node1.setBaselevelActivation(0.3);
		
		assertEquals("Problem with SetBaselevelActivation", 0.3,node1.getBaselevelActivation());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#getTotalActivation()}.
	 */
	@Test
	public void testGetTotalActivation() {
		node1.setId(1);			
		node1.setBaselevelActivation(0.3);
		node1.setActivation(0.4);
		
		assertEquals("Problem with GetTotalActivation", 0.7,node1.getTotalActivation());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#getMaxActivation()}.
	 */
	@Test
	public void testGetMaxActivation() {
		node1.setId(1);
		assertEquals("Problem with GetMaxActivation", 1.0,node1.getMaxActivation());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#getMinActivation()}.
	 */
	@Test
	public void testGetMinActivation() {
		node1.setId(1);
		assertEquals("Problem with GetMinActivation", 0.0,node1.getMinActivation());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#printActivationString()}.
	 */
	@Test
	public void testPrintActivationString() {
		node1.setId(1);			
		node1.setBaselevelActivation(0.3);
		node1.setActivation(0.4);
		node1.printActivationString();
	}

}
