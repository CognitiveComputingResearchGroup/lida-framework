/**
 * 
 */
package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.pam.PamLinkable;
import edu.memphis.ccrg.lida.pam.PamListener;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.PropagationBehavior;
import edu.memphis.ccrg.lida.pam.tasks.FeatureDetector;

/**
 * @author Javier Snaider
 *
 */
public class MockPAM implements PerceptualAssociativeMemory {

	private static double perceptThreshold = 0.0;

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#getModuleName()
	 */
	@Override
	public ModuleName getModuleName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#setModuleName(edu.memphis.ccrg.lida.framework.ModuleName)
	 */
	@Override
	public void setModuleName(ModuleName moduleName) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#getSubmodule(edu.memphis.ccrg.lida.framework.ModuleName)
	 */
	@Override
	public LidaModule getSubmodule(ModuleName name) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#addSubModule(edu.memphis.ccrg.lida.framework.LidaModule)
	 */
	@Override
	public void addSubModule(LidaModule lm) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#getModuleContent(java.lang.Object[])
	 */
	@Override
	public Object getModuleContent(Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#decayModule(long)
	 */
	@Override
	public void decayModule(long ticks) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#addListener(edu.memphis.ccrg.lida.framework.ModuleListener)
	 */
	@Override
	public void addListener(ModuleListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#setAssistingTaskSpawner(edu.memphis.ccrg.lida.framework.tasks.TaskSpawner)
	 */
	@Override
	public void setAssistingTaskSpawner(TaskSpawner ts) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#getAssistingTaskSpawner()
	 */
	@Override
	public TaskSpawner getAssistingTaskSpawner() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.initialization.FullyInitializable#setAssociatedModule(edu.memphis.ccrg.lida.framework.LidaModule, int)
	 */
	@Override
	public void setAssociatedModule(LidaModule module, int moduleUsage) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.initialization.Initializable#init(java.util.Map)
	 */
	@Override
	public void init(Map<String, ?> parameters) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.initialization.Initializable#init()
	 */
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.initialization.Initializable#getParam(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object getParam(String name, Object defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.dao.Saveable#getState()
	 */
	@Override
	public Object getState() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.dao.Saveable#setState(java.lang.Object)
	 */
	@Override
	public boolean setState(Object content) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNewNode(java.lang.String)
	 */
	@Override
	public PamNode addNewNode(String label) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNewNode(java.lang.String, java.lang.String)
	 */
	@Override
	public PamNode addNewNode(String type, String label) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addDefaultNode(edu.memphis.ccrg.lida.framework.shared.Node)
	 */
	@Override
	public PamNode addDefaultNode(Node node) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addDefaultNodes(java.util.Set)
	 */
	@Override
	public Set<PamNode> addDefaultNodes(Set<? extends Node> nodes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNewLink(edu.memphis.ccrg.lida.framework.shared.Node, edu.memphis.ccrg.lida.framework.shared.Node, edu.memphis.ccrg.lida.framework.shared.LinkCategory, double)
	 */
	@Override
	public Link addNewLink(Node source, Node sink, LinkCategory type,
			double activation) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNewLink(edu.memphis.ccrg.lida.framework.shared.ExtendedId, edu.memphis.ccrg.lida.framework.shared.ExtendedId, edu.memphis.ccrg.lida.framework.shared.LinkCategory, double)
	 */
	@Override
	public Link addNewLink(ExtendedId sourceId, ExtendedId sinkId,
			LinkCategory type, double activation) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addDefaultLinks(java.util.Set)
	 */
	@Override
	public Set<PamLink> addDefaultLinks(Set<? extends Link> links) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addFeatureDetector(edu.memphis.ccrg.lida.pam.tasks.FeatureDetector)
	 */
	@Override
	public void addFeatureDetector(FeatureDetector fd) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addPamListener(edu.memphis.ccrg.lida.pam.PamListener)
	 */
	@Override
	public void addPamListener(PamListener pl) {
		// TODO Auto-generated method stub

	}


	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#setPropagationBehavior(edu.memphis.ccrg.lida.pam.PropagationBehavior)
	 */
	@Override
	public void setPropagationBehavior(PropagationBehavior b) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#receiveActivationBurst(edu.memphis.ccrg.lida.pam.PamNode, double)
	 */
	@Override
	public void receiveActivationBurst(PamNode node, double amount) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#receiveActivationBurst(java.util.Set, double)
	 */
	@Override
	public void receiveActivationBurst(Set<PamNode> nodes, double amount) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#propagateActivation(edu.memphis.ccrg.lida.pam.PamNode, edu.memphis.ccrg.lida.pam.PamNode, edu.memphis.ccrg.lida.pam.PamLink, double)
	 */
	@Override
	public void propagateActivation(PamNode source, PamNode sink, PamLink link,
			double amount) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * Just for test matter
	 */
	private PamNode pmNode;
	
	public PamNode testGetSink(){
		return pmNode;
	}
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#propagateActivationToParents(edu.memphis.ccrg.lida.pam.PamNode)
	 */
	@Override
	public void propagateActivationToParents(PamNode pamNode) {
		pmNode = pamNode;

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#addNodeStructureToPercept(edu.memphis.ccrg.lida.framework.shared.NodeStructure)
	 */
	@Override
	public void addNodeStructureToPercept(NodeStructure ns) {
		percept = ns;
	}

	private NodeStructure percept;
	
	public NodeStructure getCurrentTestPercept(){
		return percept;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsNode(edu.memphis.ccrg.lida.framework.shared.Node)
	 */
	@Override
	public boolean containsNode(Node node) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsNode(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public boolean containsNode(ExtendedId id) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsLink(edu.memphis.ccrg.lida.framework.shared.Link)
	 */
	@Override
	public boolean containsLink(Link link) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#containsLink(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public boolean containsLink(ExtendedId id) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#setPerceptThreshold(double)
	 */
	@Override
	public void setPerceptThreshold(double t) {
		MockPAM.perceptThreshold =t;

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#setUpscaleFactor(double)
	 */
	@Override
	public void setUpscaleFactor(double f) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getUpscaleFactor()
	 */
	@Override
	public double getUpscaleFactor() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#setDownscaleFactor(double)
	 */
	@Override
	public void setDownscaleFactor(double f) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getDownscaleFactor()
	 */
	@Override
	public double getDownscaleFactor() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#isOverPerceptThreshold(edu.memphis.ccrg.lida.pam.PamLinkable)
	 */
	@Override
	public boolean isOverPerceptThreshold(PamLinkable l) {
		// TODO Auto-generated method stub
		return l.getTotalActivation()>= MockPAM.perceptThreshold;

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getPamNode(int)
	 */
	@Override
	public Node getPamNode(int id) {
		Node n = new PamNodeImpl();
		n.setId(id);
		return n;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getPamNode(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public Node getPamNode(ExtendedId id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getPamLink(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public Link getPamLink(ExtendedId id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory#getPamNodes()
	 */
	@Override
	public Collection<Node> getPamNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Link> getPamLinks() {
		// TODO Auto-generated method stub
		return null;
	}


}
