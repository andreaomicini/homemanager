package it.unibo.homemanager.predictionWEKA;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JPanel;

import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import it.unibo.homemanager.agents.TucsonAgentInterface;
import it.unibo.homemanager.dbmanagement.Database;
import it.unibo.homemanager.dbmanagement.TucsonDatabase;
import it.unibo.homemanager.meteo.Meteo;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;
;



public class SmartPredictorAgent extends AbstractTucsonAgent implements TucsonAgentInterface {

	private TucsonTupleCentreId policy;
	private TucsonTupleCentreId meteo;
	private TucsonTupleCentreId presence;
	private TucsonTupleCentreId stanza;
	
	private EnhancedSynchACC acc;
	
	private WEKAPredictor wp;
	private Meteo previsione_meteo;
	private UserPreferencePolicy upt;
	
	private  Map<Integer,String> mappa;
	
	private ViewSmartPredictorAgentPanel panel;
	private TracePanel tp;
	private Database db;
	
	
	public SmartPredictorAgent(String id, TracePanel tp, TucsonTupleCentreId policy,TucsonTupleCentreId meteo,TucsonTupleCentreId presence,Database db, TucsonTupleCentreId sala) throws Exception {
		super(id);
		this.policy = policy;
		this.meteo = meteo;
		this.presence = presence;
		this.stanza = sala;
	
		this.tp = tp;
		this.db = db;
		
		wp = new WEKAPredictor("predictorAgent",this.presence);
		
	}
	
	
	public WEKAPredictor getWEKAPredictor()
	{ return wp;}
	
	
	private UserPreferencePolicy parsePreferenza(String s)
	
	  {
		UserPreferencePolicy upt = null;
		String app = s.substring(s.indexOf('(')+1, s.indexOf(')'));
		StringTokenizer st = new StringTokenizer(app,",");
		if(st.countTokens()==3)
		   {
			 int id = Integer.parseInt(st.nextToken().trim());
			 int stanza =Integer.parseInt(st.nextToken().trim());
			 int temp = Integer.parseInt(st.nextToken().trim());
			 
			 upt = new UserPreferencePolicy(id,stanza, temp);
		   }
		return upt;
	  }
	
	private Policy parsePolicy(String s)
	   {
		   Policy p = null;
		   String app = s.substring(s.indexOf('(')+1, s.indexOf(')'));
			StringTokenizer st = new StringTokenizer(app,",");
			if(st.countTokens()==5)
			   {
				 int id = Integer.parseInt(st.nextToken().trim());
				 int stanza =Integer.parseInt(st.nextToken().trim());
				 int giorno = Integer.parseInt(st.nextToken().trim());
				 String orari_apice = st.nextToken().trim();
				 String ora_app = orari_apice.substring(1, orari_apice.length()-1);
				 String[] orari = ora_app.split("-");
				 int ora_inizio = Integer.parseInt(orari[0]);
				 int ora_fine = Integer.parseInt(orari[1]);
				 String stato = st.nextToken();
				 		 
				 p = new Policy(stanza,giorno,ora_inizio,ora_fine,stato);
			   }
		   
		   return p;
	   }
	private UserPreferencePolicy leggiPolitiche(int id_stanza)
	  {
  		
		    UserPreferencePolicy upt = null;
		    
	    	//tupla con le preferenze dell'utente loggato.
  		
			try {
				//politica per la soglia temperatura per grace in sala
				LogicTuple template = new LogicTuple("user_temp_pref_cond",new Value(4), new Value(id_stanza),new Var("Y"));
			
				LogicTuple ltTemplate = LogicTuple.parse(template.toString());
				
			    ITucsonOperation op_rdAll = acc.rdp(policy, ltTemplate, Long.MAX_VALUE);
			    if(op_rdAll.isResultSuccess()) {
				LogicTuple trovati = op_rdAll.getLogicTupleResult();
				upt = parsePreferenza(trovati.toString());
				System.out.println("[readCentreArray(V)@Policy] Successfully read " + trovati);
			    } else
				System.err.println("[readCentreArray(V)@Policy] rd (" + ltTemplate + ") failed!");
			
			    //politiche giornate specifiche
			    LogicTuple template2 = new LogicTuple("rule_cond_pref",new Value(4), new Value(id_stanza),new Var("Y"), new Var("X"),new Var("Z"));
				
				LogicTuple ltTemplate2 = LogicTuple.parse(template2.toString());
				
			    ITucsonOperation op_rdAll2 = acc.rdAll(policy, ltTemplate2, Long.MAX_VALUE);
			    if(op_rdAll2.isResultSuccess()) {
				List<LogicTuple> politiche_trovate = op_rdAll2.getLogicTupleListResult();
				
				for(LogicTuple lt : politiche_trovate)
				   {
					 Policy p = parsePolicy(lt.toString());
					 upt.addPolicy(p);
				   }
				
				
				
				
				System.out.println("[readCentreArray(V)@Policy] Successfully read " + politiche_trovate);
			    } else
				System.err.println("[readCentreArray(V)@Policy] rdAll(" + ltTemplate2 + ") failed!");
			    
			    
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    	    
		    return upt;
	  }
	
	
	private Meteo leggiMeteo()
	 {
		
		List<String> informazioniLette = new ArrayList<String>();
		Meteo m = null;
			
		
		//lettura dal centro di tuple i dati che mi interessano
		
		try{
		
	    //primo template
		LogicTuple template1 =new LogicTuple("meteo",new Value("openweathermap"),new Var("Y"), new Var("Z"),new Var("J"));
		
		LogicTuple ltTemplate = LogicTuple.parse(template1.toString());
		
	    ITucsonOperation op_rd1 = acc.rdp(meteo, ltTemplate, Long.MAX_VALUE);
	    if(op_rd1.isResultSuccess()) {
		LogicTuple trovato = op_rd1.getLogicTupleResult();
		informazioniLette.add(trovato.toString());
		}
	    
	    //secondo template
	    LogicTuple template2 =new LogicTuple("meteo",new Value("openweathermap"),new Var("Y"),new Var("A"),new Var("Z"),new Var("W"),new Var("J"),new Var("B"));
		
		LogicTuple ltTemplate2 = LogicTuple.parse(template2.toString());
		
	    ITucsonOperation op_rd2 = acc.rdp(meteo, ltTemplate2, Long.MAX_VALUE);
	    if(op_rd2.isResultSuccess()) {
		LogicTuple trovato = op_rd2.getLogicTupleResult();
		informazioniLette.add(trovato.toString());
		}
	    
	   //parse per ottenere un oggetto meteo dalle tuple
	    
	    m = ParseMeteoTucson.parseMeteo(informazioniLette);
	    
	    
		} catch(Exception e)
		  {
			e.printStackTrace();
		  }
		
     return m;
  				
	 }
	
	public Meteo getMeteoAttuale()
	   {
		 return previsione_meteo;
	   }
	
	public boolean isRainingDay(Meteo m)
	   {
		 if(m.getMeteo().toLowerCase().contains("rain")) return true;
		     else return false;
	   }
	
	public UserPreferencePolicy getUserPreferencePolicy()
	   {
		 return upt;
	   }
	
	public Map<Integer,String> getMap()

	   {
		 return mappa;
	   }
	
	
	public Map<Integer,String> makeDecision(int id_stanza)
	  {
		Map<Integer,String> predizioni_giornata = new HashMap<Integer,String>();
	    try {
				
		        //tuple preferenze utente
			    upt = leggiPolitiche(id_stanza);
			    
			    //tuple del meteo
		    	Meteo meteo = leggiMeteo();
			    
			    previsione_meteo = meteo;
			    
			  //tuple weka predette presenza in casa
		    	List<HomePresencePrediction> predizioni_home = wp.makePredictionByDay(LocalDate.now().getDayOfWeek().getValue());
			
		    	//Mix delle condizioni lette home- meteo- politiche
		    	for(HomePresencePrediction p : predizioni_home)
		    	   { 
		    		
		    		/*per la predizione ON :
		    		 * Non deve esserci previsione meteo pioggia
		    		 * la temperatura attuale deve essere superiore alla soglia imposta dall'utente
		    		 * l'utente deve essere in casa
		    		 * si devono controllare eventuali politiche
		    		 * */
		    		
		    		Policy politica = checkPolicy(p.getHour());
		    		
		    		// se non c'è politica allora procedo con mix informazioni
		    		if (politica==null){
		    		if (((!isRainingDay(meteo)) && 
		    			   (meteo.getTemperatura()>= upt.getTemp())) &&
		    		      (p.getIn_home() == User_location.casa_in))
		    	       {
		    		      predizioni_giornata.put(p.getHour(),"ON");
		    	       }
		    	      else predizioni_giornata.put(p.getHour(), "OFF");
		    	   }
		    		//se c'è la politica forzo il risultato
		    	   else 
		    	     {
		    		   predizioni_giornata.put(p.getHour(),politica.getStato_condizionatore().toUpperCase());
		    	     }
		    		
		    	   }
		    
		    } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		        
		 return  predizioni_giornata;
	
	  }


	//restituisce - la policy se la trova per l'orario disposto -> vincolo una policy per ora
public Policy  checkPolicy(int ora)
  {
	int giorno_settimana = (LocalDate.now().getDayOfWeek().getValue());
	
	ArrayList<Policy> politiche_giornata = upt.getPolitiche().get(giorno_settimana);
	
	if(politiche_giornata!=null)
	   {
		 for(Policy p: politiche_giornata)
		    {
			 if ((ora>=p.getOrario_start()) && (ora<=p.getOrario_finish())) return p;
		    }
		 return null;
	   }
	else return null;
  }
      

private long readTimer()
   {
	 TucsonDatabase dbt = (TucsonDatabase) db;
	long num = 0;
	 
	 try {
		LogicTuple init = new LogicTuple("timer_prediction",new Var("X"));
		LogicTuple lt = dbt.read(init);
		
		if(lt!=null)
		   {
			  num = Long.parseLong(lt.getArg(0).toString());
		   }
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
	 return num;
   }
			

	@Override
	protected void main() {
	
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		tp.appendText("-- SMART PREDICTOR AGENT  " + stanza.getName() +"  STARTED.");
		this.acc = this.getContext();
		wp.go();

		try {
			Thread.sleep(8000);
			while(true){
			
				int id_stanza=0;
				if(stanza.getName().equals("sala_tc")) id_stanza = 2;
				else if(stanza.getName().equals("camera_tc")) id_stanza = 3;
			
			mappa = makeDecision(id_stanza);
			
			LocalDateTime adesso = LocalDateTime.now();
			
			String new_value = mappa.get(adesso.getHour());
		    
			int id_device = 0;
			if (id_stanza==2) id_device = 6;
			   else if(id_stanza==3) id_device = 8;
			LogicTuple init = new LogicTuple("upd_dev_curr_st",new Value(id_device),new Value(new_value.toLowerCase()));
			acc.out(stanza,init, Long.MAX_VALUE);
			
		tp.appendText("-- SMART PREDICTOR AGENT "+ stanza.getName()+ " UPDATED.");
		
			Thread.sleep(readTimer());
		} 
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TucsonOperationNotPossibleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnreachableNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationTimeOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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


    public void setPanel(ViewManageAgentPanel maPanel)
    {
    	panel=new ViewSmartPredictorAgentPanel(maPanel, this, tp);
    }

	@Override
	public String getName() {
		int n = 2;
		if(stanza.getName().equals("sala_tc")) n = 1;
		return "Smart Conditionair Agent " + n;
	}

}
