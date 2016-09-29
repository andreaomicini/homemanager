package model.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import model.Device;
import model.Ingredient;
import model.Recipe;
import alice.logictuple.LogicTuple;
import alice.logictuple.TupleArgument;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;

public class OvenAgent extends AbstractTucsonAgent {
	private Device device;
	private String name;
	
	private boolean state;
	private Recipe currentRecipe;
	
	private List<StateChangeListener> listeners;
	
	private EnhancedSynchACC acc;
	
	public TucsonTupleCentreId ovenTc;
	private TucsonTupleCentreId usageManagerTc;
	
	public OvenAgent(Device device, TucsonTupleCentreId ovenTc, TucsonTupleCentreId usageManagerTc) throws TucsonInvalidAgentIdException {
		super(device.getName() + "_" + device.getId());
		
		this.device = device;
		name = device.getName() + "_" + device.getId();

		state = false;
		currentRecipe = null;
		listeners = new ArrayList<StateChangeListener>();

		this.ovenTc = ovenTc;
		this.usageManagerTc = usageManagerTc;
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
	
	public Recipe getCurrentRecipe() {
		return (currentRecipe);
	}
	
	public void setCurrentRecipe(Recipe currentRecipe) {
		this.currentRecipe = currentRecipe;
		onStateChanged();
	}
	
	public List<StateChangeListener> getListeners() {
		return (listeners);
	}
	
	public void addListener(StateChangeListener stateChangeListener) {
		getListeners().add(stateChangeListener);
	}
	
	public void removeListener(StateChangeListener stateChangeListener) {
		getListeners().remove(stateChangeListener);
	}
	
	public EnhancedSynchACC getAcc() {
		return (acc);
	}
	
	public TucsonTupleCentreId getOvenTc() {
		return (ovenTc);
	}
	
	public TucsonTupleCentreId getUsageManagerTc() {
		return (usageManagerTc);
	}
	
	protected void main() {
		acc = getContext();
		
		System.out.println("--- " + getName() + " STARTED!");

		try {
			writePresenceInformation();
			
			while (true) {
				alignState();
				
				LogicTuple recipeRequest = isThereRecipeToCook();
				if ( recipeRequest != null )
					cookRecipe(recipeRequest);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) { }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean writePresenceInformation() throws Exception {
		String nameTemplate = "presence_information";
		String tuple = nameTemplate + "(" + getDevice().getName() + "(" + getDevice().getId() + ")" + "," + "info_device" + 
						"(" + "off" + "," + getDevice().getWatt() + ")" + "," + getDevice().getType() + ")";
		
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().out(getUsageManagerTc(), template, Long.MAX_VALUE);
		return (operation.isResultSuccess());
	}
	
	public boolean removePresenceInformation() throws Exception {
		String nameTemplate = "presence_information";
		String tuple = nameTemplate + "(" + getDevice().getName() + "(" + getDevice().getId() + ")" + "," + "info_device" + 
						"(" + "A" + "," + getDevice().getWatt() + ")" + "," + getDevice().getType() + ")";
		
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().inp(getUsageManagerTc(), template, Long.MAX_VALUE);
		return (operation.isResultSuccess());
	}
	
	private void alignState() throws Exception {
		String nameTemplate = "state_change";
		String tuple = nameTemplate + "(" + "order" + "," + getDevice().getName() + "("
				+ getDevice().getId() + ")" + "," + "A" + "," + getDevice().getType() + ")";
		
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().inp(getUsageManagerTc(), template, Long.MAX_VALUE);
		
		if (operation.isResultSuccess()) {
			LogicTuple result = operation.getLogicTupleResult();
			
			int first = 0;
			int second = 1;
			int third = 2;
			int fourth = 3;
			int fifth = 4;
			
			String deviceState = result.getArg(third).toString();
			if (deviceState.equals("on")) {
				setState(true);
				
				nameTemplate = "current";
				tuple = nameTemplate + "(" + getDevice().getName() + "(" + getDevice().getId() + ")" + "," + "A" + ")";
				template = LogicTuple.parse(tuple);
				operation = getAcc().rd(getOvenTc(), template, Long.MAX_VALUE);
				
				if (operation.isResultSuccess()) {
					result = operation.getLogicTupleResult();
					TupleArgument recipeArgument = result.getArg(second);
					
					int recipeId = recipeArgument.getArg(first).intValue();
					String recipeName = recipeArgument.getArg(second).toString();
					int cookingTime = recipeArgument.getArg(third).intValue();
					int temperature = recipeArgument.getArg(fourth).intValue();
					TupleArgument ingredientsArgument = recipeArgument.getArg(fifth);
					
					List<Ingredient> ingredients = new ArrayList<Ingredient>();
					for (int i = 0; i < ingredientsArgument.getArity(); i++) {
						TupleArgument ingredientArgument = ingredientsArgument.getArg(i);
						String name = ingredientArgument.getArg(first).toString();
						int quantity = ingredientArgument.getArg(second).intValue();
						Ingredient ingredient = new Ingredient(name, quantity);
						ingredients.add(ingredient);
					}
					
					Recipe currentRecipe = new Recipe(recipeId, recipeName, cookingTime, temperature, ingredients);
					setCurrentRecipe(currentRecipe);
				}
			}
			else { // if ( deviceState.equals("off") )
				setState(false);
				setCurrentRecipe(null);
			}
		}
	}
	
	private LogicTuple isThereRecipeToCook() throws Exception {
		String nameTemplate = "cooking";
		String tuple = nameTemplate + "(" + "request" + "," + getDevice().getName() + "(" + 
						getDevice().getId() + ")" + "," + "A" + ")";
		
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().inp(getOvenTc(), template, Long.MAX_VALUE);
		
		if (!operation.isResultSuccess())
			return (null);
		
		return (operation.getLogicTupleResult());
	}
	
	private void cookRecipe(LogicTuple resultTuple) throws Exception {
		int first = 0;
		int second = 1;
		int third = 2;
		int fourth = 3;
		int fifth = 4;

		String nameTemplate = "cooking";
		final String deviceName = resultTuple.getArg(second).toString();
		final TupleArgument recipeInformation = resultTuple.getArg(third);
		
		int recipeId = recipeInformation.getArg(first).intValue();
		String recipeName = recipeInformation.getArg(second).toString();
		int cookingTime = recipeInformation.getArg(third).intValue();
		int temperature = recipeInformation.getArg(fourth).intValue();
		TupleArgument ingredientsArgument = recipeInformation.getArg(fifth);
		
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		for (int i = 0; i < ingredientsArgument.getArity(); i++) {
			TupleArgument ingredientArgument = ingredientsArgument.getArg(i);
			String name = ingredientArgument.getArg(first).toString();
			int quantity = ingredientArgument.getArg(second).intValue();
			Ingredient ingredient = new Ingredient(name, quantity);
			ingredients.add(ingredient);
		}
		
		Recipe recipe = new Recipe(recipeId, recipeName, cookingTime, temperature, ingredients);
		
		int result = turnOn(recipe);
		String tuple = nameTemplate + "(" + "response" + "," + deviceName + "," + result + ")";
		LogicTuple template = LogicTuple.parse(tuple);
		getAcc().out(getOvenTc(), template, Long.MAX_VALUE);

		if (result == 0) {
			nameTemplate = "current";
			tuple = nameTemplate + "(" + deviceName + "," + recipeInformation + ")";
			template = LogicTuple.parse(tuple);
			getAcc().out(getOvenTc(), template, Long.MAX_VALUE);
			
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						String nameTemplate = "current";
						String tuple = nameTemplate + "(" + deviceName + "," + recipeInformation + ")";
						LogicTuple template = LogicTuple.parse(tuple);
						getAcc().inp(getOvenTc(), template, Long.MAX_VALUE);
						
						turnOff();
					} catch (Exception e) { }
				}
			}, cookingTime * 1000);
		}
	}
	
	public int turnOn(Recipe currentRecipe) throws Exception {
		int third = 2;

		// alignState();
		if ( getState() )
			return (2);

		String nameTemplate = "state_change";
		String tuple = nameTemplate + "(" + "request" + "," + getDevice().getName() + "("
				+ getDevice().getId() + ")" + "," + "on" + "," + getDevice().getType() + ")";
		
		LogicTuple template = LogicTuple.parse(tuple);
		getAcc().out(getUsageManagerTc(), template, Long.MAX_VALUE);

		tuple = nameTemplate + "(" + "response" + "," + getDevice().getName() + "(" + getDevice().getId()
				+ ")" + "," + "A" + "," + getDevice().getType() + ")";
		
		template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().in(getUsageManagerTc(), template, Long.MAX_VALUE);
		
		LogicTuple tupleResult = operation.getLogicTupleResult();
		String response = tupleResult.getArg(third).toString();
		
		if ( !response.equals("on") )
			return (1);
			
		setState(true);
		setCurrentRecipe(currentRecipe);
		return (0);
	}
	
	public boolean turnOff() throws Exception {
		String nameTemplate = "state_change";
		String tuple = nameTemplate + "(" + "request" + "," + getDevice().getName() + "("
				+ getDevice().getId() + ")" + "," + "off" + "," + getDevice().getType() + ")";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().out(getUsageManagerTc(), template, Long.MAX_VALUE);
		
		if ( !operation.isResultSuccess() )
			return (false);
		
		setState(false);
		setCurrentRecipe(null);
		return (true);
	}
	
	private void onStateChanged() {
		for ( StateChangeListener listener : getListeners() )
			listener.onStateChanged();
	}
	
	public void operationCompleted(AbstractTupleCentreOperation arg) { }
	
	public void operationCompleted(ITucsonOperation arg) { }
	
}
