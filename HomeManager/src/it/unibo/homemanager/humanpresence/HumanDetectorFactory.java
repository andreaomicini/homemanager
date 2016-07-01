package it.unibo.homemanager.humanpresence;

public class HumanDetectorFactory {
	
	public static HumanDetectorDifferential createDifferential()
	{
		return new HumanDetectorDifferential(25);
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
