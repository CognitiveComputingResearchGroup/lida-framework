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

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * 
 * @author Javier Snaider
 */
public class TaskManagerPanel extends LidaPanelImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3135377683820863184L;
	private static Logger logger = Logger.getLogger("lida.framework.gui.LidaTaskPanel");
	private Collection<LidaTask> tasks;
	private LidaTask[] taskArray;

	
	public TaskManagerPanel() {
		initComponents();
		tasks = new HashSet<LidaTask>();
		taskArray = tasks.toArray(new LidaTask[0]);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		tasksTable = new javax.swing.JTable();
		jToolBar1 = new javax.swing.JToolBar();
		ApplyButton = new javax.swing.JButton();

		tasksTable.setModel(new Task2TableModel());
		jScrollPane1.setViewportView(tasksTable);

		jToolBar1.setRollover(true);

		ApplyButton.setText("Refresh");
		ApplyButton.setFocusable(false);
		ApplyButton
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		ApplyButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		ApplyButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ApplyButtonActionPerformed(evt);
			}
		});
		jToolBar1.add(ApplyButton);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 400,
				Short.MAX_VALUE).addComponent(jScrollPane1,
				javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
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
												jScrollPane1,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												269, Short.MAX_VALUE)));
	}// </editor-fold>//GEN-END:initComponents

	private void ApplyButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN
		refresh();
	}// GEN-LAST:event_ApplyButtonActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton ApplyButton;
	private javax.swing.JTable tasksTable;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JToolBar jToolBar1;
	// End of variables declaration//GEN-END:variables

	private class Task2TableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		private int columnCnt=5;
		public int getColumnCount() {
			return columnCnt;
		}

		public int getRowCount() {
			return taskArray.length;
		}

		public String getColumnName(int column) {
			String cName = "";
			switch (column) {
			case 0:
				cName = "Name";
				break;
			case 1:
				cName = "Task ID";
				
				break;
			case 2:
				cName = "Status";
				break;
			case 3:
				cName = "Spawned Task Count";	
				break;
			case 4:
				cName = "Scheduled Tick";	
				break;
			default:
				cName="col"+column;
			}
			return cName;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			LidaTask task= taskArray[rowIndex];
			Object o=null;
			switch (columnIndex) {
			case 0:
				o = task;
				break;
			case 1:
				o= task.getTaskId();
				break;
			case 2:
				o=task.getStatus();
				break;
			case 3:
				//o = ((TaskSpawnerImpl) task).getSpawnedTaskCount();
				break;
			case 4:
				o=task.getScheduledTick();
				break;
			default:
				o="";
			}
			return o;
		}

		public void setValueAt(Object value, int row, int column) {
		}

		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}

	public void refresh(){
		logger.log(Level.FINEST, "Refreshing TaskManager Panel",LidaTaskManager.getActualTick());

		display(lida.getTaskManager().getSpawnedTasks());
	}
	@SuppressWarnings("unchecked")
	public void display(Object o) {
		if (o instanceof Collection) {
			tasks = (Collection<LidaTask>) o;
			
			//Concurrent Modification Exception here. 9/15/09	
			//Iterating over a shared Collection during the call to 'toArray'
			taskArray = tasks.toArray(new LidaTask[0]);
			((AbstractTableModel)tasksTable.getModel()).fireTableDataChanged();
		}
	}//method

}//class
