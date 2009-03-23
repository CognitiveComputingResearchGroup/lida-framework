package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import edu.memphis.ccrg.lida.perception.Percept;

public class PerceptualBufferContentImpl implements PerceptualBufferContent{
	private Percept p;

	public PerceptualBufferContentImpl(){
		p = new Percept();
	}
	
	public PerceptualBufferContentImpl(Percept p){
		this.p = p;
	}
	
	public void setNodes(Percept p){
		this.p = p;
	}
	
	public Object getContent(){
		return p;
	}
	

}
