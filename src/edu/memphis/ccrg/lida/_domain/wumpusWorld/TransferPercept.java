package edu.memphis.ccrg.lida._domain.wumpusWorld;


class TransferPercept {
	
	private Environment environment;
		
	public TransferPercept(Environment wumpusEnvironment) {
		
		environment = wumpusEnvironment;
		
	}
	
	public boolean getBump() {
		return environment.getBump();
	}
	
	public boolean getGlitter() {
		return environment.getGlitter();
	}

	public boolean getBreeze() {
		return environment.getBreeze();
	}

	public boolean getStench() {
		return environment.getStench();
	}
	
	public boolean getScream() {
		return environment.getScream();
	}
	
}