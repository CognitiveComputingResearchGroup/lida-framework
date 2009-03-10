package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.perception.interfaces.PAMContent;

public class PAMContentImpl implements PAMContent{

	private Percept p;

	public PAMContentImpl(){
		p = new Percept();
	}
	
	public PAMContentImpl(Percept p){
		this.p = p;
	}
	
	public void setNodes(Percept p){
		this.p = p;
	}
	
	public Object getContent(){
		return p;
	}
	
	public void print(){
		p.print();
	}

}
