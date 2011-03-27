/**
 * 
 */
package edu.memphis.ccrg.lida.framework.shared.activation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


import edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultTotalActivationStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.TotalActivationStrategy;

/**
 * This is a JUnit class which can be used to test methods of the LearnableImpl class
 * @author Nisrine,Siminder
 *
 */
public class LearnableImplTest {

	LearnableImpl node1;
	LearnableImpl node2;
		
	/**
	 * @throws java.lang.Exception e
	 */
	@Before
	public void setUp() throws Exception {
		node1 = new LearnableImpl ();
		  
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#decay(long)}.
	 */
	@Test
	public void testDecay() {
		DecayStrategy ds = new LinearDecayStrategy();
		node1.setDecayStrategy(ds);
		node1.setBaseLevelDecayStrategy(ds);
		
		node1.setBaseLevelActivation(0.5);
		node1.setActivation(0.7);
		node1.decay(100);
		
		assertTrue("Problem with Decay", node1.getBaseLevelActivation()<0.5);
		assertTrue("Problem with Decay", node1.getActivation()<0.7);
	
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getTotalActivation()}.
	 */
	@Test
	public void testGetTotalActivation() {
		
		TotalActivationStrategy ts= new DefaultTotalActivationStrategy();
		node1.setTotalActivationStrategy(ts);
		node1.setActivation(0.2);
		node1.setBaseLevelActivation(0.2);
		assertEquals ("Problem with GetTotalActivation",0.4, node1.getTotalActivation(), 0.000000001);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#isRemovable()}.
	 */
	@Test
	public void testIsRemovable() {
		node1.setBaseLevelActivation(-1.0);
		 assertEquals ("problem with IsRemovable",true , node1.isRemovable());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#LearnableImpl(double, edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy, edu.memphis.ccrg.lida.framework.strategies.DecayStrategy, edu.memphis.ccrg.lida.framework.shared.activation.TotalActivationStrategy)}.
	 */
	@Test
	public void testLearnableImpl2() {
		ExciteStrategy es = new DefaultExciteStrategy();
		DecayStrategy ds = new LinearDecayStrategy();
		TotalActivationStrategy ts= new DefaultTotalActivationStrategy();
		node2 = new LearnableImpl(0.6,es,ds,ts,0.4);
		assertEquals("Problem with LearnableImpl2", 0.6,node2.getActivation(),0.001);
		assertEquals("Problem with LearnableImpl2",node2.getExciteStrategy(),es);
		assertEquals("Problem with LearnableImpl2",node2.getDecayStrategy(),ds);
		assertEquals("Problem with LearnableImpl2",0.4,node2.getLearnableRemovalThreshold(),0.001);
		 
		
		//assertEquals
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#LearnableImpl()}.
	 */
	@Test
	public void testLearnableImpl() {
		
	    ExciteStrategy es = new DefaultExciteStrategy();
		DecayStrategy ds = new LinearDecayStrategy();
		TotalActivationStrategy ts= new DefaultTotalActivationStrategy();
		node1 = new LearnableImpl(0.6,es,ds,ts,0.4);
		node2 = new LearnableImpl(node1);
		assertEquals("Problem with LearnableImpl",0.6,node2.getActivation(),0.001);
		assertEquals("Problem with LearnableImpl",node2.getExciteStrategy(),es);
		assertEquals("Problem with LearnableImpl",node2.getDecayStrategy(),ds);
		assertEquals("Problem with LearnableImpl",0.4,node2.getLearnableRemovalThreshold(),0.001);
		 
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#decayBaseLevelActivation(long)}.
	 */
	@Test
	public void testDecayBaseLevelActivation() {
		DecayStrategy ds = new LinearDecayStrategy();
		node1.setBaseLevelDecayStrategy(ds);
		node1.setBaseLevelActivation(0.5);			
		node1.decayBaseLevelActivation(100);
				
		assertTrue("Problem with DecayBaseLevelActivation", node1.getBaseLevelActivation()<0.5);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getBaseLevelExciteStrategy()}.
	 */
	@Test
	public void testGetBaseLevelExciteStrategy() {
		ExciteStrategy es = new DefaultExciteStrategy();
		node1.setBaseLevelExciteStrategy(es);
		
		assertEquals("Problem with SetBaseLevelExciteStrategy", es,node1.getBaseLevelExciteStrategy());;
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelExciteStrategy(edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy)}.
	 */
	@Test
	public void testSetBaseLevelExciteStrategy() {
		ExciteStrategy es = new DefaultExciteStrategy();
		node1.setBaseLevelExciteStrategy(es);
		
		assertEquals("Problem with SetBaseLevelExciteStrategy", es,node1.getBaseLevelExciteStrategy());
		
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getBaseLevelDecayStrategy()}.
	 */
	@Test
	public void testGetBaseLevelDecayStrategy() {
		DecayStrategy ds = new LinearDecayStrategy();
		node1.setBaseLevelDecayStrategy(ds);
		
		assertEquals("Problem with GetBaseLevelDecayStrategy", ds,node1.getBaseLevelDecayStrategy());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelDecayStrategy(edu.memphis.ccrg.lida.framework.strategies.DecayStrategy)}.
	 */
	@Test
	public void testSetBaseLevelDecayStrategy() {
		DecayStrategy ds = new LinearDecayStrategy();
		node1.setBaseLevelDecayStrategy(ds);
		
		assertEquals("Problem with SetBaseLevelDecayStrategy", ds,node1.getBaseLevelDecayStrategy());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#reinforceBaseLevelActivation(double)}.
	 */
	@Test
	public void testReinforceBaseLevelActivation() {
		ExciteStrategy es = new DefaultExciteStrategy();
		node1.setBaseLevelExciteStrategy(es);
		node1.setBaseLevelActivation(0.2);
		node1.reinforceBaseLevelActivation(0.3);
		
		assertEquals ( 0.5,node1.getBaseLevelActivation(),0.001);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelActivation(double)}.
	 */
	@Test
	public void testSetBaseLevelActivation() {
    node1.setBaseLevelActivation(0.4);
		
		assertEquals(0.4,node1.getBaseLevelActivation(),0.001);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getBaseLevelActivation()}.
	 */
	@Test
	public void testGetBaseLevelActivation() {
	        node1.setBaseLevelActivation(0.3);
	        assertEquals (0.3, node1.getBaseLevelActivation(), 0.001);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getLearnableRemovalThreshold()}.
	 */
	@Test
	public void testGetLearnableRemovalThreshold() {
		node1.setLearnableRemovalThreshold(0.2);
		assertEquals (0.2, node1.getLearnableRemovalThreshold(), 0.00001);
		
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setLearnableRemovalThreshold(double)}.
	 */
	@Test
	public void testSetLearnableRemovalThreshold() {
		node1.setLearnableRemovalThreshold(0.4);
		assertEquals(0.4, node1.getLearnableRemovalThreshold(), 0.0001);
		
		
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getTotalActivationStrategy()}.
	 */
	@Test
	public void testGetTotalActivationStrategy() {
		TotalActivationStrategy ts= new DefaultTotalActivationStrategy();
		node1.setTotalActivationStrategy(ts);
		assertEquals("problem with GetTotalActivationStrategy() ", ts,node1.getTotalActivationStrategy());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setTotalActivationStrategy(edu.memphis.ccrg.lida.framework.shared.activation.TotalActivationStrategy)}.
	 */
	@Test
	public void testSetTotalActivationStrategy() {
		TotalActivationStrategy ts= new DefaultTotalActivationStrategy();
		node1.setTotalActivationStrategy(ts);
		assertEquals("problem with SetTotalActivationStrategy() ", ts,node1.getTotalActivationStrategy());
		
	}

}
