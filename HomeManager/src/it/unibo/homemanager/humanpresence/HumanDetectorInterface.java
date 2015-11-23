package it.unibo.homemanager.humanpresence;

import org.opencv.core.Mat;
/**
 * @author 0000628533
 *
 */
public interface HumanDetectorInterface {

	public Mat getMask(Mat frame);
}
