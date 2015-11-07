package com.examples.model;

import java.util.StringTokenizer;

public class Conversion {
	
	 public static String getDatabaseString(String inputString) {

		    if (inputString == null)
		      return "-";

		    /* inputString = inputString.replace('"', '\''); */

		    StringBuffer temp = new StringBuffer();
		    int indexFrom = 0;
		    int indexTo = 0;
		    indexTo = inputString.indexOf("'", indexFrom);
		    while (indexTo >= 0) {
		      temp.append(inputString.substring(indexFrom, indexTo));
		      temp.append("`");
		      indexFrom = indexTo;
		      indexTo = inputString.indexOf("'", ++indexFrom);
		    }
		    temp = temp.append(inputString.substring(indexFrom, inputString.length()));

		    for (int i=temp.length()-1; i>=0; i--) {
		      if (temp.charAt(i) == '"') temp.deleteCharAt(i);
		    }

		    return temp.toString();

		  }
	 
	 
	 
	public static DeviceData createDeviceByString(String sdev,
				String sstate, String stemp) {
			String s1=sdev.substring(sdev.indexOf("(")+1,sdev.length()-1);
			StringTokenizer t= new StringTokenizer(s1,",");
			DeviceData dev=null;
			if(t.countTokens()==6) {
			    int idDev= Integer.parseInt(t.nextToken());
			    String name= Conversion.getDatabaseString(t.nextToken());
			    float energy= Float.parseFloat(t.nextToken());
			    String param= Conversion.getDatabaseString(t.nextToken());
			    String type= Conversion.getDatabaseString((t.nextToken()));
			    int roomId=Integer.parseInt(t.nextToken());
			    dev= new DeviceData(idDev,name,energy,param,type,roomId);
			    
			    String s2=sstate.substring(sstate.indexOf("(")+1,sstate.length()-1);
				StringTokenizer t2= new StringTokenizer(s2,",");
				if (t2.countTokens()==2)
				   {
					t2.nextToken();
					boolean state_curr = false;
					int temp_curr = -1;
					String sst = t2.nextToken();
					if(sst.equals("on"))
					   {
						 state_curr = true;
						 String s3 =stemp.substring(stemp.indexOf("(")+1,stemp.length()-1);
						 StringTokenizer t3= new StringTokenizer(s3,",");
							if (t3.countTokens()==2)
							   {
								 t3.nextToken();
								 temp_curr = Integer.parseInt(t3.nextToken());
								 dev.setStato_attuale(state_curr);
								 dev.setTemp(temp_curr);
							   }
					   }
					
				   }
			}
			
			return dev;
		}

}
