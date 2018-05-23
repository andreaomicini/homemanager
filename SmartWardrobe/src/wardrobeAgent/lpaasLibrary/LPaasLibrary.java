package wardrobeAgent.lpaasLibrary;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.StringTokenizer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LPaasLibrary {

	private final static String BASENAME = "http://192.168.1.25:8080/lpaas/";

	// public static String getToken(String username, String password) {
	// String token = null;
	// try {
	// URL url = new URL(BASENAME + "auth");
	// HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	//
	// connection.setRequestProperty("Accept", "application/json");
	// connection.setRequestProperty("Content-Type", "application/json");
	// connection.setRequestMethod("POST");
	// connection.setDoInput(true); // specifico di voler ricevere il body
	// connection.setDoOutput(true); // specifico l'invio del body
	// String rawJson = "{\"username\":" + "\"" + username + "\", \"password\":"
	// + "\"" + password + "\"" + "}";
	// connection.getOutputStream().write(rawJson.getBytes());
	// connection.getOutputStream().close();
	//
	// connection.connect();
	//
	// if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
	// InputStreamReader in = new
	// InputStreamReader(connection.getInputStream());
	// StringBuilder sb = new StringBuilder();
	// int c;
	// while ((c = in.read()) > 0) {
	// sb.append((char) c);
	//
	// }
	// String body = sb.toString();
	// // System.out.println(body);
	// StringTokenizer st = new StringTokenizer(body, ":");
	// st.nextToken();
	//
	// token = st.nextToken();
	// token = token.substring(1, token.length() - 2);
	// }
	// connection.disconnect();
	//
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return token;
	// }

	public static String getToken(String username, String password) {
		String token = null;

		try {
			URL url = new URL(BASENAME + "auth");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod("POST");
			connection.setDoInput(true); // specifico di voler ricevere il body
			connection.setDoOutput(true); // specifico l'invio del body
			String rawJson = "{\"username\":" + "\"" + username + "\", \"password\":" + "\"" + password + "\"" + "}";
			connection.getOutputStream().write(rawJson.getBytes());
			connection.getOutputStream().close();

			connection.connect();

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStreamReader in = new InputStreamReader(connection.getInputStream());

				JSONParser parser = new JSONParser();

				Object jsonObj = parser.parse(in);

				JSONObject jsonObject = (JSONObject) jsonObj;

				token = (String) jsonObject.get("token");

			}

			connection.disconnect();

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return token;
	}

	public static int putFact(String theoryName, String functor, String fact, String token) {
		int result = 0;

		try {
			URL url = new URL(BASENAME + "theories/" + theoryName + "/facts/" + functor);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("Authorization", "jwt " + token);
			connection.setRequestProperty("Content-Type", "text/plain");
			connection.setRequestMethod("PUT");
			connection.setDoOutput(true); // specifico l'invio del body
			connection.getOutputStream().write(fact.getBytes());
			connection.getOutputStream().close();

			connection.connect();

			result = connection.getResponseCode();

			connection.disconnect();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static int postFact(String theoryName, String fact, String token) {
		int result = 0;

		try {
			URL url = new URL(BASENAME + "theories/" + theoryName + "/facts/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestProperty("Authorization", "jwt " + token);
			connection.setRequestProperty("Content-Type", "text/plain");

			connection.setRequestMethod("POST");
			connection.setDoOutput(true); // specifico l'invio del body
			connection.getOutputStream().write(fact.getBytes());
			connection.getOutputStream().close();

			connection.connect();

			result = connection.getResponseCode();

			connection.disconnect();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

}
