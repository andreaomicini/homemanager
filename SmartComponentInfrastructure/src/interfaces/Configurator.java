package interfaces;

import exceptions.AlreadyCreatedException;
import exceptions.ExecutionFailureException;
import exceptions.NotCreatedException;

public interface Configurator {

	/*
	 * Modella i permessi che può avere un configuratore:
	 * 1)Modifica/creazione/eliminazione delle theories
	 * 2)Modifica/creazione/eliminazione delle goalList
	 */
	
	public void createTheory(String theoryName, String[] theories) throws AlreadyCreatedException, IllegalArgumentException, ExecutionFailureException;
	
	public void addTheoryVersion(String theoryName, String[] theories) throws NotCreatedException, IllegalArgumentException, ExecutionFailureException;
	
	public void removeTheory(String theoryName) throws NotCreatedException, IllegalArgumentException, ExecutionFailureException;
	
	public void replaceTheory(String theoryName, String[] theories) throws NotCreatedException, IllegalArgumentException, ExecutionFailureException;
	
	public void createGoalList(String goalName, String[] goals) throws AlreadyCreatedException, IllegalArgumentException, ExecutionFailureException;
	
	public void addGoals(String goalName, String[] goal) throws NotCreatedException, IllegalArgumentException, ExecutionFailureException;
	
	public void removeGoal(String goalName) throws NotCreatedException, IllegalArgumentException, ExecutionFailureException;
	
	public void replaceGoal(String goalName, String[] goals) throws NotCreatedException, IllegalArgumentException, ExecutionFailureException;
}
