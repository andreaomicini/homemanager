package lpaas;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import classes.Solution;
import exceptions.ExecutionFailureException;
import exceptions.NotCreatedException;
import interfaces.Client;
import parser.JsonParser;
import parser.Parser;
import parser.TextParser;

public class LPaaSClient implements Client {

	private String component;
	private URL url;
	
	LPaaSClient(String component, URL url) {
		this.component=component;
		this.url=url;
	}
	
	@Override
	public String[] getTheories() throws NotCreatedException, ExecutionFailureException {
		String[] result=this.getList();
		
		for(int i=0;i<result.length;i++){
			StringTokenizer tokenizer=new StringTokenizer(result[i],"/");
			tokenizer.nextToken();
			result[i]=tokenizer.nextToken();
		}
		
		return result;
	}
	
	@Override
	public long countVersion(String theoryName) throws IllegalArgumentException, NotCreatedException, ExecutionFailureException
	{
		if(theoryName==null)
			throw new IllegalArgumentException("Parameters error!");
		
		String[] result=this.getList();
		
		for(String s : result){
			StringTokenizer tokenizer1=new StringTokenizer(s,"/");
			tokenizer1.nextToken();
			String temp=tokenizer1.nextToken();
			
			if(theoryName.equals(temp)){
				tokenizer1.nextToken();
				return Long.parseLong(tokenizer1.nextToken());
			}
		}
		
		//se arrivo qui è perchè è stata passata una teoria che non esiste
		throw new NotCreatedException();
	}

	
	@Override
	public String[] getFacts(String theoryName, long version) throws IllegalArgumentException, ExecutionFailureException, NotCreatedException
	{
		if(theoryName==null || theoryName.length()==0)
			throw new IllegalArgumentException("Parameters Error");
		
		URL indirizzo=null;
		HttpURLConnection connection=null;
		String[] result=null;
		
		String tmp=(version<0) ? url.toString()+"/theories/"+theoryName :
			url.toString()+"/theories/"+theoryName+"/history/"+version ;
		
		try{
			indirizzo=new URL(tmp);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
		
		try{
			connection=(HttpURLConnection)indirizzo.openConnection();
			connection.setRequestMethod("GET");
			String accept=(version<0) ? "application/json" : "text/plain" ;
			connection.setRequestProperty("Accept", accept);
		
			int responseCode=connection.getResponseCode();
			if(responseCode!=200){
				if(responseCode/100==4)
					throw new NotCreatedException();
				else
					throw new ExecutionFailureException();
			}
			
			if(version<0){
				//parsing di un json
				Parser parser=new JsonParser();
				result=parser.parseFact(connection.getInputStream());
				
			}else{
				//Parsing di un text
				Parser parser=new TextParser();
				result=parser.parseFact(connection.getInputStream());
			}
			
		}catch(IOException e){
			throw new ExecutionFailureException();
		}
		
		connection.disconnect();
		
		return result;
	}

	@Override
	public String[] getFactsWithFunctor(String theoryName, long version, String functor, int arity, long limit) 
						throws IllegalArgumentException, ExecutionFailureException, NotCreatedException
	{
		if(theoryName==null || functor==null || theoryName.length()==0 || functor.length()==0)
			throw new IllegalArgumentException("Parameters Error");
		
		URL indirizzo=null;
		HttpURLConnection connection=null;
		String[] result=null;
		
		String tmp=(version<0) ? url.toString()+"/theories/"+theoryName+"/facts/"+functor :
			url.toString()+"/theories/"+theoryName+"/facts/"+functor+"/history/"+version;
		
		if(arity >= 0){
			tmp=tmp+"?arity="+arity;
		}
		if(limit>=0){
			if(!tmp.contains("?"))
				tmp=tmp+"&";
			tmp=tmp+"limit="+limit;
		}
		
		try{
			indirizzo=new URL(tmp);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
		
		try{
			connection=(HttpURLConnection)indirizzo.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "text/plain");
			
			int responseCode=connection.getResponseCode();
			if(responseCode!=200){
				if(responseCode/100==4)
					throw new NotCreatedException();
				else
					throw new ExecutionFailureException();
			}
			
			Parser parser=new TextParser();
			result=parser.parseFactWithFunctor(connection.getInputStream());
			
		}catch(IOException e){
			throw new ExecutionFailureException();
		}
		connection.disconnect();
		
		return result;
		
	}
	
	@Override
	public String[] getFactsWithFunctor(String theoryName, long version, String functor) throws IllegalArgumentException, ExecutionFailureException, NotCreatedException {
		return this.getFactsWithFunctor(theoryName, version, functor,-1,-1);
	}

	@Override
	public String[] getGoalList() throws ExecutionFailureException{
		URL indirizzo=null;
		HttpURLConnection connection=null;
		String[] result=null;
		
		try{
			indirizzo=new URL(url.toString()+"/goals");
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
		
		try{
			connection=(HttpURLConnection)indirizzo.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "text/plain");
		
			int responseCode=connection.getResponseCode();
			if(responseCode!=200){
					throw new ExecutionFailureException();
			}
			
			Parser parser=new TextParser();
			result=parser.parseGoalList(connection.getInputStream());
			
		}catch(IOException e){
			throw new ExecutionFailureException();
		}
		connection.disconnect();
		
		return result;
	}
	
	@Override
	public String[] getGoals(String goalName) throws NotCreatedException, ExecutionFailureException , IllegalArgumentException{
		if(goalName==null || goalName.length()==0)
			throw new IllegalArgumentException("Parameters Error");
		
		URL indirizzo=null;
		HttpURLConnection connection=null;
		String[] result=null;
		
		try{
			indirizzo=new URL(url.toString()+"/goals/"+goalName);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
		
		try{
			connection=(HttpURLConnection)indirizzo.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "text/plain");
			
			int responseCode=connection.getResponseCode();
			if(responseCode!=200){
				if(responseCode/100==4)
					throw new NotCreatedException();
				else
					throw new ExecutionFailureException();
			}
			
			Parser parser=new TextParser();
			result=parser.parseGoal(connection.getInputStream());
			
		}catch(IOException e){
			throw new ExecutionFailureException();
		}
		connection.disconnect();
		
		return result;
	}


	@Override
	public Solution getSolutions(String theoryPath, long version, String goalsPath, long skip, long limit,
			String within, String every) throws IllegalArgumentException, ExecutionFailureException, NotCreatedException 
	{
	
		if(theoryPath==null || theoryPath.length()==0 || goalsPath==null
				|| goalsPath.length()==0)
			throw new IllegalArgumentException("Parameters Error");
		
		URL indirizzo=null;
		HttpURLConnection connection=null;
		Solution result=null;
		
		String _url=url.toString()+"/solutions?";
		
		if(skip>0){
			if(_url.charAt(_url.length()-1)=='?')
				_url+="skip="+skip;
			else
				_url+="&skip="+skip;
		}
		
		if(limit>0){
			if(_url.charAt(_url.length()-1)=='?')
				_url+="limit="+limit;
			else
				_url+="&limit="+limit;
		}
		
		if(within!=null && within.length()>0){
			if(_url.charAt(_url.length()-1)=='?')
				_url+="within="+within;
			else
				_url+="&within="+within;
		}
		
		if(every!=null && every.length()>0){
			if(_url.charAt(_url.length()-1)=='?')
				_url+="every="+every;
			else
				_url+="&every="+every;
		}
		
		if(_url.charAt(_url.length()-1)=='?')
			_url=_url.substring(0,_url.length()-1);
		
		try{
			indirizzo=new URL(_url);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
		
		try{
			connection=(HttpURLConnection)indirizzo.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoInput(true);
			connection.setDoOutput(true);
		}catch(IOException e){
			throw new ExecutionFailureException();
		}
		
		String versione=(version<0) ? "" : "/history/"+version;
		String input_json="{";
		input_json+="\"goals\":\""+url.toString()+"/goals/"+goalsPath+"\",";
		input_json+="\"theory\":\""+url.toString()+"/theories/"+theoryPath+versione+"\"";
		input_json+="}";
		
		System.out.println("JSON-> "+input_json);
		
		try{
			connection.getOutputStream().write(input_json.getBytes());
			
			int responseCode=connection.getResponseCode();
			if(responseCode!=200){
				if(responseCode/100==4)
					throw new NotCreatedException();
				else
					throw new ExecutionFailureException();
			}
			
			Parser parser=new JsonParser();
			result=parser.parseSolution(connection.getInputStream());
			
		}catch(IOException e){
			throw new ExecutionFailureException();
		}
		connection.disconnect();
		
		return result;
	}

	public String toString(){
		return component;
	}

	private String[] getList() throws NotCreatedException, ExecutionFailureException{
		URL indirizzo=null;
		HttpURLConnection connection=null;
		String[] result=null;
		
		try{
			indirizzo=new URL(url.toString()+"/theories");
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
		
		try{
			connection=(HttpURLConnection)indirizzo.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "text/plain");
		
			int responseCode=connection.getResponseCode();
			if(responseCode!=200){
				if(responseCode/100==4)
					throw new NotCreatedException();
				else
					throw new ExecutionFailureException();
			}
			
			Parser parser=new TextParser();
			result=parser.parseTheoryList(connection.getInputStream());
			
		}catch(IOException e){
			throw new ExecutionFailureException();
		}
		connection.disconnect();
		
		return result;
	}
	
}
