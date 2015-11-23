package it.unibo.homemanager.humanpresence;

import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractorKNN;
import org.opencv.video.Video;
/**
 * @author 0000628533
 *
 */
public class HumanDetectorBayesian implements HumanDetectorInterface {

	BackgroundSubtractorKNN subtractor=null;
	public HumanDetectorBayesian(int history,double thresh)
	{
		subtractor = Video.createBackgroundSubtractorKNN(history,thresh,true);
	}
	@Override
	/*Algoritmo pesante, basato su metodi bayesiani valutando ogni pixel
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
