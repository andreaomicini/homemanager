package it.unibo.homemanager.humanpresence;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * @author 0000628533
 *
 */
public class HumanDetectorDifferential implements HumanDetectorInterface {

	Mat firstFrame=null;
	double thresh;
	List<Rect> lastSeenBounds=null;
	List<Rect> tempBounds=null;
	int min_area;
	Mat mask;
	boolean humanPresent;
	Mat processedFrame=null;
	
	public HumanDetectorDifferential(Mat firstFrame,double thresh)
	{
		this.firstFrame=firstFrame;
		Size sz = new Size(480, 360);
		//imposto le dimensioni il frame catturato
        Imgproc.resize(firstFrame, firstFrame, sz); 
                
        //converto il frame in scala di grigi
        Imgproc.cvtColor(firstFrame, firstFrame,Imgproc.COLOR_BGR2GRAY); 
                
        //uso Gaussian blur per "sfumarlo" eliminando rumori dell'immagine
        Imgproc.GaussianBlur(firstFrame, firstFrame, new Size(21,21), 0); 
            
		this.thresh=thresh;
		mask=new Mat();
		tempBounds=new ArrayList<Rect>();
		min_area=250;
		humanPresent=false;
		lastSeenBounds=new ArrayList<Rect>();
		processedFrame= new Mat();
	}
	
	@Override
	/*Algoritmo leggero, basato su semplice differenza tra il primo frame e tutti i successivi.
	  *Prendendo come ipotesi: 
	  *1) Il primo frame non contiene persone, ma solo lo sfondo della stanza da monitorare
	  *2) Non ci saranno cambiamenti notevoli in luce e ombre, o grandi oggetti spostati durante il monitoraggio
	  *3) La posizione della telecamera non cambia
	  *
	  *Prende come input il frame attuale
	  */
	public void processFrame(Mat frame) 
	{
		Mat greyFrame = new Mat();
		Mat frameDelta = new Mat();
		Mat thresholded = new Mat();
		Size sz = new Size(480, 360);
       
        //imposto le dimensioni il frame catturato
        Imgproc.resize(frame, frame, sz); 
                
        //converto il frame in scala di grigi
        Imgproc.cvtColor(frame, greyFrame,Imgproc.COLOR_BGR2GRAY); 
                
        //uso Gaussian blur per "sfumarlo" eliminando rumori dell'immagine
        Imgproc.GaussianBlur(greyFrame, greyFrame, new Size(21,21), 0); 
            
         //differenza assoluta tra il primo frame e il frame attuale, per evidenziare movimenti
         Core.absdiff(firstFrame,greyFrame,frameDelta); 
                
         //sogliatura, cioè segmentazione dell'immagine 
         Imgproc.threshold(frameDelta, thresholded, thresh, 255, Imgproc.THRESH_BINARY); 
                
         // dilatazione matematica
         Imgproc.dilate(thresholded, thresholded,new Mat(new Size(1,1),Imgproc.MORPH_RECT));
         
         mask = thresholded;
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
