package edu.memphis.ccrg.lida.workspace.sbCodelets;

public class SBCodelet {
	
	private double activation = 0.0;
	private Context context;
	private CodeletAction action;
	
	public SBCodelet(){
		context = new Context();
		action = new CodeletAction();
	}
	
	public SBCodelet(double activ, Context c, CodeletAction a){
		activation = activ;
		context = c;
		action = a;
	}
	
	public void setActivation(double a){
		activation = a;
	}
	public void setContext(Context c){
		context = c;
	}
	public void setCodeletAction(CodeletAction a){
		action = a;
	}
	public double getActivation(){
		return activation;
	}
	public Context getContext(){
		return context;
	}
	public CodeletAction getCodeletAction(){
		return action;
	}
	

}
