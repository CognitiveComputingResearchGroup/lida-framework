package edu.memphis.ccrg.sensoryMemory;

public class SensoryContent implements SensoryContentInterface{

	private int[] senseData;
	
	public SensoryContent(){
		senseData = new int[5];
	}

	public SensoryContent(int size){
		senseData = new int[size];
	}
	
	public void setContent(Object o){
		senseData = (int[])o;
	}
	
	public Object getContent(){
		int[] copy = new int[senseData.length];
		System.arraycopy(senseData, 0, copy, 0, senseData.length);		
		return copy;
	}

}
