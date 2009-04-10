package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface PAMContent {
	public Object getContent();	
	public void setContent(NodeStructure struct);
	public boolean isEmpty();
}
