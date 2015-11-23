package it.unibo.homemanager.humanpresence;

import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
/**
 * @author 0000628533
 *
 */
public class HumanDetectorGaussian implements HumanDetectorInterface {

	BackgroundSubtractorMOG2 subtractor=null;
	public HumanDetectorGaussian(int history,double thresh)
	{
		subtractor = Video.createBackgroundSubtractorMOG2(history,thresh,true);
	}
	
	@Override
	/*Algoritmo pesante, basato su metodo a gaussiane presente in opencv
	  *1) Non ci saranno cambiamenti molto notevoli in luce e ombre, o grandi oggetti spostati 
	  *2) La posizione della telecamera non cambia
	  *
	  *Prende come input il frame attuale.
	  *Restituisce una matrice contenente la mask, ovvero una impronta delle figure in movimento
	  */
	public Mat getMask(Mat frame) 
	{
		Mat fmask = new Mat();
		//un solo comando per creare una maschera che contiene i cambiamenti nel frame
		subtractor.apply(frame, fmask,0);
		return fmask;
	}

}
