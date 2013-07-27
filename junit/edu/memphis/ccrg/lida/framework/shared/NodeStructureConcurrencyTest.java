/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.Link;
import edu.memphis.ccrg.lida.framework.shared.ns.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNSImpl;

public class NodeStructureConcurrencyTest {

	private static final Logger logger = Logger.getLogger("Testthreads");
	private Node[] nodes = new Node[10];
	private Link link1, link2;
	private LinkCategory category1, category2;
	private NodeStructureImpl ns1, ns2;
	private static ElementFactory factory;

	@BeforeClass
	public static void setUpBeforeClass() {
		factory = ElementFactory.getInstance();
	}

	/**
	 * This method is called before running each test case to initialize the
	 * objects
	 * 
	 * @throws Exception
	 *             e
	 * 
	 */
	@Before
	public void setUp() throws Exception {
		nodes[0] = factory.getNode();
		nodes[0].setLabel("red");
		nodes[0].setActivation(0.1);

		nodes[1] = factory.getNode();
		nodes[1].setLabel("blue");
		nodes[1].setActivation(0.2);

		nodes[2] = factory.getNode();
		nodes[2].setLabel("purple");
		nodes[2].setActivation(0.3);

		nodes[3] = factory.getNode();
		nodes[3].setLabel("green");
		nodes[3].setActivation(0.4);

		category1 = PerceptualAssociativeMemoryNSImpl.NONE;
		category2 = PerceptualAssociativeMemoryNSImpl.FEATURE;

		link1 = factory.getLink(nodes[0], nodes[1], category1);
		link2 = factory.getLink(nodes[1], nodes[2], category2);

		ns1 = new NodeStructureImpl();
		ns2 = new NodeStructureImpl();

		ns1.addDefaultNode(nodes[0]);
		ns1.addDefaultNode(nodes[1]);
		ns1.addDefaultNode(nodes[3]);
		ns1.addDefaultLink(link1);

		ns2.addDefaultNode(nodes[0]);
		ns2.addDefaultNode(nodes[1]);
		ns2.addDefaultNode(nodes[2]);
		ns2.addDefaultLink(link2);
	}

	@Test
	public void concurrencyTest() {
		Thread[] threads = new Thread[10];

		for (int i = 0; i < 10; i++) {
			threads[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					Node n = factory.getNode();
					try {
						for (int j = 0; j < 5000; j++) {
							int a = (int) (Math.random() * 7);
							if ((j % 2000) == 0) {
								logger.log(Level.INFO, ""
										+ Thread.currentThread().getName()
										+ ":" + a + " j:" + j);
							}
							switch (a) {
							case 0:
								n = factory.getNode();
								ns1.addDefaultNode(n);
								break;
							case 1:
								Node n1 = factory.getNode();
								Node n2 = factory.getNode();
								ns1.addDefaultNode(n1);
								ns1.addDefaultNode(n2);
								ns1.addDefaultLink(n1, n2, category1, 1.0, 0.0);
								break;

							case 2:
								int sum = 0;
								Collection<Node> nodesCol = ns1.getNodes();
								for (Node nn : nodesCol) {
									sum += nn.getId();
								}
								break;
							case 3:
								NodeStructure ns4 = new NodeStructureImpl();
								Node n3 = factory.getNode();
								Node n4 = nodes[((int) (Math.random() * 4))];

								ns4.addDefaultNode(n3);
								ns4.addDefaultNode(n4);

								ns4.addDefaultLink(n3, n4, category1, .5, 0.0);

								ns1.mergeWith(ns4);
								break;
							case 4:
								ns1.removeNode(n);
								break;

							case 5:
								ns1.clearLinks();
								break;
							case 6:
								ns1.decayNodeStructure(1);
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						fail();
					}

				}
			});
		}

		for (int i = 0; i < 10; i++) {
			threads[i].start();
		}
		for (int i = 0; i < 10; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
