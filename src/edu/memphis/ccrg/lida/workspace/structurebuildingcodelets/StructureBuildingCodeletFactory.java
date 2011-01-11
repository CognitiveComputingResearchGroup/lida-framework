/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;

/**
 * The CodeletFactory is responsible for loading codelets dynamically. There are
 * also methods which will automatically add the new codelets to a scheduler.
 * This factory also recycles codelets to save creation time.
 *
 * Pattern: Factory, Singleton
 */
public class StructureBuildingCodeletFactory {
	
	/**
	 * Holds singleton instance
	 */
	private static StructureBuildingCodeletFactory instance;

	/**
	 * Pool keeping all recycled codelets.
	 * Key = codelet type
	 * Value = unused codelets of that type
	 */
	private Map<String, List<StructureBuildingCodelet>> pool;

	private String DefaultSBCodeletClassName;
	private String DefaultSBCodeletType;
	private Map<String, String> sbCodeletClasses = new HashMap<String, String>();

	private DecayStrategy defaultDecay;
	private ExciteStrategy defaultExcite;
	private Map<String, DecayStrategy> decays = new HashMap<String, DecayStrategy>();
	private Map<String, ExciteStrategy> excites = new HashMap<String, ExciteStrategy>();
	//
//	private WorkspaceBuffer episodicBuffer;
//	private WorkspaceBuffer broadcastQueue;
//	private CodeletAccessible csmReadable;	
//	private CodeletWritable csm;
	//
	private double defaultActivation = 1.0;	
	private NodeStructure defaultObjective = new NodeStructureImpl();
	private CodeletAction defaultActions = new BasicCodeletAction();
	
	/**
	 * Returns the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	static public StructureBuildingCodeletFactory getInstance() {
		if(instance == null)
			instance = new StructureBuildingCodeletFactory();
		return instance;
	}//constructor
	
	/**
	 * Builds a new pooling codelet factory
	 */
	public StructureBuildingCodeletFactory() {
		DefaultSBCodeletType = "SBCodeletImpl";
		DefaultSBCodeletClassName = "edu.memphis.ccrg.lida.workspace.structureBuildingCodelets." + DefaultSBCodeletType;
		sbCodeletClasses.put(DefaultSBCodeletType, DefaultSBCodeletClassName);
		//
		defaultDecay = new LinearDecayStrategy();
		defaultExcite = new DefaultExciteStrategy();
		pool = new HashMap<String, List<StructureBuildingCodelet>>();
//		episodicBuffer = workspace.getEpisodicBuffer();
//		broadcastQueue = workspace.getBroadcastQueue();
//		csmReadable = workspace.getCSM();
//		csm = workspace.getCSM();	
	}
	
	/**
	 * Decay Behavior
	 * @param name decay strategy name 
	 * @param decay DecayStrategy
	 */
	public void addDecayStrategy(String name, DecayStrategy decay) {
		decays.put(name, decay);
	}
	public void addExciteStrategy(String name, ExciteStrategy excite) {
		excites.put(name, excite);
	}
	
	/**
	 * @param name decay strategy name
	 * @return d
	 */
	public DecayStrategy getDecayStrategy(String name) {
		DecayStrategy d = decays.get(name);
		if (d == null) {
			d = defaultDecay;
		}
		return d;
	}

	public ExciteStrategy getExciteStrategy(String name) {
		ExciteStrategy d = excites.get(name);
		if (d == null) {
			d = defaultExcite;
		}
		return d;
	}	

	public void addSBCodeletType(String type, String className) {
		sbCodeletClasses.put(type, className);
	}

	/**
	 * Retrieves an empty codelet of the specified type. Tries to reuse objects
	 * in the pool.
	 * 
	 * @param type the class name of the desired codelet type
	 * @return A new empty codelet of the specified type
	 */
	@SuppressWarnings("unused")
	private StructureBuildingCodelet getGenericCodelet(int type) {
		StructureBuildingCodelet codelet = null;
		if(pool.containsKey(type)){
			List<StructureBuildingCodelet> codelets = pool.get(type);
			if(codelets != null)
				codelet = codelets.remove(0);
		}
		
		if(codelet == null){
			try {
				codelet = (StructureBuildingCodelet) Class.forName(DefaultSBCodeletClassName).newInstance();
				codelet.setExciteStrategy(defaultExcite);
				codelet.setDecayStrategy(defaultDecay);
				codelet.setActivation(defaultActivation);
				codelet.setSoughtContent(defaultObjective);
				codelet.setCodeletAction(defaultActions);			
				
//				if(type == PERCEPTUAL_TYPE){
//					codelet.addWritableModule(csm);
//				}else if(type == EPISODIC_TYPE){
//					codelet.addReadableBuffer(episodicBuffer.getBufferContent());
//					codelet.addWritableModule(csm);
//				}else if(type == BROADCAST_TYPE){
//					codelet.addReadableBuffer(broadcastQueue.getBufferContent());
//					codelet.addWritableModule(csm);
//				}else if(type == CSM_TYPE){
//					codelet.addReadableBuffer(csmReadable.getBufferContent());
//					codelet.addWritableModule(csm);
//				}else if(type == ALL_TYPE){
//					codelet.addReadableBuffer(episodicBuffer.getBufferContent());
//					codelet.addReadableBuffer(broadcastQueue.getBufferContent());
//					codelet.addReadableBuffer(csmReadable.getBufferContent());
//					codelet.addWritableModule(csm);
//				}
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			} catch (ClassNotFoundException e) {
			}			
		}//if
		return codelet;
	}//method
	
	//TODO: outstanding issues with whether there should be an Id and how to use
	//class for name if the object, here an SBC requires a paramter, in this case the TaskManager
//	public StructureBuildingCodelet getCodelet(int type){
//		return getGenericCodelet(type);
//	}
//
//	public StructureBuildingCodelet getCodelet(int type, double activation,
//			NodeStructure context, CodeletAction actions) {
//		StructureBuildingCodelet sbc = getGenericCodelet(type);
//		sbc.setActivation(activation);
//		sbc.setSoughtContent(context);
//		sbc.setCodeletAction(actions);
//		return sbc;
//	}
		
	/**
	 * Recycles the given codelet to be re-used later on, for quickly providing
	 * new empty codelets.
	 * 
	 * @param sbCode The codelet to be recycled
	 */
	public void recycle(StructureBuildingCodelet sbCode) {
		sbCode.reset();
		String type = sbCode.getClass().getName();
		if (!pool.containsKey(type))
			pool.put(type, new ArrayList<StructureBuildingCodelet>());
		pool.get(type).add(sbCode);
	}
	
}//class