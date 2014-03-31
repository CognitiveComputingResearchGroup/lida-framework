package edu.memphis.ccrg.lida.motivation.workspace;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.initialization.FullyInitializable;
import edu.memphis.ccrg.lida.framework.initialization.GlobalInitializer;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;

/**
 * {@link Initializer} for the {@link MotivationWorkspace}.
 * @author Ryan J McCall
 *
 */
public class MotivationWorkspaceInitializer implements Initializer {

	@Override
	public void initModule(FullyInitializable m, Agent a, Map<String, ?> params) {
		MotivationWorkspace workspace = (MotivationWorkspace)m;
		String label = (String) params.get("linkCategory");
		Object o = GlobalInitializer.getInstance().getAttribute(label.trim());
		if (o instanceof LinkCategory) {
			workspace.setTemporalCategory((LinkCategory)o);
		} 
	}
}
