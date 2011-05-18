package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.initialization.FullyInitializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;

public class MockInitializer2 implements Initializer {

	public static FullyInitializable module;
	public static Agent agent;
	public static Map<String, ?> params;
	
	@Override
	public void initModule(FullyInitializable module, Agent agent,
			Map<String, ?> params) {
		MockInitializer2.module = module;
		MockInitializer2.agent = agent;
		MockInitializer2.params = params;
	}

}
