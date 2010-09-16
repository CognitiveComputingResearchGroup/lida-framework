package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.dao.DAOManager;

public class DAOCommand extends GenericCommandImpl {
        public static String LOAD_ACTION = "load";
        public static String SAVE_ACTION = "save";

	@Override
	public void execute(Lida lida) {
		String action = getParameter("action").toString().toLowerCase();
		if (action.equals(SAVE_ACTION)) {
                    DAOManager.getInstance().save();
                }
                else if (action.equals(LOAD_ACTION)) {
                    DAOManager.getInstance().load();
                }
	}

}
