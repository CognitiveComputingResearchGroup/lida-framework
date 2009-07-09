package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.memphis.ccrg.lida.framework.FrameworkTimer;
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
	
	private static final int PERCEPTUAL_TYPE = 0;
	private static final int EPISODIC_TYPE = 1;
	private static final int BROADCAST_TYPE = 2;
	private static final int CSM_TYPE = 3;
	private static final int ALL_TYPE = 4;	

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
	private List<CodeletReadable> perceptualBuffer = new ArrayList<CodeletReadable>();
	private List<CodeletReadable> episodicBuffer = new ArrayList<CodeletReadable>();
	private List<CodeletReadable> broadcastQueue = new ArrayList<CodeletReadable>();
	private List<CodeletReadable> allBuffers = new ArrayList<CodeletReadable>();
	private List<CodeletReadable> csmRead = new ArrayList<CodeletReadable>();	
	private List<CodeletWritable> csm = new ArrayList<CodeletWritable>();
	//
	private double defaultActivation = 1.0;	
	private NodeStructure defaultObjective = new NodeStructureImpl();
	private CodeletAction defaultActions = new BasicCodeletAction();
	private FrameworkTimer timer;
	
	/**
	 * Returns the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	static public SBCodeletFactory getInstance(Workspace w, FrameworkTimer timer) {
		if(instance == null)
			instance = new SBCodeletFactory(w, timer);
		return instance;
	}//constructor
	
	/**
	 * Builds a new pooling codelet factory
	 * TODO: Are we goign to use this?
	 * @post pool.size() == 0
	 */
	public SBCodeletFactory(Workspace workspace, FrameworkTimer timer) {
		DefaultSBCodeletType = "StructBuildCodeletImpl";
		DefaultSBCodeletClassName = "edu.memphis.ccrg.lida.workspace.structureBuildingCodelets." + DefaultSBCodeletType;
		sbCodeletClasses.put(DefaultSBCodeletType, DefaultSBCodeletClassName);
		//
		defaultDecay = new LinearDecayBehavior();
		defaultExcite = new BasicExciteBehavior();
		pool = new HashMap<String, List<StructureBuildingCodelet>>();
		//
		perceptualBuffer.add(workspace.getPerceptualBuffer());
		episodicBuffer.add(workspace.getEpisodicBuffer());
		broadcastQueue.add(workspace.getBroadcastBuffer());
		csmRead.add(workspace.getCSM());
		allBuffers.add(workspace.getPerceptualBuffer());
		allBuffers.add(workspace.getEpisodicBuffer());
		allBuffers.add(workspace.getBroadcastBuffer());	
		csm.add(workspace.getCSM());
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
	private StructureBuildingCodelet getBlankCodelet(int type) {
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
				
				codelet.setExciteBehavior(defaultExcite);
				codelet.setDecayBehavior(defaultDecay);
				codelet.setActivation(defaultActivation);
				codelet.setSoughtContent(defaultObjective);
				codelet.setCodeletAction(defaultActions);			
				
				if(type == SBCodeletFactory.PERCEPTUAL_TYPE)
					codelet.setAccessibleModules(perceptualBuffer, csm);
				else if(type == SBCodeletFactory.EPISODIC_TYPE)
					codelet.setAccessibleModules(episodicBuffer, csm);
				else if(type == SBCodeletFactory.BROADCAST_TYPE)
					codelet.setAccessibleModules(broadcastQueue, csm);
				else if(type == SBCodeletFactory.CSM_TYPE)
					codelet.setAccessibleModules(csmRead, csm);
				else if(type == SBCodeletFactory.ALL_TYPE)
					codelet.setAccessibleModules(allBuffers, csm);

			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
			}			
		}

		return codelet;
	}//method
	
	public StructureBuildingCodelet getCodelet(int type){
		return getBlankCodelet(type);
	}

	public StructureBuildingCodelet getCodelet(int type, double activation,
			NodeStructure context, CodeletAction actions) {
		StructureBuildingCodelet sbc = getBlankCodelet(type);
		sbc.setActivation(activation);
		sbc.setSoughtContent(context);
		sbc.setCodeletAction(actions);
		return sbc;
	}
		
	/**
	 * Recycles the given codelet to be re-used lateron, for quickly providing
	 * new empty codelets.
	 * 
	 * @param p
	 *            The codelet to be recycled
	 */
	public void recycle(StructureBuildingCodelet sbCode) {
		sbCode.clearForReuse();
		String type = sbCode.getClass().getName();
		if (!pool.containsKey(type))
			pool.put(type, new ArrayList<StructureBuildingCodelet>());
		pool.get(type).add(sbCode);
	}
	
}//class