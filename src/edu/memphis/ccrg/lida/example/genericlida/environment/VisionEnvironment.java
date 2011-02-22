/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.example.genericlida.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.gui.panels.VisualEnvironmentPanel;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;

public class VisionEnvironment extends EnvironmentImpl implements GuiEventProvider {

	private Logger logger = Logger.getLogger(VisionEnvironment.class.getCanonicalName());
	private int imageHeight = 5;
	private int imageWidth = 5;
	private double[][] environContent = new double[imageHeight][imageWidth];
	//
	private FrameworkGuiEvent contentEvent = new FrameworkGuiEvent(ModuleName.Environment, "matrix", environContent);

	public VisionEnvironment(int height, int width) {
		imageHeight = height;
		imageWidth = width;
		iloc = imageHeight / 2;
		jloc = imageWidth / 2;
		environContent = new double[imageHeight][imageWidth];
	}
	public VisionEnvironment() {
		iloc = imageHeight / 2;
		jloc = imageWidth / 2;
		environContent = new double[imageHeight][imageWidth];
	}

	@Override
	public synchronized void processAction(Object action) {
		//TODO 
	}
	
	private int arrow = 0;
	private int counter = 0;


	@Override
	public void resetState() {
		iloc = -1;
		jloc = -1;
		environContent = new double[imageHeight][imageWidth];
		 fillImageBlank(environContent);
		 contentEvent.setContent(environContent);
		 sendContentEvent(contentEvent);
		 logger.log(Level.FINE, "Environment Reseted",0L);
	}

	// ************Specific methods**************
	private final double MIN_VALUE = 0.0;
	private final double MAX_VALUE = 1.0;
	private int iloc;
	private int jloc;
	private final int resetLocation = 0;

	private void getNextMoveRight() {
		if (jloc == imageWidth + 2)
			jloc = resetLocation;
		createFrame(iloc, jloc);
		jloc++;
	}

	private void getNextMoveLeft() {
		if (jloc == imageWidth + 2)
			jloc = resetLocation;
		createFrame(iloc, imageWidth - 1 - jloc);
		jloc++;
	}

	private void getNextMoveUp() {
		if (iloc == imageWidth + 2)
			iloc = resetLocation;
		createFrame(imageHeight - 1 - iloc, jloc);
		iloc++;
	}

	private void getNextMoveDown() {
		if (iloc == imageWidth + 2)
			iloc = resetLocation;
		createFrame(iloc, jloc);
		iloc++;
	}

	private void createFrame(int i, int j) {
		environContent = new double[imageHeight][imageWidth];
		fillImageBlank(environContent);
		addBlock(i, j, environContent);
		contentEvent.setContent(environContent);
		// environContent.setGuiContent(convertToString(image));
	}

	private void fillImageBlank(double[][] image) {
		for (int i = 0; i < imageHeight; i++)
			for (int j = 0; j < imageWidth; j++)
				image[i][j] = MIN_VALUE;
	}// fillImageBlank

	private void addBlock(int row, int col, double[][] image) {
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

	private void addHoriz(int i, int col, double[][] image) { // assumes that 0
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
	}
	
	private List<FrameworkGuiEventListener> frameworkGuis = new ArrayList<FrameworkGuiEventListener>();

	private void sendContentEvent(FrameworkGuiEvent evt) {
		for(FrameworkGuiEventListener l: frameworkGuis)
			if(l instanceof VisualEnvironmentPanel)
				l.receiveFrameworkGuiEvent(evt);
	}
	
	@Override
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		frameworkGuis.add(listener);
	}
	@Override
	public void sendEventToGui(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener fg : frameworkGuis)
			fg.receiveFrameworkGuiEvent(evt);
	}

	@Override
	public String toString() {
		return "VisionEnvironment";
	}

	@Override
	public Object getModuleContent(Object... params) {
		return environContent;
	}

	@Override
	public void addSubModule(LidaModule lm) {
	}
	
	@Override
	public void addListener(ModuleListener listener) {
	}

	@Override
	public void init(){
		imageHeight = (Integer)getParam("height",10);
		imageWidth = (Integer)getParam("width",10);
		iloc = imageHeight / 2;
		jloc = imageWidth / 2;
		environContent = new double[imageHeight][imageWidth];
		
		taskSpawner.addTask(new BackgroundTask());
	}

	private class BackgroundTask extends LidaTaskImpl {

		public BackgroundTask() {
			super();
			// TODO: make a parameter
			setTicksPerStep(1);
		}

		@Override
		protected void runThisLidaTask() {

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
		}
	}//background task

	@Override
	public Object getState(Map<String, ?> params) {
		// TODO Auto-generated method stub
		return null;
	}
	
}