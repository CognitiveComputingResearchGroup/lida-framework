package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;
import java.util.Map;

public class SetTimeScaleCommand implements Command {

	@Override
	public void execute(Lida lida) {
	}

    public void setParameter(String name, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getParameter(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getResult() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setParameters(Map<String, Object> parameters) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
