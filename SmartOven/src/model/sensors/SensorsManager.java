package model.sensors;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class SensorsManager {
	private static SensorsManager sensorsManager;
	
	private GpioPinDigitalOutput greenLed;
	private GpioPinDigitalOutput redLed;
	
	private SensorsManager() {
		GpioController controller = GpioFactory.getInstance();
		greenLed = controller.provisionDigitalOutputPin(RaspiPin.GPIO_00, "GreenLed", PinState.LOW);
		redLed = controller.provisionDigitalOutputPin(RaspiPin.GPIO_01, "RedLed", PinState.LOW);
		
		getGreenLed().setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
		getRedLed().setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
	}
	
	public static SensorsManager getInstance() {
		if ( sensorsManager == null )
			sensorsManager = new SensorsManager();
		return (sensorsManager);
	}
	
	private GpioPinDigitalOutput getGreenLed() {
		return (greenLed);
	}

	private GpioPinDigitalOutput getRedLed() {
		return (redLed);
	}

	public void turnOnGreenLed() {
		getGreenLed().high();
	}
	
	public void turnOffGreenLed() {
		getGreenLed().low();
	}
	
	public void turnOnRedLed() {
		getRedLed().high();
	}
	
	public void turnOffRedLed() {
		getRedLed().low();
	}
}
