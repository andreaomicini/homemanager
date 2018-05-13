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
	private final static String THEORY_NAME = "wardrobeTheory";
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

				String fact = "degrees(" + getMockTemperature() + ")";
				int result = LPaasLibrary.putFact(THEORY_NAME, "degrees", fact, token);

				if (result == 200) {

					System.out.println("PUT degrees OK!");
				} else {
					System.out.println("PUT degrees: ERROR " + result);

				}

				fact = "humidity(" + getMockHumidity() + ")";
				result = LPaasLibrary.putFact(THEORY_NAME, "humidity", fact, token);

				if (result == 200) {

					System.out.println("PUT humidity OK!");
				} else {
					System.out.println("PUT humidity: ERROR " + result);

				}

				fact = "light(" + getMockLightIntensity() + ")";
				result = LPaasLibrary.putFact(THEORY_NAME, "light", fact, token);

				if (result == 200) {

					System.out.println("PUT light OK!");
				} else {
					System.out.println("PUT light: ERROR " + result);

				}

//				// SENSORS
//
//				System.out.println("TEMPERATURE: " + sensors.getTemperature());
//				System.out.println("HUMIDITY: " + sensors.getHumidity());
//				System.out.println("LIGHT: " + sensors.getLightIntensity());
//
//				// PUT
//
//				String fact = "degrees(" + sensors.getTemperature() + ")";
//				int result = LPaasLibrary.putFact(THEORY_NAME, "degrees", fact, token);
//
//				if (result == 200) {
//
//					System.out.println("PUT degrees OK!");
//				} else {
//					System.out.println("PUT degrees: ERROR " + result);
//
//				}
//
//				fact = "humidity(" + sensors.getHumidity() + ")";
//				result = LPaasLibrary.putFact(THEORY_NAME, "humidity", fact, token);
//
//				if (result == 200) {
//
//					System.out.println("PUT humidity OK!");
//				} else {
//					System.out.println("PUT humidity: ERROR " + result);
//
//				}
//
//				fact = "light(" + sensors.getLightIntensity() + ")";
//				result = LPaasLibrary.putFact(THEORY_NAME, "light", fact, token);
//
//				if (result == 200) {
//
//					System.out.println("PUT light OK!");
//				} else {
//					System.out.println("PUT light: ERROR " + result);
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
