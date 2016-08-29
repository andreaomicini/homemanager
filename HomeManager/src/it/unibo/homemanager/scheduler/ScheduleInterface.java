package it.unibo.homemanager.scheduler;

import it.unibo.homemanager.tablemap.User;

public interface ScheduleInterface {
	
	public void setUser(User u);
	
	public void launch() throws Exception;
	
}
