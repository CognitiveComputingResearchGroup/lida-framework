package edu.memphis.ccrg.lida.framework.gui.panels;
	
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.table.AbstractTableModel;
import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;

public class TaskManagerPanel extends LidaPanelImpl implements FrameworkGuiEventListener {

	private static final long serialVersionUID = -9220606154051976645L;
	Map<Module, Integer> taskMap = new HashMap<Module, Integer>();

	/** Creates new form ThreadPanel */
    public TaskManagerPanel() {
	    initComponents();
	}

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

	        jToolBar1 = new javax.swing.JToolBar();
	        refreshButton = new javax.swing.JButton();
	        threadPane = new javax.swing.JScrollPane();
	        PropertiesTable = new javax.swing.JTable();

	        jToolBar1.setRollover(true);

	        refreshButton.setText("Refresh");
	        refreshButton.setFocusable(false);
	        refreshButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
	        refreshButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
	        refreshButton.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                refreshButtonActionPerformed(evt);
	            }
	        });
	        jToolBar1.add(refreshButton);

	        PropertiesTable.setModel(new PropertiesTableModel());
	        threadPane.setViewportView(PropertiesTable);

	        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
	        this.setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	            .add(layout.createSequentialGroup()
	                .add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
	                .addContainerGap())
	            .add(threadPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
	            .add(layout.createSequentialGroup()
	                .add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
	                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
	                .add(threadPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE))
	        );
	    }// </editor-fold>

	// Variables declaration - do not modify
	private javax.swing.JTable PropertiesTable;
	private javax.swing.JToolBar jToolBar1;
	private javax.swing.JButton refreshButton;
	private javax.swing.JScrollPane threadPane;
   // End of variables declaration
	    
	    private class PropertiesTableModel extends AbstractTableModel {

			private static final long serialVersionUID = 1L;

			public int getColumnCount() {
				return 2;
			}

			public int getRowCount() {
				return taskMap.size();
			}

			public String getColumnName(int column) {
				if (column == 0) {
					return "Key";
				} else {
					return "Value";
				}
			}

			public Object getValueAt(int rowIndex, int columnIndex) {
				if (columnIndex == 0) {
					return getKey(rowIndex);
				} else {
					return taskMap.get(getKey(rowIndex));
				} // if-else

			}

			private String getKey(int a_index) {
				String retval = "";
				Set<Module> keys = taskMap.keySet();
				for (int i = 0; i < a_index + 1; i++) {
				//	retval = (String) e.nextElement();
				} // for

				return retval;
			}

			public void setValueAt(Object value, int row, int column) {
				if (column == 1) {
					//properties.setProperty(getKey(row), (String) value);
				}
			}

			public boolean isCellEditable(int row, int column) {
				return (column == 1);
			}
		}//inner class
	    
		private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {
			display(lida.getPamDriver().getRunningTasks());
		}


}//class
