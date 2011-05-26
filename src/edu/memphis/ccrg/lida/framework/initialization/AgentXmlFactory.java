/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.AgentImpl;
import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * Creates and returns a {@link Agent} Object based on an XML file.
 * 
 * Each module that is created is instantiated using {@link Class#forName(String)}. 
 * Next its {@link FrameworkModule#init()} method is run.  After all modules have been 
 * created in this way, all associated modules are added for each module {@link FrameworkModule#setAssociatedModule(FrameworkModule, String)
 * } Finally each module's initializer is run if it has one.  Thus associated modules should not be used by the init method of modules, 
 * only constants and other variables should be set.
 *   
 * @author Javier Snaider, Ryan J. McCall
 * 
 */ 
public class AgentXmlFactory implements AgentFactory {
	//TODO consider 'postInit' method that runs after initializers run.
	//This will allow objects created in the initial pass, e.g. Nodes, AgentActions, to be used by other 
	//modules initialized before the objects were created.
	private static final Logger logger = Logger.getLogger(AgentXmlFactory.class.getCanonicalName());
	
	private static final String DEFAULT_XML_FILE_PATH = "configs/agent.xml";
	private static final String DEFAULT_SCHEMA_FILE_PATH = "edu/memphis/ccrg/lida/framework/initialization/config/LidaXMLSchema.xsd";

	@Override
	public Agent getAgent(Properties properties) {
		String fileName = DEFAULT_XML_FILE_PATH;
		if(properties != null){
			fileName = properties.getProperty("lida.factory.data",DEFAULT_XML_FILE_PATH);
		}else{
			logger.log(Level.WARNING, "Properties was null using default agent XML file path");
		}
		
		Document dom = XmlUtils.parseXmlFile(fileName, DEFAULT_SCHEMA_FILE_PATH);
		Agent agent = parseDocument(dom);
		return agent;
	}

	/**
	 * Parses the xml document creating the TaskManager, TaskSpawners, Modules, submodules.  Sets up listeners
	 * and associates modules.
	 */
	Agent parseDocument(Document dom) {
		if(dom == null){
			logger.log(Level.SEVERE, "Document dom was null. Cannot parse it");
			return null;
		}
		
		Agent agent=null;
		TaskManager tm;
		List<FrameworkModule> modules;
		Map<String,TaskSpawner> taskSpawners;
		List<Object[]> toInitialize = new ArrayList<Object[]>();
		List<Object[]> toAssociate = new ArrayList<Object[]>();
		
		// get the root element
		Element docEle = dom.getDocumentElement();

		tm = getTaskManager(docEle);
		logger.log(Level.INFO, "Finished obtaining TaskManager\n", 0L);
		agent = new AgentImpl(tm);
		
		taskSpawners=getTaskSpawners(docEle,tm);
		logger.log(Level.INFO, "Finished creating TaskSpawners\n", 0L);
		
		modules = getModules(docEle,toAssociate,toInitialize,taskSpawners);
		for (FrameworkModule frameworkModule :modules) {
			agent.addSubModule(frameworkModule);
		}
		logger.log(Level.INFO, "Finished creating modules and submodules\n", 0L);
		
		getListeners(docEle,agent);
		logger.log(Level.INFO, "Finished setting up listeners\n", 0L);

		associateModules(toAssociate,agent);
		logger.log(Level.INFO, "Finished associating modules\n", 0L);
		
		initializeModules(agent,toInitialize);
		logger.log(Level.INFO, "Finished initializing modules\n", 0L);
		agent.init();
		
		return agent;
	}
	
	/**
	 * @param element Element containing the task manager
	 * @return {@link TaskManager}
	 */
	TaskManager getTaskManager(Element element) {		
		List<Element> nl = XmlUtils.getChildren(element,"taskmanager");
		Element taskManagerElement=null;
		if (nl != null && nl.size() > 0) {
			 taskManagerElement = nl.get(0);
		}
		Map<String,Object> params = XmlUtils.getTypedParams(taskManagerElement);
		
		Object t = params.get("taskManager.tickDuration");
		Object m = params.get("taskManager.maxNumberOfThreads");
		
		Integer tickDuration = null; 
		Integer maxNumberOfThreads = null;

		if(t instanceof String){
			try{
			tickDuration = Integer.parseInt((String) t);
			}catch (NumberFormatException e){
				logger.warning("Could not load tick duration. using default");				
			}
		}else if(t instanceof Integer){
			tickDuration = (Integer)t;
		}else{
			logger.warning("Could not load tick duration. using default");
		}
		
		if(m instanceof String){
			try{
			maxNumberOfThreads = Integer.parseInt((String) m);
			}catch (NumberFormatException e){
				logger.warning("Could not load max no of threads, using default");				
			}
		}else if(m instanceof Integer){
			maxNumberOfThreads = (Integer)m;
		}else{
			logger.warning("Could not load max no of threads, using default");
		}
		
		if (tickDuration==null){
			tickDuration=TaskManager.DEFAULT_TICK_DURATION;
		}
		if(maxNumberOfThreads==null){
			 maxNumberOfThreads=TaskManager.DEFAULT_NUMBER_OF_THREADS;			
		}		
		TaskManager taskManager = new TaskManager(tickDuration, maxNumberOfThreads);

		return taskManager;
	}
	
	/**
	 * Reads in and creates all {@link TaskSpawner}s specified in {@link Element}
	 * @param element Dom element
	 * @param tm the {@link TaskManager}
	 */
	Map<String,TaskSpawner> getTaskSpawners(Element element, TaskManager tm) {
		Map<String,TaskSpawner>spawners = new HashMap<String, TaskSpawner>();
		List<Element> elementList = XmlUtils.getChildren(element,"taskspawners");
		if (elementList != null && elementList.size() > 0) {
			Element taskSpawnersElement = elementList.get(0);
			List<Element> list = XmlUtils.getChildren(taskSpawnersElement,"taskspawner");
			if (list != null && list.size() > 0) {
				for (Element taskSpawnerElement:list) {					
					getTaskSpawner(taskSpawnerElement,tm,spawners);
				}
			}
		}
		return spawners;
	}
	/**
	 * Creates a {@link TaskSpawner} from specified {@link Element} and {@link TaskManager}
	 * @param element Dom element
	 * @param tm the {@link TaskManager}
	 * @param spawners The msp of {@link TaskSpawner}s where the new {@link TaskSpawner} is included
	 */
	void getTaskSpawner(Element element, TaskManager tm,Map<String,TaskSpawner>spawners) {
		TaskSpawner ts = null;
		String className = XmlUtils.getTextValue(element, "class");
		String name = element.getAttribute("name").trim();
		try {
			ts = (TaskSpawner) Class.forName(className).newInstance();
		}
		catch(ClassNotFoundException e){
			logger.log(Level.SEVERE, "Module class name: " + className + 
						" is not found.  Check TaskSpawner class name.\n", 0L);
		}catch (Exception e) {
			logger.log(Level.SEVERE, "Exception \"" + e.toString() + 
					"\" occurred during creation of object of class " + className + "\n", 0L);
			return;
		}
		
		ts.setTaskManager(tm);
		Map<String,Object> params = XmlUtils.getTypedParams(element);
		try{
			ts.init(params);
		}catch(Exception e){
			logger.log(Level.SEVERE, "Error initializing  task spawner: " + ts.toString(), 0L);
			e.printStackTrace();
		}
		spawners.put(name, ts);
		logger.log(Level.INFO, "TaskSpawner: " + name + " added.", 0L);
	}
	
	/**
	 * Reads and creates all {@link FrameworkModule}s in specified element
	 * @param element dom element
	 * @return {@link FrameworkModule}s
	 */
	List<FrameworkModule> getModules(Element element,List<Object[]>toAssoc,List<Object[]>toInit, Map<String, TaskSpawner>spawners) {
		List<FrameworkModule> modules = new ArrayList<FrameworkModule>();
		List<Element> nl = XmlUtils.getChildren(element, "submodules");
		if (nl != null && nl.size() > 0) {
			Element submoduleElement = nl.get(0);
			List<Element> list = XmlUtils.getChildren(submoduleElement,"module");
			if (list != null && list.size() > 0) {
				for (Element moduleElement : list) {					
					FrameworkModule module = getModule(moduleElement,toAssoc,toInit,spawners);
					if(module != null){
						modules.add(module);
					}
				}
			}
		}
		return modules;
	}
	/**
	 * Reads and creates a {@link FrameworkModule} in specified moduleElement
	 * @param moduleElement dom element
	 * @return {@link FrameworkModule}
	 */
	FrameworkModule getModule(Element moduleElement,List<Object[]>toAssoc,List<Object[]>toInit, Map<String, TaskSpawner> spawners) {
		//Get module name and class name
		FrameworkModule module = null;
		String className = XmlUtils.getTextValue(moduleElement, "class");
		String name = moduleElement.getAttribute("name").trim();
		ModuleName moduleName = ModuleName.NoModule;
		try {
			moduleName = ModuleName.addModuleName(name);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "ModuleName: " + name + " is not valid.", 0L);
			return null;
		}
		//Create module.
		try {
			module = (FrameworkModule) Class.forName(className).newInstance();
		} catch (Exception e) {
			if(e instanceof ClassNotFoundException){
				logger.log(Level.SEVERE, "Module class name: " + className + 
							" is not valid.  Check module class name.\n", 0L);
			}else{
				logger.log(Level.SEVERE, "Exception \"" + e.toString() + 
						"\" occurred during creation of object of class " + className + "\n", 0L);
				e.printStackTrace();
			}
			return null; 
		}
		module.setModuleName(moduleName);
		
		//Set up module's Taskspawner and initial tasks.
		String taskspawner = XmlUtils.getTextValue(moduleElement,"taskspawner");
		TaskSpawner ts = spawners.get(taskspawner);
		if (ts != null) {
			module.setAssistingTaskSpawner(ts);
			List<FrameworkTask>initialTasks = getTasks(moduleElement,toAssoc);
			ts.addTasks(initialTasks);
		}else{
			logger.log(Level.WARNING, "Illegal TaskSpawner definition for module: " + name, 0L);			
		}
		
		//Get and add all submodules.
		for (FrameworkModule lm : getModules(moduleElement,toAssoc,toInit, spawners)) {
			module.addSubModule(lm);
		}
		
		//Get parameters specified for this module
		Map<String, Object> params = XmlUtils.getTypedParams(moduleElement);
		//Initialize module's parameters.
		try{
			module.init(params);
		}catch(Exception e){
			logger.log(Level.WARNING, "Module: " + name + " threw exception " + e + " during call to init()", 
							TaskManager.getCurrentTick());
			e.printStackTrace();
		}
		
		//Setup the user-specified Initializer that will run later to perform
		// custom initialization of the module. 
		String classInit = XmlUtils.getTextValue(moduleElement,	"initializerclass");
		if (classInit != null) {
			toInit.add(new Object[] { module, classInit, params});
		}
		
		//Gathers all 'associatedmodules' for this module.  To be added later.
		getAssociatedModules(moduleElement, module,toAssoc);
		
		logger.log(Level.INFO, "Module: " + name + " added.", 0L);
		return module;
	}
	
	/**
	 * Reads and creates {@link FrameworkTask}s specified in element
	 * @param element dom element
	 * @return a list of {@link FrameworkTask}s
	 */
	List<FrameworkTask> getTasks(Element element,List<Object[]>toAssoc) {
		List<FrameworkTask> tasks = new ArrayList<FrameworkTask>();
		List<Element> nl = XmlUtils.getChildren(element,"initialTasks");
		if (nl != null && nl.size() > 0) {
			Element initialTasksElement =  nl.get(0);
			nl = XmlUtils.getChildren(initialTasksElement,"task");
			if (nl != null && nl.size() > 0) {
				for (Element taskElement:nl) {
					FrameworkTask task = getTask(taskElement,toAssoc);
					if(task != null){
						tasks.add(task);
					}
				}
			}
		}
		return tasks;
	}
	
	/**
	 * Reads and creates {@link FrameworkTask} specified in element
	 * @param element dom element
	 * @return a {@link FrameworkTask}
	 */
	FrameworkTask getTask(Element moduleElement,List<Object[]>toAssoc) {
		FrameworkTask task = null;
		String className = XmlUtils.getTextValue(moduleElement, "class");	
		String name = moduleElement.getAttribute("name").trim();
		try {
			task = (FrameworkTask) Class.forName(className).newInstance();
		} catch(ClassNotFoundException e){
			logger.log(Level.SEVERE, "Framework Task class name: " + className + 
					" not found.  Check class name.\n", 0L);
			return null;
		}catch (Exception e) {
			logger.log(Level.SEVERE, "Exception \"" + e.toString() + 
				"\" occurred during creation of object of class " + className + "\n", 0L);
			return null;
		}
		
		Integer ticks = XmlUtils.getIntegerValue(moduleElement, "ticksperrun");
		if(ticks != null){
			task.setTicksPerStep(ticks);
		}else{
			logger.log(Level.WARNING, "Cannot set ticksPerRun for task \"" + name + "\". Using default ticksPerRun.");
		}
		
		Map<String,Object> params = XmlUtils.getTypedParams(moduleElement);
		task.init(params);
		getAssociatedModules(moduleElement, task,toAssoc);

		logger.log(Level.INFO, "Task: " + name + " added.", 0L);
		return task;
	}

	/**
	 * Gets associated modules of the specified {@link Initializable}
	 * @param element
	 * @param initializable
	 */
	void getAssociatedModules(Element element, Initializable initializable,List<Object[]>toAssoc) {
		List<Element> nl = XmlUtils.getChildren(element,"associatedmodule");
		if (nl != null && nl.size() > 0) {
			for (Element assocModuleElement:nl ) {
				String assocMod=XmlUtils.getValue(assocModuleElement);
				String function = element.getAttribute("function").trim();
				toAssoc.add(new Object[]{initializable,assocMod,function});
			}
		}
	}
	
	/**
	 * Reads and creates all listeners specified in element.
	 * @param element dom element
	 * 
	 */
	void getListeners(Element element, FrameworkModule topModule) {
		List<Element> childrenList = XmlUtils.getChildren(element,"listeners");
		if (childrenList != null && childrenList.size() > 0) {
			Element listenersElement = (Element) childrenList.get(0);
			childrenList = XmlUtils.getChildren(listenersElement,"listener");
			if (childrenList != null) {
				for (Element listenerElement:childrenList) {
					getListener(listenerElement,topModule);
				}
			}
		}
		return;
	}
	
	/**
	 * Reads and creates a listener specified in element.
	 * @param element dom element
	 * 
	 */	
	void getListener(Element moduleElement,FrameworkModule topModule) {
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

		ModuleName moduleName;
		
		moduleName = ModuleName.getModuleName(name);
		if (moduleName==null) {
			logger.log(Level.WARNING,
					"Module name: " + name + " is not valid.", 0L);
			return;
		}
		FrameworkModule module = topModule.getSubmodule(moduleName);

		ModuleName listenerModuleName;
		
		listenerModuleName = ModuleName.getModuleName(listenername);
		if (listenerModuleName==null) {
			logger.log(Level.WARNING, "Listener name: " + listenername
					+ " is not valid.", 0L);
			return;
		}

		ModuleListener listener = null;
		FrameworkModule listenerModule = topModule.getSubmodule(listenerModuleName);
		if(listenerModule == null){
			logger.log(Level.WARNING, "Could not find listener module " + listenerModuleName + 
						" listener will not be set up", 0L);
			return;
		}else if(listenerModule instanceof ModuleListener){
			listener = (ModuleListener) listenerModule; 
		}else{
			logger.log(Level.WARNING, "Listener: " + listenerModule.toString()
					+ " is not a ModuleListener i.e. doesn't implement any listeners. Listener will not be set up.", 0L);
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

	/**
	 * Iterates through the module/associated-module pairs and associates them
	 */
	void associateModules(List<Object[]>toAssoc, FrameworkModule topModule) {
		ModuleName moduleName;
		for (Object[] vals : toAssoc) {
			FullyInitializable initializable = (FullyInitializable) vals[0];
			String assocModule = (String) vals[1];
				moduleName = ModuleName.getModuleName(assocModule);
			if (moduleName==null) {
				logger.log(Level.WARNING,
					"Module associated module name: " + assocModule + " is not valid.", 0L);
				break;
			}
			FrameworkModule module=topModule.getSubmodule(moduleName);
			if(module != null){
				String function = ModuleUsage.NOT_SPECIFIED;
				if(vals[2]!=null){
					function=(String) vals[2];
				}
				initializable.setAssociatedModule(module, function);
			}else{
				logger.log(Level.SEVERE, 
						"Could not obtain " + module + ".  Module will NOT be associated to " + initializable, 
						0L);
			}
			logger.log(Level.INFO, "Module: " + assocModule + " associated.", 0L);
		}
	}
	
	/**
	 * For all modules with an initializer, run the initializer passing in the specific module.
	 */
	void initializeModules(Agent topModule,List<Object[]>toInit) {
		//TODO change first parameter to FrameworkModule
		for (Object[] vals : toInit) {
			FullyInitializable moduleToInitialize = (FullyInitializable) vals[0];
			String initializerClassName = (String) vals[1];
			@SuppressWarnings("unchecked")
			Map<String,?> params = (Map<String,?>) vals[2];
			Initializer initializer = null;
			try {
				initializer = (Initializer) Class.forName(initializerClassName).newInstance();
			}catch(ClassNotFoundException e){
				logger.log(Level.SEVERE, "Initializer class name: " + initializerClassName + 
							" not found.  Check class name.\n", 0L);
			}catch (Exception e) {
				logger.log(Level.SEVERE, "Exception \"" + e.toString() + 
						"\" occurred during creation of object of class " + initializerClassName + "\n", 0L);
				return;
			}
			
			if(initializer != null){
				try{
					logger.log(Level.INFO, "Initializing " + 
							((FrameworkModule) moduleToInitialize).getModuleName() + 
							" module using initializer " + 
							initializer.getClass().getCanonicalName() + "\n");
					initializer.initModule(moduleToInitialize, topModule, params);
				}catch (Exception e){
					logger.log(Level.SEVERE, "Exception occurred running initializer: " + initializerClassName , TaskManager.getCurrentTick());
					e.printStackTrace();
				}
			}
		}
	}
}
