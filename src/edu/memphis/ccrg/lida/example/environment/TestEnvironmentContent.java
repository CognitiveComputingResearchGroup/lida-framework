package edu.memphis.ccrg.lida.example.environment;

import edu.memphis.ccrg.lida.environment.EnvironmentContent;

public class TestEnvironmentContent implements EnvironmentContent{
	
	private Object content = -1;

	public Object getContent() {
		return content;
	}

	public void setContent(Object o) {
		content = o;		
	}

}
