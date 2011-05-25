package edu.memphis.ccrg.lida.framework.strategies;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.NodeImpl;



public class LinearDecayStrategyTest1 {
	LinearDecayStrategy decayStrategy;
	LinearDecayStrategy decayStrategy1;
	@Before
	public void setUp() throws Exception {
	decayStrategy = new LinearDecayStrategy ();
	decayStrategy1 = new LinearDecayStrategy (0.5);
       
	}


	@Test
	public void testDecay1() {
		double newact = decayStrategy.decay(1.0, 2, (Object[])null);
		assertEquals(.8,newact,0.00001);
		
	}
	@Test
	public void testDecay2() {
		double newact = decayStrategy.decay(1.0, 2, (Map<String,Object>)null);
		assertEquals(.8,newact,0.00001);
		
	}
	@Test
	public void testDecay3() {
		double newact = decayStrategy.decay(1.0, 20, (Map<String,Object>)null);
		assertEquals(0.0,newact,0.00001);
		
	}
	@Test
	public void testDecay4() {
		double newact = decayStrategy1.decay(1.0, 3, (Map<String,Object>)null);
		assertEquals(0.0,newact,0.00001);
		
	}
	@Test
	public void testDecay5() {
		double newact = decayStrategy1.decay(1.0, 3, (Object[])null);
		assertEquals(0.0,newact,0.00001);
		
	}
	@Test
	public void testDecay6() {
		HashMap<String,Object> map = new HashMap <String,Object> ();
		map.put("one",0.2);
		map.put("two",0.7);	
		map.put("three",1);	
		map.put("four","a");	
	    double newact = decayStrategy1.decay(1.0, 3 ,map);		
		assertEquals(0.0,newact,0.00001);
		
	}
	@Test
	public void testDecay7() {
		Object [] params = new Object [2];
		params[0]= "a";
		params[1]=  0.4;
		//System.out.println((Double) params[0]);
	    double newact = decayStrategy1.decay(1.0, 3 ,params);		
		assertEquals(0.0,newact,0.00001);
		
	}

	

}
