/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ControlPanel.java
 *
 * Created on 12/07/2009, 09:26:00
 */
package edu.memphis.ccrg.lida.framework.gui.panels;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JSlider;

import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.commands.Command;
import edu.memphis.ccrg.lida.framework.gui.commands.SetTimeScaleCommand;

/**
 * 
 * @author Javier Snaider
 */
public class UpdateToControlPanel extends LidaPanelImpl implements FrameworkGuiEventListener {

	private static final long serialVersionUID = 1L;

	boolean isPaused = true;
	private int sliderMin = 0;
	private int sliderMax = 50;
	private int sliderStartValue = (sliderMax - sliderMin) / 2;

	/** Creates new form ControlPanel */
	public UpdateToControlPanel() {
		initComponents();
		setSupportedModule(Module.noModule);

		minSleepTimeLabel.setText(sliderMin + " ms");
		maxSleepTimeLabel.setText(sliderMax + " ms");
		sleepTimeTextField.setText(this.sliderStartValue + "");
	}
	
	/** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        startPauseButton = new javax.swing.JButton();
        quitButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        sleepTimeTextField = new javax.swing.JTextField();
        speedSlider = new javax.swing.JSlider(sliderMin, sliderMax,
    			sliderStartValue);
        jPanel1 = new javax.swing.JPanel();
        addTicksButton = new javax.swing.JButton();
        sleepTimeTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TicksEnabledCheckBox = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        minSleepTimeLabel = new javax.swing.JLabel();
        maxSleepTimeLabel = new javax.swing.JLabel();

        startPauseButton.setText("Start/Pause");
        startPauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startPauseButtonActionPerformed(evt);
            }
        });

        quitButton.setText("Quit");
        quitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel1.setText("Control Panel");

        statusLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        statusLabel.setText("Paused");

        jLabel2.setText("Ticks Scale (ms)");

        sleepTimeTextField.setText("--");

        speedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                speedSliderStateChanged(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        addTicksButton.setText("add ticks");
        addTicksButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTicksButtonActionPerformed(evt);
            }
        });

        sleepTimeTextField1.setText("--");

        jLabel5.setText("Ticks:");

        TicksEnabledCheckBox.setText("Ticks Mode Enabled");
        TicksEnabledCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TicksEnabledCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sleepTimeTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addTicksButton, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(TicksEnabledCheckBox)
                .addContainerGap(128, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(TicksEnabledCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sleepTimeTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addTicksButton)
                    .addComponent(jLabel5)))
        );

        jLabel3.setText("Status:");

        minSleepTimeLabel.setText("Min");

        maxSleepTimeLabel.setText("Max");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(statusLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(speedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(startPauseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                                .addComponent(quitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))))
                .addContainerGap(28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(216, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(minSleepTimeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(1, 1, 1)
                        .addComponent(sleepTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(maxSleepTimeLabel))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(7, 7, 7)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(minSleepTimeLabel)
                        .addComponent(maxSleepTimeLabel))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(sleepTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(speedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startPauseButton)
                    .addComponent(quitButton))
                .addContainerGap(23, Short.MAX_VALUE))
        );
    }// </editor-fold>

	private void startPauseButtonActionPerformed(java.awt.event.ActionEvent evt) {
		isPaused = !isPaused;
		if (isPaused) {
			statusLabel.setText("PAUSED");
			controller.executeCommand("pauseRunningThreads", null);
		} else {
			statusLabel.setText("RUNNING");
			controller.executeCommand("resumeRunningThreads", null);
		}
		refresh();
	}

	private void quitButtonActionPerformed(java.awt.event.ActionEvent evt) {
		statusLabel.setText("QUITTING");
		controller.executeCommand("quitAll", null);
	}

	private void speedSliderStateChanged(javax.swing.event.ChangeEvent evt) {
		JSlider source = (JSlider) evt.getSource();
		if (!source.getValueIsAdjusting()) {
			int sleepTime = (int) source.getValue();
			sleepTimeTextField.setText(sleepTime + "");

			// Another way to execute commands
			Command command = new SetTimeScaleCommand();
			command.setParameter("timeScale", sleepTime);
			controller.executeCommand(command);
			refresh();
		}
	}

	private void TicksEnabledCheckBoxActionPerformed(
			java.awt.event.ActionEvent evt) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("enable", TicksEnabledCheckBox.isSelected());
		controller.executeCommand("EnableTicksMode", parameters);
	}

	private void addTicksButtonActionPerformed(java.awt.event.ActionEvent evt) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		int ticks;
		try {
			ticks = Integer.parseInt(sleepTimeTextField1.getText());
		} catch (NumberFormatException e) {
			ticks = 0;
		}
		parameters.put("ticks", ticks);
		controller.executeCommand("AddTicks", parameters);
	}

	// Variables declaration - do not modify
    private javax.swing.JCheckBox TicksEnabledCheckBox;
    private javax.swing.JButton addTicksButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel maxSleepTimeLabel;
    private javax.swing.JLabel minSleepTimeLabel;
    private javax.swing.JButton quitButton;
    private javax.swing.JTextField sleepTimeTextField;
    private javax.swing.JTextField sleepTimeTextField1;
    private javax.swing.JSlider speedSlider;
    private javax.swing.JButton startPauseButton;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration

    
	public void refresh() {
		isPaused = lida.getTaskManager().isTasksPaused();
		if (isPaused)
			statusLabel.setText("PAUSED");
		else
			statusLabel.setText("RUNNING");
	}

	public void receiveGuiEvent(FrameworkGuiEvent event) {
	}

}// class