/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LidaPanelImpl.java
 *
 * Created on 17/08/2009, 08:08:08
 */

package edu.memphis.ccrg.lida.framework.gui.panels;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.gui.LidaGuiController;

import javax.swing.JPanel;

/**
 *
 * @author Javier
 */
public class LidaPanelImpl extends javax.swing.JPanel implements LidaPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected LidaGuiController controller;
	protected Lida lida;
    private Module supportedModule;

    /** Creates new form LidaPanelImpl */
    public LidaPanelImpl() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public void registrerLidaGuiController(LidaGuiController lgc) {
        controller = lgc;
    }

    public void display(Object o) {

    }

    public void refresh() {
    }

    public JPanel getPanel() {
        return this;
    }

    	public void registerLida(Lida lida) {
		this.lida=lida;
	}

    public Module getSupportedModule() {
        return supportedModule;
    }
    public void setSupportedModule(Module module) {
         this.supportedModule=module;
    }
}
