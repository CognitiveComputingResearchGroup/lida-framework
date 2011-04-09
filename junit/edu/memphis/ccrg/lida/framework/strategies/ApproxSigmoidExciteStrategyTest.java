package edu.memphis.ccrg.lida.framework.strategies;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Random;

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
		
		//To prove that default value of 'a' is 1.0
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, ... Object)",
				(ases.excite(0.5, 1.0, 1.0) == ases.excite(0.5, 1.0)));
		
	
	}

	@Test
	public final void testExciteDoubleDoubleObjectArray() {
		ApproxSigmoidExciteStrategy ases = new ApproxSigmoidExciteStrategy();
		
		//To prove that the third argument of method 
		//excite(double currentActivation, double excitation, Object... params) is effective
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, ... Object)",
				(ases.excite(0.5, 1.0, 2.0) != ases.excite(0.5, 1.0)));

	}

	@Test
	public final void testExciteDoubleDoubleMapOfStringQextendsObject() {
		
		ApproxSigmoidExciteStrategy ases = new ApproxSigmoidExciteStrategy();
				
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		map.put("a", 2.0);
		
		//To prove that the third argument of method 
		//excite(double currentActivation, double excitation, Map<String, ? extends Object> params) is effective
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, MAP<String, Object>)",
				(ases.excite(0.7, 1.0, map) != ases.excite(0.7, 1.0)));

	}

	@Test
	public final void testCalcExcitation() {
		double LOWER_BOUND = 0.007;
		double UPPER_BOUND = 0.993;
		ApproxSigmoidExciteStrategy ases = new ApproxSigmoidExciteStrategy();

		// Testing of upper and lower bound of function
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, MAP<String, Object>)",
				((ases.calcExcitation(1.0, 10.0, 1.0) == UPPER_BOUND)
						&&(ases.calcExcitation(0.0, -10.0, 2.0) == LOWER_BOUND)));
		
		//Test of correction of function
		double curActive = 0.0, aa = 0.0;
		double excitation = 0.0;
		
		int counter = 0;
		int m = 0, n = 0, k = 0;
		double total = 0.0;
		Random rand1, rand2, rand3;
		
		long runningTime = 100000;
		
		//System.out.println("Running with " + runningTime + " times...");
		while( counter < runningTime){
			rand1 = new Random();
			k = rand1.nextInt();
			curActive = (double)Math.abs(k % 10)/10;
			//System.out.println("curActive " + curActive);
			
			rand2 = new Random();
			m = rand2.nextInt();
			excitation = (double)Math.abs(m % 10);
			//System.out.println("excitation " + excitation);
			
			rand3 = new Random();
			n = rand3.nextInt();
			aa = (double)Math.abs(n % 10)/10;
			//System.out.println("aa " + aa);
			
			total = total + (Math.abs(ases.excite(curActive, excitation, aa) - realSigmmoidExciteFunc(curActive, excitation, aa)));
				
			//System.out.println("total " + total);
			
			counter ++;
		}

		//System.out.println("The average mistake is " + total/counter);
		double mistake = total/counter;
		
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, MAP<String, Object>)",
				(mistake < 0.05));

	}
	
	public double realSigmmoidExciteFunc(double curActiv, double excitation, double aa){
		
		double curExi = Math.log(curActiv/(1-curActiv));
		
		double newExi = curExi + excitation*aa;
		
		double newAct = 1/(1 + Math.pow((Math.E),(0 - newExi)));
		
		return newAct;
	}

}
