package it.unibo.homemanager.humanpresence;

import java.util.ArrayList;
import java.util.List;
import com.pi4j.io.gpio.*;

public class LampSwitch implements LampSwitchInterface{	
	//LUCI
  	private GpioController gpio;
    private List<GpioPinDigitalOutput> pins;
    // Ultimo pin assegnato
    private int pin;
    
    public LampSwitch(){
    	// create gpio controller
  		gpio = GpioFactory.getInstance();
  		this.pin=0;

  		GpioPin[] gpiopins = gpio.getProvisionedPins().toArray(new GpioPin[gpio.getProvisionedPins().size()]);
  		for (int i=gpio.getProvisionedPins().size()-1; i>=0; i--){
  			gpio.unprovisionPin(gpiopins[i]);
  		}  		
  		// INIZIALIZZO LUCI
  		pins = new ArrayList<GpioPinDigitalOutput>();
  		
  		pins.add(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04,PinState.HIGH));
 		pins.add(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05,PinState.HIGH));
        pins.add(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07,PinState.HIGH));
        pins.add(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09,PinState.HIGH));
        //pins.add(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11,PinState.HIGH));
        //da mettere al termine dell'applicazione
        //gpio.shutdown();
    }

    @Override
	public void lightOn(int number) {
		pins.get(number-1).low();		
	}
	@Override
	public void lightOff(int number) {
		pins.get(number-1).high();
	}
	@Override
	public void lightSwitch(int number) {
		pins.get(number-1).toggle();
	}
	@Override
	public int getPin() {
		if (pin<4){
			pin++;
			return pin-1;
		}
		else return -1;
	}
}
