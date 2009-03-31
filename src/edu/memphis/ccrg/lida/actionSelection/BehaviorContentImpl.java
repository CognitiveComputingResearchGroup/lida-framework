package edu.memphis.ccrg.lida.actionSelection;



public class BehaviorContentImpl implements BehaviorContent{
	
	private Integer action = 0;

	public BehaviorContentImpl(){
		
	}
		
	public BehaviorContentImpl(int i){
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
