package it.unibo.homemanager.humanpresence;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.video.BackgroundSubtractorKNN;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;
/**
 * @author 0000628533
 *
 */
public class HumanDetectorPixelDensity implements HumanDetectorVideoInterface {

	private BackgroundSubtractorKNN subtractor=null;
	private List<Rect> lastSeenBounds=null;
	private List<Rect> tempBounds=null;
	private int min_area;
	private Mat mask;
	private boolean humanPresent;
	private Mat processedFrame=null;
	private VideoCapture camera=null;
	private String filePath;
	
	public HumanDetectorPixelDensity(String filePath,int history,double thresh)
	{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        subtractor = Video.createBackgroundSubtractorKNN(history,thresh,true);
		mask=new Mat();
		tempBounds=new ArrayList<Rect>();
		min_area=250;
		humanPresent=false;
		lastSeenBounds=new ArrayList<Rect>();
		processedFrame= new Mat();
		this.filePath=filePath;
	}
	public void init()throws IOException
	{
		this.camera = new VideoCapture(filePath);
    	if (!camera.isOpened())
    		throw new IOException("Camera not working");
	}
	@Override
	/*
	 * Semplicemente, qui continua a processare frame fino alla chiusura dello stream (processedframe empty)
	 */
	public void run()
	{
		processFrame();
		while(getCurrentProcessedFrame()!=null)
		{
			processFrame();
		}
	}
	
	/*Algoritmo pesante, basato su metodi bayesiani valutando ogni pixel
	  *1) Non ci saranno cambiamenti molto notevoli in luce e ombre, o grandi oggetti spostati 
	  *2) La posizione della telecamera non cambia
	  */
	private void processFrame() 
	{
		Mat frame= new Mat();
		camera.read(frame);
		if(frame.empty())
		{
			processedFrame=frame;
			camera.release();
			return;
		}
		 //un solo comando per creare una maschera che contiene i cambiamenti nel frame
		 subtractor.apply(frame, mask,0);
		 processedFrame= frame.clone();
         tempBounds=MovementDetectionUtilities.getHumanBounds(frame,mask, min_area);
         if(MovementDetectionUtilities.isRoomOccupied(tempBounds))
         {
        	 humanPresent=true;     
        	 lastSeenBounds=tempBounds;
        	 processedFrame=MovementDetectionUtilities.drawHumanRecognition(frame, lastSeenBounds);
         }
         else
         {
        	 humanPresent=false;
        	 for (Rect bound : lastSeenBounds)
     			if (bound.x>(frame.width())/2)
     			{
     				humanPresent=true;
     				//test: uso gli ultimi dati per continuare a disegnare il rettangolo verde
     				processedFrame=MovementDetectionUtilities.drawHumanRecognition(frame, lastSeenBounds);
     			}
          }
	}
	@Override
	public Mat getCurrentMask() {
		return mask;
	}

	@Override
	public BufferedImage getCurrentProcessedFrame() {
		if(processedFrame.empty())
			return null;
		return MovementDetectionUtilities.Mat2bufferedImage(processedFrame);
	}

	@Override
	public boolean isHumanPresent() {
		return humanPresent;
	}

}
