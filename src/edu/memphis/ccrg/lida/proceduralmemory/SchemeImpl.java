package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.memphis.ccrg.lida.framework.shared.ActivatibleImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public class SchemeImpl extends ActivatibleImpl implements Scheme {

	private static final double RELIABLE_TRESHOLD = 0.5;
	private long id;
	private boolean intrinsicBehavior;
	private int numberOfExecutions;
	private int successExecutions;
	private double curiosity;
	private List<NodeStructure> contextConditions = new ArrayList<NodeStructure>();
	private List<NodeStructure> resultConditions = new ArrayList<NodeStructure>();
	private ConcurrentMap<Long, Argument> arguments = new ConcurrentHashMap<Long, Argument>();
	private ConcurrentMap<Long, List<NodeStructure>> argumentsCC = new ConcurrentHashMap<Long, List<NodeStructure>>();
	private ConcurrentMap<Long, List<NodeStructure>> argumentsRC = new ConcurrentHashMap<Long, List<NodeStructure>>();

	private long actionId;

	public SchemeImpl(long id, long actionId) {
		this.id = id;
		this.actionId = actionId;
	}
	
	public SchemeImpl(Scheme s){
		this.id = s.getId();
		this.actionId = s.getSchemeActionId();
		this.intrinsicBehavior = s.isIntrinsicBehavior();
		//TODO: More copying
	}

	@Override
	public synchronized void addArgument(Argument a) {
		arguments.putIfAbsent(a.getArgumentId(), a);
		argumentsCC.putIfAbsent(a.getArgumentId(),
				new ArrayList<NodeStructure>());
		argumentsRC.putIfAbsent(a.getArgumentId(),
				new ArrayList<NodeStructure>());
	}

	@Override
	public synchronized void addContextCondition(long argumentId,
			NodeStructure ns) {
		if (ns != null) {
			if (argumentId == 0) {// condition without argument.
				contextConditions.add(ns);
			} else {
				Argument a = arguments.get(argumentId);
				if (a != null) {
					contextConditions.add(ns);
					List<NodeStructure> conditions = argumentsCC.get(argumentId);
					conditions.add(ns);
				}
			}
		}
	}

	@Override
	public void addContextCondition(NodeStructure ns) {
		addContextCondition(0L,ns);
	}

	@Override
	public synchronized void addResultConditions(long argumentId,
			NodeStructure ns) {
		if (ns != null) {
			if (argumentId == 0) {// condition without argument.
				resultConditions.add(ns);
			} else {
				Argument a = arguments.get(argumentId);
				if (a != null) {
					resultConditions.add(ns);
					List<NodeStructure> conditions = argumentsRC.get(argumentId);
					conditions.add(ns);
				}
			}
		}
	}

	@Override
	public void addResultConditions(NodeStructure ns) {
		addResultConditions(0L,ns);		
	}

	@Override
	public void decayCuriosity() {
	}

	public boolean equals(Object o) {
		if (!(o instanceof Scheme)) {
			return false;
		}
		return ((Scheme) o).getId() == id;
	}

	@Override
	public Argument getArgument(long argumentId) {
		return arguments.get(argumentId);
	}

	@Override
	public Collection<Argument> getArguments() {
		return Collections.unmodifiableCollection(arguments.values());
	}

	@Override
	public List<NodeStructure> getContextConditions() {
		return Collections.unmodifiableList(contextConditions);
	}

	@Override
	public List<NodeStructure> getContextConditions(long argumentId) {
		List<NodeStructure> conditions = argumentsCC.get(argumentId);
		if (conditions != null) {
			return Collections.unmodifiableList(conditions);
		}
		return null;
	}

	@Override
	public double getCuriosity() {
		return curiosity;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public int getNumberOfExecutions() {
		return numberOfExecutions;
	}

	@Override
	public double getReliability() {
		return (numberOfExecutions > 0) ? ((double) successExecutions)
				/ numberOfExecutions : 0.0;
	}

	@Override
	public List<NodeStructure> getResultConditions() {
		return Collections.unmodifiableList(resultConditions);
	}

	@Override
	public List<NodeStructure> getResultConditions(long argumentId) {
		List<NodeStructure> conditions = argumentsRC.get(argumentId);
		if (conditions != null) {
			return Collections.unmodifiableList(conditions);
		}
		return null;
	}

	@Override
	public long getSchemeActionId() {
		return actionId;
	}

	public int hashCode() {
		return ((int) id % 31);
	}

	@Override
	public void incrementNumberOfExecutions() {
		numberOfExecutions++;
	}

	@Override
	public boolean isIntrinsicBehavior() {
		return intrinsicBehavior;
	}

	@Override
	public boolean isReliable() {
		return (numberOfExecutions > 0)
				&& ((((double) successExecutions) / numberOfExecutions) > RELIABLE_TRESHOLD);
	}

	@Override
	public void setCuriosity(double curiosity) {
		this.curiosity = curiosity;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public void setIntrinsicBehavior(boolean intrinsicBehavior) {
		this.intrinsicBehavior = intrinsicBehavior;
	}

	@Override
	public void setNumberOfExecutions(int numberOfExecutions) {
		this.numberOfExecutions = numberOfExecutions;
	}

	@Override
	public void setSchemeActionId(long actionId) {
		this.actionId = actionId;
	}
	//TODO: FIX
	public boolean isContextCompleted(){
		return true;
	}
}
