package edu.memphis.ccrg.lida.example.genericlida.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.gui.panels.VisualEnvironmentPanel;

public class VisionEnvironment extends EnvironmentImpl implements GuiEventProvider {

	private Logger logger = Logger.getLogger("lida.example.genericlida.environsensorymem.VisionEnvironment");
	private boolean actionHasChanged = false;
	private LidaAction actionContent = null;
	private int imageHeight = 5;
	private int imageWidth = 5;
	private volatile double[][] environContent = new double[imageHeight][imageWidth];
	//
	FrameworkGuiEvent contentEvent = new FrameworkGuiEvent(ModuleName.Environment, "matrix", environContent);

	public VisionEnvironment(int height, int width) {
		super();
		imageHeight = height;
		imageWidth = width;
		iloc = imageHeight / 2;
		jloc = imageWidth / 2;
		environContent = new double[imageHeight][imageWidth];
	}
	public VisionEnvironment() {
		super();
		iloc = imageHeight / 2;
		jloc = imageWidth / 2;
		environContent = new double[imageHeight][imageWidth];
	}

	public void init(Properties lidaProperties) {
		imageHeight = Integer.parseInt(lidaProperties.getProperty("height"));
		imageWidth = Integer.parseInt(lidaProperties.getProperty("width"));
		iloc = imageHeight / 2;
		jloc = imageWidth / 2;
		environContent = new double[imageHeight][imageWidth];
	}

	public synchronized void receiveAction(LidaAction action) {
		actionContent = action;
		actionHasChanged = true;
	}
	
	private int arrow = 0;
	private int counter = 0;

	public void runThisDriver() {
		Integer latestAction = null;

		if(arrow == 0)
			getNextMoveDown();
		else if(arrow == 1)
			getNextMoveUp();
		else if(arrow == 2)
			getNextMoveRight();
		else if(arrow == 3)
			getNextMoveLeft();
			
		sendContentEvent(contentEvent);
		
		counter++;
		if(counter == 3){
			counter = 0;
			if(Math.random() > 0.8)
				arrow = -1;
			else 
				arrow = (int) Math.floor(Math.random() * 4);
		}
		
		if (actionHasChanged) {
			latestAction = (Integer) actionContent.getContent();
			synchronized (this) {
				actionHasChanged = false;
			}
			if (!latestAction.equals(null)) {
				// handleAction(currentAction);
			}
		}// if actionHasChanged
	}//run one step

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
		environContent = new double[imageHeight][imageWidth];
		 fillImageBlank(environContent);
		 contentEvent.setData(environContent);
		 sendContentEvent(contentEvent);
		 logger.log(Level.FINE, "Environment Reseted",0L);
	}// method

	// ************Specific methods**************
	private final double MIN_VALUE = 0.0;
	private final double MAX_VALUE = 1.0;
	private int iloc;
	private int jloc;
	private final int resetLocation = 0;

	public void getNextMoveRight() {
		if (jloc == imageWidth + 2)
			jloc = resetLocation;
		createFrame(iloc, jloc);
		jloc++;
	}// method

	public void getNextMoveLeft() {
		if (jloc == imageWidth + 2)
			jloc = resetLocation;
		createFrame(iloc, imageWidth - 1 - jloc);
		jloc++;
	}// method

	public void getNextMoveUp() {
		if (iloc == imageWidth + 2)
			iloc = resetLocation;
		createFrame(imageHeight - 1 - iloc, jloc);
		iloc++;
	}// method

	public void getNextMoveDown() {
		if (iloc == imageWidth + 2)
			iloc = resetLocation;
		createFrame(iloc, jloc);
		iloc++;
	}// method

	public void createFrame(int i, int j) {
		environContent = new double[imageHeight][imageWidth];
		fillImageBlank(environContent);
		addBlock(i, j, environContent);
		contentEvent.setData(environContent);
		// environContent.setGuiContent(convertToString(image));
	}

	public void fillImageBlank(double[][] image) {
		for (int i = 0; i < imageHeight; i++)
			for (int j = 0; j < imageWidth; j++)
				image[i][j] = MIN_VALUE;
	}// fillImageBlank

	public void addBlock(int row, int col, double[][] image) {
		if (row > imageHeight || row < -1 || col > imageWidth || col < -1)
			return;

		int i = row - 1;
		if (i >= 0 && i < imageHeight)
			addHoriz(i, col, image);

		i = row;
		if (i >= 0 && i < imageHeight)
			addHoriz(i, col, image);

		i = row + 1;
		if (i >= 0 && i < imageHeight)
			addHoriz(i, col, image);
	}// addBlock

	public void addHoriz(int i, int col, double[][] image) { // assumes that 0
		// <= i <
		// imageHeight
		int j = col - 1;// left
		if (j >= 0 && j < imageWidth)
			image[i][j] = MAX_VALUE;

		j = col;// middle
		if (j >= 0 && j < imageWidth)
			image[i][j] = MAX_VALUE;

		j = col + 1;// right
		if (j >= 0 && j < imageWidth)
			image[i][j] = MAX_VALUE;
	}// addHoriz

	public String convertToString(double[][] a) {
		String res = "";

		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				if (a[i][j] == 0.0)
					res += "_._ ";
				else
					res += a[i][j] + " ";
			}
			res += "\n";
		}
		return res;
	}// method
	
	List<FrameworkGuiEventListener> frameworkGuis = new ArrayList<FrameworkGuiEventListener>();

	private void sendContentEvent(FrameworkGuiEvent evt) {
		for(FrameworkGuiEventListener l: frameworkGuis)
			if(l instanceof VisualEnvironmentPanel)
				l.receiveGuiEvent(evt);
	}
	
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		frameworkGuis.add(listener);
	}
	public void sendEvent(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener fg : frameworkGuis)
			fg.receiveGuiEvent(evt);
	}

	@Override
	public String toString() {
		return "VisionEnvironment";
	}

	public Object getModuleContent() {
		return environContent;
	}

	public void addModule(LidaModule lm) {
	}

}// class