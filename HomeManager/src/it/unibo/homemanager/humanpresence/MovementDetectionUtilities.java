package it.unibo.homemanager.humanpresence;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @author 0000628533
 *
 */
public class MovementDetectionUtilities {
	
	
		//Converte una matrice in una immagine jpg tramite Imgcodecs
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
		public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
		    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		    Graphics2D g2d = dimg.createGraphics();
		    g2d.drawImage(tmp, 0, 0, null);
		    g2d.dispose();

		    return dimg;
		}  
		 
		 public static boolean isRoomOccupied(List<Rect> bounds)
		 {
			 return !bounds.isEmpty();
		 }
		//Disegna un rettangolo intorno alla figura umana, se rilevata. 
		 public static List<Rect> getHumanBounds(Mat frame,Mat mask,int min_area)
		 {
	         List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	         Mat hierarchy= new Mat();
	         Imgproc.findContours(mask.clone(), contours, hierarchy, 
	         		Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
	         
	         //loop sui contorni
	         List<Rect> bounds=new ArrayList<Rect>();
	         for (MatOfPoint c : contours)
	         {
	         	//ignoriamo i contorni troppo piccoli
	         	if (Imgproc.contourArea(c)<min_area)
	         		continue;
	         	//ottengo i limiti del contorno
	         	bounds.add(Imgproc.boundingRect(c));
	          }
	         return bounds;
		 }
		 
		//Disegna un rettangolo intorno ai bounds
		 public static Mat drawHumanRecognition(Mat frame,List<Rect> bounds)
		 {
			 Mat result=frame.clone();
	         for (Rect bound : bounds)
	         {
	         	// disegno un rettangolo verde (0,255,0) di spessore 2 sul frame attuale
	         	Imgproc.rectangle(result, new Point(bound.x,bound.y), 
	         			new Point(bound.x+bound.width, bound.y+bound.height),new Scalar(0,255,0),2 );
	          }
	         return result;
		 }
		 
	
}
