package edu.memphis.ccrg.lida.framework.strategies;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.NodeImpl;



public class LinearDecayStrategyTest1 {
	LinearDecayStrategy decayStrategy;
	NodeImpl node1;
	@Before
	public void setUp() throws Exception {
	decayStrategy = new LinearDecayStrategy ();
	node1 = new NodeImpl();
	
	node1.setId(1);
	node1.setDecayStrategy(decayStrategy);
	node1.setActivation(0.8);
       
	}


	@Test
	public void testDecayDoubleLongObjectArray() {
		Double [] array = new  Double [3];
		array[0]=10.0;
		array[1]= 6.0;
		array[2]= 9.0;	
		
		double mm = 0.1, currentActivation = 0;
		mm= (Double) array[0];			
		currentActivation -= mm * 1000;		
		node1.setActivation(currentActivation);
		assertTrue("Problem with Decay", 0.0== node1.getTotalActivation() );
		
	}

	@Test
	public void testDecayDoubleLongMapOfStringQextendsObject() {
		Map <String, Double> params= new HashMap <String, Double>();
		params.put("one",  10.0);
		params.put("two",  6.0);
		params.put("three",  15.0);
		
		double mm = 0.1, currentActivation = 0;
		mm = (Double) params.get("one");
		currentActivation -= mm * 1000;		
		node1.setActivation(currentActivation);
		assertTrue("Problem with Decay", 0.0== node1.getTotalActivation() );
		
	}

}
