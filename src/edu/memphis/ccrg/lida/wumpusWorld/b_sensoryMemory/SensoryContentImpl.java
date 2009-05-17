package edu.memphis.ccrg.lida.wumpusWorld.b_sensoryMemory;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;

public class SensoryContentImpl implements SensoryContent{

	private int worldSize = 4; ///TODO: unhardcode this
	private char[][][] senseData = new char[worldSize][worldSize][4];
	
	public synchronized void setContent(Object o){
		senseData = (char[][][])o;
	}
	
	public Object getContent(){	
		char[][][] copy = new char[worldSize][worldSize][4];
		if(senseData != null)
			System.arraycopy(senseData, 0, copy, 0, senseData.length);		
		return copy;	
	}
	
	public Object getThis(){
		return this;
	}	
	
	public void print(){
		
	}

}
