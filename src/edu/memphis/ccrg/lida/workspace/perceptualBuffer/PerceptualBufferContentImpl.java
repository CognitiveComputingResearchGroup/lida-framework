package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.GraphImpl;

public class PerceptualBufferContentImpl implements PerceptualBufferContent{

	private GraphImpl g = new GraphImpl();
	
	public PerceptualBufferContentImpl(GraphImpl g){
		this.g = g;
	}
	
	public PerceptualBufferContentImpl() {
		// TODO Auto-generated constructor stub
	}

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
