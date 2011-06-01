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
 * LogPanel.java
 *
 * Created on 12/07/2009, 09:42:13
 */
package edu.memphis.ccrg.lida.framework.gui.panels;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollBar;

import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * A {@link GuiPanel} to display the logs of the system.
 * 
 * Panel is initialized by loading all relevant Loggers which includes all those
 * in the framework (edu.memphis.ccrg.lida.*) and optionally those specified by
 * the first parameter.
 * 
 * @author Javier Snaider
 */
public class LoggingPanel extends GuiPanelImpl {

	private String logName = "edu.memphis.ccrg.lida";
	private Logger logger = Logger.getLogger(logName);

	/** Creates new form LogPanel */
	public LoggingPanel() {
		initComponents();
	}

	/** 
	 * 
	 */
	@Override
	public void initPanel(String[] params) {
		GuiLogHandler handler = new GuiLogHandler();
		logger.addHandler(handler);
		String otherName = null;
		if (params != null && params.length > 0 && params[0] != null) {
			otherName = params[0].trim();
			Logger otherLogger = Logger.getLogger(otherName);
			otherLogger.addHandler(handler);
		}
		Enumeration<String> el = LogManager.getLogManager().getLoggerNames();
		loggerComboBox.removeAllItems();
		loggerComboBox.addItem("GLOBAL");
		List<String> names = new ArrayList<String>();
		while (el.hasMoreElements()) {
			String logN = el.nextElement();
			if (logN.regionMatches(0, logName, 0, logName.length())) {
				names.add(logN);
			} else if (otherName != null
					&& (logN.regionMatches(0, otherName, 0, otherName.length()))) {
				names.add(logN);
			}
		}
		Collections.sort(names);
		for (String n : names) {
			loggerComboBox.addItem(n);
		}
		refresh();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		loggingText = new javax.swing.JTextArea();
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		clearButton = new javax.swing.JButton();
		levelComboBox = new javax.swing.JComboBox();
		jLabel2 = new javax.swing.JLabel();
		loggerComboBox = new javax.swing.JComboBox();

		setLayout(new java.awt.BorderLayout());

		loggingText.setColumns(20);
		loggingText.setRows(5);
		jScrollPane1.setViewportView(loggingText);

		add(jScrollPane1, java.awt.BorderLayout.CENTER);

		jPanel1.setMinimumSize(new java.awt.Dimension(100, 60));

		jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 14));
		jLabel1.setText("Logging level");

		clearButton.setText("Clear log");
		clearButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				clearButtonActionPerformed(evt);
			}
		});

		levelComboBox.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "<Parent Level>", "SEVERE", "WARNING", "INFO",
						"CONFIG", "FINE", "FINER", "FINEST", "ALL", "OFF" }));
		levelComboBox.setSelectedItem("INFO");
		levelComboBox.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				levelComboBoxActionPerformed(evt);
			}
		});

		jLabel2.setFont(new java.awt.Font("Lucida Grande", 0, 14));
		jLabel2.setText("Logger:");

		loggerComboBox.setModel(new DefaultComboBoxModel());
		loggerComboBox.setSelectedItem("INFO");
		loggerComboBox.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loggerComboBoxActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(jLabel2)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												loggerComboBox,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												260,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jLabel1)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												levelComboBox,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												134,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE).addComponent(
												clearButton).addGap(123, 123,
												123)));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel1)
														.addComponent(
																clearButton)
														.addComponent(
																levelComboBox,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(jLabel2)
														.addComponent(
																loggerComboBox,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		add(jPanel1, java.awt.BorderLayout.PAGE_START);
	}// </editor-fold>//GEN-END:initComponents

	private void loggerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_loggerComboBoxActionPerformed

		String loggerN = loggerComboBox.getSelectedItem().toString();
		if (loggerN.equals("GLOBAL")) {
			loggerN = "";
		}

		String levelName;
		Level levelVal = Logger.getLogger(loggerN).getLevel();
		if (levelVal == null) {
			levelName = "<Parent Level>";
		} else {
			levelName = levelVal.toString();
		}

		levelComboBox.setSelectedItem(levelName);
	}// GEN-LAST:event_loggerComboBoxActionPerformed

	private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {
		loggingText.setText("");
	}

	private void levelComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
		Level levelVal = null;
		if (levelComboBox.getSelectedIndex() != 0) {
			levelVal = Level.parse(levelComboBox.getSelectedItem().toString());
		}
		String loggerN = loggerComboBox.getSelectedItem().toString();
		if (loggerN.equals("GLOBAL")) {
			loggerN = "";
		}

		Logger.getLogger(loggerN).setLevel(levelVal);
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton clearButton;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JComboBox levelComboBox;
	private javax.swing.JComboBox loggerComboBox;
	private javax.swing.JTextArea loggingText;

	// End of variables declaration//GEN-END:variables

	@Override
	public void display(Object o) {
		if (o instanceof String) {
			String s = (String) o;
			loggingText.append(s);
		}
	}

	/*
	 * Logger handler for this panel
	 *
	 */
	private class GuiLogHandler extends Handler {

		/** Creates a new instance of GuiLogHandler */
		public GuiLogHandler() {
		}

		@Override
		public void publish(LogRecord logRecord) {
			String logMessages = new String("");
			// String dateString="";
			long actualTick = 0L;
			// String name;

			String message = logRecord.getMessage();
			if (message == null) {
				logger.log(Level.WARNING, "in Logging Panel message was null",
						TaskManager.getCurrentTick());
			} else {
				MessageFormat mf = new MessageFormat(message);

				Object[] param = logRecord.getParameters();
				if (param != null && param[0] instanceof Long) {
					actualTick = (Long) param[0];
				}
				logMessages = String.format(
						"%010d :%010d :%-10s :%-60s \t-> %s %n", logRecord
								.getSequenceNumber(), actualTick, logRecord
								.getLevel(), logRecord.getLoggerName(), mf
								.format(logRecord.getParameters()));
				synchronized (loggingText) {
					loggingText.append(logMessages);
				}
				JScrollBar jsb = jScrollPane1.getVerticalScrollBar();
				jsb.setValue(jsb.getMaximum());
			}

		}

		@Override
		public void flush() {
		}

		@Override
		public void close() {
		}
	}// inner class

}