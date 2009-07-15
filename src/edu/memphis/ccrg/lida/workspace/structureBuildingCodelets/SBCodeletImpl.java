package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.List;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;

public class SBCodeletImpl implements StructureBuildingCodelet{
	
	private boolean keepRunning = true;
	//Initialized by constructor
	private FrameworkTimer frameworkTimer;
	
	private List<CodeletReadable> accessibleBuffers;
	private CodeletWritable csm;//to access CSM	
	
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
	
	public void addFrameworkTimer(FrameworkTimer timer) {
		frameworkTimer = timer;
	}
	
	public void run(){
		while(keepRunning){
			try{
				Thread.sleep(codeletSleepTime);
			}catch(InterruptedException e){
				stopRunning();
			}
			frameworkTimer.checkForStartPause();
			for(CodeletReadable buffer: accessibleBuffers)
				action.performAction(buffer, csm);	
		//	keepRunning = false;
		}//while	
	}//run

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
	}
	public long getId() {
		return id;
	}
	//
	public void setAccessibleModules(List<CodeletReadable> buffers, List<CodeletWritable> writableBuffers) {
		accessibleBuffers = buffers;		
		csm = writableBuffers.get(0);
	}
	public List<CodeletReadable> getAccessibleBuffers() {
		return accessibleBuffers;
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
	
}//class SBCodelet
