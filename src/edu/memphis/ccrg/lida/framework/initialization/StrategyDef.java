package edu.memphis.ccrg.lida.framework.initialization;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.strategies.Strategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public class StrategyDef {

	private static Logger logger = Logger
			.getLogger("lida.framework.shared.StrategyDef");
	private String name;
	private String className;
	private String type;
	private Map<String, Object> params;
	private boolean flyWeight=true;
	private Strategy instance = null;

	/**
	 * if this strategy is flyweight returns the only one instance, a new instance otherwise.
	 * 
	 * @return the instance
	 */
	public Strategy getInstance() {
		if (flyWeight) {
			if (instance == null) {
				synchronized (this) {
					if (instance == null)
						instance = getNewInstance();
				}
				instance = getNewInstance();
			}
			return instance;
		} else {
			return getNewInstance();
		}
	}

	private Strategy getNewInstance() {
		Strategy st = null;
		try {
			st = (Strategy) Class.forName(className).newInstance();
			st.init(params);

		} catch (InstantiationException e) {
			logger.log(Level.WARNING, "Error creating Strategy.",
					LidaTaskManager.getActualTick());
		} catch (IllegalAccessException e) {
			logger.log(Level.WARNING, "Error creating Strategy.",
					LidaTaskManager.getActualTick());
		} catch (ClassNotFoundException e) {
			logger.log(Level.WARNING, "Error creating Strategy.",
					LidaTaskManager.getActualTick());
		}
		return st;
	}

	/**
	 * @return the flyWeight
	 */
	public boolean isFlyWeight() {
		return flyWeight;
	}

	/**
	 * @param flyWeight
	 *            the flyWeight to set
	 */
	public void setFlyWeight(boolean flyWeight) {
		this.flyWeight = flyWeight;
	}

	/**
	 * @param className
	 * @param name
	 * @param params
	 * @param type
	 */
	public StrategyDef(String className, String name,
			Map<String, Object> params, String type, boolean flyWeight) {
		this.className = className;
		this.name = name;
		this.params = params;
		this.type = type;
		this.flyWeight=flyWeight;
	}

	public StrategyDef() {
		params = new HashMap<String, Object>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
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
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the params
	 */
	public Map<String, Object> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

}
