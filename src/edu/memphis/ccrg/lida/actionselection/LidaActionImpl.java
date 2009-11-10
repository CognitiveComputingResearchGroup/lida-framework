package edu.memphis.ccrg.lida.actionselection;



public class LidaActionImpl implements LidaAction{
	
	private Integer action = 0;

	public LidaActionImpl(){
		
	}
		
	public LidaActionImpl(int i){
		action = new Integer(i);		
	}
	
	public void setContent(int i){
		action = new Integer(i);
	}
	
	public Object getContent() {
		return action;
	}

	public void print() {
		System.out.println("Action is: " + action);
	}
	

}
