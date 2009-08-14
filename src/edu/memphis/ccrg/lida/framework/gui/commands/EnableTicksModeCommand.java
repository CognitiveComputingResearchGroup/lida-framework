package edu.memphis.ccrg.lida.framework.gui.commands;
 
import edu.memphis.ccrg.lida.framework.Lida;

public class EnableTicksModeCommand extends GenericCommandImpl {

	@Override
	public void execute(Lida lida) {
		Boolean b= (Boolean)getParameter("enable");
		lida.getTaskManager().setInTicksMode(b);
	} 

}
=======
package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;

public class EnableTicksModeCommand extends GenericCommandImpl {

	@Override
	public void execute(Lida lida) {
		Boolean b= (Boolean)getParameter("enable");
		lida.getTaskManager().setInTicksMode(b);
	}

}

