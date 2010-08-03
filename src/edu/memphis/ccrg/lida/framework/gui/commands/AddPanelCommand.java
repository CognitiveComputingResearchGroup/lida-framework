package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;

public class AddPanelCommand extends GenericCommandImpl {

	@Override
	public void execute(Lida lida) {
		java.awt.Container parent = (java.awt.Container)getParameter("parent");
		javax.swing.JPanel panel = (javax.swing.JPanel)getParameter("panel");
		
		if (parent != null) {
			boolean panelFound = false;
			for (java.awt.Component c : parent.getComponents()) {
				if (c.equals(panel)) {
					panelFound = true;
					//panel exists, remove it
					parent.remove(panel);
					break;
				}
			}
			if (!panelFound) {
				//panel doesn't exist, add it
				parent.add(panel);
			}
			parent.repaint();
		}
	}

}
