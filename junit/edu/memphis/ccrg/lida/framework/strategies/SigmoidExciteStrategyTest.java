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
		
		//To prove that default value of 'a' is 1.0 and 'c' is 0.0
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, ... Object)",
				(ses.excite(0.5, 1.0, 1.0, 0.0) == ses.excite(0.5, 1.0)));
	}

	@Test
	public final void testExciteDoubleDoubleObjectArray() {
		SigmoidExciteStrategy ses = new SigmoidExciteStrategy();
		
		//To prove that the third argument of method 
		//excite(double currentActivation, double excitation, Object... params) is effective
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, ... Object)",
				(ses.excite(0.5, 1.0, 2.0, 1.0) != ses.excite(0.5, 1.0)));

		//Test for correction
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, ... Object)",
				(ses.excite(0.5, 0.2) > 0.5));
		
		//Value of "ses.excite(0.5, 0.0)" is close to 0.5 very much but not equal to 0.5
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, ... Object)",
				(ses.excite(0.5, 0.0) > 0.4999)&&(ses.excite(0.5, 0.0) < 0.5));
		
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, ... Object)",
				(ses.excite(0.5, -0.2) < 0.5));
	}

	@Test
	public final void testExciteDoubleDoubleMapOfStringQextendsObject() {

		SigmoidExciteStrategy ses = new SigmoidExciteStrategy();
		
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		map.put("a", 2.0);
		map.put("c", 1.0);
		
		//To prove that the third argument of method 
		//excite(double currentActivation, double excitation, Map<String, ? extends Object> params) is effective
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, MAP<String, Object>)",
				(ses.excite(0.7, 1.0, map) != ses.excite(0.7, 1.0)));

	}
	
}
