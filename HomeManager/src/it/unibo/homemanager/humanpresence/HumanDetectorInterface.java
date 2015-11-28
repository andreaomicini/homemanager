package it.unibo.homemanager.humanpresence;

import java.awt.image.BufferedImage;

import org.opencv.core.Mat;
/**
 * @author 0000628533
 *
 */
public interface HumanDetectorInterface {

	public Mat getCurrentMask();
	public BufferedImage getCurrentProcessedFrame();
	public void processFrame(Mat frame);
	public boolean isHumanPresent();
}
