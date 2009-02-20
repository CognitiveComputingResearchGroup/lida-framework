package edu.memphis.ccrg.wumpus;

import java.util.ArrayList;

import edu.memphis.ccrg.sensoryMemory.SensoryMemory;
import edu.memphis.ccrg.sensoryMemory.SimulationContent;
import edu.memphis.ccrg.sensoryMemory.SimulationListener;


public class Simulation {
	
	private final int SENSE_SIZE = 5;
	private int currScore = 0;
	private static int actionCost = -1;
	private static int deathCost = -1000;
	private static int shootCost = -10;
	private int stepCounter = 0;
	private int lastAction = 0;
	
	private boolean simulationRunning;
	
	private Agent agent;
	private Environment environment;
	private TransferPercept transferPercept;
	private int[] currentSense;
	private SimulationContent simContent;
	//private boolean simHasChanged;
	
	private int maxSteps;
	private boolean nonDeterministic;
	private ArrayList<SimulationListener> listeners;
	
	public Simulation(Environment wumpusEnvironment, int numSteps, boolean nonDet){ 		
		environment = wumpusEnvironment;
		maxSteps = numSteps;
		nonDeterministic = nonDet;
		currentSense = new int[SENSE_SIZE];
		simContent = new SimulationContent(SENSE_SIZE);
		listeners = new ArrayList<SimulationListener>();
		//simHasChanged = true;
	}//Simulation
	
	public void addSimulationListener(SimulationListener listener){
		listeners.add(listener);
	}
	
	public void runSim(){
		simulationRunning = true;
		transferPercept = new TransferPercept(environment);				
		agent = new Agent(environment, transferPercept, nonDeterministic);
		
		environment.placeAgent(agent);
		environment.printEnvironment();
			
		int timesAdded = 0;
		long startTime = System.currentTimeMillis();
			
		while (simulationRunning) {								
			//handleAction(agent.chooseAction());//add in later after action selection is made
			environment.placeAgent(agent);
			senseEnvironment();
			
			simContent.setContent(currentSense);			
			
			for(int i = 0; i < listeners.size(); i++)
				(listeners.get(i)).receiveSimContent(simContent);
			
			System.out.println("Sense sent    : " + currentSense[0] + " " + currentSense[1] + " " + currentSense[2] + " " + currentSense[3] + " " + currentSense[4] + " ");
			timesAdded++;
							
			//environment.printEnvironment();								
			//printCurrentPerceptSequence();
					
			stepCounter += 1;
				
			if (stepCounter == maxSteps || simulationRunning == false) {
				//System.out.println("Last action: " + Action.printAction(lastAction));					
				//System.out.println("Time step: " + stepCounter);
					
				lastAction = Action.END_TRIAL;
			}
				
			if (agent.getHasGold() == true) 
				System.out.println("\n" + agent.getName() + " found the GOLD!!");
				
			if (agent.getIsDead() == true) 
				System.out.println("\n" + agent.getName() + " is DEAD!!");
						
			try{Thread.sleep(23);}
			catch(Exception e){}
			
		}//while keepRunning and trials
		long finishTime = System.currentTimeMillis();		
		System.out.println("S: ave sim cycle time " + (finishTime - startTime)/(double)stepCounter);		
		System.out.println("S: total times added " + timesAdded);			
		//printEndWorld();		
		System.out.println("S: Simulation ending");
	}//method runSim
	
	public void stopRunning(){
		simulationRunning = false;		
	}//stopRunning
	
	public void senseEnvironment(){
		//System.out.println("sense list has size " + currentSense.length + " beg of sense method");
		
		for(int i = 0; i < currentSense.length; i++)
			currentSense[i] = 0;
		
		if (transferPercept.getBump() == true) {
			currentSense[0] = 1;
			//System.out.print(" bump,");
			//outputWriter.write("bump,");
		}
		//else if (transferPercept.getBump() == false) {
			//System.out.print(" none,");
			//outputWriter.write("none,");
		//}
		if (transferPercept.getGlitter() == true) {
			currentSense[1] = 1;
			//System.out.print(" glitter,");
			//outputWriter.write("glitter,");
		}
		//else if (transferPercept.getGlitter() == false) {
			//System.out.print(" none,");
			//outputWriter.write("none,");
		//}
		if (transferPercept.getBreeze() == true) {
			currentSense[2] = 1;
			//System.out.print(" breeze,");
			//outputWriter.write("breeze,");
		}
		//else if (transferPercept.getBreeze() == false) {
			//System.out.print(" none,");
			//outputWriter.write("none,");
		//}
		if (transferPercept.getStench() == true) {
			currentSense[3] = 1;
			//System.out.print(" stench,");
			//outputWriter.write("stench,");
		}
		//else if (transferPercept.getStench() == false) {
			//System.out.print(" none,");
			//outputWriter.write("none,");
		//}
		if (transferPercept.getScream() == true) {
			currentSense[4] = 1;
			//System.out.print(" scream>\n");
			//outputWriter.write("scream>\n");
		}
		//else if (transferPercept.getScream() == false) {
		//	System.out.print(" none>\n");
			//outputWriter.write("none>\n");
		//}
		
		//System.out.println("sense list has size " + currentSense.length + " end of sense method");
	}//senseEnvironment	
	
	public void printEndWorld() {
		
		try {			
			environment.printEnvironment();
			
			System.out.println("Final score: " + currScore);
			//outputWriter.write("Final score: " + currScore + "\n");
			
			System.out.println("Last action: " + Action.printAction(lastAction));
			//outputWriter.write("Last action: " + Action.printAction(lastAction) + "\n");

		}
		catch (Exception e) {
			System.out.println("An exception was thrown: " + e);
		}
		
	}
	
	public void printCurrentPerceptSequence() {
		
		System.out.println("Percept: <bump, glitter, breeze, stench, scream>");
		
		try {
		
			System.out.print("Percept: <");	
			//outputWriter.write("Percept: <");
			
			if (transferPercept.getBump() == true) {
				System.out.print(" bump,");
				//outputWriter.write("bump,");
			}
			else if (transferPercept.getBump() == false) {
				System.out.print(" none,");
				//outputWriter.write("none,");
			}
			if (transferPercept.getGlitter() == true) {
				System.out.print(" glitter,");
				//outputWriter.write("glitter,");
			}
			else if (transferPercept.getGlitter() == false) {
				System.out.print(" none,");
				//outputWriter.write("none,");
			}
			if (transferPercept.getBreeze() == true) {
				System.out.print(" breeze,");
				//outputWriter.write("breeze,");
			}
			else if (transferPercept.getBreeze() == false) {
				System.out.print(" none,");
				//outputWriter.write("none,");
			}
			if (transferPercept.getStench() == true) {
				System.out.print(" stench,");
				//outputWriter.write("stench,");
			}
			else if (transferPercept.getStench() == false) {
				System.out.print(" none,");
				//outputWriter.write("none,");
			}
			if (transferPercept.getScream() == true) {
				System.out.print(" scream>\n");
				//outputWriter.write("scream>\n");
			}
			else if (transferPercept.getScream() == false) {
				System.out.print(" none>\n");
				//outputWriter.write("none>\n");
			}
		
		}
		catch (Exception e) {
			System.out.println("An exception was thrown: " + e);
		}
		
	}
	
	public void handleAction(int action) {
		
		try {
		
			if (action == Action.GO_FORWARD) {
				
				if (environment.getBump() == true) environment.setBump(false);
				
				agent.goForward();
				environment.placeAgent(agent);
				
				if (environment.checkDeath() == true) {
					
					currScore += deathCost;
					simulationRunning = false;
					
					agent.setIsDead(true);
				}
				else {
					currScore += actionCost;
				}
				
				if (environment.getScream() == true) environment.setScream(false);
				
				lastAction = Action.GO_FORWARD;
			}
			else if (action == Action.TURN_RIGHT) {
				
				currScore += actionCost;
				agent.turnRight();		
				environment.placeAgent(agent);
				
				if (environment.getBump() == true) environment.setBump(false);
				if (environment.getScream() == true) environment.setScream(false);
				
				lastAction = Action.TURN_RIGHT;
			}
			else if (action == Action.TURN_LEFT) {
				
				currScore += actionCost;
				agent.turnLeft();		
				environment.placeAgent(agent);
				
				if (environment.getBump() == true) environment.setBump(false);
				if (environment.getScream() == true) environment.setScream(false);
				
				lastAction = Action.TURN_LEFT;
			}
			else if (action == Action.GRAB) {
				
				if (environment.grabGold() == true) {
					
					currScore += 1000;
					simulationRunning = false;
					
					agent.setHasGold(true);
				}
				else currScore += actionCost;
				
				environment.placeAgent(agent);
				
				if (environment.getBump() == true) environment.setBump(false);
				if (environment.getScream() == true) environment.setScream(false);
				
				lastAction = Action.GRAB;
			}
			else if (action == Action.SHOOT) {
				
				if (agent.shootArrow() == true) {
					
					if (environment.shootArrow() == true) environment.setScream(true);
				
					currScore += shootCost;					
				}
				else {
					
					if (environment.getScream() == true) environment.setScream(false);
					
					currScore += actionCost;
				}
				
				environment.placeAgent(agent);
				
				if (environment.getBump() == true) environment.setBump(false);
				
				lastAction = Action.SHOOT;
			}
			else if (action == Action.NO_OP) {
				
				environment.placeAgent(agent);
				
				if (environment.getBump() == true) environment.setBump(false);
				if (environment.getScream() == true) environment.setScream(false);
				
				lastAction = Action.NO_OP;
			}
			
		}
		catch (Exception e) {
			System.out.println("An exception was thrown: " + e);
		}
	}
	
	public int getScore() {		
		return currScore;		
	}
	
}//class Simulation