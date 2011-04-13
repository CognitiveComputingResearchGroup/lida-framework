package edu.memphis.ccrg.lida.framework.strategies;

import static org.junit.Assert.*;

import java.util.HashMap;

/**
 * 
 * @author Daqi
 */

import org.junit.Test;

public class SigmoidDecayStrategyTest {

	@Test
	public final void testInit() {
		SigmoidDecayStrategy sds = new SigmoidDecayStrategy();
		sds.init();
		
		//To prove that default value of 'a' is 1.0 and 'c' is 0.0
		assertTrue("Problem with class SigmoidExciteStrategy for decay( double, double, ... Object)",
				(sds.decay(0.5, 1, 1.0, 0.0) == sds.decay(0.5, 1)));
		
	}

	@Test
	public final void testDecayDoubleLongObjectArray() {
		
		SigmoidDecayStrategy sds = new SigmoidDecayStrategy();
		
		//To prove that the third argument of method 
		//decay(double currentActivation, long ticks, Object... params) is effective
		assertTrue("Problem with class SigmoidExciteStrategy for decay( double, double, ... Object)",
				(sds.decay(0.5, 1, 2.0, 1.0) != sds.decay(0.5, 1)));

		//Test for correction
		assertTrue("Problem with class SigmoidExciteStrategy for decay( double, double, ... Object)",
				(sds.decay(0.5, 2) < 0.5));
		
		//Value of "sds.decay(0.5, 0)" is close to 0.5 very much but not equal to 0.5
		assertTrue("Problem with class SigmoidExciteStrategy for decay( double, double, ... Object)",
				(sds.decay(0.5, 0) > 0.4999)&&(sds.decay(0.5, 0) < 0.5));
		
		assertTrue("Problem with class SigmoidExciteStrategy for decay( double, double, ... Object)",
				(sds.decay(0.5, -2) > 0.5));
		
	}

	@Test
	public final void testDecayDoubleLongMapOfStringQextendsObject() {
		SigmoidDecayStrategy sds = new SigmoidDecayStrategy();
		
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		map.put("a", 2.0);
		map.put("c", 1.0);
		
		//To prove that the third argument of method 
		//decay(double currentActivation, long ticks, Map<String, ? extends Object> params) is effective
		assertTrue("Problem with class SigmoidExciteStrategy for decay( double, double, MAP<String, Object>)",
				(sds.decay(0.7, 1, map) != sds.decay(0.7, 1)));
	}

}
