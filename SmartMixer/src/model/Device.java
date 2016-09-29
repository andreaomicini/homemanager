package model;

public class Device {
	private String name;
	private int id;
	private String type;
	private int watt;
	
	public Device() { }
	
	public Device(String fullName, String type, int watt) {
		String[] array = fullName.split("_");
		name = array[0];
		id = Integer.parseInt(array[1]);
		this.type = type;
		this.watt = watt;
	}
	
	public String getName() {
		return (name);
	}
	
	public int getId() {
		return (id);
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return (type);
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public int getWatt() {
		return (watt);
	}
	
	public void setWatt(int watt) {
		this.watt = watt;
	}
}