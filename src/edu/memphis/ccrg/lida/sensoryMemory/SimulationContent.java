package edu.memphis.ccrg.lida.sensoryMemory;

public class SimulationContent {
	
	private int[] content;
	
	public SimulationContent(int size){
		content = new int[size];
	}
	
	public void setContent(Object o){
		content = (int[])o;
	}
	
	public Object getContent(){
		int[] copy = new int[content.length];
		System.arraycopy(content, 0, copy, 0, content.length);		
		return copy;
	}	

	public void print(){
		System.out.println("SM:: " + content[0] + " " + content[1] + " " + content[2] + " " + content[3] + " " + content[4] + " ");
	}

}

