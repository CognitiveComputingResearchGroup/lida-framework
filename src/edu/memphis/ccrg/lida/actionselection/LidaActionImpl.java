package edu.memphis.ccrg.lida.actionselection;

import edu.memphis.ccrg.lida.framework.LidaModule;

/**
 * @author Javier Snaider
 *
 */
public abstract class LidaActionImpl implements LidaAction{
	
	private Object content = 0;
	protected LidaModule module;
	private String label;

	public LidaActionImpl(){
	}
	
	public LidaActionImpl(String label){
		this.label = label;
	}
		
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.actionselection.LidaAction#setContent(java.lang.Object)
	 */
	public void setContent(Object content){
		this.content =  content;
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.actionselection.LidaAction#getContent()
	 */
	public Object getContent() {
		return content;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.actionselection.LidaAction#getLabel()
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.actionselection.LidaAction#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.actionselection.LidaAction#setAssociatedModule(edu.memphis.ccrg.lida.framework.LidaModule)
	 */
	@Override
	public void setAssociatedModule(LidaModule module) {
		this.module = module;		
	}
}
