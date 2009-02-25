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
		return content;
	}

}
