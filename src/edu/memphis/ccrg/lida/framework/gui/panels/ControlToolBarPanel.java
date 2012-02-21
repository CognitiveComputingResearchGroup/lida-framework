/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
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

import edu.memphis.ccrg.lida.framework.gui.FrameworkGui;
import edu.memphis.ccrg.lida.framework.gui.commands.Command;
import edu.memphis.ccrg.lida.framework.gui.commands.SetTimeScaleCommand;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * Implements the tool bar of the {@link FrameworkGui}. 
 * Can receive parameters for the tick slider min and max values.
 * @author Javier Snaider
 */
public class ControlToolBarPanel extends GuiPanelImpl {

    private boolean isPaused = true;
    private int tickDurationStartValue = 1;
    private TaskManager tm;
    private final String PAUSED_LABEL = " Paused ";
    private final String RUNNING_LABEL = " Running ";

    /** Creates new form ControlToolBarPanel */
    public ControlToolBarPanel() {
        initComponents();
    }

    @Override
    public void initPanel(String[] params) {
        tm = agent.getTaskManager();
        tickDurationStartValue = tm.getTickDuration();
        tickDurationSpinner.setValue(tickDurationStartValue);
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
        jLabel3 = new javax.swing.JLabel();
        currentTickTextField = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        ticksModeTB = new javax.swing.JToggleButton();
        tiksTB = new javax.swing.JTextField();
        addTicksButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        tickDurationSpinner = new javax.swing.JSpinner();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        toolbar.setRollover(true);
        toolbar.setPreferredSize(new java.awt.Dimension(50, 25));

        startPauseButton.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        startPauseButton.setText("Start / Pause ");
        startPauseButton.setToolTipText("Toggles system operation");
        startPauseButton.setFocusable(false);
        startPauseButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        startPauseButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        startPauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startPauseButtonActionPerformed(evt);
            }
        });
        toolbar.add(startPauseButton);

        statusLabel.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statusLabel.setText(" Paused ");
        statusLabel.setToolTipText("System run status");
        toolbar.add(statusLabel);
        toolbar.add(jSeparator2);

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel3.setText("  Current tick: ");
        toolbar.add(jLabel3);

        currentTickTextField.setEditable(false);
        currentTickTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        currentTickTextField.setToolTipText("Current tick");
        currentTickTextField.setMaximumSize(new java.awt.Dimension(100, 24));
        currentTickTextField.setMinimumSize(new java.awt.Dimension(70, 24));
        currentTickTextField.setPreferredSize(new java.awt.Dimension(70, 24));
        toolbar.add(currentTickTextField);
        toolbar.add(jSeparator3);

        ticksModeTB.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        ticksModeTB.setText("Step mode");
        ticksModeTB.setToolTipText("Toggles step-by-step mode");
        ticksModeTB.setFocusable(false);
        ticksModeTB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ticksModeTB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ticksModeTB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ticksModeTBActionPerformed(evt);
            }
        });
        toolbar.add(ticksModeTB);

        tiksTB.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tiksTB.setText("0");
        tiksTB.setToolTipText("Enter a number of ticks here.  The system will run this number of ticks when adjacent 'Add' button is pressed and the system is in step mode.");
        tiksTB.setMaximumSize(new java.awt.Dimension(100, 24));
        tiksTB.setMinimumSize(new java.awt.Dimension(70, 24));
        tiksTB.setPreferredSize(new java.awt.Dimension(60, 24));
        toolbar.add(tiksTB);

        addTicksButton.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        addTicksButton.setText("Run ticks");
        addTicksButton.setToolTipText("Runs system the number of ticks specified in adjacent text field.");
        addTicksButton.setFocusable(false);
        addTicksButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addTicksButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addTicksButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTicksButtonActionPerformed(evt);
            }
        });
        toolbar.add(addTicksButton);
        toolbar.add(jSeparator1);

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        jLabel2.setText("  Tick duration (ms): ");
        toolbar.add(jLabel2);

        tickDurationSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 0, 1000, 1));
        tickDurationSpinner.setToolTipText("The system's current tick duration in milliseconds.");
        tickDurationSpinner.setMaximumSize(new java.awt.Dimension(110, 24));
        tickDurationSpinner.setMinimumSize(new java.awt.Dimension(63, 24));
        tickDurationSpinner.setPreferredSize(new java.awt.Dimension(63, 24));
        tickDurationSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tickDurationSpinnerStateChanged(evt);
            }
        });
        toolbar.add(tickDurationSpinner);
        toolbar.add(jSeparator4);
        toolbar.add(jPanel1);

        add(toolbar, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void tickDurationSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tickDurationSpinnerStateChanged
        int tickDuration = (Integer) tickDurationSpinner.getValue();
        if (tickDuration >= 0) {
            // Another way to execute commands
            Command command = new SetTimeScaleCommand();
            command.setParameter("tickDuration", tickDuration);
            controller.executeCommand(command);
            refresh();
        }
    }//GEN-LAST:event_tickDurationSpinnerStateChanged

    /*
     * Sends pauseRunningThreads and resumeRunningThreads commands
     * @param evt
     */
    private void startPauseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        isPaused = !isPaused;
        if (isPaused) {
            statusLabel.setText(PAUSED_LABEL);
            controller.executeCommand("pauseRunningThreads", null);
        } else {
            statusLabel.setText(RUNNING_LABEL);
            controller.executeCommand("resumeRunningThreads", null);
        }
        refresh();
    }

    /*
     * Adds ticks for execution during ticks mode. using AddTicksCommand
     * @param evt
     */
    private void addTicksButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (ticksModeTB.isSelected()) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            int ticks;
            try {
                ticks = Integer.parseInt(tiksTB.getText());
            } catch (NumberFormatException e) {
                ticks = 0;
            }
            parameters.put("ticks", ticks);
            controller.executeCommand("AddTicks", parameters);

            if (isPaused) {
                isPaused = !isPaused;
                statusLabel.setText(RUNNING_LABEL);
                controller.executeCommand("resumeRunningThreads", null);
            }
        }
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
    private javax.swing.JTextField currentTickTextField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JButton startPauseButton;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JSpinner tickDurationSpinner;
    private javax.swing.JToggleButton ticksModeTB;
    private javax.swing.JTextField tiksTB;
    private javax.swing.JToolBar toolbar;
    // End of variables declaration//GEN-END:variables

    @Override
    public void refresh() {
        if (isPaused) {
            statusLabel.setText(PAUSED_LABEL);
        } else {
            statusLabel.setText(RUNNING_LABEL);
        }
        currentTickTextField.setText(TaskManager.getCurrentTick() + "");
    }
}
