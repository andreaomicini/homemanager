package com.examples.model;

public class Utente {
	
	
	private String username;
	private String password;
	private int id;
	
	
	public Utente(int idUser,String username, String pass) {
		super();
		
		this.username = username;
		this.password= pass;
		this.id=idUser;
	}
	
	
	public int getId() {
		return id;
	}


	public String getPassword() {
		return password;
	}


	public String getUsername() {
		return username;
	}
	
	

}
