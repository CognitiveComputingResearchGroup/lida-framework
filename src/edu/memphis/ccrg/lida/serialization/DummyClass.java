package edu.memphis.ccrg.lida.serialization;

public class DummyClass {
	
	private int howDumb = 0;
	
	public DummyClass(int a){
		howDumb = a;
	}

	public int getIQ() {
		return howDumb;
	}
	
	public String toString(){
		return "dummy " + howDumb;
	}

}
