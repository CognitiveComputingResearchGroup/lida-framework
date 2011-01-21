/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.util;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

 
/**
 * Basic Gui test for reference.
 * @author Ryan J. McCall
 *
 */
public class GuiDemo extends JFrame implements ActionListener{
	
	static final long serialVersionUID = 0;

	JLabel emptyLabel;
	JLabel world, sim, sm, pam, pbuffer, csm;
	LidaTaskManager timer;
	TaskSpawner motherThread;
	boolean isPaused = true;
    
    public GuiDemo(LidaTaskManager t, TaskSpawner main){
    	timer = t;
    	motherThread = main;
    	this.setTitle("The Mind of LIDA");
    	setCloseOperation();
        
        Container content = getContentPane();
        
        emptyLabel = new JLabel("Welcome");
        emptyLabel.setPreferredSize(new Dimension(175, 100));
        content.add(emptyLabel, BorderLayout.CENTER);
      
        GridLayout layout = new GridLayout(3, 7, 10, 10);
        
        content.setBackground(Color.white);
        content.setLayout(layout); 
        
        JButton start = new JButton("Start/Pause");
        start.addActionListener(this);    
        start.setActionCommand("start/pause");
        content.add(start);
        
        JButton quit = new JButton("Quit");
        quit.addActionListener(this);    
        quit.setActionCommand("quit");
        content.add(quit);

        pack();
        setSize(500, 300);
        setLocation(100, 100);
    }//public GuiTest()

	private void setCloseOperation() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);		
	}//setCloseOperation()

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("start/pause")){
			isPaused = !isPaused;
			if(isPaused)
				emptyLabel.setText("PAUSED");
			else
				emptyLabel.setText("RUNNING");
			//timer.toggleSpawnedThreads();
			if(isPaused)
				System.out.println("\n***PAUSED***\n");
		}

		if(e.getActionCommand().equals("quit")){
			timer.resumeSpawnedTasks();
			motherThread.stopRunning();
			try{Thread.sleep(1000);}catch(Exception e2){}
			System.exit(0);
		}
	}
   
} GuiTest
