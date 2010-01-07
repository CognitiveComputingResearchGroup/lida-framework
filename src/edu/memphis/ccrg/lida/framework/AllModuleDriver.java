/**
 * 
 */
package edu.memphis.ccrg.lida.framework;


/**
 * @author Javier Snaider
 *
 */
public class AllModuleDriver extends TaskSpawnerImpl {

	
	public AllModuleDriver(LidaTaskManager tm){
	super(tm);
	}

	@Override
	protected void runThisLidaTask() {
	}

	@Override
	public String toString() {
		return "All ModuleType Driver";
	}


}
