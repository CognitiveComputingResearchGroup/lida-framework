/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 *  which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ControlToolBarPanel.java
 *
 * Created on 13/09/2009, 10:12:30
 */

package edu.memphis.ccrg.lida.framework.gui.panels;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JSlider;

import edu.memphis.ccrg.lida.framework.gui.FrameworkGui;
import edu.memphis.ccrg.lida.framework.gui.commands.Command;
import edu.memphis.ccrg.lida.framework.gui.commands.SetTimeScaleCommand;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;

/**
 * Implements the tool bar of the {@link FrameworkGui}. 
 * Can receive parameters for the tick slider min and max values.
 * @author Javier Snaider
 */
public class ControlToolBarPanel extends GuiPanelImpl implements FrameworkGuiEventListener {

	private static final Logger logger = Logger
			.getLogger(ControlToolBarPanel.class.getCanonicalName());
	private boolean isPaused = true;
	private int sliderMin = 1;
	private int sliderMax = 1000;
	private int sliderStartValue = sliderMax - (sliderMax - sliderMin) / 2;

	/** Creates new form ControlToolBarPanel */
	public ControlToolBarPanel() {
		initComponents();
	}

	@Override
	public void initPanel(String[] params) {
		sliderStartValue = agent.getTaskManager().getTickDuration();
		if (params.length >= 1) {
			try {
				sliderMin = Integer.parseInt(params[0]);
				sliderMin = (sliderMin < sliderStartValue)? sliderMin : sliderStartValue;
			} catch (NumberFormatException e) {
				logger.log(Level.WARNING, "Invalid Parameter {1}", new Object[]{0L,params[0]});
			}
		}
		if (params.length >= 2) {
			try {
				sliderMax = Integer.parseInt(params[1]);
				sliderMax = (sliderMax > sliderStartValue)? sliderMax : sliderStartValue;
			} catch (NumberFormatException e) {
				logger.log(Level.WARNING, "Invalid Parameter {1}", new Object[]{0L,params[1]});
			}
		}

		speedSlider.setMaximum(sliderMax);
		speedSlider.setMinimum(sliderMin);
		speedSlider.setValue(sliderStartValue);
		sleepTimeTextField.setText(sliderStartValue + "");
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolbar = new javax.swing.JToolBar();
        startPauseButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        ticksModeTB = new javax.swing.JToggleButton();
        addTicksButton = new javax.swing.JButton();
        tiksTB = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        speedSlider = new javax.swing.JSlider();
        sleepTimeTextField = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        toolbar.setRollover(true);
        toolbar.setPreferredSize(new java.awt.Dimension(50, 25));

        startPauseButton.setFont(new java.awt.Font("Lucida Grande", 0, 12));
        startPauseButton.setText("Start/Pause");
        startPauseButton.setFocusable(false);
        startPauseButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        startPauseButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        startPauseButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                startPauseButtonActionPerformed(evt);
            }
        });
        toolbar.add(startPauseButton);

        statusLabel.setFont(new java.awt.Font("Lucida Grande", 1, 12));
        statusLabel.setText("Paused");
        toolbar.add(statusLabel);
        toolbar.add(jSeparator2);

        ticksModeTB.setFont(new java.awt.Font("Lucida Grande", 0, 12));
        ticksModeTB.setText("Ticks mode");
        ticksModeTB.setFocusable(false);
        ticksModeTB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ticksModeTB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ticksModeTB.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                ticksModeTBActionPerformed(evt);
            }
        });
        toolbar.add(ticksModeTB);

        addTicksButton.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        addTicksButton.setText("Add ticks");
        addTicksButton.setFocusable(false);
        addTicksButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addTicksButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addTicksButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTicksButtonActionPerformed(evt);
            }
        });
        toolbar.add(addTicksButton);

        tiksTB.setText("0");
        toolbar.add(tiksTB);
        toolbar.add(jSeparator1);

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 0, 12));
        jLabel2.setText("Ticks Scale (ms)");
        toolbar.add(jLabel2);

        speedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                speedSliderStateChanged(evt);
            }
        });
        toolbar.add(speedSlider);

        sleepTimeTextField.setText("--");
        toolbar.add(sleepTimeTextField);

        add(toolbar, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

	/*
	 * Sends pauseRunningThreads and resumeRunningThreads commands
	 * @param evt
	 */
	private void startPauseButtonActionPerformed(java.awt.event.ActionEvent evt) {
		isPaused = !isPaused;
		if (isPaused) {
			statusLabel.setText("PAUSED");
			controller.executeCommand("pauseRunningThreads", null);
		} else {
			statusLabel.setText("RUNNING");
			controller.executeCommand("resumeRunningThreads", null);
		}
		refresh();
	}

	/*
	 * Changes TaskManager's tick Duration using SetTimeScaleCommand
	 * @param evt
	 */
	private void speedSliderStateChanged(javax.swing.event.ChangeEvent evt) {
		JSlider source = (JSlider) evt.getSource();
		if (!source.getValueIsAdjusting()) {
			int sleepTime = source.getValue();
			sleepTimeTextField.setText(sleepTime + "");
			// Another way to execute commands
			Command command = new SetTimeScaleCommand();
			command.setParameter("tickDuration", sleepTime);
			controller.executeCommand(command);
			refresh();
		}
	}

	/*
	 * Adds ticks for execution during ticks mode. using AddTicksCommand
	 * @param evt
	 */
	private void addTicksButtonActionPerformed(java.awt.event.ActionEvent evt) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		int ticks;
		try {
			ticks = Integer.parseInt(tiksTB.getText());
		} catch (NumberFormatException e) {
			ticks = 0;
		}
		parameters.put("ticks", ticks);
		controller.executeCommand("AddTicks", parameters);
	}

	/* 
	 * Toggles the TaskManager's ticks mode using the EnableTicksMode command. 
	 * @param evt
	 */
	private void ticksModeTBActionPerformed(java.awt.event.ActionEvent evt) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("enable", ticksModeTB.isSelected());
		controller.executeCommand("EnableTicksMode", parameters);
	}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addTicksButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JTextField sleepTimeTextField;
    private javax.swing.JSlider speedSlider;
    private javax.swing.JButton startPauseButton;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JToggleButton ticksModeTB;
    private javax.swing.JTextField tiksTB;
    private javax.swing.JToolBar toolbar;
    // End of variables declaration//GEN-END:variables

	@Override
	public void refresh() {
		isPaused = agent.getTaskManager().isTasksPaused();
		if (isPaused){
			statusLabel.setText("PAUSED");
		}else{
			statusLabel.setText("RUNNING");
		}
	}

	@Override
	public void receiveFrameworkGuiEvent(FrameworkGuiEvent event) {
	}
}
