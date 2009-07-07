package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.memphis.ccrg.lida.shared.strategies.BasicExciteBehavior;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.shared.strategies.LinearDecayCurve;

/**
 * The CodeletFactory is responsible for loading codelets dynamically. There are
 * also methods which will automatically add the new codelets to a scheduler.
 * This factory also recycles codelets to save creation time.
 *
 * Pattern: Factory, Singleton
 */
public class SBCodeletFactory {

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
	
	/**
	 * Returns the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	static public SBCodeletFactory getInstance() {
		if(instance == null)
			instance = new SBCodeletFactory();
		return instance;
	}//constructor
	
	/**
	 * Builds a new pooling codelet factory
	 * TODO: Are we goign to use this?
	 * @post pool.size() == 0
	 */
	public SBCodeletFactory() {
		DefaultSBCodeletType = "StructBuildCodeletImpl";
		DefaultSBCodeletClassName = "edu.memphis.ccrg.lida.workspace.structureBuildingCodelets." + DefaultSBCodeletType;
		sbCodeletClasses.put(DefaultSBCodeletType, DefaultSBCodeletClassName);
		defaultDecay = new LinearDecayCurve();
		defaultExcite = new BasicExciteBehavior();
		
		pool = new HashMap<String, List<StructureBuildingCodelet>>();
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
	protected StructureBuildingCodelet getBlankCodelet(String type) {
		StructureBuildingCodelet codelet = null;
		if(pool.containsKey(type)){
			List<StructureBuildingCodelet> codelets = pool.get(type);
			if(codelets != null)
				codelet = codelets.remove(0);
		}
		if(codelet == null){
			//create a new codelet
		}

		return codelet;
	}//method
	
//	public StructureBuildingCodelet getCodelet() {
//		Node n = null;
//		try {
////			n = (Node) Class.forName(DefaultClassName).newInstance();
////			n.setExciteBehavior(defaultExcite);
////			n.setDecayBehavior(defaultDecay);
//
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//		}
//		return n;
//	}

	public StructureBuildingCodelet getCodelet(String type, double activation, String... args) {
		StructureBuildingCodelet code = getBlankCodelet(type);
		//code.setType(type);
		code.setActivation(activation);
		//code.initialize(args);
		return code;
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
		
	/**
	 * @param knownTypes The knownTypes to set.
	 */
	public void addCodeletType(List<Integer> newTypes) {

	}
		
}//class
