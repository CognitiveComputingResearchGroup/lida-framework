package edu.memphis.ccrg.lida._actionSelection;



public class ActionContentImpl implements ActionContent{
	
	private Integer action = 0;

	public ActionContentImpl(){
		
	}
		
	public ActionContentImpl(int i){
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
