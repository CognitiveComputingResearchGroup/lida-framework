package edu.memphis.ccrg.lida.sensoryMemory;

public class SimulationContent {
	
	private int[] simSense;
	private String environment;
	
	public SimulationContent(int size){
		simSense = new int[size];
	}
	
	public void setContent(Object o, String environment){
		simSense = (int[])o;
		this.environment = environment;
	}
	
	public Object getSenseContent(){
		int[] copy = new int[simSense.length];
		System.arraycopy(simSense, 0, copy, 0, simSense.length);		
		return copy;
	}	
	
	public String getEnvironment(){
		return environment;
	}

	public void print(){
		System.out.println("SM:: " + simSense[0] + " " + simSense[1] + 
							" " + simSense[2] + " " + simSense[3] + " " + simSense[4] + " ");
	}

}//SimulationContent

