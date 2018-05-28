package lpaas;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import exceptions.AlreadyCreatedException;
import exceptions.ExecutionFailureException;
import exceptions.NotCreatedException;
import interfaces.Configurator;
import parser.JsonParser;

public class LPaaSConfigurator implements Configurator {

	private URL url;
	private String component;
	private String token;
	
	public LPaaSConfigurator(URL url, String component, String token) {
		super();
		this.url = url;
		this.component = component;
		this.token = token;
	}

	@Override
	public void createTheory(String theoryName, String[] theories) throws AlreadyCreatedException, ExecutionFailureException{
		if(theoryName==null || theoryName.isEmpty() || theories==null || !verifica(theories))
			throw new IllegalArgumentException("Parameters Exception");
		
		HttpURLConnection connection=null;
		URL indirizzo=null;
		int responseCode=-1;
		
		do{
			if(responseCode==401)
				reNew();
			
			try{
				indirizzo=new URL(this.url+"/theories?name="+theoryName);
				connection= (HttpURLConnection) indirizzo.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-type", "text/plain");
				connection.setRequestProperty("Authorization", "JWT "+this.token);
				
				for (String s : theories){
					connection.getOutputStream().write(s.getBytes());
					connection.getOutputStream().write(10);
				}
				
				responseCode=connection.getResponseCode();
				if(responseCode!=200 && responseCode!=401){
					if(responseCode==409)
						throw new AlreadyCreatedException();
					else
						throw new ExecutionFailureException();
				}
				
			}catch(IOException e){
				throw new ExecutionFailureException();
			}
		}while(responseCode==401);
	}

	@Override
	public void addTheoryVersion(String theoryName, String[] theories) throws NotCreatedException, ExecutionFailureException{
		if(theoryName==null || theoryName.isEmpty() || theories==null || !verifica(theories))
			throw new IllegalArgumentException("Parameters Exception");
		
		HttpURLConnection connection=null;
		URL indirizzo=null;
		int responseCode=-1;
		
		do{
			if(responseCode==401)
				reNew();
			
			try{
				indirizzo=new URL(this.url+"/theories/"+theoryName);
				connection= (HttpURLConnection) indirizzo.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-type", "text/plain");
				connection.setRequestProperty("Authorization", "JWT "+this.token);
				
				for (String s : theories){
					connection.getOutputStream().write(s.getBytes());
					connection.getOutputStream().write(10);
				}
				
				responseCode=connection.getResponseCode();
				if(responseCode!=200 && responseCode!=401){
						throw new ExecutionFailureException();
				}
				
			}catch(IOException e){
				throw new ExecutionFailureException();
			}
		}while(responseCode==401);
	}

	@Override
	public void removeTheory(String theoryName) throws ExecutionFailureException, NotCreatedException, IllegalArgumentException {
		if(theoryName==null || theoryName.length()==0)
			throw new IllegalArgumentException("Parameters Exception");
		
		HttpURLConnection connection=null;
		URL indirizzo=null;
		int responseCode=-1;
		
		do{
			if(responseCode==401)
				reNew();
			
			try{
				indirizzo=new URL(this.url+"/theories/"+theoryName);
				connection= (HttpURLConnection) indirizzo.openConnection();
				connection.setRequestMethod("DELETE");
				connection.setDoInput(true);
				connection.setRequestProperty("Content-type", "text/plain");
				connection.setRequestProperty("Authorization", "JWT "+this.token);
				
				responseCode=connection.getResponseCode();
				if(responseCode!=200 && responseCode!=204 && responseCode!=401){
					System.out.println("ERROR CODE: "+responseCode);
					if(responseCode==404)
						throw new NotCreatedException();
					else
						throw new ExecutionFailureException();
				}
				
			}catch(IOException e){
				throw new ExecutionFailureException();
			}
			
		}while(responseCode==401);

	}
	
	@Override
	public void replaceTheory(String theoryName, String[] theories) throws IllegalArgumentException, ExecutionFailureException, NotCreatedException {
		if(theoryName==null || theoryName.length()==0 || theories==null || !verifica(theories))
			throw new IllegalArgumentException("Parameters Exception");
		
		this.removeTheory(theoryName);
		try {
			this.createTheory(theoryName, theories);
		} catch (AlreadyCreatedException e) {}
		
	}

	@Override
	public void createGoalList(String goalName, String[] goals) throws AlreadyCreatedException, ExecutionFailureException, IllegalArgumentException {
		if(goalName==null || goalName.length()==0 || goals==null || !verifica(goals))
			throw new IllegalArgumentException("Parameters Exception");
		
		HttpURLConnection connection=null;
		URL indirizzo=null;
		int responseCode=-1;
		
		do{
			if(responseCode==401)
				reNew();
			
			try{
				indirizzo=new URL(this.url+"/goals?name="+goalName);
				connection= (HttpURLConnection) indirizzo.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-type", "text/plain");
				connection.setRequestProperty("Authorization", "JWT "+this.token);
				
				OutputStream writer=connection.getOutputStream();
				for(int i=0;i<goals.length;i++){
					if(goals[i].endsWith("."))
						goals[i]=goals[i].substring(0, goals[i].length()-2);
					writer.write(goals[i].getBytes());
					if(i!=goals.length-1)
						writer.write(",".getBytes());
				}
				
				responseCode=connection.getResponseCode();
				if(responseCode!=200 && responseCode!=204 && responseCode!=401){
					System.out.println("ERROR CODE: "+responseCode);
					if(responseCode==409)
						throw new AlreadyCreatedException();
					
					throw new ExecutionFailureException();
				}
				
			}catch(IOException e){
				throw new ExecutionFailureException();
			}
			
		}while(responseCode==401);
	}
	

	@Override
	public void addGoals(String goalName, String[] goals) throws ExecutionFailureException, NotCreatedException {
		if(goalName==null || goalName.length()==0 || goals==null || !verifica(goals))
			throw new IllegalArgumentException("Parameters Exception");
		
		HttpURLConnection connection=null;
		URL indirizzo=null;
		int responseCode=-1;
		
		do{
			if(responseCode==401)
				reNew();
			
			try{
				indirizzo=new URL(this.url+"/goals/"+goalName);
				connection= (HttpURLConnection) indirizzo.openConnection();
				connection.setRequestMethod("POST");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-type", "text/plain");
				connection.setRequestProperty("Authorization", "JWT "+this.token);
				
				OutputStream writer=connection.getOutputStream();
				for(int i=0;i<goals.length;i++){
					if(goals[i].endsWith("."))
						goals[i]=goals[i].substring(0, goals[i].length()-2);
					writer.write(goals[i].getBytes());
					if(i!=goals.length-1)
						writer.write(",".getBytes());
				}
				
				responseCode=connection.getResponseCode();
				if(responseCode!=200 && responseCode!=204 && responseCode!=401){
					System.out.println("ERROR CODE: "+responseCode);
					if(responseCode==404)
						throw new NotCreatedException();
					
					throw new ExecutionFailureException();
				}
				
			}catch(IOException e){
				throw new ExecutionFailureException();
			}
			
		}while(responseCode==401);
	}

	@Override
	public void replaceGoal(String goalName, String[] goals) throws NotCreatedException, ExecutionFailureException, IllegalArgumentException {
		if(goalName==null || goalName.length()==0 || goals==null || !verifica(goals))
			throw new IllegalArgumentException("Parameters Exception");
		
		HttpURLConnection connection=null;
		URL indirizzo=null;
		int responseCode=-1;
		
		do{
			if(responseCode==401)
				reNew();
			
			try{
				indirizzo=new URL(this.url+"/goals/"+goalName);
				connection= (HttpURLConnection) indirizzo.openConnection();
				connection.setRequestMethod("PUT");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestProperty("Content-type", "text/plain");
				connection.setRequestProperty("Authorization", "JWT "+this.token);
				
				OutputStream writer=connection.getOutputStream();
				for(int i=0;i<goals.length;i++){
					if(goals[i].endsWith("."))
						goals[i]=goals[i].substring(0, goals[i].length()-2);
					writer.write(goals[i].getBytes());
					if(i!=goals.length-1)
						writer.write(",".getBytes());
				}
				
				responseCode=connection.getResponseCode();
				if(responseCode!=200 && responseCode!=204 && responseCode!=401){
					System.out.println("ERROR CODE: "+responseCode);
					if(responseCode==404)
						throw new NotCreatedException();
					
					throw new ExecutionFailureException();
				}
				
			}catch(IOException e){
				throw new ExecutionFailureException();
			}
			
		}while(responseCode==401);
	}

	@Override
	public void removeGoal(String goalName) throws NotCreatedException, ExecutionFailureException, IllegalArgumentException {
		if(goalName==null || goalName.length()==0)
			throw new IllegalArgumentException("Parameters Exception");
		
		HttpURLConnection connection=null;
		URL indirizzo=null;
		int responseCode=-1;
		
		do{
			if(responseCode==401)
				reNew();
			
			try{
				indirizzo=new URL(this.url+"/goals/"+goalName);
				connection= (HttpURLConnection) indirizzo.openConnection();
				connection.setRequestMethod("DELETE");
				connection.setDoInput(true);
				connection.setRequestProperty("Content-type", "text/plain");
				connection.setRequestProperty("Authorization", "JWT "+this.token);
				
				responseCode=connection.getResponseCode();
				if(responseCode!=200 && responseCode!=204 && responseCode!=401){
					System.out.println("ERROR CODE: "+responseCode);
					if(responseCode==404)
						throw new NotCreatedException();
					
					throw new ExecutionFailureException();
				}
				
			}catch(IOException e){
				e.printStackTrace();
				throw new ExecutionFailureException();
			}
			
		}while(responseCode==401);
	}
	
	
	public String toString(){
		return component;
	}
	
	
	
	private void reNew() throws ExecutionFailureException{
		String username="configurator";
		String password="C0nfigurator@";
		HttpURLConnection connection=null;
		String token=null;
		URL indirizzo=null;
		
		try{
			indirizzo=new URL(this.url+"/auth");
			connection= (HttpURLConnection) indirizzo.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
		
			String jsonMessage="{\"password\":"+password+",\"username\":"+username+"}";
			connection.getOutputStream().write(jsonMessage.getBytes());
			//System.out.println(obj.toJSONString());
			
			int responseCode=connection.getResponseCode();
			if(responseCode!=200){
				System.out.println("CODE: "+responseCode);
				System.out.println("Errore! Accertarsi che le credenziali siano corrette");
			}
			
			token= new JsonParser().parseToken(connection.getInputStream());
			
			//Chiusura risorse
			connection.disconnect();
			
			this.token=token;
		}catch(IOException e){
			throw new ExecutionFailureException();
		}
	}
	
	private boolean verifica(String[] array){
		for(String s : array)
			if(s==null)
				return false;
		
		return true;
	}
}
