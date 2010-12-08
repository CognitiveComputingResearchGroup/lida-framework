/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.LidaImpl;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.ModuleUsage;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * Creates a Lida Object from an xml file
 * @author Javier Snaider
 * 
 */ 
public class LidaXmlFactory implements LidaFactory {

	private static final String DEFAULT_FILE_NAME = "configs/lida.xml";
	private static final int DEFAULT_TICK_DURATION = 10;
	private static final int DEFAULT_NUMBER_OF_THREADS = 20;
	private static Logger logger = Logger
			.getLogger(LidaXmlFactory.class.getCanonicalName());
	private Document dom;
	private Lida lida;
	private List<Object[]> toInitialize = new ArrayList<Object[]>();
	private List<Object[]> toAssociate = new ArrayList<Object[]>();
	private Map<String,TaskSpawner> taskSpawners = new HashMap<String,TaskSpawner>();

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaFactory#getLida()
	 */
	@Override
	public Lida getLida(Properties properties) { //Properties not used in this Factory
		
		String fileName = properties.getProperty("lida.factory.data",DEFAULT_FILE_NAME);
		parseXmlFile(fileName);
		parseDocument();
		lida.init();
		return lida;
	}

	private void parseXmlFile(String fileName) {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			dom = db.parse(fileName);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.close();
			logger.log(Level.WARNING, sw.getBuffer().toString(), 0L);
		}
	}

	private void parseDocument() {
		// get the root element
		Element docEle = dom.getDocumentElement();

		LidaTaskManager tm = getTaskManager(docEle);
		lida = new LidaImpl(tm);
		
		getTaskSpawners(docEle);
		
		for (LidaModule lm : getModules(docEle)) {
			lida.addSubModule(lm);
		}
		getListeners(docEle);

		associateModules();
		initializeModules();
	}
	
	private List<LidaModule> getModules(Element element) {
		List<LidaModule> modules = new ArrayList<LidaModule>();
		NodeList nl = element.getElementsByTagName("submodules");
		if (nl != null && nl.getLength() > 0) {
			Element modulesElemet = (Element) nl.item(0);
			List<Element> list = XmlUtils.getChildren(modulesElemet,"module");
			if (list != null && list.size() > 0) {
				for (Element moduleElement:list) {					
					LidaModule module = getModule(moduleElement);
					if(module != null){
						modules.add(module);
					}
				}
			}
		}
		return modules;
	}
	
	private void getTaskSpawners(Element element) {
		NodeList nl = element.getElementsByTagName("taskspawners");
		if (nl != null && nl.getLength() > 0) {
			Element modulesElemet = (Element) nl.item(0);
			List<Element> list = XmlUtils.getChildren(modulesElemet,"taskspawner");
			if (list != null && list.size() > 0) {
				for (Element moduleElement:list) {					
					getTaskSpawner(moduleElement);
				}
			}
		}
	}
	private void getTaskSpawner(Element moduleElement) {
		TaskSpawner ts = null;
		String className = XmlUtils.getTextValue(moduleElement, "class");
		String name = moduleElement.getAttribute("name");
		try {
			ts = (TaskSpawner) Class.forName(className).newInstance();
		} catch (Exception e) {
			logger.log(Level.WARNING, "TaskSpawner class: " + className
					+ " is not valid.", 0L);
			return;
		}
		
		ts.setTaskManager(lida.getTaskManager());
		Map<String,Object> params = XmlUtils.getTypedParams(moduleElement);
		ts.init(params);
		taskSpawners.put(name, ts);
		logger.log(Level.INFO, "TaskSpawner: " + name + " added.", 0L);
	}
	
	private LidaModule getModule(Element moduleElement) {
		LidaModule module = null;
		String className = XmlUtils.getTextValue(moduleElement, "class");
		String name = moduleElement.getAttribute("name");
		ModuleName moduleName = ModuleName.NoModule;
		try {
			moduleName = Enum.valueOf(ModuleName.class, name);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "ModuleName: " + name + " is not valid.",
					0L);
			return null;
		}
		try {
			module = (LidaModule) Class.forName(className).newInstance();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Module class name: " + className
					+ " is not valid.  Check module class name.", 0L);
			return null; 
		}
		module.setModuleName(moduleName);
		String taskspawner = XmlUtils.getTextValue(moduleElement,"taskspawner");
		TaskSpawner ts = taskSpawners.get(taskspawner);
	
		if (ts!=null) {
			module.setAssistingTaskSpawner(ts);
			List<LidaTask>initialTasks = getTasks(moduleElement);
			ts.setInitialTasks(initialTasks);
		}else{
			logger.log(Level.WARNING, "Module: " + name + " illegal TaskSpawner definition.", 0L);			
		}
		
		Map<String,Object> params = XmlUtils.getTypedParams(moduleElement);
		try{
			module.init(params);
		}catch(Exception e){
			logger.log(Level.WARNING, "Module: " + name + " threw exception " + e + " during call to init()", 
							LidaTaskManager.getActualTick());
			e.printStackTrace();
		}
		for (LidaModule lm : getModules(moduleElement)) {
			module.addSubModule(lm);
		}
		String classInit = XmlUtils.getTextValue(moduleElement,
				"initializerclass");
		if (classInit != null) {
			toInitialize.add(new Object[] { module, classInit, params});
		}
		getAssociatedModules(moduleElement, module);
		
		logger.log(Level.INFO, "Module: " + name + " added.", 0L);
		return module;
	}

	/**
	 * @param moduleElement
	 * @param initializable
	 */
	private void getAssociatedModules(Element moduleElement, Initializable initializable) {
		NodeList nl = moduleElement.getElementsByTagName("associatedmodule");
		if (nl != null && nl.getLength() > 0) {
			
			for (int i = 0; i < nl.getLength(); i++) {
				String assocMod=XmlUtils.getValue((Element) nl.item(i));
				toAssociate.add(new Object[]{initializable,assocMod});
			}
		}
	}
	
	/**
	 * Iterates through the module associated module pairs and associates them
	 */
	private void associateModules() {
		ModuleName moduleName = ModuleName.NoModule;
		for (Object[] vals : toAssociate) {
			FullyInitializable ini = (FullyInitializable) vals[0];
			String assocModule = (String) vals[1];
			try {
				moduleName = Enum.valueOf(ModuleName.class, assocModule);
			} catch (Exception e) {
				logger.log(Level.WARNING,
					"Module associated module name: " + assocModule + " is not valid.", 0L);
				break;
			}
			LidaModule module=lida.getSubmodule(moduleName);
		
			ini.setAssociatedModule(module, ModuleUsage.NOT_SPECIFIED);
			logger.log(Level.INFO, "Module: " + assocModule + " associated.", 0L);
		}//for
	}
	
	private void initializeModules() {
		for (Object[] vals : toInitialize) {
			Initializable ini = (Initializable) vals[0];
			String classInit = (String) vals[1];
			@SuppressWarnings("unchecked")
			Map<String,?> p = (Map<String,Object>) vals[2];
			Initializer initializer = null;
			try {
				initializer = (Initializer) Class.forName(classInit).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			initializer.initModule(ini, lida, p);
		}
	}

	private LidaTaskManager getTaskManager(Element element) {
		NodeList nl = element.getElementsByTagName("taskmanager");
		Element moduleElement=null;
		if (nl != null && nl.getLength() > 0) {
			 moduleElement = (Element) nl.item(0);
		}
		Map<String,Object> params = XmlUtils.getTypedParams(moduleElement);
		
		Object t = params.get("taskManager.tickDuration");
		Object m = params.get("taskManager.maxNumberOfThreads");
		
		Integer tickDuration = null; 
		Integer maxNumberOfThreads = null;
		
		if(t instanceof String){
			tickDuration = Integer.parseInt((String) t);
		}else if(t instanceof Integer){
			tickDuration = (Integer)t;
		}else{
			logger.warning("Could not load tick duration. using default");
		}
		
		if(m instanceof String){
			maxNumberOfThreads = Integer.parseInt((String) m);
		}else if(m instanceof Integer){
			maxNumberOfThreads = (Integer)m;
		}else{
			logger.warning("Could not load max no of threads, using default");
		}
		
		if (tickDuration==null){
			tickDuration=DEFAULT_TICK_DURATION;
		}
		if(maxNumberOfThreads==null){
			 maxNumberOfThreads=DEFAULT_NUMBER_OF_THREADS;			
		}		
		LidaTaskManager taskManager = new LidaTaskManager(tickDuration, maxNumberOfThreads);

		return taskManager;
	}

	
	private List<LidaTask> getTasks(Element element) {
		List<LidaTask> tasks = new ArrayList<LidaTask>();
		NodeList nl = element.getElementsByTagName("initialTasks");

		if (nl != null && nl.getLength() > 0) {
			Element modulesElemet = (Element) nl.item(0);
			nl = modulesElemet.getElementsByTagName("task");
			if (nl != null && nl.getLength() > 0) {
				for (int i = 0; i < nl.getLength(); i++) {
					Element moduleElement = (Element) nl.item(i);
					LidaTask task = getTask(moduleElement);
					tasks.add(task);
				}
			}
		}
		return tasks;
	}
	
	private LidaTask getTask(Element moduleElement) {
		LidaTask task = null;
		String className = XmlUtils.getTextValue(moduleElement, "class");
		String name = moduleElement.getAttribute("name");
		int ticks = XmlUtils.getIntValue(moduleElement, "ticksperrun");
		try {
			task = (LidaTask) Class.forName(className).newInstance();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Task class: " + className
					+ " is not valid.", 0L);
			return null;
		}
		task.setNumberOfTicksPerRun(ticks);
		Map<String,Object> params = XmlUtils.getTypedParams(moduleElement);
		task.init(params);
		getAssociatedModules(moduleElement, task);

		logger.log(Level.INFO, "Task: " + name + " added.", 0L);
		return task;
	}

	private void getListeners(Element element) {
		NodeList nl = element.getElementsByTagName("listeners");
		if (nl != null && nl.getLength() > 0) {
			Element modulesElemet = (Element) nl.item(0);
			nl = modulesElemet.getElementsByTagName("listener");
			if (nl != null && nl.getLength() > 0) {
				for (int i = 0; i < nl.getLength(); i++) {
					Element moduleElement = (Element) nl.item(i);
					getListener(moduleElement);
				}
			}
		}
		return;
	}

	private void getListener(Element moduleElement) {
		Class<?> listenerClass = null;
		String listenerType = XmlUtils.getTextValue(moduleElement,
				"listenertype");
		String name = XmlUtils.getTextValue(moduleElement, "modulename");
		String listenername = XmlUtils.getTextValue(moduleElement,
				"listenername");

		try {
			listenerClass = Class.forName(listenerType);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Listener type class: " + listenerType
					+ " is not valid.", 0L);
			return;
		}

		ModuleName moduleName = ModuleName.NoModule;
		try {
			moduleName = Enum.valueOf(ModuleName.class, name);
		} catch (Exception e) {
			logger.log(Level.WARNING,
					"Module name: " + name + " is not valid.", 0L);
			return;
		}
		LidaModule module = lida.getSubmodule(moduleName);

		ModuleName listenerMN = ModuleName.NoModule;
		ModuleListener listener = null;
		try {
			listenerMN = Enum.valueOf(ModuleName.class, listenername);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Listener name: " + listenername
					+ " is not valid.", 0L);
			return;
		}

		try {
			listener = (ModuleListener) lida.getSubmodule(listenerMN);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Listener: " + listenername
					+ " is not a valid ModuleListener.", 0L);
			return;
		}

		if (module != null && listener != null
				&& listenerClass.isInstance(listener)) {
			module.addListener(listener);
			logger.log(Level.INFO, "Listener type: "+listenerType + listener + " -> " + module + " added.", 0L);
		} else {
			logger.log(Level.WARNING, "Listener: " + listenername
					+ " is not a valid " + listenerType + " listener.", 0L);
			return;
		}
	}
}
