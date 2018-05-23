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
import wardrobeAgent.lpaasLibrary.LPaasLibrary;
import wardrobeAgent.model.Device;

public class WardrobeAgent extends AbstractTucsonAgent {

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

	public WardrobeAgent(Device device, TucsonTupleCentreId wardrobeTc, TucsonTupleCentreId usageManagerTc) throws TucsonInvalidAgentIdException {
		super(device.getName() + "_" + device.getId());

		this.device = device;
		this.name = device.getName() + "_" + device.getId();
		this.state = false;
		this.wardrobeTc = wardrobeTc;
		this.usageManagerTc = usageManagerTc;

		this.rfidMapping = new HashMap<>();
		this.rfidInnerClothing = new Vector<>();
		// this.sensors = new Sensors();
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

		// boolean ok;
		try {
			/* ok = */initTheory();
		} catch (IOException e) {

			System.err.println("ERRORE INIZIALIZZAZIONE TEORIA");
			e.printStackTrace();
			System.exit(3);

		}

		// if (!ok) {
		// System.err.println("ERRORE INIZIALIZZAZIONE TEORIA");
		// System.exit(3);
		// }

		SensorManager sensorManager = new SensorManager(sensors, sensorUsername, sensorPassword);
		sensorManager.start();
		String token = LPaasLibrary.getToken(sensorUsername, sensorPassword);
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

				// sensors.displayOutTemp();

				// String rfid = sensors.readRfid();

				String rfid = randomRfid();

				if (!rfid.equals("NO-TAG")) {
					System.out.println("Read: " + rfid);

					if (rfidInnerClothing.contains(rfid)) {
						rfidInnerClothing.remove(rfid);

						Dress d = getDressByRfid(rfid);

						String fact = PRESENT_FUNCTOR + "(" + d.getRfid() + "," + "no)";

						result = LPaasLibrary.putFact(THEORY_NAME, PRESENT_FUNCTOR, fact, token);

						if (result == 200) {
							System.out.println("REMOVED " + getRfidMapping().get(rfid));
							persist(rfidInnerClothing);

							// sensors.ledOn();
							// sensors.doBip();
							// sensors.ledOff();
							// sensors.displayAction("removed",
							// getRfidMapping().get(rfid));
						} else {
							System.out.println("ERROR REMOVING");
							// sensors.displayAction("error removing");

						}
					} else {
						rfidInnerClothing.add(rfid);

						Dress d = getDressByRfid(rfid);

						String fact = DRESS_FUNCTOR + "([" + d.getRfid() + "," + d.getName() + "," + d.getSize() + "," + d.getColor() + "," + d.getHeaviness() + "," + d.getType()
								+ "])";
						result = LPaasLibrary.postFact(THEORY_NAME, fact, token);

						fact = PRESENT_FUNCTOR + "(" + d.getRfid() + "," + "yes)";

						result = LPaasLibrary.putFact(THEORY_NAME, PRESENT_FUNCTOR, fact, token);

						if (result == 200) {
							System.out.println("ADDED " + getRfidMapping().get(rfid));
							persist(rfidInnerClothing);
							// sensors.ledOn();
							// sensors.doBip();
							// sensors.ledOff();
							// sensors.displayAction("added",
							// getRfidMapping().get(rfid));
						} else {
							System.out.println("ERROR ADDING");
							// sensors.displayAction("error adding");
						}
					}

					if (result == 401) {
						System.out.println("TOKEN SCADUTO!");
						token = LPaasLibrary.getToken(sensorUsername, sensorPassword);
					}

				}
				Thread.sleep(5000);

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
						Dress dress = new Dress(rfid, name, size, type, heaviness, color);

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

		boolean result = true;
		String token = LPaasLibrary.getToken(sensorUsername, sensorPassword);
		// init logic part of theory

		BufferedReader br = new BufferedReader(new FileReader("wardrobeInitFacts.txt"));
		String line;
		while ((line = br.readLine()) != null) {

			int code = LPaasLibrary.postFact(THEORY_NAME, line, token);

			if (code != 200) {
				result = false;
				break;
			}

		}

		br.close();

		// init sensor fact with mock data

		String fact = SensorManager.DEGREES_FUNCTOR + "(1000)";

		int code = LPaasLibrary.postFact(THEORY_NAME, fact, token);

		if (code != 200) {
			result = false;
		}

		fact = SensorManager.HUMIDITY_FUNCTOR + "(1000)";

		code = LPaasLibrary.postFact(THEORY_NAME, fact, token);

		if (code != 200) {
			result = false;
		}

		fact = SensorManager.LIGHT_FUNCTOR + "(1000)";

		code = LPaasLibrary.postFact(THEORY_NAME, fact, token);

		if (code != 200) {
			result = false;
		}

		Vector<String> contentWB = loadContent();
		setRfidInnerClothing(contentWB);
		String in;

		for (String k : getRfidMapping().keySet()) {

			// init present functor with saved status

			if (getRfidInnerClothing().contains(k)) {
				in = "yes";

			} else {
				in = "no";
			}
			fact = PRESENT_FUNCTOR + "(" + k + "," + in + ")";

			code = LPaasLibrary.postFact(THEORY_NAME, fact, token);

			if (code != 200) {
				result = false;
				break;
			}

			// init all dress fact

			Dress d = getDressByRfid(k);
			fact = DRESS_FUNCTOR + "([" + d.getRfid() + "," + d.getName() + "," + d.getSize() + "," + d.getColor() + "," + d.getHeaviness() + "," + d.getType() + "])";
			code = LPaasLibrary.postFact(THEORY_NAME, fact, token);

			if (code != 200) {
				result = false;
				break;
			}

		}

		return result;
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

			result.add(line);

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

	private String randomRfid() {
		String[] rfidArray = new String[] { "eab5e4a4", "aa8be4a4", "baf7e3a4", "caf0e1a4", "NO-TAG" };
		Random random = new Random();
		int index = random.nextInt(rfidArray.length);
		return (rfidArray[index]);
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
