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
			
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, ... Object)",
				(sds.decay(0.5, 1, 1.0, 0.0) == sds.decay(0.5, 1)));
		
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		map.put("a", 1.0);
		map.put("c", 0.0);
		
		
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double)",
				((sds.decay(1.0, 0) == 1.0)&&(sds.decay(0.0, 0)== 0.0)&&(sds.decay(0.5, 0)== 0.5)));
		
	}

	@Test
	public final void testDecayDoubleLongObjectArray() {
		//Be tested in testInit()
	}

	@Test
	public final void testDecayDoubleLongMapOfStringQextendsObject() {
		//Be tested in testInit()
	}

}
