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
public class ApproxSigmoidDecayStrategyTest {

	@Test
	public final void testInit() {
		ApproxSigmoidDecayStrategy asds = new ApproxSigmoidDecayStrategy();
		asds.init();
		
		//To prove that default value of 'a' is 1.0
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, ... Object)",
				(asds.decay(0.5, 1, 1.0) == asds.decay(0.5, 1)));

	}

	@Test
	public final void testDecayDoubleLongObjectArray() {
		ApproxSigmoidDecayStrategy asds = new ApproxSigmoidDecayStrategy();
		
		//To prove that the third argument of method 
		//decay(double currentActivation, long ticks, Object... params) is effective
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, ... Object)",
				(asds.decay(0.5, 1, 2.0) != asds.decay(0.5, 1)));

	}

	@Test
	public final void testDecayDoubleLongMapOfStringQextendsObject() {
		
		ApproxSigmoidDecayStrategy asds = new ApproxSigmoidDecayStrategy();
				
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		map.put("a", 2.0);
		
		//To prove that the third argument of method 
		//decay(double currentActivation, long ticks, Map<String, ? extends Object> params) is effective
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, MAP<String, Object>)",
				(asds.decay(0.7, 1, map) != asds.decay(0.7, 1)));
		
    }

	@Test
	public final void testCalcActivation() {
		double LOWER_BOUND = 0.007;
		double UPPER_BOUND = 0.993;
		ApproxSigmoidDecayStrategy asds = new ApproxSigmoidDecayStrategy();

		// Testing of upper and lower bound of function
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, MAP<String, Object>)",
				((asds.calcActivation(1.0, 10, 1.0) == LOWER_BOUND)
						&&(asds.calcActivation(9.0, 0, 0.0) == UPPER_BOUND)));
		
		//Test of correction of function
		double curActive = 0.0, aa = 0.0;
		long ticks = 0;
		
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
			ticks = Math.abs(m % 10);
			//System.out.println("ticks " + ticks);
			
			rand3 = new Random();
			n = rand3.nextInt();
			aa = (double)Math.abs(n % 10)/10;
			//System.out.println("aa " + aa);
			
			total = total + (Math.abs(asds.decay(curActive, ticks, aa) - realSigmmoidDecayFunc(curActive, ticks, aa)));
				
			//System.out.println("total " + total);
			
			counter ++;
		}

		//System.out.println("The average mistake is " + total/counter);
		double mistake = total/counter;
		
		assertTrue("Problem with class SigmoidExciteStrategy for exict( double, double, MAP<String, Object>)",
				(mistake < 0.05));
	}
	
	public double realSigmmoidDecayFunc(double curActiv, long ticks, double aa){
		
		double curExi = Math.log(curActiv/(1-curActiv));
		
		double newExi = curExi - ticks*aa;
		
		double newAct = 1/(1 + Math.pow((Math.E),(0 - newExi)));
		
		return newAct;
	}

}
