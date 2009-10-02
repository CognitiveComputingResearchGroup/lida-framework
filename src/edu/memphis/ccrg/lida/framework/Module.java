package edu.memphis.ccrg.lida.framework;

import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryDriver;

public enum Module {
	
	environment, 
	sensoryMemory,
	perceptualAssociativeMemory,
	
	transientEpisodicMemory,
	declarativeMemory,
	
	perceptualBuffer,
	episodicBuffer,
	broadcastQueue,
	currentSituationalModel,
	
	structureBuildingCodelets,
	attentionCodelets,	
	globalWorkspace,
	
	proceduralMemory,
	actionSelection,
	sensoryMotorMemory,
	
	allModules,
	noModule, 
	
	sensoryMemoryDriver,
	pamDriver,
	perceptualBufferDriver,
	attentionDriver,
	sbCodeletDriver, 
	temDriver, 
	proceduralDriver
}
