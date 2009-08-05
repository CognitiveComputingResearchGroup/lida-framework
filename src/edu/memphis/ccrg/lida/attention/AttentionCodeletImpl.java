package edu.memphis.ccrg.lida.attention;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CurrentSituationalModel;

public class AttentionCodeletImpl extends LidaTaskImpl implements AttentionCodelet{
	
	private ContentDetectBehavior checkBehavior = new DefaultContentDetectBehavior();
	//
	private CurrentSituationalModel csm;
	private GlobalWorkspace global;
	private LidaTaskManager timer;
	private NodeStructure soughtContent;
	    
    public AttentionCodeletImpl(CurrentSituationalModel csm, GlobalWorkspace g, int ticksPerStep,
    							double activation,LidaTaskManager timer, NodeStructure soughtContent){
    	super(ticksPerStep);
    	setActivation(activation);
    	this.csm = csm;
    	global = g;
    	this.soughtContent=soughtContent;
    	
    	this.timer=timer;
    }

	public void run() {
		//If not is ticks Mode then business as usual.
		if (!LidaTaskManager.isTicksModeEnabled()){
			//System.out.println("not in ticks mode");
			runOneStep();
		}else if(hasEnoughTicks()){
			//System.out.println("use ticks");
			useOneStepOfTicks();
			runOneStep();
		}
		//setTaskStatus(LidaTask.RUNNING);
		//System.out.println("module driver impl, run, setting task status to run " + LidaTask.RUNNING);
	}//method	
	private void runOneStep(){
		runOneProcessingStep();
		try {
			// Sleeps a lap proportional for each task
			Thread.sleep(timer.getTimeScale() * getNumberOfTicksPerStep());
		}catch (InterruptedException e){
			stopRunning();
		}
	}

	protected void runOneProcessingStep(){
		if(checkBehavior.hasSoughtContent(csm)){
			NodeStructure csmContent = checkBehavior.getSoughtContent(csm); 
			if(csmContent != null)
				global.addCoalition(new CoalitionImpl(csmContent, getActivation()));				
		}//if	
	}

	public boolean hasSoughtContent(CurrentSituationalModel csm) {
		NodeStructure model = csm.getModel();
		Collection<Node> nodes = soughtContent.getNodes();
		Collection<Link> links = soughtContent.getLinks();
		for(Node n: nodes)
			if(!model.hasNode(n))
				return false;
		
		for(Link l: links)
			if(!model.hasLink(l))	
				return false;
		
		return true;
	}

	public NodeStructure getSoughtContent(CurrentSituationalModel csm) {
		if(hasSoughtContent(csm))
			return csm.getModel();
		else 
			return null;
	}

}//class