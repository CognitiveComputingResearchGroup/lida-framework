package edu.memphis.ccrg.lida.wumpusWorld.a_environment;

public class EnvironmentContentImpl {
	
	private char[][][] simSense;//For LIDA
	private String environment;//For GUI
	private String senseMatrix;//For GUI
	private String message = "";
	private String lastAction = "";
	
	public EnvironmentContentImpl(int size){
		simSense = new char[size][size][4];
	}
	
	public void setContent(Object o, String environment, String senseMatrix, 
						   String message, String lastAction){
		simSense = (char[][][])o;
		this.environment = environment;
		this.senseMatrix = senseMatrix;
		this.message = message;
		this.lastAction = lastAction;
	}//method
	
	public Object getSenseContent(){
		char[][][] copy = new char[simSense.length][simSense[0].length][simSense[0][0].length];
		System.arraycopy(simSense, 0, copy, 0, simSense.length);		
		return copy;
	}	
	
	public String getEnvironment(){
		return environment;
	}
	
	public String getSenseMatrix(){
		return senseMatrix;
	}

	public void print(){

	}

	public int getSize() {
		return simSense.length;
	}

	public String getMessage() {
		return message;
	}

	public String getLastAction() {
		return lastAction;
	}

}//class