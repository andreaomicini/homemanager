package it.unibo.homemanager.humanpresence;

import java.net.URL;


public class HumanDetectorFactory {

	private final static URL resource= Testhumanrecog.class.getResource("/testvideo2b.mp4");
	
	private static String filePath()
	{
		String osname=System.getProperty("os.name");
		boolean isWindows = osname.startsWith("Windows");
		if (isWindows)
			return resource.getPath().substring(1);
		else
			return resource.getPath();
	}
	public static HumanDetectorDifferential createDifferential()
	{
		return new HumanDetectorDifferential(filePath(),25);
	}
	public static HumanDetectorGaussian createGaussian()
	{
		return new HumanDetectorGaussian(filePath(),1000,190);
	}
	public static HumanDetectorPixelDensity createPixelDensity()
	{
		return new HumanDetectorPixelDensity(filePath(),100,200);
	}
	
}
