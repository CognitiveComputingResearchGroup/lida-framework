/*
 * @(#)SimpleAttentionCodelet.java  1.0  May 3, 2007
 *
 * Copyright 2006-2008 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.attention;

import java.util.ArrayList;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.workspace.csm.CSM;

public class SimpleAttentionCodelet implements AttentionCodelet {
	
	private CSM model;
	//private Global
    
    public SimpleAttentionCodelet() {
    }
    
    public ArrayList<Coalition> buildCoalitions() {
        ArrayList<Coalition> coalitions = new ArrayList<Coalition>();
//        Model model = workspace.getModel();
//        ArrayList<WorkspaceNode> nodes = model.getNodes();
//        for (WorkspaceNode n : nodes) {
//            Coalition c = new Coalition(this, n.getSlipnetNode(),
//                    new ArrayList(n.getSlipnetNode().getChildren()));
//            coalitions.add(c);
//        }
        return coalitions;
    }

	public void addCoalToGlobalWorkspace() {
		// TODO Auto-generated method stub
		
	}
}