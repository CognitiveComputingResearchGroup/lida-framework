package edu.memphis.ccrg.lida.example.globalworkspace;

import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;

public class MockWorkspace implements Runnable {
	private GlobalWorkspace gw;
	private long delay;
	
	public MockWorkspace(GlobalWorkspace gw, long delay){
		this.gw=gw;
		this.delay=delay;
	}

	public void run() {
		int num;
		double act;
		while (true){
			num=(int)(Math.random()*100);
			act=Math.random();
			gw.addCoalition(new MockCoalition(num,act));
			System.out.println("WS:"+num +":"+act);
			try {
				Thread.sleep((long) (Math.random()*delay));
			} catch (InterruptedException e) {
			}
		}
	}

}
