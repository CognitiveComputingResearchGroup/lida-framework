package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.List;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.workspace.main.Workspace;

public class StructBuildCodeletImpl implements StructureBuildingCodelet{
	
	private boolean keepRunning = true;
	//Initialized by constructor
	private FrameworkTimer frameworkTimer;
	private Workspace workspace;//to access CSM
	
	private List<CodeletReadable> accessibleBuffers;
	private int codeletSleepTime = 50;
	private double activation;
	private NodeStructure soughtContent = new NodeStructureImpl();
	private CodeletAction action = new BasicCodeletAction();
	//
	private ExciteBehavior exciteBehavior;
	private DecayBehavior decayBehavior;
	//TODO: How will these be used?
	private Long id;
	private int type;
		
	public StructBuildCodeletImpl(FrameworkTimer t, Workspace w, List<CodeletReadable> buffers, 
								 double activation, NodeStructure content, CodeletAction action){
		frameworkTimer = t;
		workspace = w;
		accessibleBuffers = buffers;
		this.activation = activation;
		soughtContent = content;
		this.action = action;
	}//constructor
	
	public void run(){
		while(keepRunning){
			try{
				Thread.sleep(codeletSleepTime);
			}catch(InterruptedException e){
				stopRunning();
			}
			frameworkTimer.checkForStartPause();
			for(CodeletReadable buffer: accessibleBuffers)
				action.performAction(buffer, workspace);	
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
	public void setAccessibleBuffers(List<CodeletReadable> buffers) {
		accessibleBuffers = buffers;		
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
