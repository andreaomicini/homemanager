package it.unibo.homemanager.humanpresence;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorKNN;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
/**
 * @author 0000628533
 *
 */
public class Testhumanrecog {
	
	static
	{ 
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
	}

	public static void main (String args[])
	{
		VideoCapture camera = new VideoCapture(
				"C:/Users/Ameli/Documents/homemanager/HomeManager/src/testvideo2b.mp4");
		
		if (!camera.isOpened())
			System.out.println("Error: camera not working");
		
		//Testing human detection
		JFrame jframe= new JFrame("Test Human Detection");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JLabel vidpanel = new JLabel();
	    jframe.setContentPane(vidpanel);
	    jframe.setSize(480, 360);
	    jframe.setVisible(true);
		
	    JFrame jframe2= new JFrame("Test Human Detection2");
		jframe2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JLabel vidpanel2 = new JLabel();
	    jframe2.setContentPane(vidpanel2);
	    jframe2.setSize(480, 360);
	    jframe2.setVisible(true);
	    //parametri per segmentazione e eliminazione sfondo
	    double thresh=20;
	    int history=100;
	    //parametro per human detection
	    int min_area=250;
	    
	    try 
	    {
	    	//Test senza elaborazione frame
			//differentialAlgorithm(camera,min_area,thresh);
	    	//backgroundSubtractionKNNAlgorithm(camera, history, thresh,min_area);
	    	//backgroundSubtractionMOG2Algorithm(camera, history, thresh,min_area);
	    	
	    	
	    	//test con elaborazione frame
	    	Mat firstFrame=new Mat();
	    	Mat frame= new Mat();
	    	camera.read(firstFrame);
	    	//HumanDetectorInterface humanDetector= HumanDetectorFactory.createDifferential(firstFrame);
	    	HumanDetectorVideoInterface humanDetector= HumanDetectorFactory.createPixelDensity();
	    	//HumanDetectorInterface humanDetector= HumanDetectorFactory.createGaussian();
	    	long average=0;
	    	long total=0;
	    	int i=1;
	    	while(true)
	    	{
	    		//Test prestazioni
            	long t1,t2;
            	t1=new Date().getTime();
	    		camera.read(frame);
            	if (frame.empty())
            		break;
            	humanDetector.processFrame();
            	vidpanel.setIcon(new ImageIcon(MovementDetectionUtilities.Mat2bufferedImage(humanDetector.getCurrentMask())));
            	vidpanel2.setIcon(new ImageIcon(humanDetector.getCurrentProcessedFrame()));
            	t2=new Date().getTime();
                System.out.println("frame processed in "+(t2-t1)+" ms");
                total=total+t2-t1;
                average=total/i;
                i++;
                System.out.println("average: "+average);
	    	}
		}
	    catch ( Exception e) 
	    {
			e.printStackTrace();
		}
		
	}
	
	 public static void differentialAlgorithm(VideoCapture camera,int min_area,
			 double thresh) throws IOException,NullPointerException
	 {
		Mat frame= new Mat();
		Mat firstFrame = new Mat();
		Mat gray = new Mat();
		Mat frameDelta = new Mat();
		Mat thresholded = new Mat();
		Size sz = new Size(640, 480);
		long total=0;
		long average=0;
		int i=1;
		
        
        //loop sul video
        while (true) 
        {
            if (camera.read(frame)) 
            {
            	//Test prestazioni
            	long t1,t2;
            	t1=new Date().getTime();
            	//imposto le dimensioni il frame catturato
                Imgproc.resize(frame, frame, sz); 
                
                //converto il frame in scala di grigi
                Imgproc.cvtColor(frame, gray,Imgproc.COLOR_BGR2GRAY); 
                
                //uso Gaussian blur per "sfumarlo"
                Imgproc.GaussianBlur(gray, gray, new Size(21,21), 0); 
                
                if (firstFrame.empty()) // si basa sull'ipotesi che il primo frame sia lo standard, senza persone
                {
                	firstFrame=gray.clone();
                	continue;
                }
                
                //differenza assoluta tra il primo frame e il frame attuale, per evidenziare movimenti
                Core.absdiff(firstFrame,gray,frameDelta); 
                
                //sogliatura, cioè segmentazione dell'immagine 
                Imgproc.threshold(frameDelta, thresholded, thresh, 255, Imgproc.THRESH_BINARY); 
                
                // dilatazione matematica
                Imgproc.dilate(thresholded, thresholded,new Mat(new Size(1,1),Imgproc.MORPH_RECT));
                
                t2=new Date().getTime();
                System.out.println("frame processed in "+(t2-t1)+" ms");
                total=total+t2-t1;
                average=total/i;
                i++;
                System.out.println("average: "+average);
            }
            else
            {
            	camera.release();
            	break;
            }
        }
	 }
	 public static void backgroundSubtractionMOG2Algorithm(VideoCapture camera,
			 int history,double thresh,int min_area) throws IOException,NullPointerException
	 {
		 BackgroundSubtractorMOG2 subtractor = Video.createBackgroundSubtractorMOG2(history,thresh,true);
		 
		 Mat frame= new Mat();
		 Mat fmask = new Mat();
		long total=0;
		long average=0;
		int i=1;
		 while(true)
		 {
			 if (camera.read(frame))
			 {
				//Test prestazioni
	            long t1,t2;
	            t1=new Date().getTime();
				//un solo comando per creare una maschera che contiene i cambiamenti nel frame
				subtractor.apply(frame, fmask,0);
				t2=new Date().getTime();
				 System.out.println("frame processed in "+(t2-t1)+" ms");
	                total=total+t2-t1;
	                average=total/i;
	                i++;
	                System.out.println("average: "+average);
			 }
			 else
			 {
				 camera.release();
				 break;
			 }
		 }
		 
	 }
	 
	 public static void backgroundSubtractionKNNAlgorithm(VideoCapture camera,
			 int history,double thresh,int min_area) throws IOException,NullPointerException
	 {
		 BackgroundSubtractorKNN subtractor = Video.createBackgroundSubtractorKNN(history,thresh,true);
		 
		 Mat frame= new Mat();
		 Mat fmask = new Mat();
		 long total=0;
		 long average=0;
		 int i=1;
		 
		 while(true)
		 {
			 if (camera.read(frame))
			 {
				//Test prestazioni
		        long t1,t2;
		        t1=new Date().getTime();
				//un solo comando per creare una maschera che contiene i cambiamenti nel frame
				subtractor.apply(frame, fmask,0);
				t2=new Date().getTime();
				 System.out.println("frame processed in "+(t2-t1)+" ms");
	                total=total+t2-t1;
	                average=total/i;
	                i++;
	                System.out.println("average: "+average);
			 }
			 else
			 {
				 camera.release();
				 break;
			 }
		 }
		 
	 }
}
