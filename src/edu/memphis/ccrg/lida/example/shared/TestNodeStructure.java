package edu.memphis.ccrg.lida.example.shared;

import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.LinkType;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeFactory;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

public class TestNodeStructure {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		NodeStructure ns = new NodeStructureImpl();
		ns.setDefaultNode("NodeImpl");
		ns.setDefaultLink("LinkImpl");
		NodeFactory.getInstance().addNodeType("NodeImpl", "edu.memphis.ccrg.lida.shared.NodeImpl");
		NodeFactory.getInstance().addLinkType("LinkImpl", "edu.memphis.ccrg.lida.shared.LinkImpl");
		
		
		Node n2;
		
		ns.addNode(n2=NodeFactory.getInstance().getNode("NodeImpl",10L));
		ns.addNode(NodeFactory.getInstance().getNode("NodeImpl",20L));
		ns.addNode(NodeFactory.getInstance().getNode("NodeImpl",30L));
		
		Link l = NodeFactory.getInstance().getLink("LinkImpl", ns.getNode(10L), ns.getNode(20L), LinkType.parent);
		ns.addLink(l);
		
		l=NodeFactory.getInstance().getLink("LinkImpl", ns.getNode(20L), ns.getNode(30L), LinkType.lateral);
		ns.addLink(l);

		Link l2=NodeFactory.getInstance().getLink("LinkImpl", ns.getNode(20L), ns.getLink("L(20:30:lateral)"), LinkType.lateral);
		ns.addLink(l2);
		

		showState(ns);
		
		ns.deleteLink(l2);
		
		showState(ns);
		
		ns.deleteNode(ns.getNode(30L));
		showState(ns);

	}
static void showState(NodeStructure ns){
	for (Node n: ns.getNodes()){
		System.out.println("Node-> "+n.getId()+" "+n.getLabel()+ " "+ns.getLinks(n));
		
	}
	for (Link link: ns.getLinks()){
		System.out.println("Link-> "+link.getIds()+ " "+ns.getLinks(link));
	}
	System.out.println();

}
}
