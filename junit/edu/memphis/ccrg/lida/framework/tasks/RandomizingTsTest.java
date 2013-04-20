package edu.memphis.ccrg.lida.framework.tasks;

import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

public class RandomizingTsTest {
	private static final Logger logger = Logger.getLogger("RandomizingTsTest");
	private RandomizingTaskSpawner ts;

	@Before
	public void setUp() throws Exception {
		ts = new RandomizingTaskSpawner();
	}

	@Test
	public void testRandomize() {
		long ticks = 100L;

		for (int i = 0; i < 100; i++) {
			long result = ts.randomizeTicksPerRun(ticks);
			assertTrue(result >= 90);
			assertTrue(result <= 110);
			logger.log(Level.INFO, result + "");
		}
	}

	@Test
	public void testRandomize1() {
		long ticks = 4;
		ts.setVariation(.2);
		for (int i = 0; i < 100; i++) {
			long result = ts.randomizeTicksPerRun(ticks);
			assertTrue(result >= 3);
			assertTrue(result <= 5);
			logger.log(Level.INFO, result + "");
		}
	}

	@Test
	public void testRandomize2() {
		long ticks = 1;
		ts.setVariation(.9);
		for (int i = 0; i < 100; i++) {
			long result = ts.randomizeTicksPerRun(ticks);
			assertTrue(result > 0);
			assertTrue(result <= 2);
			logger.log(Level.INFO, result + "");
		}
	}
}
