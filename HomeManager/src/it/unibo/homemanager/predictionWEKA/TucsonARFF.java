package it.unibo.homemanager.predictionWEKA;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import alice.logictuple.LogicTuple;
import alice.logictuple.Var;
import alice.respect.api.TupleCentreId;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;

public class TucsonARFF {
	
	private TucsonTupleCentreId tc;
	private EnhancedSynchACC acc;
	private String file_name;
	
	
	public TucsonARFF(String file, TucsonTupleCentreId myTid, EnhancedSynchACC acc)
	  {
		 file_name = file;
		 tc = myTid;
		 this.acc = acc;
		 
		 createARFFFile();
	  }

	
	
	public String getFile_name()
	  {
		return file_name;
	  }
	
	
	//CREA IL FILE ARFF A PARTIRE DAL CENTRO DI TUPLE
  public boolean createARFFFile()
    {
  	try {
  		
			FileOutputStream fos = new FileOutputStream(file_name);
			PrintStream output = new PrintStream(fos);
			
			//inzio la scrittura del file ARFF
			output.println("@relation home_manager_human");
			output.println("@attribute 'giorno' numeric");
			output.println("@attribute 'ora' numeric");
			output.println("@attribute 'mese' numeric");
			output.println("@attribute 'class' {casa_out,casa_in}");
			output.println("@data");
			
			//aggiungo le tuple
			List<LogicTuple> fromTucson = readAllTuple();
			for(LogicTuple l : fromTucson)
			  {
				output.println(dataARFFfromTucson(l));
			  }
			
			output.close();
			fos.close();
			
			
		} catch(IOException e)
  	        {
			  e.printStackTrace();
  	        }
  	
  	return true;
    }



private String dataARFFfromTucson(LogicTuple l) {
	String risultato="";
	String tupla = l.toString();
	//solo il contenuto
	tupla = tupla.substring(tupla.indexOf('(')+1, tupla.indexOf(')')-1);
	StringTokenizer st = new StringTokenizer(tupla, ",");
	
	if(st.countTokens()==4)
	  {
		int pres = Integer.parseInt(st.nextToken());
		st.nextToken();
		st.nextToken();
		String data = st.nextToken(",'_");
		String ora = st.nextToken();
		ora = ora.replace(".", ":");
		
		//elaborazioni per ottenere quello che mi serve
		LocalDate data_finita = LocalDate.parse(data);
		LocalTime ora_finita = LocalTime.parse(ora);
		LocalDateTime ldt = LocalDateTime.of(data_finita,ora_finita);
		
		//composizione del formato per file ARFF
		risultato += ldt.getDayOfWeek().getValue() +",";
		risultato +=ldt.getHour() + ",";
		risultato += ldt.getMonth().getValue()+",";
		
		if(pres==1) risultato+= "casa_in"; 
		    else risultato+= "casa_out";
	  }
	
	return risultato;
}




private List<LogicTuple> readAllTuple()
{
	List<LogicTuple> trovati = new ArrayList<LogicTuple>();
	try {
		
    	LogicTuple template= new LogicTuple("presence",new Var("X"),new Var("Y"),new Var("Z"),new Var("J"));
	    LogicTuple ltTemplate = LogicTuple.parse(template.toString());

	    ITucsonOperation op_rdAll = acc.rdAll(tc, ltTemplate, Long.MAX_VALUE);
	    if(op_rdAll.isResultSuccess()) {
		trovati = op_rdAll.getLogicTupleListResult();
		System.out.println("[readCentreArray(V)@Presence] Successfully read " + trovati);
	    } else
		System.err.println("[readCentreArray(V)@Presence] rdAll(" + ltTemplate + ") failed!");
	    //acc.exit();
	   
	    
	} catch (Exception e) {
		
		e.printStackTrace();
	}

return trovati;
}


}