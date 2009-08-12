package edu.memphis.ccrg.lida.example.genericlida.environ_sm;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import edu.memphis.ccrg.lida.actionselection.ActionContent;
import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;

public class VisionEnvironment extends EnvironmentImpl implements GuiContentProvider {

	private Logger logger = Logger.getLogger("lida.example.genericlida.environsensorymem.VisionEnvironment");
	private boolean actionHasChanged = false;
	private ActionContent actionContent = null;
	private double[][] environContent = new double[1][1];
	private int IMAGE_HEIGHT = 5;
	private int IMAGE_WIDTH = 5;
	List<FrameworkGuiEventListener> frameworkGuis = new ArrayList<FrameworkGuiEventListener>();

	public VisionEnvironment(LidaTaskManager timer, int ticksPerStep, int height, int width) {
		super(timer, ticksPerStep);
		IMAGE_HEIGHT = height;
		IMAGE_WIDTH = width;
		iloc = IMAGE_HEIGHT / 2;
		jloc = IMAGE_WIDTH / 2;
	}

	public synchronized void receiveBehaviorContent(ActionContent action) {
		actionContent = action;
		actionHasChanged = true;
	}
	
	private int arrow = 0;
	private int counter = 0;

	public void runDriverOneProcessingStep() {
		Integer latestAction = null;

		if(arrow == 0)
			getNextMoveDown();
		else if(arrow == 1)
			getNextMoveUp();
		else if(arrow == 2)
			getNextMoveRight();
		else if(arrow == 3)
			getNextMoveLeft();
			
		counter++;
		if(counter == 3){
			counter = 0;
			if(Math.random() > 0.8)
				arrow = -1;
			else 
				arrow = (int) Math.floor(Math.random() * 4);
		}
		
		sendEvent();
		if (actionHasChanged) {
			latestAction = (Integer) actionContent.getContent();
			synchronized (this) {
				actionHasChanged = false;
			}
			if (!latestAction.equals(null)) {
				// handleAction(currentAction);
			}
		}// if actionHasChanged
	}


	/**
	 * @return the environContent
	 */
	public double[][] getEnvironContent() {
		return environContent;
	}

	/**
	 * @param environContent
	 *            the environContent to set
	 */
	public void setEnvironContent(double[][] environContent) {
		this.environContent = environContent;
	}

	public void resetEnvironment() {
		iloc = -1;
		jloc = -1;
		 double image[][] = new double[IMAGE_HEIGHT][IMAGE_WIDTH];
		// fillImageBlank(image);
		 environContent=image;
		 sendEvent();
		 logger.info("Environment Reseted");
	}// method

	// ************Specific methods**************
	private final double MIN_VALUE = 0.0;
	private final double MAX_VALUE = 1.0;
	private int iloc;
	private int jloc;
	private final int resetLocation = 0;

	public void getNextMoveRight() {
		if (jloc == IMAGE_WIDTH + 2)
			jloc = resetLocation;
		createFrame(iloc, jloc);
		jloc++;
	}// method

	public void getNextMoveLeft() {
		if (jloc == IMAGE_WIDTH + 2)
			jloc = resetLocation;
		createFrame(iloc, IMAGE_WIDTH - 1 - jloc);
		jloc++;
	}// method

	public void getNextMoveUp() {
		if (iloc == IMAGE_WIDTH + 2)
			iloc = resetLocation;
		createFrame(IMAGE_HEIGHT - 1 - iloc, jloc);
		iloc++;
	}// method

	public void getNextMoveDown() {
		if (iloc == IMAGE_WIDTH + 2)
			iloc = resetLocation;
		createFrame(iloc, jloc);
		iloc++;
	}// method

	public void createFrame(int i, int j) {
		double image[][] = new double[IMAGE_HEIGHT][IMAGE_WIDTH];
		fillImageBlank(image);
		addBlock(i, j, image);
		environContent = image;
		// environContent.setGuiContent(convertToString(image));
	}

	public void fillImageBlank(double[][] image) {
		for (int i = 0; i < IMAGE_HEIGHT; i++)
			for (int j = 0; j < IMAGE_WIDTH; j++)
				image[i][j] = MIN_VALUE;
	}// fillImageBlank

	public void addBlock(int row, int col, double[][] image) {
		if (row > IMAGE_HEIGHT || row < -1 || col > IMAGE_WIDTH || col < -1)
			return;

		int i = row - 1;
		if (i >= 0 && i < IMAGE_HEIGHT)
			addHoriz(i, col, image);

		i = row;
		if (i >= 0 && i < IMAGE_HEIGHT)
			addHoriz(i, col, image);

		i = row + 1;
		if (i >= 0 && i < IMAGE_HEIGHT)
			addHoriz(i, col, image);
	}// addBlock

	public void addHoriz(int i, int col, double[][] image) { // assumes that 0
		// <= i <
		// IMAGE_HEIGHT
		int j = col - 1;// left
		if (j >= 0 && j < IMAGE_WIDTH)
			image[i][j] = MAX_VALUE;

		j = col;// middle
		if (j >= 0 && j < IMAGE_WIDTH)
			image[i][j] = MAX_VALUE;

		j = col + 1;// right
		if (j >= 0 && j < IMAGE_WIDTH)
			image[i][j] = MAX_VALUE;
	}// addHoriz

	public String convertToString(double[][] a) {
		String res = "";

		for (int i = 0; i < IMAGE_HEIGHT; i++) {
			for (int j = 0; j < IMAGE_WIDTH; j++) {
				if (a[i][j] == 0.0)
					res += "_._ ";
				else
					res += a[i][j] + " ";
			}
			res += "\n";
		}
		return res;
	}// method

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		frameworkGuis.add(listener);
	}

	public void sendEvent() {
		for (FrameworkGuiEventListener fg : frameworkGuis) {
			FrameworkGuiEvent evt = new FrameworkGuiEvent(Module.environment,"data",environContent);
			fg.receiveGuiEvent(evt);
		}
	}

	// public void printImage(double[][] image){
	// for(int i = 0; i < IMAGE_HEIGHT; i++){
	// for(int j = 0; j < IMAGE_WIDTH; j++){
	// System.out.print((int)image[i][j] + " ");
	// }//for j
	// System.out.println();
	// }//for i
	// System.out.println();
	// }//printImage

}// class