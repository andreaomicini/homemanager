package fridgeAgent.model;

public class Device {
    private int id;
    private String name;
    private String type;
    private float watt;

    public Device() { }

    public Device(String fullName, String type, int watt) {
        String[] array = fullName.split("_");
        name = array[0];
        id = Integer.parseInt(array[1]);
        this.type = type;
        this.watt = watt;
    }

    public int getId() {
        return (id);
    }

    public String getName() {
        return (name);
    }

    public String getType() {
        return (type);
    }

    public float getWatt() {
        return (watt);
    }
}