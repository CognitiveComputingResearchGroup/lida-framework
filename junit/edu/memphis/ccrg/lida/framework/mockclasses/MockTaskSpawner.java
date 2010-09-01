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

	}

	@Override
	public void cancelTask(LidaTask task) {

	}

	@Override
	public int getSpawnedTaskCount() {
		return 0;
	}

	@Override
	public Collection<LidaTask> getSpawnedTasks() {
		return null;
	}

	@Override
	public LidaTaskManager getTaskManager() {
		return null;
	}

	@Override
	public void receiveFinishedTask(LidaTask task) {

	}

	@Override
	public void setInitialTasks(Collection<? extends LidaTask> initialTasks) {
		

	}

	@Override
	public void setTaskManager(LidaTaskManager taskManager) {
		

	}

	@Override
	public long getNextExcecutionTickLap() {
		
		return 0;
	}

	@Override
	public Object getParam(String name, Object defaultValue) {
		
		return null;
	}

	@Override
	public long getScheduledTick() {
		
		return 0;
	}

	@Override
	public LidaTaskStatus getStatus() {
		
		return null;
	}

	@Override
	public long getTaskId() {
		
		return 0;
	}

	@Override
	public TaskSpawner getTaskSpawner() {
		
		return null;
	}

	@Override
	public int getTicksPerStep() {
		
		return 0;
	}

	@Override
	public void init(Map<String, ? extends Object> parameters) {
		

	}

	@Override
	public void reset() {
		

	}

	@Override
	public void setNextExcecutionTickLap(long lapTick) {
		

	}

	@Override
	public void setNumberOfTicksPerRun(int ticks) {
		

	}

	@Override
	public void setScheduledTick(long scheduledTick) {
		

	}

	@Override
	public void setTaskID(long id) {
		

	}

	@Override
	public void setTaskSpawner(TaskSpawner ts) {
		

	}

	@Override
	public void setTaskStatus(LidaTaskStatus status) {
		

	}

	@Override
	public void stopRunning() {
		

	}

	@Override
	public LidaTask call() throws Exception {
		
		return null;
	}

	@Override
	public void decay(long ticks) {
		

	}

	@Override
	public void excite(double amount) {
		

	}

	@Override
	public double getActivation() {
		
		return 0;
	}

	@Override
	public DecayStrategy getDecayStrategy() {
		
		return null;
	}

	@Override
	public ExciteStrategy getExciteStrategy() {
		
		return null;
	}

	@Override
	public double getTotalActivation() {
		
		return 0;
	}

	@Override
	public void setActivation(double activation) {
		

	}

	@Override
	public void setDecayStrategy(DecayStrategy strategy) {
		

	}

	@Override
	public void setExciteStrategy(ExciteStrategy strategy) {
		

	}

}
