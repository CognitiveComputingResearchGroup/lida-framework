package edu.memphis.ccrg.lida.attention;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CurrentSituationalModel;

public class AttentionCodeletImpl extends LidaTaskImpl implements AttentionCodelet{
	
	private boolean keepRunning = true;
	private ContentDetectBehavior checkBehavior = new DefaultContentDetectBehavior();
	//
	private CurrentSituationalModel csm;
	private GlobalWorkspace global;
	private LidaTaskManager timer;
	private NodeStructure soughtContent = new NodeStructureImpl();
	    
    public AttentionCodeletImpl(CurrentSituationalModel csm, GlobalWorkspace g, 
    							double activation,LidaTaskManager timer){
    	super(3);
    	setActivation(activation);
    	this.csm = csm;
    	global = g;
    	
    	this.timer=timer;
    }

	public void run() {
		timer.checkForStartPause();
		if (!LidaTaskManager.isTicksMode() || (hasEnoughTicks())) {
			if (LidaTaskManager.isTicksMode()) {
				consumeTicksForACycle();
			}
			try {
				// Sleeps a lap proportional for each task
				Thread.sleep(timer.getSleepTime() * getTicksForCycle());
			} catch (InterruptedException e) {
				stopRunning();
				return;
			}
		}
			if(checkBehavior.hasSoughtContent(csm)){
				NodeStructure csmContent = checkBehavior.getSoughtContent(csm); 
				if(csmContent != null)
					global.addCoalition(new CoalitionImpl(csmContent, getActivation()));				
			}//if
		return;
	}//method	

	public void stopRunning() {
		keepRunning = false;		
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