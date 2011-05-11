package edu.memphis.ccrg.lida.framework.shared.activation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.StrategyDef;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.strategies.SigmoidDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.SigmoidExciteStrategy;

public class FooActivatible {
	
	private ActivatibleImpl act1;
	private ActivatibleImpl multArg;
	private static ElementFactory factory;

	@BeforeClass
	public static void setUpFirst(){
		factory = ElementFactory.getInstance();
		StrategyDef decayDef = new StrategyDef(SigmoidDecayStrategy.class.getCanonicalName(), 
				"specialDecay", null, "decay", true);
		factory.addDecayStrategy("specialDecay", decayDef);
		
		StrategyDef exciteDef = new StrategyDef(SigmoidExciteStrategy.class.getCanonicalName(), 
				"specialExcite", null, "excite", true);
		factory.addExciteStrategy("specialExcite", exciteDef);
	}
	
	@Before
	public void setUp() throws Exception {
		act1 = new ActivatibleImpl();
		multArg = new ActivatibleImpl(0.11, 0.22, 
				factory.getExciteStrategy("specialExcite"), 
				factory.getDecayStrategy("specialDecay"));
	}

	@Test
	public void testGetActivation() {
		assertTrue(act1.getActivation() == Activatible.DEFAULT_ACTIVATION);
		assertTrue(multArg.getActivation() == 0.11);
	}

	@Test
	public void testGetActivatibleRemovalThreshold() {
		assertTrue(act1.getActivatibleRemovalThreshold() == Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD);
		assertTrue(multArg.getActivatibleRemovalThreshold() == 0.22);
	}

	@Test
	public void testGetDecayStrategy() {
		assertEquals(act1.getDecayStrategy(), factory.getDefaultDecayStrategy());
		assertEquals(SigmoidDecayStrategy.class, multArg.getDecayStrategy().getClass());
	}

	@Test
	public void testGetExciteStrategy() {
		assertEquals(act1.getExciteStrategy(), factory.getDefaultExciteStrategy());
		assertEquals(SigmoidExciteStrategy.class, multArg.getExciteStrategy().getClass());
	}

	@Test
	public void testSetActivation() {
		
	}

	@Test
	public void testSetActivatibleRemovalThreshold() {
		
	}

	@Test
	public void testSetDecayStrategy() {
		
	}

	@Test
	public void testSetExciteStrategy() {
		
	}

	@Test
	public void testGetTotalActivation() {
		
	}
	
	@Test
	public void testDecay() {
		
	}

	@Test
	public void testExcite() {
		
	}

	@Test
	public void testIsRemovable() {
		
	}

}
