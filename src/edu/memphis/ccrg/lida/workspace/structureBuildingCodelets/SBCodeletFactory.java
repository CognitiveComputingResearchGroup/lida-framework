package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	 * Map of created codelets.
	 * Key = codelet id
	 * Value = codelet
	 */
	private Map<Long, StructureBuildingCodelet> codelets;

	/**
	 * Pool keeping all recycled codelets.
	 * Key = codelet type
	 * Value = unused codelets of that type
	 */
	private Map<String, List<StructureBuildingCodelet>> pool;
	
	/**
	 * List of all known codelet types
	 */
	private List<Integer> knownTypes;

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
		pool = new HashMap<String, List<StructureBuildingCodelet>>();
		codelets = new HashMap<Long, StructureBuildingCodelet>();
		knownTypes = new ArrayList<Integer>(); 
	}
		
	/**
	 * Registers the given codelet. Makes codelets that were created outside
	 * of this factory easier to find.
	 * 
	 * @param codelet The codelet to register
	 */
	public void register(StructureBuildingCodelet codelet){
		codelets.put(codelet.getId(),codelet);
	}

	/**
	 * Returns the codelet with the given key, if it was created by this
	 * factory, returns null otherwise. Does not create codelets, only sees if a
	 * reference to an existent codelet can be found.
	 * 
	 * @param key
	 *            The key or id of the wanted codelet
	 * @return The codelet with the given key, if it was created by this
	 *         factory, null otherwise.
	 */
	public StructureBuildingCodelet findCodelet(Long key) {
		return codelets.get(key);
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
		for(Integer i: newTypes)
			knownTypes.add(i);
	}
		
}//class
