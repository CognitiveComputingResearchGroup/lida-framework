/**
 * 
 */
package edu.memphis.ccrg.lida.episodicmemory;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.mockclasses.MockLocalAssocListener;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTranslator;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

/**
 * @author Javier Snaider
 */
public class EpisodicMemoryImplTest {
	
	private static final ElementFactory factory = ElementFactory.getInstance();

	private EpisodicMemoryImpl em;
	private MockTranslator translator;
	private NodeStructureImpl content;
	private Node node1;
	private MockLocalAssocListener listener;

	/**
	 * @throws java.lang.Exception e
	 */
	@Before
	public void setUp() throws Exception {
		em=new EpisodicMemoryImpl();
		em.init();
		translator=new MockTranslator();
    	content = new NodeStructureImpl();
    	node1 = factory.getNode();
    	listener=new MockLocalAssocListener();
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
	public void testReceiveBroadcast() {
		em.setTranslator(translator);
		em.receiveBroadcast(content);
		
		assertEquals(content, translator.ns);
		BitVector v = em.getSdm().retrieve(translator.v);
		assertEquals(translator.v, v);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.episodicmemory.EpisodicMemoryImpl#receiveCue(edu.memphis.ccrg.lida.framework.shared.NodeStructure)}.
	 */
	@Test
	public void testReceiveCue() {
		NodeStructure ns2 =  new NodeStructureImpl();
		ns2.addDefaultNode(node1);
		translator.ns2 = ns2;
		em.getSdm().store(translator.v);
		em.setTranslator(translator);
		em.addListener(listener);
		
		em.receiveCue(content);
		assertEquals(content, translator.ns);
		assertEquals(translator.v,translator.data);
		assertEquals(ns2,listener.ns);
	}


	/**
	 * Test method for {@link edu.memphis.ccrg.lida.episodicmemory.EpisodicMemoryImpl#addListener(edu.memphis.ccrg.lida.framework.ModuleListener)}.
	 */
	@Test
	public void testAddListener() {
		NodeStructure ns2 =  new NodeStructureImpl();
		ns2.addDefaultNode(node1);
    	MockLocalAssocListener listener2=new MockLocalAssocListener();

		translator.ns2 = ns2;
		em.getSdm().store(translator.v);
		em.setTranslator(translator);
		em.addListener(listener);
		em.addListener(listener2);
		
		em.receiveCue(content);
		
		assertEquals(ns2,listener.ns);
		assertEquals(ns2,listener2.ns);
	}

	@Test
	public void testsetTranslator() {
		em.setTranslator(translator);
		assertEquals(translator, em.getTranslator());
		
	}

}
