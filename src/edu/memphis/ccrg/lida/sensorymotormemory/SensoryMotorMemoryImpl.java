package edu.memphis.ccrg.lida.sensorymotormemory;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;


public class SensoryMotorMemoryImpl extends LidaModuleImpl implements SensoryMotorMemory{

	public SensoryMotorMemoryImpl() {
		super(ModuleName.SensoryMotorMemory);
		// TODO Auto-generated constructor stub
	}

	private List<SensoryMotorListener> listeners = new ArrayList<SensoryMotorListener>();

	@Override
	public void addSensoryMotorListener(SensoryMotorListener l) {
		listeners.add(l);		
	}

	@Override
	public Object getModuleContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof SensoryMotorListener){
			addSensoryMotorListener((SensoryMotorListener)listener);
		}
	}

	@Override
	public void receiveSensoryMemoryContent(Object content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveActionId(long id) {
		// TODO Auto-generated method stub
		
	}

}
