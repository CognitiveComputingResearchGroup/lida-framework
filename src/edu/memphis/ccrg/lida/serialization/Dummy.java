package edu.memphis.ccrg.lida.serialization;

public class Dummy {
	
	private int howDumb = 0;
	
	public Dummy(int a){
		howDumb = a;
	}

	public int getIQ() {
		return howDumb;
	}
	
	public String toString(){
		return "dummy " + howDumb;
	}

}
