package edu.memphis.ccrg.lida.example.globalworkspace;

import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceInterface;

public class MockWorkspace implements Runnable {
	private GlobalWorkspaceInterface gw;
	private long delay;
	
	public MockWorkspace(GlobalWorkspaceInterface gw, long delay){
		this.gw=gw;
		this.delay=delay;
	}

	public void run() {
		int num;
		double act;
		while (true){
			num=(int)(Math.random()*100);
			act=Math.random();
			gw.putCoalition(new MockCoalition(num,act));
			System.out.println("WS:"+num +":"+act);
			try {
				Thread.sleep((long) (Math.random()*delay));
			} catch (InterruptedException e) {
			}
		}
	}

}
