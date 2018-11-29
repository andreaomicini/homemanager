package interfaces;

import exceptions.NotCreatedException;

public interface Sensor {

	/*
	 * Modella i permessi che hanno i sensori sui dispositivi smart:
	 * 1)Inserire un nuovo fact alla teoria principale
	 */
	
	/*
	 * begin: indica se si vuole inserire il fact in cima alla lista
	 * begin e functor sono parametri opzionali
	 */
	public void putFact(String theoryName, String fact, String functor, 
			boolean begin) throws NotCreatedException, IllegalArgumentException;
}
