package it.unibo.homemanager.agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.logictuple.Value;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import it.unibo.homemanager.communication.AgentCommunicationLanguage;
import it.unibo.homemanager.dbmanagement.Database;
import it.unibo.homemanager.detection.Device;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;
import it.unibo.homemanager.userinterfaces.agents.FridgePanel;
import it.unibo.homemanager.util.LogicTupleArgsPosition;

public class FridgeAgent extends AbstractTucsonAgent implements TucsonAgentInterface, ContainersInterface {
	private Device device;
	private String name;
	
	private boolean state;
	
	private List<Ingredient> currentIngredients;
	
	private List<StateChangeListener> stateListeners;
	private List<IngredientsChangeListener> ingredientsListeners;
	
	private EnhancedSynchACC acc;

	private TucsonTupleCentreId fridgeTc;
	private TucsonTupleCentreId mixerContainerTc;
	private TucsonTupleCentreId usageManagerTc;
	private TucsonTupleCentreId shopperTc;

	private FridgePanel fridgePanel;
	private TracePanel tracePanel;

	public FridgeAgent(Device device, TracePanel tracePanel, TucsonTupleCentreId fridgeTc,
			TucsonTupleCentreId mixerContainerTc, TucsonTupleCentreId usageManagerTc, TucsonTupleCentreId shopperTc, Database database)
					throws TucsonInvalidAgentIdException {
		super(device.getDeviceName() + "_" + device.getDeviceId());

		this.device = device;
		this.name = device.getDeviceName() + "_" + device.getDeviceId();
		
		state = false;
		currentIngredients = new ArrayList<Ingredient>();
		
		stateListeners = new ArrayList<StateChangeListener>();
		ingredientsListeners = new ArrayList<IngredientsChangeListener>();
		
		this.fridgeTc = fridgeTc;
		this.mixerContainerTc = mixerContainerTc;
		this.usageManagerTc = usageManagerTc;
		this.shopperTc = shopperTc;
		
		this.tracePanel = tracePanel;
	}

	public JPanel getInterface() {
		return (fridgePanel);
	}

	public void setPanel(ViewManageAgentPanel manageAgentPanel) {
		fridgePanel = new FridgePanel(manageAgentPanel, this, getTracePanel());
	}

	public Device getDevice() {
		return (device);
	}

	public String getName() {
		return (name);
	}

	public boolean getState() {
		return (state);
	}

	public void setState(boolean state) {
		this.state = state;
		onStateChanged();
	}
	
	public List<Ingredient> getCurrentIngredients() {
		return (currentIngredients);
	}

	public void setCurrentIngredients(List<Ingredient> currentIngredients) throws Exception {
		this.currentIngredients = currentIngredients;
		onIngredientsChanged();
		
		persistIngredients();
	}
	
	private void persistIngredients() throws Exception{
		File file = new File(getName() + "_ingredients.txt");
		PrintWriter writer = new PrintWriter(file);
		
		String nameTemplate = "content";
		
		for(Ingredient ingredient : getCurrentIngredients()){
			// content(name, productName, productQuantity)
			String tuple = nameTemplate + "(" + getName() + "," + ingredient.getName() + "," + ingredient.getQuantity() + ")";
			writer.println(tuple);
		}
		
		writer.close();
	}

	public List<StateChangeListener> getStateListeners() {
		return (stateListeners);
	}
	
	public void addStateListener(StateChangeListener stateChangeListener) {
		getStateListeners().add(stateChangeListener);
	}
	
	public void removeStateListener(StateChangeListener stateChangeListener) {
		getStateListeners().remove(stateChangeListener);
	}
	
	public List<IngredientsChangeListener> getIngredientsListeners() {
		return (ingredientsListeners);
	}
	
	public void addIngredientsListener(IngredientsChangeListener ingredientsChangeListener) {
		getIngredientsListeners().add(ingredientsChangeListener);
	}
	
	public void removeIngredientsListener(IngredientsChangeListener ingredientsChangeListener) {
		getIngredientsListeners().remove(ingredientsChangeListener);
	}

	public EnhancedSynchACC getAcc() {
		return (acc);
	}

	public TucsonTupleCentreId getFridgeTupleCentre() {
		return (fridgeTc);
	}

	public TucsonTupleCentreId getMixerContainerTc() {
		return (mixerContainerTc);
	}

	public TucsonTupleCentreId getUsageManagerTc() {
		return (usageManagerTc);
	}
	
	public TucsonTupleCentreId getShopperTc() {
		return (shopperTc);
	}
	
	public FridgePanel getFridgePanel() {
		return (fridgePanel);
	}

	public TracePanel getTracePanel() {
		return (tracePanel);
	}

	protected void main() {
		acc = getContext();

		try {
			getTracePanel().appendText("--- " + getName() + " STARTED!");
			
			initIngredients();
			
			writePresenceInformation();
			
			boolean result = false;
			do {
				result = turnOn();
			} while (result == false);
			
			while (true) {
				try {
					getContent();
					
					LogicTuple checkRecipeRequest = checkRecipeRequest();
					if (checkRecipeRequest != null) {
						checkRecipeAnswer(checkRecipeRequest);
						checkRemoveIngredientsFromFridge();
					}
					
					Thread.sleep(1000);
				} catch (Exception e) { }
			}
		} catch (Exception e) { }
	}

	private void initIngredients() throws Exception{
		File file = new File(getName() + "_ingredients.txt");
		
		if ( !file.exists() )
			file.createNewFile();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String line = "";
		
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		
		while ( (line = reader.readLine()) != null) {
			LogicTuple logicTuple = LogicTuple.parse(line);
			getAcc().out(getFridgeTupleCentre(), logicTuple, Long.MAX_VALUE);
						
			String productName = logicTuple.getArg(LogicTupleArgsPosition.SECOND).toString();
			int productQuantity = logicTuple.getArg(LogicTupleArgsPosition.THIRD).intValue();
			
			Ingredient ingredient = new Ingredient(productName, productQuantity);
			ingredients.add(ingredient);
		}
		
		reader.close();
		
		setCurrentIngredients(ingredients);
	}
	
	private boolean writePresenceInformation() throws Exception {
		String nameTemplate = AgentCommunicationLanguage.getPresenceInformation();
		String tuple = nameTemplate + "(" + getDevice().getDeviceName() + "(" + getDevice().getDeviceId() + ")" + "," + AgentCommunicationLanguage.getInfoDevice() + 
						"(" + "off" + "," + getDevice().getDeviceEnergy() + ")" + "," + getDevice().getDeviceType() + ")";
		
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().out(getUsageManagerTc(), template, Long.MAX_VALUE);
		return (operation.isResultSuccess());
	}

	private boolean turnOn() throws Exception {
		String nameTemplate = AgentCommunicationLanguage.getStateChange();
		String tuple = nameTemplate + "(" + AgentCommunicationLanguage.getRequest() + "," + getDevice().getDeviceName() + "(" + getDevice().getDeviceId() + 
						")" + "," + "on" + "," + getDevice().getDeviceType() + ")";
		
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().out(getUsageManagerTc(), template, Long.MAX_VALUE);
		
		if ( !operation.isResultSuccess() )
			return (false);
		
		nameTemplate = AgentCommunicationLanguage.getStateChange();
		tuple = nameTemplate + "(" + AgentCommunicationLanguage.getResponse() + "," + getDevice().getDeviceName() + "(" + getDevice().getDeviceId() + ")" + "," + "A" + "," + 
				 getDevice().getDeviceType() + ")";
		
		template = LogicTuple.parse(tuple);
		operation = getAcc().in(getUsageManagerTc(), template, Long.MAX_VALUE);
		
		if ( !operation.isResultSuccess() )
			return (false);
		
		LogicTuple result = operation.getLogicTupleResult();
		String deviceState = result.getArg(LogicTupleArgsPosition.THIRD).toString();
	
		if ( !deviceState.equals("on") )
			return (false);
		
		setState(true);
		return (true);
	}

	private LogicTuple checkRecipeRequest() throws Exception {
		String nameTemplate = AgentCommunicationLanguage.getCheckRecipe();
		String fridgeName = getName();

		String tuple = nameTemplate + "(" + AgentCommunicationLanguage.getRequest() + "," + fridgeName + "," + "A" + ")";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().inp(getMixerContainerTc(), template, Long.MAX_VALUE);

		if (!operation.isResultSuccess())
			return (null);
		return (operation.getLogicTupleResult());
	}

	private boolean checkRecipeAnswer(LogicTuple request) throws Exception {
		List<Ingredient> ingredients = getCurrentIngredients();

		TupleArgument missingIngredientsArgument = request.getArg(LogicTupleArgsPosition.THIRD);
		List<Ingredient> missingIngredients = new ArrayList<Ingredient>();

		for (int i = 0; i < missingIngredientsArgument.getArity(); i++) {
			TupleArgument missingIngredientArgument = missingIngredientsArgument.getArg(i);
			String ingredientName = missingIngredientArgument.getArg(LogicTupleArgsPosition.FIRST).toString();
			int ingredientQuantity = missingIngredientArgument.getArg(LogicTupleArgsPosition.SECOND).intValue();
			Ingredient missingIngredient = new Ingredient(ingredientName, ingredientQuantity);
			missingIngredients.add(missingIngredient);
		}

		List<Ingredient> myIngredients = new ArrayList<Ingredient>();
		for (Ingredient ingredient : missingIngredients)
			if (ingredients.contains(ingredient)) {
				Ingredient fridgeIngredient = ingredients.get(ingredients.indexOf(ingredient));
				if (fridgeIngredient.getQuantity() > ingredient.getQuantity())
					myIngredients.add(ingredient);
				else
					myIngredients.add(fridgeIngredient);
			}
		
		String nameTemplate = AgentCommunicationLanguage.getCheckRecipe();
		String fridgeName = getName();

		String strIngredients = getStringListIngredients(myIngredients);
		
		String tuple = nameTemplate + "(" + AgentCommunicationLanguage.getResponse() + "," + fridgeName + "," + strIngredients + ")";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().out(getMixerContainerTc(), template, Long.MAX_VALUE);
		return (operation.isResultSuccess());
	}

	private void checkRemoveIngredientsFromFridge() throws Exception {
		String nameTemplate = AgentCommunicationLanguage.getRemoveIngredients();
		String fridgeName = getName();

		String tuple = nameTemplate + "(" + fridgeName + "," + "A,B" + ")";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().in(getMixerContainerTc(), template, Long.MAX_VALUE);
		if (operation.isResultSuccess()) {
			LogicTuple removeRequest = operation.getLogicTupleArgument();
			String remove = removeRequest.getArg(LogicTupleArgsPosition.THIRD).toString();
			if (remove.equals("remove"))
				removeIngredientsFromFridge(removeRequest);
		}
	}
	
	private boolean removeIngredientsFromFridge(LogicTuple removeRequest) throws Exception {
		List<Ingredient> fridgeContent = getCurrentIngredients();
		TupleArgument ingredients = removeRequest.getArg(LogicTupleArgsPosition.SECOND);

		for (int i = 0; i < ingredients.getArity(); i++) {
			TupleArgument ingredientArgument = ingredients.getArg(i);
			String name = ingredientArgument.getArg(LogicTupleArgsPosition.FIRST).toString();
			int quantity = ingredientArgument.getArg(LogicTupleArgsPosition.SECOND).intValue();
			Ingredient fridgeIngredient = fridgeContent.get(fridgeContent.indexOf(new Ingredient(name, -1)));
			Ingredient ingredient = new Ingredient(name, fridgeIngredient.getQuantity() - quantity);
			if (!updateContent(ingredient))
				return (false);
		}
		return (true);
	}

	private String getStringListIngredients(List<Ingredient> ingredients) {
		if (ingredients.size() == 0)
			return ("ingredients");

		String result = "";
		result = "ingredients" + "(";
		for (Ingredient ingredient : ingredients)
			result = result + "ingredient" + "(" + ingredient.getName() + "," + ingredient.getQuantity() + ")" + ",";
		result = result.substring(0, result.length() - 1);
		result = result + ")";
		return (result);
	}

	public void operationCompleted(AbstractTupleCentreOperation arg) { }

	public void operationCompleted(ITucsonOperation arg) { }

	public synchronized void getContent() throws Exception {
		String nameTemplate = "content";
		String fridgeName = getName();

		String tuple = nameTemplate + "(" + fridgeName + "," + "A,B" + ")";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().rdAll(getFridgeTupleCentre(), template, Long.MAX_VALUE);

		List<LogicTuple> logicTuples = operation.getLogicTupleListResult();
		List<Ingredient> ingredients = new ArrayList<Ingredient>();

		for (LogicTuple logicTuple : logicTuples) {
			String name = logicTuple.getArg(LogicTupleArgsPosition.SECOND).toString();
			int quantity = logicTuple.getArg(LogicTupleArgsPosition.THIRD).intValue();
			Ingredient ingredient = new Ingredient(name, quantity);
			ingredients.add(ingredient);
		}
		
		if ( checkDifferences(ingredients) )
			setCurrentIngredients(ingredients);
	}
	
	private boolean checkDifferences(List<Ingredient> ingredients) {
		if ( ingredients.size() != getCurrentIngredients().size() )
			return (true);
		
		for (Ingredient ingredient : ingredients) {
			if ( !getCurrentIngredients().contains(ingredient) )
				return (true);
			
			Ingredient fridgeIngredient = getCurrentIngredients().get(getCurrentIngredients().indexOf(ingredient));
			if ( ingredient.getQuantity() != fridgeIngredient.getQuantity() )
				return (true);
		}
		
		return (false);
	}

	public boolean addIngredient(Ingredient ingredient) throws Exception {
		String nameTemplate = "content";
		String fridgeName = getName();

		String tuple = nameTemplate + "(" + fridgeName + "," + ingredient.getName() + "," + "A" + ")";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().inp(getFridgeTupleCentre(), template, Long.MAX_VALUE);

		if (operation.isResultSuccess()) {
			LogicTuple result = operation.getLogicTupleResult();
			int oldQuantity = result.getArg(LogicTupleArgsPosition.THIRD).intValue();
			int newQuantity = oldQuantity + ingredient.getQuantity();
			tuple = nameTemplate + "(" + fridgeName + "," + ingredient.getName() + "," + newQuantity + ")";
			template = LogicTuple.parse(tuple);
			operation = getAcc().out(getFridgeTupleCentre(), template, Long.MAX_VALUE);
			return (operation.isResultSuccess());
		} else { // ( if operation.isResultFailure() )
			tuple = nameTemplate + "(" + fridgeName + "," + ingredient.getName() + "," + ingredient.getQuantity() + ")";
			template = LogicTuple.parse(tuple);
			operation = getAcc().out(getFridgeTupleCentre(), template, Long.MAX_VALUE);
			return (operation.isResultSuccess());
		}
	}

	public boolean updateContent(Ingredient ingredient) throws Exception {
		String nameTemplate = "content";
		String fridgeName = getName();
		
		String tuple = nameTemplate + "(" + fridgeName + "," + ingredient.getName() + "," + "A" + ")";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().inp(getFridgeTupleCentre(), template, Long.MAX_VALUE);

		if (operation.isResultSuccess()) {
			int newQuantity = ingredient.getQuantity();
			if (newQuantity > 0) {
				tuple = nameTemplate + "(" + fridgeName + "," + ingredient.getName() + "," + newQuantity + ")";
				template = LogicTuple.parse(tuple);
				operation = getAcc().out(getFridgeTupleCentre(), template, Long.MAX_VALUE);
				checkThreshold(ingredient);
				return (operation.isResultSuccess());
			}
			return (true);
		}
		return (false);
	}
	
	private void checkThreshold(Ingredient ingredient) throws Exception{
		String nameTemplate = "user_pref";
		String tuple = nameTemplate + "(A," + ingredient.getName() + ",B)";
		
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().rdp(getShopperTc(), template, Long.MAX_VALUE);
		if(operation.isResultSuccess()){
			int threshold = operation.getLogicTupleResult().getArg(LogicTupleArgsPosition.THIRD).intValue();
			if(ingredient.getQuantity() <= threshold){
				//genera tupla scarcity
				//scarcity(idUser,nameProduct,quantityActual,threshold,TimeStamp)
				Date d = new Date();
		        SimpleDateFormat df = new SimpleDateFormat("\"yyyy-mm-dd_hh-mm-ss\"");
		        String timestamp = df.format(d);
				nameTemplate = "scarcity";
				tuple = nameTemplate + "(" + operation.getLogicTupleResult().getArg(LogicTupleArgsPosition.FIRST).toString() + "," + ingredient.getName() + "," + ingredient.getQuantity() + "," + threshold + "," + timestamp + ")";
				template = LogicTuple.parse(tuple);
				operation = getAcc().out(getShopperTc(), template, Long.MAX_VALUE);
			}
		}
	}

	private void onStateChanged() {
		for ( StateChangeListener listener : getStateListeners() )
			listener.onStateChanged();
	}
	
	private void onIngredientsChanged() {
		for ( IngredientsChangeListener listener : getIngredientsListeners() )
			listener.onIngredientsChanged();
	}

	public void show() {
		getFridgePanel().init();
	}

	public void hide() {
		getFridgePanel().hidePanel();
	}

}
