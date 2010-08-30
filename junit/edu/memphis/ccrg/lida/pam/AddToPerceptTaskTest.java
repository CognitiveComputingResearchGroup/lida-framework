/**
 * 
 */
package edu.memphis.ccrg.lida.pam;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Siminder Kaur
 *
 */
public class AddToPerceptTaskTest {
	
	PamNode pamNode;
	PerceptualAssociativeMemory pam;
	AddToPerceptTask addToTask;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		pamNode = new PamNodeImpl();
		pam = new PerceptualAssociativeMemoryImpl();
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.AddToPerceptTask#runThisLidaTask()}.
	 */
	@Test
	public void testRunThisLidaTask() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.AddToPerceptTask#AddToPerceptTask(edu.memphis.ccrg.lida.pam.PamNode, edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory)}.
	 */
	@Test
	public void testAddToPerceptTaskPamNodePerceptualAssociativeMemory() {
		addToTask = new AddToPerceptTask(pamNode, pam);
		fail("Not yet implemented"); // TODO
		
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.pam.AddToPerceptTask#AddToPerceptTask(edu.memphis.ccrg.lida.framework.shared.NodeStructure, edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory)}.
	 */
	@Test
	public void testAddToPerceptTaskNodeStructurePerceptualAssociativeMemory() {
		fail("Not yet implemented"); // TODO
	}

}
