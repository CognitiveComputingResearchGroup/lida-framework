package edu.memphis.ccrg.lida.util;

import ij.ImagePlus;

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
	
	private static Logger logger = Logger.getLogger("lida.util.ScreenCaptureUtil");
	
	/**
	 * Java utility
	 */
	private Robot robot;
	
	/**
	 * Area in your screen where the robot will capture from
	 */
	private Rectangle areaToCapture;
	
	public ScreenCaptureUtil(){
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
	public ScreenCaptureUtil(Rectangle r){
		try {
			robot = new Robot();
			areaToCapture = r;
		} catch (AWTException e) {
			logger.warning("Robot could not be created for screen capture!");
		}
	}

	public BufferedImage getScreenCapture() {
		return robot.createScreenCapture(areaToCapture);	
	}//method
	
	public static void main(String[] args){
		ScreenCaptureUtil util = new ScreenCaptureUtil();
		long start = System.currentTimeMillis();
		BufferedImage img = util.getScreenCapture();
		//img.
		long finish = System.currentTimeMillis();
		System.out.println(finish - start);
		
		System.out.println("height: " + img.getHeight());
		System.out.println("width: " + img.getWidth());
		System.out.println("min tile x: " + img.getMinTileX());
		System.out.println("min tile y: " + img.getMinTileY());
		System.out.println("min x: " + img.getMinX());
		System.out.println("min y: " + img.getMinY());
		System.out.println("num x tiles: " + img.getNumXTiles());
		System.out.println("num y tiles: " + img.getNumYTiles());
		
		System.out.println(img.getColorModel());
		
		ImagePlus imgPlus = new ImagePlus("test", img);
		
//		for(int i = 0; i < img.getWidth(); i++){
//			for(int j = 0; j < img.getHeight(); j++){
//				System.out.println(img.getRGB(i, j) + " ");
//			}
//			System.out.println();
//		}
		
	    /**
	     * Represents an image with 8-bit RGB color components packed into
	     * integer pixels.  The image has a {@link DirectColorModel} without
	     * alpha.
	     * When data with non-opaque alpha is stored
	     * in an image of this type,
	     * the color data must be adjusted to a non-premultiplied form
	     * and the alpha discarded,
	     * as described in the
	     * {@link java.awt.AlphaComposite} documentation.
	     */
	    //public static final int TYPE_INT_RGB = 1;
	}

}//class