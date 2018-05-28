package classes;

import java.time.LocalDateTime;

public class Solution {
	
	private String hook;
	private long version;
	private String theory;
	private String goalList;
	private LocalDateTime timestamp;
	private String[] solutions;
	
	public Solution(String hook, long version, String theory, String goalList, LocalDateTime timestamp, String[] solution) {
		super();
		this.hook = hook;
		this.version = version;
		this.theory = theory;
		this.goalList = goalList;
		this.timestamp = timestamp;
		this.solutions = solution;
	}

	public String getHook() {
		return hook;
	}

	public long getVersion() {
		return version;
	}

	public String getTheory() {
		return theory;
	}

	public String getGoalList() {
		return goalList;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String[] getSolution() {
		return solutions;
	}
	
	public String toString(){
		String message="";
		message=message+"hook: "+this.hook+"\n";
		message=message+"version: "+this.version+"\n";
		message=message+"theory: "+this.theory+"\n";
		message=message+"goalList: "+this.goalList+"\n";
		message=message+"timestamp: "+this.timestamp+"\n";
		message=message+"SOLUTIONS:";
		for(String s : this.solutions)
			message=message+s+"\n";		
		
		return message;
	}

}
