package edu.memphis.ccrg.lida.framework.initialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

public class InitializableImplTest {

	private static final Logger logger = Logger.getLogger(InitializableImplTest.class.getCanonicalName());
	private InitializableImpl initializable;
	private Map<String, Object> params;
	
	@Before
	public void setup(){
		initializable = new InitializableImpl();
		params = new HashMap<String, Object>();
		params.put("a", null);
		params.put("", false);
		params.put("b", 1.0);
	}
	
	@Test
	public void test1() {
		Object o = initializable.getParam("a", 1984);
		assertEquals(1984, o);
		
		o = initializable.getParam("a", null);
		assertNull(o);
		
		o = initializable.getParam(null, 1984);
		assertEquals(1984, o);
		
		o = initializable.getParam(null, null);
		assertNull(o);
	}
	
	@Test
	public void test2() {
		initializable.init(params);
		
		Object o = initializable.getParam(null, null);
		assertNull(o);
		
		o = initializable.getParam(null, 1984);
		assertEquals(1984, o);
		
		o = initializable.getParam("a", 1984);
		assertEquals(1984, o);
		
		o = initializable.getParam("", 1984);
		assertEquals(false, o);
		
		o = initializable.getParam("b", 1984);
		assertEquals(1.0, o);
	}
	
	@Test
	public void test3() {
		logger.log(Level.INFO, "test 3-----");
		initializable.init(null);
		
		Object o = initializable.getParam("a", 1984);
		assertEquals(1984, o);
		
		o = initializable.getParam("a", null);
		assertNull(o);
		
		o = initializable.getParam(null, 1984);
		assertEquals(1984, o);
		
		o = initializable.getParam(null, null);
		assertNull(o);
	}	

}