package edu.memphis.ccrg.lida.framework;


public abstract class GenericModuleDriver implements ModuleDriver{

	protected boolean keepRunning = true;
	protected FrameworkThreadManager timer;
	private static long threadIdCount = 0;
	private long threadID;

	public GenericModuleDriver(FrameworkThreadManager timer) {
		super();
		this.timer=timer;
		threadID=threadIdCount++;
	}

	public void run() {
		while(keepRunning){
			try{
				Thread.sleep(timer.getSleepTime());
			}catch(InterruptedException e){
				stopRunning();
			}				
			timer.checkForStartPause();
			
			cycleStep();
		}//while	
	}//method run

	public abstract void cycleStep();		

	public void stopRunning() {
		keepRunning = false;		
	}//method 

	public void setThreadID(long id) {
		threadID = id;
	}

	public long getThreadID() {
		return threadID;
	}

}