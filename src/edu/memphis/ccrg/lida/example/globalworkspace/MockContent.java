package edu.memphis.ccrg.lida.example.globalworkspace;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;

public class MockContent implements BroadcastContent {
private Object content;

/**
 * @param content
 */
public MockContent(Object content) {
	super();
	this.content = content;
}

@Override
public String toString() {
	// TODO Auto-generated method stub
	return content.toString();
}

public Object getContent() {
	// TODO Auto-generated method stub
	return null;
}

}
