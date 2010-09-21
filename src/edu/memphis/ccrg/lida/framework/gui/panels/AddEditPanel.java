/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
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
 * AddPanel.java
 *
 * Created on Aug 2, 2010, 4:24:41 PM
 */

package edu.memphis.ccrg.lida.framework.gui.panels;

import java.util.List;

/**
 *
 * @author Administrator
 */
public class AddEditPanel extends LidaPanelImpl {

    /** Creates new form AddPanel */
    public AddEditPanel() {
        initComponents();
    }

    public void init(String name) {
       this.setName(name);
        for (java.awt.Component c : this.getComponents()) {
          if (c instanceof javax.swing.JTextField)
            ((javax.swing.JTextField)c).setText("");
        }
    }

    public void initClassnames(List<String> classNames) {
        for (String c : classNames) {
            cmbClassname.addItem(c);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbPosition = new javax.swing.JComboBox();
        chkRefresh = new javax.swing.JCheckBox();
        btnAddPanel = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtParameters = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cmbClassname = new javax.swing.JComboBox();

        jLabel1.setText("Name:");

        txtName.setPreferredSize(new java.awt.Dimension(60, 20));

        jLabel2.setText("Classname:");

        jLabel3.setText("Position:");

        cmbPosition.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A", "B", "C", "FLOAT", "TOOL" }));
        cmbPosition.setSelectedIndex(1);

        chkRefresh.setSelected(true);
        chkRefresh.setText("Refresh after load");

        btnAddPanel.setText("OK");
        btnAddPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPanelActionPerformed(evt);
            }
        });

        jLabel5.setText("Parameters:");

        txtParameters.setPreferredSize(new java.awt.Dimension(60, 20));

        jLabel6.setText("(separated by \",\")");

        cmbClassname.setEditable(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbPosition, 0, 245, Short.MAX_VALUE)
                                    .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                                    .addComponent(jLabel6)
                                    .addComponent(txtParameters, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                                    .addComponent(cmbClassname, 0, 245, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(chkRefresh)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnAddPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbClassname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(txtParameters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkRefresh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(btnAddPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>

    private void btnAddPanelActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private static int PANEL_NAME = 0;
    private static int CLASS_NAME = 1;
    private static int PANEL_POSITION = 2;
    private static int TAB_ORDER = 3;
    private static int MUST_REFRESH = 4;
    private static int FIRST_PARAM = 5;

    private String tabOrder = "1";

    public String[] getPanelParams() {
        String[] extraparamArray = txtParameters.getText().split(",");
        int extraParams = txtParameters.getText().length() == 0 ? 0 : extraparamArray.length;

        String[] panelParams = new String[FIRST_PARAM + extraParams];
        panelParams[PANEL_NAME] = txtName.getText();
        panelParams[CLASS_NAME] = cmbClassname.getSelectedItem().toString();
        panelParams[PANEL_POSITION] = cmbPosition.getSelectedItem().toString();
        panelParams[TAB_ORDER] = tabOrder;
        panelParams[MUST_REFRESH] = chkRefresh.isSelected() ? "Y" : "N";

        System.arraycopy(extraparamArray, 0, panelParams, FIRST_PARAM, extraParams);

        return panelParams;
    }

    public void setPanelParams(String[] panelParams) {
        txtName.setText(panelParams[PANEL_NAME]);
        cmbClassname.setSelectedItem(panelParams[CLASS_NAME]);
        cmbPosition.setSelectedItem(panelParams[PANEL_POSITION]);
        tabOrder = panelParams[TAB_ORDER];
        chkRefresh.setSelected(panelParams[MUST_REFRESH].equalsIgnoreCase("y"));

        if (panelParams.length > FIRST_PARAM) {
            String extraParams = "";
            for (int i = FIRST_PARAM; i < panelParams.length; i++) {
                extraParams += panelParams[i] + ",";
            }
            txtParameters.setText(extraParams.substring(0, extraParams.length() - 1));
        }
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnAddPanel;
    private javax.swing.JCheckBox chkRefresh;
    private javax.swing.JComboBox cmbClassname;
    private javax.swing.JComboBox cmbPosition;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtParameters;
    // End of variables declaration

}
