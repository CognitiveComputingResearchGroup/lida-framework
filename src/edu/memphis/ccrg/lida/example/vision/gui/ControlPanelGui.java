package edu.memphis.ccrg.lida.example.vision.gui;

import javax.swing.JSlider;
import edu.memphis.ccrg.lida.actionSelection.ActionSelection;
import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.environment.EnvironmentImplTemplate;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.ThreadSpawner;

/**
 *
 * @author ryanjmccall
 */
public class ControlPanelGui extends javax.swing.JFrame {
	
	private static final long serialVersionUID = 1L;
	private FrameworkTimer timer;
	private ThreadSpawner motherThread;
	private Environment environment;
	boolean isPaused = true;
	private int sliderMin = 15;
	private int sliderMax = 350;
	private int sliderStartValue = 100;
	private ThreadSpawner codeletThread;
	private ActionSelection actionSelection;
	
    /** Creates new form ContactEditorUI 
     * @param start 
     * @param timer 
     * @param pam */
    public ControlPanelGui(FrameworkTimer timer, ThreadSpawner start, ThreadSpawner codeletDriver, Environment e, ActionSelection as) {
    	this.timer = timer;
    	isPaused = timer.getStartStatus();
    	motherThread = start;
    	codeletThread = codeletDriver;
    	environment = e;
    	actionSelection = as;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        threadCountTextField = new javax.swing.JTextField();
        statusLabel = new javax.swing.JLabel();
        startPauseButton = new javax.swing.JButton();
        resetEnvironmentButton = new javax.swing.JButton();
        quitButton = new javax.swing.JButton();
        speedSlider = new javax.swing.JSlider(sliderMin, sliderMax, sliderStartValue);
        jLabel2 = new javax.swing.JLabel();
        sleepTimeTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        minSleepTimeLabel = new javax.swing.JLabel();
        maxSleepTimeLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        jLabel1.setText("LIDA Control Panel");

        jLabel4.setText("Thread Count");

        String threadCount = "";
        if(codeletThread != null)
        	threadCount = (motherThread.getSpawnedThreadCount() + codeletThread.getSpawnedThreadCount())+"";
        else
        	threadCount = motherThread.getSpawnedThreadCount() + "";
        
        threadCountTextField.setText(threadCount);

        statusLabel.setFont(new java.awt.Font("Lucida Grande", 1, 14));
        if(isPaused)
			statusLabel.setText("PAUSED");
		else
			statusLabel.setText("RUNNING");

        startPauseButton.setText("Start/Pause");
        startPauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startPauseButtonActionPerformed(evt);
            }
        });

        resetEnvironmentButton.setText("Reset Environment");
        resetEnvironmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetEnvironmentButtonClicked(evt);
            }
        });

        quitButton.setText("Quit");
        quitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitButtonActionPerformed(evt);
            }
        });

        speedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                speedSliderStateChanged(evt);
            }
        });

        jLabel2.setText("Module Sleep Time");

        sleepTimeTextField.setText(this.sliderStartValue + "");

        jLabel3.setText("ms");

        minSleepTimeLabel.setText(sliderMin+" ms");

        maxSleepTimeLabel.setText(sliderMax+" ms");

        jLabel7.setText("System Status");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(startPauseButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(resetEnvironmentButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(quitButton))
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(layout.createSequentialGroup()
                        .add(jLabel4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(threadCountTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel7)
                            .add(statusLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(27, 27, 27)
                        .add(jLabel2)
                        .add(2, 2, 2)
                        .add(sleepTimeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel3))
                    .add(layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(speedSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(26, 26, 26)
                        .add(minSleepTimeLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 155, Short.MAX_VALUE)
                        .add(maxSleepTimeLabel)
                        .add(8, 8, 8)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel2)
                            .add(sleepTimeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel3))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(speedSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel4)
                            .add(threadCountTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(18, 18, 18)
                        .add(jLabel7)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(maxSleepTimeLabel)
                        .add(minSleepTimeLabel))
                    .add(statusLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(12, 12, 12)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(startPauseButton)
                    .add(resetEnvironmentButton)
                    .add(quitButton)))
        );
        setLocation(90, 270);
        pack();
    }// </editor-fold>
    
    public void refreshGuiValues(){
    	String threadCount = "";
        if(codeletThread != null)
        	threadCount = (motherThread.getSpawnedThreadCount() + codeletThread.getSpawnedThreadCount())+"";
        else
        	threadCount = motherThread.getSpawnedThreadCount() + "";
        threadCountTextField.setText(threadCount);
    }

    private void startPauseButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	refreshGuiValues();
    	isPaused = !isPaused;
		if(isPaused)
			statusLabel.setText("PAUSED");
		else
			statusLabel.setText("RUNNING");
		timer.toggleRunningThreads();
    }
    
    private void resetEnvironmentButtonClicked(java.awt.event.ActionEvent evt) {  
    	actionSelection.stopActionSelection();
    	refreshGuiValues();

    	isPaused = !isPaused;
		if(isPaused)
			statusLabel.setText("PAUSED");
		else
			statusLabel.setText("RUNNING");
    	timer.toggleRunningThreads();
    	
    	environment.resetEnvironment();
    	try{Thread.sleep(200);}catch(Exception e){}
    	
    	isPaused = !isPaused;
		if(isPaused)
			statusLabel.setText("PAUSED");
		else
			statusLabel.setText("RUNNING");
    	timer.toggleRunningThreads();
    }
    
    /**
     * Ensures threads print out their cycle times even if they were paused.
     * Tells the main thread to end all child threads.
     * Pauses giving the children time to shutdown.
     * Finally, terminates everything.
     * 
     * @param evt
     */
    private void quitButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	statusLabel.setText("QUITTING");
		timer.resumeRunningThreads(); 
		motherThread.stopSpawnedThreads();
    }
    
    private void speedSliderStateChanged(javax.swing.event.ChangeEvent evt) {
    	 JSlider source = (JSlider)evt.getSource();
    	 if(!source.getValueIsAdjusting()){
    	     int sleepTime = (int)source.getValue();
    	     timer.setSleepTime(sleepTime);
    	     sleepTimeTextField.setText(sleepTime + "");
    	 }    
    	 refreshGuiValues();
    }//method
    
    // Variables declaration - do not modify
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel maxSleepTimeLabel;
    private javax.swing.JLabel minSleepTimeLabel;
    private javax.swing.JButton quitButton;
    private javax.swing.JButton resetEnvironmentButton;
    private javax.swing.JTextField sleepTimeTextField;
    private javax.swing.JSlider speedSlider;
    private javax.swing.JButton startPauseButton;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField threadCountTextField;
    // End of variables declaration

}
