package com.examples.model;

public class CondizionatoreData {
	
	private int id;
	private String alias;
    private boolean stato_attuale;
	private float consumo;
	private int temp;
	
	public CondizionatoreData(int id, String alias,float consumo) {
		super();
		this.id = id;
		this.alias = alias;
		this.stato_attuale=false;
		this.temp=0;
		this.consumo = consumo;
	}

	public int getTemp() {
		return temp;
	}

	public void setTemp(int f) {
		this.temp = f;
	}

	public boolean isStato_attuale() {
		return stato_attuale;
	}

	public void setStato_attuale(boolean stato_attuale) {
		this.stato_attuale = stato_attuale;
	}

	public int getId() {
		return id;
	}

	public String getAlias() {
		return alias;
	}



	public float getConsumo() {
		return consumo;
	}
	
	

}
