package edu.memphis.ccrg.lida.episodicmemory.sdm;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.mockclasses.MockPAM;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

public class BasicTranslatorTest {

	private Translator tr;
	private static int SIZE=100;
	private PerceptualAssociativeMemory pam;
	private NodeStructure ns;
	private BitVector vector;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		pam= new MockPAM();
		tr = new BasicTranslator(SIZE, pam);
		ns = new NodeStructureImpl();
		Node n = new NodeImpl();
		n.setId(10);
		ns.addDefaultNode(n);
		n = new NodeImpl();
		n.setId(20);
		ns.addDefaultNode(n);
		vector = new BitVector(SIZE);
		vector.set(10);
		vector.set(20);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTranslateBitVector() {
		BitVector v = tr.translate(ns);
		assertEquals(vector, v);
	}

	@Test
	public void testTranslateNodeStructure() {
		NodeStructure nss = tr.translate(vector);
		assert(nss.getNodes().equals(ns.getNodes()));
	}

}
