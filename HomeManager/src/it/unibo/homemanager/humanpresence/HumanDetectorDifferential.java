package it.unibo.homemanager.humanpresence;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * @author 0000628533
 *
 */
public class HumanDetectorDifferential implements HumanDetectorInterface {

	Mat firstFrame=null;
	double thresh;
	public HumanDetectorDifferential(Mat firstFrame,double thresh)
	{
		this.firstFrame=firstFrame;
		Size sz = new Size(640, 480);
		//imposto le dimensioni il frame catturato
        Imgproc.resize(firstFrame, firstFrame, sz); 
                
        //converto il frame in scala di grigi
        Imgproc.cvtColor(firstFrame, firstFrame,Imgproc.COLOR_BGR2GRAY); 
                
        //uso Gaussian blur per "sfumarlo" eliminando rumori dell'immagine
        Imgproc.GaussianBlur(firstFrame, firstFrame, new Size(21,21), 0); 
            
		this.thresh=thresh;
	}
	
	@Override
	/*Algoritmo leggero, basato su semplice differenza tra il primo frame e tutti i successivi.
	  *Prendendo come ipotesi: 
	  *1) Il primo frame non contiene persone, ma solo lo sfondo della stanza da monitorare
	  *2) Non ci saranno cambiamenti notevoli in luce e ombre, o grandi oggetti spostati durante il monitoraggio
	  *3) La posizione della telecamera non cambia
	  *
	  *Prende come input il frame attuale
	  *Restituisce una matrice contenente la mask, ovvero una impronta delle figure in movimento
	  */
	public Mat getMask(Mat frame) 
	{
		Mat greyFrame = new Mat();
		Mat frameDelta = new Mat();
		Mat thresholded = new Mat();
		Size sz = new Size(640, 480);
       
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
                
         return thresholded;
	}

}
