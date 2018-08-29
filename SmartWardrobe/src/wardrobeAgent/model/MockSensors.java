package wardrobeAgent.model;

import java.io.IOException;
import java.util.Random;

public class MockSensors implements Sensors {

	@Override
	public void displayOutTemp() throws IOException {
		System.out.println("MOCK_SENSOR: TEMPERATURA ESTERNA " + this.getTemperature() + "C");
	}

	@Override
	public void displayAction(String action, Dress dress) throws IOException {
		System.out.println("MOCK_SENSOR: " + action.toUpperCase() + " " + dress.getName().toUpperCase());

	}

	@Override
	public void displayError(String error) throws IOException {
		System.out.println("MOCK_SENSOR: " + error.toUpperCase());

	}

	@Override
	public double getTemperature() throws IOException {
		return 25;

	}

	@Override
	public double getHumidity() throws IOException {
		return 90;

	}

	@Override
	public double getLightIntensity() throws IOException {
		return 65000;

	}

	@Override
	public void doBip() throws IOException, InterruptedException {
		System.out.println("MOCK_SENSOR: BIP");

	}

	@Override
	public void ledOn() throws IOException {
		System.out.println("MOCK_SENSOR: LED ON");

	}

	@Override
	public void ledOff() throws IOException {
		System.out.println("MOCK_SENSOR: LED OFF");

	}

	@Override
	public String readRfid() throws IOException {
		return randomRfid();
	}

	private String randomRfid() {
		String[] rfidArray = new String[] { "eab5e4a4", "aa8be4a4", "baf7e3a4", "caf0e1a4", "NO-TAG" };
		Random random = new Random();
		int index = random.nextInt(rfidArray.length);
		return (rfidArray[index]);
	}
}
