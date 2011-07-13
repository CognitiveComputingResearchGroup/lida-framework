/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ActionSelectionPanel.java
 *
 * Created on Jul 13, 2011, 3:36:27 PM
 */
package edu.memphis.ccrg.lida.framework.gui.panels;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.Behavior;
import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.panels.GuiPanel;
import edu.memphis.ccrg.lida.framework.gui.panels.GuiPanelImpl;

/**
 * A {@link GuiPanel} which displays the current {@link Behavior} elements in the {@link ActionSelection} module.
 * @author Ryan J. McCall
 */
public class ActionSelectionPanel extends GuiPanelImpl {

    private static final Logger logger = Logger.getLogger(ActionSelectionPanel.class.getCanonicalName());
    private FrameworkModule module;
    private Collection<Behavior> behaviors;
    private Behavior[] behaviorArray = new Behavior[0];
	
    /** Creates new form ActionSelectionPanel */
    public ActionSelectionPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        refreshButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        behaviorsTable = new javax.swing.JTable();

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

        behaviorsTable.setModel(new BehaviorTableModel());
        jScrollPane1.setViewportView(behaviorsTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
        );
    }// </editor-fold>

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
    	refresh();
    }//GEN-LAST:event_refreshButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable behaviorsTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton refreshButton;
    // End of variables declaration//GEN-END:variables
    
    @Override
    public void initPanel(String[] param) {
        module = agent.getSubmodule(ModuleName.ActionSelection);
        if (module == null) {
            logger.log(Level.WARNING,
                    "Error initializing panel, Module does not exist in agent.",
                    0L);
        }
    }
    
    @Override
    public void refresh() {
        display(module.getModuleContent("behaviors"));
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public void display(Object o) {
        behaviors = (Collection<Behavior>) o;
        behaviorArray = behaviors.toArray(new Behavior[0]);

        ((AbstractTableModel) behaviorsTable.getModel()).fireTableStructureChanged();
    }

    private class BehaviorTableModel extends AbstractTableModel {
        private String[] columNames = {"Behavior ID", "Activation",
            "Context", "Action", "Adding Result", "Deleting Result"};
        private DecimalFormat df = new DecimalFormat("0.0000");

        @Override
        public int getColumnCount() {
            return columNames.length;
        }

        @Override
        public int getRowCount() {
            return behaviorArray.length;
        }

        @Override
        public String getColumnName(int column) {
            if (column < columNames.length) {
                return columNames[column];
            }
            return "";
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex > behaviorArray.length || columnIndex > columNames.length
                    || rowIndex < 0 || columnIndex < 0) {
                return null;
            }
            Behavior behavior = behaviorArray[rowIndex];

            switch (columnIndex) {
                case 0:
                    return behavior.getId();
                case 1:
                    return df.format(behavior.getActivation());
                case 2:
                    return behavior.getContext();
                case 3:
                    return behavior.getAction().getLabel();
                case 4:
                    return behavior.getAddingList();
                case 5:
                    return behavior.getDeletingList();
                default:
                    return "";
            }

        }
    }
}
