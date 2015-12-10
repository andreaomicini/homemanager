package it.unibo.homemanager.humanpresence;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

/**
 * @author 0000628533
 *
 */
public class HumanDetectorDifferential implements HumanDetectorVideoInterface {

	private Mat firstFrame=null;
	private double thresh;
	private List<Rect> lastSeenBounds=null;
	private List<Rect> tempBounds=null;
	private int min_area;
	private Mat mask;
	private boolean humanPresent;
	private Mat processedFrame=null;
	private VideoCapture camera=null;
	private String filePath;
	
	public HumanDetectorDifferential(String filePath,double thresh)
	{
		this.thresh=thresh;
		mask=new Mat();
		tempBounds=new ArrayList<Rect>();
		min_area=250;
		humanPresent=false;
		lastSeenBounds=new ArrayList<Rect>();
		processedFrame= new Mat();
		this.filePath=filePath;
	}
	//Inizializza libreria openCV e primo frame
	public void init()throws IOException
	{

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
		firstFrame=new Mat();
		this.camera = new VideoCapture(filePath);
    	if (!camera.isOpened())
    		throw new IOException("Camera not working");
    	camera.read(firstFrame);

		Size sz = new Size(480, 360);
    	//imposto le dimensioni il frame catturato
        Imgproc.resize(firstFrame,firstFrame, sz); 
                
        //converto il frame in scala di grigi
        Imgproc.cvtColor(firstFrame, firstFrame,Imgproc.COLOR_BGR2GRAY); 
                
        //uso Gaussian blur per "sfumarlo" eliminando rumori dell'immagine
        Imgproc.GaussianBlur(firstFrame, firstFrame, new Size(21,21), 0); 
            
	}
	@Override
	/*Algoritmo leggero, basato su semplice differenza tra il primo frame e tutti i successivi.
	  *Prendendo come ipotesi: 
	  *1) Il primo frame non contiene persone, ma solo lo sfondo della stanza da monitorare
	  *2) Non ci saranno cambiamenti notevoli in luce e ombre, o grandi oggetti spostati durante il monitoraggio
	  *3) La posizione della telecamera non cambia
	  *
	  */
	public void processFrame() 
	{
		Mat greyFrame = new Mat();
		Mat frameDelta = new Mat();
		Mat thresholded = new Mat();
		Size sz = new Size(480, 360);
       
		Mat frame= new Mat();
		camera.read(frame);
		if(frame.empty())
		{
			processedFrame=frame;
			camera.release();
			return;
		}
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
		if(processedFrame.empty())
			return null;
		return MovementDetectionUtilities.Mat2bufferedImage(processedFrame);
	}

	@Override
	public boolean isHumanPresent() {
		return humanPresent;
	}

}
