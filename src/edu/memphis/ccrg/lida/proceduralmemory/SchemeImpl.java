package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.framework.shared.ActivatibleImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

public class SchemeImpl extends ActivatibleImpl implements Scheme {
	
	private long id;
	private NodeStructure context = new NodeStructureImpl();
	private LidaAction action;
	private NodeStructure result = new NodeStructureImpl();
	
	public SchemeImpl(long id, NodeStructure context, NodeStructure action, NodeStructure result){
		
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public NodeStructure getContext() {
		return context;
	}
	public void setContext(NodeStructure c) {
		context = c;
	}

	public LidaAction getSchemeAction() {
		return action;
	}
	public void setSchemeAction(LidaAction a) {
		action = a;
	}
	
	public NodeStructure getResult() {
		return result;
	}
	public void setResult(NodeStructure r) {
		result = r;
	}



}
