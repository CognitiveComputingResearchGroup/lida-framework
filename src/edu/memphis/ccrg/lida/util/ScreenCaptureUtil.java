package edu.memphis.ccrg.lida.util;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;


/**
 * Utility to capture the screen. 
 * Drawbacks: 
 * 1. Does not capture the mouse cursor.  Does capture program window movement and everything else
 * 2. So far I cannot get bitmap quality images, I think the robot only gets compressed jpg ones. It doesn't
 * seem to matter what parameters I pass to the write() function in ImageIO 
 * 
 * @author Ryan J McCall
 *
 */
public class ScreenCaptureUtil{
	
	private Robot robot;
	private static Logger logger = Logger.getLogger("lida.util.ScreenCaptureUtil");
	private Rectangle areaToCapture;
	
	public ScreenCaptureUtil(){
		try {
			robot = new Robot();
			areaToCapture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		} catch (AWTException e) {
			logger.warning("Robot could not be created for screen capture!");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		System.out.println(new ScreenCaptureUtil().getScreenCapture().getType());
	}
	
	public ScreenCaptureUtil(Rectangle r){
		try {
			robot = new Robot();
			areaToCapture = r;
		} catch (AWTException e) {
			logger.warning("Robot could not be created for screen capture!");
			e.printStackTrace();
		}
	}

	public BufferedImage getScreenCapture() {
		return robot.createScreenCapture(areaToCapture);	
	}//method

}//class