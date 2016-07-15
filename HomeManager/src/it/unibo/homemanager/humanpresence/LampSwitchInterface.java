package it.unibo.homemanager.humanpresence;

public interface LampSwitchInterface {	
	public void lightOn(int number);
	public void lightOff(int number);
	public void lightSwitch(int number);
	public int getPin();
}
