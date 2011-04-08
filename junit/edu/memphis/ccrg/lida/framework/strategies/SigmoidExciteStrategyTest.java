package edu.memphis.ccrg.lida.framework.strategies;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

/**
 * 
 * @author Daqi
 *
 */
public class SigmoidExciteStrategyTest {

	@Test
	public final void testInit() {
		SigmoidExciteStrategy ses = new SigmoidExciteStrategy();
		ses.init();
			
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, ... Object)",
				(ses.excite(0.5, 0.01, 1.0, 0.0) == ses.excite(0.5, 0.01)));
		
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		map.put("a", 1.0);
		map.put("c", 0.0);
		
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, Map<String, Object>)",
				(ses.excite(0.5, 0.01, map) == ses.excite(0.5, 0.01)));
		
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double)",
				((ses.excite(1.0, 0.0) == 1.0)&&(ses.excite(0.0, 0.0) == 0.0)&&(ses.excite(0.5, 0)== 0.5)));
	}

	@Test
	public final void testExciteDoubleDoubleObjectArray() {
		//Be tested in testInit()
		

	}

	@Test
	public final void testExciteDoubleDoubleMapOfStringQextendsObject() {
		//Be tested in testInit()

	}

}
