package edu.memphis.ccrg.lida.actionselection;

public class LidaActionImpl implements LidaAction{
	
	private Integer content = 0;

	public LidaActionImpl(){
	}
		
	public LidaActionImpl(int i){
		content = new Integer(i);		
	}
	
	public void setContent(Object o){
		content = (Integer) o;
	}
	
	public Object getContent() {
		return content;
	}

	public void print() {
		System.out.println("Action is: " + content);
	}
	
}
