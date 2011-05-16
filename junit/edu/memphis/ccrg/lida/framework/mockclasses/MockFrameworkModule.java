package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;

public class MockFrameworkModule extends FrameworkModuleImpl {

	public boolean wasDecayed;
	public long decayTicks;
	public List<ModuleListener>listeners = new ArrayList<ModuleListener>();

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
		listeners.add(listener);

	}

	@Override
	public void decayModule(long ticks) {
		decayTicks=ticks;
		wasDecayed = true;
	}

}
