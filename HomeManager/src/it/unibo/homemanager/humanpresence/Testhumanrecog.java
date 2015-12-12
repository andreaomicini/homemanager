package it.unibo.homemanager.humanpresence;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * @author 0000628533
 *
 */
public class Testhumanrecog {

	public static void main (String args[])
	{
		
		//Testing human detection
		JFrame jframe= new JFrame("Test Human Detection");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JLabel vidpanel = new JLabel();
	    jframe.setContentPane(vidpanel);
	    jframe.setSize(480, 360);
	    jframe.setVisible(true);
		
	    JFrame jframe2= new JFrame("Test Human Detection2");
		jframe2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JLabel vidpanel2 = new JLabel();
	    jframe2.setContentPane(vidpanel2);
	    jframe2.setSize(480, 360);
	    jframe2.setVisible(true);
	    
	    try 
	    {
	    	//HumanDetectorVideoInterface humanDetector= HumanDetectorFactory.createDifferential();
	    	//HumanDetectorVideoInterface humanDetector= HumanDetectorFactory.createPixelDensity();
	    	HumanDetectorVideoInterface humanDetector= HumanDetectorFactory.createGaussian();
	    	humanDetector.init();
	    	Thread humanDetectorThread=new Thread(humanDetector);
        	humanDetectorThread.start();
	    	while(true)
	    	{
            	vidpanel.setIcon(new ImageIcon(MovementDetectionUtilities.Mat2bufferedImage(humanDetector.getCurrentMask())));
            	vidpanel2.setIcon(new ImageIcon(humanDetector.getCurrentProcessedFrame()));
	    	}
		}
	    catch ( Exception e) 
	    {
			e.printStackTrace();
		}
		
	}
	
}
