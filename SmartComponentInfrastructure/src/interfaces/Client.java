package interfaces;

import classes.Solution;
import exceptions.ExecutionFailureException;
import exceptions.NotCreatedException;

public interface Client {

	/*
	 * Modella i permessi del normale utente:
	 * 1)Accesso alle theories e i relativi facts
	 * 2)Accesso ai goals
	 * 3)Richiesta di solutions
	 */
	
	public String[] getTheories() throws NotCreatedException, ExecutionFailureException;
	
	public long countVersion(String theoryName) throws IllegalArgumentException, NotCreatedException, ExecutionFailureException;
	
	public String[] getFacts(String theoryName, long version) throws IllegalArgumentException, NotCreatedException, ExecutionFailureException;
	
	/*
	 * arity: Numero "operandi" di un fact
	 * limit: indica quanti facts si vuole in output
	 */
	public String[] getFactsWithFunctor(String theoryName, long version,
			String functor, int arity, long limit) throws IllegalArgumentException, ExecutionFailureException, NotCreatedException;
	
	public String[] getFactsWithFunctor(String theoryName, long version,
			String functor) throws IllegalArgumentException, ExecutionFailureException, NotCreatedException;
	
	public String[] getGoalList() throws ExecutionFailureException;
	
	public String[] getGoals(String goalName) throws IllegalArgumentException, ExecutionFailureException, NotCreatedException;
	
	public Solution getSolutions(String theoryPath, long version, String goalsPath,
			long skip, long limit, String within, String every) throws IllegalArgumentException, ExecutionFailureException, NotCreatedException;
	
}
