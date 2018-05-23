package wardrobeAgent.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;

import wardrobeAgent.lpaasLibrary.LPaasLibrary;

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

	public void run() {

		String token = LPaasLibrary.getToken(sensorUsername, sensorPassword);

		while (true) {

			try {

				System.out.println("TEMPERATURE: " + getMockTemperature());
				System.out.println("HUMIDITY: " + getMockHumidity());
				System.out.println("LIGHT: " + getMockLightIntensity());

				String fact = DEGREES_FUNCTOR + "(" + getMockTemperature() + ")";
				int result = LPaasLibrary.putFact(THEORY_NAME, DEGREES_FUNCTOR, fact, token);

				if (result == 200) {

					System.out.println("PUT " + DEGREES_FUNCTOR + " OK!");
				} else {
					System.out.println("PUT " + DEGREES_FUNCTOR + ": ERROR " + result);

				}
				
				int h = (getMockHumidity() / 65535) * 100;
				fact = HUMIDITY_FUNCTOR + "(" + h + ")";
				result = LPaasLibrary.putFact(THEORY_NAME, HUMIDITY_FUNCTOR, fact, token);

				if (result == 200) {

					System.out.println("PUT " + HUMIDITY_FUNCTOR + " OK!");
				} else {
					System.out.println("PUT " + HUMIDITY_FUNCTOR + ": ERROR " + result);

				}
				
				int l = (getMockLightIntensity() / 65535) * 100;
				fact = LIGHT_FUNCTOR + "(" + l + ")";
				result = LPaasLibrary.putFact(THEORY_NAME, LIGHT_FUNCTOR, fact, token);

				if (result == 200) {

					System.out.println("PUT " + LIGHT_FUNCTOR + " OK!");
				} else {
					System.out.println("PUT " + LIGHT_FUNCTOR + ": ERROR " + result);

				}

//				// SENSORS
//
//				System.out.println("TEMPERATURE: " + sensors.getTemperature());
//				System.out.println("HUMIDITY: " + sensors.getHumidity());
//				System.out.println("LIGHT: " + sensors.getLightIntensity());
//
//				// PUT
//				String fact = DEGREES_FUNCTOR + "(" + sensors.getTemperature() + ")";
//				int result = LPaasLibrary.putFact(THEORY_NAME, DEGREES_FUNCTOR, fact, token);
//
//				if (result == 200) {
//
//					System.out.println("PUT " + DEGREES_FUNCTOR + " OK!");
//				} else {
//					System.out.println("PUT " + DEGREES_FUNCTOR + ": ERROR " + result);
//
//				}
//				int h = (int) ((sensors.getHumidity() / 65535) * 100);
//				fact = HUMIDITY_FUNCTOR + "(" + h + ")";
//				result = LPaasLibrary.putFact(THEORY_NAME, HUMIDITY_FUNCTOR, fact, token);
//
//				if (result == 200) {
//
//					System.out.println("PUT " + HUMIDITY_FUNCTOR + " OK!");
//				} else {
//					System.out.println("PUT " + HUMIDITY_FUNCTOR + ": ERROR " + result);
//
//				}
//				int l = (int) ((sensors.getLightIntensity() / 65535) * 100);
//				fact = LIGHT_FUNCTOR + "(" + l + ")";
//				result = LPaasLibrary.putFact(THEORY_NAME, "light", fact, token);
//
//				if (result == 200) {
//
//					System.out.println("PUT " + LIGHT_FUNCTOR + " OK!");
//				} else {
//					System.out.println("PUT " + LIGHT_FUNCTOR + ": ERROR " + result);
//
//				}

				if (result == 401) {
					System.out.println("TOKEN SCADUTO!");
					token = LPaasLibrary.getToken(sensorUsername, sensorPassword);
				}

				Thread.sleep(1000);
			} catch (InterruptedException /* | IOException */ e) {

				e.printStackTrace();
			}
		}

	}

	private static int getMockLightIntensity() {
		return 16000;
	}

	private static int getMockTemperature() {
		return 25;

	}

	private static int getMockHumidity() {
		return 80;

	}
}
