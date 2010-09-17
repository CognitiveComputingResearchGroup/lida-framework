package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.dao.DAOManager;

public class DAOCommand extends GenericCommandImpl {
        public static String LOAD_ACTION = "load";
        public static String SAVE_ACTION = "save";

        public static String SUCCESS_STRING = "successful";
        public static String FAILED_STRING = "failed";

	@Override
	public void execute(Lida lida) {
		String action = getParameter("action").toString().toLowerCase();
                boolean success = false;
                String displayString;
		if (action.equals(SAVE_ACTION)) {
                    success = DAOManager.getInstance().save();
                }
                else if (action.equals(LOAD_ACTION)) {
                    success = DAOManager.getInstance().load();
                }
                if (success) displayString = action + " " + SUCCESS_STRING + ".";
                else displayString = action + " " + FAILED_STRING + ".";
                javax.swing.JOptionPane.showMessageDialog(null, displayString);
	}

}
