package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

import classes.Solution;

public class TextParser implements Parser {

	
	public TextParser(){}
	
	@Override
	public String[] parseTheoryList(InputStream input) {
		String line;
		Vector<String> vector=new Vector<String>();
		BufferedReader reader=new BufferedReader(new InputStreamReader(input));
		
		try {
			while((line=reader.readLine())!=null){
				if(line.startsWith("/theories/"))
					vector.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return vector.toArray(new String[vector.size()]);
	}
	
	public String[] parseGoalList(InputStream input) throws IOException {
		String line;
		Vector<String> vector=new Vector<String>();
		BufferedReader reader=new BufferedReader(new InputStreamReader(input));
		
		while((line=reader.readLine())!=null){
			if(line.startsWith("/goals/")){
				StringTokenizer tokenizer=new StringTokenizer(line,"/");
				tokenizer.nextToken();
				vector.add(tokenizer.nextToken());
			}
		}
		
		return vector.toArray(new String[vector.size()]);
	}

	@Override
	public String[] parseGoal(InputStream input) throws IOException {
		String line;
		Vector<String> vector=new Vector<String>();
		BufferedReader reader=new BufferedReader(new InputStreamReader(input));
		
		line=reader.readLine();
		String goal="";
		for(String stripe : line.split(", ")){
			if(!stripe.startsWith("'")){
				if(!goal.isEmpty())
					vector.add(goal);
				goal=stripe;
			}
			else{
				goal+=stripe;
			}
			
		}
		vector.add(goal);
		
		return vector.toArray(new String[vector.size()]);
	}

	@Override
	public String[] parseFact(InputStream input) {
		String line;
		Vector<String> vector=new Vector<String>();
		BufferedReader reader=new BufferedReader(new InputStreamReader(input));
		
		try {
			while((line=reader.readLine())!=null){
				vector.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return vector.toArray(new String[vector.size()]);
	}

	@Override
	public String[] parseFactWithFunctor(InputStream input) {
		String line;
		Vector<String> vector=new Vector<String>();
		BufferedReader reader=new BufferedReader(new InputStreamReader(input));
		try {
			while((line=reader.readLine())!=null){
				if(line.length()!=0)
					vector.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return vector.toArray(new String[vector.size()]);
	}

	@Override
	public Solution parseSolution(InputStream input) {
		
		return null;
	}

	@Override
	public String parseToken(InputStream input) {
		
		return null;
	}

}
