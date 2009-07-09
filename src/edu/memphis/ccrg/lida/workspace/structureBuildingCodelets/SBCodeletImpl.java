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
	private Long id;
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
	public void setId(Long id){
		this.id = id;
	}
	public Long getId() {
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
		id = 0L;
		type = 0;		
	}
	public int getType() {
		return type;
	}
	
}//class SBCodelet
