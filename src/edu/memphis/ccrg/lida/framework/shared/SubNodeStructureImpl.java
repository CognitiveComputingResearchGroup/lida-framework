package edu.memphis.ccrg.lida.framework.shared;

import java.util.Collection;
import java.util.Map;

public class SubNodeStructureImpl extends NodeStructureImpl{
	
	/**
	 * Finds and returns a sub NodeStructure that contains all source Nodes connected to specified Nodes
	 * after given distance.
	 * @param nodes set of nodes and distance
	 * @param distance distance from specified node
	 * @return sub NodeStructure
	 */
	
	public NodeStructure getSubNodeStructure(Collection<Node> nodes, int distance, double threshold)
	{
		NodeStructure subNodeStructure=new NodeStructureImpl();
		
		for (Node n : nodes) {
			int tempDistance=distance;
			((NodeStructureImpl)subNodeStructure).addNode(n,true);
			while(tempDistance-- >= 0){
				Map<Linkable,Link> connectedSinks=getConnectedSinks(n);
				for (Linkable sink : connectedSinks.keySet()) {
					Link l=connectedSinks.get(sink);
					if(sink instanceof Node && sink.getActivation() > threshold)
					{
						((NodeStructureImpl)subNodeStructure).addNode(sink,true);		
						((NodeStructureImpl)subNodeStructure).addDefaultLink(l);
						
					}
					else if(sink.getExtendedId().isSimpleLink())
					{
						
					}
					else if(sink.getExtendedId().isComplexLink())
					{
						
					}
					else
					{
						
					}
				}
			}
		}
		
		return subNodeStructure;
	}

}
