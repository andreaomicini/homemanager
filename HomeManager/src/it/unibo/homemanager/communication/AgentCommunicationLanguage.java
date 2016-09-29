package it.unibo.homemanager.communication;

public abstract class AgentCommunicationLanguage {
	
	private static final String NEW_NAME = "new_name";
	private static final String DEVICE = "device";
	private static final String INFO_DEVICE = "info_device";
	private static final String PRESENCE_INFORMATION = "presence_information";
	private static final String STATE_CHANGE = "state_change";
	private static final String REQUEST = "request";
	private static final String RESPONSE = "response";
	private static final String ORDER = "order";
	private static final String COOKING = "cooking";
	private static final String CHECK_RECIPE = "check_recipe";
	private static final String REMOVE_INGREDIENTS = "remove_ingredients";
	
	public static String getNewName() {
		return (NEW_NAME);
	}
	
	public static String getDevice() {
		return (DEVICE);
	}
	
	public static String getInfoDevice() {
		return (INFO_DEVICE);
	}

	public static String getPresenceInformation() {
		return (PRESENCE_INFORMATION);
	}

	public static String getStateChange() {
		return (STATE_CHANGE);
	}

	public static String getRequest() {
		return (REQUEST);
	}

	public static String getResponse() {
		return (RESPONSE);
	}

	public static String getOrder() {
		return (ORDER);
	}

	public static String getCooking() {
		return (COOKING);
	}

	public static String getCheckRecipe() {
		return (CHECK_RECIPE);
	}

	public static String getRemoveIngredients() {
		return (REMOVE_INGREDIENTS);
	}
}
