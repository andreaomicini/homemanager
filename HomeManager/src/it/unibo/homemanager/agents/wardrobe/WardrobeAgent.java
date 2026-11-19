package it.unibo.homemanager.agents.wardrobe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JPanel;

import alice.logictuple.LogicTuple;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import classes.Solution;
import exceptions.AlreadyCreatedException;
import exceptions.NotCreatedException;
import interfaces.Client;
import interfaces.Configurator;
import interfaces.SmartComponentManager;
import it.unibo.homemanager.agents.StateChangeListener;
import it.unibo.homemanager.agents.TucsonAgentInterface;
import it.unibo.homemanager.communication.AgentCommunicationLanguage;
import it.unibo.homemanager.dbmanagement.Database;
import it.unibo.homemanager.detection.Device;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;
import it.unibo.homemanager.userinterfaces.agents.WardrobePanel;
import lpaas.LPaaSComponentManager;

public class WardrobeAgent extends AbstractTucsonAgent implements TucsonAgentInterface {

	// private final static String BASENAME = "http://localhost:8080/lpaas";

	private static String BASENAME = null;

	private final static String THEORY_NAME = "wardrobeTheory";

	private final static String GOAL_SUNNY = "is_sunny";
	private final static String GOAL_HUMIDITY = "is_humid";
	private final static String GOAL_OUTFIT = "outfit([Top_rfid|Top_dress],[Bot_rfid|Bot_dress])";

	private final static String GOALSLIST_SUNNY = "sunny";
	private final static String GOALSLIST_HUMIDITY = "humidity";
	private final static String GOALSLIST_OUTFIT = "outfits";

	final static String DRESS_FUNCTOR = "dress";
	final static String PRESENT_FUNCTOR = "present";

	private Device device;
	private String name;
	private String color;
	private boolean state;

	private List<StateChangeListener> stateListeners;
	private List<ContentChangeListener> listeners;

	private EnhancedSynchACC acc;

	private TucsonTupleCentreId wardrobeTc; // centro di tuple non usato per la
											// comunicazione. Al suo posto viene
											// utilizzato il servizio LPaaS
	private TucsonTupleCentreId usageManagerTc;

	private WardrobePanel wardrobePanel;
	private TracePanel tracePanel;

	public WardrobeAgent(Device device, TracePanel tracePanel, TucsonTupleCentreId wardrobeTc, TucsonTupleCentreId usageManagerTc, Database database)
			throws TucsonInvalidAgentIdException {
		super(device.getDeviceName() + "_" + device.getDeviceId());

		this.device = device;
		this.name = device.getDeviceName() + "_" + device.getDeviceId();

		state = false;
		listeners = new ArrayList<ContentChangeListener>();
		stateListeners = new ArrayList<StateChangeListener>();

		this.wardrobeTc = wardrobeTc;
		this.usageManagerTc = usageManagerTc;

		this.tracePanel = tracePanel;

		// lettura da file esterno dell'ip del server LPaaS per rendere più dinamica la configurazione
		readIPServerLPaaS();

	}

	private static void readIPServerLPaaS() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("ip_serverLPaaS.txt"));
			String line;

			line = br.readLine();

			if (line != null) {

			}
			BASENAME = "http://" + line.trim() + ":8080/lpaas";

			br.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	protected void main() {

		acc = getContext();

		try {
			getTracePanel().appendText("--- " + getName() + " STARTED!");

			writePresenceInformation();

			boolean result = false;
			do {
				result = turnOn();
			} while (result == false);

			SmartComponentManager scm = LPaaSComponentManager.getInstance();

			scm.register(getName(), BASENAME);

			String[] credentials = readCredential("lpaas_credential.txt");

			Configurator configurator = scm.getConfigurator(getName(), credentials[0], credentials[1]);

			try {

				configurator.removeTheory(THEORY_NAME);

				System.out.println("TEORIA " + THEORY_NAME + " ELIMINATA");

			} catch (NotCreatedException e) {
				System.out.println("TEORIA " + THEORY_NAME + " NON ESISTENTE");

			}

			String[] empty = new String[1];
			empty[0] = "";

			configurator.createTheory(THEORY_NAME, empty);

			System.out.println("TEORIA " + THEORY_NAME + " CREATA");

			// remove GoalsList

			// try {
			// configurator.removeGoal(GOALSLIST_OUTFIT);
			// }
			//
			// catch (NotCreatedException e) {
			//
			// System.out.println("GOALLIST " + GOALSLIST_OUTFIT + " NON
			// ESISTENTE");
			//
			// }
			//
			// try {
			// configurator.removeGoal(GOALSLIST_SUNNY);
			// }
			//
			// catch (NotCreatedException e) {
			// System.out.println("GOALLIST " + GOALSLIST_SUNNY + " NON
			// ESISTENTE");
			//
			// }
			//
			// try {
			// configurator.removeGoal(GOALSLIST_HUMIDITY);
			// }
			//
			// catch (NotCreatedException e) {
			//
			// System.out.println("GOALLIST " + GOALSLIST_HUMIDITY + " NON
			// ESISTENTE");
			//
			// }

			// create GoalsList

			String[] goal = new String[1];

			try {
				goal[0] = GOAL_OUTFIT;
				configurator.createGoalList(GOALSLIST_OUTFIT, goal);

			} catch (AlreadyCreatedException e) {
				System.out.println("GOALLIST " + GOALSLIST_OUTFIT + " GIA' PRESENTE");

			}

			try {

				goal[0] = GOAL_SUNNY;
				configurator.createGoalList(GOALSLIST_SUNNY, goal);

			} catch (AlreadyCreatedException e) {
				System.out.println("GOALLIST GIA' " + GOALSLIST_SUNNY + " PRESENTE");

			}

			try {
				goal[0] = GOAL_HUMIDITY;
				configurator.createGoalList(GOALSLIST_HUMIDITY, goal);

			} catch (AlreadyCreatedException e) {
				System.out.println("GOALLIST GIA' " + GOALSLIST_HUMIDITY + " PRESENTE");

			}

			while (true) {
				try {

					onContentChanged();

					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private String[] readCredential(String nomeFile) {
		String[] result = null;

		try {
			String username = "";
			String password = "";

			String keyword;

			BufferedReader br = new BufferedReader(new FileReader(nomeFile));
			String line;
			while ((line = br.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(line, ":");

				if (st.countTokens() == 2) {
					keyword = st.nextToken();
					if (keyword.equalsIgnoreCase("username")) {
						username = st.nextToken().trim();
					}
					if (keyword.equalsIgnoreCase("password")) {
						password = st.nextToken().trim();
					}
				}

			}

			br.close();

			result = new String[2];
			result[0] = username;
			result[1] = password;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	public boolean turnOn() throws Exception {
		String nameTemplate = AgentCommunicationLanguage.getStateChange();
		String tuple = nameTemplate + "(" + AgentCommunicationLanguage.getRequest() + "," + getDevice().getDeviceName() + "(" + getDevice().getDeviceId() + ")" + "," + "on" + ","
				+ getDevice().getDeviceType() + ")";

		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().out(getUsageManagerTc(), template, Long.MAX_VALUE);

		if (!operation.isResultSuccess())
			return (false);

		nameTemplate = AgentCommunicationLanguage.getStateChange();
		tuple = nameTemplate + "(" + AgentCommunicationLanguage.getResponse() + "," + getDevice().getDeviceName() + "(" + getDevice().getDeviceId() + ")" + "," + "A" + ","
				+ getDevice().getDeviceType() + ")";

		template = LogicTuple.parse(tuple);
		operation = getAcc().in(getUsageManagerTc(), template, Long.MAX_VALUE);

		if (!operation.isResultSuccess())
			return (false);

		LogicTuple result = operation.getLogicTupleResult();
		int third = 2;
		String deviceState = result.getArg(third).toString();

		if (!deviceState.equals("on"))
			return (false);

		setState(true);
		return (true);
	}

	private boolean writePresenceInformation() throws Exception {
		String nameTemplate = AgentCommunicationLanguage.getPresenceInformation();
		String tuple = nameTemplate + "(" + getDevice().getDeviceName() + "(" + getDevice().getDeviceId() + ")" + "," + AgentCommunicationLanguage.getInfoDevice() + "(" + "off"
				+ "," + getDevice().getDeviceEnergy() + ")" + "," + getDevice().getDeviceType() + ")";

		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().out(getUsageManagerTc(), template, Long.MAX_VALUE);
		return (operation.isResultSuccess());
	}

	@Override
	public void operationCompleted(AbstractTupleCentreOperation arg0) {

	}

	@Override
	public void operationCompleted(ITucsonOperation arg0) {

	}

	public void show() {
		getWardrobePanel().init();
	}

	public void hide() {
		getWardrobePanel().hidePanel();
	}

	public void setPanel(ViewManageAgentPanel manageAgentPanel) {
		wardrobePanel = new WardrobePanel(manageAgentPanel, this, getTracePanel());
	}

	@Override
	public JPanel getInterface() {
		return (wardrobePanel);
	}

	@Override
	public String getName() {
		return name;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public EnhancedSynchACC getAcc() {
		return acc;
	}

	public void setAcc(EnhancedSynchACC acc) {
		this.acc = acc;
	}

	public TucsonTupleCentreId getWardrobeTc() {
		return wardrobeTc;
	}

	public void setWardrobeTc(TucsonTupleCentreId wardrobeTc) {
		this.wardrobeTc = wardrobeTc;
	}

	public TucsonTupleCentreId getUsageManagerTc() {
		return usageManagerTc;
	}

	public void setUsageManagerTc(TucsonTupleCentreId usageManagerTc) {
		this.usageManagerTc = usageManagerTc;
	}

	public WardrobePanel getWardrobePanel() {
		return wardrobePanel;
	}

	public void setWardrobePanel(WardrobePanel wardrobePanel) {
		this.wardrobePanel = wardrobePanel;
	}

	public TracePanel getTracePanel() {
		return tracePanel;
	}

	public void setTracePanel(TracePanel tracePanel) {
		this.tracePanel = tracePanel;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public List<ContentChangeListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<ContentChangeListener> listeners) {
		this.listeners = listeners;
	}

	public List<StateChangeListener> getStateListeners() {
		return stateListeners;
	}

	public void setStateListeners(List<StateChangeListener> stateListeners) {
		this.stateListeners = stateListeners;
	}

	public void addStateListener(ContentChangeListener stateChangeListener) {
		getListeners().add(stateChangeListener);
	}

	public void removeStateListener(ContentChangeListener stateChangeListener) {
		getListeners().remove(stateChangeListener);
	}

	private void onContentChanged() {
		for (ContentChangeListener listener : getListeners())
			listener.onContentChanged();
	}

	private void onStateChanged() {
		for (StateChangeListener listener : getStateListeners())
			listener.onStateChanged();
	}

	public Vector<Outfit> generateOutfit() {

		Vector<Outfit> outfits = new Vector<>();
		Vector<Outfit> result = new Vector<>();

		SmartComponentManager scm = LPaaSComponentManager.getInstance();

		try {
			Client client = scm.getClient(getName());

			// long theoryVersion = client.countVersion(THEORY_NAME);

			long theoryVersion = -1; // usa l'ultima versione

			Solution solutionSunny = client.getSolutions(THEORY_NAME, theoryVersion, GOALSLIST_SUNNY, -1, -1, null, null);
			Solution solutionHumidity = client.getSolutions(THEORY_NAME, theoryVersion, GOALSLIST_HUMIDITY, -1, -1, null, null);

			Solution solutionOutfit = client.getSolutions(THEORY_NAME, theoryVersion, GOALSLIST_OUTFIT, -1, -1, null, null);

			for (String s : solutionOutfit.getSolution()) {

				StringTokenizer st = new StringTokenizer(s, "()[], ");
				if (st.hasMoreTokens()) {
					String outfit = st.nextToken();
					if (outfit.equalsIgnoreCase("outfit")) {

						String rfid_top = st.nextToken();
						String name_top = st.nextToken();
						String stringSize_top = st.nextToken();
						String color_top = st.nextToken();

						int size_top = Integer.parseInt(stringSize_top);

						Dress top = new Dress(rfid_top, name_top, size_top, color_top, "top");

						String rfid_bot = st.nextToken();
						String name_bot = st.nextToken();
						String stringSize_bot = st.nextToken();
						String color_bot = st.nextToken();

						int size_bot = Integer.parseInt(stringSize_bot);

						Dress bot = new Dress(rfid_bot, name_bot, size_bot, color_bot, "bot");
						Outfit o = new Outfit(top, bot);

						if (solutionSunny.getSolution().length != 0 && solutionSunny.getSolution()[0].equalsIgnoreCase(GOAL_SUNNY)) {
							o.addAccessory("Sunglasses");
						}

						if (solutionHumidity.getSolution().length != 0 && solutionHumidity.getSolution()[0].equalsIgnoreCase(GOAL_HUMIDITY)) {
							o.addAccessory("Umbrella");
						}

						outfits.add(o);

					}
				}
			}

			OutfitFilter f = new OutfitColorFilter(getColor());

			for (Outfit o : outfits) {
				if (f.filter(o)) {
					result.add(o);
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

	public Vector<Dress> getContent() {

		Vector<Dress> contentList = new Vector<>();

		SmartComponentManager scm = LPaaSComponentManager.getInstance();

		try {
			Client client = scm.getClient(getName());

			// long version = client.countVersion(THEORY_NAME);

			long version = -1; // usa l'ultima versione

			String[] present_facts = client.getFactsWithFunctor(THEORY_NAME, version, PRESENT_FUNCTOR);

			Vector<String> rfid_in = new Vector<>();
			String rfid;
			String state;

			for (String fact : present_facts) {

				StringTokenizer st = new StringTokenizer(fact, "(), ");
				st.nextToken();

				rfid = st.nextToken();
				state = st.nextToken();

				if (state.equalsIgnoreCase("yes")) {
					rfid_in.add(rfid);
				}

			}

			String[] dress_facts = client.getFactsWithFunctor(THEORY_NAME, version, DRESS_FUNCTOR);

			for (String fact : dress_facts) {

				StringTokenizer st = new StringTokenizer(fact, "()[], ");
				st.nextToken();

				rfid = st.nextToken();

				if (rfid_in.contains(rfid)) {

					String name = st.nextToken();
					String stringSize = st.nextToken();
					String color = st.nextToken();
					String heaviness = st.nextToken();
					String type = st.nextToken();
					int size = Integer.parseInt(stringSize);

					Dress d = new Dress(rfid, name, size, color, heaviness, type);

					contentList.add(d);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return contentList;
	}
}
