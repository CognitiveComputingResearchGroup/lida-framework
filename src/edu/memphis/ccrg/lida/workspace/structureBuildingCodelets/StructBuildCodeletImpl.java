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
	private int codeletSleepTime = 50;
	//Initialized by constructor
	private FrameworkTimer frameworkTimer;
	private Workspace workspace;
	private List<CodeletReadable> buffersIuse;
	//
	private double activation;
	private NodeStructure soughtContent = new NodeStructureImpl();
	private CodeletAction action = new BasicCodeletAction();
	//
	private ExciteBehavior exciteBehavior;
	private DecayBehavior decayBehavior;
	//TODO: How will these be used?
		
	public StructBuildCodeletImpl(FrameworkTimer t, Workspace w, List<CodeletReadable> buffers, 
								 double activation, NodeStructure content, CodeletAction action){
		frameworkTimer = t;
		workspace = w;
		buffersIuse = buffers;
		this.activation = activation;
		soughtContent = content;
		this.action = action;
	}//constructor
	
	public void run(){
		while(keepRunning){
			try{Thread.sleep(codeletSleepTime);}catch(Exception e){}
			frameworkTimer.checkForStartPause();
			for(CodeletReadable buffer: buffersIuse)
				checkBufferAndPerformAction(buffer);		
		}//while		
	}//run
	
	private void checkBufferAndPerformAction(CodeletReadable buffer){		
		NodeStructure bufferContent = buffer.lookForContent(soughtContent);
		NodeStructure updatedContent = action.getResultOfAction(bufferContent);			
		workspace.addContentToCSM(updatedContent);
	}//checkAndWorkOnBuffer

	public void setActivation(double a){
		activation = a;
	}
	public void setSoughtContent(NodeStructure content){
		soughtContent = content;
	}
	public void setCodeletAction(CodeletAction a){
		action = a;
	}
	public double getActivation(){
		return activation;
	}
	public NodeStructure getSoughtContent(){
		return soughtContent;
	}
	public CodeletAction getCodeletAction(){
		return action;
	}

	public void stopRunning() {
		keepRunning = false;		
	}

	public void decay() {
		decayBehavior.decay(activation);
		
	}

	public void excite(double amount) {
		// TODO Auto-generated method stub
		
	}

	public DecayBehavior getDecayBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public ExciteBehavior getExciteBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDecayBehavior(DecayBehavior c) {
		// TODO Auto-generated method stub
		
	}

	public void setExciteBehavior(ExciteBehavior behavior) {
		// TODO Auto-generated method stub
		
	}

}//class SBCodelet
