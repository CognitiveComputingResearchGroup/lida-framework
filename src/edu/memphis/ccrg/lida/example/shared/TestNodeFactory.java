package edu.memphis.ccrg.lida.example.shared;

import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeFactory;
import edu.memphis.ccrg.lida.shared.strategies.LinearDecayCurve;

public class TestNodeFactory {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NodeFactory factory = NodeFactory.getInstance();
		
		//adding a Node Type to the Factory
		factory.addNodeType("PamNode", "edu.memphis.ccrg.lida.perception.PamNodeImpl");

		//adding a decaybehavior type to the Factory
		factory.addDecayBehavior("linear", new LinearDecayCurve());
		
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
		
		System.out.println("Activation: "+n.getCurrentActivation());
		System.out.println("Decaying...");

		n.decay();
		System.out.println("Activation: "+n.getCurrentActivation());
		

	}

}
