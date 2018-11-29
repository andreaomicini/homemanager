package wardrobeAgent.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import alice.logictuple.LogicTuple;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import wardrobeAgent.lpaasLibrary.LPaaSLibrary;
import wardrobeAgent.model.Device;

public class WardrobeSensorsAgent extends AbstractTucsonAgent {

	final static String THEORY_NAME = "wardrobeTheory";
	final static String DRESS_FUNCTOR = "dress";
	final static String PRESENT_FUNCTOR = "present";

	private Device device;
	private String name;
	private String sensorUsername;
	private String sensorPassword;

	private boolean state;

	private EnhancedSynchACC acc;
	private TucsonTupleCentreId wardrobeTc;
	private TucsonTupleCentreId usageManagerTc;

	private Map<String, Dress> rfidMapping;
	private Vector<String> rfidInnerClothing; // Mantengo la lista del contenuto
												// per evitare di fare get
	private Sensors sensors;

	public WardrobeSensorsAgent(Device device, TucsonTupleCentreId wardrobeTc, TucsonTupleCentreId usageManagerTc) throws TucsonInvalidAgentIdException {
		super(device.getName() + "_" + device.getId());

		this.device = device;
		this.name = device.getName() + "_" + device.getId();
		this.state = false;
		this.wardrobeTc = wardrobeTc;
		this.usageManagerTc = usageManagerTc;

		this.rfidMapping = new HashMap<>();
		this.rfidInnerClothing = new Vector<>();

		// DECOMMENTARE PER DEBUG SU PC
		this.sensors = new MockSensors();

		// DECOMMENTARE PER ESECUZIONE SU RASPBERRY
		// this.sensors = new GroveSensors();

		readCredential("credential.txt");
	}

	@Override
	protected void main() {

		System.out.println("--- " + getName() + " STARTED!");

		acc = getContext();

		try {
			writePresenceInformation();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			initRfidMap();
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean ok = false;
		try {
			ok = initTheory();
		} catch (IOException e) {

			System.err.println("ERRORE INIZIALIZZAZIONE TEORIA");
			e.printStackTrace();
			System.exit(3);

		}

		if (!ok) {
			System.err.println("ERRORE INIZIALIZZAZIONE TEORIA");
			System.exit(3);
		}

		SensorManager sensorManager = new SensorManager(sensors, sensorUsername, sensorPassword);
		sensorManager.start();
		String token = LPaaSLibrary.getToken(sensorUsername, sensorPassword);
		int result = -1;
		while (true) {
			try {
				StringBuilder sb = new StringBuilder();
				sb.append("\n*************************\nWardobe content:\n");
				for (String dress : rfidInnerClothing) {
					sb.append(getDressByRfid(dress) + "\n");

				}
				sb.append("*************************\n");
				System.out.println(sb.toString());

				sensors.displayOutTemp();
				String rfid = sensors.readRfid();
				rfid = rfid.toLowerCase();

				if (!rfid.equalsIgnoreCase("NO-TAG")) {
					System.out.println("Read: " + rfid);

					if (rfidInnerClothing.contains(rfid)) {

						Dress d = getDressByRfid(rfid);

						String oldFact = PRESENT_FUNCTOR + "(" + d.getRfid() + "," + "_)";
						String newFact = PRESENT_FUNCTOR + "(" + d.getRfid() + "," + "no)";

						String jsonBody = "{ \"oldFact\":" + "\"" + oldFact + "\"" + ", " + "\"newFact\":" + "\"" + newFact + "\" }";

						result = LPaaSLibrary.putFact(THEORY_NAME, PRESENT_FUNCTOR, jsonBody, token);

						if (result == 200) {
							rfidInnerClothing.remove(rfid);
							System.out.println("REMOVED " + getRfidMapping().get(rfid));
							persist(rfidInnerClothing);

							sensors.ledOn();
							sensors.doBip();
							sensors.ledOff();
							sensors.displayAction("removed", getRfidMapping().get(rfid));

							Thread.sleep(2000);

						} else {
							System.out.println("ERROR REMOVING");
							sensors.displayError("error removing");
							Thread.sleep(2000);

						}
					} else {

						Dress d = getDressByRfid(rfid);

						String oldFact = PRESENT_FUNCTOR + "(" + d.getRfid() + "," + "_)";
						String newFact = PRESENT_FUNCTOR + "(" + d.getRfid() + "," + "yes)";

						String jsonBody = "{ \"oldFact\":" + "\"" + oldFact + "\"" + ", " + "\"newFact\":" + "\"" + newFact + "\" }";

						result = LPaaSLibrary.putFact(THEORY_NAME, PRESENT_FUNCTOR, jsonBody, token);

						if (result == 200) {
							rfidInnerClothing.add(rfid);
							System.out.println("ADDED " + getRfidMapping().get(rfid));
							persist(rfidInnerClothing);

							sensors.ledOn();
							sensors.doBip();
							sensors.ledOff();
							sensors.displayAction("added", getRfidMapping().get(rfid));

							Thread.sleep(2000);

						} else {
							System.out.println("ERROR ADDING");

							sensors.displayError("error adding");
							Thread.sleep(2000);

						}
					}

					if (result == 401) {
						System.out.println("TOKEN SCADUTO!");
						token = LPaaSLibrary.getToken(sensorUsername, sensorPassword);
					}

				}

			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}

		}

	}

	private void persist(Vector<String> content) throws IOException {

		File file = new File(getName() + "_content.txt");
		if (!file.exists()) {
			file.createNewFile();
		}

		PrintWriter pw = new PrintWriter(file);

		for (String s : content) {
			pw.println(s);
		}

		pw.close();

	}

	private Dress getDressByRfid(String rfid) {

		return getRfidMapping().get(rfid);
	}

	private boolean writePresenceInformation() throws Exception {
		String nameTemplate = "presence_information";
		String tuple = nameTemplate + "(" + device.getName() + "(" + device.getId() + ")" + "," + "info_device" + "(" + "off" + "," + device.getWatt() + ")" + ","
				+ device.getType() + ")";

		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = acc.out(usageManagerTc, template, Long.MAX_VALUE);

		if (!operation.isResultSuccess())
			return (false);

		nameTemplate = "state_change";
		tuple = nameTemplate + "(" + "order" + "," + device.getName() + "(" + device.getId() + ")" + "," + "A" + "," + device.getType() + ")";

		template = LogicTuple.parse(tuple);
		operation = acc.in(usageManagerTc, template, Long.MAX_VALUE);

		if (!operation.isResultSuccess())
			return (false);

		LogicTuple result = operation.getLogicTupleResult();
		int third = 2;
		String deviceState = result.getArg(third).toString();
		state = deviceState.equals("on");
		return (true);
	}

	private void initRfidMap() throws Exception {

		BufferedReader br = new BufferedReader(new FileReader("mapping.txt"));
		String line;
		while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, ":\n\r");

			if (st.countTokens() == 6) {
				String rfid = st.nextToken();
				String name = st.nextToken();
				String stringSize = st.nextToken();
				String color = st.nextToken();
				String heaviness = st.nextToken();
				String type = st.nextToken();

				try {

					int size = Integer.parseInt(stringSize);

					if (type.equalsIgnoreCase("top") || type.equalsIgnoreCase("bot")) {
						Dress dress = new Dress(rfid, name, size, color, heaviness, type);

						getRfidMapping().put(rfid, dress);

					} else {
						throw new BadFileFormatException("Error in type field!");
					}

				} catch (NumberFormatException e) {
					e.printStackTrace();
					throw new BadFileFormatException("Error in size field!");
				}

			} else {

				throw new BadFileFormatException("Number of field != 6");

			}

		}

		br.close();

	}

	private boolean initTheory() throws IOException {

		String token = LPaaSLibrary.getToken(sensorUsername, sensorPassword);
		// init logic part of theory

		BufferedReader br = new BufferedReader(new FileReader("wardrobeInitFacts.txt"));
		String line;
		while ((line = br.readLine()) != null) {

			if (!line.isEmpty()) {
				int code = LPaaSLibrary.postFact(THEORY_NAME, line, token);

				if (code != 200) {
					return false;

				}
			}
		}

		br.close();

		Vector<String> contentWB = loadContent();
		setRfidInnerClothing(contentWB);
		String in;
		String fact;
		int code;

		for (String k : getRfidMapping().keySet()) {

			// init present functor with saved status

			if (getRfidInnerClothing().contains(k)) {
				in = "yes";

			} else {
				in = "no";
			}

			fact = PRESENT_FUNCTOR + "(" + k + "," + in + ")";

			code = LPaaSLibrary.postFact(THEORY_NAME, fact, token);

			if (code != 200) {
				return false;

			}

			// init all dress fact

			Dress d = getDressByRfid(k);
			fact = DRESS_FUNCTOR + "([" + d.getRfid() + "," + d.getName() + "," + d.getSize() + "," + d.getColor() + "," + d.getHeaviness() + "," + d.getType() + "])";
			code = LPaaSLibrary.postFact(THEORY_NAME, fact, token);

			if (code != 200) {
				return false;
			}

		}

		// init sensor data facts

		String newFact = SensorManager.DEGREES_FUNCTOR + "(" + (int) sensors.getTemperature() + ")";

		code = LPaaSLibrary.postFact(THEORY_NAME, newFact, token);
		if (code != 200) {
			return false;

		}

		newFact = SensorManager.HUMIDITY_FUNCTOR + "(" + (int) sensors.getHumidity() + ")";

		code = LPaaSLibrary.postFact(THEORY_NAME, newFact, token);
		if (code != 200) {
			return false;

		}

		int l = (int) ((sensors.getLightIntensity() / 65535) * 100);
		newFact = SensorManager.LIGHT_FUNCTOR + "(" + l + ")";

		code = LPaaSLibrary.postFact(THEORY_NAME, newFact, token);
		if (code != 200) {
			return false;

		}

		return true;
	}

	private Vector<String> loadContent() throws IOException {

		Vector<String> result = new Vector<>();
		File file = new File(getName() + "_content.txt");
		if (!file.exists()) {
			file.createNewFile();
		}

		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (!line.isEmpty()) {
				result.add(line);
			}
		}

		br.close();

		return result;
	}

	private void readCredential(String nomeFile) {

		try {

			String keyword;

			BufferedReader br = new BufferedReader(new FileReader(nomeFile));
			String line;
			while ((line = br.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(line, ":");

				if (st.countTokens() == 2) {
					keyword = st.nextToken();
					if (keyword.equalsIgnoreCase("username")) {
						this.sensorUsername = st.nextToken().trim();
					}
					if (keyword.equalsIgnoreCase("password")) {
						this.sensorPassword = st.nextToken().trim();
					}
				}

			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Device getDevice() {
		return device;
	}

	public String getName() {
		return name;
	}

	public boolean getState() {
		return state;
	}

	public Map<String, Dress> getRfidMapping() {
		return rfidMapping;
	}

	@Override
	public void operationCompleted(AbstractTupleCentreOperation arg0) {

	}

	@Override
	public void operationCompleted(ITucsonOperation arg0) {

	}

	public Vector<String> getRfidInnerClothing() {
		return rfidInnerClothing;
	}

	public void setRfidInnerClothing(Vector<String> rfidInnerClothing) {
		this.rfidInnerClothing = rfidInnerClothing;
	}

}
