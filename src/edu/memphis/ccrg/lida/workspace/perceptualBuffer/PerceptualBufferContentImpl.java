package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import edu.memphis.ccrg.lida.perception.GraphImpl;
import edu.memphis.ccrg.lida.shared.Node;

public class PerceptualBufferContentImpl implements PerceptualBufferContent{

	private GraphImpl g = new GraphImpl(0.0, 0.0);
	
	public void addNode(Node n){
		g.addNode(n);
	}
	
	public void addContent(GraphImpl newGraph){
		g = newGraph;
	}
	
	public Object getContent(){
		return g;
	}
	
}//
