package wardrobeAgent.model;

import java.io.IOException;

public interface Sensors {

	void displayOutTemp() throws IOException;

	void displayAction(String action, Dress dress) throws IOException;

	void displayError(String error) throws IOException;

	double getTemperature() throws IOException;

	double getHumidity() throws IOException;

	double getLightIntensity() throws IOException;

	void doBip() throws IOException, InterruptedException;

	void ledOn() throws IOException;

	void ledOff() throws IOException;

	String readRfid() throws IOException;

}
