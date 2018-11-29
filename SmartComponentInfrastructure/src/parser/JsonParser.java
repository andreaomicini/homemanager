package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import classes.Solution;

public class JsonParser implements Parser {

	public JsonParser(){}
	
	@Override
	public String[] parseTheoryList(InputStream input) {
		return null;
	}
	
	public String[] parseGoalList(InputStream input) {
		return null;
	}

	@Override
	public String[] parseGoal(InputStream input) {
		return null;
	}

	@Override
	public String[] parseFact(InputStream input) {
		String line;
		Vector<String> vector=new Vector<String>();
		BufferedReader reader=new BufferedReader(new InputStreamReader(input));
		String text="";
		String facts=null;
		try {
			while((line=reader.readLine())!=null){
				text+=line;
			}
			JSONParser parser=new JSONParser();
			JSONObject object=(JSONObject)parser.parse(text);
			facts=(String)object.get("theory");
			StringTokenizer tokenizer=new StringTokenizer(facts,"\n");
			while(tokenizer.hasMoreTokens())
				vector.add(tokenizer.nextToken());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ParseException e){
			e.printStackTrace();
			return null;
		}
		
		return vector.toArray(new String[vector.size()]);
	}

	@Override
	public String[] parseFactWithFunctor(InputStream input) {
		return null;
	}

	@Override
	public Solution parseSolution(InputStream input) throws IOException {
		
		String tmp=null;
		String response="";
		JSONObject object=null;
		
		BufferedReader reader=new BufferedReader(new InputStreamReader(input));
		try{
			while((tmp=reader.readLine())!=null){
				response=response+tmp+"\n";
			}

			//System.out.println(response);
			//Parse della risposta
			JSONParser parser=new JSONParser();
			object=(JSONObject)parser.parse(response);
			
			String hook=(String) object.get("hook");
			long version=(long) object.get("version");
			String theory=(String) object.get("theory");
			String goalList=(String) object.get("goalList");
			
			/*PARSING TIMESTAMP*/
			LocalDateTime timestamp=null;
			String _timestamp=(String)object.get("timestamp");
			DateTimeFormatter formatter= DateTimeFormatter.RFC_1123_DATE_TIME;
			timestamp=LocalDateTime.parse(_timestamp, formatter);
			/**/
			
			Vector<String> vector=new Vector<String>();
			JSONArray solutions=(JSONArray) object.get("solutions");
			for(int i=0;i<solutions.size();i++){
				object=(JSONObject)parser.parse(solutions.get(i).toString());
				
				boolean success=(boolean) object.get("success");
				String solution=null;
				if(success){
					solution=(String) object.get("solution");
					
					boolean ok=false;
					while(!ok){
						if(solution.startsWith(","))
							solution=solution.substring(1);
						
						if(solution.matches("^','.+")){
							solution=solution.substring(4,solution.length()-1);
						}
						else{
							if(solution.matches("^[a-zA-Z]+\\(.+\\),.+") || solution.matches("^[a-zA-Z]+\\(.+\\)$")){
								
									String temp=solution.substring(0,solution.indexOf(")")+1);
									vector.add(temp);
									solution=solution.substring(solution.indexOf(")")+1,solution.length());
								
							}
							else{
								if(solution.matches("^'[^,]'.+")){
									solution=solution.substring(solution.indexOf(")")+1,solution.length());
								}else{
									if(solution.matches("^.+,?")){
										int index=solution.indexOf(",");
										if(index>0){
											String temp=solution.substring(0,index);
											solution=solution.substring(index,solution.length());
											vector.add(temp);
										}
										else{
											vector.add(solution);
											ok=true;
										}
									}else{
										ok=true;
									}
								}
							}
						}
					}
				}
			}
			
			Solution s=new Solution(hook,version,theory,goalList,timestamp,vector.toArray(new String[vector.size()]));
			
			return s;
		} catch (Exception e){
			throw new IOException();
		}
	}

	@Override
	public String parseToken(InputStream input) throws IOException {
		String response="";
		String tmp=null;
		JSONObject object=null;
		
		BufferedReader reader=new BufferedReader(new InputStreamReader(input));
		try {
			while((tmp=reader.readLine())!=null){
				response=response+tmp+"\n";
			}

			//Parse della risposta
			JSONParser parser=new JSONParser();
			object=(JSONObject)parser.parse(response);
			
		} catch (Exception e){
			throw new IOException();
		}
		
		return (String) object.get("token");
	}

}
