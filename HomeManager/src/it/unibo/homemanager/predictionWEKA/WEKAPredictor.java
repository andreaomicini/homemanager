package it.unibo.homemanager.predictionWEKA;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import alice.tucson.api.AbstractTucsonAgent;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tuplecentre.core.AbstractTupleCentreOperation;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemoveWithValues;

public class WEKAPredictor extends AbstractTucsonAgent{

	private TucsonTupleCentreId tc;
	private EnhancedSynchACC acc;
	private TucsonARFF genera_file;
	
	private DataSource source;
	private DataSource test_source;
	private J48 classificatore;
	
	public WEKAPredictor(String id, TucsonTupleCentreId centro_presence) throws TucsonInvalidAgentIdException {
		super(id);
		tc = centro_presence;	
		
	}

	@Override
	protected void main() {
		
		acc = this.getContext();
		genera_file = new TucsonARFF("presence.arff", tc, acc);
		
		try {
			
			source = new DataSource(genera_file.getFile_name());
			test_source = new DataSource("presence_test.arff");
			createClassificator();
			System.out.println(viewDecisionalTree());
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void createClassificator() throws Exception
	   {
		 Instances dataset_datapreprocessed;
		 //applico il filtro per costruire un classificatore più accurato basato sulle stagioni
		 LocalDate data_oggi = LocalDate.now();
		 if((data_oggi.getMonth().getValue()>=6) && (data_oggi.getMonth().getValue()<=9))
		    {
			 dataset_datapreprocessed = filterSeason(Stagione.SUMMER);
		    }
		 else dataset_datapreprocessed = filterSeason(Stagione.WINTER);
		
		 //setto la classe sull'ultimo attributo
		 dataset_datapreprocessed.setClassIndex(dataset_datapreprocessed.numAttributes()-1);
		//Costruisco un classificatore
	     classificatore = new J48();
		 classificatore.buildClassifier(dataset_datapreprocessed);	 
	   }
	
	
	//effettua predizioni basandosi sul file di test
	public ArrayList<String> makeAllPretiction() throws Exception
	{
		ArrayList<String> predicted_value = new ArrayList<String>();
		Instances test = test_source.getDataSet();
		test.setClassIndex(test.numAttributes()-1);
		
		//effettuo la predizione
		for(int j = 0;j<test.numInstances();j++)
		{
		  Instance newInst = test.instance(j);				
		  double predictedValue = classificatore.classifyInstance(newInst);
	     predicted_value.add(test.classAttribute().value((int) predictedValue).toString());
	      }
		
		return predicted_value;
	
	}
	
	
	
	//dataPreprocessing - Discriminare le stagioni- operazione da fare prima 
	//della creazione del classificatore per ridurre il campo di azione e conferire
	//maggiore accuratezza
	
	//enumerativo stagioni
	private Instances filterSeason(Stagione s)
	   {
		
		//COSTRUZIONE DI UN FILTRO su attributo numeric
		try {
			Instances dataset = source.getDataSet();
			RemoveWithValues filter_num = new RemoveWithValues();			
			
			//STAGIONE ESTIVA
			if(s == Stagione.SUMMER){
		     
				//opzioni per limite inferiore Giugno
		        String[] options = new String[4];
		        options[0] = "-C"; //attributo sul quale si vuole filtrare
		        options[1]= "3"; 
		        options[2] ="-S"; //Instances with values più grande di quello imposto
		        options[3]="6"; 		        
		        
		        filter_num.setOptions(options);
				 
	            filter_num.setInputFormat(dataset); 
				Instances newData2 = Filter.useFilter(dataset, filter_num);
				  
				//applico filtro per limite superiore a Settembre
				  
				 String[] options2 = new String[5];
			     options2[0] = "-C"; 
			     options2[1]= "3"; 
			     options2[2] ="-S";                      
			     options2[3]="9"; 	
			     options2[4] ="-V"; //inversione di matching
			        
			     filter_num.setOptions(options2);
					 
		         filter_num.setInputFormat(newData2); 
				 Instances risultati_estate = Filter.useFilter(newData2, filter_num);
				 
			     return risultati_estate;
			}
			//STAGIONE INVERNALE
			else
			{
				//opzioni per limite inferiore Gennaio
		        String[] options = new String[4];
		        options[0] = "-C"; //attributo sul quale si vuole filtrare
		        options[1]= "3"; 
		        options[2] ="-S"; //Instances with values più grande di quello imposto
		        options[3]="1"; 		        
		        
		        filter_num.setOptions(options);
				 
	            filter_num.setInputFormat(dataset); 
				Instances newData2 = Filter.useFilter(dataset, filter_num);
				  
				//applico filtro per limite superiore a Maggio
				  
				 String[] options2 = new String[5];
			     options2[0] = "-C"; 
			     options2[1]= "3"; 
			     options2[2] ="-S";                      
			     options2[3]="5"; 	
			     options2[4] ="-V"; //inversione di matching
			        
			     filter_num.setOptions(options2);
					 
		         filter_num.setInputFormat(newData2); 
				 Instances risultati_inverno_1 = Filter.useFilter(newData2, filter_num);
				 
				 
				//opzioni per limite inferiore Ottobre
			        String[] options3 = new String[4];
			        options3[0] = "-C"; //attributo sul quale si vuole filtrare
			        options3[1]= "3"; 
			        options3[2] ="-S"; //Instances with values più grande di quello imposto
			        options3[3]="10"; 		        
			        
			        filter_num.setOptions(options3);
					 
		            filter_num.setInputFormat(dataset); 
					Instances newData3 = Filter.useFilter(dataset, filter_num);
					  
					//applico filtro per limite superiore Dicembre
					  
					 String[] options4 = new String[5];
				     options4[0] = "-C"; 
				     options4[1]= "3"; 
				     options4[2] ="-S";                      
				     options4[3]="5"; 	
				     options4[4] ="-V"; //inversione di matching
				        
				     filter_num.setOptions(options4);
						 
			         filter_num.setInputFormat(newData3); 
					 Instances risultati_inverno_2 = Filter.useFilter(newData3, filter_num); 
				    
					 //unisco i risultati dei due filtri
					risultati_inverno_1.addAll(risultati_inverno_2);
					
					return risultati_inverno_1;
					
					 
				
			}
				  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	   }
	
	
	public ArrayList<HomePresencePrediction> makePredictionByDay(int day) throws Exception
	 {
		
		ArrayList<String> predicted_days = new ArrayList<String>();
	    ArrayList<HomePresencePrediction> predizioni = new ArrayList<HomePresencePrediction>();
		
	    
	    Instance inst = new SparseInstance(3); 
	
		
		Attribute giorno = new Attribute("giorno",0);
		Attribute ora = new Attribute("ora",1);
		Attribute mese = new Attribute("mese", 2);
	
		
		List my_nominal_values = new ArrayList(2); 
		my_nominal_values.add("casa_in"); 
		my_nominal_values.add("casa_out");  
		
		// Create nominal attribute "class" 
		Attribute classe = new Attribute("class", my_nominal_values);
    
		
		Instances cercati = test_source.getDataSet();
		//setto la classe sull'ultimo attributo
		cercati.setClassIndex(cercati.numAttributes()-1);
		cercati.clear();
		inst.setDataset(cercati);
		//per tutte le 24 ore compongo un'istanza per predire i valori
		for(int i = 0; i<=23; i++)
		{
		inst.setValue(giorno, day); 
		inst.setValue(ora,i); 
		inst.setMissing(3);
		inst.setClassMissing();
		cercati.add(inst);
		
		HomePresencePrediction hpp = new HomePresencePrediction();
		hpp.setDay(day);
		hpp.setHour(i);
		predizioni.add(hpp);
		}
		
		
		
		//effettuo predizione
		for(int j = 0;j<cercati.numInstances();j++)
		{
		  Instance newInst = cercati.instance(j);
		  double predictedValue = classificatore.classifyInstance(newInst);
		
	      predicted_days.add(cercati.classAttribute().value((int) predictedValue).toString());
	      User_location ul = User_location.valueOf(cercati.classAttribute().value((int) predictedValue).toString());
	      predizioni.get(j).setIn_home(ul);
		}
		
		return predizioni;
		
	 }
	
	
	
	
	public String viewDecisionalTree()
	  {
		return classificatore.toString();
	  }

	@Override
	public void operationCompleted(AbstractTupleCentreOperation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void operationCompleted(ITucsonOperation arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
