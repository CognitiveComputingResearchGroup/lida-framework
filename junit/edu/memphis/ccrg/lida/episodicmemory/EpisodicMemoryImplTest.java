/**
 * 
 */
package edu.memphis.ccrg.lida.episodicmemory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.mockclasses.MockFrameworkModule;
import edu.memphis.ccrg.lida.framework.mockclasses.MockPAM;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * @author Javier Snaider
 */
public class EpisodicMemoryImplTest {
	
	private EpisodicMemoryImpl em;
	private PerceptualAssociativeMemory pam;

	/**
	 * @throws java.lang.Exception e
	 */
	@Before
	public void setUp() throws Exception {
		em=new EpisodicMemoryImpl();
		pam = new MockPAM();
		em.init();
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

	@Test
	public void testSetAssociatedModule() {
		em.setAssociatedModule(pam, ModuleUsage.NOT_SPECIFIED);
		assertEquals(pam, em.getPam());
	}

	@Test
	public void testSetAssociatedModule2() {
		FrameworkModule module = new MockFrameworkModule();
		em.setAssociatedModule(module , ModuleUsage.NOT_SPECIFIED);
		assertEquals(null, em.getPam());
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
	 * Test method for {@link edu.memphis.ccrg.lida.episodicmemory.EpisodicMemoryImpl#addListener(edu.memphis.ccrg.lida.framework.ModuleListener)}.
	 */
	@Test
	public void testAddListener() {
		fail("Not yet implemented");
	}

}
