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
 * PropertiesPanel.java 
 *
 * Created on 14/08/2009, 13:37:17
 */
package edu.memphis.ccrg.lida.framework.gui.panels;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.gui.utils.GuiUtils;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * A {@link GuiPanel} which displays the {@link FrameworkTask}s which are
 * controlled by the {@link TaskSpawner} associated with a particular
 * {@link FrameworkModule}.
 * 
 * @author Javier Snaider
 */
public class FrameworkTaskPanel extends GuiPanelImpl {

	private static final Logger logger = Logger
			.getLogger(FrameworkTaskPanel.class.getCanonicalName());
	private Collection<FrameworkTask> tasks;
	private FrameworkTask[] taskArray;
	private FrameworkModule module;
	private TaskTableModel taskTableModel;

	/**
	 * creates new {@link FrameworkTaskPanel}
	 */
	public FrameworkTaskPanel() {
		taskTableModel = new TaskTableModel();
		tasks = new HashSet<FrameworkTask>();
		taskArray = tasks.toArray(new FrameworkTask[0]);
		initComponents();
	}

	@Override
	public void initPanel(String[] param) {
		if (param == null || param.length == 0) {
			logger
					.log(
							Level.WARNING,
							"Error initializing FrameworkTaskPanel, not enough parameters.",
							0L);
			return;
		}

		module = GuiUtils.parseFrameworkModule(param[0], agent);
		if (module != null) {
			refresh();
		} else {
			logger.log(Level.WARNING,
					"Unable to parse module {1}. Panel not initialized.",
					new Object[] { 0L, param[0] });
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jToolBar1 = new javax.swing.JToolBar();
		refreshButton = new javax.swing.JButton();
		threadPane = new javax.swing.JScrollPane();
		tasksTable = new javax.swing.JTable();

		jToolBar1.setRollover(true);

		refreshButton.setText("Refresh");
		refreshButton.setFocusable(false);
		refreshButton
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		refreshButton
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		refreshButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				refreshButtonActionPerformed(evt);
			}
		});
		jToolBar1.add(refreshButton);

		tasksTable.setModel(taskTableModel);
		tasksTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		tasksTable.setMaximumSize(new java.awt.Dimension(1000, 1000));
		threadPane.setViewportView(tasksTable);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addComponent(jToolBar1,
						javax.swing.GroupLayout.DEFAULT_SIZE, 390,
						Short.MAX_VALUE).addContainerGap()).addComponent(
				threadPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400,
				Short.MAX_VALUE));
		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addComponent(
												jToolBar1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												25,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												threadPane,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												215, Short.MAX_VALUE)));
	}// </editor-fold>//GEN-END:initComponents

	private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
		refresh();
	}// GEN-LAST:event_ApplyButtonActionPerformed

	@Override
	public void refresh() {
		if (module != null) {
			display(module.getAssistingTaskSpawner().getTasks());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void display(Object o) {
		logger.log(Level.FINEST, "Refreshing display", TaskManager
				.getCurrentTick());
		if (o instanceof Collection) {
			tasks = (Collection<FrameworkTask>) o;

			taskArray = tasks.toArray(new FrameworkTask[0]);

			((AbstractTableModel) tasksTable.getModel()).fireTableDataChanged();
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JToolBar jToolBar1;
	private javax.swing.JButton refreshButton;
	private javax.swing.JTable tasksTable;
	private javax.swing.JScrollPane threadPane;

	// End of variables declaration//GEN-END:variables

	/*
	 * This TaskTableModel adapts the collection of FrameworkTasks to an
	 * AbstractTableModel
	 */
	private class TaskTableModel extends AbstractTableModel {

		private String[] columnNames = { "Task ID", "Activation", "Status",
				"Description", "Next scheduled tick" };
		private int[] columnAlign = { SwingConstants.RIGHT,
				SwingConstants.RIGHT, SwingConstants.LEFT, SwingConstants.LEFT,
				SwingConstants.RIGHT };
		private DecimalFormat df = new DecimalFormat("0.0000");
		private Map<String, Integer> columnAlignmentMap = new HashMap<String, Integer>();

		public TaskTableModel() {
			for (int i = 0; i < columnNames.length; i++) {
				columnAlignmentMap.put(columnNames[i], columnAlign[i]);
			}
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public int getRowCount() {
			return taskArray.length;
		}

		@Override
		public String getColumnName(int column) {
			String cName = "";
			if (column < columnNames.length) {
				cName = columnNames[column];
			}
			return cName;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			FrameworkTask task = taskArray[rowIndex];
			Object o = null;
			switch (columnIndex) {
			case 0:
				o = task.getTaskId();
				break;
			case 1:
				o = df.format(task.getActivation());
				break;
			case 2:
				o = task.getTaskStatus();
				break;
			case 3:
				o = task;
				break;
			case 4:
				o = task.getScheduledTick();
				break;
			case 5:
				o = task.getNextTicksPerRun();
				break;
			default:
				o = "";
			}
			return o;
		}

		@Override
		public void setValueAt(Object value, int row, int column) {
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		/**
		 * @return the columnAlignmentMap
		 */
		public Map<String, Integer> getColumnAlignmentMap() {
			return columnAlignmentMap;
		}
	}

	@SuppressWarnings("unused")
	private class AlignedColumnTableModel extends DefaultTableColumnModel {

		private DefaultTableCellRenderer render;

		public AlignedColumnTableModel() {
			render = new DefaultTableCellRenderer();
			render.setHorizontalAlignment(SwingConstants.RIGHT);
		}

		@Override
		public void addColumn(TableColumn column) {
			if (taskTableModel.getColumnAlignmentMap().get(
					column.getHeaderValue().toString()) == SwingConstants.RIGHT) {
				column.setCellRenderer(render);
			}
			super.addColumn(column);
		}
	}
}
