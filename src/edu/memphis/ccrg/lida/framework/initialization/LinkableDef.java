package edu.memphis.ccrg.lida.framework.initialization;

import java.util.HashMap;
import java.util.Map;

public class LinkableDef {
	private String name;
	private String className;
	private Map<String,String>defeaultStrategies;
	private Map<String,Object> params;

	/**
	 * @param className
	 * @param defeaultStrategies
	 * @param name
	 * @param params
	 */
	public LinkableDef(String className, Map<String,String> defeaultStrategies,
			String name, Map<String,Object> params) {
		this.className = className;
		this.defeaultStrategies = defeaultStrategies;
		this.name = name;
		this.params = params;
	}

	/**
	 * 
	 */
	public LinkableDef() {
		this.params = new HashMap<String,Object>();;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the defeaultBehaviors
	 */
	public Map<String,String> getDefeaultStrategies() {
		return defeaultStrategies;
	}
	/**
	 * @param defeaultStrategies the defeaultBehaviors to set
	 */
	public void setDefeaultStrategies(Map<String,String> defeaultStrategies) {
		this.defeaultStrategies = defeaultStrategies;
	}
	/**
	 * @return the params
	 */
	public Map<String,Object> getParams() {
		return params;
	}
	/**
	 * @param params the params to set
	 */
	public void setParams(Map<String,Object> params) {
		this.params = params;
	}

}
