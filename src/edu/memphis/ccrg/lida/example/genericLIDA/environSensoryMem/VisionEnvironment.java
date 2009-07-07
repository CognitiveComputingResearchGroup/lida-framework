package edu.memphis.ccrg.lida.example.genericLIDA.environSensoryMem;

import java.util.ArrayList;
import java.util.List;
import edu.memphis.ccrg.lida.actionSelection.ActionContent;
import edu.memphis.ccrg.lida.actionSelection.ActionSelectionListener;
import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.environment.EnvironmentListener;
import edu.memphis.ccrg.lida.framework.FrameworkModuleDriver;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;

public class VisionEnvironment implements Environment, FrameworkModuleDriver, ActionSelectionListener {

	private FrameworkTimer timer;
	private List<EnvironmentListener> listeners;
	private boolean keepRunning = true;	
	private boolean actionHasChanged = false;
	private ActionContent actionContent = null;
	private VisionEnvironmentContent environContent = new VisionEnvironmentContent();
	private final int IMAGE_HEIGHT;
	private final int IMAGE_WIDTH;
	
	public VisionEnvironment(FrameworkTimer timer, int height, int width) {
		this.timer = timer;
		IMAGE_HEIGHT = height;
		IMAGE_WIDTH = width;
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
			try{Thread.sleep(timer.getSleepTime());}catch(Exception e){}			
			timer.checkForStartPause();

			getNextMoveDown();
			getNextMoveRight();
	
			sendContent();
			
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
	
	public void sendContent(){
		for(int i = 0; i < listeners.size(); i++)
			(listeners.get(i)).receiveEnvironmentContent(environContent);
	}

	public void stopRunning() {
		keepRunning = false;
	}

	public void resetEnvironment() {
		iloc = -1;
		jloc = -1;
//		double image[][] = new double[IMAGE_HEIGHT][IMAGE_WIDTH];
//		fillImageBlank(image);	
//		environContent.setContent(image);
//		environContent.setGuiContent(convertToString(image));
//		sendContent();
	}//method
	
	//************Specific methods**************
	private final double MIN_VALUE = 0.0;
	private final double MAX_VALUE = 1.0;
	private int iloc = -1;
	private int jloc = -1;
	
	public void getNextMoveRight(){
		if(jloc == IMAGE_WIDTH + 2)
			jloc = -2;
		createFrame(iloc, jloc);
		jloc++;
	}//method
	
	public void getNextMoveLeft(){
		if(jloc == IMAGE_WIDTH + 2)
			jloc = -2;
		createFrame(iloc, IMAGE_WIDTH - 1 - jloc);
		jloc++;
	}//method
	
	public void getNextMoveUp(){
		if(iloc == IMAGE_WIDTH + 2)
			iloc = -2;
		createFrame(IMAGE_HEIGHT - 1 - iloc, jloc);
		iloc++;
	}//method
	
	public void getNextMoveDown(){
		if(iloc == IMAGE_WIDTH + 2)
			iloc = -2;
		createFrame(iloc, jloc);
		iloc++;
	}//method
	
	public void createFrame(int i, int j){
		double image[][] = new double[IMAGE_HEIGHT][IMAGE_WIDTH];
		fillImageBlank(image);
		addBlock(i, j, image);	
		environContent.setContent(image);
		environContent.setGuiContent(convertToString(image));		
	}
	
	public void fillImageBlank(double[][] image){
		 for(int i = 0; i < IMAGE_HEIGHT; i++)
			 for(int j = 0; j < IMAGE_WIDTH; j++)
				 image[i][j] = MIN_VALUE;		
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
			image[i][j] = MAX_VALUE;
		
		j = col;//middle
		if(j >= 0 && j < IMAGE_WIDTH)
			image[i][j] = MAX_VALUE;
		
		j = col + 1;//right
		if(j >= 0 && j < IMAGE_WIDTH)
			image[i][j] = MAX_VALUE;			
	}//addHoriz
	
	public String convertToString(double[][] a){
		String res = "";
		
		 for(int i = 0; i < IMAGE_HEIGHT; i++){
			 for(int j = 0; j < IMAGE_WIDTH; j++){
				 if(a[i][j] == 0.0)
					 res += "_._ ";
				 else
					 res += a[i][j] + " ";
			 }
			 res += "\n";
		 }
		return res;
	}//method

//	public void printImage(double[][] image){
//		for(int i = 0; i < IMAGE_HEIGHT; i++){
//			 for(int j = 0; j < IMAGE_WIDTH; j++){
//				 System.out.print((int)image[i][j] + " ");
//			 }//for j
//			System.out.println();
//		}//for i	
//		System.out.println();
//	}//printImage
		
}//class