package it.unibo.homemanager.agents.wardrobe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
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
import cli.System.Array;
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
	private final static String THEORY_NAME = "wardrobeTheory";

	private final static String GOAL_SUNNY = "is_sunny.";
	private final static String GOAL_HUMIDITY = "is_humid.";
	private final static String GOAL_OUTFIT = "outfit([_|TOP_DRESS],[_|BOT_DRESS]).";

	private final static String GOALSLIST_SUNNY = "sunny";
	private final static String GOALSLIST_HUMIDITY = "humidity";
	private final static String GOALSLIST_OUTFIT = "outfits";

	private Device device;
	private String name;
	private String color;
	private boolean state;

	private List<StateChangeListener> stateListeners;
	private List<ContentChangeListener> listeners;

	private EnhancedSynchACC acc;

	private TucsonTupleCentreId wardrobeTc;
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

			do {

				result = scm.register(getName(), new URL("http://localhost:8080/lpaas"));

			} while (result == false);

			String[] credentials = readCredential("lpaas_credential.txt");

			Configurator configurator = scm.getConfigurator(getName(), credentials[0], credentials[1]);

			if (configurator.removeTheory(THEORY_NAME)) {
				System.out.println("TEORIA ELIMINATA");

			}

			String[] empty = new String[1];
			empty[1] = "";

			do {

				result = configurator.createTheory(THEORY_NAME, empty);

			} while (result == false);

			System.out.println("TEORIA CREATA");

			// init GoalsList

			String[] goal = new String[1];

			goal[1] = GOAL_OUTFIT;
			configurator.createGoal(GOALSLIST_OUTFIT, goal);
			goal[1] = GOAL_SUNNY;
			configurator.createGoal(GOALSLIST_SUNNY, goal);
			goal[1] = GOAL_HUMIDITY;
			configurator.createGoal(GOALSLIST_HUMIDITY, goal);

			while (true) {
				try {

					onContentChanged();

					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
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

		// Outfit outfit = new Outfit(new Dress("111", "felpa", 52, "top",
		// "Blu"), new Dress("222", "jeans", 52, "bot", "Blu"));
		//
		// outfit.addAccessory("occhiali da sole");
		//
		// outfits.add(outfit);

		SmartComponentManager scm = LPaaSComponentManager.getInstance();

		Client client = scm.getClient(getName());
		long theoryVersion = client.countVersion(THEORY_NAME);

		Solution solutionSunny = client.getSolutions(THEORY_NAME, theoryVersion, GOAL_SUNNY, -1, -1, null, null);
		Solution solutionHumidity = client.getSolutions(THEORY_NAME, theoryVersion, GOAL_HUMIDITY, -1, -1, null, null);

		// parse solutions e creazione lista accessori
		Solution solutionOutfit = client.getSolutions(THEORY_NAME, theoryVersion, GOALSLIST_OUTFIT, -1, -1, null, null);

		for (String s : solutionOutfit.getSolution()) {
			// parse stringa solution e costruzione oggetto outfit

			// Outfit o = new Outfit(top, bot);
			// outfits.add(o);

		}

		Vector<Outfit> result = new Vector<>();
		OutfitFilter f = new OutfitColorFilter(getColor());

		for (Outfit o : outfits) {
			if (f.filter(o)) {
				result.add(o);
			}
		}

		return result;
	}

	public Vector<Dress> getContent() {

		Vector<Dress> contentList = new Vector<>();
		
		
		SmartComponentManager scm = LPaaSComponentManager.getInstance();

		Client client = scm.getClient(getName());
		
		
		
		
		
		
		
		return contentList;
	}
}
