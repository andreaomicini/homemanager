package interfaces;

import exceptions.AlreadyRegisteredException;
import exceptions.AuthorizationFailureException;
import exceptions.NotAuthorizedException;
import exceptions.NotRegisteredException;

public interface SmartComponentManager {
	/*
	 * Il manager è ciò che permette agli agenti di interfacciarsi
	 * con i dispositivi smart presenti in HM
	 */

	//Permette di registrare un componente sul manager
	public void register(String component, String serverLocation) throws AlreadyRegisteredException, IllegalArgumentException;
	
	//Permette di rimuovere un componente dal manager
	public void unRegister(String component) throws NotRegisteredException, IllegalArgumentException;
	
	//Permette di ottenere l'interfaccia Configurator per un particolare componente
	public Configurator getConfigurator(String component, String username, String password) throws NotRegisteredException, NotAuthorizedException, IllegalArgumentException, AuthorizationFailureException;
	
	//Permette di ottenere l'interfaccia Client per un particolare componente
	public Client getClient(String component) throws NotRegisteredException, IllegalArgumentException;
	
	//Permette di ottenere l'interfaccia Sensor per un particolare componente
	public Sensor getSensor(String component, String username, String password) throws NotRegisteredException, NotAuthorizedException, IllegalArgumentException, AuthorizationFailureException;
}
