package fridgeAgent.configuration;

public class Configuration {
    private static Configuration configuration;

    private String ip;
    private int port;

    private String deviceType;
    private int deviceWatt;

    private Configuration() {
        ip = "localhost";
        port = 20504;

        deviceType = "fridge";
        deviceWatt = 400;
    }

    public static Configuration getInstance() {
        if (configuration == null) {
            configuration = new Configuration();
        }
        return (configuration);
    }

    public String getIp() {
        return (ip);
    }

    public int getPort() {
        return (port);
    }

    public String getDeviceType() {
        return (deviceType);
    }

    public int getDeviceWatt() {
        return (deviceWatt);
    }
}
