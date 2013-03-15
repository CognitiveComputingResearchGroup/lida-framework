package edu.memphis.ccrg.lida.framework.initialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

public class InitializableImplTest {

	private static final Logger logger = Logger
			.getLogger(InitializableImplTest.class.getCanonicalName());
	private InitializableImpl initializable;
	private Map<String, Object> params;
	private double epsilon = 10e-9;

	@Before
	public void setup() {
		initializable = new InitializableImpl();
		params = new HashMap<String, Object>();
		params.put("a", null);
		params.put("", false);
		params.put("b", 1.0);
		params.put("c", 100);
		params.put("d", "hello");
	}

	@Test
	public void testTypeIncompat() {
		initializable.init(params);
		String s = initializable.getParam("b", "hi");
		assertEquals("hi", s);

		s = initializable.getParam("sflkj", "hi");
		assertEquals("hi", s);
	}

	@Test
	public void testType() {
		initializable.init(params);
		boolean b = initializable.getParam("", true);
		assertFalse(b);

		String s = initializable.getParam("d", "hi");
		assertEquals("hello", s);

		int i = initializable.getParam("c", 0);
		assertEquals(100, i);

		double d = initializable.getParam("b", 0.0);
		assertEquals(1.0, d, epsilon);
	}

	@Test
	public void test1() {
		Object o = initializable.getParam("a", 1984);
		assertEquals(1984, o);

		try {
			initializable.getParam("a", null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		o = initializable.getParam(null, 1984);
		assertEquals(1984, o);

		try {
			o = initializable.getParam(null, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	@Test
	public void test2() {
		initializable.init(params);

		Object o = null;
		try {
			o = initializable.getParam(null, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		o = initializable.getParam(null, 1984);
		assertEquals(1984, o);

		o = initializable.getParam("a", 1984);
		assertEquals(1984, o);

		o = initializable.getParam("", true);
		assertEquals(false, o);

		o = initializable.getParam("b", 0.0);
		assertEquals(1.0, o);
	}

	@Test
	public void test3() {
		// logger.log(Level.INFO, "test 3-----");
		initializable.init(null);

		Object o = initializable.getParam("a", 1984);
		assertEquals(1984, o);

		try {
			initializable.getParam("a", null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		o = initializable.getParam(null, 1984);
		assertEquals(1984, o);

		try {
			o = initializable.getParam(null, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

}