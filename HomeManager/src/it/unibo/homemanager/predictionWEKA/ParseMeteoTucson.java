package it.unibo.homemanager.predictionWEKA;
import java.util.List;
import java.util.StringTokenizer;

import it.unibo.homemanager.meteo.Meteo;



public class ParseMeteoTucson {
	
	
	public static Meteo parseMeteo(List<String> informazioniLette)
	   {
		 Meteo m = new Meteo();
		 
		 String tupla1 = informazioniLette.get(0);
		 
		String app = tupla1.substring(tupla1.indexOf('(')+1, tupla1.indexOf(')'));
		 StringTokenizer st = new StringTokenizer(app,",");
		 if(st.countTokens()==4)
		    {
			 st.nextToken();
			 m.setPrecipitazioni(Float.parseFloat(st.nextToken().trim()));
			 m.setMeteoCode(Integer.parseInt(st.nextToken()));
			 String forcast = st.nextToken();
			 if(forcast.endsWith("'"))
			            {forcast = forcast.substring(1,forcast.length()-1);}
			 else {forcast = forcast.substring(0,forcast.length());}
			 m.setMeteo(forcast);
		    }
		 
		 
		 String tupla2 = informazioniLette.get(1);
		 
		 app = tupla2.substring(tupla2.indexOf('(')+1, tupla2.indexOf(')'));
		 StringTokenizer st2 = new StringTokenizer(app,",");
		 if(st2.countTokens()==7)
		    {
			 st2.nextToken();
			 m.setCitta(st2.nextToken());
			 m.setOraAlba(Integer.parseInt(st2.nextToken().trim()));
			 m.setMinutoAlba(Integer.parseInt(st2.nextToken().trim()));
			 m.setOraTramonto(Integer.parseInt(st2.nextToken().trim()));
			 m.setMinutoTramonto(Integer.parseInt(st2.nextToken().trim()));
			 m.setTemperatura(Float.parseFloat(st2.nextToken().trim()));
		    }
		 
		 return m;
	   }

}
