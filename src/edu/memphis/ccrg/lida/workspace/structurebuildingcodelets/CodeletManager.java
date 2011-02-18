package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

//TODO review CodeletInterface
//TODO refactor to Codelet only
public interface CodeletManager {

	/**
	 * Model attention module
	 * @param t CodeletType
	 * @return new codelet
	 */
	public StructureBuildingCodelet getNewCodelet(CodeletType t);

	public void runCodelet(StructureBuildingCodelet cod);

	/**
	 * Recycles the given codelet to be re-used later on, for quickly providing
	 * new empty codelets.
	 * 
	 * @param sbCode The codelet to be recycled
	 */
	public void recycle(StructureBuildingCodelet sbCode);

}