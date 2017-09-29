package it.unibo.homemanager.agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.swing.JPanel;

import alice.logictuple.LogicTuple;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;
import it.unibo.homemanager.userinterfaces.agents.ShopperPanel;
import it.unibo.homemanager.util.LogicTupleArgsPosition;
import it.unibo.homemanager.util.MailUtility;

public class ShopperAgent extends AbstractTucsonAgent implements TucsonAgentInterface {

	private String name; 
   	private TucsonTupleCentreId shopper_tc;
    private TracePanel tp;
    private EnhancedSynchACC acc;
    
    private ShopperPanel panel;
    
    private String fileName = "shopper_user_prefs.txt";
    
    private List<SupermarketsChangeListener> supermarketsListeners;
    
    private Map<String, List<String>> policiesTemplate;
    private List<String> priorityTemplate; 
    
    private int counter = 0;
	
    public ShopperAgent(String name, TucsonTupleCentreId shopper_tc, 
    		TracePanel tp) throws TucsonInvalidAgentIdException {
		super(name);
		this.name = name;
		this.shopper_tc = shopper_tc;
		this.tp = tp;
		
		supermarketsListeners = new ArrayList<>();
		
		policiesTemplate = initPoliciesTemplate();
		priorityTemplate = initPriorityTemplate();
	}
    
	public TucsonTupleCentreId getShopperTc() {
		return shopper_tc;
	}

	public TracePanel getTracePanel() {
		return tp;
	}

	public EnhancedSynchACC getAcc() {
		return acc;
	}

	public void setPanel(ViewManageAgentPanel manageAgentPanel) {
		panel = new ShopperPanel(manageAgentPanel, this, getTracePanel());
	}
	
	@Override
	public void show() {
		panel.init();
	}

	@Override
	public void hide() {
		panel.hidePanel();
	}

	@Override
	public JPanel getInterface() {
		return panel;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	protected void main() {
		
		int second = 1000;
		int minute = 60 * second;
				
		acc = getContext();

		try {
			getTracePanel().appendText("--- " + getName() + " STARTED!");
			
			initPolicies();

			while (true) {
				try {
					
					checkScarcityTuples();
					makeOrder();
					
					Thread.sleep(1 * second);
					
				} catch (Exception e) { }
			}
		} catch (Exception e) { }

	}

	private Map<String, List<String>> initPoliciesTemplate(){
		/* Qua bisogna inserire il nome e la descrizione dei campi delle varie tuple
		 * di policy che possono essere inserite dalla finestra add policy
		 * Come chiave il nome della policy, poi come elementi della lista corrispondente
		 * una stringa da far visualizzare all'utente nell'immissione.
		 * Quello che riguarda il supermarket nella policy non importa perchè la policy
		 * viene inserita a partire dal supermarket selezionato
		 * Per esempio:
		 * 		Nome policy: offer
		 * 		Formato tupla: offer(days_of_week,ingredient_name,supermarket_id)
		 * 		
		 * 		Chiave corrispondente: offer
		 * 		Elementi ordinati della lista: ["Days of week", "Ingredient name"]
		 */
		Map<String, List<String>> dict = new HashMap<>();
		List<String> element = new ArrayList<>();
		element.add("Days of week");
		dict.put("fidelty_card", element);
		
		element = new ArrayList<>();
		element.add("Days of week");
		element.add("Ingredient name");
		dict.put("offer", element);
		
		element = new ArrayList<>();
		element.add("Ingredient name");
		element.add("Quantity");
		dict.put("big_quantity", element);
		
		element = new ArrayList<>();
		element.add("Min products");
		dict.put("min_order", element);
		
		return dict;
	}
	
	private List<String> initPriorityTemplate(){
		/* 
		 * Qua bisogna inserire le politiche delle quali bisognerà scegliere
		 * la priorità, semplicemente mettendone il nome 
		 */
		List<String> res = new ArrayList<>();
		res.add("fidelty_card");
		res.add("offer");
		res.add("big_quantity");
		return res;
	}
	
	public List<String> getPriorityTemplate(){
		return priorityTemplate;
	}
	
	private void makeOrder() throws Exception{
		//buy(ingredient(ingredient_name,quantity))
		String nameTemplate = "buy";
		String tuple = nameTemplate + "(ingredient(A,B))";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().inAll(getShopperTc(), template, Long.MAX_VALUE);
		for(LogicTuple buyTuple : operation.getLogicTupleListResult())
			makeSingleOrder(buyTuple);
		
		//ordering(ingredient_name,quantity,supermarket_id)
		nameTemplate = "ordering";
		
		counter++;
		
		//l'ordine complessivo lo faccio solo ogni minuto circa (ogni 60 iterazioni)
		if(counter == 60){
			for(int id : getIds()){
				int minOrder = getMinOrderForSupermarket(id);
				tuple = nameTemplate + "(A,B," + id + ")";
				template = LogicTuple.parse(tuple);
				operation = getAcc().rdAll(getShopperTc(), template, Long.MAX_VALUE);
				if(operation.getLogicTupleListResult().size() >= minOrder){
					getAcc().inAll(getShopperTc(), template, Long.MAX_VALUE);
					sendOrder(operation.getLogicTupleListResult());				
				}
			}
			counter = 0;
		}
	}

    private int getMinOrderForSupermarket(int id) throws Exception{
    	//min_order(supermarket_id,min_element)
		String nameTemplate = "min_order";
		String tuple = nameTemplate + "(" + id + ",A)";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().rdp(getShopperTc(), template, Long.MAX_VALUE);
		if(operation.isResultSuccess())
			return operation.getLogicTupleResult().getArg(LogicTupleArgsPosition.SECOND).intValue();
		return 0; //ovvero faccio sempre l'ordine
	}

	private void sendOrder(List<LogicTuple> logicTupleListResult) throws Exception{
    	if(logicTupleListResult.isEmpty())
    		return;
    	
    	//supermarket_info(id,name,contact)
		String nameTemplate = "supermarket_info";
		String tuple = nameTemplate + "(" + logicTupleListResult.get(0).getArg(LogicTupleArgsPosition.THIRD).toString() + ",A,B)";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().rd(getShopperTc(), template, Long.MAX_VALUE);
		if(operation.isResultSuccess()){
			String supermarketName = operation.getLogicTupleResult().getArg(LogicTupleArgsPosition.SECOND).toString();
			String supermarketContact = operation.getLogicTupleResult().getArg(LogicTupleArgsPosition.THIRD).toString();
			if(supermarketContact.contains("@"))
				sendMail(logicTupleListResult, supermarketName, supermarketContact);
			else;
				//se ho altri metodi per contattare il venditore
		}
	}

    private void sendMail(List<LogicTuple> logicTupleListResult, String name, String mailto) {

    	StringBuilder list = new StringBuilder();
    	
    	list.append("Dear " + name + ", this is my shopping list:");
    	list.append(System.lineSeparator());
    	list.append(System.lineSeparator());
    	for(LogicTuple ingredient : logicTupleListResult){
    		list.append(ingredient.getArg(LogicTupleArgsPosition.FIRST).toString());
    		list.append(" - ");
    		list.append(ingredient.getArg(LogicTupleArgsPosition.SECOND).toString());
    		list.append(System.lineSeparator());
    	}
    	
        String host ="smtp.gmail.com";
        String us = "nicio.fava@gmail.com";
        String pass = "";

        String mitt = "nicio.fava@gmail.com";
        String[] toAddr = new String[2];
        toAddr[0] = mailto.startsWith("'") ? (mailto.replace("'", "")) : (mailto);
        toAddr[1]="nicio.fava@gmail.com";
        
        String oggetto = "Shopping list";
        try {
            MailUtility.sendMail(host, us, pass, toAddr, mitt, oggetto, list.toString());
        } catch (MessagingException ex) {
            Logger.getLogger(ShopperAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
	private List<Integer> getIds() {
		//supermarket_info(id,name,contact)
		String nameTemplate = "supermarket_info";
		String tuple = nameTemplate + "(A,B,C)";
		List<Integer> res = new ArrayList<>();
		try{
			LogicTuple template = LogicTuple.parse(tuple);
			ITucsonOperation operation = getAcc().rdAll(getShopperTc(), template, Long.MAX_VALUE);
			if(operation.isResultSuccess())
				for(LogicTuple supermarketId : operation.getLogicTupleListResult())
					res.add(supermarketId.getArg(LogicTupleArgsPosition.FIRST).intValue());
		}catch (Exception e) {
			return res;
		}
		return res;
	}

	private void makeSingleOrder(LogicTuple buyTuple) throws Exception{
		String ingredientName = buyTuple.getArg(LogicTupleArgsPosition.FIRST).getArg(LogicTupleArgsPosition.FIRST).toString();
		int quantity = buyTuple.getArg(LogicTupleArgsPosition.FIRST).getArg(LogicTupleArgsPosition.SECOND).intValue();
		
		Class[] signature = {String.class, int.class};		
		List<Method> findMethods = new ArrayList<>();
		
		for(String p : getIngredientPriority(ingredientName))
			findMethods.add(getClass().getDeclaredMethod("find_" + p, signature));

		boolean res = false;
		for(Method find : findMethods){
			res = (boolean)find.invoke(this, ingredientName, quantity);
			if(res)
				break;
		}
		
		//se nessuna policy ha dato esito positivo faccio l'ordine nel supermarket con id più basso
		if(!res)
			insertOrdering(ingredientName, quantity, Collections.min(getIds(), Integer::compare));
		
		
	}

	private List<String> getIngredientPriority(String ingredientName) throws Exception{
		//policies_priority(ingredient_name,priority,policy_name)
		
		List<String> policies = new ArrayList<>();
		
		String nameTemplate = "policies_priority";
		String tuple = nameTemplate + "(" + ingredientName + ",A,B)";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().rdAll(getShopperTc(), template, Long.MAX_VALUE);
		if(operation.isResultSuccess()){
			for(LogicTuple priority : operation.getLogicTupleListResult())
				policies.add(priority.getArg(LogicTupleArgsPosition.SECOND).toString() + "-" + priority.getArg(LogicTupleArgsPosition.THIRD).toString());
			
			policies = policies.stream().sorted().map(s -> s.substring(s.indexOf("-") + 1)).collect(Collectors.toList());
		}
		if(policies.size() == 0){
			if(ingredientName.equals("default")){
				policies.add("offer");
				policies.add("big_quantity");
				policies.add("fidelty_card");
				return policies;
			}
			return getIngredientPriority("default");
		}
		
		return policies;
	}
	
	
	private void insertOrdering(String ingredientName, int quantity, int supermarket_id) throws Exception{
		//ordering(ingredient_name,quantity,supermarket_id)
		String nameTemplate = "ordering";
		String tuple = nameTemplate + "(" + ingredientName + ",A," + supermarket_id + ")";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().inp(getShopperTc(), template, Long.MAX_VALUE);
		if(operation.isResultSuccess())
			quantity += operation.getLogicTupleResult().getArg(LogicTupleArgsPosition.SECOND).intValue();
		
		tuple = nameTemplate + "(" + ingredientName + "," + quantity + "," + supermarket_id + ")";
		template = LogicTuple.parse(tuple);
		getAcc().out(getShopperTc(), template, Long.MAX_VALUE);
	}
	
	private boolean find_offer(String ingredientName, int quantity) throws Exception{
		//offer(nro_day_of_week,ingredient_name,supermarket_id)
		int supermarket_id = -1;
		String today = LocalDate.now().getDayOfWeek().getValue() + "";
		
		String nameTemplate = "offer";	
		String tuple = nameTemplate + "(A," + ingredientName + ",B)";
		
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().rdAll(getShopperTc(), template, Long.MAX_VALUE);
		
		if(operation.isResultSuccess()){
			List<Integer> supermarket_ids = new ArrayList<>();		
			for(LogicTuple t : operation.getLogicTupleListResult())
				if(t.getArg(LogicTupleArgsPosition.FIRST).toString().contains(today))
					supermarket_ids.add(t.getArg(LogicTupleArgsPosition.THIRD).intValue());
		
			if(!supermarket_ids.isEmpty())
				supermarket_id = Collections.min(supermarket_ids, Integer::compare);
		}
		
		if(supermarket_id > -1){
			insertOrdering(ingredientName, quantity, supermarket_id);
			return true;
		}
		return false;
	}
	
	private boolean find_fidelty_card(String ingredientName, int quantity)  throws Exception{
		//fidelty_card(supermarket_id, days_of_week)
		int supermarket_id = -1;
		String today = LocalDate.now().getDayOfWeek().getValue() + "";
		
		String nameTemplate = "fidelty_card";
		String tuple = nameTemplate + "(A,B)";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().rdAll(getShopperTc(), template, Long.MAX_VALUE);
		if(operation.isResultSuccess()){
			List<Integer> supermarket_ids = new ArrayList<>();
			for(LogicTuple fideltyCard : operation.getLogicTupleListResult()){
				String daysOfWeek = fideltyCard.getArg(LogicTupleArgsPosition.SECOND).toString();
				if(daysOfWeek.contains(today))
					supermarket_ids.add(fideltyCard.getArg(LogicTupleArgsPosition.FIRST).intValue());
			}
			if(!supermarket_ids.isEmpty())
				supermarket_id = Collections.min(supermarket_ids, Integer::compare);
		}
		
		if(supermarket_id > -1){
			insertOrdering(ingredientName, quantity, supermarket_id);
			return true;
		}
		return false;
	}
	
	private boolean find_big_quantity(String ingredientName, int quantity)  throws Exception{
		//big_quantity(ingredient_name,big_quantity,supermarket_id)
		int supermarket_id = -1;
		
		String nameTemplate = "big_quantity";
		String tuple = nameTemplate + "(" + ingredientName + ",A,B)";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().rdp(getShopperTc(), template, Long.MAX_VALUE);
		if(operation.isResultSuccess() && quantity >= operation.getLogicTupleResult().getArg(LogicTupleArgsPosition.SECOND).intValue())
			supermarket_id = operation.getLogicTupleResult().getArg(LogicTupleArgsPosition.THIRD).intValue();
		
		if(supermarket_id > -1){
			insertOrdering(ingredientName, quantity, supermarket_id);
			return true;
		}
		return false;
	}
	
	/*
	 * TEMPLATE PER POLICY
	 * 
	 * private boolean find_policy(String ingredientName, int quantity)  throws Exception{
	 *	 //policy(ingredient_name,...,supermarket_id)
	 *   int supermarket_id = -1;
	 *	
	 *	 String nameTemplate = "policy";
	 *	 
	 *	 .
	 *	 .
	 * 	 .
	 *
	 *   if(supermarket_id > -1){
	 *		insertOrdering(ingredientName, quantity, supermarket_id);
	 *		return true;
	 *	 } 
	 *	 return false;
	 * }
	 * 
	 */
	
	private void checkScarcityTuples() throws Exception {
		//scarcity(id_user,ingredient_name,actual_quantity,threshold,timestamp)
		String nameTemplate = "scarcity";
		String tuple = nameTemplate + "(A,B,C,D,E)";
		
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().inAll(getShopperTc(), template, Long.MAX_VALUE);
		if(operation.isResultSuccess())
			for(LogicTuple scarcityTuple : operation.getLogicTupleListResult())
				addBuyTuple(scarcityTuple);		
		
	}

	private void addBuyTuple(LogicTuple scarcityTuple) throws Exception{
		//buy(ingredient(ingredient_name,quantity_to_buy))
    	String templateName = "buy";
    	String ingredientName = scarcityTuple.getArg(LogicTupleArgsPosition.SECOND).toString();
    	String tuple = templateName + "(ingredient(" + ingredientName + ",B))";
    	LogicTuple template = LogicTuple.parse(tuple);
    	ITucsonOperation operation = acc.inp(getShopperTc(), template, Long.MAX_VALUE);
    	int prevOrder = 0;
    	if(operation.isResultSuccess())
    		prevOrder = operation.getLogicTupleResult().getArg(LogicTupleArgsPosition.FIRST).getArg(LogicTupleArgsPosition.SECOND).intValue();
    	
    	int diffThresholdActual = scarcityTuple.getArg(LogicTupleArgsPosition.FOURTH).intValue()
    			- scarcityTuple.getArg(LogicTupleArgsPosition.THIRD).intValue();
    	
    	int minQuantityToBuy = getMinQuantityToBuy(ingredientName);
    	
    	int actualOrder = ((diffThresholdActual / minQuantityToBuy) + 1) * minQuantityToBuy;
    	
    	int order = prevOrder + actualOrder;
    	
    	tuple = templateName + "(ingredient(" + ingredientName + "," + order + "))";
    	template = LogicTuple.parse(tuple);
    	getAcc().out(getShopperTc(), template, Long.MAX_VALUE);
	}

	private int getMinQuantityToBuy(String ingredientName) throws Exception{
		//min_quantity(ingredient_name,min_quantity)
		String templateName = "min_quantity";
    	String tuple = templateName + "(" + ingredientName + ",B)";
    	LogicTuple template = LogicTuple.parse(tuple);
    	ITucsonOperation operation = acc.rdp(getShopperTc(), template, Long.MAX_VALUE);
    	if(!operation.isResultSuccess()){    	
	    	tuple = templateName + "(default,A)";
	    	template = LogicTuple.parse(tuple);
	    	operation = acc.rdp(getShopperTc(), template, Long.MAX_VALUE);
	    	if(!operation.isResultSuccess())
	    		return 1;
    	}
    	
    	return operation.getLogicTupleResult().getArg(LogicTupleArgsPosition.SECOND).intValue();
	}

	private void initPolicies() {
		File file = new File(fileName);
		try{
			if ( !file.exists() )
				file.createNewFile();

			BufferedReader reader = new BufferedReader(new FileReader(file));

			String line = "";

			/*
			 * le linee hanno lo stesso formato delle tuple
			 * ci sono diversi tipi di linee
			 * 
			 * user_pref(id_user,ingredient_name,threshold)
			 * min_quantity(ingredient_name,quantity)
			 * 		deve sempre essercene una con min_quantity(default,quantity)
			 * supermarket_info(id,name,contact)
		 	 * fidelty_card(supermarket_id, days_of_week)
		 	 * big_quantity(ingredient_name,big_quantity,supermarket_id)
			 * offer(days_of_week,ingredient_name,supermarket_id)
			 * 
			 */
			while ((line = reader.readLine()) != null) {
				if(!line.startsWith("#")){
					LogicTuple logicTuple = LogicTuple.parse(line);
					getAcc().out(getShopperTc(), logicTuple, Long.MAX_VALUE);
				}
			}

			reader.close();
			
			onSupermarketsChanged();
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	@Override
	public void operationCompleted(AbstractTupleCentreOperation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void operationCompleted(ITucsonOperation arg0) {
		// TODO Auto-generated method stub

	}

	
	public List<String> getAllSupermarkets(){
		//supermarket_info(id,name,contact)
		List<String> res = new ArrayList<>();
		
		try{
			String nameTemplate = "supermarket_info";
			String tuple = nameTemplate + "(A,B,C)";
			LogicTuple template = LogicTuple.parse(tuple);
			ITucsonOperation operation = getAcc().rdAll(getShopperTc(), template, Long.MAX_VALUE);
			if(operation.isResultSuccess())
				for(LogicTuple supermarket : operation.getLogicTupleListResult())
					res.add(supermarket.getArg(LogicTupleArgsPosition.FIRST).toString() + " " + supermarket.getArg(LogicTupleArgsPosition.SECOND).toString());
		}
		catch (Exception e) {}
		
		return res;
	}
	
	
	public List<SupermarketsChangeListener> getSupermarketsListeners() {
		return (supermarketsListeners);
	}
	
	public void addSupermarketsListener(SupermarketsChangeListener supermarketsChangeListener) {
		getSupermarketsListeners().add(supermarketsChangeListener);
	}
	
	public void removeSupermarketsListener(IngredientsChangeListener supermarketsChangeListener) {
		getSupermarketsListeners().remove(supermarketsChangeListener);
	}
	
	private void onSupermarketsChanged() {
		for ( SupermarketsChangeListener listener : getSupermarketsListeners() )
			listener.onSupermarketsChanged();
	}
	
	public void addSupermarket(String name, String contact) throws Exception{
		List<Integer> ids = getIds();
		ids.sort(null);
		int newId = ids.get(ids.size() - 1) + 1;
		
		//supermarket_info(id,name,contact)
		String nameTemplate = "supermarket_info";
		String tuple = nameTemplate + "(" + newId + "," + name + ",\"" + contact + "\")";
		LogicTuple template = LogicTuple.parse(tuple);
		getAcc().out(getShopperTc(), template, Long.MAX_VALUE);
		
		onSupermarketsChanged();
	}
	
	public Map<String, List<String>> getPoliciesTemplate(){
		return policiesTemplate;
	}
	
	public void addPolicy(String policy) throws Exception{
		/* Viene usato per aggiungere qualsiasi policy.
		 * Dovrei controllare che non ce ne sia già una uguale
		 * ("uguale" cambia a seconda del tipo di policy che inserisco)
		 * 
		 * Possibili:
		 * 
		 * offer(days_of_week,ingredient_name,supermarket_id)
		 * big_quantity(ingredient_name,big_quantity,supermarket_id)
		 * fidelty_card(supermarket_id,days_of_week)
		 * 
		 * min_order(supermarket_id,min_element)
		 * 
		 * policies_priority(ingredient_name,priority,policy_name)
		 */
		
		
		removePrev_offer(policy);
		removePrev_big_quantity(policy);
		removePrev_fidelty_card(policy);
		removePrev_min_order(policy);
		removePrev_policies_priority(policy);
		
		LogicTuple template = LogicTuple.parse(policy);
		getAcc().out(getShopperTc(), template, Long.MAX_VALUE);
	}
	
	private void removePrev_policies_priority(String policy) throws Exception{
		if(!policy.startsWith("policies_priority"))
			return;
		String tuple = policy.substring(0, policy.indexOf(",")) + ",A" + policy.substring(policy.lastIndexOf(","));
		LogicTuple template = LogicTuple.parse(tuple);
		getAcc().inAll(getShopperTc(), template, Long.MAX_VALUE);
	}

	private void removePrev_min_order(String policy) throws Exception{
		if(!policy.startsWith("min_order"))
			return;	
		String tuple = policy.substring(0, policy.indexOf(",")) + ",A)";
		LogicTuple template = LogicTuple.parse(tuple);
		getAcc().inp(getShopperTc(), template, Long.MAX_VALUE);
	}
	
	private void removePrev_fidelty_card(String policy) throws Exception{
		if(!policy.startsWith("fidelty_card"))
			return;
		String tuple = policy.substring(0, policy.indexOf(",")) + ",A)";
		LogicTuple template = LogicTuple.parse(tuple);
		getAcc().inp(getShopperTc(), template, Long.MAX_VALUE);
	}

	private void removePrev_big_quantity(String policy) throws Exception{
		if(!policy.startsWith("big_quantity"))
			return;
		String tuple = policy.substring(0, policy.indexOf(",")) + ",A" + policy.substring(policy.lastIndexOf(","));
		LogicTuple template = LogicTuple.parse(tuple);
		getAcc().inp(getShopperTc(), template, Long.MAX_VALUE);
	}

	private void removePrev_offer(String policy) throws Exception{
		if(!policy.startsWith("offer"))
			return;
		String tuple = policy.substring(0, 6) + "A" + policy.substring(policy.indexOf(","));
		LogicTuple template = LogicTuple.parse(tuple);
		getAcc().inp(getShopperTc(), template, Long.MAX_VALUE);
		tuple = policy.substring(0, policy.lastIndexOf(",")) + ",A)";
		template = LogicTuple.parse(tuple);
		getAcc().inp(getShopperTc(), template, Long.MAX_VALUE);
	}

	public void addThreshold(String user, String ingredient, int threshold) throws Exception{
		//user_pref(id_user,ingredient_name,threshold)
		String nameTemplate = "user_pref";
		String tuple = nameTemplate + "(A," + ingredient + ",B)";
		LogicTuple template = LogicTuple.parse(tuple);
		getAcc().inp(getShopperTc(), template, Long.MAX_VALUE);
		
		tuple = nameTemplate + "(" + user + "," + ingredient + "," + threshold + ")";
		template = LogicTuple.parse(tuple);
		getAcc().out(getShopperTc(), template, Long.MAX_VALUE);
	}
	
	public List<String> getAllThresholds() throws Exception{
		List<String> res = new ArrayList<>();
		
		//user_pref(id_user,ingredient_name,threshold)
		String tuple = "user_pref(A,B,C)";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().rdAll(getShopperTc(), template, Long.MAX_VALUE);
		if(operation.isResultSuccess())
			for(LogicTuple t : operation.getLogicTupleListResult())
				res.add("Ingredient name: " + t.getArg(LogicTupleArgsPosition.SECOND).toString() + " - " +
						"Threshold: " + t.getArg(LogicTupleArgsPosition.THIRD).intValue());		
		return res;
	}
	
	public List<String> getAllPolicies() throws Exception{
		List<String> res = new ArrayList<>();
		
		//big_quantity(ingredient_name,big_quantity,supermarket_id)
		String tuple = "big_quantity(A,B,C)";
		LogicTuple template = LogicTuple.parse(tuple);
		ITucsonOperation operation = getAcc().rdAll(getShopperTc(), template, Long.MAX_VALUE);
		if(operation.isResultSuccess())
			for(LogicTuple t : operation.getLogicTupleListResult())
				res.add("Ingredient: " + t.getArg(LogicTupleArgsPosition.FIRST).toString() + " - " +
						"big_quantity - " +
						"Quantity: " + t.getArg(LogicTupleArgsPosition.SECOND).toString() + " - " +
						"Supermarket id: " + t.getArg(LogicTupleArgsPosition.THIRD).toString());
		
		//offer(days_of_week,ingredient_name,supermarket_id)
		tuple = "offer(A,B,C)";
		template = LogicTuple.parse(tuple);
		operation = getAcc().rdAll(getShopperTc(), template, Long.MAX_VALUE);
		if(operation.isResultSuccess())
			for(LogicTuple t : operation.getLogicTupleListResult())
				res.add("Ingredient: " + t.getArg(LogicTupleArgsPosition.SECOND).toString() + " - " + 
						"offer - " + 
						"Days of week: " + t.getArg(LogicTupleArgsPosition.FIRST).toString() + " - "  +
						"Supermarket id: " + t.getArg(LogicTupleArgsPosition.THIRD).toString());
		res.sort(null);
		
		//fidelty_card(supermarket_id, days_of_week)
		tuple = "fidelty_card(A,B)";
		template = LogicTuple.parse(tuple);
		operation = getAcc().rdAll(getShopperTc(), template, Long.MAX_VALUE);
		if(operation.isResultSuccess())
			for(LogicTuple t : operation.getLogicTupleListResult())
				res.add("Supermarket id: " + t.getArg(LogicTupleArgsPosition.FIRST).toString() + 
						" - fidelty_card - Days of week: " + 
						t.getArg(LogicTupleArgsPosition.SECOND).toString());
		
				return res;
	}
}
