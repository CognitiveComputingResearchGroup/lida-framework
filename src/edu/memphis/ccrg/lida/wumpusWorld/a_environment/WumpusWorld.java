package edu.memphis.ccrg.lida.wumpusWorld.a_environment;



import java.io.BufferedWriter;
import java.util.List;
import java.util.Random;

import edu.memphis.ccrg.lida.actionSelection.ActionContent;
import edu.memphis.ccrg.lida.actionSelection.ActionSelectionListener;
import edu.memphis.ccrg.lida.environment.EnvironmentListener;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.wumpusWorld.a_environment.Simulation;

public class WumpusWorld implements Runnable, Stoppable, ActionSelectionListener{
	
	private Simulation simulation;
	//WW parameters
	private final int wumpusWorldSize = 5;
	private final double pitPercentage = 0.25;
	private boolean randomAgentLoc = true;
	private boolean nonDeterministicMode = false;
	//
	private int numPits = (int)(wumpusWorldSize*wumpusWorldSize*pitPercentage);				
	//
	private long threadID;
	
	//Standard Constructor
	public WumpusWorld(FrameworkTimer timer){
	    int seed = new Random().nextInt();	    
	    char[][][] wumpusWorld = generateRandomWumpusWorld(seed, wumpusWorldSize, randomAgentLoc, numPits);
	    Environment wumpusEnvironment = new Environment(wumpusWorldSize, wumpusWorld);
	   	simulation = new Simulation(timer, wumpusEnvironment, nonDeterministicMode); 		
	}

	//*************************For testing******************
	public WumpusWorld(FrameworkTimer timer, Environment e, BufferedWriter out, Starter s) {
	   	simulation = new Simulation(timer, e, nonDeterministicMode, out, s);
	}//************
	
//	//For environment reset functionality through the GUI
//	public void getNewEnvironment() {
//		int seed = new Random().nextInt();
//		char[][][] wumpusWorld = generateRandomWumpusWorld(seed, wumpusWorldSize, randomAgentLoc, numPits);
//	    Environment wumpusEnvironment = new Environment(wumpusWorldSize, wumpusWorld);
//		simulation.setNewEnvironment(wumpusEnvironment);
//	}
	
	public void addEnvironmentListener(EnvironmentListener listener){
		simulation.addEnvironmentListener(listener);
	}

	public void receiveBehaviorContent(ActionContent c) {
		simulation.receiveBehaviorContent(c);		
	}
	
	public void run(){		
	   	simulation.runSim();	   	
    	System.runFinalization();
	}//method run
	
	public void stopRunning(){
		simulation.stopRunning();		
	}
	
	public void setThreadID(long id){
		simulation.setThreadID(id);
	}
	
	public long getThreadID() {
		return simulation.getThreadID();
	}
	
	public static char[][][] generateRandomWumpusWorld(int seed, int size, boolean randomlyPlaceAgent, int pits){		
		char[][][] newWorld = new char[size][size][4];
		boolean[][] occupied = new boolean[size][size];		
		int x, y;		
		Random randGen = new Random(seed);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int k = 0; k < 4; k++) {
					newWorld[i][j][k] = '_';  
				}
			}
		}
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				occupied[i][j] = false;
			}
		}
		
		// default agent location
		// and orientation
		int agentXLoc = 0;
		int agentYLoc = 0;
		char agentIcon = '>';		
		// randomly generate agent
		// location and orientation
		if (randomlyPlaceAgent == true) {			
			agentXLoc = randGen.nextInt(size);
			agentYLoc = randGen.nextInt(size);
			
			switch (randGen.nextInt(4)) {				
				case 0: agentIcon = 'A'; break;
				case 1: agentIcon = '>'; break;
				case 2: agentIcon = 'V'; break;
				case 3: agentIcon = '<'; break;
			}
		}
		
		// place agent in the world
		newWorld[agentXLoc][agentYLoc][3] = agentIcon;
	     
		// Pit generation
		for (int i = 0; i < pits; i++) {	     
			x = randGen.nextInt(size);
			y = randGen.nextInt(size);		     
			while ((x == agentXLoc && y == agentYLoc) | occupied[x][y] == true) {
				x = randGen.nextInt(size);
				y = randGen.nextInt(size);    	   
			}		     
			occupied[x][y] = true;
			newWorld[x][y][0] = 'P';		     
		}

		// Wumpus Generation
		x = randGen.nextInt(size);
		y = randGen.nextInt(size);
	     
		while (x == agentXLoc && y == agentYLoc) {
			x = randGen.nextInt(size);
			y = randGen.nextInt(size);   
		}
		occupied[x][y] = true;
		newWorld[x][y][1] = 'W';
		
		// Gold Generation
		x = randGen.nextInt(size);
		y = randGen.nextInt(size);
	     
		//while (x == 0 && y == 0) {
		//	x = randGen.nextInt(size);
		//	y = randGen.nextInt(size);    	   
		//}
	     
		occupied[x][y] = true;	     
		newWorld[x][y][2] = 'G';
		
		return newWorld;
	}//GEN WW
	
}//class WorldApplication