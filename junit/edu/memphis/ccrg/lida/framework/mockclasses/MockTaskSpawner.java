package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.Collection;
import java.util.Map;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

public class MockTaskSpawner implements TaskSpawner {

	@Override
	public void addTask(LidaTask task) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancelTask(LidaTask task) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getSpawnedTaskCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<LidaTask> getSpawnedTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LidaTaskManager getTaskManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void receiveFinishedTask(LidaTask task) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInitialTasks(Collection<? extends LidaTask> initialTasks) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTaskManager(LidaTaskManager taskManager) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getNextExcecutionTickLap() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getParam(String name, Object defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getScheduledTick() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LidaTaskStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTaskId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TaskSpawner getTaskSpawner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTicksPerStep() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void init(Map<String, ? extends Object> parameters) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNextExcecutionTickLap(long lapTick) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNumberOfTicksPerStep(int ticks) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setScheduledTick(long scheduledTick) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTaskID(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTaskSpawner(TaskSpawner ts) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTaskStatus(LidaTaskStatus status) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopRunning() {
		// TODO Auto-generated method stub

	}

	@Override
	public LidaTask call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void decay(long ticks) {
		// TODO Auto-generated method stub

	}

	@Override
	public void excite(double amount) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getActivation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DecayStrategy getDecayStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExciteStrategy getExciteStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getTotalActivation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setActivation(double activation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDecayStrategy(DecayStrategy strategy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setExciteStrategy(ExciteStrategy strategy) {
		// TODO Auto-generated method stub

	}

}
