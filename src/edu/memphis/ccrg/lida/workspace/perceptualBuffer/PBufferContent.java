package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import edu.memphis.ccrg.lida.perception.Percept;

public class PBufferContent implements PBufferContentInter {
	private Percept p;

	public PBufferContent(){
		p = new Percept();
	}
	
	public PBufferContent(Percept p){
		this.p = p;
	}
	
	public void setNodes(Percept p){
		this.p = p;
	}
	
	public Object getContent(){
		return p;
	}
	

}
