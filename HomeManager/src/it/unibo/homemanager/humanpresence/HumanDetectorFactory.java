package it.unibo.homemanager.humanpresence;

import org.opencv.core.Mat;

public class HumanDetectorFactory {

	private final static String filePath="C:/Users/Ameli/Documents/homemanager/HomeManager/src/testvideo2b.mp4";
	public static HumanDetectorDifferential createDifferential()
	{
		return new HumanDetectorDifferential(filePath,25);
	}
	public static HumanDetectorGaussian createGaussian()
	{
		return new HumanDetectorGaussian(filePath,1000,190);
	}
	public static HumanDetectorPixelDensity createPixelDensity()
	{
		return new HumanDetectorPixelDensity(filePath,100,200);
	}
	
}
