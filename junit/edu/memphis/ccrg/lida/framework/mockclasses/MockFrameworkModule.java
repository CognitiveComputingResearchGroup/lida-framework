package edu.memphis.ccrg.lida.framework.mockclasses;

import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;

public class MockFrameworkModule extends FrameworkModuleImpl {

	public boolean wasDecayed;
	public long decayTicks;

	@Override
	public Object getModuleContent(Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addListener(ModuleListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void decayModule(long ticks) {
		decayTicks=ticks;
		wasDecayed = true;

	}

}
