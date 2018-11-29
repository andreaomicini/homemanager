package test.controller;

import java.util.HashMap;
import java.util.Map;

import classes.Solution;
import exceptions.AlreadyRegisteredException;
import exceptions.NotCreatedException;
import interfaces.Configurator;
import lpaas.LPaaSComponentManager;

public class Controller {

	private Map<String,Configurator> components;
	private LPaaSComponentManager manager;
	
	public Controller(){
		this.components=new HashMap<String,Configurator>();
		this.manager=LPaaSComponentManager.getInstance();
	}
	
	public String[] getComponents(){
		return components.keySet().toArray(new String[components.keySet().size()]);
	}
	
	public boolean register(String component, String location) throws AlreadyRegisteredException{
		if(component==null || location==null)
			return false;
		
		try{
			manager.register(component, location);
			components.put(component, null);
		}catch(IllegalArgumentException e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean addConfigurator(String component, String username, String password){
		if(component==null || username==null || password==null)
			return false;
		
		Configurator configurator=null;
		try {
			configurator=manager.getConfigurator(component, username, password);
			components.put(component, configurator);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean unregister(String component){
		if(component==null)
			return false;
		
		try {
			if(components.containsKey(component)){
				manager.unRegister(component);
				components.remove(component);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean configuratorPresent(String selectedItem) {
		if(this.components.get(selectedItem)!=null)
			return true;
		 return false;
	}
	
	//PARTE CLIENT
	/*
	 * Modella i permessi del normale utente:
	 * 1)Accesso alle theories e i relativi facts
	 * 2)Accesso ai goals
	 * 3)Richiesta di solutions
	 */
	public String[] getGoalLists(String component){
		try {
			return manager.getClient(component).getGoalList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String[] getTheories(String component){
		try {
			return manager.getClient(component).getTheories();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public long getCountVersion(String component,String theoryName){
		try {
			return manager.getClient(component).countVersion(theoryName);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public String[] getFacts(String component,String theoryName, Long long1){
		if(component==null)
			return null;
		
		try {
			return manager.getClient(component).getFacts(theoryName, long1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String[] getFactsWithFunctor(String component,String theoryName, Long long1,
			String functor, int arity, long limit){
		try {
			return manager.getClient(component).getFactsWithFunctor(theoryName, long1, functor, arity, limit);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String[] getGoals(String component,String goalName){
		if(component==null)
			return null;
		try {
			return manager.getClient(component).getGoals(goalName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Solution getSolutions(String component,String theoryPath, long versione, String goalsPath,
			String hook, long skip,long limit, String within, String every){
		try {
			return manager.getClient(component).getSolutions(theoryPath, versione, goalsPath, skip, limit, within, every);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//PARTE CONFIGURATOR
	public boolean createTheory(String component,String theoryName, String[] theories){
		
		try {
			components.get(component).createTheory(theoryName, theories);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean addTheoryVersion(String component,String theoryName, String[] theories) throws NotCreatedException{
		try { 
		components.get(component).addTheoryVersion(theoryName, theories);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean removeTheory(String component,String theoryName){
		try { 
		components.get(component).removeTheory(theoryName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean replaceTheory(String component,String theoryName, String[] theories){
		try { 
		components.get(component).replaceTheory(theoryName, theories);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean createGoal(String component,String goalName, String[] goals){
		try { 
		 components.get(component).createGoalList(goalName, goals);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	
		return true;
	
	}
	
	public boolean addGoals(String component,String goalName, String[] goal){
		try { 
		components.get(component).addGoals(goalName, goal);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean removeGoal(String component,String goalName){
		try { 
		components.get(component).removeGoal(goalName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean replaceGoal(String component,String goalName, String[] goals){
		try { 
		components.get(component).replaceGoal(goalName, goals);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

}
