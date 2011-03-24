package edu.memphis.ccrg.lida.framework;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.StrategyDef;
import edu.memphis.ccrg.lida.framework.strategies.Strategy;

public class StrategyDefTest {

	StrategyDef strategy;
	Strategy instance;
	
	@Before
	public void setUp() throws Exception {
		strategy = new StrategyDef() ;
	}

	@Test
	public void testGetInstance() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		 Map<String, Object> m = new HashMap <String, Object> ();
		 strategy  =  new  StrategyDef ("StartegyDef","strategy",m, "public", true);	
		 instance  = (Strategy) Class.forName("strategy").newInstance();
		 instance.init(m);
		 assertEquals ("problem with GetInstance", instance , strategy.getInstance()); 
		
	
	}

	

	@Test
	public void testIsFlyWeight() {
		Map<String, Object> p = new HashMap <String, Object> ();
		StrategyDef strategy =  new  StrategyDef ("Strategy1","st",p, "public", true);
		assertEquals ("problem wit GetInstance", true, strategy.isFlyWeight()); 
	}

	@Test
	public void testSetFlyWeight() {
		fail("Not yet implemented");
	}

	@Test
	public void testStrategyDefStringStringMapOfStringObjectStringBoolean() {
		 Map<String, Object> p = new HashMap <String, Object> ();
		 StrategyDef strategy =  new  StrategyDef ("Strategy","st",p, "public", true);		 
		 assertEquals ("problem wit GetInstance", "Strategy", strategy.getClassName());
		 assertEquals ("problem wit GetInstance", "st", strategy.getName()); 
		 assertEquals ("problem wit GetInstance", p, strategy.getParams());
		 assertEquals ("problem wit GetInstance", "public", strategy.getType());
		 assertEquals ("problem wit GetInstance", true, strategy.isFlyWeight());
	}

	@Test
	public void testStrategyDef() {
		 Map<String, Object> p = new HashMap <String, Object> ();
		 strategy =  new  StrategyDef ("Strategy","st",p, "public", true);
		 assertEquals ("problem with StrategyDef", p, strategy.getParams());
	}

	@Test
	public void testGetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetClassName() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetClassName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetType() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetType() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetParams() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetParams() {
		fail("Not yet implemented");
	}

}
