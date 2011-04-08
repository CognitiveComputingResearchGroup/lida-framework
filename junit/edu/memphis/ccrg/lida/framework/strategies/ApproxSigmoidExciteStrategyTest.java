package edu.memphis.ccrg.lida.framework.strategies;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

/**
 * 
 * @author Daqi
 *
 */
public class ApproxSigmoidExciteStrategyTest {

	@Test
	public final void testInit() {
		ApproxSigmoidExciteStrategy ases = new ApproxSigmoidExciteStrategy();
		ases.init();
		
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, ... Object)",
				(ases.excite(0.5, 0.01, 1.0) == ases.excite(0.5, 0.01)));

		
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		map.put("a", 1.0);
		map.put("c", 0.0);
		
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, MAP<String, Object>)",
				(ases.excite(0.5, 0.01, map) == ases.excite(0.5, 0.01)));
		
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, MAP<String, Object>)",
				((ases.calcExcitation(-9, 0, 0.0) == 0.007)
						&&(ases.calcExcitation(0.5, 0, 0.0) == 0.5)
						&&(ases.calcExcitation(1, 10, 1.0) == 0.993)));
		
	
	}

	@Test
	public final void testExciteDoubleDoubleObjectArray() {
		//Be tested in testInit()

	}

	@Test
	public final void testExciteDoubleDoubleMapOfStringQextendsObject() {
		//Be tested in testInit()

	}

	@Test
	public final void testCalcExcitation() {
		//Be tested in testInit()

	}

}
