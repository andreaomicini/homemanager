package wardrobeAgent.model;

import java.io.IOException;

import wardrobeAgent.lpaasLibrary.LPaaSLibrary;

public class SensorManager extends Thread {
	final static String THEORY_NAME = "wardrobeTheory";
	final static String DEGREES_FUNCTOR = "degrees";
	final static String HUMIDITY_FUNCTOR = "humidity";
	final static String LIGHT_FUNCTOR = "light";

	private Sensors sensors;
	private String sensorUsername;
	private String sensorPassword;

	public SensorManager(Sensors sensors, String username, String password) {
		this.sensors = sensors;
		this.sensorUsername = username;
		this.sensorPassword = password;

	}

	@Override
	public void run() {

		String token = LPaaSLibrary.getToken(sensorUsername, sensorPassword);

		while (true) {

			try {

				System.out.println("TEMPERATURE: " + (int) sensors.getTemperature() + " C");
				System.out.println("HUMIDITY: " + (int) sensors.getHumidity() + " %");
				System.out.println("LIGHT: " + (int) ((sensors.getLightIntensity() / 65535) * 100) + " %");

				int result;

				String oldFact = DEGREES_FUNCTOR + "(_)";
				String newFact = DEGREES_FUNCTOR + "(" + (int) sensors.getTemperature() + ")";

				String jsonBody = "{ \"oldFact\":" + "\"" + oldFact + "\"" + ", " + "\"newFact\":" + "\"" + newFact + "\" }";

				// PUT

				result = LPaaSLibrary.putFact(THEORY_NAME, DEGREES_FUNCTOR, jsonBody, token);

				if (result == 200) {

					System.out.println("PUT " + DEGREES_FUNCTOR + " OK!");
				} else {
					System.out.println("PUT " + DEGREES_FUNCTOR + ": ERROR " + result);

				}

				oldFact = HUMIDITY_FUNCTOR + "(_)";
				newFact = HUMIDITY_FUNCTOR + "(" + (int) sensors.getHumidity() + ")";

				jsonBody = "{ \"oldFact\":" + "\"" + oldFact + "\"" + ", " + "\"newFact\":" + "\"" + newFact + "\" }";

				// PUT
				result = LPaaSLibrary.putFact(THEORY_NAME, HUMIDITY_FUNCTOR, jsonBody, token);

				if (result == 200) {

					System.out.println("PUT " + HUMIDITY_FUNCTOR + " OK!");
				} else {
					System.out.println("PUT " + HUMIDITY_FUNCTOR + ": ERROR " + result);

				}

				int l = (int) ((sensors.getLightIntensity() / 65535) * 100);

				oldFact = LIGHT_FUNCTOR + "(_)";
				newFact = LIGHT_FUNCTOR + "(" + l + ")";

				jsonBody = "{ \"oldFact\":" + "\"" + oldFact + "\"" + ", " + "\"newFact\":" + "\"" + newFact + "\" }";

				// PUT
				result = LPaaSLibrary.putFact(THEORY_NAME, LIGHT_FUNCTOR, jsonBody, token);

				if (result == 200) {

					System.out.println("PUT " + LIGHT_FUNCTOR + " OK!");
				} else {
					System.out.println("PUT " + LIGHT_FUNCTOR + ": ERROR " + result);

				}

				if (result == 401) {
					System.out.println("TOKEN SCADUTO!");
					token = LPaaSLibrary.getToken(sensorUsername, sensorPassword);
				}

				Thread.sleep(5000);
			} catch (InterruptedException | IOException e) {

				e.printStackTrace();
			}
		}

	}

}
