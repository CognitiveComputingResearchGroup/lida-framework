package edu.memphis.ccrg.lida.sensoryMemory;

public class SimulationContent {
	
	private int[][][] simSense;
	private String environment;
	
	public SimulationContent(int size){
		simSense = new int[size][size][4];
	}
	
	public void setContent(Object o, String environment){
		simSense = (int[][][])o;
		this.environment = environment;
	}
	
	public Object getSenseContent(){
		int[][][] copy = new int[simSense.length][simSense[0].length][simSense[0][0].length];
		System.arraycopy(simSense, 0, copy, 0, simSense.length);		
		return copy;
	}	
	
	public String getEnvironment(){
		return environment;
	}

	public void print(){
		for(int i = 0; i < simSense.length; i++){
			for(int j = 0; j < simSense[0].length; j++){
				System.out.print(simSense[i][j] + " ");
			}
			System.out.println();
		}
	}

}//SimulationContent

