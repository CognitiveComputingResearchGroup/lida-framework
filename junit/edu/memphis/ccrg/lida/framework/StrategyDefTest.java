package edu.memphis.ccrg.lida.framework;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.StrategyDef;
import edu.memphis.ccrg.lida.framework.strategies.Strategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public class StrategyDefTest {

	StrategyDef strategy;
	Strategy instance;
	private static final Logger logger = Logger.getLogger(StrategyDef.class.getCanonicalName());
	
	@Before
	public void setUp() throws Exception {
		strategy = new StrategyDef() ;
	}

	@Test
	public void testGetInstance() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Map<String, Object> p = new HashMap <String, Object> ();
		strategy =  new  StrategyDef ("Strategy1","st",p, "public", true);
		 
		 try {
			   instance = (Strategy) Class.forName("Strategy1").newInstance();
			   instance.init(p);

			} catch (InstantiationException e) {
				logger.log(Level.WARNING, "Error creating Strategy.",
						LidaTaskManager.getCurrentTick());
			} catch (IllegalAccessException e) {
				logger.log(Level.WARNING, "Error creating Strategy.",
						LidaTaskManager.getCurrentTick());
			} catch (ClassNotFoundException e) {
				logger.log(Level.WARNING, "Error creating Strategy.",
						LidaTaskManager.getCurrentTick());
			}
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
