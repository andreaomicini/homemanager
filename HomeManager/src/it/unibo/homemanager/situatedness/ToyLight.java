package it.unibo.homemanager.situatedness;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Label;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import alice.logictuple.LogicTuple;
import alice.logictuple.exceptions.InvalidLogicTupleException;
import alice.respect.core.TransducersManager;
import alice.respect.situatedness.AbstractProbeId;
import alice.respect.situatedness.AbstractTransducer;
import alice.respect.situatedness.ISimpleProbe;
import alice.respect.situatedness.TransducerId;
import alice.respect.situatedness.TransducerStandardInterface;
import alice.tucson.api.EnhancedSynchACC;
import alice.tucson.api.ITucsonOperation;
import alice.tucson.api.TucsonAgentId;
import alice.tucson.api.TucsonMetaACC;
import alice.tucson.api.TucsonTupleCentreId;
import alice.tucson.api.exceptions.TucsonInvalidAgentIdException;
import alice.tucson.api.exceptions.TucsonInvalidTupleCentreIdException;
import alice.tucson.api.exceptions.TucsonOperationNotPossibleException;
import alice.tucson.api.exceptions.UnreachableNodeException;
import alice.tuplecentre.api.exceptions.InvalidOperationException;
import alice.tuplecentre.api.exceptions.OperationTimeOutException;

public class ToyLight implements ISimpleProbe{
	
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "20504";
    private EnhancedSynchACC acc;
    private final AbstractProbeId pid;
    private TransducerId tid;
    private TransducerStandardInterface transducer;
    private int lamp_id;
   
    JFrame frame;
    
    private boolean light_state; 
    private JLabel luce_accesa;
    private JLabel luce_spenta;
    
    public ToyLight(final AbstractProbeId i) {
    	this.pid = i;
    	this.lamp_id = Integer.parseInt(pid.toString().replace("lamp_", ""));
        try {
            final TucsonAgentId aid = new TucsonAgentId("actuator" + i);
            this.acc = TucsonMetaACC.getContext(aid, ToyLight.DEFAULT_HOST, Integer.valueOf(ToyLight.DEFAULT_PORT));
        } catch (final TucsonInvalidAgentIdException e) {
            e.printStackTrace();
        }
        
        light_state = false;
        
        frame = new JFrame("Lamp "+this.lamp_id);
        
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        //Generazione random del colore di background 
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        
        frame.getContentPane().setBackground(randomColor);
        
        //Leggo le immagini dal disco e le metto nella label
        try {
        	File file = new File("src/it/unibo/homemanager/situatedness/spenta.png");
			BufferedImage image = ImageIO.read(file);
			ImageIcon spenta = new ImageIcon(image, "Lamp "+this.lamp_id+" off");
			file = new File("src/it/unibo/homemanager/situatedness/accesa.png");
			image = ImageIO.read(file);
			ImageIcon accesa = new ImageIcon(image, "Lamp "+this.lamp_id+" on");
			luce_spenta = new JLabel(spenta);
	        luce_accesa = new JLabel(accesa);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        frame.getContentPane().add(luce_spenta, BorderLayout.CENTER);
        
		frame.pack();
		frame.setVisible(true);
    }

    //Mostra una lampada accesa o spenta in base al boolean in ingresso
	private void setLabel(boolean state) {
		frame.getContentPane().removeAll();
		if(state) {
			frame.getContentPane().add(luce_accesa, BorderLayout.CENTER);
		} else {
			frame.getContentPane().add(luce_spenta, BorderLayout.CENTER);
		}
		
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public AbstractProbeId getIdentifier() {
		return this.pid;
	}

	@Override
	public TransducerId getTransducer() {
		return this.tid;
	}
	
	@Override
	public void setTransducer(TransducerId arg0) {
		this.tid = arg0;
	}

	@Override
	public boolean readValue(String key) {
		
		//Controlla l'associazione con il transducer
		if (this.tid == null) {
            System.err.println("[" + this.pid
                    + "]: Non è stato ancora associato nessun traduttore a questo PID.");
            return false;
        }
        if (this.transducer == null) {
            this.transducer = TransducersManager.INSTANCE
                    .getTransducer(this.tid.getAgentName());
            if (this.transducer == null) {
                System.err.println("[" + this.pid
                        + "]: Non posso collegarmi al transducer.");
                return false;
            }
        }
        
        //La key deve essere costruita correttamente in questo modo
        final String correct_light_state = "light_state_"+this.lamp_id;
        try {
        	if(key.equals(correct_light_state)) {
                final int state;
                if (this.light_state) {
                	state = 1;
                } else {
                	state = 0;
                }
	            
                this.transducer.notifyEnvEvent(key, state, AbstractTransducer.GET_MODE);
	            
	            return true;
        	} else {
	        	System.err.println("[" + this.pid + "]: Proprietà sconosciuta " + key);
	        	return false;
        	}
        	
        } catch (final TucsonOperationNotPossibleException e) {
            e.printStackTrace();
        } catch (final UnreachableNodeException e) {
            e.printStackTrace();
        } catch (final OperationTimeOutException e) {
            e.printStackTrace();
        } catch (final InvalidOperationException e) {
            e.printStackTrace();
        } 
        return false;
		
	}



	@Override
	public boolean writeValue(String key, int state) {

		//Controlla l'associazione con il transducer		
		if (this.tid == null) {
            System.err.println("[" + this.pid
                    + "]: Non è stato ancora associato nessun traduttore a questo PID.");
            return false;
        }
        if (this.transducer == null) {
            this.transducer = TransducersManager.INSTANCE
                    .getTransducer(this.tid.getAgentName());
            if (this.transducer == null) {
                System.err.println("[" + this.pid
                        + "]: Non posso collegarmi al transducer.");
                return false;
            }
        }
        
        
        //La key deve essere costruita correttamente in questo modo
        final String correct_light_mode = "light_mode_"+this.lamp_id;
        
        if(key.equals(correct_light_mode)) {
        	if (state == 0) {
        		//La lampadina accesa-spenta è semplicemente il valore di una variabile di tipo booleano
        		this.light_state = false;
				setLabel(light_state);
	        } else if (state == 1) {
	        	this.light_state = true;
	        	setLabel(light_state);
	        } else {
	        	System.err.println("[" + this.pid + "]: stato non valido: " + state);
	        	return false;
	        }
			return true;
        } else {
        	System.err.println("[" + this.pid + "]: Proprietà sconosciuta " + key);
            return false;
        }
        
	}

}
