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
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * Creates and returns a Lida Object based on an XML file.
 * 
 * Each module that is created is instantiated using {@link Class#forName(String)}. 
 * Next its {@link LidaModule#init()} method is run.  After all modules have been 
 * created in this way, all associated modules are added for each module {@link LidaModule#setAssociatedModule(LidaModule, String)
 * } Finally each module's initializer is run if it has one.  Thus associated modules should not be used by the init method of modules, 
 * only constants and other variables should be set.
 *   
 * 
 * @author Javier Snaider, Ryan J. McCall
 * 
 */ 
public class LidaXmlFactory implements LidaFactory {
//	TODO consider 'postInit' method that runs after initializers run.
	//This will allow objects created in the initial pass, e.g. Nodes, LidaActions, to be used by other 
	//modules initialized before the objects were created.
	private static final Logger logger = Logger.getLogger(LidaXmlFactory.class.getCanonicalName());
	
	private static final String DEFAULT_XML_FILE_PATH = "configs/lida.xml";
	private static final String DEFAULT_SCHEMA_FILE_PATH = "configs/LidaXMLSchema.xsd";
	private static final int DEFAULT_TICK_DURATION = 10;
	private static final int DEFAULT_NUMBER_OF_THREADS = 20;
	
	private Document dom;
	private Lida lida;
	private List<Object[]> toInitialize = new ArrayList<Object[]>();
	private List<Object[]> toAssociate = new ArrayList<Object[]>();
	private Map<String,TaskSpawner> taskSpawners = new HashMap<String,TaskSpawner>();

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaFactory#getLida()
	 */
	@Override
	public Lida getLida(Properties properties) { 
		String fileName = properties.getProperty("lida.factory.data",DEFAULT_XML_FILE_PATH);
		parseXmlFile(fileName);
		parseDocument();
		lida.init();
		return lida;
	}

	/**
	 * Verifies and parses specified xml file into a {@link Document}.
	 * @param fileName
	 */
	private void parseXmlFile(String fileName) {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			if(XmlUtils.validateXmlFile(fileName, DEFAULT_SCHEMA_FILE_PATH)){
				dom = db.parse(fileName);
			}else{
				logger.log(Level.SEVERE, "Lida XML file is invalid.", LidaTaskManager.getCurrentTick());
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			logger.log(Level.WARNING, sw.getBuffer().toString(), 0L);
			
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.close();
		}
	}

	/**
	 * Parses the xml document creating the TaskManager, TaskSpawners, Modules, submodules.  Sets up listeners
	 * and associates modules.
	 */
	private void parseDocument() {
		// get the root element
		Element docEle = dom.getDocumentElement();

		LidaTaskManager tm = getTaskManager(docEle);
		logger.log(Level.INFO, "Finished obtaining TaskManager\n", 0L);
		lida = new LidaImpl(tm);
		
		getTaskSpawners(docEle);
		logger.log(Level.INFO, "Finished creating TaskSpawners\n", 0L);
		
		for (LidaModule lm : getModules(docEle)) {
			lida.addSubModule(lm);
		}
		logger.log(Level.INFO, "Finished creating modules and submodules\n", 0L);
		
		getListeners(docEle);
		logger.log(Level.INFO, "Finished setting up listeners\n", 0L);

		associateModules();
		logger.log(Level.INFO, "Finished associating modules\n", 0L);
		
		initializeModules();
	}
	
	/**
	 * @param element Element containing the task manager
	 * @return {@link LidaTaskManager}
	 */
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
	
	/**
	 * Reads in and creates all task spawners specified in {@link Element}
	 * @param element
	 */
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
		}
		catch(ClassNotFoundException e){
			logger.log(Level.SEVERE, "Module class name: " + className + 
						" is not found.  Check TaskSpawner class name.\n", 0L);
		}catch (Exception e) {
			logger.log(Level.SEVERE, "Exception \"" + e.toString() + 
					"\" occurred during creation of object of class " + className + "\n", 0L);
			return;
		}
		
		ts.setTaskManager(lida.getTaskManager());
		Map<String,Object> params = XmlUtils.getTypedParams(moduleElement);
		try{
			ts.init(params);
		}catch(Exception e){
			logger.log(Level.SEVERE, "Error initializing  task spawner: " + ts.toString(), 0L);
			e.printStackTrace();
		}
		taskSpawners.put(name, ts);
		logger.log(Level.INFO, "TaskSpawner: " + name + " added.", 0L);
	}
	
	/**
	 * Reads and creates all {@link LidaModule}s in specified element
	 * @param element dom element
	 * @return {@link LidaModule}s
	 */
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
	private LidaModule getModule(Element moduleElement) {
		//Get module name and class name
		LidaModule module = null;
		String className = XmlUtils.getTextValue(moduleElement, "class");
		String name = moduleElement.getAttribute("name");
		ModuleName moduleName = ModuleName.NoModule;
		try {
			moduleName = ModuleName.addModuleName(name);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "ModuleName: " + name + " is not valid.", 0L);
			return null;
		}
		//Create module.
		try {
			module = (LidaModule) Class.forName(className).newInstance();
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
		TaskSpawner ts = taskSpawners.get(taskspawner);
		if (ts != null) {
			module.setAssistingTaskSpawner(ts);
			List<LidaTask>initialTasks = getTasks(moduleElement);
			ts.addTasks(initialTasks);
		}else{
			logger.log(Level.WARNING, "Illegal TaskSpawner definition for module: " + name, 0L);			
		}
		
		//Get and add all submodules.
		for (LidaModule lm : getModules(moduleElement)) {
			module.addSubModule(lm);
		}
		
		//Get parameters specified for this module
		Map<String, Object> params = XmlUtils.getTypedParams(moduleElement);
		//Initialize module's parameters.
		try{
			module.init(params);
		}catch(Exception e){
			logger.log(Level.WARNING, "Module: " + name + " threw exception " + e + " during call to init()", 
							LidaTaskManager.getCurrentTick());
			e.printStackTrace();
		}
		
		//Setup the user-specified Initializer that will run later to perform
		// custom initialization of the module. 
		String classInit = XmlUtils.getTextValue(moduleElement,	"initializerclass");
		if (classInit != null) {
			toInitialize.add(new Object[] { module, classInit, params});
		}
		
		//Gathers all 'associatedmodules' for this module.  To be added later.
		getAssociatedModules(moduleElement, module);
		
		logger.log(Level.INFO, "Module: " + name + " added.", 0L);
		return module;
	}
	
	/**
	 * Reads and creates {@link LidaTask}s specified in element
	 * @param element
	 * @return
	 */
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
					if(task != null){
						tasks.add(task);
					}
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
		} catch(ClassNotFoundException e){
			logger.log(Level.SEVERE, "Lida Task class name: " + className + 
					" not found.  Check class name.\n", 0L);
			return null;
		}catch (Exception e) {
			logger.log(Level.SEVERE, "Exception \"" + e.toString() + 
				"\" occurred during creation of object of class " + className + "\n", 0L);
			return null;
		}

		task.setTicksPerStep(ticks);
		Map<String,Object> params = XmlUtils.getTypedParams(moduleElement);
		task.init(params);
		getAssociatedModules(moduleElement, task);

		logger.log(Level.INFO, "Task: " + name + " added.", 0L);
		return task;
	}

	/**
	 * Gets associated modules of the specified {@link Initializable}
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
	 * Reads and creates all listeners specified in element.
	 * @param element
	 * 
	 */
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

		ModuleName moduleName;
		
			moduleName = ModuleName.getModuleName(name);
		if (moduleName==null) {
			logger.log(Level.WARNING,
					"Module name: " + name + " is not valid.", 0L);
			return;
		}
		LidaModule module = lida.getSubmodule(moduleName);

		ModuleName listenerModuleName;
		
			listenerModuleName = ModuleName.getModuleName(listenername);
		if (listenerModuleName==null) {
			logger.log(Level.WARNING, "Listener name: " + listenername
					+ " is not valid.", 0L);
			return;
		}

		ModuleListener listener = null;
		LidaModule listenerModule = lida.getSubmodule(listenerModuleName);
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
	private void associateModules() {
		ModuleName moduleName;
		for (Object[] vals : toAssociate) {
			FullyInitializable initializable = (FullyInitializable) vals[0];
			String assocModule = (String) vals[1];
				moduleName = ModuleName.getModuleName(assocModule);
			if (moduleName==null) {
				logger.log(Level.WARNING,
					"Module associated module name: " + assocModule + " is not valid.", 0L);
				break;
			}
			LidaModule module=lida.getSubmodule(moduleName);
			if(module != null){
				initializable.setAssociatedModule(module, ModuleUsage.NOT_SPECIFIED);
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
	private void initializeModules() {
		for (Object[] vals : toInitialize) {
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
					initializer.initModule(moduleToInitialize, lida, params);
				}catch (Exception e){
					logger.log(Level.SEVERE, "Exception occurred running initializer: " + initializerClassName , LidaTaskManager.getCurrentTick());
					e.printStackTrace();
				}
			}
		}
	}
}
