package it.unibo.homemanager.humanpresence;

import org.opencv.core.Mat;

public class HumanDetectorFactory {

	public static HumanDetectorDifferential createDifferential(Mat firstFrame)
	{
		return new HumanDetectorDifferential(firstFrame,25);
	}
	public static HumanDetectorGaussian createGaussian()
	{
		return new HumanDetectorGaussian(1000,190);
	}
	public static HumanDetectorPixelDensity createPixelDensity()
	{
		return new HumanDetectorPixelDensity(100,200);
	}
	
}
