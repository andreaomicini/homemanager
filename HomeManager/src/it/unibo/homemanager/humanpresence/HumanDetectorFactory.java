package it.unibo.homemanager.humanpresence;

import java.net.URL;


public class HumanDetectorFactory {

	private final static URL resource= Testhumanrecog.class.getResource("/testvideo2b.mp4");
	private final static String filePath=resource.getPath().substring(1);;
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
