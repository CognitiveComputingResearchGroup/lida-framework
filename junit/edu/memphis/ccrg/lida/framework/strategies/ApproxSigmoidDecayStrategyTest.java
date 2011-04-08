package edu.memphis.ccrg.lida.framework.strategies;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

/**
 * 
 * @author Daqi
 *
 */
public class ApproxSigmoidDecayStrategyTest {

	@Test
	public final void testInit() {
		ApproxSigmoidDecayStrategy asds = new ApproxSigmoidDecayStrategy();
		asds.init();
		
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, ... Object)",
				(asds.decay(0.5, 0, 1.0) == asds.decay(0.5, 0)));

		
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		map.put("a", 1.0);
		map.put("c", 0.0);
		
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, MAP<String, Object>)",
				(asds.decay(0.5, 0, map) == asds.decay(0.5, 0)));
		
		
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, MAP<String, Object>)",
				((asds.calcActivation(1, 10, 1.0) == 0.007)
						&&(asds.calcActivation(0.5, 0, 0.0) == 0.5)
						&&(asds.calcActivation(9, 0, 0.0) == 0.993)));
		

	}

	@Test
	public final void testDecayDoubleLongObjectArray() {
		//Be tested in testInit()
	}

	@Test
	public final void testDecayDoubleLongMapOfStringQextendsObject() {
		//Be tested in testInit()
	}

	@Test
	public final void testCalcActivation() {
		//Be tested in testInit()
	}

}
