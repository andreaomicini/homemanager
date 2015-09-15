package it.unibo.homemanager.meteo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import it.unibo.homemanager.tablemap.User;
import it.unibo.homemanager.userinterfaces.TracePanel;
import it.unibo.homemanager.userinterfaces.ViewManageAgentPanel;
import it.unibo.homemanager.userinterfaces.ViewMeteoAgentPanel;
import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.Tuple;
import alice.tuplecentre.api.exceptions.InvalidTupleException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import alice.tuples.javatuples.api.IJTupleTemplate;
import alice.tuples.javatuples.exceptions.InvalidJValException;
import alice.tuples.javatuples.impl.JTupleTemplate;
import alice.tuples.javatuples.impl.JVal;

public class MeteoAgent extends AbstractTucsonAgent implements TucsonAgentInterface {

	 	private TucsonTupleCentreId myTid;
	    private TracePanel tp;
	    private EnhancedSynchACC acc;
	    private ViewMeteoAgentPanel panel;
	    
	    /**
	     * Creates a new instance of MeteoAgent
	     */
	    public MeteoAgent(String id,TracePanel tp, TucsonTupleCentreId tuple) throws TucsonInvalidAgentIdException {
	        super(id);
	        this.tp=tp;
	        this.myTid=tuple;
	       
	    }
	    
	    public void setPanel(ViewManageAgentPanel maPanel)
	    {
	    	panel=new ViewMeteoAgentPanel(maPanel, this, tp);
	    }
	    
	    @Override
	    public void operationCompleted(ITucsonOperation ito) {
	        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	    }
	    
	    @Override
	    public void operationCompleted(AbstractTupleCentreOperation atco) {
	        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	    }

	    public List<String> getSiti()
	    {
	    	List<String> siti=new ArrayList<String>();
	    	siti.add("Google");
	        siti.add("Yahoo");
	        siti.add("OpenWeatherMap");
	        return siti;
	    }
	    
		@Override
		protected void main() {
			tp.appendText("-- METEO AGENT STARTED.");
	        this.acc = this.getContext();
	        LogicTuple t1a,t1b,t1c,t2a,t2b,t2c,t3a,t3b,t3c;
	        Meteo m1,m2,m3;
	        LogicTuple template;
	        try{
	            while(true) {
	            	
	            	ParserMeteo.init();		//inizializza la posizione per i parser
	            	//Thread.sleep(5000);
	            	m1=ParserMeteo.previsioniM();	//weathermap
	            	m2=ParserMeteo.previsioniY();	//yahoo
	            	m3=ParserMeteo.previsioniG();	//google
	            	
	            	
	            	
	            	//creo tutte le tuple---> 3 tuple per ogni sito meteo
	            	t1a=new LogicTuple("meteo",new Value(m1.getNomeSito()),new Value(m1.getCitta()),new Value(m1.getOraAlba()),new Value(m1.getMinutoAlba()),new Value(m1.getOraTramonto()),new Value(m1.getMinutoTramonto()),new Value(m1.getTemperatura()));
	            	t1b=new LogicTuple("meteo",new Value(m1.getNomeSito()) ,new Value(m1.getTemperaturaMax()),new Value(m1.getTemperaturaMin()),new Value(m1.getUmidita()),new Value(m1.getPressione()),new Value(m1.getVento()));
	            	t1c=new LogicTuple("meteo",new Value(m1.getNomeSito()) ,new Value(m1.getPrecipitazioni()), new Value(m1.getMeteoCode()),new Value(m1.getMeteo()));
	            	
	            	
	            	t2a=new LogicTuple("meteo",new Value(m2.getNomeSito()),new Value(m2.getCitta()),new Value(m2.getOraAlba()),new Value(m2.getMinutoAlba()),new Value(m2.getOraTramonto()),new Value(m2.getMinutoTramonto()),new Value(m2.getTemperatura()));
	            	t2b=new LogicTuple("meteo",new Value(m2.getNomeSito()) ,new Value(m2.getTemperaturaMax()),new Value(m2.getTemperaturaMin()),new Value(m2.getUmidita()),new Value(m2.getPressione()),new Value(m2.getVento()));
	            	t2c=new LogicTuple("meteo",new Value(m2.getNomeSito()),new Value(m2.getPrecipitazioni()), new Value(m2.getMeteoCode()),new Value(m2.getMeteo()));
	            	
	            	
	            	t3a=new LogicTuple("meteo",new Value(m3.getNomeSito()),new Value(m3.getCitta()),new Value(m3.getOraAlba()),new Value(m3.getMinutoAlba()),new Value(m3.getOraTramonto()),new Value(m3.getMinutoTramonto()),new Value(m3.getTemperatura()));
	            	t3b=new LogicTuple("meteo",new Value(m3.getNomeSito()) ,new Value(m3.getTemperaturaMax()),new Value(m3.getTemperaturaMin()),new Value(m3.getUmidita()),new Value(m3.getPressione()),new Value(m3.getVento()));
	            	t3c=new LogicTuple("meteo",new Value(m3.getNomeSito()),new Value(m3.getPrecipitazioni()), new Value(m3.getMeteoCode()),new Value(m3.getMeteo()));
	            	
	            	
	            	//blocco vers1.0 di eliminazione tuple vecchie
	            	/*{
	            	template=new LogicTuple("meteo", new Var(), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
	            	acc.inp(myTid, template, Long.MAX_VALUE); 	//perche MAX_VALUE?
	            	template=new LogicTuple("meteo", new Var(), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
	            	acc.inp(myTid, template, Long.MAX_VALUE);
	            	template=new LogicTuple("meteo", new Var(), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
	            	acc.inp(myTid, template, Long.MAX_VALUE);
	            	template=new LogicTuple("meteo", new Var(), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
	            	acc.inp(myTid, template, Long.MAX_VALUE); 	//perche MAX_VALUE?
	            	template=new LogicTuple("meteo", new Var(), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
	            	acc.inp(myTid, template, Long.MAX_VALUE);
	            	template=new LogicTuple("meteo", new Var(), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
	            	acc.inp(myTid, template, Long.MAX_VALUE);
	            	template=new LogicTuple("meteo", new Var(), new Var(), new Var());
	            	acc.inp(myTid, template, Long.MAX_VALUE); 	//perche MAX_VALUE?
	            	template=new LogicTuple("meteo", new Var(), new Var(), new Var());
	            	acc.inp(myTid, template, Long.MAX_VALUE);
	            	template=new LogicTuple("meteo", new Var(), new Var(), new Var());
	            	acc.inp(myTid, template, Long.MAX_VALUE);
	            	}*/
	            	
	            	
	            	
	            	//inserisco le tuple nuove
	            	acc.out(this.myTid,t1a, null); 
	            	acc.out(this.myTid,t1b, null);
	            	acc.out(this.myTid,t1c, null);
	            	
	            	acc.out(this.myTid,t2a, null); 
	            	acc.out(this.myTid,t2b, null);
	            	acc.out(this.myTid,t2c, null);
	            	
	            	acc.out(this.myTid,t3a, null); 
	            	acc.out(this.myTid,t3b, null);
	            	acc.out(this.myTid,t3c, null);
	            	
	            	
	            	tp.appendText("-- METEO AGENT UPDATED.");
	            	Thread.sleep(900000);  //imposto che le previsioni si aggiornano ogni 15 minuti(=900000 millisec)          [ 8 ore(=28800000 millisec)]
	            	
	            	//elimino le tuple precedenti nel centro di tuple per poi salvare quelle piu aggiornate
	            	//vers2.0 però devo metterla dopo l aggiunta perchè senno la inAll è bloccante fino a quando non trova tuple
	            	
	            	reset();
	            	
	            }
	            //acc.exit();
	          }catch(TucsonOperationNotPossibleException ex){
	              ex.printStackTrace();
	              tp.appendText("MeteoAgent : error in activity.");
	          }
	          catch(UnreachableNodeException ex){
	              ex.printStackTrace();
	              tp.appendText("MeteoAgent : error in activity.");
	          }
	          catch(OperationTimeOutException ex){
	              ex.printStackTrace();
	              tp.appendText("MeteoAgent : error in activity.");
	          }
	          catch(Exception ex)
	          {
	        	  ex.printStackTrace();
	              tp.appendText("MeteoAgent : error in activity.");
	          }
		}
		
		private void reset() throws OperationTimeOutException,UnreachableNodeException,TucsonOperationNotPossibleException
		{
			LogicTuple template=new LogicTuple("meteo", new Var(), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
        	acc.inAll(myTid, template, Long.MAX_VALUE);
        	template=new LogicTuple("meteo", new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
        	acc.inAll(myTid, template, Long.MAX_VALUE);
        	template=new LogicTuple("meteo",new Var(), new Var(), new Var(), new Var());
        	acc.inAll(myTid, template, Long.MAX_VALUE);
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
			return "Weather Agent";
		}

		public String[] getColumnNames(String site) {
			
			switch (site) {
			case "OpenWeatherMap":
				String[] uscita1={"Sito",
                        "Città",
                        "Ora alba",
                        "Minut alba",
                        "Ora tramonto",
                       "Minut tramonto",
                        "Temperatura",
                        "Temp max",
                        "Temp min",
                        "Umidità %",
                        "Pressione",
                        "Vento",
                        "Precipitazioni",
                        "Code meteo",
                        "Meteo",
                        };
				return uscita1;
				
			case "Yahoo":
				String[] uscita2={"Sito",
                        "Città",
                        "Ora alba",
                        "Minut alba",
                        "Ora tramonto",
                       "Minut tramonto",
                        "Temperatura",
                        "Temp max",
                        "Temp min",
                        "Umidità %",
                        "Pressione",
                        "Vento",
                        //"Precipitazioni",
                        "Code meteo",
                        "Meteo",
                        };
				return uscita2;
				
			case "Google":
				String[] uscita3={"Sito",
                        "Città",
                        //"Ora alba",
                        //"Minut alba",
                        //"Ora tramonto",
                       //"Minut tramonto",
                        "Temperatura",
                        //"Temp max",
                        //"Temp min",
                        "Umidità %",
                        "Pressione",
                        "Vento",
                        "Precipitazioni",
                        //"Code meteo",
                        "Meteo",
                        };
				return uscita3;
				
			default:
				return null;
			}
			
		}
		//GETDATA TOTALE PER AVERE TUTTO IN UNA SOLA MATRICE. not used
		public Object[][] getData(String site)  {
			Object[][] data=null;
			switch (site) {
			case "OpenWeatherMap":
				try{
				LogicTuple t1=new LogicTuple("meteo", new Value("openweathermap"), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
				LogicTuple t2=new LogicTuple("meteo", new Value("openweathermap"), new Var(), new Var(), new Var(), new Var(), new Var());
				LogicTuple t3=new LogicTuple("meteo", new Value("openweathermap"), new Var(), new Var(), new Var());
				ITucsonOperation op1=acc.rdp(myTid, t1, Long.MAX_VALUE); 	
				ITucsonOperation op2=acc.rdp(myTid, t2, Long.MAX_VALUE);
				ITucsonOperation op3=acc.rdp(myTid, t3, Long.MAX_VALUE);
				
				if(op1.isResultSuccess() && op2.isResultSuccess()&& op3.isResultSuccess())
				{
					data= new Object[1][15];
					data[0][0]=t1.getArg(0); //sito
					data[0][1]=t1.getArg(1); //citta
					data[0][2]=t1.getArg(2); //ora alba
					data[0][3]=t1.getArg(3); //min alba
					data[0][4]=t1.getArg(4); //ora tram
					data[0][5]=t1.getArg(5); //min tram
					data[0][6]=t1.getArg(6); //temp
					
					data[0][7]=t2.getArg(1); //temp max
					data[0][8]=t2.getArg(2); //temp min
					data[0][9]=t2.getArg(3); //umidita
					data[0][10]=t2.getArg(4); //pressione
					data[0][11]=t2.getArg(5); //vento
					
					data[0][12]=t3.getArg(1); //precip
					data[0][13]=t3.getArg(2); //code meteo
					data[0][14]=t3.getArg(3); //meteo
				}
				else
				{
					tp.appendText("MeteoAgent : error in activity.");
				}
				}
				catch(TucsonOperationNotPossibleException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(UnreachableNodeException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(OperationTimeOutException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(Exception ex)
		          {
		        	  ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
			
				return data;
				
			case "Yahoo":
				
				try{
					LogicTuple t1=new LogicTuple("meteo", new Value("yahoo"), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
					LogicTuple t2=new LogicTuple("meteo", new Value("yahoo"), new Var(), new Var(), new Var(), new Var(), new Var());
					LogicTuple t3=new LogicTuple("meteo", new Value("yahoo"), new Var(), new Var(), new Var());
					ITucsonOperation op1=acc.rdp(myTid, t1, Long.MAX_VALUE); 	
					ITucsonOperation op2=acc.rdp(myTid, t2, Long.MAX_VALUE);
					ITucsonOperation op3=acc.rdp(myTid, t3, Long.MAX_VALUE);
					
					if(op1.isResultSuccess() && op2.isResultSuccess()&& op3.isResultSuccess())
					{
						data= new Object[1][14];
						data[0][0]=t1.getArg(0); //sito
						data[0][1]=t1.getArg(1); //citta
						data[0][2]=t1.getArg(2); //ora alba
						data[0][3]=t1.getArg(3); //min alba
						data[0][4]=t1.getArg(4); //ora tram
						data[0][5]=t1.getArg(5); //min tram
						data[0][6]=t1.getArg(6); //temp
						
						data[0][7]=t2.getArg(1); //temp max
						data[0][8]=t2.getArg(2); //temp min
						data[0][9]=t2.getArg(3); //umidita
						data[0][10]=t2.getArg(4); //pressione
						data[0][11]=t2.getArg(5); //vento
						
						//data[0][12]=t3.getArg(1); //precip
						data[0][12]=t3.getArg(2); //code meteo
						data[0][13]=t3.getArg(3); //meteo
					}
					else
					{
						tp.appendText("MeteoAgent : error in activity.");
					}
					}
				catch(TucsonOperationNotPossibleException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(UnreachableNodeException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(OperationTimeOutException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(Exception ex)
		          {
		        	  ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
				
					return data;
				
			case "Google":
				
				try{
					LogicTuple t1=new LogicTuple("meteo", new Value("google"), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
					LogicTuple t2=new LogicTuple("meteo", new Value("google"), new Var(), new Var(), new Var(), new Var(), new Var());
					LogicTuple t3=new LogicTuple("meteo", new Value("google"), new Var(), new Var(), new Var());
					ITucsonOperation op1=acc.rdp(myTid, t1, Long.MAX_VALUE); 	
					ITucsonOperation op2=acc.rdp(myTid, t2, Long.MAX_VALUE);
					ITucsonOperation op3=acc.rdp(myTid, t3, Long.MAX_VALUE);
					
					if(op1.isResultSuccess() && op2.isResultSuccess()&& op3.isResultSuccess())
					{
						data= new Object[1][8];
						data[0][0]=t1.getArg(0); //sito
						data[0][1]=t1.getArg(1); //citta
						//data[0][2]=t1.getArg(2); //ora alba
						//data[0][3]=t1.getArg(3); //min alba
						//data[0][4]=t1.getArg(4); //ora tram
						//data[0][5]=t1.getArg(5); //min tram
						data[0][2]=t1.getArg(6); //temp
						
						//data[0][7]=t2.getArg(1); //temp max
						//data[0][8]=t2.getArg(2); //temp min
						data[0][3]=t2.getArg(3); //umidita
						data[0][4]=t2.getArg(4); //pressione
						data[0][5]=t2.getArg(5); //vento
						
						data[0][6]=t3.getArg(1); //precip
						//data[0][13]=t3.getArg(2); //code meteo
						data[0][7]=t3.getArg(3); //meteo
					}
					else
					{
						tp.appendText("MeteoAgent : error in activity.");
					}
					}
				catch(TucsonOperationNotPossibleException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(UnreachableNodeException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(OperationTimeOutException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(Exception ex)
		          {
		        	  ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
				
					return data;
				
			default:
				return null;
			}
		}

		public Object[][] getData1(String site) { // primi 6
			Object[][] data=null;
			switch (site) {
			case "OpenWeatherMap":
				try{
				LogicTuple t1=new LogicTuple("meteo", new Value("openweathermap"), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
				//LogicTuple t2=new LogicTuple("meteo", new Value("openweathermap"), new Var(), new Var(), new Var(), new Var(), new Var());
				//LogicTuple t3=new LogicTuple("meteo", new Value("openweathermap"), new Var(), new Var(), new Var());
				ITucsonOperation op1=acc.rdp(myTid, t1, Long.MAX_VALUE); 	
				
				
				if(op1.isResultSuccess())
				{
					data= new Object[1][6];
					data[0][0]=t1.getArg(0); //sito
					data[0][1]=t1.getArg(1); //citta
					data[0][2]=t1.getArg(2); //ora alba
					data[0][3]=t1.getArg(3); //min alba
					data[0][4]=t1.getArg(4); //ora tram
					data[0][5]=t1.getArg(5); //min tram
					
				}
				else
				{
					tp.appendText("MeteoAgent : error in activity.");
				}
				}
				catch(TucsonOperationNotPossibleException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(UnreachableNodeException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(OperationTimeOutException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(Exception ex)
		          {
		        	  ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
			
				return data;
				
			case "Yahoo":
				
				try{
					LogicTuple t1=new LogicTuple("meteo", new Value("yahoo"), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
					//LogicTuple t2=new LogicTuple("meteo", new Value("openweathermap"), new Var(), new Var(), new Var(), new Var(), new Var());
					//LogicTuple t3=new LogicTuple("meteo", new Value("openweathermap"), new Var(), new Var(), new Var());
					ITucsonOperation op1=acc.rdp(myTid, t1, Long.MAX_VALUE); 	
					
					
					if(op1.isResultSuccess())
					{
						data= new Object[1][6];
						data[0][0]=t1.getArg(0); //sito
						data[0][1]=t1.getArg(1); //citta
						data[0][2]=t1.getArg(2); //ora alba
						data[0][3]=t1.getArg(3); //min alba
						data[0][4]=t1.getArg(4); //ora tram
						data[0][5]=t1.getArg(5); //min tram
						
					}
					else
					{
						tp.appendText("MeteoAgent : error in activity.");
					}
					}
				catch(TucsonOperationNotPossibleException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(UnreachableNodeException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(OperationTimeOutException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(Exception ex)
		          {
		        	  ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
				
					return data;
				
			case "Google":
				
				try{
					LogicTuple t1=new LogicTuple("meteo", new Value("google"), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
					LogicTuple t2=new LogicTuple("meteo", new Value("google"), new Var(), new Var(), new Var(), new Var(), new Var());
					//LogicTuple t3=new LogicTuple("meteo", new Value("google"), new Var(), new Var(), new Var());
					ITucsonOperation op1=acc.rdp(myTid, t1, Long.MAX_VALUE); 	
					ITucsonOperation op2=acc.rdp(myTid, t2, Long.MAX_VALUE);
					
					
					if(op1.isResultSuccess() && op2.isResultSuccess())
					{
						data= new Object[1][6];
						data[0][0]=t1.getArg(0); //sito
						data[0][1]=t1.getArg(1); //citta
						//data[0][2]=t1.getArg(2); //ora alba
						//data[0][3]=t1.getArg(3); //min alba
						//data[0][4]=t1.getArg(4); //ora tram
						//data[0][5]=t1.getArg(5); //min tram
						data[0][2]=t1.getArg(6); //temp
						
						//data[0][7]=t2.getArg(1); //temp max
						//data[0][8]=t2.getArg(2); //temp min
						data[0][3]=t2.getArg(3); //umidita
						data[0][4]=t2.getArg(4); //pressione
						data[0][5]=t2.getArg(5); //vento
						
						
					}
					else
					{
						tp.appendText("MeteoAgent : error in activity.");
					}
					}
				catch(TucsonOperationNotPossibleException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(UnreachableNodeException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(OperationTimeOutException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(Exception ex)
		          {
		        	  ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
				
					return data;
				
			default:
				return null;
			}
		}

		public Object[][] getData2(String site) { //secondi 6
			Object[][] data=null;
			switch (site) {
			case "OpenWeatherMap":
				try{
				LogicTuple t1=new LogicTuple("meteo", new Value("openweathermap"), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
				LogicTuple t2=new LogicTuple("meteo", new Value("openweathermap"), new Var(), new Var(), new Var(), new Var(), new Var());
				//LogicTuple t3=new LogicTuple("meteo", new Value("openweathermap"), new Var(), new Var(), new Var());
				ITucsonOperation op1=acc.rdp(myTid, t1, Long.MAX_VALUE); 	
				ITucsonOperation op2=acc.rdp(myTid, t2, Long.MAX_VALUE);
				
				
				if(op1.isResultSuccess() && op2.isResultSuccess())
				{
					data= new Object[1][6];
					
					data[0][0]=t1.getArg(6); //temp
					
					data[0][1]=t2.getArg(1); //temp max
					data[0][2]=t2.getArg(2); //temp min
					data[0][3]=t2.getArg(3); //umidita
					data[0][4]=t2.getArg(4); //pressione
					data[0][5]=t2.getArg(5); //vento
					
					
				}
				else
				{
					tp.appendText("MeteoAgent : error in activity.");
				}
				}
				catch(TucsonOperationNotPossibleException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(UnreachableNodeException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(OperationTimeOutException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(Exception ex)
		          {
		        	  ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
			
				return data;
				
			case "Yahoo":
				
				try{
					LogicTuple t1=new LogicTuple("meteo", new Value("yahoo"), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
					LogicTuple t2=new LogicTuple("meteo", new Value("yahoo"), new Var(), new Var(), new Var(), new Var(), new Var());
					//LogicTuple t3=new LogicTuple("meteo", new Value("openweathermap"), new Var(), new Var(), new Var());
					ITucsonOperation op1=acc.rdp(myTid, t1, Long.MAX_VALUE); 	
					ITucsonOperation op2=acc.rdp(myTid, t2, Long.MAX_VALUE);
					
					
					if(op1.isResultSuccess() && op2.isResultSuccess())
					{
						data= new Object[1][6];
						
						data[0][0]=t1.getArg(6); //temp
						
						data[0][1]=t2.getArg(1); //temp max
						data[0][2]=t2.getArg(2); //temp min
						data[0][3]=t2.getArg(3); //umidita
						data[0][4]=t2.getArg(4); //pressione
						data[0][5]=t2.getArg(5); //vento
						
						
					}
					else
					{
						tp.appendText("MeteoAgent : error in activity.");
					}
					}
				catch(TucsonOperationNotPossibleException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(UnreachableNodeException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(OperationTimeOutException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(Exception ex)
		          {
		        	  ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
				
					return data;
				
			case "Google":
				
				try{
					//LogicTuple t1=new LogicTuple("meteo", new Value("google"), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
					//LogicTuple t2=new LogicTuple("meteo", new Value("google"), new Var(), new Var(), new Var(), new Var(), new Var());
					LogicTuple t3=new LogicTuple("meteo", new Value("google"), new Var(), new Var(), new Var());
					
					ITucsonOperation op3=acc.rdp(myTid, t3, Long.MAX_VALUE);
					
					if(op3.isResultSuccess())
					{
						data= new Object[1][2];
						
						
						data[0][0]=t3.getArg(1); //precip
						//data[0][13]=t3.getArg(2); //code meteo
						data[0][1]=t3.getArg(3); //meteo
					}
					else
					{
						tp.appendText("MeteoAgent : error in activity.");
					}
					}
				catch(TucsonOperationNotPossibleException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(UnreachableNodeException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(OperationTimeOutException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(Exception ex)
		          {
		        	  ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
				
					return data;
				
			default:
				return null;
			}
		}

		public Object[][] getData3(String site) { //terzi 6
			Object[][] data=null;
			switch (site) {
			case "OpenWeatherMap":
				try{
				//LogicTuple t1=new LogicTuple("meteo", new Value("openweathermap"), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
				//LogicTuple t2=new LogicTuple("meteo", new Value("openweathermap"), new Var(), new Var(), new Var(), new Var(), new Var());
				LogicTuple t3=new LogicTuple("meteo", new Value("openweathermap"), new Var(), new Var(), new Var());
			
				ITucsonOperation op3=acc.rdp(myTid, t3, Long.MAX_VALUE);
				
				if(op3.isResultSuccess())
				{
					data= new Object[1][3];
					
					
					data[0][0]=t3.getArg(1); //precip
					data[0][1]=t3.getArg(2); //code meteo
					data[0][2]=t3.getArg(3); //meteo
				}
				else
				{
					tp.appendText("MeteoAgent : error in activity.");
				}
				}
				catch(TucsonOperationNotPossibleException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(UnreachableNodeException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(OperationTimeOutException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(Exception ex)
		          {
		        	  ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
			
				return data;
				
			case "Yahoo":
				
				try{
					//LogicTuple t1=new LogicTuple("meteo", new Value("yahoo"), new Var(), new Var(), new Var(), new Var(), new Var(), new Var());
					//LogicTuple t2=new LogicTuple("meteo", new Value("yahoo"), new Var(), new Var(), new Var(), new Var(), new Var());
					LogicTuple t3=new LogicTuple("meteo", new Value("yahoo"), new Var(), new Var(), new Var());
					
					ITucsonOperation op3=acc.rdp(myTid, t3, Long.MAX_VALUE);
					
					if(op3.isResultSuccess())
					{
						data= new Object[1][2];
						
						
						//data[0][12]=t3.getArg(1); //precip
						data[0][0]=t3.getArg(2); //code meteo
						data[0][1]=t3.getArg(3); //meteo
					}
					else
					{
						tp.appendText("MeteoAgent : error in activity.");
					}
					}
				catch(TucsonOperationNotPossibleException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(UnreachableNodeException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(OperationTimeOutException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(Exception ex)
		          {
		        	  ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
				
					return data;
				
			case "Google":
			
					return null;
				
			default:
				return null;
			}
		}

		
		//in questo modo è piu estendibile
		public int getIndex(String site) {
			switch (site) {
			case "Yahoo":
				return 12;
				
			case "Google":
				
				return 8;

			case "OpenWeatherMap":
				
				return 12;

			default:
				return -1;
			}
		}

		//DA TESTARE
		
		public String[] getShuttersActions() {
			
			 LogicTuple t3=new LogicTuple("shutters_action", new Value("up"), new Var(), new Var());
			 LogicTuple t4=new LogicTuple("shutters_action", new Value("down"), new Var(), new Var());
			 try{
				ITucsonOperation op3=acc.rdp(myTid, t3, Long.MAX_VALUE);
				ITucsonOperation op4=acc.rdp(myTid, t4, Long.MAX_VALUE);
				
				if(op3.isResultSuccess() && op4.isResultSuccess())
				{
					String one=""+t3.getArg(1)+" "+t3.getArg(2)+":"+t3.getArg(3);
					String two=""+t4.getArg(1)+" "+t4.getArg(2)+":"+t4.getArg(3);
					String[] ar = {one, two};
					return ar;
				}
				else
				{
					tp.appendText("MeteoAgent : error in activity.");
					return null;
				}
				}
				catch(TucsonOperationNotPossibleException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(UnreachableNodeException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(OperationTimeOutException ex){
		              ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
		          catch(Exception ex)
		          {
		        	  ex.printStackTrace();
		              tp.appendText("MeteoAgent : error in activity.");
		          }
			 return null;
	
		}
}
