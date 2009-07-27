package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.shared.strategies.BasicExciteBehavior;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.shared.strategies.LinearDecayBehavior;
import edu.memphis.ccrg.lida.workspace.main.Workspace;

/**
 * The CodeletFactory is responsible for loading codelets dynamically. There are
 * also methods which will automatically add the new codelets to a scheduler.
 * This factory also recycles codelets to save creation time.
 *
 * Pattern: Factory, Singleton
 */
public class SBCodeletFactory {
	
	public static final int PERCEPTUAL_TYPE = 0;
	public static final int EPISODIC_TYPE = 1;
	public static final int BROADCAST_TYPE = 2;
	public static final int CSM_TYPE = 3;
	public static final int ALL_TYPE = 4;	
	
	private static long codeletIdCount = 0;

	/**
	 * Holds singleton instance
	 */
	private static SBCodeletFactory instance;

	/**
	 * Pool keeping all recycled codelets.
	 * Key = codelet type
	 * Value = unused codelets of that type
	 */
	private Map<String, List<StructureBuildingCodelet>> pool;

	private String DefaultSBCodeletClassName;
	private String DefaultSBCodeletType;
	private Map<String, String> sbCodeletClasses = new HashMap<String, String>();

	private DecayBehavior defaultDecay;
	private ExciteBehavior defaultExcite;
	private Map<String, DecayBehavior> decays = new HashMap<String, DecayBehavior>();
	private Map<String, ExciteBehavior> excites = new HashMap<String, ExciteBehavior>();
	//
	//
	private List<NodeStructure> perceptualBuffer = new ArrayList<NodeStructure>();
	private List<NodeStructure> episodicBuffer = new ArrayList<NodeStructure>();
	private List<NodeStructure> broadcastQueue = new ArrayList<NodeStructure>();
	private List<NodeStructure> csmReadable = new ArrayList<NodeStructure>();	
	private CodeletWritable csm;
	//
	private double defaultActivation = 1.0;	
	private NodeStructure defaultObjective = new NodeStructureImpl();
	private CodeletAction defaultActions = new BasicCodeletAction();
	private LidaTaskManager timer;
	
	/**
	 * Returns the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	static public SBCodeletFactory getInstance(Workspace w, LidaTaskManager timer) {
		if(instance == null)
			instance = new SBCodeletFactory(w, timer);
		return instance;
	}//constructor
	
	/**
	 * Builds a new pooling codelet factory
	 * TODO: Are we going to use this?
	 * @post pool.size() == 0
	 */
	public SBCodeletFactory(Workspace workspace, LidaTaskManager timer) {
		DefaultSBCodeletType = "SBCodeletImpl";
		DefaultSBCodeletClassName = "edu.memphis.ccrg.lida.workspace.structureBuildingCodelets." + DefaultSBCodeletType;
		sbCodeletClasses.put(DefaultSBCodeletType, DefaultSBCodeletClassName);
		//
		defaultDecay = new LinearDecayBehavior();
		defaultExcite = new BasicExciteBehavior();
		pool = new HashMap<String, List<StructureBuildingCodelet>>();
		//TODO: TO FIX THIS!!!!!!!!!!!!
//		perceptualBuffer = workspace.getPerceptualBuffer();
//		episodicBuffer = workspace.getEpisodicBuffer();
//		broadcastQueue = workspace.getBroadcastQueue();
		csmReadable.add(workspace.getCSM().getModel());
		csm = workspace.getCSM();
		this.timer = timer;		
	}
	
	/**
	 * Decay Behavior
	 * @param name
	 * @param decay
	 */
	public void addDecayBehavior(String name, DecayBehavior decay) {
		decays.put(name, decay);
	}
	public void addExciteBehavior(String name, ExciteBehavior excite) {
		excites.put(name, excite);
	}
	
	/**
	 * @param name
	 * @return
	 */
	public DecayBehavior getDecayBehavior(String name) {
		DecayBehavior d = decays.get(name);
		if (d == null) {
			d = defaultDecay;
		}
		return d;
	}

	public ExciteBehavior getExciteBehavior(String name) {
		ExciteBehavior d = excites.get(name);
		if (d == null) {
			d = defaultExcite;
		}
		return d;
	}	
	//************************
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
				codelet.addFrameworkTimer(timer);	
//				codelet.setId(codeletIdCount++);
				codelet.setExciteBehavior(defaultExcite);
				codelet.setDecayBehavior(defaultDecay);
				codelet.setActivation(defaultActivation);
				codelet.setSoughtContent(defaultObjective);
				codelet.setCodeletAction(defaultActions);			
				
				if(type == SBCodeletFactory.PERCEPTUAL_TYPE){
					codelet.addReadableBuffer(perceptualBuffer);
					codelet.addWritableModule(csm);
				}else if(type == SBCodeletFactory.EPISODIC_TYPE){
					codelet.addReadableBuffer(episodicBuffer);
					codelet.addWritableModule(csm);
				}else if(type == SBCodeletFactory.BROADCAST_TYPE){
					codelet.addReadableBuffer(broadcastQueue);
					codelet.addWritableModule(csm);
				}else if(type == SBCodeletFactory.CSM_TYPE){
					codelet.addReadableBuffer(csmReadable);
					codelet.addWritableModule(csm);
				}else if(type == SBCodeletFactory.ALL_TYPE){
					codelet.addReadableBuffer(perceptualBuffer);
					codelet.addReadableBuffer(episodicBuffer);
					codelet.addReadableBuffer(broadcastQueue);
					codelet.addReadableBuffer(csmReadable);
					codelet.addWritableModule(csm);
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
			}			
		}//if
		return codelet;
	}//method
	
	public StructureBuildingCodelet getCodelet(int type){
		return getGenericCodelet(type);
	}

	public StructureBuildingCodelet getCodelet(int type, double activation,
			NodeStructure context, CodeletAction actions) {
		StructureBuildingCodelet sbc = getGenericCodelet(type);
		sbc.setActivation(activation);
		sbc.setSoughtContent(context);
		sbc.setCodeletAction(actions);
		return sbc;
	}
		
	/**
	 * Recycles the given codelet to be re-used later on, for quickly providing
	 * new empty codelets.
	 * 
	 * @param p
	 *            The codelet to be recycled
	 */
	public void recycle(StructureBuildingCodelet sbCode) {
		sbCode.reset();
		String type = sbCode.getClass().getName();
		if (!pool.containsKey(type))
			pool.put(type, new ArrayList<StructureBuildingCodelet>());
		pool.get(type).add(sbCode);
	}
	
}//class