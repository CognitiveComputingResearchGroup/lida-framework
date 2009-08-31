package edu.memphis.ccrg.lida.example.shared;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeFactory;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayBehavior;

public class TestNodeFactory {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NodeFactory factory = NodeFactory.getInstance();
		
		//adding a Node Type to the Factory
		factory.addNodeType("PamNode", "edu.memphis.ccrg.lida.perception.PamNodeImpl");

		//adding a decaybehavior type to the Factory
		factory.addDecayBehavior("linear", new LinearDecayBehavior());
		
		//Creating a default Node
		Node n=factory.getNode();
		
		System.out.println(n);
		System.out.println("NodeType: "+n.getClass().getName());
		
		//Creating a PamNode
		n=factory.getNode("PamNode");
		
		//setting the decayBehavior 
		n.setDecayBehavior(factory.getDecayBehavior("linear"));
		//setting activation
		n.setActivation(.5);
		
		System.out.println(n);
		System.out.println("NodeType: "+n.getClass().getName());
		
		System.out.println("Activation: "+n.getActivation());
		System.out.println("Decaying...");

		n.decay();
		System.out.println("Activation: "+n.getActivation());
		

	}

}
