package it.unibo.homemanager.humanpresence;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

public class Testhumanrecog {
	
	static
	{ 
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
	}

	public static void main (String args[])
	{
		VideoCapture camera = new VideoCapture(
				"C:/Users/Ameli/Documents/homemanager/HomeManager/src/testvideo2a.mp4");
		
		if (!camera.isOpened())
			System.out.println("Error: camera not working");
		
		//Testing human detection
		JFrame jframe= new JFrame("Test Human Detection");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JLabel vidpanel = new JLabel();
	    jframe.setContentPane(vidpanel);
	    jframe.setSize(640, 520);
	    jframe.setVisible(true);
		
	    //parametri per segmentazione e eliminazione sfondo
	    double thresh=220;
	    int history=2000;
	    //parametro per human detection
	    int min_area=250;
	    
	    try 
	    {
			//differentialAlgorithm(camera,vidpanel,min_area,thresh,255);
	    	backgroundSubtractionMOG2Algorithm(camera, history, thresh, vidpanel,min_area);
		}
	    catch (NullPointerException | IOException e) 
	    {
			e.printStackTrace();
		}
		
	}
	
	//converte una matrice in una immagine jpg
	public static BufferedImage Mat2bufferedImage(Mat image) 
	{
	        MatOfByte bytemat = new MatOfByte();
	        Imgcodecs.imencode(".jpg", image, bytemat);
	        byte[] bytes = bytemat.toArray();
	        InputStream in = new ByteArrayInputStream(bytes);
	        BufferedImage img = null;
	        try 
	        {
	            img = ImageIO.read(in);
	        } 
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        }
	        return img;
	 }
	 
	//disegna un rettangolo intorno alla figura umana rilevata. 
	 public static Mat drawHumanRecognition(Mat frame,Mat mask,int min_area)
	 {
		 //trovo i contorni delle figure in movimento
         List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
         Mat hierarchy= new Mat();
         Mat result=frame;
         Imgproc.findContours(mask.clone(), contours, hierarchy, 
         		Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
         
         //loop sui contorni
         Rect bounds;
         for (MatOfPoint c : contours)
         {
         	//ignoriamo i contorni troppo piccoli
         	if (Imgproc.contourArea(c)<min_area)
         		continue;
         	//ottengo i limiti del contorno
         	bounds=Imgproc.boundingRect(c);
         	result=frame.clone();
         	// diseegno un rettangolo verde (0,255,0) di spessore 2 sul frame attuale
         	Imgproc.rectangle(result, new Point(bounds.x,bounds.y), 
         			new Point(bounds.x+bounds.width, bounds.y+bounds.height),new Scalar(0,255,0),2 );
          }
         return result;
	 }
	 
	 /*Algoritmo 1: leggero, basato su semplice differenza tra il primo frame e tutti i successivi.
	  *Prendendo come ipotesi: 
	  *1) Il primo frame non contiene persone, ma solo lo sfondo della stanza da monitorare
	  *2) Non ci saranno cambiamenti notevoli in luce e ombre, o grandi oggetti spostati durante il monitoraggio
	  *3) La posizione della telecamera non cambia
	  */
	 public static void differentialAlgorithm(VideoCapture camera,JLabel vidpanel,int min_area,
			 double thresh) throws IOException,NullPointerException
	 {
		Mat frame= new Mat();
		Mat result= new Mat();
		Mat firstFrame = new Mat();
		Mat gray = new Mat();
		Mat frameDelta = new Mat();
		Mat thresholded = new Mat();
		Size sz = new Size(640, 480);

		String roomState = "Unoccupied";
        
        //loop sul video
        while (true) 
        {
            if (camera.read(frame)) 
            {
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
                
                //trovo i contorni delle figure in movimento e ci disegno un rettangolo verde
                result=drawHumanRecognition(frame,thresholded,min_area);
                if(!result.equals(frame))
                	roomState="Occupied";
                else
                	roomState="Unoccupied";
                
                //scrivo lo stato della stanza sul frame
                Imgproc.putText(result, "Room state: "+roomState, 
                		new Point (10,20),Core.FONT_HERSHEY_SIMPLEX, 0.35, new Scalar(0,0,255),1);
             
                //Il frame viene mostrato
                ImageIcon image = new ImageIcon(Mat2bufferedImage(result));
                vidpanel.setIcon(image);
                vidpanel.repaint();
            }
            else
            {
            	camera.release();
            	break;
            }
        }
	 }
	 public static void backgroundSubtractionMOG2Algorithm(VideoCapture camera,
			 int history,double thresh,JLabel vidpanel,int min_area) throws IOException,NullPointerException
	 {
		 BackgroundSubtractorMOG2 subtractor = Video.createBackgroundSubtractorMOG2(history,thresh,true);
		 Mat frame= new Mat();
		 Mat fmask = new Mat();
		 Mat result = new Mat();
		 
		 String roomState= "Unoccupied";
		 while(true)
		 {
			 if (camera.read(frame))
			 {
				//un solo comando per creare una maschera che contiene i cambiamenti nel frame
				subtractor.apply(frame, fmask,0);
				result = drawHumanRecognition(frame, fmask, min_area);
				if(!result.equals(frame))
                	roomState="Occupied";
                else
                	roomState="Unoccupied";
				//scrivo lo stato della stanza sul frame, in questo test tramite opencv
                Imgproc.putText(result, "Room state: "+roomState, 
                		new Point (10,20),Core.FONT_HERSHEY_SIMPLEX, 0.35, new Scalar(0,0,255),1);
				vidpanel.setIcon(new ImageIcon(Mat2bufferedImage(result)));	
			 }
			 else
			 {
				 camera.release();
				 break;
			 }
		 }
		 
	 }
}
