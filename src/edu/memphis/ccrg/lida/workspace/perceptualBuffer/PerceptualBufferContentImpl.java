package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import edu.memphis.ccrg.lida.perception.Percept;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.shared.Node;

public class PerceptualBufferContentImpl implements PerceptualBufferContent{
	private Percept p;
	//private Graph g;

	public PerceptualBufferContentImpl(){
		p = new Percept();
	}
	
	public PerceptualBufferContentImpl(Percept p){
		this.p = p;
	}
	
	public void addNode(Node n){
		p.add((PamNodeImpl)n);
	}
	
	public void addContent(Percept p){
		this.p = p;
	}
	
	public Object getContent(){
		return p;
	}
	

}
