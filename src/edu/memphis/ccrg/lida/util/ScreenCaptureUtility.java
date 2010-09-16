package edu.memphis.ccrg.lida.util;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;


/**
 * Utility to capture the screen. 
 * Drawbacks: Does not capture the mouse cursor.  However mouse coordinates can be obtained from MouseInfo
 * 
 * @author Ryan J McCall
 */
public class ScreenCaptureUtility{
	
	private static Logger logger = Logger.getLogger("lida.util.ScreenCaptureUtil");
	
	/**
	 * Java utility
	 */
	private Robot robot;
	
	/**
	 * Area in your screen where the robot will capture from
	 */
	private Rectangle areaToCapture;
	
	public ScreenCaptureUtility(){
		try{
			robot = new Robot();
			areaToCapture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		}catch (AWTException e){
			logger.warning("Robot could not be created for screen capture!");
		}
	}
	
	/**
	 * @param r
	 */
	public ScreenCaptureUtility(Rectangle r){
		try {
			robot = new Robot();
			areaToCapture = r;
		} catch (AWTException e) {
			logger.warning("Robot could not be created for screen capture!");
		}
	}

	public BufferedImage getScreenCapture() {
		return robot.createScreenCapture(areaToCapture);	
	}
	

}//class