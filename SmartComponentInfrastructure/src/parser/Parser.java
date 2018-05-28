package parser;

import java.io.IOException;
import java.io.InputStream;

import classes.Solution;

public interface Parser {

	public String[] parseTheoryList(InputStream input);
	
	public String[] parseGoalList(InputStream input) throws IOException;
	
	public String[] parseGoal(InputStream input) throws IOException;
	
	public String[] parseFact(InputStream input);
	
	public String[] parseFactWithFunctor(InputStream input);
	
	public Solution parseSolution(InputStream input) throws IOException;
	
	public String parseToken(InputStream input) throws IOException;
}
