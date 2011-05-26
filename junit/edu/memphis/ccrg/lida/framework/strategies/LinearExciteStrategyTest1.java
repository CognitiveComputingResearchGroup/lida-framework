package edu.memphis.ccrg.lida.framework.strategies;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Nisrine 
 *
 */
public class LinearExciteStrategyTest1 {

	LinearExciteStrategy ExciteStrategy;
	LinearExciteStrategy ExciteStrategy1;
	
	@Before
	public void setUp() throws Exception {
	  ExciteStrategy = new LinearExciteStrategy ();
	  
	}
@Test
	  public void testExcite1() {
	  
	  double newact =   ExciteStrategy.excite(0.4, 0.2,(Object[])null);
	  assertEquals ("Error with testExcite 1 ()", 0.48, newact,0.0001);
	}

@Test
	public void testExcite2() {
	  Object [] params = new Object [2];
	  params [0]= 0.3;
	  params [1]= 0.2;
	  double newact =   ExciteStrategy.excite(0.4, 0.2,params);
	  assertEquals ("Error with testExcite 1 ()", 0.46, newact,0.0001);
	}
@Test
  public void testExcite3() {
     Object [] params = new Object [2];
     params [0]= "a";
     params [1]= 0.2;
     double newact =   ExciteStrategy.excite(0.4, 0.2,params);
     assertEquals ("Error with testExcite 1 ()", 0.48, newact,0.0001);
  }


@Test
  public void testExcite4() {
    Object [] params = new Object [2];
    params [0]= 0.3;
    params [1]= 0.2;
    double newact =   ExciteStrategy.excite(-0.4, 0.2,params);
    assertEquals ("Error with testExcite 1 ()",0.0, newact,0.0001);
  }

@Test
  public void testExcite5() {
    Map <String, Double> params = new HashMap <String, Double>(); 
    params.put("a",2.0);
    params.put("b",3.0);
    
    double newact =   ExciteStrategy1.excite(0.4, 0.2,params.values().toArray());
   // System.out.println(params.values().toArray());
    assertEquals ("Error with testExcite 1 ()",1.0, newact,0.0001);
  }
	

}
