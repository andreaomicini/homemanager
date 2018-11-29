package lpaas;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import exceptions.AlreadyRegisteredException;
import exceptions.AuthorizationFailureException;
import exceptions.NotAuthorizedException;
import exceptions.NotRegisteredException;
import interfaces.Client;
import interfaces.Configurator;
import interfaces.Sensor;
import interfaces.SmartComponentManager;
import parser.JsonParser;

public class LPaaSComponentManager implements SmartComponentManager {

	private Map<String,URL> table;
	private static LPaaSComponentManager instance=null;
	
	
	public static LPaaSComponentManager getInstance(){
		if(instance==null)
			instance=new LPaaSComponentManager();
		return instance;
	}
	
	private LPaaSComponentManager(){
		table=new HashMap<String,URL>();
	}
	
	@Override
	public void register(String component, String serverLocation) throws AlreadyRegisteredException, IllegalArgumentException
	{
		if(component==null || serverLocation==null || component.length()==0)
			throw new IllegalArgumentException("Parameters Error!");
		
		//HttpURLConnection connection=null;
		String tag="/lpaas";
		String temp=null;
		
		/*
		 * IPOTESI BASE: viene passato un url del tipo http://HOST:PORT/lpaas
		 * oppure del tipo http://HOST:PORT
		 */
		String regex1="^http://.*:[0-9]+/lpaas$";
		String regex2="^http://.*:[0-9]+$";
		if(!serverLocation.matches(regex1) && !serverLocation.matches(regex2))
			throw new IllegalArgumentException("URL incorrect!");
		
		
		if(serverLocation.matches(regex1))
			temp=serverLocation.substring(0, serverLocation.toString().indexOf(tag));
		else
			temp=serverLocation.toString();
		
		/*QUESTO CONTROLLO NON VA CON IL ./GRADLE
		try {
			URL reachable=new URL(temp);
			connection = (HttpURLConnection) reachable.openConnection();
	        connection.setRequestMethod("HEAD");
	        if(!(connection.getResponseCode()==200))
	        	throw new IllegalArgumentException("Unreachable Host!");
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Unreachable Host");
		}*/
		
		temp=temp+tag;	//ottengo la forma finale http://HOST:PORT/lpaas
		URL location=null;
		try {
			location=new URL(temp);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		//controllo che il componente(URL) non sia già registrato
		for(URL u: table.values()){
			if(u.equals(location)){
				throw new AlreadyRegisteredException();
			}
		}
		
		//inserisco il nuovo componente
		table.put(component, location);
	}

	@Override
	public void unRegister(String component) throws NotRegisteredException, IllegalArgumentException 
	{
		if(component==null || component.length()==0)
			throw new IllegalArgumentException("Parameters Error!");
		
		//rimuovo il componente
		if(table.containsKey(component)){
			table.remove(component);
		}
		else
			throw new NotRegisteredException();
	}

	@Override
	public Configurator getConfigurator(String component, String username, String password) 
						throws NotAuthorizedException, IllegalArgumentException,
								AuthorizationFailureException, NotRegisteredException 
	{
		if(component==null || username==null || password==null || component.length()==0)
			throw new IllegalArgumentException("Parameters Error!");
		
		if(table.get(component)==null)
			throw new NotRegisteredException();
		
		URL indirizzo=null;
		String token=null;
		
		try {
			indirizzo = new URL(""+table.get(component).toString()+"/auth");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		
		try{
			token=verify(indirizzo, username, password);
		} catch(IOException e){
			throw new AuthorizationFailureException();
		}
		
		return new LPaaSConfigurator(table.get(component), component, token);
	}

	@Override
	public Client getClient(String component) 
							throws NotRegisteredException, IllegalArgumentException
	{
		
		if(component==null || component.length()==0)
			throw new IllegalArgumentException();
		
		if(table.get(component)==null)
			throw new NotRegisteredException();
		
		return new LPaaSClient(component, table.get(component));
	}

	@Override
	public Sensor getSensor(String component, String username, String password) 
					throws NotRegisteredException, NotAuthorizedException, 
								IllegalArgumentException, AuthorizationFailureException
	{
		return null;
	}

	private String verify(URL indirizzo,String username,String password) 
											throws NotAuthorizedException, IOException
	{
		HttpURLConnection connection=null;
		String token=null;
		
		connection= (HttpURLConnection) indirizzo.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestProperty("Content-type", "application/json");
		connection.setRequestProperty("Accept", "application/json");
	
		String jsonMessage="{\"password\":"+password+",\"username\":"+username+"}";
		connection.getOutputStream().write(jsonMessage.getBytes());
		
		int responseCode=connection.getResponseCode();
		if(responseCode!=200){
			throw new NotAuthorizedException();
		}
		
		token= new JsonParser().parseToken(connection.getInputStream());
		
		//Chiusura risorse
		connection.disconnect();
		
		return token;
	}
}
