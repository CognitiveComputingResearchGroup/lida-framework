/**
 * 
 */
package edu.memphis.ccrg.lida.episodicmemory;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cern.colt.bitvector.BitVector;

/**
 * @author Javier Snaider
 *
 */
public class EpisodicMemoryImplTest {
	
	private EpisodicMemoryImpl em;

	/**
	 * @throws java.lang.Exception e
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception e
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception e
	 */
	@Before
	public void setUp() throws Exception {
		em=new EpisodicMemoryImpl();
		em.init();
	}

	/**
	 * @throws java.lang.Exception e
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.episodicmemory.EpisodicMemoryImpl#getModuleContent(java.lang.Object[])}.
	 */
	@Test
	public void testGetModuleContent() {
		assertEquals(null, em.getModuleContent());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.episodicmemory.EpisodicMemoryImpl#init()}.
	 */
	@Test
	public void testInit() {
		Map<String,Object>params = new HashMap<String,Object>();
		params.put("tem.numOfHardLoc", 20);
		params.put("tem.addressLength", 100);
		params.put("tem.activationRadius", 451);
		params.put("tem.wordLength", 150);
		em.init(params);
		
		Object[] data = (Object[])em.getState();
		assertEquals(20,((BitVector[])data[0]).length);
		assertEquals(100,((BitVector[])data[0])[0].size());
		assertEquals(150,((byte[][])data[1])[0].length);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.episodicmemory.EpisodicMemoryImpl#setAssociatedModule(edu.memphis.ccrg.lida.framework.LidaModule, String)}.
	 */
	@Test
	public void testSetAssociatedModule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.episodicmemory.EpisodicMemoryImpl#receiveBroadcast(edu.memphis.ccrg.lida.globalworkspace.BroadcastContent)}.
	 */
	@Test
	public void testReceiveBroadcast() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.episodicmemory.EpisodicMemoryImpl#receiveCue(edu.memphis.ccrg.lida.framework.shared.NodeStructure)}.
	 */
	@Test
	public void testReceiveCue() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.episodicmemory.EpisodicMemoryImpl#learn(edu.memphis.ccrg.lida.globalworkspace.BroadcastContent)}.
	 */
	@Test
	public void testLearn() {
		
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.episodicmemory.EpisodicMemoryImpl#addListener(edu.memphis.ccrg.lida.framework.ModuleListener)}.
	 */
	@Test
	public void testAddListener() {
		fail("Not yet implemented");
	}

}
