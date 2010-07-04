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
public class DefaultExciteStrategyTest extends TestCase{
	
	DefaultExciteStrategy exciteStrategy;
	NodeImpl node1;
	
	@Before
	public void setUp() throws Exception {
		exciteStrategy = new DefaultExciteStrategy();
		node1 = new NodeImpl();
		
		node1.setId(1);
		node1.setExciteStrategy(exciteStrategy);
		node1.setActivation(0.2);		
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy#excite(double, double)}.
	 */
	@Test
	public void testExcite() {
		
		double newActivation = exciteStrategy.excite(node1.getActivation(),0.8);		
		node1.setActivation(newActivation);		
		
		assertEquals("Problem with Excite", 1.0, node1.getTotalActivation());
	}

}
