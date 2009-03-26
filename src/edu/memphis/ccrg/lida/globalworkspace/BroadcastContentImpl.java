package edu.memphis.ccrg.lida.globalworkspace;

public class BroadcastContentImpl implements BroadcastContent{
	
	private Coalition c;
	
	public BroadcastContentImpl(Coalition c){
		this.c = c;
	}
	
	public void setContent(Coalition c){
		this.c = c;
	}

	public Object getContent() {
		return c;
	}

}
