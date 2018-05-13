package wardrobeAgent.model;

public class Dress {
	private String rfid;
	private String name;
	private int size;
	private String type;
	private String color;

	public Dress() {

		this("NO-TAG", "NONE", -1, "NONE", null);
	}

	public Dress(String rfid, String name, int size, String type, String color) {
		this.rfid = rfid;
		this.name = name;
		this.size = size;
		this.type = type;
		this.color = color;
	}

	public String getRfid() {
		return rfid;
	}

	public String getName() {
		return name;
	}

	public int getSize() {
		return size;
	}

	public String getType() {
		return type;
	}

	public String getColor() {
		return color;
	}

	@Override
	public String toString() {
		return getName() + " - Color: " + getColor() + " - Size: " + getSize();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Dress) {
			Dress d = (Dress) obj;
			return getRfid().equalsIgnoreCase(d.getRfid());
			// return
			// (getRfid().equalsIgnoreCase(d.getRfid())&&getName().equalsIgnoreCase(d.getName())
			// && getSize() == d.getSize() &&
			// getType().equalsIgnoreCase(d.getType()) &&
			// getColor().equalsIgnoreCase(d.getColor()));
		}
		return (false);
	}

}