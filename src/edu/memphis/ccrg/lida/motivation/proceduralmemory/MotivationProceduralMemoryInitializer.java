package edu.memphis.ccrg.lida.motivation.proceduralmemory;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.initialization.FullyInitializable;
import edu.memphis.ccrg.lida.framework.initialization.GlobalInitializer;
import edu.memphis.ccrg.lida.framework.initialization.Initializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.BasicProceduralMemoryInitializer;

/**
 * An {@link Initializer} for the {@link MotivationProceduralMemory}.
 * Its only function is to give the Procedural Memory access to the temporal 
 * {@link LinkCategory}. This must be done in an initializer because the LinkCategory may have not yet 
 * been created when the procedural memory's {@link Initializable#init()} runs.
 * @author Ryan J McCall
 */
public class MotivationProceduralMemoryInitializer extends BasicProceduralMemoryInitializer implements Initializer {
	
	private static final Logger logger = Logger.getLogger(MotivationProceduralMemoryInitializer.class.getCanonicalName());

	@Override
	public void initModule(FullyInitializable m, Agent a, Map<String, ?> params) {
		super.initModule(m, a, params);
		MotivationProceduralMemory pm = (MotivationProceduralMemory) m;
		String label = (String) params.get("linkCategory");
		GlobalInitializer gi = GlobalInitializer.getInstance();
		Object o = gi.getAttribute(label.trim());
		if (o instanceof LinkCategory) {
			pm.setTemporalLinkCategory((LinkCategory) o);
		} else {
			logger.log(Level.WARNING,
					"Did not get a LinkCategory from GlobalInitializer using name {1}.",
					new Object[] {TaskManager.getCurrentTick(), label});
		}
	}

}
