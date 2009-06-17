package edu.memphis.ccrg.lida.example.vision;

import edu.memphis.ccrg.lida.environment.EnvironmentContent;


public class VisionEnvironmentContent implements EnvironmentContent{
	
	private String guiContent = "";
	private Object content = new double[1][1];
	
	public String getGuiContent(){
		return guiContent;
	}
	
	public void setGuiContent(String c){
		guiContent = c;
	}
	
	public Object getContent() {
		return content;
	}

	public void setContent(Object o) {
		content = o;		
	}

}//class