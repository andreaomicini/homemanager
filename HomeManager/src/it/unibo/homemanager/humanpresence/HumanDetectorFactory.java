package it.unibo.homemanager.humanpresence;

import java.net.URL;


public class HumanDetectorFactory {

	private final static URL resource= HumanDetectorFactory.class.getResource("/testvideo2b.mp4");
	private final static URL resource2= HumanDetectorFactory.class.getResource("/testvideo2c.mp4");
	private static int callnum=1;
	//Modifica: se viene chiamato una seconda volta, il path cambia.
	private static String filePath()
	{
		String osname=System.getProperty("os.name");
		boolean isWindows = osname.startsWith("Windows");
		if (isWindows && callnum==1)
			return resource.getPath().substring(1);
		else if (callnum==1)
			return resource.getPath();
		else if(isWindows)
			return resource2.getPath().substring(1);
		else
			return resource2.getPath();
	}
	
	public static HumanDetectorDifferential createDifferential()
	{
		callnum++;
		return new HumanDetectorDifferential(filePath(),25);
	}
	public static HumanDetectorGaussian createGaussian()
	{
		callnum++;
		return new HumanDetectorGaussian(filePath(),1000,190);
	}
	public static HumanDetectorPixelDensity createPixelDensity()
	{
		callnum++;
		return new HumanDetectorPixelDensity(filePath(),100,200);
	}
	
}
