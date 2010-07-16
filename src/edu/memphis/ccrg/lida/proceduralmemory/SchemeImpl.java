package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.memphis.ccrg.lida.framework.shared.ActivatibleImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public class SchemeImpl extends ActivatibleImpl implements Scheme {
	
	private static final double RELIABLE_TRESHOLD = 0.5;
	private long id;
	private boolean intrinsicBehavior;
	private int numberOfExecutions;
	private int successExecutions;
	private double curiosity;
	private List <NodeStructure> contextConditions=new ArrayList<NodeStructure>();
	private List <NodeStructure> resultConditions=new ArrayList<NodeStructure>();
	private Map<Long,Argument> arguments = new ConcurrentHashMap<Long, Argument>();
	private long actionId;
	
	public SchemeImpl(long id, long actionId){
		this.id = id;
		this.actionId = actionId;
	}

	@Override
	public long getId() {
		return id;
	}
	@Override
	public void setId(long id) {
		this.id = id;
	}
	

	@Override
	public long getSchemeActionId() {
		return actionId;
	}
	@Override
	public void setSchemeActionId(long actionId) {
		this.actionId = actionId;
	}
		
	public boolean equals(Object o) {
		if (!(o instanceof Scheme)) {
			return false;
		}
		return ((Scheme) o).getId() == id;
	}
	public int hashCode() {
		return ((int) id % 31);
	}

	@Override
	public void addContextCondition(long argumentId, NodeStructure ns) {
		Argument arg = arguments.get(argumentId);
		if(ns != null){
			contextConditions.add(ns);
			if(arg != null)
				arg.addContextCondition(ns);
		}
	}

	@Override
	public void decayCuriosity() {
		// TODO Auto-generated method stub
	}

	@Override
	public List<NodeStructure> getContextConditions() {
		return Collections.unmodifiableList(contextConditions);
	}

	@Override
	public double getCuriosity() {
		return curiosity;
	}

	@Override
	public int getNumberOfExecutions() {
		return numberOfExecutions;
	}

	@Override
	public double getReliability() {
		return (numberOfExecutions>0)?((double) successExecutions)/numberOfExecutions:0.0;
	}

	@Override
	public List<NodeStructure> getResultConditions() {
		return Collections.unmodifiableList(resultConditions);
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
		return (numberOfExecutions>0)&&((((double) successExecutions)/numberOfExecutions)>RELIABLE_TRESHOLD);
	}

	@Override
	public void setCuriosity(double curiosity) {
		this.curiosity=curiosity;
	}

	@Override
	public void setIntrinsicBehavior(boolean intrinsicBehavior) {
		this.intrinsicBehavior=intrinsicBehavior;		
	}

	@Override
	public void setNumberOfExecutions(int numberOfExecutions) {
		this.numberOfExecutions=numberOfExecutions;
	}

	@Override
	public void addArgument(Argument a) {
		arguments.put(a.getArgumentId(),a);		
	}

	@Override
	public void addResultConditions(long argumentId, NodeStructure ns) {
		Argument a = arguments.get(argumentId);
		if (ns!=null){
			resultConditions.add(ns);
			if(a!=null){
				a.addResultCondition(ns);
			}
		}
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
	public List<NodeStructure> getContextConditions(long argumentId) {
		Argument a = arguments.get(argumentId);
		if (a!=null){
			return Collections.unmodifiableList(a.getContextConditions());
		}
		return null;
	}

	@Override
	public List<NodeStructure> getResultConditions(long argumentId) {
		Argument a = arguments.get(argumentId);
		if (a!=null){
			return Collections.unmodifiableList(a.getResultConditions());
		}
		return null;
	}

}
