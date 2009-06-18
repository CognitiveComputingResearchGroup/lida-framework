package edu.memphis.ccrg.lida.sensoryMemory;

/**
 * Since this class is implementation specific and it needs to be in the 
 * framework, I've written it abstractly.  Specific implementations can 
 * extend from this class and define the field and methods as needed. 
 * 
 * @author ryanjmccall
 *
 */
public class SensoryContentImpl implements SensoryContent{

	private Object content = null;
	
	public synchronized void setContent(Object o){
		content = o;
	}
	
	public Object getContent(){	
		return content;	
	}
	
	public Object getThis(){
		return this;
	}	
	
}//class
