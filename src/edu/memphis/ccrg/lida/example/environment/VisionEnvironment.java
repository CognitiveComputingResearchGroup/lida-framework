package edu.memphis.ccrg.lida.example.environment;

import java.util.ArrayList;
import java.util.List;
import edu.memphis.ccrg.lida.actionSelection.ActionContent;
import edu.memphis.ccrg.lida.actionSelection.ActionSelectionListener;
import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.environment.EnvironmentListener;
import edu.memphis.ccrg.lida.environment.TestEnvironmentContent;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;

public class VisionEnvironment implements Environment, Runnable, Stoppable, ActionSelectionListener {

	private FrameworkTimer timer;
	private List<EnvironmentListener> listeners;
	private boolean keepRunning = true;	
	private boolean actionHasChanged = false;
	private ActionContent actionContent = null;
	private TestEnvironmentContent environContent = new TestEnvironmentContent();
	
	public VisionEnvironment(FrameworkTimer timer) {
		this.timer = timer;
		listeners = new ArrayList<EnvironmentListener>();
	}
	
	public void addEnvironmentListener(EnvironmentListener listener) {
		listeners.add(listener);		
	}
	
	public synchronized void receiveBehaviorContent(ActionContent action){
		actionContent = action;		
		actionHasChanged = true;
	}

	public void run() {
		Integer latestAction = 0;
		while(keepRunning){
			try{Thread.sleep(50);}catch(Exception e){}			
			timer.checkForStartPause();
			
			runSimOneStep();
	
			for(int i = 0; i < listeners.size(); i++)
				(listeners.get(i)).receiveEnvironmentContent(environContent);
			
			if(actionHasChanged){
				latestAction = (Integer)actionContent.getContent();
				synchronized(this){
					actionHasChanged = false;
				}
				if(!latestAction.equals(null)){
					//handleAction(currentAction);
				}
			}//if actionHasChanged	
			
		}//while
		
	}//method

	public void stopRunning() {
		keepRunning = false;
	}

	public void resetEnvironment() {
		// TODO Auto-generated method stub
		//reset environ
	}//method
	
	//**********************************

	private final int IMAGE_HEIGHT = 6;
	private final int IMAGE_WIDTH = 6;
	private final double MIN_VALUE = 0.0;
	private final double MAX_VALUE = 1.0;
	
	public void runSimOneStep(){
		getNextMoveRight();
		
	}

	private int iloc = 2;
	private int jloc = 2;
	
	public void getNextMoveRight(){
		if(jloc == IMAGE_WIDTH + 2)
			jloc = -2;
	
		environContent.setContent(createFrame(iloc, jloc));
		jloc++;
	}//method
	
	public void blockMovesRight(int i){
		for(int j = -2; j < IMAGE_WIDTH + 2; j++){
			createFrame(i, j);
		}		
	}
	
	public void blockMovesLeft(int i){
		for(int j = -2; j < IMAGE_WIDTH + 2; j++){
			createFrame(i, IMAGE_WIDTH - 1 - j);
		}		
	}
	
	public void blockMovesUp(int j){
		for(int i = -2; i < IMAGE_HEIGHT + 2; i++){
			createFrame(IMAGE_HEIGHT - 1 - i, j);
		}		
	}
	
	public void blockMovesDown(int j){
		for(int i = -2; i < IMAGE_HEIGHT + 2; i++){
			createFrame(i, j);
		}		
	}
	
	public double[][] createFrame(int i, int j){
		double image[][] = new double[IMAGE_HEIGHT][IMAGE_WIDTH];
		fillImageBlank(image);
		addBlock(i, j, image);	
		return image;
	}
	
	public void fillImageBlank(double[][] image){
		 for(int i = 0; i < IMAGE_HEIGHT; i++)
			 for(int j = 0; j < IMAGE_WIDTH; j++)
				 image[i][j] = MAX_VALUE;		
	}//fillImageBlank

	
	public void addBlock(int row, int col, double[][] image){
		if(row > IMAGE_HEIGHT || row < -1 || col > IMAGE_WIDTH || col < -1)
			return;
		
		int i = row - 1;
		if(i >= 0 && i < IMAGE_HEIGHT)
			addHoriz(i, col, image);
		
		i = row;		
		if(i >= 0 && i < IMAGE_HEIGHT)
			addHoriz(i, col, image);	
		
		i = row + 1;		
		if(i >= 0 && i < IMAGE_HEIGHT)
			addHoriz(i, col, image);			
	}//addBlock
	
	public void addHoriz(int i, int col, double[][] image){  //assumes that 0 <= i < IMAGE_HEIGHT
		int j = col - 1;//left
		if(j >= 0 && j < IMAGE_WIDTH)
			image[i][j] = MIN_VALUE;
		
		j = col;//middle
		if(j >= 0 && j < IMAGE_WIDTH)
			image[i][j] = MIN_VALUE;
		
		j = col + 1;//right
		if(j >= 0 && j < IMAGE_WIDTH)
			image[i][j] = MIN_VALUE;			
	}//addHoriz

	public void printImage(double[][] image){
		for(int i = 0; i < IMAGE_HEIGHT; i++){
			 for(int j = 0; j < IMAGE_WIDTH; j++){
				 System.out.print((int)image[i][j] + " ");
			 }//for j
			System.out.println();
		}//for i	
		System.out.println();
	}//printImage
		
}//class