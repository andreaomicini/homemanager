package com.examples.model;

import java.io.Serializable;

public class DeviceData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	 public int idDev, roomId;
	  public String alias, param;
	  public String type;
	  public float energy;
	  private boolean stato_attuale;
		private int temp;
	  
	  /** Creates a new instance of Device */
	  public DeviceData(int id, String nm, float e, String p, String t, int ri) {                  

	    idDev = id;
	    alias = nm;
	    energy = e;
	    param = p;
	    type = t;
	    roomId = ri;
	  }
	
	


	public float getConsumo() {
		return energy;
	}

	public void setConsumo(float consumo) {
		this.energy = consumo;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getId() {
		return idDev;
	}

	public void setId(int id) {
		this.idDev = id;
	}

	public boolean isStato_attuale() {
		return stato_attuale;
	}

	public void setStato_attuale(boolean stato_attuale) {
		this.stato_attuale = stato_attuale;
	}

	public int getTemp() {
		return temp;
	}

	public void setTemp(int temp) {
		this.temp = temp;
	}
		
	
}

