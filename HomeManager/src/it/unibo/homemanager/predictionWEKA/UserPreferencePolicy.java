package it.unibo.homemanager.predictionWEKA;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserPreferencePolicy {

	private int id_user;
	private int temp;
	private int id_stanza;
	private Map<Integer, ArrayList<Policy>> politiche;
	
	public UserPreferencePolicy(int id,int stanza, int temp)
	{
		setId_user(id);
		setTemp(temp);
		setId_stanza(stanza);
		politiche = new HashMap<Integer,ArrayList<Policy>>();
	}

	public int getId_user() {
		return id_user;
	}

	private void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public int getTemp() {
		return temp;
	}

	private void setTemp(int temp) {
		this.temp = temp;
	}
	

	
	public int getId_stanza() {
		return id_stanza;
	}

	public void setId_stanza(int id_stanza) {
		this.id_stanza = id_stanza;
	}
	

	public Map<Integer, ArrayList<Policy>> getPolitiche() {
		return politiche;
	}

	//metodo per l'aggiunta delle policy lette da rbac
	public boolean addPolicy(Policy p)
	   {
		 if (p!=null)
		    {
			 //se non è già presente il giorno devo creare la struttura ospitante
			  if (!politiche.containsKey(p.getGiorno_settimana())) 
			     {
				    ArrayList<Policy> insieme = new ArrayList<Policy>();
				    insieme.add(p);
				    politiche.put(p.getGiorno_settimana(),insieme);
			     }
			  else
			     {
				     ArrayList<Policy> esistenti = politiche.get(p.getGiorno_settimana());
				    esistenti.add(p);
				    politiche.put(p.getGiorno_settimana(),esistenti);
			     }
			 return true;
		    }else return false;
	   }
	
	
	//metodo per la rimozione di una politica qualora non servisse più
	public boolean removePolicy(Policy p)
	   {
		 ArrayList<Policy> lette =  politiche.get(p.getGiorno_settimana());
		 lette.remove(p);
		 politiche.put(p.getGiorno_settimana(), lette);
		 return true;
	   }

	@Override
	public String toString() {
		return "UserPreferencePolicy [id_user=" + id_user + ", temp=" + temp + ", id_stanza=" + id_stanza
				+ ", politiche=" + politiche + "]";
	}
	
	
	
	
}
