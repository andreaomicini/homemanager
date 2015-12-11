package it.unibo.homemanager.humanpresence;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.opencv.core.Mat;
/**
 * @author 0000628533
 *
 */
public interface HumanDetectorVideoInterface extends HumanDetectorInterface,Runnable {

	public void init()throws IOException;
	public Mat getCurrentMask();
	public BufferedImage getCurrentProcessedFrame();

}
