/**
 * 
 */
package edu.memphis.ccrg.lida.framework.strategies;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.NodeImpl;

/**
 * @author Siminder Kaur
 *
 */
public class LinearDecayStrategyTest extends TestCase{

	LinearDecayStrategy decayStrategy;
	NodeImpl node1;
	
	@Before
	public void setUp() throws Exception {
		decayStrategy = new LinearDecayStrategy();		
		node1 = new NodeImpl();
		
		node1.setId(1);
		node1.setDecayStrategy(decayStrategy);
		node1.setActivation(0.8);	
	}	

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy#decay(double, long)}.
	 */
	@Test
	public void testDecay() {
		double newActivation = decayStrategy.decay(node1.getActivation(), 1000);	
		node1.setActivation(newActivation);		
		
		assertEquals("Problem with Decay", 0.0, node1.getTotalActivation());
	}

}
