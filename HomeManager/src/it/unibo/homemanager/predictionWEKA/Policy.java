package it.unibo.homemanager.predictionWEKA;



public class Policy {
	
	private int id_stanza;
	private int giorno_settimana;
	private int orario_start;
	private int orario_finish;
	private String stato_condizionatore;
	
	

	public Policy(int id_stanza, int giorno_settimana, int orario_start, int orario_finish,String stato_condizionatore) {
		super();
		this.id_stanza = id_stanza;
		this.giorno_settimana = giorno_settimana;
		this.orario_start = orario_start;
		this.orario_finish = orario_finish;
	
		this.stato_condizionatore = stato_condizionatore;
	}



	public int getGiorno_settimana() {
		return giorno_settimana;
	}



	public int getOrario_start() {
		return orario_start;
	}



	public int getOrario_finish() {
		return orario_finish;
	}



	



	public String getStato_condizionatore() {
		return stato_condizionatore;
	}



	@Override
	public String toString() {
		return "Policy [id_stanza=" + id_stanza + ", giorno_settimana=" + giorno_settimana + ", orario_start="
				+ orario_start + ", orario_finish=" + orario_finish + ", stato_condizionatore=" + stato_condizionatore
				+ "]";
	}




	
	
	
	
	
	
	
	

}
