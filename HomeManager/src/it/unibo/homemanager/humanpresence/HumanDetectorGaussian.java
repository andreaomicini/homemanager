package it.unibo.homemanager.humanpresence;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
/**
 * @author 0000628533
 *
 */
public class HumanDetectorGaussian implements HumanDetectorInterface {

	BackgroundSubtractorMOG2 subtractor=null;
	List<Rect> lastSeenBounds=null;
	List<Rect> tempBounds=null;
	int min_area;
	Mat mask;
	boolean humanPresent;
	Mat processedFrame=null;
	
	public HumanDetectorGaussian(int history,double thresh)
	{
		subtractor = Video.createBackgroundSubtractorMOG2(history,thresh,true);
		mask=new Mat();
		tempBounds=new ArrayList<Rect>();
		min_area=250;
		humanPresent=false;
		lastSeenBounds=new ArrayList<Rect>();
		processedFrame= new Mat();
	}
	
	@Override
	/*Algoritmo pesante, basato su metodo a gaussiane presente in opencv
	  *1) Non ci saranno cambiamenti molto notevoli in luce e ombre, o grandi oggetti spostati 
	  *2) La posizione della telecamera non cambia
	  *
	  *Prende come input il frame attuale.
	  *Restituisce una matrice contenente la mask, ovvero una impronta delle figure in movimento
	  */
	public void processFrame(Mat frame)  
	{
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
		return MovementDetectionUtilities.Mat2bufferedImage(processedFrame);
	}

	@Override
	public boolean isHumanPresent() {
		return humanPresent;
	}

}
