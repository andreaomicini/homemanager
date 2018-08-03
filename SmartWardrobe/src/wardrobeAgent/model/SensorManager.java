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

	private boolean firstTemperature;
	private boolean firstHumidity;
	private boolean firstLight;

	public SensorManager(Sensors sensors, String username, String password) {
		this.sensors = sensors;
		this.sensorUsername = username;
		this.sensorPassword = password;
		firstTemperature = true;
		firstHumidity = true;
		firstLight = true;
	}

	public void run() {

		String token = LPaasLibrary.getToken(sensorUsername, sensorPassword);

		while (true) {

			try {

				// System.out.println("TEMPERATURE: " + getMockTemperature());
				// System.out.println("HUMIDITY: " + getMockHumidity());
				// System.out.println("LIGHT: " + (int)
				// ((getMockLightIntensity() / 65535) * 100));
				//
				// int result;
				//
				// String fact = DEGREES_FUNCTOR + "(" + getMockTemperature() +
				// ")";
				//
				// if (firstTemperature) {
				//
				// result = LPaasLibrary.postFact(THEORY_NAME, fact, token);
				//
				// if (result == 200) {
				//
				// System.out.println("POST " + DEGREES_FUNCTOR + " OK!");
				// firstTemperature = false;
				// } else {
				// System.out.println("POST " + DEGREES_FUNCTOR + ": ERROR " +
				// result);
				//
				// }
				// } else {
				//
				// result = LPaasLibrary.putFact(THEORY_NAME, DEGREES_FUNCTOR,
				// fact, token);
				//
				// if (result == 200) {
				//
				// System.out.println("PUT " + DEGREES_FUNCTOR + " OK!");
				// } else {
				// System.out.println("PUT " + DEGREES_FUNCTOR + ": ERROR " +
				// result);
				//
				// }
				//
				// }
				//
				// fact = HUMIDITY_FUNCTOR + "(" + getMockHumidity() + ")";
				//
				// if (firstHumidity) {
				//
				// result = LPaasLibrary.postFact(THEORY_NAME, fact, token);
				// if (result == 200) {
				//
				// System.out.println("POST " + HUMIDITY_FUNCTOR + " OK!");
				// firstHumidity = false;
				// } else {
				// System.out.println("POST " + HUMIDITY_FUNCTOR + ": ERROR " +
				// result);
				//
				// }
				//
				// } else {
				// result = LPaasLibrary.putFact(THEORY_NAME, HUMIDITY_FUNCTOR,
				// fact, token);
				//
				// if (result == 200) {
				//
				// System.out.println("PUT " + HUMIDITY_FUNCTOR + " OK!");
				// } else {
				// System.out.println("PUT " + HUMIDITY_FUNCTOR + ": ERROR " +
				// result);
				//
				// }
				//
				// }
				//
				// int l = (int) ((getMockLightIntensity() / 65535) * 100);
				// fact = LIGHT_FUNCTOR + "(" + l + ")";
				//
				// if (firstLight) {
				//
				// result = LPaasLibrary.postFact(THEORY_NAME, fact, token);
				// if (result == 200) {
				//
				// System.out.println("POST " + LIGHT_FUNCTOR + " OK!");
				// firstLight = false;
				// } else {
				// System.out.println("POST " + LIGHT_FUNCTOR + ": ERROR " +
				// result);
				//
				// }
				//
				// }
				//
				// else
				//
				// {
				//
				// result = LPaasLibrary.putFact(THEORY_NAME, LIGHT_FUNCTOR,
				// fact, token);
				//
				// if (result == 200) {
				//
				// System.out.println("PUT " + LIGHT_FUNCTOR + " OK!");
				// } else {
				// System.out.println("PUT " + LIGHT_FUNCTOR + ": ERROR " +
				// result);
				//
				// }
				// }

				// SENSORS

				System.out.println("TEMPERATURE: " + (int) sensors.getTemperature());
				System.out.println("HUMIDITY: " + (int) sensors.getHumidity());
				System.out.println("LIGHT: " + (int) ((sensors.getLightIntensity() / 65535) * 100));

				int result;

				String fact = DEGREES_FUNCTOR + "(" + (int) sensors.getTemperature() + ")";

				if (firstTemperature) {
					// POST
					result = LPaasLibrary.postFact(THEORY_NAME, fact, token);

					if (result == 200) {

						System.out.println("POST " + DEGREES_FUNCTOR + " OK!");
						firstTemperature = false;
					} else {
						System.out.println("POST " + DEGREES_FUNCTOR + ": ERROR " + result);

					}
				} else {
					// PUT

					result = LPaasLibrary.putFact(THEORY_NAME, DEGREES_FUNCTOR, fact, token);

					if (result == 200) {

						System.out.println("PUT " + DEGREES_FUNCTOR + " OK!");
					} else {
						System.out.println("PUT " + DEGREES_FUNCTOR + ": ERROR " + result);

					}

				}

				fact = HUMIDITY_FUNCTOR + "(" + (int) sensors.getHumidity() + ")";

				if (firstHumidity) {
					// POST
					result = LPaasLibrary.postFact(THEORY_NAME, fact, token);
					if (result == 200) {

						System.out.println("POST " + HUMIDITY_FUNCTOR + " OK!");
						firstHumidity = false;
					} else {
						System.out.println("POST " + HUMIDITY_FUNCTOR + ": ERROR " + result);

					}

				} else {
					// PUT
					result = LPaasLibrary.putFact(THEORY_NAME, HUMIDITY_FUNCTOR, fact, token);

					if (result == 200) {

						System.out.println("PUT " + HUMIDITY_FUNCTOR + " OK!");
					} else {
						System.out.println("PUT " + HUMIDITY_FUNCTOR + ": ERROR " + result);

					}

				}

				int l = (int) ((sensors.getLightIntensity() / 65535) * 100);
				fact = LIGHT_FUNCTOR + "(" + l + ")";
				if (firstLight) {
					// POST

					result = LPaasLibrary.postFact(THEORY_NAME, fact, token);
					if (result == 200) {

						System.out.println("POST " + LIGHT_FUNCTOR + " OK!");
						firstLight = false;
					} else {
						System.out.println("POST " + LIGHT_FUNCTOR + ": ERROR " + result);

					}

				}

				else {
					// PUT
					result = LPaasLibrary.putFact(THEORY_NAME, LIGHT_FUNCTOR, fact, token);

					if (result == 200) {

						System.out.println("PUT " + LIGHT_FUNCTOR + " OK!");
					} else {
						System.out.println("PUT " + LIGHT_FUNCTOR + ": ERROR " + result);

					}
				}

				if (result == 401) {
					System.out.println("TOKEN SCADUTO!");
					token = LPaasLibrary.getToken(sensorUsername, sensorPassword);
				}

				Thread.sleep(3000);
			} catch (InterruptedException | IOException e) {

				e.printStackTrace();
			}
		}

	}

	private static double getMockLightIntensity() {
		return 65000;
	}

	private static int getMockTemperature() {
		return 25;

	}

	private static int getMockHumidity() {
		return 90;

	}
}
