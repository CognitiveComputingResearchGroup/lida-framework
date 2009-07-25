package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.ArrayList;
import java.util.List;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;

public class SBCodeletImpl implements StructureBuildingCodelet{
	
	private boolean keepRunning = true;
	//Initialized by constructor
	private LidaTaskManager frameworkTimer;
	
	private List<List<NodeStructure>> accessibleBuffers = new ArrayList<List<NodeStructure>>();
	private List<CodeletWritable> writables = new ArrayList<CodeletWritable>();
	
	private int codeletSleepTime;
	private double activation;
	private NodeStructure soughtContent;
	private CodeletAction action;
	//
	private ExciteBehavior exciteBehavior;
	private DecayBehavior decayBehavior;
	//TODO: How will these be used?
	private long id;
	private int type;
	private CodeletResult results = new BasicCodeletResult();
	
	public void addFrameworkTimer(LidaTaskManager timer) {
		frameworkTimer = timer;
	}
	
	public Object call(){
		
		return results;
	}//run
	

	public void run() {
		while(keepRunning){
			try{
				Thread.sleep(codeletSleepTime);
			}catch(InterruptedException e){
				keepRunning = false;
			}
			frameworkTimer.checkForStartPause();
			//TODO: Should this use the GenericModuleDriver?
			
			for(List<NodeStructure> buffer: accessibleBuffers)
				for(CodeletWritable writable: writables)
					action.performAction(buffer, writable);	
		}//while
		results.reportFinished();	
	}

	public void stopRunning() {
		keepRunning = false;		
	}

	//**** Activatible methods *****
	public void setActivation(double a){
		activation = a;
	}
	public double getActivation(){
		return activation;
	}
	//
	public void decay() {
		decayBehavior.decay(activation);
	}
	public void excite(double amount) {
		exciteBehavior.excite(activation, amount);
	}
	//
	public void setDecayBehavior(DecayBehavior db) {
		decayBehavior = db;
	}
	public DecayBehavior getDecayBehavior() {
		return decayBehavior;
	}
	//
	public void setExciteBehavior(ExciteBehavior eb) {
		exciteBehavior = eb;		
	}
	public ExciteBehavior getExciteBehavior() {
		return exciteBehavior;
	}

	//**** StructureBuildingCodelet methods ****
	public void setSoughtContent(NodeStructure content){
		soughtContent = content;
	}
	public NodeStructure getSoughtContent(){
		return soughtContent;
	}
	//
	public void setCodeletAction(CodeletAction a){
		action = a;
	}	
	public CodeletAction getCodeletAction(){
		return action;
	}
	//
	public void setId(long id){
		this.id = id;
		results.setId(id);
	}
	public long getId() {
		return results.getId();
	}
	//

	public void addReadableBuffer(List<NodeStructure> buffer) {
		accessibleBuffers.add(buffer);		
	}
	public void addWritableModule(CodeletWritable module) {
		writables.add(module);
	}
	public List<List<NodeStructure>> getReadableBuffers() {
		return accessibleBuffers;
	}
	public List<CodeletWritable> getWriteableBuffers() {
		return writables;
	}
	//
	public void setSleepTime(int ms) {
		codeletSleepTime = ms;
	}
	public int getSleepTime() {
		return codeletSleepTime;
	}
	//
	public void clearForReuse() {
		accessibleBuffers.clear();
		writables.clear();
		codeletSleepTime = 50;
		activation = 0.0;
		soughtContent = new NodeStructureImpl();
		action = new BasicCodeletAction();
		id = 0;
		type = 0;		
	}
	public int getType() {
		return type;
	}
	
	/**
	 * This method compares this object with any kind of Node. returns true if
	 * the id of both are the same.
	 */
	public boolean equals(Object o) {
		if (!(o instanceof StructureBuildingCodelet)) {
			return false;
		}
		StructureBuildingCodelet temp = (StructureBuildingCodelet) o;
		return temp.getId() == id && temp.getType() == type;
	}

	public int hashCode() {
		int hash = 1;
	    Integer v1 = new Integer(type);
	    Long v2 = new Long(id);
	    hash = hash * 31 + v2.hashCode();
	    hash = hash * 31 + (v1 == null ? 0 : v1.hashCode());
	    return hash;
	}

	public long getTaskID() {
		return id;
	}

	public void setTaskID(long id) {
		this.id = id;
	}
	
}//class SBCodelet
