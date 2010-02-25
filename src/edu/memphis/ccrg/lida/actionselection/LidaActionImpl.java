package edu.memphis.ccrg.lida.actionselection;

public class LidaActionImpl implements LidaAction{
	
	private Object content = 0;

	public LidaActionImpl(){
	}
		
	public LidaActionImpl(int i){
		content = new Integer(i);		
	}
	
	public void setContent(Object o){
		content =  o;
	}
	
	public Object getContent() {
		return content;
	}

	public void print() {
		System.out.println("Action is: " + content);
	}

	public void performAction() {
		// TODO Auto-generated method stub
		
	}
	
}
