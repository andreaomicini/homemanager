package it.unibo.homemanager.predictionWEKA;

public class HomePresencePrediction {
	
	private int hour;
	private int day;
	private User_location in_home;
	
	public HomePresencePrediction(){}


	public HomePresencePrediction(int day, int hour, User_location in_home)
	  {
		setHour(hour);
		setDay(day);
		setIn_home(in_home);
	  }
	
	
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		if((hour>=0) && (hour<=23))
			this.hour = hour;
	}

	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		if((day>=1) && (day<=7))
			 this.day = day;
	}


	public User_location getIn_home() {
		return in_home;
	}


	public void setIn_home(User_location in_home) {
		this.in_home = in_home;
	}


	@Override
	public String toString() {
		return day + ","+ hour+ ","+ in_home.name();
	}
	
	
	
	

}
