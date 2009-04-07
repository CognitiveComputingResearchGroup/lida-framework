package edu.memphis.ccrg.lida._environment.wumpusWorld;



import java.util.Random;

import edu.memphis.ccrg.lida._environment.main.SimulationListener;
import edu.memphis.ccrg.lida._environment.wumpusWorld.Simulation;
import edu.memphis.ccrg.lida.actionSelection.ActionContent;
import edu.memphis.ccrg.lida.actionSelection.ActionSelectionListener;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;

//import java.util.concurrent.ConcurrentLinkedQueue;

public class WorldApplication implements Runnable, Stoppable, ActionSelectionListener{
	
	private Simulation trial;
	private long threadID;
	
	public void stopRunning(){
		trial.stopRunning();		
	}
	
	public WorldApplication(FrameworkTimer timer){
		int worldSize = 4;
		int numPits = 7;				
		boolean nonDeterministicMode = false;
		boolean randomAgentLoc = true;		
	    Random rand = new Random();
	    int seed = rand.nextInt();	    
	    char[][][] wumpusWorld = generateRandomWumpusWorld(seed, worldSize, randomAgentLoc, numPits);
	    Environment wumpusEnvironment = new Environment(worldSize, wumpusWorld); //, outputWriter);
	   	trial = new Simulation(timer, wumpusEnvironment, nonDeterministicMode); 		
	}
	
	public void addSimulationListener(SimulationListener listener){
		trial.addSimulationListener(listener);
	}
	

	public void receiveBehaviorContent(ActionContent c) {
		trial.receiveBehaviorContent(c);		
	}
	
	public void run(){		
	   	trial.runSim();	   	
    	System.runFinalization();
	}//method run
	
	public void setThreadID(long id){
		trial.setThreadID(id);
	}
	
	public long getThreadID() {
		return trial.getThreadID();
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
	
	public void run2(){
//		int worldSize = 4;
//		int numTrials = 1;
//		int numPits = 2;		
//		
//		int maxSteps = 1;
//		
//		boolean nonDeterministicMode = false;
//		boolean randomAgentLoc = false;
//		//boolean userDefinedSeed = false;
//		
//		//String outFilename = "wumpus_out.txt";
//		
//	    Random rand = new Random();
//	   // int seed = rand.nextInt();
	    	    
	    try{
	    	
		    //BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outFilename));
			/*
			System.out.println("Dimensions: " + worldSize + "x" + worldSize);
		    //outputWriter.write("Dimensions: " + worldSize + "x" + worldSize + "\n");
			
		    System.out.println("Maximum number of steps: " + maxSteps);
		    //outputWriter.write("Maximum number of steps: " + maxSteps + "\n");
		    
		    System.out.println("Number of trials: " + numTrials);
		    //outputWriter.write("Number of trials: " + numTrials + "\n");
		    
		    System.out.println("Random Agent Location: " + randomAgentLoc);
		    //outputWriter.write("Random Agent Location: " + randomAgentLoc + "\n");
		    
			System.out.println("Random number seed: " + seed);
			//outputWriter.write("Random number seed: " + seed + "\n");		    	
		    
		    //System.out.println("Output filename: " + outFilename);
		    //outputWriter.write("Output filename: " + outFilename + "\n");
		    
		    System.out.println("Non-Deterministic Behavior: " + nonDeterministicMode + "\n");
		    //outputWriter.write("Non-Deterministic Behavior: " + nonDeterministicMode + "\n\n");
		    */
		    
		   // char[][][] wumpusWorld = generateRandomWumpusWorld(seed, worldSize, randomAgentLoc, numPits);
		    //Environment wumpusEnvironment = new Environment(worldSize, wumpusWorld); //, outputWriter);
		    
		    //int trialScores[] = new int[numTrials];
		   // int totalScore = 0;
	    
		    //for (int currTrial = 0; currTrial < numTrials; currTrial++) {
		    		    	
		    	//Simulation trial = new Simulation(wumpusEnvironment, maxSteps, nonDeterministicMode); //, outputWriter, nonDeterministicMode);
		    	//trialScores[currTrial] = trial.getScore();
		    	//totalScore = trial.getScore();
		    	
		    	//System.out.println("\n\n___________________________________________\n");
		    	//outputWriter.write("\n\n___________________________________________\n\n");
		    	
			  //  if (userDefinedSeed == true) {
			  //  	wumpusWorld = generateRandomWumpusWorld(++seed, worldSize, randomAgentLoc);	
			   // }
			   // else {
			 //   	wumpusWorld = generateRandomWumpusWorld(rand.nextInt(), worldSize, randomAgentLoc);
			//    }

			//    wumpusEnvironment = new Environment(worldSize, wumpusWorld); //, outputWriter);

		    	System.runFinalization();
		   // }

		    /*for (int i = 0; i < numTrials; i++) {
		    	
		    	System.out.println("Trial " + (i+1) + " score: " + trialScores[i]);
		    	//outputWriter.write("Trial " + (i+1) + " score: " + trialScores[i] + "\n");
		    	totalScore += trialScores[i];
		    	
		    }*/
		    
		    //System.out.println("\nTotal Score: " + totalScore);
		    //outputWriter.write("\nTotal Score: " + totalScore + "\n");
		    
		    //System.out.println("Average Score: " + ((double)totalScore/(double)numTrials));
		    //outputWriter.write("Average Score: " + ((double)totalScore/(double)numTrials) + "\n");
		    
		    //outputWriter.close();
		    
	    }
	    catch (Exception e) {
	    	System.out.println("An exception was thrown: " + e);
	    }
	    
	    //System.out.println("\nFinished.");			
	}//run2	
	
}//class WorldApplication