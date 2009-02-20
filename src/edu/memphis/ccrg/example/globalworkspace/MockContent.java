package edu.memphis.ccrg.example.globalworkspace;

import edu.memphis.ccrg.globalworkspace.BroadcastContent;

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

}
