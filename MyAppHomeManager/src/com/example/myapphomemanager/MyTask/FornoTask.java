package com.example.myapphomemanager.MyTask;


import com.example.myapphomemanager.Costanti;
import com.example.myapphomemanager.Forno;


import com.examples.model.Conversion;
import com.examples.model.DeviceData;
import com.examples.model.Utente;



import alice.logictuple.LogicTuple;
import alice.logictuple.Value;
import alice.logictuple.Var;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;

import alice.tucson.api.TucsonTupleCentreId;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


public class FornoTask extends AsyncTask<String, Void, DeviceData> {

	TucsonTupleCentreId tc,device_room_tc;
	TucsonAgentId agent, fornoAgent;
	Activity act;
	EnhancedSynchACC acc, acc2;
	String operazione;
	Utente u;
	
	public FornoTask(TucsonTupleCentreId tc, TucsonAgentId ag, Activity a, TucsonTupleCentreId tc2, TucsonAgentId ag2, Utente u)
	{
		this.tc=tc;
		this.agent= ag;
		this.act= a;
		this.device_room_tc = tc2;
		this.fornoAgent = ag2;
		this.acc= TucsonMetaACC.getContext(agent,Costanti.getIndirizzo(),20504);
		this.acc2 = TucsonMetaACC.getContext(fornoAgent,Costanti.getIndirizzo(),20504);
		Log.i("Acc: ", this.acc.toString());
		this.u = u;

	}
	
	
	
	
	@Override
	protected DeviceData doInBackground(String... params) {
		
		
		DeviceData ris=null;

		operazione= params[0];

		if(operazione!=null)
		{
		   //caso in cui si debba leggere lo stato del dispositivo
			if(operazione.equalsIgnoreCase("read"))
			 {
				try{
					final LogicTuple arr_dev= LogicTuple.parse("device(5,"+ new Var("X")+","+ new Var("W")+ "," + new Var("Y")+ ","+ new Var("Z")+ ","+ new Var("A")+")");
					
					Log.i("Arr = ", arr_dev.toString());
					final ITucsonOperation op = this.acc.rdp(this.tc, arr_dev,
							Long.MAX_VALUE);

					if (op.isResultSuccess()) {
						Log.i("Risulato = ", "Bene");

						/*leggo i dati che riguardano lo stato attuale del dispositivo per mostrarli*/

						final LogicTuple arr_dev_state = LogicTuple.parse("dev_curr_st(5," + new Var("X")+ ")");
						Log.i("Arr2 = ", arr_dev_state.toString());
						final ITucsonOperation op2 = this.acc2.rdp(this.device_room_tc, arr_dev_state, Long.MAX_VALUE);

						if (op2.isResultSuccess()) {
							Log.i("Risulato 2 = ", "Bene");

							final LogicTuple arr_dev_temp = LogicTuple.parse("temp_curr_dev(5," + new Var("X")+ ")");
							Log.i("Arr3 = ", arr_dev_temp.toString());
							final ITucsonOperation op3 = this.acc2.rdp(this.device_room_tc, arr_dev_temp, Long.MAX_VALUE);

							if (op3.isResultSuccess()) {
								Log.i("Risulato 3 = ", "Bene");
							
							

							ris=Conversion.createDeviceByString(op.getLogicTupleResult().toString(), op2.getLogicTupleResult().toString(),op3.getLogicTupleResult().toString());


						     }
						else {Log.i("Risultato 3 = ", "Male");
						}
						}
						else {Log.i("Risultato 2 = ", "Male");}


					} else {
						Log.i("Risultato = ", "Male");
					}
					
					
			     

					this.acc.exit();
					this.acc2.exit();

				} catch (final Exception e) {
					// TODO Auto-generated catch block
					Log.i("Creazione errore", "Non è stato possibile continuare");
				}
			 }
			
			
			
			//caso in cui debba salvare nuove impostazioni
			else if(operazione.equalsIgnoreCase("save"))
			{

				try{
					//recupero alias del dispositivo
                   final LogicTuple arr_dev= LogicTuple.parse("device(5,"+ new Var("X")+","+ new Var("W")+ "," + new Var("Y")+ ","+ new Var("Z")+ ","+ new Var("A")+")");
					
					Log.i("Arr = ", arr_dev.toString());
					final ITucsonOperation op = this.acc.rdp(this.tc, arr_dev,
							Long.MAX_VALUE);

					if (op.isResultSuccess()) {
						Log.i("Risulato = ", "Bene");

							//recupero i due parametri passati
							String stato= params[1];
							String temp= params[2];
                            

							if((stato!=null) &&(temp!=null)){
								//devo valvare come intero la temperatura per evitare gli apici
								int t;
								if (stato.equalsIgnoreCase("off")) t=0;
								    else t= Integer.parseInt(temp);
								Value vst= new Value(stato);
			                    Value idu = new Value(u.getIdUser());
								Value tst= new Value(t);
								final LogicTuple arr_update = LogicTuple.parse("send_cmd("+idu+",5,5,"+vst +")");
								//metto in output  il nuovo stato

								final ITucsonOperation op3 = this.acc2.out(this.device_room_tc, arr_update,
										Long.MAX_VALUE);

								if (op3.isResultSuccess()) {
									Log.i("Risulato scritto nuovo stato= ", "Bene");
									//se tutto è andato a buon fine posso creare l'oggetto riscaldamento con
									//i parametri letti
									
									//genera la tupla per creare reazione della temperatura
									 LogicTuple arr_dev_temp = LogicTuple.parse("upd_curr_temp_dev(5," + tst +")");
									Log.i("Arr3 = ", arr_dev_temp.toString());
									final ITucsonOperation op5 = this.acc2.out(this.device_room_tc, arr_dev_temp, Long.MAX_VALUE);
									
									
									
									final LogicTuple arr = LogicTuple.parse("dev_curr_st(5," + new Var("X")+ ")");
									Log.i("Arr lettura update = ", arr.toString());
									final ITucsonOperation op4 = this.acc2.rdp(this.device_room_tc, arr, Long.MAX_VALUE);
									if(op4.isResultSuccess())
									{
										arr_dev_temp = LogicTuple.parse("temp_curr_dev(5," + new Var("X")+ ")");
										Log.i("Arr3 = ", arr_dev_temp.toString());
										final ITucsonOperation op6 = this.acc2.rdp(this.device_room_tc, arr_dev_temp, Long.MAX_VALUE);

										if (op6.isResultSuccess()) {
											Log.i("Risulato 5 = ", "Bene");
									ris=Conversion.createDeviceByString(op.getLogicTupleResult().toString(), op4.getLogicTupleResult().toString(), op6.getLogicTupleResult().toString());

									}
									} 
							}
						}
					
					}

					//fondamentale: apro e chiudo così posso rietrare tutte le volte
					this.acc.exit();
					this.acc2.exit();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return ris;
	}
	
	

	@Override
	protected void onPostExecute(DeviceData ris)
	{
		super.onPostExecute(ris);

		if((ris!=null)&&(operazione.equalsIgnoreCase("read")))
		{
			Intent inte= new Intent(act,Forno.class);

			inte.putExtra("FornoObj", ris);

			inte.putExtra("CentroTuple", tc); 
			inte.putExtra("Agente", agent);
			inte.putExtra("CentroTuple_room", device_room_tc);
			inte.putExtra("Agente_room", fornoAgent);
			inte.putExtra("Utente", u);
			
			act.startActivity(inte);
		}

	}
}
	
