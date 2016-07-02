package com.examples.model;

import java.io.Serializable;

public class Utente implements Serializable {
	
	
	 public int idUser, temp_heat, temp_ac;
	  public String firstname, surname, username, pwd, activate;
	  public String role;

	  
	  public Utente(int id, String fn, String sn, String un, String p, String r,
	          int th, int tac, String act) {                  

	    idUser = id;
	    firstname = fn;
	    surname = sn;
	    username = un;
	    pwd = p;
	    role = r;
	    temp_heat = th;
	    temp_ac = tac;
	    activate = act;

	  }

	public String getFirstname() {
		return firstname;
	}

	public String getRole() {
		return role;
	}

	public int getIdUser() {
		return idUser;
	}


	public String getUsername() {
		return username;
	}
	  
	  
	

}
